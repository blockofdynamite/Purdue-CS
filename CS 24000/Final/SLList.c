#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "SLList.h"

SLList *sllist_create() {
    SLList *sl = (SLList *) malloc(sizeof(SLList));
    assert(sl != NULL);
    sl->head = NULL;
}

int sllist_insert(SLList *sllist, char *name, char *value) {

    SLListEntry *e = sllist->head;

    while (e != NULL && strcmp(e->name, name)) {
        e = e->next;
    }

    if (e != NULL) {
        free(e->value);
        e->value = strdup(value);
        return 1;
    }
    e = (SLListEntry *) malloc(sizeof(SLListEntry));
    e->name = strdup(name);
    e->value = strdup(value);
    e->next = sllist->head;
    sllist->head = e;
    return 0;

}

int sllist_remove(SLList *sllist, char *name) {
    SLListEntry *e = sllist->head;
    SLListEntry *prev = NULL;
    while (e != NULL && strcmp(e->name, name)) {
        prev = e;
        e = e->next;
    }
    if (e == NULL) {
        return 0;
    }
    if (prev == NULL) {
        sllist->head = e->next;
    }
    else {
        prev->next = e->next;
    }
    free(e->name);
    free(e->value);
}

int sllist_last(SLList *sllist, char **pname, char **pvalue) {
    SLListEntry *e = sllist->head;
    SLListEntry *prev = NULL;
    if (e == NULL) {
        return 0;
    }
    while (e != NULL) {
        prev = e;
        e = e->next;
    }
    *pname = prev->name;
    *pvalue = prev->value;
    return 1;
}

void sllist_print(SLList *sllist) {
    SLListEntry *e = sllist->head;
    int i = 0;
    while (e != NULL) {
        printf("%d: name=%s value=%s\n", i, e->name, e->value);
        e = e->next;
        i++;
    }
}

void sllist_reverse(SLList *sllist) {
    SLListEntry *e = sllist->head;
    SLListEntry *newHead = NULL;
    SLListEntry *next;
    while (e != NULL) {
        next = e->next;
        e->next = newHead;
        newHead = e;
        e = next;
    }
    sllist->head = newHead;
}

