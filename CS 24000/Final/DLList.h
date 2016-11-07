
typedef struct DLListEntry {
  public: 
    char * name;
    char * value;
    DLListEntry * next;
    DLListEntry * previous;
};

class DLList {
  DLListEntry * head;
public:
  DLList();
  int add(char *  name, char * value);
  int remove(char * name);
  void concatenate(DLList * list);
  ~DLList();
};





