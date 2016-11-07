#include "mysort.h"
#include <alloca.h>
#include <assert.h>
#include <string.h>
#include <stdlib.h>

//
// Sort an array of element of any type
// it uses "compFunc" to sort the elements.
// The elements are sorted such as:
//
// if ascending != 0
//   compFunc( array[ i ], array[ i+1 ] ) <= 0
// else
//   compFunc( array[ i ], array[ i+1 ] ) >= 0
//
// See test_sort to see how to use mysort.
//
void mysort( int n,                      // Number of elements
	     int elementSize,            // Size of each element
	     void * array,               // Pointer to an array
	     int ascending,              // 0 -> descending; 1 -> ascending
	     CompareFunction comp )  // Comparison function.
{
	unsigned char *save = (unsigned char*) malloc ( elementSize );
	unsigned char *bytes = (unsigned char*) array;

	int i = 0;
	while (i < n) {
		if (ascending) {
			if ( i == 0 || comp(&bytes[(i - 1) * elementSize], &bytes[i * elementSize] ) <= 0 )
				i++;
			else {
				memcpy ( save, &bytes[i * elementSize], elementSize );
				memcpy ( &bytes[i * elementSize], &bytes[(i - 1) * elementSize], elementSize );
				memcpy ( &bytes[(i - 1) * elementSize], save, elementSize );
				i--;
			}
		} else {
			if ( i == 0 || comp(&bytes[(i - 1) * elementSize], &bytes[i * elementSize] ) >= 0 )
				i++;
			else {
				memcpy ( save, &bytes[i * elementSize], elementSize );
				memcpy ( &bytes[i * elementSize], &bytes[(i - 1) * elementSize], elementSize );
				memcpy ( &bytes[(i - 1) * elementSize], save, elementSize );
				i--;
			}
		}
	}
	free(save);
}

