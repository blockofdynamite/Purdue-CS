
/*
 *
 * CS-252 Fall 2013
 * shell.l: lexical analyzer for shell
 * You have to extend it.
 * https://www.cs.purdue.edu/homes/grr/SystemsProgrammingBook/Book/Chapter5-WritingYourOwnShell.pdf
 *
 */

%{

#include <string.h>
#include "y.tab.h"
#include <sys/wait.h>
#include <unistd.h>

static  void yyunput (int c,char *buf_ptr  );

void myunputc(int c) {
    unput(c);
}

extern "C" char * read_line();
int mygetc(FILE * f) {
	static char *p;
	char ch;
	if (!isatty(0)) {
		// stdin is not a tty. Call real getc
		return getc(f);
	}
	// stdin is a tty. Call our read_line.
	if (p==NULL || *p == 0) {
	      	char * s = read_line();
	        p = s;
	}
	ch = *p;
	p++;
	return ch;
}
#undef getc
#define getc(f) mygetc(f)

%}

%%

\n 		{
		return NEWLINE;
}

[ \t] 	{
		/* Discard spaces and tabs */
     	}

"|"		{
		return PIPE;
	}

"&"		{
		return AMPERSAND;
}

">" 	{
		return GREAT;
	}

">>"	{
		return GREATGREAT;
	}

">&"	{
		return GREATAMPERSAND;
	}

">>&"	{
		return GREATGREATAMPERSAND;
	}

"<"		{
		return LESS;
}

[^ \t\n][^ \t\n]*  {
		//dup and return
		yylval.string_val = strdup(yytext);
		return WORD;
	}

\"[^"]*\"	{
		//get rid of first quotes
		yytext = (char*)(yytext+1); 

		//get rid of end quotes
		yytext[ strlen(yytext) - 1 ] = '\0'; 

		//return said string as a new word
		yylval.string_val = strdup(yytext);
		return WORD;
	}
\`[^\n\`]*\`	{
		//I have no idea what to do, so I'm not gonna do it unless I have time.
		yylval.string_val = strdup(yytext);
		return WORD;
	}

