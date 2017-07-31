const char *usage =
        "                                                               \n"
                "IRCServer:                                                   \n"
                "                                                               \n"
                "Simple server program used to communicate multiple users       \n"
                "                                                               \n"
                "To use it in one window type:                                  \n"
                "                                                               \n"
                "   IRCServer <port>                                          \n"
                "                                                               \n"
                "Where 1024 < port < 65536.                                     \n"
                "                                                               \n"
                "In another window type:                                        \n"
                "                                                               \n"
                "   telnet <host> <port>                                        \n"
                "                                                               \n"
                "where <host> is the name of the machine where talk-server      \n"
                "is running. <port> is the port number you used when you run    \n"
                "daytime-server.                                                \n"
                "                                                               \n";

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <time.h>
#include <list>
#include <string>
#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <algorithm>

#include "IRCServer.h"

//using namespace std;

int QueueLength = 5;

Server *server = new Server();

int
IRCServer::open_server_socket(int port) {

    // Set the IP address and port for this server
    struct sockaddr_in serverIPAddress;
    memset(&serverIPAddress, 0, sizeof(serverIPAddress));
    serverIPAddress.sin_family = AF_INET;
    serverIPAddress.sin_addr.s_addr = INADDR_ANY;
    serverIPAddress.sin_port = htons((u_short) port);

    // Allocate a socket
    int masterSocket = socket(PF_INET, SOCK_STREAM, 0);
    if (masterSocket < 0) {
        perror("socket");
        exit(-1);
    }

    // Set socket options to reuse port. Otherwise we will
    // have to wait about 2 minutes before reusing the sae port number
    int optval = 1;
    int err = setsockopt(masterSocket, SOL_SOCKET, SO_REUSEADDR,
                         (char *) &optval, sizeof(int));

    // Bind the socket to the IP address and port
    int error = bind(masterSocket,
                     (struct sockaddr *) &serverIPAddress,
                     sizeof(serverIPAddress));
    if (error) {
        perror("bind");
        exit(-1);
    }

    // Put socket in listening mode and set the
    // size of the queue of unprocessed connections
    error = listen(masterSocket, QueueLength);
    if (error) {
        perror("listen");
        exit(-1);
    }

    return masterSocket;
}

void
IRCServer::runServer(int port) {
    int masterSocket = open_server_socket(port);

    initialize();

    while (1) {

        // Accept incoming connections
        struct sockaddr_in clientIPAddress;
        int alen = sizeof(clientIPAddress);
        int slaveSocket = accept(masterSocket,
                                 (struct sockaddr *) &clientIPAddress,
                                 (socklen_t *) &alen);

        if (slaveSocket < 0) {
            perror("accept");
            exit(-1);
        }

        // Process request.
        processRequest(slaveSocket);
    }
}

int
main(int argc, char **argv) {
    // Print usage if not enough arguments
    if (argc < 2) {
        fprintf(stderr, "%s", usage);
        exit(-1);
    }

    // Get the port from the arguments
    int port = atoi(argv[1]);

    IRCServer ircServer;

    // It will never return
    ircServer.runServer(port);

}

//
// Commands:
//   Commands are started y the client.
//
//   Request: ADD-USER <USER> <PASSWD>\r\n
//   Answer: OK\r\n or DENIED\r\n
//
//   REQUEST: GET-ALL-USERS <USER> <PASSWD>\r\n
//   Answer: USER1\r\n
//            USER2\r\n
//            ...
//            \r\n
//
//   REQUEST: CREATE-ROOM <USER> <PASSWD> <ROOM>\r\n
//   Answer: OK\n or DENIED\r\n
//
//   Request: LIST-ROOMS <USER> <PASSWD>\r\n
//   Answer: room1\r\n
//           room2\r\n
//           ...
//           \r\n
//
//   Request: ENTER-ROOM <USER> <PASSWD> <ROOM>\r\n
//   Answer: OK\n or DENIED\r\n
//
//   Request: LEAVE-ROOM <USER> <PASSWD>\r\n
//   Answer: OK\n or DENIED\r\n
//
//   Request: SEND-MESSAGE <USER> <PASSWD> <MESSAGE> <ROOM>\n
//   Answer: OK\n or DENIED\n
//
//   Request: GET-MESSAGES <USER> <PASSWD> <LAST-MESSAGE-NUM> <ROOM>\r\n
//   Answer: MSGNUM1 USER1 MESSAGE1\r\n
//           MSGNUM2 USER2 MESSAGE2\r\n
//           MSGNUM3 USER2 MESSAGE2\r\n
//           ...\r\n
//           \r\n
//
//    REQUEST: GET-USERS-IN-ROOM <USER> <PASSWD> <ROOM>\r\n
//    Answer: USER1\r\n
//            USER2\r\n
//            ...
//            \r\n
//

void
IRCServer::processRequest(int fd) {
    // Buffer used to store the comand received from the client
    const int MaxCommandLine = 1024;
    char commandLine[MaxCommandLine + 1];
    int commandLineLength = 0;
    int n;

    // Currently character read
    unsigned char prevChar = 0;
    unsigned char newChar = 0;

    //
    // The client should send COMMAND-LINE\n
    // Read the name of the client character by character until a
    // \n is found.
    //

    // Read character by character until a \n is found or the command string is full.
    while (commandLineLength < MaxCommandLine &&
           read(fd, &newChar, 1) > 0) {

        if (newChar == '\n' && prevChar == '\r') {
            break;
        }

        commandLine[commandLineLength] = newChar;
        commandLineLength++;

        prevChar = newChar;
    }

    // Add null character at the end of the string
    // Eliminate last \r
    commandLineLength--;
    commandLine[commandLineLength] = 0;

    //printf("RECEIVED: %s\n", commandLine);

    char *command = strdup(strtok(commandLine, " "));
    char *user = strdup(strtok(NULL, " "));
    char *password = strdup(strtok(NULL, " "));
    char *args;
    char *check = strtok(NULL, "\n");
    if (check != NULL) {
        args = strdup(check);
    }
    //delete(check);
    //check = NULL;

    /*if (args == NULL) {
        args = (char *) "";
    }*/

    if (!strcmp(command, "ADD-USER")) {
        addUser(fd, user, password, args);
    }
    else if (!strcmp(command, "ENTER-ROOM")) {
        enterRoom(fd, user, password, args);
    }
    else if (!strcmp(command, "LEAVE-ROOM")) {
        leaveRoom(fd, user, password, args);
    }
    else if (!strcmp(command, "SEND-MESSAGE")) {
        sendMessage(fd, user, password, args);
    }
    else if (!strcmp(command, "GET-MESSAGES")) {
        getMessages(fd, user, password, args);
    }
    else if (!strcmp(command, "GET-USERS-IN-ROOM")) {
        getUsersInRoom(fd, user, password, args);
    }
    else if (!strcmp(command, "GET-ALL-USERS")) {
        getAllUsers(fd, user, password, args);
    }
    else if (!strcmp(command, "CREATE-ROOM")) {
        createRoom(fd, user, password, args);
    }
    else if (!strcmp(command, "LIST-ROOMS")) {
        listRooms(fd, user, password, args);
    }
    else {
        const char *msg = "UNKNOWN COMMAND\r\n";
        write(fd, msg, strlen(msg));
    }

    // Send OK answer
    //const char * msg =  "OK\n";
    //write(fd, msg, strlen(msg));
    command = NULL;
    user = NULL;
    password = NULL;
    args = NULL;
    close(fd);
}

//HELPER METHOD
User
getUser(const char *user, const char *password) {
    int i = 0;
    while (i < server->globalUsers.size()) {
        //printf("here: %s %s\n", server->globalUsers.at(i).name, server->globalUsers.at(i).password);
        if (strcmp(server->globalUsers.at(i).name, user) == 0) {
            return server->globalUsers.at(i);
        }
        i++;
    }
}

void
IRCServer::initialize() {
    std::ifstream passwords;
    passwords.open(PASSWORD_FILE);

    std::string username;
    std::string password;

    printf("Initializing user list from file password.txt\r\n");

    while (passwords >> username) {
        passwords >> password;
        addUser(0, username.data(), password.data(), NULL);
    }

    passwords.close();

}

bool
IRCServer::checkPassword(int fd, const char *user, const char *password) {
    int i = 0;
    while (i < server->globalUsers.size()) {
        if (strcmp(server->globalUsers.at(i).name, user) == 0) {
            return strcmp(server->globalUsers.at(i).password, password) == 0;
        }
        i++;
    }
    return false;
}

bool
IRCServer::checkUser(int fd, const char *user, const char *password) {
    int i = 0;
    while (i < server->globalUsers.size()) {
        if (strcmp(server->globalUsers.at(i).name, user) == 0) {
            return true;
        }
        i++;
    }
    return false;
}

void
IRCServer::addUser(int fd, const char *user, const char *password, const char *args) {
    User newUser;

    newUser.name = (char*) user;
    newUser.password = (char*) password;

    //printf("%s %s\n", newUser.name, newUser.password);

    int i = 0;

    const char *msg1 = "DENIED\r\n";
    const char *msg = "OK\r\n";

    if (fd != 0) {
        while (i < server->globalUsers.size()) {
            if (!strcmp(server->globalUsers.at(i).name, user)) {
            	if (checkPassword(fd, user, password)) {
            		write(fd, "Logged in\r\n", strlen("Logged in\r\n"));
            		return;
            	}
                write(fd, msg1, strlen(msg1));
                return;
            }
            i++;
        }
    }

    if (fd == 0) {
        User tempUser;
        tempUser.name = strdup(user);
        tempUser.password = strdup(password);
        std::cout << tempUser.name << " ";
        std::cout << tempUser.password << std::endl;
        server->globalUsers.push_back(tempUser);
        printf("Ok\n");
        return;
    }

    printf("%s %s\n", newUser.name, newUser.password);

    server->globalUsers.push_back(newUser);

    write(fd, msg, strlen(msg));

    FILE *passwords = fopen(PASSWORD_FILE, "a+");

    fprintf(passwords, "%s %s\n", newUser.name, newUser.password);
    fclose(passwords);

    return;
}

void
IRCServer::createRoom(int fd, const char *user, const char *password, const char *args) {
    if (!checkUser(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }

    if (!checkPassword(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }

    if (args==NULL) {
    	write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
    	return;
    }

    int i = 0;
    while (i < server->Channels.size()) {
        if (!strcmp(server->Channels.at(i).channelName, args)) {
            write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
            return;
        }
        i++;
    }

    Channel channel;
    channel.channelName = strdup(args);

    //channel.usersInChannel.push_back(getUser(user, password));

    server->Channels.push_back(channel);

    const char *msg = "OK\r\n";
    write(fd, msg, strlen(msg));
    return;
}

void
IRCServer::listRooms(int fd, const char *user, const char *password, const char *args) {
    if (!checkUser(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (!checkPassword(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    int i = 0;
    while (i < server->Channels.size()) {
        write(fd, server->Channels.at(i).channelName, strlen(server->Channels.at(i).channelName));
        write(fd, "\r\n", strlen("\r\n"));
        //printf("%d %s\n", i, server->Channels.at(i).channelName);
        i++;
    }
}

void
IRCServer::enterRoom(int fd, const char *user, const char *password, const char *args) {
    if (!checkUser(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (!checkPassword(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }

    if (args==NULL) {
    	write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
    	return;
    }

    int i = 0;
    while (i < server->Channels.size()) {
        if (!strcmp(server->Channels.at(i).channelName, args)) {
            for (int j = 0; j < server->Channels.at(i).usersInChannel.size(); j++) {
                //write(fd, server->Channels.at(i).usersInChannel.at(j).name, strlen(server->Channels.at(i).usersInChannel.at(j).name));
                //std::cout << server->Channels.at(i).usersInChannel.at(j).name << std::endl;
                if(!strcmp(server->Channels.at(i).usersInChannel.at(j).name, user)) {
                    write(fd, "OK\r\n", strlen("OK\r\n"));
                    return;
                }
            }
            server->Channels.at(i).usersInChannel.push_back(getUser(user, password));
            write(fd, "OK\r\n", strlen("OK\r\n"));
            return;
        }
        i++;
    }
    write(fd, "ERROR (No room)\r\n", strlen("ERROR (No room)\r\n"));
    return;
}

void
IRCServer::leaveRoom(int fd, const char *user, const char *password, const char *args) {
    if (!checkUser(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (!checkPassword(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (args==NULL) {
    	write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
    	return;
    }
    int i = 0;
    while (i < server->Channels.size()) {
        //printf("Yo\n");
        if (!strcmp(server->Channels.at(i).channelName, args)) {
            for (int j = 0; j < server->Channels.at(i).usersInChannel.size(); j++) {
                //printf("Yo\n");
                if(!strcmp(server->Channels.at(i).usersInChannel.at(j).name, user)) {
                    server->Channels.at(i).usersInChannel.erase(server->Channels.at(i).usersInChannel.begin() + j - 1);
                    write(fd, "OK\r\n", strlen("OK\r\n"));
                    return;
                }
            }
        }
        i++;
    }
    write(fd, "ERROR (No user in room)\r\n", strlen("ERROR (No user in room)\r\n"));
}

void
IRCServer::sendMessage(int fd, const char *user, const char *password, const char *args) {
    if (!checkUser(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (!checkPassword(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (args==NULL) {
    	write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
    	return;
    }
    char* room = strdup(strtok((char*) args, " "));
    char* messageString = strtok(NULL, "\0");
    int i = 0;
    while (i < server->Channels.size()) {
        if (!strcmp(server->Channels.at(i).channelName, room)) {
            for (int j = 0; j < server->Channels.at(i).usersInChannel.size(); j++) {
                if (!strcmp(server->Channels.at(i).usersInChannel.at(j).name, user)) {
                    if (messageString == NULL) {
                        break;
                    }
                    Message message;
                    message.user = (char*) user;
                    message.message = messageString;
                    server->Channels.at(i).messages.push_back(message);
                    const char *msg = "OK\r\n";
                    write(fd, msg, strlen(msg));
                    return;
                }
            }
        }
        i++;
    }
    write(fd, "ERROR (user not in room)\r\n", strlen("ERROR (user not in room)\r\n"));
}

void
IRCServer::getMessages(int fd, const char *user, const char *password, const char *args) {
    if (!checkUser(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (!checkPassword(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (args==NULL) {
    	write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
    	return;
    }
    int num = atoi(strtok((char*) args, " "));
    char* room = NULL;

    char* test = strtok(NULL, "\0");

    if (test == NULL) {
        write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
        return;
    } else {
        room = strdup(test);
    }

    for (int i = 0; i < server->Channels.size(); i++) {
        if (!strcmp(server->Channels.at(i).channelName, room)) {
            int j;
            for (j = 0; j < server->Channels.at(i).usersInChannel.size(); j++) {
                if (!strcmp(server->Channels.at(i).usersInChannel.at(j).name, user)) {
                    break;
                }
            }
            if (j == server->Channels.at(i).usersInChannel.size()) {
                write(fd, "ERROR (User not in room)\r\n", strlen("ERROR (User not in room)\r\n"));
                return;
            }
            if ((num) >= server->Channels.at(i).messages.size() - 1 || num > 100) {
                write(fd, "NO-NEW-MESSAGES\r\n", strlen("NO-NEW-MESSAGES\r\n"));
                return;
            }
            for (int j = num; j < server->Channels.at(i).messages.size(); j++) {
                char* message = server->Channels.at(i).messages.at(j).message;
                char* name = server->Channels.at(i).messages.at(j).user;
                std::string msgNum;
                std::string toAppend = "";
                msgNum.assign(toAppend);

                std::ostringstream convert;

                convert << j;
                msgNum.append(convert.str());
                write(fd, msgNum.data(), strlen(msgNum.data()));
                write(fd, " ", strlen(" "));
                write(fd, name, strlen(name));
                write(fd, " ", strlen(" "));
                write(fd, message, strlen(message));
                write(fd, "\r\n", strlen("\r\n"));
            }
            write(fd, "\r\n", strlen("\r\n"));
            return;
        }
    }
    write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
}

void
IRCServer::getUsersInRoom(int fd, const char *user, const char *password, const char *args) {
    if (!checkUser(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (!checkPassword(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (args==NULL) {
    	write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
    	return;
    }
    for (int i = 0; i < server->Channels.size(); i++) {
        if (!strcmp(server->Channels.at(i).channelName, args)) {
            std::sort(server->Channels.at(i).usersInChannel.begin(), server->Channels.at(i).usersInChannel.end(), CompareByMember<User,char*>(&User::name));
            for (int j = 0; j < server->Channels.at(i).usersInChannel.size(); j++) {
                char* username = (char*) server->Channels.at(i).usersInChannel.at(j).name;
                //std::cout << username << std::endl;
                write(fd, username, strlen(username));
                write(fd, "\r\n", strlen("\r\n"));
            }
            write(fd, "\r\n", strlen("\r\n"));
            return;
        }
    }
    write(fd, "DENIED\r\n", strlen("DENIED\r\n"));
}

void
IRCServer::getAllUsers(int fd, const char *user, const char *password, const char *args) {
    if (!checkUser(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }
    if (!checkPassword(fd, user, password)) {
        write(fd, "ERROR (Wrong password)\r\n", strlen("ERROR (Wrong password)\r\n"));
        return;
    }

    int i = 0;
    std::sort(server->globalUsers.begin(), server->globalUsers.end(), CompareByMember<User,char*>(&User::name));
    while (i < server->globalUsers.size()) {
        write(fd, server->globalUsers.at(i).name, strlen(server->globalUsers.at(i).name));
        write(fd, "\r\n", strlen("\r\n"));
        i++;
    }
    write(fd, "\r\n", strlen("\r\n"));
}

