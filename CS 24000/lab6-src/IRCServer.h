#include <bits/stl_bvector.h>

#ifndef IRC_SERVER
#define IRC_SERVER

#define PASSWORD_FILE "password.txt"

class IRCServer {
	//FILE* fd = fopen(PASSWORD_FILE, "a+");

private:
	int open_server_socket(int port);

public:
	void initialize();
	bool checkPassword(int fd, const char * user, const char * password);
	bool checkUser(int fd, const char * user, const char * password);
	void processRequest( int socket );
	void addUser(int fd, const char * user, const char * password, const char * args);
	void enterRoom(int fd, const char * user, const char * password, const char * args);
	void leaveRoom(int fd, const char * user, const char * password, const char * args);
	void sendMessage(int fd, const char * user, const char * password, const char * args);
	void getMessages(int fd, const char * user, const char * password, const char * args);
	void getUsersInRoom(int fd, const char * user, const char * password, const char * args);
	void getAllUsers(int fd, const char * user, const char * password, const char * args);
	void runServer(int port);
	void createRoom(int fd, const char * user, const char * password, const char * args);
	void listRooms(int fd, const char * user, const char * password, const char * args);
};

#endif

class User {
public:
	char* name;
	char* password;
};

template <typename T, typename U>
struct CompareByMember {
	// This is a pointer-to-member, it represents a member of class T
	// The data member has type U
	U T::*field;
	CompareByMember(U T::*f) : field(f) {}
	bool operator()(const T &lhs, const T &rhs) {
		return strcmp(lhs.*field, rhs.*field) < 0;
	}
};

class Message {
public:
	char* message;
	char* user;
};

class Channel {
public:
	char* channelName;
	std::vector<User> usersInChannel;
	std::vector<Message> messages;
};

class Server {
public:
	std::vector<Channel> Channels;
	std::vector<User> globalUsers;
};

