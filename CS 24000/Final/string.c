
#include <stdlib.h>

int strlen(char * str) {

	int i = 0;

	while (*str) {
		str++;
		i++;
	}

	return i;
}

char * strcpy(char * dest, char * src) {

	char * p = src;
	char * q = dest;
	while (*p) {
		*q = *p;
		p++;
		q++;
	}
	*q = '\0';
	return dest;
}

char * strcat(char * dest, char * src) {
	char * q = dest;
	while (*q) {
		q++;
	}

	char * p = src;
	while (*p) {
		*q = *p;
		p++;
		q++;
	}
	*q = '\0';
	return dest;
}

char * strstr(char * hay, char * needle) {
	while (*hay) {
		char * p = hay;
		char * q = needle;
		while (*p && *q && *p == *q) {
			p++;
			q++;
		}
		if (*q=='\0') {
			return hay;
		}
		hay++;
	}

	return NULL;
}

