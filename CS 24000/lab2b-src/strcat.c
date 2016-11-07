
#include "mystring.h"

#include <assert.h>
#include <stdlib.h>
#include <stdio.h>

char * mystrcat(char * dest, char * src) {
	unsigned int i, j = mystrlen(dest);
	if (src[0] != '\0') {
		for (i = 0; i < mystrlen(src); i++) {
		    dest[j] = src[i];
		    j++;
		}
	}
}

int main() {
	char a[200];
	char * b;
	strcpy(a, "Hello world");
	b = mystrcat(a, ", CS240 C Programming");
	b = mystrcat(a, ", This is a great course");
	printf("\"%s\"\n", a);
	printf("\"%s\"\n", b);

	mystrcpy(a, "");
	b = mystrcat(a, "");
	b = mystrcat(b, "Hello");
	
	printf("\"%s\"\n", a);
	printf("\"%s\"\n", b);
}
