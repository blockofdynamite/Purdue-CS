#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void mymemdump(FILE * fd, char * p , int len) {

	int i; //for use in for loops
	int j; // " "
	int k; // " "

    for (i = 0; i < len; i += 16) {

		fprintf(fd, "0x%016lX: ", (unsigned long) p + i);

		int c;

		for (j = i; j < i + 16; j++) {
			c = p[j] & 0xFF;
			if (j > len - 1) {
				fprintf(fd, "   ", c);
			}
			else fprintf(fd, "%02X ", c);
		}

		fprintf(fd, " ", c);

		for (j = i; j < i + 16; j++) {
			c = p[j] & 0xFF;
			if (j > len - 1) {
				break;
			}
			fprintf(fd, "%c", (c >= 32 && c <= 127) ? c : '.');		
		}

		fprintf(fd,"\n");
	}
}

//                   _
//                 _(_)_ 
//     @@@@       (_)@(_)  vVVVv    _     @@@@
//    @@()@@ wWWWw  (_)\   (___)  _(_)_  @@()@@
//     @@@@  (___)     `|/   Y   (_)@(_)  @@@@
//      /      Y       \|   \|/   /(_)    \|
//   \ |     \ |/       | /\ | / \|/       |/
//     |///  \\|/// \\\\|//\\|///\|///  \\\|//
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 
