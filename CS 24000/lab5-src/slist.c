
#include <stdio.h>
#include <stdlib.h>

#include "slist.h"

void
sllist_init(SLList * list)
{
	list->head = NULL;
}

// Add int value to the end of list. The values may be repeated.
void
sllist_add_end( SLList *list, int value )  {
	SLEntry *temp = (SLEntry*) malloc(sizeof(SLEntry));
	if (list->head == NULL) {
		list->head = temp;
		temp->value = value;
		list->head->next = NULL;
		return;
	}
	temp = list->head;             
	while(temp->next != NULL) {
		temp = temp->next;
	}
	SLEntry *temp1 = (SLEntry*) malloc(sizeof(SLEntry));
	temp1->value = value;
	temp1->next = NULL;
	temp->next = temp1;
}


// Remove first occurrence of value in the list. It returns 1 if value is found or 0 otherwise.
int sllist_remove(SLList *list, int value) {
	SLEntry* temp1 = list->head;
	//ListNode* temp2 = (ListNode*) malloc(sizeof(ListNode));
	if (list->head->value == value && list->head->next != NULL) {
		list->head = list->head->next;
		return 1;
	}
	while (temp1->next != NULL) {
		if (temp1->next->value == value) {
			temp1->next = temp1->next->next;
			return 1;
			//break;
		}
		temp1 = temp1->next;
	}
	return 0;
}


// Removes from the list and frees the nodes of all the items that are min <= value <= max
void sllist_remove_interval(SLList *list, int min, int max) {
	for (int i = min; i <= max; i++) {
		while (sllist_remove(list, i) == 1);
	}
}

//Helper
int checkIfExists(SLList* list, int value) {
	SLEntry *temp = (SLEntry*) malloc(sizeof(SLEntry));
	temp = list->head;
	while (temp != NULL) {
		if (temp->value == value) {
			return 1;
		}
		temp = temp->next;
	}
	return 0;
}

// It allocates and returns a new list with the intersection of the two lists, that is the 
// list of all the items that are common to the two lists. The items in the intersection are
// unique. 
SLList* sllist_intersection(SLList *a, SLList *b) {
    SLEntry *first, *second;
    SLList* ret_list = (SLList*) malloc(sizeof(SLList));
        
    sllist_init(ret_list);
    for (second = b->head; second; second = second->next) {
        for (first = a->head; first; first = first->next) {
            if (first->value == second->value && !checkIfExists(ret_list, first->value)) {
                sllist_add_end(ret_list, first->value);
            }
        }
    }
        
    return ret_list;
}

void sllist_print(SLList *list)
{
	// Move to the end
	SLEntry * e = list->head;

	printf("--- List ---\n");
	while (e!= NULL) {
		printf("val=%d\n", e->value);
		e = e->next;
	}
}

