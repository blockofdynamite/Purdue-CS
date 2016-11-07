#include <stdio.h>
#include <gtk/gtk.h>
#include <time.h>
//#include <curses.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <iostream>
#include <cstdio>
#include <ctime>
#include <typeinfo>

#define MAX_RESPONSE (10 * 1024)

GtkListStore *list_rooms;
GtkListStore *usersInRoom;

GtkWidget *tree_view;
GtkTreeSelection *selection;

GtkTextBuffer *buffer;

std::string user;
std::string password;
std::string host;
int port;
std::string room("");

GtkWidget *window;
GtkWidget *roomList;
GtkWidget *list;
GtkWidget *myMessage;
GtkWidget *messages;

int open_client_socket(char *host, int port) {
    // Initialize socket address structure
    struct sockaddr_in socketAddress;

    // Clear sockaddr structure
    memset((char *) &socketAddress, 0, sizeof(socketAddress));

    // Set family to Internet
    socketAddress.sin_family = AF_INET;

    // Set port
    socketAddress.sin_port = htons((u_short) port);

    // Get host table entry for this host
    struct hostent *ptrh = gethostbyname(host);
    if (ptrh == NULL) {
        perror("gethostbyname");
        exit(1);
    }

    // Copy the host ip address to socket address structure
    memcpy(&socketAddress.sin_addr, ptrh->h_addr, ptrh->h_length);

    // Get TCP transport protocol entry
    struct protoent *ptrp = getprotobyname("tcp");
    if (ptrp == NULL) {
        perror("getprotobyname");
        exit(1);
    }

    // Create a tcp socket
    int sock = socket(PF_INET, SOCK_STREAM, ptrp->p_proto);
    if (sock < 0) {
        perror("socket");
        exit(1);
    }

    // Connect the socket to the specified server
    if (connect(sock, (struct sockaddr *) &socketAddress,
                sizeof(socketAddress)) < 0) {
        perror("connect");
        exit(1);
    }

    return sock;
}

int sendCommand(char *host, int port, char *command, char *response) {
    int sock = open_client_socket(host, port);

    if (sock < 0) {
        return 0;
    }
    response[0] = 0;
    // Send command
    write(sock, command, strlen(command));
    write(sock, "\r\n", 2);

    //Print copy to stdout
    write(1, command, strlen(command));
    write(1, "\r\n", 2);

    // Keep reading until connection is closed or MAX_REPONSE
    int n = 0;
    int len = 0;
    while ((n = read(sock, response + len, MAX_RESPONSE - len)) > 0) {
        //printf("%c %d\n", n, n);
        len += n;
    }
    response[len] = 0;

    printf("response: %s\n", response);

    close(sock);

    return 1;
}

int numRooms = 0;
void update_list_rooms() {
    GtkTreeIter iter2;
    int i;

    std::string command(std::string("LIST-ROOMS") + " " + user + " " + password);

    char respond[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command.data(), respond);
    int it = 0;
    char *room = strtok(respond, "\r\n");
    if (0 == numRooms) {
        gtk_list_store_append(GTK_LIST_STORE(list_rooms), &iter2);
        gtk_list_store_set(GTK_LIST_STORE(list_rooms), &iter2, 0, room, -1);
        it++;
        numRooms = 1;
    }
    if (numRooms > 0) {
        it++;
    }
    while ((room = strtok(NULL, "\r\n")) != NULL) {
        if (numRooms <= it++) {
            //printf("In here!\n");
            gtk_list_store_append(GTK_LIST_STORE(list_rooms), &iter2);
            gtk_list_store_set(GTK_LIST_STORE(list_rooms), &iter2, 0, room, -1);
            numRooms++;
        }
    }
}

int inRoom;
void getUsersInRoom() {
    if (!strcmp(room.data(), "") || room.data() == NULL) {
        return;
    }

    gtk_list_store_clear(usersInRoom);

    GtkTreeIter iter;
    int i;

    char respond[MAX_RESPONSE];
    std::string command = std::string("GET-USERS-IN-ROOM") + " " + user + " " + password + " " + room.data();
    sendCommand((char *) host.data(), port, (char *) command.data(), respond);
    //char* response = strdup(respond);
    if (!strcmp("DENIED\r\n", respond)) {
        return;
    }
    int it = 0;
    char *userNameToAdd = strtok(respond, "\r\n");
    gtk_list_store_append(GTK_LIST_STORE(usersInRoom), &iter);
    gtk_list_store_set(GTK_LIST_STORE(usersInRoom), &iter, 0, userNameToAdd, -1);
    while ((userNameToAdd = strtok(NULL, "\r\n")) != NULL) {
        gtk_list_store_append(GTK_LIST_STORE(usersInRoom), &iter);
        gtk_list_store_set(GTK_LIST_STORE(usersInRoom), &iter, 0, userNameToAdd, -1);
    }
}

void getRoomSelection(GtkWidget *selection, gpointer text) {

    std::string command = (std::string("SEND-MESSAGE ") + user + " " + password + " " + room.data() + " left room.");
    char respond[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command.data(), respond);

    std::string command1(std::string("LEAVE-ROOM ") + user + " " + password + " " + room.data());
    char response[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command1.data(), response);

    GtkListStore *store;
    GtkTreeModel *model;
    GtkTreeIter iter;

    int LIST_ITEM = 0;

    gtk_tree_selection_get_selected(GTK_TREE_SELECTION(selection), &model, &iter);
    gtk_tree_model_get(model, &iter, LIST_ITEM, &room, -1);

    if (room.data() == NULL) {
        return;
    }

    //sleep(100); //wait 2 seconds for the server to process the request :D

    std::string command2(std::string("ENTER-ROOM ") + user + " " + password + " " + room.data());
    char respon[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command2.data(), respon);

    command = (std::string("SEND-MESSAGE ") + user + " " + password + " " + room.data() + " entered room.");
    char responsed[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command.data(), responsed);

    inRoom = 0;

    getUsersInRoom();
    update_list_rooms();
}

/* Create the list of "messages" */
static GtkWidget *create_list(const char *titleColumn, GtkListStore *model) {
    GtkWidget *scrolled_window;
    //GtkListStore *model;
    GtkCellRenderer *cell;
    GtkTreeViewColumn *column;
    GtkTreeSelection *selection;


    int i;

    /* Create a new scrolled window, with scrollbars only if needed */
    scrolled_window = gtk_scrolled_window_new(NULL, NULL);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scrolled_window),
                                   GTK_POLICY_AUTOMATIC,
                                   GTK_POLICY_AUTOMATIC);

    //model = gtk_list_store_new (1, G_TYPE_STRING);
    tree_view = gtk_tree_view_new();
    gtk_container_add(GTK_CONTAINER(scrolled_window), tree_view);
    gtk_tree_view_set_model(GTK_TREE_VIEW(tree_view), GTK_TREE_MODEL(model));
    gtk_widget_show(tree_view);

    cell = gtk_cell_renderer_text_new();

    selection = gtk_tree_view_get_selection(GTK_TREE_VIEW(tree_view));

    g_signal_connect(selection, "changed", G_CALLBACK(getRoomSelection), selection);

    column = gtk_tree_view_column_new_with_attributes(titleColumn, cell, "text", 0, NULL);

    gtk_tree_view_append_column(GTK_TREE_VIEW(tree_view),
                                GTK_TREE_VIEW_COLUMN(column));

    return scrolled_window;
}

void getUser(GtkWidget *selection, gpointer text) {
    GtkListStore *store;
    GtkTreeModel *model;
    GtkTreeIter iter;

    int LIST_ITEM = 0;

    gtk_tree_selection_unselect_all(GTK_TREE_SELECTION(selection));
}

static GtkWidget *create_list_users(const char *titleColumn, GtkListStore *model) {
    GtkWidget *scrolled_window;
    //GtkListStore *model;
    GtkCellRenderer *cell;
    GtkTreeViewColumn *column;
    GtkTreeSelection *selection;


    int i;

    /* Create a new scrolled window, with scrollbars only if needed */
    scrolled_window = gtk_scrolled_window_new(NULL, NULL);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scrolled_window),
                                   GTK_POLICY_AUTOMATIC,
                                   GTK_POLICY_AUTOMATIC);

    //model = gtk_list_store_new (1, G_TYPE_STRING);
    tree_view = gtk_tree_view_new();
    gtk_container_add(GTK_CONTAINER(scrolled_window), tree_view);
    gtk_tree_view_set_model(GTK_TREE_VIEW(tree_view), GTK_TREE_MODEL(model));
    gtk_widget_show(tree_view);

    cell = gtk_cell_renderer_text_new();

    selection = gtk_tree_view_get_selection(GTK_TREE_VIEW(tree_view));

    g_signal_connect(selection, "changed", G_CALLBACK(getUser), selection);

    column = gtk_tree_view_column_new_with_attributes(titleColumn, cell, "text", 0, NULL);

    gtk_tree_view_append_column(GTK_TREE_VIEW(tree_view),
                                GTK_TREE_VIEW_COLUMN(column));

    return scrolled_window;
}

/* Add some text to our text widget - this is a callback that is invoked
when our window is realized. We could also force our window to be
realized with gtk_widget_realize, but it would have to be part of
a hierarchy first */

static void insert_text(GtkTextBuffer *buffer, const char *initialText) {

    GtkTextIter iterEnd;
    GtkTextIter begin;
    GtkTextIter iter;

    gtk_text_buffer_get_iter_at_offset(buffer, &iterEnd, 0);
    gtk_text_iter_forward_to_end(&iterEnd);

    gtk_text_buffer_get_iter_at_offset(buffer, &begin, 0);

    gtk_text_buffer_delete(buffer, &begin, &iterEnd);

    gtk_text_buffer_get_iter_at_offset(buffer, &iter, 0);

    gtk_text_buffer_insert(buffer, &iter, initialText, -1);


}

/* Create a scrolled text area that displays a "message" */
static GtkWidget *create_text(const char *initialText) {
    GtkWidget *scrolled_window;
    GtkWidget *view;

    view = gtk_text_view_new();
    buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(view));

    scrolled_window = gtk_scrolled_window_new(NULL, NULL);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scrolled_window),
                                   GTK_POLICY_AUTOMATIC,
                                   GTK_POLICY_AUTOMATIC);

    gtk_container_add(GTK_CONTAINER(scrolled_window), view);
    insert_text(buffer, initialText);

    gtk_widget_show_all(scrolled_window);

    return scrolled_window;
}

int i = 0;
void populateMessages() {
    char toReceive[MAX_RESPONSE];
    std::string toSend(std::string("GET-MESSAGES ") + user.data() + " " + password.data() + " 0 " + room.data());
    sendCommand((char *) host.data(), port, (char *) toSend.data(), toReceive);
    if (!strcmp(toReceive, "DENIED\r\n")) {
        return;
    }
    if (i++ != 0) {
        insert_text(buffer, toReceive);
    }
}

void sendMessage(GtkWidget * widget, GtkWidget * entry) {
    std::string toSend(gtk_entry_get_text(GTK_ENTRY(entry)));
    if (!strcmp(toSend.data(), "") || toSend.data() == NULL) {
        return;
    }
    std::string command("SEND-MESSAGE " + user + " " + password + " " + room.data() + " " + toSend.data());
    char response[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command.data(), response);
    gtk_entry_set_text(GTK_ENTRY(entry), "");
    //populateMessages();
}

void enterRoom(GtkWidget * widget, GtkWidget * entry) {
    if (room.data() == NULL) {
        return;
    }
    std::string command(std::string("ENTER-ROOM ") + user + " " + password + " " + room.data());
    char response[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command.data(), response);
    if (!strcmp("DENIED\r\n", response)) {
        return;
    }
    command = (std::string("SEND-MESSAGE ") + user + " " + password + " " + room.data() + " entered room.");
    char respond[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command.data(), respond);
    getUsersInRoom();
    populateMessages();
}

static void enter_callback(GtkWidget * widget, GtkWidget * entry) {
    const gchar *entry_text;
    entry_text = gtk_entry_get_text(GTK_ENTRY(widget));
    printf("Room name: %s\n", entry_text);
    std::string command("CREATE-ROOM " + user + " " + password + " " + entry_text + "\r\n");
    char response[MAX_RESPONSE];
    sendCommand((char *) host.data(), port, (char *) command.data(), response);
    gtk_widget_destroy(gtk_widget_get_toplevel(entry));
}

static void entry_toggle_editable(GtkWidget * checkbutton, GtkWidget * entry) {
    gtk_editable_set_editable(GTK_EDITABLE(entry),
                              GTK_TOGGLE_BUTTON(checkbutton)->active);
}

static void entry_toggle_visibility(GtkWidget * checkbutton, GtkWidget * entry) {
    gtk_entry_set_visibility(GTK_ENTRY(entry),
                             GTK_TOGGLE_BUTTON(checkbutton)->active);
}

void createRoom() {
    GtkWidget *window;
    GtkWidget *vbox, *hbox;
    GtkWidget *entry;
    GtkWidget *button;
    GtkWidget *button2;
    gint tmp_pos;

    gtk_init(NULL, NULL);

    /* create a new window */
    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_widget_set_size_request(GTK_WIDGET(window), 500, 100);
    gtk_window_set_title(GTK_WINDOW(window), "Create Room");
    g_signal_connect(window, "destroy",
                     G_CALLBACK(gtk_main_quit), NULL);
    g_signal_connect_swapped(window, "delete-event",
                             G_CALLBACK(gtk_widget_destroy),
                             window);

    vbox = gtk_vbox_new(FALSE, 0);
    gtk_container_add(GTK_CONTAINER(window), vbox);
    gtk_widget_show(vbox);

    entry = gtk_entry_new();
    gtk_entry_set_max_length(GTK_ENTRY(entry), 50);
    g_signal_connect(entry, "activate",
                     G_CALLBACK(enter_callback),
                     entry);
    gtk_entry_set_text(GTK_ENTRY(entry), "");
    tmp_pos = GTK_ENTRY(entry)->text_length;
    //gtk_editable_insert_text (GTK_EDITABLE (entry), " world", -1, &tmp_pos);
    //gtk_editable_select_region (GTK_EDITABLE (entry), 0, GTK_ENTRY (entry)->text_length);
    gtk_box_pack_start(GTK_BOX(vbox), entry, TRUE, TRUE, 0);
    gtk_widget_show(entry);

    hbox = gtk_hbox_new(FALSE, 0);
    gtk_container_add(GTK_CONTAINER(vbox), hbox);
    gtk_widget_show(hbox);

    button2 = gtk_button_new_from_stock(GTK_STOCK_OK);
    g_signal_connect_swapped(button2, "clicked",
                             G_CALLBACK(enter_callback),
                             entry);
    gtk_box_pack_start(GTK_BOX(vbox), button2, TRUE, TRUE, 0);
    gtk_widget_set_can_default(button2, TRUE);
    gtk_widget_grab_default(button2);
    gtk_widget_show(button2);

    button = gtk_button_new_from_stock(GTK_STOCK_CANCEL);
    g_signal_connect_swapped(button, "clicked",
                             G_CALLBACK(gtk_widget_destroy),
                             window);
    gtk_box_pack_start(GTK_BOX(vbox), button, TRUE, TRUE, 0);
    gtk_widget_set_can_default(button, TRUE);
    gtk_widget_grab_default(button);
    gtk_widget_show(button);

    gtk_widget_show(window);

    gtk_main();
}

GtkWidget *usernameWidget;
GtkWidget *passwordWidget;
GtkWidget *hostnameWidget;
GtkWidget *portWidget;

static void enter_callback_login(GtkWidget * widget, GtkWidget * entry) {
    user = gtk_entry_get_text(GTK_ENTRY(usernameWidget));
    password = gtk_entry_get_text(GTK_ENTRY(passwordWidget));
    host = gtk_entry_get_text(GTK_ENTRY(hostnameWidget));
    port = atoi(gtk_entry_get_text(GTK_ENTRY(portWidget)));
    printf("Username: %s\n", user.data());
    printf("Password: %s\n", password.data());
    printf("Hostname: %s\n", host.data());
    printf("Port: %d\n", port);
    gtk_widget_destroy(widget);
}

void login() {
    GtkWidget *window;
    GtkWidget *vbox, *hbox;
    GtkWidget *button;
    GtkWidget *button2;
    gint tmp_pos;

    gtk_init(NULL, NULL);

    /* create a new window */
    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_widget_set_size_request(GTK_WIDGET(window), 500, 200);
    gtk_window_set_title(GTK_WINDOW(window), "Login");
    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
    g_signal_connect_swapped(window, "delete-event", G_CALLBACK(gtk_widget_destroy), window);

    vbox = gtk_vbox_new(FALSE, 0);
    gtk_container_add(GTK_CONTAINER(window), vbox);
    gtk_widget_show(vbox);

    usernameWidget = gtk_entry_new();
    gtk_entry_set_max_length(GTK_ENTRY(usernameWidget), 50);
    gtk_entry_set_text(GTK_ENTRY(usernameWidget), "Username");
    tmp_pos = GTK_ENTRY(usernameWidget)->text_length;

    passwordWidget = gtk_entry_new();
    gtk_entry_set_max_length(GTK_ENTRY(passwordWidget), 50);
    gtk_entry_set_text(GTK_ENTRY(passwordWidget), "Password");
    tmp_pos = GTK_ENTRY(passwordWidget)->text_length;

    hostnameWidget = gtk_entry_new();
    gtk_entry_set_max_length(GTK_ENTRY(hostnameWidget), 50);
    gtk_entry_set_text(GTK_ENTRY(hostnameWidget), "Host");
    tmp_pos = GTK_ENTRY(hostnameWidget)->text_length;

    portWidget = gtk_entry_new();
    gtk_entry_set_max_length(GTK_ENTRY(portWidget), 50);
    gtk_entry_set_text(GTK_ENTRY(portWidget), "Port");
    tmp_pos = GTK_ENTRY(portWidget)->text_length;

    gtk_box_pack_start(GTK_BOX(vbox), usernameWidget, TRUE, TRUE, 0);
    gtk_widget_show(usernameWidget);

    gtk_box_pack_start(GTK_BOX(vbox), passwordWidget, TRUE, TRUE, 0);
    gtk_widget_show(passwordWidget);

    gtk_box_pack_start(GTK_BOX(vbox), hostnameWidget, TRUE, TRUE, 0);
    gtk_widget_show(hostnameWidget);

    gtk_box_pack_start(GTK_BOX(vbox), portWidget, TRUE, TRUE, 0);
    gtk_widget_show(portWidget);

    hbox = gtk_hbox_new(FALSE, 0);
    gtk_container_add(GTK_CONTAINER(vbox), hbox);
    gtk_widget_show(hbox);

    button2 = gtk_button_new_from_stock(GTK_STOCK_OK);
    g_signal_connect_swapped(button2, "clicked", G_CALLBACK(enter_callback_login), window);
    gtk_box_pack_start(GTK_BOX(vbox), button2, TRUE, TRUE, 0);
    gtk_widget_set_can_default(button2, TRUE);
    gtk_widget_grab_default(button2);
    gtk_widget_show(button2);

    button = gtk_button_new_from_stock(GTK_STOCK_CANCEL);
    g_signal_connect_swapped(button, "clicked", G_CALLBACK(gtk_widget_destroy), window);
    gtk_box_pack_start(GTK_BOX(vbox), button, TRUE, TRUE, 0);
    gtk_widget_set_can_default(button, TRUE);
    gtk_widget_grab_default(button);
    gtk_widget_show(button);

    gtk_widget_show(window);

    gtk_main();
}

static gboolean time_handler(GtkWidget * widget) {
    if (widget->window == NULL) return FALSE;
    update_list_rooms();
    if (room.data() != NULL) {
        getUsersInRoom();
        populateMessages();
    }
    return TRUE;
}

int main(int argc, char *argv[]) {

    login();

    char responseToAdd[MAX_RESPONSE];

    sendCommand((char *) host.data(), port, (char *) (std::string("ADD-USER ") + user + " " + password).data(),
                responseToAdd);

    if (!strcmp(responseToAdd, "DENIED\r\n")) {
        printf("Invalid username of password\n");
        exit(26);
    } else if (!strcmp("Logged in\r\n", responseToAdd)) {
        printf("Logged in\n");
    } else if (!strcmp("OK\r\n", responseToAdd)) {
        printf("User created on server\n");
    }

    gtk_init(&argc, &argv);

    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_title(GTK_WINDOW(window), std::string(std::string("CS240 IRC Client") + " " + user).data());
    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
    gtk_container_set_border_width(GTK_CONTAINER(window), 10);
    gtk_widget_set_size_request(GTK_WIDGET(window), 450, 400);

    // Create a table to place the widgets. Use a 7x4 Grid (7 rows x 4 columns)
    GtkWidget *table = gtk_table_new(7, 5, TRUE);
    gtk_container_add(GTK_CONTAINER(window), table);
    gtk_table_set_row_spacings(GTK_TABLE(table), 5);
    gtk_table_set_col_spacings(GTK_TABLE(table), 5);
    gtk_widget_show(table);

    // Add list of rooms. Use columns 0 to 4 (exclusive) and rows 0 to 4 (exclusive)
    list_rooms = gtk_list_store_new(1, G_TYPE_STRING);
    update_list_rooms();
    roomList = create_list("Rooms", list_rooms);
    gtk_table_attach_defaults(GTK_TABLE(table), roomList, 3, 5, 0, 2);
    gtk_widget_show(roomList);

    // Add messages text. Use columns 0 to 4 (exclusive) and rows 4 to 7 (exclusive) 
    messages = create_text("");
    gtk_table_attach_defaults(GTK_TABLE(table), messages, 0, 5, 2, 5);
    gtk_widget_show(messages);

    // Add messages entry area
    myMessage = gtk_entry_new();
    gtk_table_attach_defaults(GTK_TABLE(table), myMessage, 0, 5, 5, 7);
    gtk_widget_show(myMessage);

    // Add send button. Use columns 0 to 1 (exclusive) and rows 4 to 7 (exclusive)
    GtkWidget *send_button = gtk_button_new_with_label("Send");
    gtk_table_attach_defaults(GTK_TABLE(table), send_button, 0, 1, 7, 8);
    g_signal_connect(send_button, "clicked", G_CALLBACK(sendMessage), myMessage);
    gtk_widget_show(send_button);

    //Force Enter Room button
    /*GtkWidget *enter_button = gtk_button_new_with_label("(Force)\nEnter Room");
    gtk_table_attach_defaults(GTK_TABLE(table), enter_button, 2, 3, 7, 8);
    g_signal_connect(enter_button, "clicked", G_CALLBACK(enterRoom), roomList);
    gtk_widget_show(enter_button);*/

    //Make room button
    GtkWidget *room_button = gtk_button_new_with_label("Make Room");
    gtk_table_attach_defaults(GTK_TABLE(table), room_button, 4, 5, 7, 8);
    g_signal_connect(room_button, "clicked", G_CALLBACK(createRoom), NULL);
    gtk_widget_show(room_button);

    //Add list of users in current room
    usersInRoom = gtk_list_store_new(1, G_TYPE_STRING);
    list = create_list_users("Users", usersInRoom);
    gtk_table_attach_defaults(GTK_TABLE(table), list, 0, 3, 0, 2);
    gtk_widget_show(list);

    g_timeout_add(5000, (GSourceFunc) time_handler, (gpointer) window);

    gtk_widget_show(table);
    gtk_widget_show(window);

    //gtk_text_view_set_editable(GTK_TEXT_VIEW(buffer), false);

    gtk_main();

    return 0;
}

