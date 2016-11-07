
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// Adds src at the end of dest. Return the value of dest.
char *mystrcat(char * dest, char * src) {
	size_t i,j;
    for (i = 0; dest[i] != '\0'; i++)
        ;
    for (j = 0; src[j] != '\0'; j++)
        dest[i+j] = src[j];
    dest[i+j] = '\0';
    return dest;
}


// Return a pointer to the first occurrence of the substring needle in
// the string haystack. 
// Return NULL if the string is not found.
char *mystrstr( char*hay, char*needle ){
	char *p=hay;
	char *q=needle;
	char *last;
	while( *p!='\0' ){
		last=p;
		while(*p==*q){
			if(*(q+1)=='\0'){//end of needle->needle is found
				return last;
			}
			q++;
			p++;
		}
		p = last;
		p++; //advance along hay
		q=needle; //reset needle
	}
	return NULL;
}


// Trims any spaces that are at the beginning or the end of s and returns s.
// Use the same memory passed in s. 
// Example:
// char s1[20];
// strcpy(s1,      hello    );
// strtrim(s1);
// printf(<s1=%s>\n, s1); // it will print <s1=hello>  without the spaces.
char * mystrtrim(char * str) {

  	size_t len = 0;
    char *frontp = str;
    char *endp = NULL;

    if( str == NULL ) { return NULL; }
    if( str[0] == '\0' ) { return str; }

    len = strlen(str);
    endp = str + len;

    while( isspace(*frontp) ) { ++frontp; }
    if( endp != frontp )
    {
        while( isspace(*(--endp)) && endp != frontp ) {}
    }

    if( str + len - 1 != endp )
            *(endp + 1) = '\0';
    else if( frontp != str &&  endp == frontp )
            *str = '\0';

    endp = str;
    if( frontp != str )
    {
            while( *frontp ) { *endp++ = *frontp++; }
            *endp = '\0';
    }


    return str;
}


// Returns a new string that will substitute the first occurrence of the substring from
//  to the substring to in the string src
// This call will allocate memory for the new string and it will return the new string.
// Example:
// char a[6];
// strcpy(a, apple);
// char * b = strreplace(a, pp, orange);
// printf(<b=%s>\n, b); // It will print <b=aorangele>  
char * mystrreplace( char * src, char * from, char * to)  {
   size_t size    = strlen(src) + 1;
   size_t fromlen = strlen(from);
   size_t tolen   = strlen(to);

   char *value = malloc(size);

   char *dst = value;

   if ( value != NULL )
   {
      for ( ;; )
      {
         const char *match = strstr(src, from);
         if ( match != NULL )
         {
            size_t count = match - src;
            char *temp;
            size += tolen - fromlen;
            temp = realloc(value, size);
            if ( temp == NULL )
            {
               free(value);
               return NULL;
            }
            value = temp;
            memmove(dst, src, count);
            src += count;
            dst += count;
            memmove(dst, to, tolen);
            src += fromlen;
            dst += tolen;
         }
         else
         {
            strcpy(dst, src);
            break;
         }
      }
   }
   return value;
}


