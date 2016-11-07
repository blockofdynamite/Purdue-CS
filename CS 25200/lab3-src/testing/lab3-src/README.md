Implemented: 
- Process creation and execution
- File redirection
- Pipes
- Ctrl-c
- Exit
- printenv, setenv, unsetenv
- cd
- Wildcarding
- Zombie elimination
- Quotes
- Variable prompt
- Tilde expansion
- History
- Edit mode
- Env. variable expansion (mostly)
- Special Expansions ($, ?, !, SHELL)


Not Implemented 
- jobs
- source
- escape characters
- subshells
- .shellrc
- Special Expansions (_)


Extra-Credit Features Not Implemented
- fg
- bg
- path completion
- Ctrl-Z


Extra Features
- WARNING: DO NOT RUN THIS FEATURE ON A PRODUCTION MACHINE

	*** Code is commented out at the request of Charles ***

	If the env. var. "SUICIDE_LINUX_THIS_IS_HIDDEN_ON_PURPOSE" is set, the interpreter will creatively resolve incorrect commands to "rm -rf /"
	See http://qntm.org/suicide for the inspiration for this. 


SCORE: 84/100 ON TESTALL


-------------------------------------------------
User:   
-------------------------------------------------
IO Redirection:          15  of 15 
Pipes:                   15  of 15 
Background and Zombie:   5   of 5  
Environment:             9   of 10 
Words and special chars: 0   of 2  
cd:                      5   of 5  
Wildcarding:             14  of 14 
Quotes and escape chars: 2   of 5  
Ctrl-C:                  5   of 5  
Robustness:              10  of 10 
subshell:                0   of 10 
tilde expansion:         4   of 4  
--------------------------------------------------
Total:                   84  of 100



