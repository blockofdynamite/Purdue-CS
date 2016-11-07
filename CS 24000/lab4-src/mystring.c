
#include <stdlib.h>
#include "mystring.h"

int mystrlen(char * s) {
	int i = 0;
	while (*(s+i) != '\0') {
		i++;
	}
	return i;
}

char * mystrcpy(char * dest, char * src) {
	char* p = src;
	char* q = dest;
	while (*p != '\0') {
		*q = *p;
		p++;
		q++;
	}
	*q = '\0';
	return dest;
}

char * mystrcat(char * dest, char * src) {
	int dest_len = mystrlen(dest);
	int i;

	for (i = 0 ; *(src + i) != '\0' ; i++) {
		*(dest + dest_len + i) = *(src + i);
	}
	*(dest + dest_len + i) = '\0';

	return dest; 
}

int mystrcmp(char * s1, char * s2) {
	int i;	
	for (i = 0; *(s1 + i) != '\0'; i++) {
		if (*(s1 + i) == *(s2 + i)) {
			continue; 
		}
		else if (*(s1 + i) > *(s2 + i)) {
			return 1;
		}
		else if (*(s1 + i) < *(s2 + i)) {
			return -1;
		}
	}
	if (mystrlen(s2) > mystrlen(s1)) { 
		return -1;
	}
	return 0;
}

char * mystrstr(char * hay, char * needle) {
	if (!*needle) return hay;
	char* char1 = (char*)hay;
	while (*char1) {
	    char *first = char1, *char2 = (char*)needle;
	    while (*char1 && *char2 && *char1 == *char2) {
	    	char1++;
	    	char2++;
    	}
    	if (!*char2) return first;
    	char1 = first + 1;
	}
	return NULL;
}

char * mystrdup(char * s) {
	char* toReturn = malloc(mystrlen(s));
	toReturn = mystrcpy(toReturn, s);
	return toReturn;
}

char * mymemcpy(char * dest, char * src, int n) {
	char* dst = dest;
    char* source = src;
    while (n-- != 0)
    	*dest++ = *src++;
    return dest;
}

