
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char ** argv) {
	int c;
	int lines=0;

	printf("Program that counts lines.\n");

 	if (argc < 2) {
	  printf("Usage: toascii filename\n");
	  exit(1);
	}

	// Get file from first argument.
	char * fileName = argv[1];
	FILE * fd = fopen(fileName, "r");
	if (fd == NULL) {
	  printf("Could not open file %s\n", fileName);
	  exit(1);
	}

	// Iterate over all characters in file and test for end of line character
	while ((c=fgetc(fd))!=-1) {
		if (c == '\n') {
			lines++;
		}
	}

	printf("Total lines: %d\n", lines);

	return 0;
}
