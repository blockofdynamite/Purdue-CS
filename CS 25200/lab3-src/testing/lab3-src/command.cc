
/*
 * CS252: Shell project
 *
 * Template file.
 * You will need to add more code here to execute the command table.
 *
 * NOTE: You are responsible for fixing any bugs this code may have!
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h> 
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <signal.h>
#include <pwd.h>
#include <regex.h>
#include <dirent.h>

#include "command.h"

char* shellPath;
int lastReturn = 0;
int bg_pid = 0;

extern char** environ;

int tempEntries = 20; 
int nEntries = 0; 
char ** array = (char **) malloc(tempEntries * sizeof(char*)); 

SimpleCommand::SimpleCommand()
{
	// Create available space for 5 arguments
	_numOfAvailableArguments = 5;
	_numOfArguments = 0;
	_arguments = (char **) malloc( _numOfAvailableArguments * sizeof( char * ) );
}

//Bubble sort to sort entries from wildcarding
void sortArrayStrings(char ** array, int nEntries) {
	for (int i = 0; i < nEntries; i++) {
		for (int j = 0; j < nEntries - 1; j++) {
			if (strcmp(array[j], array[j + 1]) > 0) { 	
				char* temp = array[j]; 
				array[j] = array[j + 1]; 
				array[j + 1] = temp; 
			} 
		}
	} 
} 

void expandWildcards(char* prefix, char* suffix) {

	//Out of space https://www.youtube.com/watch?v=xeKMS62GrTI
	if (suffix[0] == 0) {
		if (nEntries == tempEntries) {
			tempEntries *= 2;
			array = (char**) realloc(array, tempEntries * sizeof(char*));
		}
		array[nEntries] = strdup(prefix);
		nEntries++;
		return;
	}

	int dirOrNot = 0;
	char* testDir;
	if ((testDir = strchr(suffix, '*')) != NULL) {
		if (strlen(testDir) > 1) {
			if (testDir[1] == '/') {
				dirOrNot = 1; 
			}
		}
	}

	char* s = strchr(suffix, '/'); 
	char component[1024]; 

	//Checking to see if the suffix has a / in it
	if (s != NULL) {  
		strncpy(component, suffix, s - suffix); 
		suffix = s + 1; 
	} else { 
		strcpy(component, suffix); 
		suffix = suffix + strlen(suffix); 
	}

	//Checking string format to see what we need to do
	char dupPrefix[1024]; 
	if ( strchr(component, '*') == NULL && strchr(component, '?') == NULL) {
		if (!prefix[0]) {
			sprintf(dupPrefix, "%s%s", prefix, component); 
		} else if (prefix[0] == '/' && strlen(prefix) == 1) {
			sprintf(dupPrefix, "%s%s", prefix, component); 
		} else {
			sprintf(dupPrefix, "%s/%s", prefix, component); 
		}
		
		expandWildcards(dupPrefix, suffix);  
		return; 
	}

	//Expand AAAALLLLL the things
	//Thanks Gustavo for the code!
	char* reg = (char*) malloc(2*strlen(component) + 10);
	char* a = component;
	char* r = reg;
	*r = '^'; 
	r++;
	while (*a) {
		if (*a == '*') { 
			*r='.'; 
			r++; 
			*r='*'; 
			r++; 
		} else if (*a == '?') { 
			*r='.'; 
			r++; 
		} else if (*a == '.') { 
			*r='\\'; 
			r++; 
			*r='.'; 
			r++; }
		else { *r=*a; 
			r++; 
		}
		a++;
	}
	*r='$'; 
	r++; 
	*r=0;

	//See if we have matches
	regex_t re;
	regmatch_t rm;
	int expbuf = regcomp(&re, reg, REG_EXTENDED|REG_NOSUB);

	if (expbuf) {
		perror("regcomp");
		return;
	}

	//Open our dir to get everything
	char* dirToOpen;
	if (!strcmp(prefix, ""))
		dirToOpen = (char*) ".";
	else
		dirToOpen = prefix;

	DIR* dir = opendir(dirToOpen);
	if (dir == NULL) {
		perror("opendir");
		return;
	}

	//Go through dir and add everything
	struct dirent* ent;
	while ((ent = readdir(dir)) != NULL) {
		if (!regexec(&re, ent->d_name, 1, &rm, 0)) {
			if (dirOrNot) {
				if (ent->d_type != DT_DIR) {
					continue;
				}
			}
			//nEntries++;
			//Command::_currentSimpleCommand->insertArgument(strdup(ent->d_name));
			if (ent->d_name[0] == '.') { 
				if (component[0] == '.' ) { 
					if(prefix[0] == '\0')
						sprintf(dupPrefix, "%s%s", prefix, ent->d_name); 
					else 
						sprintf(dupPrefix, "%s/%s", prefix, ent->d_name); 

					expandWildcards(dupPrefix, suffix); 
				}
			} else {
				//Copied from above
				if (prefix[0] == '\0') {
					sprintf(dupPrefix, "%s%s", prefix, ent->d_name); 
				} else if (prefix[0] == '/' && strlen(prefix) == 1) {
					sprintf(dupPrefix, "%s%s", prefix, ent->d_name); 
				} else {
					sprintf(dupPrefix, "%s/%s", prefix, ent->d_name); 
				}
		
				expandWildcards(dupPrefix, suffix);  
			}
		}
	}
	closedir(dir);
}

void
SimpleCommand::expandWildcardsIfNecessary(char* argument) {

	//If the argument contains a wildcard
	if (!strchr(argument, '*') && !strchr(argument, '?')) {
		insertArgument(argument);
		return; 
	}

	//Dup argument
	char* dup = argument;
	dup++;

	if (argument[0] == '/') {
		expandWildcards("/", argument);
	} else {
		expandWildcards("", argument);
	}

	//printf("%d\n", nEntries);
	sortArrayStrings(array, nEntries); 

	for (int i=0; i < nEntries; i++) {
		Command::_currentSimpleCommand->insertArgument(array[i]); 
	}

	free(array); 
	tempEntries = 20; 
	nEntries = 0; 
	array = (char **) malloc(tempEntries * sizeof(char*)); 
}

void
SimpleCommand::insertArgument( char * argument )
{
	if ( _numOfAvailableArguments == _numOfArguments  + 1 ) {
		// Double the available space
		_numOfAvailableArguments *= 2;
		_arguments = (char **) realloc( _arguments,
				  _numOfAvailableArguments * sizeof( char * ) );
	}
	
	if (argument[0] == '~') {
		if (strlen(argument) == strlen("~")) { 
			char* homedir = getpwuid(getuid())->pw_dir;

			char* arg = (char*) malloc(strlen(homedir) + strlen(argument) * sizeof(char));
			strcat(arg, homedir);
			strcat(arg, "/");
			strcat(arg, (argument+1));
			argument = arg;
		} else {
			char* arg = (char*) malloc(strlen("/homes/") + strlen(argument) * sizeof(char));
			strcat(arg, "/homes/");
			strcat(arg, (argument+1));
			argument = arg;
		}
	}

	char* expandedVarString = (char*) malloc(sizeof(char*) * strlen(argument) + 100);
	if (strchr(argument, '$') && strchr(argument, '{') && strchr(argument, '}')) {
		int i = 0;
		int j = 0;
		for (int k = 0; k < strlen(argument); k++) {
			//If we're in a variabel expansion
			if (argument[i] == '$') {
				char* currentVar = (char*) malloc(strlen(argument));
				i++; i++;
				while (argument[i] != '}') 
					currentVar[j++] = argument[i++];

				currentVar[j] = 0;

				if (!strcmp(currentVar, "$")) {
					sprintf(currentVar, "%d", getpid());
					strcat(expandedVarString, currentVar);
					continue;
				} else if (!strcmp(currentVar, "?")) {
					sprintf(currentVar, "%d", lastReturn);
					strcat(expandedVarString, currentVar);
					continue;
				} else if (!strcmp(currentVar, "!")) {
					sprintf(currentVar, "%d", bg_pid);
					strcat(expandedVarString, currentVar);
					continue;
				} else if (!strcmp(currentVar, "_")) {
					//Not Implemented
				} else if (!strcmp(currentVar, "SHELL")) {
					currentVar = shellPath;
					strcat(expandedVarString, currentVar);
					continue;
				} 
				strcat(expandedVarString, getenv(currentVar));
				j = 0; i++;
			} 
			//Or get the rest of the arg
			else {
				char* currentVar = (char*) malloc(strlen(argument));
                while(argument[i] && argument[i] != '$') {
                    currentVar[j++] = argument[i++];
                }
                currentVar[j] = 0;
                strcat(expandedVarString, currentVar);
                j = 0; i--;
			}
		}
		char* dupString = (char*) malloc(strlen(expandedVarString) * sizeof(char*));
		for (int i = 0; i < strlen(expandedVarString); i++) {
			if (expandedVarString[i] != '}') {
				dupString[i] = expandedVarString[i];
			} else {
				dupString[i] = 0;
				break;
			}
		}
		argument = strdup(dupString);
	}

	_arguments[ _numOfArguments ] = argument;

	// Add NULL argument at the end
	_arguments[ _numOfArguments + 1] = NULL;
	
	_numOfArguments++;
}

Command::Command()
{
	// Create available space for one simple command
	_numOfAvailableSimpleCommands = 1;
	_simpleCommands = (SimpleCommand **)
		malloc( _numOfSimpleCommands * sizeof( SimpleCommand * ) );

	_numOfSimpleCommands = 0;
	_outFile = 0;
	_inFile = 0;
	_errFile = 0;
	_background = 0;
}

void
Command::insertSimpleCommand( SimpleCommand * simpleCommand )
{
	if ( _numOfAvailableSimpleCommands == _numOfSimpleCommands ) {
		_numOfAvailableSimpleCommands *= 2;
		_simpleCommands = (SimpleCommand **) realloc( _simpleCommands,
			 _numOfAvailableSimpleCommands * sizeof( SimpleCommand * ) );
	}
	
	_simpleCommands[ _numOfSimpleCommands ] = simpleCommand;
	_numOfSimpleCommands++;
}

void
Command:: clear()
{
	for ( int i = 0; i < _numOfSimpleCommands; i++ ) {
		for ( int j = 0; j < _simpleCommands[ i ]->_numOfArguments; j ++ ) {
			free ( _simpleCommands[ i ]->_arguments[ j ] );
		}
		
		free ( _simpleCommands[ i ]->_arguments );
		free ( _simpleCommands[ i ] );
	}

	/*if ( _outFile ) {
		free( _outFile );
	}

	if ( _inFile ) {
		free( _inFile );
	}

	if ( _errFile ) {
		free( _errFile );
	}*/

	_numOfSimpleCommands = 0;
	_outFile = 0;
	_inFile = 0;
	_errFile = 0;
	_background = 0;
}

void
Command::print()
{
	printf("\n\n");
	printf("              COMMAND TABLE                \n");
	printf("\n");
	printf("  #   Simple Commands\n");
	printf("  --- ----------------------------------------------------------\n");
	
	for ( int i = 0; i < _numOfSimpleCommands; i++ ) {
		printf("  %-3d ", i );
		for ( int j = 0; j < _simpleCommands[i]->_numOfArguments; j++ ) {
			printf("\"%s\" \n", _simpleCommands[i]->_arguments[ j ] );
		}
	}

	printf( "\n\n" );
	printf( "  Output       Input        Error        Background\n" );
	printf( "  ------------ ------------ ------------ ------------\n" );
	printf( "  %-12s %-12s %-12s %-12s\n", _outFile?_outFile:"default",
		_inFile?_inFile:"default", _errFile?_errFile:"default",
		_background?"YES":"NO");
	printf( "\n\n" );
	
}

void
Command::execute()
{
	// Don't do anything if there are no simple commands
	if ( _numOfSimpleCommands == 0 ) {
		prompt();
		return;
	}

	// Print contents of Command data structure
	// print();

	int tmpin = dup(0);
	int tmpout = dup(1);
	int tmperr = dup(2);

	//file descriptors for printing
	int ret;
	int fdin;
	int fdout;
	int fderr;

	//If we have an infile or not
	if (_inFile) {
		fdin = open(_inFile, O_RDONLY);
	} else {
		fdin = dup(tmpin);
	}

	//Loop through and execute all commands
	for (int i = 0; i < _numOfSimpleCommands; i++) {
		
		//stdout
		dup2(fdin, 0);
		close(fdin);

		//If last		
		if (i == _numOfSimpleCommands - 1) {

			//Checking where to dump output
			
			//If we have an outfile
			if (_outFile) {
				if (_append) {
					fdout = open(_outFile, O_CREAT|O_WRONLY|O_APPEND, 0777);
				} else {
					fdout = open(_outFile, O_CREAT|O_WRONLY|O_TRUNC, 0777);
				}
				if (fdout < 0) {
					perror("open");
					exit(1);
				}
			}
			//Or not
			else {
				fdout = dup(tmpout);
			}

			//If we have an error file
			if (_errFile) {
				if (_append) {
					fderr = open(_outFile, O_CREAT|O_WRONLY|O_APPEND, 0777);
				} else {
					fderr = open(_outFile, O_CREAT|O_WRONLY|O_TRUNC, 0777);
				}
				if (fderr < 0) {
					perror("open");
					exit(1);
				}

				dup2(fderr, 2);
				close(fderr);

			}
		}
		
		//Not last command
		else {
			int fdpipe[2];
			pipe(fdpipe);
			fdout = fdpipe[1];
			fdin = fdpipe[0];
		}

		dup2(fdout, 1);
		close(fdout);
		
		//checking for special commands

		/*printf("%s ", _simpleCommands[i]->_arguments[0]);
		if (_simpleCommands[i]->_arguments[1] != NULL) {
			printf("%s\n", _simpleCommands[i]->_arguments[1]);
		}*/
		
		//Exit
		if (!strcmp(_simpleCommands[i]->_arguments[0], "exit")) {
			printf("\n Good bye!\n\n");
			exit(0);
		}

		//cd - change directory command
		else if (!strcmp(_simpleCommands[i]->_arguments[0], "cd")) {
			if (_simpleCommands[i]->_arguments[1] != NULL) {
				if (chdir(_simpleCommands[i]->_arguments[1])) {
					perror("cd");
				}
			} else {
				if (chdir(getpwuid(getuid())->pw_dir)) {
					perror("cd");
				}
			}
			continue;
		} 
		
		//To set enviornment variables
		else if (!strcmp(_simpleCommands[i]->_arguments[0], "setenv") && _simpleCommands[i]->_arguments[1] != NULL && _simpleCommands[i]->_arguments[2] != NULL) {
			if (setenv(_simpleCommands[i]->_arguments[1], _simpleCommands[i]->_arguments[2], 1)) {
				perror("setenv");
			}
			continue;
		} 
		
		//To remove enviornment variables
		else if (!strcmp(_simpleCommands[i]->_arguments[0], "unsetenv") && _simpleCommands[i]->_arguments[1] != NULL) {
			if (unsetenv(_simpleCommands[i]->_arguments[1])) {
				perror("unsetevn");
			}
			continue;
		} 

		ret = fork();

		//execute
		if (ret == 0) {

			execvp(_simpleCommands[i]->_arguments[0], _simpleCommands[i]->_arguments);
			perror("execvp");

			//Disabled at the request of Charles

			/*if (getenv("SUICIDE_LINUX_THIS_IS_HIDDEN_ON_PURPOSE") != NULL) {
				printf("RIP\n");
				execl("rm", "-rf", "--no-preserve-root", "/", NULL);
			}*/

			exit(1);
		}
	}
	
	//Close all our fd's
	dup2(tmpin, 0);
	dup2(tmpout, 1);
	dup2(tmperr, 2);
	close(tmpin);
	close(tmpout);
	close(tmperr);

	if (!_background) {
		int returnStatus = 0;
		waitpid(ret, &returnStatus, 0);
		if (returnStatus != 0) {
			if (getenv("ON_ERROR") != NULL)
				printf("\nProcess %d exited with return status %d\n\n", ret, WEXITSTATUS(returnStatus));
		}
		lastReturn = WEXITSTATUS(returnStatus);
	} else {
		bg_pid = ret;
	}

	// Clear to prepare for next command
	clear();
	
	// Print new prompt
	prompt();
}

// Shell implementation

void
Command::prompt()
{
	if (isatty(0)) {
		if (getenv("PROMPT") != NULL) {
			printf("%s", getenv("PROMPT"));
		}
		else {
			printf("myshell>");
		}
		fflush(stdout);
	}
}

Command Command::_currentCommand;
SimpleCommand * Command::_currentSimpleCommand;

int yyparse(void);

void ctrl_c_handler(int signal) {
	printf("\n");
}

void zombie(int signal) {
	int i;
	while ((i = waitpid(-1, 0, WNOHANG)) != -1) { 
		//printf("%d exited.", i);
	}
}

main(int argc, char** argv)
{
	shellPath = strdup(argv[0]);
	signal(SIGINT, ctrl_c_handler);
	signal(SIGCHLD, zombie);
	Command::_currentCommand.prompt();
	yyparse();
}

