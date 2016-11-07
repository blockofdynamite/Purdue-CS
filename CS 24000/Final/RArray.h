
typedef struct RArrayEntry {
  char * value;
} RArrayEntry;

typedef struct RArray {
  int nElements;
  int maxElements;
  RArrayEntry * array;
} RArray;

