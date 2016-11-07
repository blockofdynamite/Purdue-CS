
/*
 * CS-252
 * shell.y: parser for shell
 *
 * This parser compiles the following grammar:
 *
 *	cmd [arg]* [> filename]
 *
 * you must extend it to understand the complete shell grammar
 *
 */

%token	<string_val> WORD

//https://www.cs.purdue.edu/homes/grr/SystemsProgrammingBook/Book/Chapter5-WritingYourOwnShell.pdf
%token 	NOTOKEN PIPE NEWLINE AMPERSAND GREAT GREATGREAT GREATAMPERSAND GREATGREATAMPERSAND LESS

%union	{
		char   *string_val;
	}

%{
//#define yylex yylex
#include <stdio.h>
#include "command.h"
void yyerror(const char * s);
int yylex();

%}

%%

goal:	
	commands
	;

commands: 
	command
	| commands command 
	;

command: simple_command
        ;

simple_command:	
	pipe_list iomodifier_list background_opt NEWLINE {
		//printf("   Yacc: Execute command\n");
		Command::_currentCommand.execute();
	}
	| NEWLINE 
	| error NEWLINE { yyerrok; }
	;

command_and_args:
	command_word argument_list {
		Command::_currentCommand.
			insertSimpleCommand( Command::_currentSimpleCommand );
	}
	;

argument_list:
	argument_list argument
	| /* can be empty */
	;

argument:
	WORD {
		//printf("   Yacc: insert argument \"%s\"\n", $1);

	    Command::_currentSimpleCommand->expandWildcardsIfNecessary( $1 );\
	}
	;

command_word:
	WORD {
		//printf("   Yacc: insert command \"%s\"\n", $1);
	       
	    Command::_currentSimpleCommand = new SimpleCommand();
	    Command::_currentSimpleCommand->insertArgument( $1 );
	}
	;

pipe_list:
	pipe_list PIPE command_and_args
	| command_and_args
	;

background_opt:
	AMPERSAND {
		Command::_currentCommand._background = 1;
	}
	|
	;

iomodifier_list:
	iomodifier_list iomodifier_opt
	|
	;

iomodifier_opt:
	GREAT WORD {
		//This indentation is terrible.
		//printf("   Yacc: insert output \"%s\"\n", $2);
		if (!Command::_currentCommand._outFile) { //Don't have an out file
			Command::_currentCommand._outFile = $2;
		} else {
			perror("Ambiguous output redirect\n");
		}
	}
	| GREATGREAT WORD {
		//I really hate this indentation
		//printf("   Yacc: insert append output \"%s\"\n", $2);
		if (!Command::_currentCommand._outFile) { //Don't have an out file
			Command::_currentCommand._outFile = $2;

			//Additonal flag to make it easier to tell when to append
			Command::_currentCommand._append = 1;
		} else {
			perror("Ambiguous output redirect\n");
		}
	}
	| GREATAMPERSAND WORD {
		//Yep. Still hate this indentation.
		//printf("   Yacc: insert output and error \"%s\"\n", $2);
		if (!Command::_currentCommand._outFile || !Command::_currentCommand._errFile) { //Don't have an out file or error file
			Command::_currentCommand._outFile = $2;
			Command::_currentCommand._errFile = $2;
		} else {
			perror("Ambiguous output redirect\n");
		}
	}
	| GREATGREATAMPERSAND WORD {
		//Absolute trash.
		//printf("   Yacc: insert append output and error \"%s\"\n", $2);
		if (!Command::_currentCommand._outFile || !Command::_currentCommand._errFile) { //Don't have an out file or error file
			Command::_currentCommand._outFile = $2;
			Command::_currentCommand._errFile = $2;

			//Additonal flag to make it easier to tell when to append
			Command::_currentCommand._append = 1;
		} else {
			perror("Ambiguous output redirect\n");
		}
	}
	| LESS WORD {
		//Basically the worst thing ever. 
		//printf("   Yacc: insert input \"%s\"\n", $2);
		if (!Command::_currentCommand._inFile) { //Don't have an out file
			Command::_currentCommand._inFile = $2;
		} else {
			printf("Ambigous output redirect\n");
		}
	}
	;

%%

void
yyerror(const char * s)
{
	fprintf(stderr,"%s", s);
}

#if 0
main()
{
	yyparse();
}
#endif
