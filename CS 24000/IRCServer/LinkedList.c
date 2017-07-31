
#include <assert.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "LinkedList.h"

//
// Initialize a linked list
//
void llist_init(LinkedList * list) {
	list->head = NULL;
}

//
// It prints the elements in the list in the form:
// 4, 6, 2, 3, 8,7
//
void llist_print(LinkedList * list) {
	
	ListNode * e;

	if (list->head == NULL) {
		printf("{EMPTY}\n");
		return;
	}

	printf("{");

	e = list->head;
	while (e != NULL) {
		printf("%d", e->value);
		e = e->next;
		if (e!=NULL) {
			printf(", ");
		}
	}
	printf("}\n");
}

//
// Appends a new node with this value at the beginning of the list
//
void llist_add(LinkedList * list, int value) {
	// Create new node
	ListNode * n = (ListNode *) malloc(sizeof(ListNode));
	n->value = value;
	
	// Add at the beginning of the list
	n->next = list->head;
	list->head = n;
}

//
// Returns true if the value exists in the list.
//
int llist_exists(LinkedList * list, int value) { 
	ListNode *temp = (ListNode*) malloc(sizeof(ListNode));
	temp = list->head;
	while (temp != NULL) {
		if (temp->value == value) {
			return 1;
		}
		temp = temp->next;
	}
	return 0;
}

//
// It removes the entry with that value in the list.
//
int llist_remove(LinkedList * list, int value) {
	ListNode* temp1 = list->head;
	//ListNode* temp2 = (ListNode*) malloc(sizeof(ListNode));
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

//
// It stores in *value the value that correspond to the ith entry.
// It returns 1 if success or 0 if there is no ith entry.
//
int llist_get_ith(LinkedList * list, int ith, int * value) {
	ListNode* temp = (ListNode*) malloc(sizeof(ListNode));
	temp = list->head;

	int count = 0;
	while (temp != NULL) {
		if (count == ith) {
			*value = temp->value;
			return 1;
		}
		temp = temp->next;
		count++;
	}
	return 0;
}

//
// It removes the ith entry from the list.
// It returns 1 if success or 0 if there is no ith entry.
//
int llist_remove_ith(LinkedList * list, int ith) {
	ListNode *temp1 = (ListNode*) malloc(sizeof(ListNode)); 
	temp1 = list->head;
 
	ListNode *old_temp = (ListNode*) malloc(sizeof(ListNode));
	old_temp = temp1;

	for(int i = 0; i < ith; i++) {
    	old_temp = temp1;
    	temp1 = temp1->next;
    	if (temp1 == NULL && i <= ith) {
    		return 0;
    	}
	}
	old_temp->next = temp1->next;  
	free(temp1);
	return 1;
}

//
// It returns the number of elements in the list.
//
int llist_number_elements(LinkedList * list) {
	ListNode *temp = (ListNode*) malloc(sizeof(ListNode));
	temp = list->head;
	int i = 0;
	while (temp != NULL) {
		temp = temp->next;
		i++;
	}
	return i;
}


//
// It saves the list in a file called file_name. The format of the
// file is as follows:
//
// value1\n
// value2\n
// ...
//
int llist_save(LinkedList * list, char * file_name) {
	ListNode* temp = (ListNode*) malloc(sizeof(ListNode));
	if (list->head == NULL) {
		return 0;
	}
	temp = list->head;
	FILE* fd = fopen(file_name, "w+");
	if (fd == NULL) {
		//printf("was here!");
		return 0;
	}
	while (temp->next != NULL) {
		//printf("%s\n", temp->value);
		fprintf(fd, "%d\n", temp->value);
		temp = temp->next;
	}
	fprintf(fd, "%d\n", temp->value);
	fclose(fd);
}

//
// It reads the list from the file_name indicated. If the list already has entries, 
// it will clear the entries.
//
int llist_read(LinkedList * list, char * file_name) {
	FILE* fd = fopen(file_name, "r");
	if (fd == NULL){
	    //printf("file does not exists %s", filename);
	    return 0;
	}


	//read line by line
	const int line_size = 300;
	char* line = malloc(line_size);
	while (fgets(line, line_size, fd) != NULL){
    	llist_add(list, (int) atof(line));
	}
	return 1;
}


//
// It sorts the list. The parameter ascending determines if the
// order si ascending (1) or descending(0).
//
void llist_sort(LinkedList * list, int ascending) {
	if (ascending == 1) {
		ListNode *temp1 = (ListNode*) malloc(sizeof(ListNode)); 
 
		ListNode *temp2 = (ListNode*) malloc(sizeof(ListNode)); 
 
		int temp = 0;
 
		for(temp1 = list->head; temp1 != NULL; temp1 = temp1->next) {
      		for(temp2 = temp1->next; temp2 != NULL; temp2 = temp2->next) {
            	if(temp1->value > temp2->value) {
                  temp = temp1->value;
                  temp1->value = temp2->value;
                  temp2->value = temp;
            	}
	      	}
		}
	} else if (ascending == 0) {
		ListNode *temp1 = (ListNode*) malloc(sizeof(ListNode)); 
 
		ListNode *temp2 = (ListNode*) malloc(sizeof(ListNode)); 
 
		int temp = 0;
 
		for(temp1 = list->head; temp1 != NULL; temp1 = temp1->next) {
      		for(temp2 = temp1->next; temp2 != NULL; temp2 = temp2->next) {
            	if(temp1->value < temp2->value) {
                  temp = temp2->value;
                  temp2->value = temp1->value;
                  temp1->value = temp;
            	}
	      	}
		}
	}
}

//
// It removes the first entry in the list and puts value in *value.
// It also frees memory allocated for the node
//
int llist_remove_first(LinkedList * list, int * value) {
	if (list->head == NULL) {
		list->head = NULL;
		return 0;
	}
	list->head = list->head->next;
	*value = list->head->value + 1;
	return 1;

}

//
// It removes the last entry in the list and puts value in *value/
// It also frees memory allocated for node.
//
int llist_remove_last(LinkedList * list, int *value) {
	ListNode *temp1 = (ListNode*) malloc(sizeof(ListNode));
	temp1 = list->head;
	if (list->head == NULL) {
		return 0;
	}
	if (llist_number_elements(list) == 1) {
		list->head = NULL;
		return 1;
	}
	while (temp1->next->next != NULL) {
		temp1 = temp1->next;
	}
	*value = temp1->next->value;
	ListNode* temp2 = temp1;
	temp2->next = NULL;
	temp1 = temp1->next;
	free(temp1);
	return 1;
}

//
// Insert a value at the beginning of the list.
// There is no check if the value exists. The entry is added
// at the beginning of the list.
//
void llist_insert_first(LinkedList * list, int value) {
	// Create new node
	ListNode * n = (ListNode *) malloc(sizeof(ListNode));
	n->value = value;
	
	// Add at the beginning of the list
	n->next = list->head;
	list->head = n;
}

//
// Insert a value at the end of the list.
// There is no check if the name already exists. The entry is added
// at the end of the list.
//
void llist_insert_last(LinkedList * list, int value) {
	ListNode *temp = (ListNode*) malloc(sizeof(ListNode));
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
	ListNode *temp1 = (ListNode*) malloc(sizeof(ListNode));
	temp1->value = value;
	temp1->next = NULL;
	temp->next = temp1;
}

//
// Clear all elements in the list and free the nodes
//
void llist_clear(LinkedList *list) {
	int *value = (int*) malloc(sizeof(int));
	while (list->head != NULL) {
		llist_remove_last(list, value);
	}
}
