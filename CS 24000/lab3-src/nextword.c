
#include <stdio.h>
#include <stdlib.h>

//
// Separates the file into words
//

#define MAXWORD 200
char word[MAXWORD];
int wordLength;

// It returns the next word from fd.
// If there are no more more words it returns NULL. 
char * nextword(FILE * fd) {
  	int c;

	wordLength = 0;

	for (int i = 0; i < MAXWORD; i++) word[i] = 0;
	
	// While it is not EOF read char
		// While it is not EOF and it is a non-space char
		// store character in word.
		// if char is not in word return word so far.
		//
	// Return null if there are no more words


	while ((c=fgetc(fd))!= EOF) {
		if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
			if (word[0] == 0) continue;
			return word;
		}
		if (c != ' ' && c != '\n' && c != '\t' && c != '\r') {
			word[wordLength++] = c;
		}
	}
	
	if (wordLength >= 1) {
		word[wordLength] = '\0';
		return word;
	}
	
	return NULL;
}

