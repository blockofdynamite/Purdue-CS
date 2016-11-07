#include <sys/types.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <signal.h>

#include <string>
#include <iostream>
#include <fstream>
#include <thread>
#include <mutex>

using namespace std;

string usage =
"                                                               \n"
"myhttpd:                                                       \n"
"                                                               \n"
"Simple HTTP server                                             \n"
"                                                               \n"
"To use it in one window type:                                  \n"
"                                                               \n"
"   myhttpd [-f|-t|-p] [<port>]                                 \n"
"                                                               \n"
"Where 1024 < port < 65536.   						            \n"
"                                                               \n"
"In another window type:                                        \n"
"                                                               \n"
"   telnet <host> <port>                                        \n"
"                                                               \n"
"where <host> is the name of the machine where daytime-server   \n"
"is running. <port> is the port number you used when you run    \n"
"daytime-server.                                                \n"
"                                                               \n";

string notFound = 
"HTTP/1.1 404 File Not Found\nServer: NotApache\nContent-type: %s\n\n<html><h1>404 File Not Found</h1></html>\n";

string badReq = 
"HTTP/1.1 400 File Not Found\nServer: NotApache\nContent-type: %s\n\n<html><h1>400 Bad Request</h1></html>\n";

string docFound = 
"HTTP/1.1 200 Document follows\nServer: NotApache\nContent-type: %s\n\n";

int queueLength = 5;
int port;
bool toFork;
bool toThread;
bool toPool;

mutex thread_lock;

void zombie(int signal) {
	int i;
	while ((i = waitpid(-1, NULL, WNOHANG)) > 0) {
		// Do nothing
	}
}

void dummy(int signal) { /* Do nothing */ }

int socketAccept(int masterSocket) {
	struct sockaddr_in clientIPAddress;
    int alen = sizeof( clientIPAddress );
    return accept( masterSocket, (struct sockaddr *)&clientIPAddress, (socklen_t*)&alen);
}

void processRequest(int slaveSocket) {

	//Get the request from the server
	char* get = (char*) malloc(sizeof(char) * 4096);
	int n = read(slaveSocket, get, sizeof(char) * 4096);
	string getString(get);
	free(get);

	//Getting path
	bool done = false;
	int len = 0;
	for (int i = 4; i < getString.length(); i++) {
		if (getString[i] == ' ') {
			break;
		}
		len++;
	}
	string path(getString.substr(4,len));

	//Getting path to get on filesystem
	string file;
	if (path.find("icons") != string::npos) {
		file = string(get_current_dir_name()) + "/http-root-dir" + path;
	} else {
		if (!path.compare("/")) {
			file = string(get_current_dir_name()) + "/http-root-dir/htdocs/index.html";
		} else {
			file = string(get_current_dir_name()) + "/http-root-dir/htdocs" + path;
		}
	}

	//Checking if the user is a terrible person
	if (file.find("http-root-dir") == string::npos || file.find("..") != string::npos) {
		write(slaveSocket, notFound.c_str(), notFound.length());
		return;
	}

	//Checking if file is a directory 
	//Throw a 404 if it is since directory browsing isn't implemented
	struct stat st;
	if (!stat(file.c_str(), &st)) {
		if (S_ISDIR(st.st_mode)) {
			write(slaveSocket, notFound.c_str(), notFound.length());
			return;
		}
	}

	//Open file and check if null
	FILE* f = fopen(file.c_str(), "r");
	if (f == NULL) {
		write(slaveSocket, notFound.c_str(), notFound.length());
		return;
	}

	//Get file descriptor
	int fd = fileno(f);

	//Write that we found a doc
	write(slaveSocket, docFound.c_str(), docFound.length());

	//Write to the socket
	char c;
	int c2;
	while (c2 = read(fd, &c, sizeof(char))) {
		write(slaveSocket, &c, sizeof(c));
	}

	write(slaveSocket, "\n\n", 2);
}

void threadRunner(int slaveSocket) {
	try {
	    processRequest( slaveSocket );
	} catch (...) {
		write(slaveSocket, badReq.c_str(), badReq.length());
	}
	
	//Close socker
	close(slaveSocket);
}

void poolRunner(int masterSocket) {
	while (true) {

		thread_lock.lock();

		int slaveSocket = socketAccept(masterSocket);

		thread_lock.unlock();

		try {
	    	processRequest( slaveSocket );
	    } catch (...) {
	    	write(slaveSocket, badReq.c_str(), badReq.length());
		}

		close(slaveSocket);
	}
}

int main( int argc, char **argv) {
	if (argc > 3) {
		cout << usage << endl;
		return -1;
	}

	//Set up listener for dead processes and dead sockets
	signal(SIGCHLD, zombie);
	signal(SIGPIPE, dummy);

	//Parse arguments
	for (int i = 0; i < argc; i++) {
		if (!strcmp("-f", argv[i])) {
			toFork = true;
		} else if (!strcmp("-t", argv[i])) {
			toThread = true;
		} else if (!strcmp("-p", argv[i])) {
			toPool = true;
		} else if (i == (argc - 1)) {
			port = atoi(argv[i]);
			if (port > 65536 || port < 1024 && port != 0) {
				cout << usage << endl;
				return -1;
			}
		}
	}

	//if the user didn't specify a port
	if (port == 0) {
		port = 2024;
	}

	// Set the IP address and port for this server
    struct sockaddr_in serverIPAddress; 
    memset( &serverIPAddress, 0, sizeof(serverIPAddress) );
  	serverIPAddress.sin_family = AF_INET;
  	serverIPAddress.sin_addr.s_addr = INADDR_ANY;
  	serverIPAddress.sin_port = htons((u_short) port);
  
  	// Allocate a socket
  	int masterSocket =  socket(PF_INET, SOCK_STREAM, 0);
  	if ( masterSocket < 0) {
    	perror("socket");
    	exit( -1 );
  	}

  	// Set socket options to reuse port. Otherwise we will
  	// have to wait about 2 minutes before reusing the sae port number
  	int optval = 1; 
  	int err = setsockopt(masterSocket, SOL_SOCKET, SO_REUSEADDR,(char *) &optval, sizeof( int ) );
   
  	// Bind the socket to the IP address and port
  	int error = bind( masterSocket,(struct sockaddr *)&serverIPAddress, sizeof(serverIPAddress) );
  	if ( error ) {
    	perror("bind");
    	exit( -1 );
  	}
  
  	// Put socket in listening mode and set the 
  	// size of the queue of unprocessed connections
  	error = listen( masterSocket, queueLength);
  	if ( error ) {
    	perror("listen");
    	exit( -1 );
  	}

  	//Pool threads
  	if (toPool) {
  		thread thread1(poolRunner, masterSocket);
  		thread thread2(poolRunner, masterSocket);
  		thread thread3(poolRunner, masterSocket);
  		thread thread4(poolRunner, masterSocket);
  		thread thread5(poolRunner, masterSocket);

  		thread1.join();
  	} 

  	else {
  		while (true) {
	    	// Accept incoming connections
	    	int slaveSocket = socketAccept(masterSocket);

	    	// Socket broken
	    	if ( slaveSocket < 0 ) {
	      		perror( "accept" );
	      		continue;
	    	}

	   		if (toFork) {
	   			//New Process
	   			if (fork() == 0) {
	   				try {
	    				processRequest( slaveSocket );
	    			} catch (...) {
	    				write(slaveSocket, badReq.c_str(), badReq.length());
		    		}

	   				close(slaveSocket);
					exit(0);
	   			}
	   			close(slaveSocket);
	   		} 

	   		else if (toThread) {
	   			//New Thread
	   			//Uses C++'s "new" std::thread
	   			thread toRun(threadRunner, slaveSocket);
	   			toRun.detach();
	   		} 

	   		else {
	   			//Single threaded
	   			//Process request.
	    		try {
	    			processRequest( slaveSocket );
	    		} catch (...) {
	    			write(slaveSocket, badReq.c_str(), badReq.length());
		    	}

		    	//Close socker
	   			close(slaveSocket);
	   		}
	  	}
  	}
}