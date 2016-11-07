//
// CS252: MyMalloc Project
//
// The current implementation gets memory from the OS
// every time memory is requested and never frees memory.
//
// You will implement the allocator as indicated in the handout.
// 
// Also you will need to add the necessary locking mechanisms to
// support multi-threaded programs.
//

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/mman.h>
#include <pthread.h>
#include "MyMalloc.h"

static pthread_mutex_t mutex;

const int ArenaSize = 2097152;
const int NumberOfFreeLists = 1;

// Header of an object. Used both when the object is allocated and freed
struct ObjectHeader {
    size_t _objectSize;         // Real size of the object.
    int _allocated;             // 1 = yes, 0 = no 2 = sentinel
    struct ObjectHeader * _next;       // Points to the next object in the freelist (if free).
    struct ObjectHeader * _prev;       // Points to the previous object.
};

struct ObjectFooter {
    size_t _objectSize;
    int _allocated;
};

  //STATE of the allocator

  // Size of the heap
  static size_t _heapSize;

  // initial memory pool
  static void * _memStart;

  // number of chunks request from OS
  static int _numChunks;

  // True if heap has been initialized
  static int _initialized;

  // Verbose mode
  static int _verbose;

  // # malloc calls
  static int _mallocCalls;

  // # free calls
  static int _freeCalls;

  // # realloc calls
  static int _reallocCalls;
  
  // # realloc calls
  static int _callocCalls;

  // Free list is a sentinel
  static struct ObjectHeader _freeListSentinel; // Sentinel is used to simplify list operations
  static struct ObjectHeader *_freeList;


  //FUNCTIONS

  //Initializes the heap
  void initialize();

  // Allocates an object 
  void * allocateObject( size_t size );

  // Frees an object
  void freeObject( void * ptr );

  // Returns the size of an object
  size_t objectSize( void * ptr );

  // At exit handler
  void atExitHandler();

  //Prints the heap size and other information about the allocator
  void print();
  void print_list();

  // Gets memory from the OS
  void * getMemoryFromOS( size_t size );

  void increaseMallocCalls() { _mallocCalls++; }

  void increaseReallocCalls() { _reallocCalls++; }

  void increaseCallocCalls() { _callocCalls++; }

  void increaseFreeCalls() { _freeCalls++; }

extern void
atExitHandlerInC()
{
  atExitHandler();
}

void initialize()
{
  // Environment var VERBOSE prints stats at end and turns on debugging
  // Default is on
  _verbose = 1;
  const char * envverbose = getenv( "MALLOCVERBOSE" );
  if ( envverbose && !strcmp( envverbose, "NO") ) {
    _verbose = 0;
  }

  pthread_mutex_init(&mutex, NULL);
  void * _mem = getMemoryFromOS( ArenaSize + (2*sizeof(struct ObjectHeader)) + (2*sizeof(struct ObjectFooter)) );

  // In verbose mode register also printing statistics at exit
  atexit( atExitHandlerInC );

  //establish fence posts
  struct ObjectFooter * fencepost1 = (struct ObjectFooter *)_mem;
  fencepost1->_allocated = 1;
  fencepost1->_objectSize = 123456789;
  char * temp = 
      (char *)_mem + (2*sizeof(struct ObjectFooter)) + sizeof(struct ObjectHeader) + ArenaSize;
  struct ObjectHeader * fencepost2 = (struct ObjectHeader *)temp;
  fencepost2->_allocated = 1;
  fencepost2->_objectSize = 123456789;
  fencepost2->_next = NULL;
  fencepost2->_prev = NULL;

  //initialize the list to point to the _mem
  temp = (char *) _mem + sizeof(struct ObjectFooter);
  struct ObjectHeader * currentHeader = (struct ObjectHeader *) temp;
  temp = (char *)_mem + sizeof(struct ObjectFooter) + sizeof(struct ObjectHeader) + ArenaSize;
  struct ObjectFooter * currentFooter = (struct ObjectFooter *) temp;
  _freeList = &_freeListSentinel;
  currentHeader->_objectSize = ArenaSize + sizeof(struct ObjectHeader) + sizeof(struct ObjectFooter); //2MB
  currentHeader->_allocated = 0;
  currentHeader->_next = _freeList;
  currentHeader->_prev = _freeList;
  currentFooter->_allocated = 0;
  currentFooter->_objectSize = currentHeader->_objectSize;
  _freeList->_prev = currentHeader;
  _freeList->_next = currentHeader; 
  _freeList->_allocated = 2; // sentinel. no coalescing.
  _freeList->_objectSize = 0;
  _memStart = (char*) currentHeader;
}

void * allocateObject( size_t size )
{
  //Make sure that allocator is initialized
  if ( !_initialized ) {
    _initialized = 1;
    initialize();
  }

  // Add the ObjectHeader/Footer to the size and round the total size up to a multiple of
  // 8 bytes for alignment. 
  size_t roundedSize = (size + sizeof(struct ObjectHeader) + sizeof(struct ObjectFooter) + 7) & ~7;

  //Pointer to _freeList so we can iterate without iterating the "real" pointer
  struct ObjectHeader* freeList = _freeList;

  //Iterating through the whole list of usable memory
  //This is where we will allocate memory if we have enough space
  //We go through, get memory if we can
  //Then we update the header, then footer, then the object's header and footers
  //All these steps have space inbetween them
  while (freeList != _freeList->_next) {
  	if (_freeList->_next->_objectSize >= roundedSize) {
  		void* toReturn = (void*) ((char*) _freeList->_next + sizeof(struct ObjectHeader));

  		struct ObjectHeader* newHeader = _freeList->_next;
  		_freeList->_next = (struct ObjectHeader*) ((char*) _freeList->_next + roundedSize); //Turns out I don't know how to cast...
  		_freeList->_next->_objectSize = newHeader->_objectSize - roundedSize; 
  		_freeList->_next->_allocated = 0;
  		_freeList->_next->_next = newHeader->_next;
  		_freeList->_next->_prev = newHeader->_prev;

  		struct ObjectFooter* newFooter = (struct ObjectFooter*) ((char*) _freeList->_next + (_freeList->_next->_objectSize - sizeof(struct ObjectFooter)));
  		newFooter->_objectSize = _freeList->_next->_objectSize;

  		struct ObjectHeader* objectHeader = (struct ObjectHeader*) (toReturn - sizeof(struct ObjectHeader));
  		objectHeader->_allocated = 1;
  		objectHeader->_objectSize = roundedSize;
  		objectHeader->_prev = 0;
  		objectHeader->_next = 0;

  		struct ObjectFooter* objectFooter = (struct ObjectFooter*) (toReturn + (roundedSize - sizeof(struct ObjectFooter) - sizeof(struct ObjectHeader)));
  		objectFooter->_allocated = 1;
  		objectFooter->_objectSize = roundedSize;

		pthread_mutex_unlock(&mutex);

		return toReturn;
  	}
  	freeList = freeList->_next;
  }

  // Need to get more memory for the object.
  void * _mem = getMemoryFromOS(ArenaSize + 2 * sizeof(struct ObjectFooter) + 2 * sizeof(struct ObjectHeader));
  char* ptr = (char*) _mem + sizeof(struct ObjectFooter);
  struct ObjectHeader* nMem = (struct ObjectHeader*) ptr;

  //Make sure to update fenceposts (which I forgot to do :/)
  struct ObjectHeader* fpBegin = (struct ObjectHeader*) _mem;
  fpBegin->_allocated = 1;
  fpBegin->_objectSize = 123456789; //Size doesn't matter apparently... (Or so I was told)

  ptr = (char*) _mem + 2 * sizeof(struct ObjectFooter) + sizeof(struct ObjectHeader) + ArenaSize;

  //Update fencepost at the end
  struct ObjectHeader* fpEnd = (struct ObjectHeader*) ptr;
  fpEnd->_allocated = 1;
  fpEnd->_objectSize = 123456789;
  fpEnd->_next = 0;
  fpEnd->_prev = 0;

  _freeList->_next = nMem; //add new memory to the _freeList
  nMem->_next = _freeList; //Set up Doubly Linked List
  nMem->_objectSize = ArenaSize + sizeof(struct ObjectFooter) + sizeof(struct ObjectHeader);
  nMem->_allocated = 0;

  // Return a pointer to usable memory using a recursive call now that there is more memory. 
  return allocateObject(size);

}

void freeObject( void * ptr )
{
	//Sorry for weird formatting, Sublime can be weird... Esp. when Gustavo can't decide if he likes 2 or 4 space tabs...
	struct ObjectHeader* header = (struct ObjectHeader*) ((char*) ptr - sizeof(struct ObjectHeader));
	header->_allocated = 0;
	struct ObjectFooter* footer = (struct ObjectFooter*) ((char*) header + (header->_objectSize - sizeof(struct ObjectFooter)));
	footer->_allocated = 0;

  	struct ObjectHeader* freeList = _freeList->_next;

  	//perror("Test");

  	//Set up headers and footers for the object
  	//Then we check for coallesing
  	//Then we combine the chunks if need be
  	while (freeList != _freeList) {
  		if ((void*) freeList > ptr) {
  			struct ObjectHeader* bottom = freeList->_prev;
  			struct ObjectHeader* top = freeList;
  			struct ObjectFooter* bottomFooter = (struct ObjectFooter*) ((char*) header - sizeof(struct ObjectFooter));
  			struct ObjectHeader* topHeader = (struct ObjectHeader*) ((char*) header + header->_objectSize);

  			if (bottomFooter->_allocated == 0 && topHeader->_allocated == 0) { //both sides of the chunk can be combined
  				bottom->_objectSize += header->_objectSize + top->_objectSize;
  				bottom->_next = top->_next;
  				top->_next->_prev = bottom;
  				struct ObjectFooter* newFooter = (struct ObjectFooter*) ((char*) bottom + (bottom->_objectSize - sizeof(struct ObjectFooter)));
  				newFooter->_objectSize = bottom->_objectSize;
  				return;
  			} else if (bottomFooter->_allocated == 0) { //Bottom of chunk can be combined
  				//bottom->_next = top;
  				//top->_next = bottom;
  				bottom->_objectSize += header->_objectSize;
  				bottom->_allocated = 0;
  				struct ObjectFooter* newFooter = (struct ObjectFooter*) ((char*) bottom + (bottom->_objectSize - sizeof(struct ObjectFooter)));
  				newFooter->_objectSize = bottom->_objectSize;
  				newFooter->_allocated = 0;
  				return;
  			} else if (topHeader->_allocated == 0) { //Top of chunk can be combined
  				header->_objectSize += top->_objectSize;
  				header->_allocated = 0;

  				header->_next = top->_next;
  				top->_next->_prev = header;
  				top->_prev->_next = header;
  				header->_prev = top->_prev;

  				struct ObjectFooter* newFooter = (struct ObjectFooter*) ((char*) header + (header->_objectSize - sizeof(struct ObjectFooter)));
  				newFooter->_objectSize = header->_objectSize;
  				newFooter->_allocated = 0;
  				return;
  			} else {
  				bottom->_next = header;
  				header->_prev = bottom;
  				top->_prev = header;
  				header->_next = top;
  			}
  		}
  		freeList = freeList->_next;
  	}
  	return;
}

size_t objectSize( void * ptr )
{
  // Return the size of the object pointed by ptr. We assume that ptr is a valid obejct.
  struct ObjectHeader * o =
    (struct ObjectHeader *) ( (char *) ptr - sizeof(struct ObjectHeader) );

  // Substract the size of the header
  return o->_objectSize;
}

void print()
{
  printf("\n-------------------\n");

  printf("HeapSize:\t%zd bytes\n", _heapSize );
  printf("# mallocs:\t%d\n", _mallocCalls );
  printf("# reallocs:\t%d\n", _reallocCalls );
  printf("# callocs:\t%d\n", _callocCalls );
  printf("# frees:\t%d\n", _freeCalls );

  printf("\n-------------------\n");
}

void print_list()
{
  printf("FreeList: ");
  if ( !_initialized ) {
    _initialized = 1;
    initialize();
  }
  struct ObjectHeader * ptr = _freeList->_next;
  while(ptr != _freeList){
      long offset = (long)ptr - (long)_memStart;
      printf("[offset:%ld,size:%zd]",offset,ptr->_objectSize);
      ptr = ptr->_next;
      if(ptr != NULL){
          printf("->");
      }
  }
  printf("\n");
}

void * getMemoryFromOS( size_t size )
{
  // Use sbrk() to get memory from OS
  _heapSize += size;
 
  void * _mem = sbrk( size );

  if(!_initialized){
      _memStart = _mem;
  }

  _numChunks++;

  return _mem;
}

void atExitHandler()
{
  // Print statistics when exit
  if ( _verbose ) {
    print();
  }
}

//
// C interface
//

extern void *
malloc(size_t size)
{
  pthread_mutex_lock(&mutex);
  increaseMallocCalls();
  
  return allocateObject( size );
}

extern void
free(void *ptr)
{
  pthread_mutex_lock(&mutex);
  increaseFreeCalls();
  
  if ( ptr == 0 ) {
    // No object to free
    pthread_mutex_unlock(&mutex);
    return;
  }
  
  freeObject( ptr );
}

extern void *
realloc(void *ptr, size_t size)
{
  pthread_mutex_lock(&mutex);
  increaseReallocCalls();
    
  // Allocate new object
  void * newptr = allocateObject( size );

  // Copy old object only if ptr != 0
  if ( ptr != 0 ) {
    
    // copy only the minimum number of bytes
    size_t sizeToCopy =  objectSize( ptr );
    if ( sizeToCopy > size ) {
      sizeToCopy = size;
    }
    
    memcpy( newptr, ptr, sizeToCopy );

    //Free old object
    freeObject( ptr );
  }

  return newptr;
}

extern void *
calloc(size_t nelem, size_t elsize)
{
  pthread_mutex_lock(&mutex);
  increaseCallocCalls();
    
  // calloc allocates and initializes
  size_t size = nelem * elsize;

  void * ptr = allocateObject( size );

  if ( ptr ) {
    // No error
    // Initialize chunk with 0s
    memset( ptr, 0, size );
  }

  return ptr;
}

