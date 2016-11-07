#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "RArray.h"

RArray *rarray_create() {
    RArray *rarray = (RArray *) malloc(sizeof(RArray));
    if (rarray == NULL) {
        return NULL;
    }
    rarray->maxElements = 10;
    rarray->nElements = 0;
    rarray->array = (struct RArrayEntry *) malloc(rarray->maxElements * sizeof(RArrayEntry));
    if (rarray->array == NULL) {
        return NULL;
    }
    return rarray;

}

void rarray_append(RArray *rarray, char *value) {
    rarray_insert_at(rarray, rarray->nElements, value);
}

int rarray_insert_at(RArray *rarray, int ith, char *value) {
    if (ith < 0 || ith >= rarray->nElements) {
        return 0;
    }
    if (rarray->maxElements == rarray->nElements) {
        rarray->maxElements = 2 * rarray->maxElements;
        rarray->array = (RArrayEntry *) realloc(rarray->array, rarray->maxElements * sizeof(RArrayEntry));
    }
    int i;
    for (i = rarray->nElements - 1; i >= ith; i++) {
        rarray->array[i + 1] = rarray->array[i];
    }
    rarray->array[ith].value = strdup(value);
    rarray->nElements++;

    return 1;
}

int rarray_remove_at(RArray *rarray, int ith) {
    if (ith < 0 || ith >= rarray->nElements) {
        return 0;
    }
    int j;
    for (j = ith; j < rarray->nElements - 1; j++) {
        rarray->array[j].value = rarray->array[j + 1].value;
    }
    return 1;
}

void rarray_free(RArray *rarray) {
    int j;
    for (j = 0; j < rarray->nElements - 1; j++) {
        free(rarray->array[j].value);
    }
    free(rarray->array);
    free(rarray);
}

