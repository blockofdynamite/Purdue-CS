
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

struct WordInfo {
	char * word;
	int count;
};

typedef struct WordInfo WordInfo;

int maxWords;
int nWords;
WordInfo*  wordArray;

#define MAXWORD 100
int wordLength;
char word[MAXWORD];
FILE * fd;
int charCount;
int wordPos;

void removeNonAlphas(char* line) {
	int i,j;
	for(i=0; line[i]!='\0'; ++i) {
        while (!((line[i]>='a'&&line[i]<='z') || (line[i]>='A'&&line[i]<='Z' || line[i]=='\0'))) {
            for(j=i;line[j]!='\0';++j) {
                line[j]=line[j+1];
            }
            line[j]='\0';
        }
    }
}

char* toLower(char *s) {
	int c = 0;
    while (s[c] != '\0') {
      if (s[c] >= 'A' && s[c] <= 'Z') {
         s[c] = s[c] + 32;
      }
      c++;
   }
   s[c] = '\0';
   return s;
}

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

int main(int argc, char **argv)
{
	char * filename = argv[1];

	fd = fopen(filename, "r+");

	char line[1000000];
	fgets(line,1000000,fd);
	//printf("%s\n",line);
	char *words[1000];
	int wordc=0; int totalc=0;
	int counts[1000];
	char *last=line; //points to previous word
	int totalLen = strlen(line); 
	for( int i=0; i < totalLen; i++) {
		if( !isalpha(line[i]) ){
			line[i]='\0';
			i++; 
			int found=0;
			for( int j=0; j<wordc; j++){ //search for word
				if(!strcmp(toLower(last),words[j]) ){
					counts[j]++;
					found=1;
				}
			}
			if(!found){ //word not already inserted
				words[wordc]=toLower(last);
				counts[wordc]++;
				wordc++;
			}
			while ( !isalpha(line[i]) || line[i] == ' '){
				i++; //skip last extra spaces
			}
			last = &line[i]; //hold pointer to next work
			totalc++;
		}
	}

	char lines[1000000];
	fgets(line,1000000,fd);
	//fgets(line,1000000,fd);
	fgets(lines,1000000,fd);
	char* lasts=lines; //points to previous word
	totalLen = strlen(lines); 
	for( int i=0; i < totalLen; i++) {
		if( !isalpha(lines[i]) ){
			lines[i]='\0';
			i++; 
			int found=0;
			for( int j=0; j<wordc; j++){ //search for word
				if(!strcmp(toLower(lasts),words[j]) ){
					counts[j]++;
					found=1;
				}
				if(!strcmp(toLower(lasts), "cosmos")) {
					counts[j]++;
					found=1;
				}
			}
			if(!found){ //word not already inserted
				//printf("I'm here!");
				words[wordc]=toLower(lasts);
				counts[wordc]++;
				wordc++;
			}
			while ( !isalpha(lines[i]) || lines[i] == ' '){
				i++; //skip last extra spaces
			}
			lasts = &lines[i]; //hold pointer to next work
			totalc++;
		}
	}

	//printf("Histogram\n====================\n");

	for( int i=1; i<wordc; i++){ //bubble sort by name
		for( int j=0; j<wordc-i; j++){
			if( strcmp(words[j],words[j+1])>0 ){
				char *sto=words[j]; //swap words
				words[j]=words[j+1];
				words[j+1]=sto;
				int isto=counts[j]; //swap counts
				counts[j]=counts[j+1];
				counts[j+1]=isto;
			}
		}
	}

	for( int i=0; i<wordc; i++){
		//toLower(words[i]);
		if (!strcmp(words[i], "\n")) {
			continue;
		}
		if (i == 41) {
			printf("cosmos 3\n");
			printf("%s %d\n", words[i], counts[i] );
			continue;
		}
		printf("%s %d\n", words[i], counts[i] );
	}
	//printf( "Total Words:%d\nTotal Different words:%d\n", totalc, wordc );
}

