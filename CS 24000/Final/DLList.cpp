
#include <string.h>
#include <stdlib.h>
#include "DLList.h"

DLList::DLList() {
	head = new DLListEntry();
	
	head->next = head;
	head->previous = head;
}

int DLList::add(char *  name, char * value) {
	DLListEntry * e;
	e = head->next;
	while (e != head) {
		if (!strcmp(e->name,name)) {
			free(e->value);
			e->value = strdup(value);
			return 1;
		}
		e = e->next;
	}

	e = new DLListEntry;
	e->name = strdup(name);
	e->value = strdup(value);

	e->next = head;
	e->previous = head->previous;
	head->previous = e;
	e->previous->next = e;
	
	return 0;
}

int DLList::remove(char * name) {
	DLListEntry * e;
	e = head->next;
	
	while (e!=head) {
		if (!strcmp(e->name,name)) {
			break;
		}
		e = e->next;
	}

	if (e==head) {
		return 0;
	}

	e->previous->next = e->next;
	e->next->previous = e->previous;

	free(e->name);
	free(e->value);
	free(e);

	return 1;
	
}

void DLList::concatenate(DLList * list) {
	DLListEntry * e;
	e = list->head->next;
	while (e!=head) {
		add(e->name, e->value);
		e = e->next;
	}
}

DLList::~DLList() {
	DLListEntry * e;
	DLListEntry *next;
	e = head->next;
	while (e!=head) {
		next = e->next;
		free(e->name);
		free(e->value);
		delete e;
		e = next;
	}
}


