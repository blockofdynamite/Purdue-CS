
#include <stdio.h>

#include "array.h"

// Return sum of the array
double sumArray(int n, double * array) {
  double sum = 0;
  
  double * p = array;
  double * pend = p+n;

  while (p < pend) {
    sum += *p;
    p++;
}

return sum;
}

// Return maximum element of array
double maxArray(int n, double * array) {
  double max = *array;

  double* p = array;
  double* pend = p + n;
  
  while (pend > p) {
    if (max < *(++p)) {
      max = *p;
    }
  }
  return max;
}

// Return minimum element of array
double minArray(int n, double * array) {
  double min = *array;

  double* p = array;
  double* pend = p + n;
  
  while (pend > p) {
    if (min > *(++p)) {
      min = *p;
    }
  }
  return min;
}

// Find the position int he array of the first element x 
// such that min<=x<=max or -1 if no element was found
int findArray(int n, double * array, double min, double max) {
  double* p = array;
  double* pend = p + n;
  
  int index = 0;

  while (pend > p) {
    if (min < *(++p + index) && max > *(++p + index)) {
      return ++index;
    } else {
      index++;
    }
  }
  return -1;
}

// Sort array without using [] operator. Use pointers 
// Hint: Use a pointer to the current and another to the next element
int sortArray(int n, double * array) {
	int i, j, temp;
  for(i = 0; i < n; i++){
    for(j = i + 1; j < n; j++){
      if(*(array + j) < *(array + i)){
        temp = *(array + j);
        *(array + j) = *(array + i);
        *(array + i) = temp;
      }
    }
  } 
}

// Print array
void printArray(int n, double * array) {
  double* p = array;
  double* pend = p + n;

  int index = 0;
  while (pend > p) {
    printf("%d:%lf\n", index++, *(p++));
  }
}

