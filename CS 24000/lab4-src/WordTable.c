
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "WordTable.h"

// Initializes a word table
void wtable_init(WordTable * wtable) {
	// Allocate and initialize space for the table
	wtable->nWords = 0;
	wtable->maxWords = 10;
	wtable->wordArray = (WordInfo *) malloc(wtable->maxWords * sizeof(WordInfo));
	for (int i = 0; i < wtable->maxWords; i++) {
		llist_init(&wtable->wordArray[i].positions);
	}
}

// Add word to the table and position. Position is added to the corresponding linked list.
void wtable_add(WordTable * wtable, char * word, int position) {
	// Find first word if it exists
	for (int i = 0; i < wtable->nWords; i++) {
		if ( strcmp(wtable->wordArray[i].word, word)== 0 ) {
			// Found word. Add position in the list of positions
			llist_insert_last(&wtable->wordArray[i].positions, position);
			return;
		}
	}

	// Word not found.

	if (wtable->nWords >= wtable->maxWords) {
		wtable->maxWords *= 2;
		WordInfo* temp = (WordInfo*) 
			realloc(wtable->wordArray, sizeof(WordInfo) * wtable->maxWords);
		wtable->wordArray = temp;
	}

	// Make sure that the array has space.
	// Expand the wordArray here.

	// Add new word and position
	wtable->wordArray[wtable->nWords].word = strdup(word);
	llist_insert_last(&wtable->wordArray[wtable->nWords].positions, position);
	wtable->nWords++;
}

// Print contents of the table.
void wtable_print(WordTable * wtable, FILE * fd) {
	fprintf(fd, "------- WORD TABLE -------\n");

	// Print words
	for (int i = 0; i < wtable->nWords; i++) {
		fprintf(fd, "%d: %s: ", i, wtable->wordArray[i].word);
		llist_print( &wtable->wordArray[i].positions);
	}
}

// Get positions where the word occurs
LinkedList * wtable_getPositions(WordTable * wtable, char * word) {
	for (int i = 0; i < wtable->nWords; i++) {
		if (strcmp(wtable->wordArray[i].word, word) == 0) {
			return &wtable->wordArray[i].positions;
		}
	}
}

//
// Separates the string into words
//

#define MAXWORD 200
char word[MAXWORD];
int wordLength;
int wordCount;
int charCount;
int beginningOfWord;

// It returns the next word from stdin.
// If there are no more more words it returns NULL.
// A word is a sequence of alphabetical characters.
static char * nextword() {
	int c;

	wordLength = 0;

	for (int i = 0; i < MAXWORD; i++) word[i] = 0;

	while ((c=fgetc(fd))!= EOF) {
		charCount++;
		if (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '-' || c == '\'') {
			if (word[0] == 0) continue;
			return word;
		}
		if (c != ' ' && c != '\n' && c != '\t' && c != '\r') {
			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
				if (wordLength == 0) {
					beginningOfWord = charCount - 1;
				}
				word[wordLength++] = tolower(c);
			}
		}
	}
	
	if (wordLength >= 1) {
		word[wordLength] = '\0';
		//beginningOfWord = charCount - wordLength;
		return word;
	}
	
	return NULL;
}


// Conver string to lower case
void toLower(char *s) { 
	for (int i = 0; i <= strlen(s); i++) {
            if(s[i] >= 97 && s[i] <= 122)
            	s[i] = s[i] - 32;
  	}
} 


// Read a file and obtain words and positions of the words and save them in table.
int wtable_createFromFile(WordTable * wtable, char * fileName, int verbose) {
	FILE* fd = fopen(fileName, "r");
	if (fd == NULL) {
		return 0;
	}
	int i = 0;
	char * w;
	while ( (w = nextword(fd)) != NULL) {
		if (verbose == 1) {
			printf("%d: word=%s, pos=%d\n", i, w, beginningOfWord);
		}
		i++;
		wtable_add(wtable, w, beginningOfWord);
	}
}

// Sort table in alphabetical order.
void wtable_sort(WordTable * wtable) {
	WordInfo temp;
	for (int i = 0; i < wtable->nWords; i++) {
		for (int j = i; j < wtable->nWords; j++) {
			if (strcmp(wtable->wordArray[i].word, wtable->wordArray[j].word) > 0) {
				temp = wtable->wordArray[j];
				wtable->wordArray[j] = wtable->wordArray[i];
				wtable->wordArray[i] = temp; 
			} 
		}
	}
}

// Print all segments of text in fileName that contain word.
// at most 200 character. Use fseek to position file pointer.
// Type "man fseek" for more info. 
int wtable_textSegments(WordTable * wtable, char * word, char * fileName) {
	FILE* fd = fopen(fileName, "r");
	if (fd == NULL) {
		return 0;
	}
	printf("===== Segments for word \"%s\" in book \"%s\" =====\n", word, fileName);
	LinkedList* temp = wtable_getPositions(wtable, word);
	int value;
	int num = llist_number_elements(temp);
	for (int i = 0; i < num; i++) {
		llist_get_ith(temp, i, &value);
		//printf("%d\n", strlen(word));
		fseek(fd, value, 0);
		printf("---------- pos=%d-----\n", value);
		printf("......");
		int j = 0;
		for (char c = fgetc(fd); j < 200; j++) {
			printf("%c", c);
			c = fgetc(fd);
		}
		printf("......\n");
		//printf("HERE!\n");
	}
}

