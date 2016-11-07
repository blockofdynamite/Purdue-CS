CS18000 Project 2
=====

Project 2: CLI Parser
Due 2014-10-06 at 11:59pm

1 bonus point per day (maximum 5 pts) for early submission
Prerequisites

    Everything you've learned so far up to Arrays

Learning Objectives

    Command line arguments
    Multi-dimensional arrays
    Error handling

Introduction
Command line

In lab 1, you were introduced to the command line. Since then, you've been using it for every lab so far, like cd, ls, mkdir and turnin.

Wouldn't it be cool if you could write your own simple command line application?! That's why we made this project for you!
Command line argument

Every Java program must have a main method as an entry point, declared as follows:

public static void main(String[] args)

You may wonder why this method is declared with all that “magic” that we've asked you to memorize over and over.

Let's break it down and understand what the incantation for the main method actually does, so you can explain it to your friend who just started learning Java.

public

public is an access level modifier. Access level modifiers determine whether other classes can use a particular field or invoke a particular method.

In this case, public allows the method to be invoked from anywhere, including by the Java virtual machine (JVM), which looks for the main method of the class that you pass on the command line when you invoke the JVM using the command java. For example, the command java YourClassNameHere will invoke the method YourClassNameHere.main.

static

The normal usage of static is to allow the method to be invoked directly without needing to create an object. A static method is a method that belongs to the class, as opposed to instance objects created using new. It is the same for the main method. When the JVM starts up it doesn't have any instances of the class you are trying to run, but it still needs an entry point for your program that it can start executing right away. If you invoke the JVM with the command java YourClassNameHere it will try to run the method YourClassNameHere.main.

void

void is the return type of the method, meaning that the main method does not return anything.

main

The main method is the entry point for your program.

(String[] args)

The main method accepts a single argument: an array of references to String objects. This array is the mechanism through which the run-time system passes command line arguments to your application. Consider:

java YourClasNameHere arg0 arg1

This will result in the args array having two elements: the first element args[0] holds a reference to a String object “arg0” and the second element args[1] holds a reference to a String object “arg1”.

Ahh, you got it? Now, let's do a project using this String[] args so you can really understand it.
Problem

You are going to write a simple Command Line Interface (CLI) parser in Java.

When you run your program from the command line, you will pass arguments along with it. Then, the program should be able to interpret the arguments and do what is asked for.

$ java MyCLIParser arg0 arg1 arg2 ...

The arguments are separated by spaces (whitespace). The strings arg0, arg1, arg2, … are saved into the array referred to by the String[] args parameter of the main method. You can use args.length to obtain the number of arguments.
Command Features

You must implement the following commands:
-help

$ java MyCLIParser -help

The help command will print, to System.out, the list of commands and instructions that you have implemented.
-add

$ java MyCLIParser -add num1 num2 ...

The add command will take one or more integer arguments. Then it will add up all the integers and print the value. For example:

$ java MyCLIParser -add 10 5 3
18
$

Error handling:

    If there is no argument after -add, print out an error message “Argument count mismatch”.
    If any argument after -add is not an integer, print out an error message “Argument type mismatch”.

-sub

$ java MyCLIParser -sub num1 num2

The sub command will take two integer arguments. Then it will subtract the second integer from the first integer and print the value. For example:

$ java MyCLIParser -sub 10 2
8
$

Error handling:

    If the number of arguments after -sub is not two, print out an error message “Argument count mismatch”.
    If any argument after -sub is not an integer, print out an error message “Argument type mismatch”.

-mul

$ java MyCLIParser -mul num1 num2 ...

The mul command will take one or more integer arguments. Then it will multiply all the integers and print the value. For example:

$ java MyCLIParser -mul 10 2 5
100
$

Error handling:

    If there is no argument after -mul, print out an error message “Argument count mismatch”.
    If any argument after -mul is not an integer, print out an error message “Argument type mismatch”.

-div

$ java MyCLIParser -div num1 num2

The div command will take two integer arguments. Then it will divide the first integer by the second integer and print the double value result. For example:

$ java MyCLIParser -div 5 2
2.50
$

Error handling:

    If the number of arguments after -div is not two, print out an error message “Argument count mismatch”.
    If any argument after -div is not an integer, print out an error message “Argument type mismatch”.
    If the second integer is 0, print out “undefined”.

-stats

$ java MyCLIParser -stats num1 num2 ...

The -stats command will take one or more integer arguments. Then it will find and print out total, maximum, minimum, average values, in that order and each on a separate line.
NOTE: average is double value.

$ java MyCLIParser -stats 10 3 25 30 2 5 7
82
30
2
11.71
$

Error handling:

    If there is no argument after -stats, print out an error message “Argument count mismatch”.
    If any argument after -stats is not an integer, print out an error message “Argument type mismatch”.

-table

$ java MyCLIParser -table num1

The -table command will take one integer value argument. It will generate a multiplication table and store the table in a 2-dimensional array. It will then add the argument to each value in the table. The starting table will be the multiplication table from 0 to 9:

     0     0     0     0     0     0     0     0     0     0
     0     1     2     3     4     5     6     7     8     9
     0     2     4     6     8    10    12    14    16    18
     0     3     6     9    12    15    18    21    24    27
     0     4     8    12    16    20    24    28    32    36
     0     5    10    15    20    25    30    35    40    45
     0     6    12    18    24    30    36    42    48    54
     0     7    14    21    28    35    42    49    56    63
     0     8    16    24    32    40    48    56    64    72
     0     9    18    27    36    45    54    63    72    81

Say that the argument we pass to -table is 5. After adding 5 to each value in our table, the resulting table looks like this:

     5     5     5     5     5     5     5     5     5     5
     5     6     7     8     9    10    11    12    13    14
     5     7     9    11    13    15    17    19    21    23
     5     8    11    14    17    20    23    26    29    32
     5     9    13    17    21    25    29    33    37    41
     5    10    15    20    25    30    35    40    45    50
     5    11    17    23    29    35    41    47    53    59
     5    12    19    26    33    40    47    54    61    68
     5    13    21    29    37    45    53    61    69    77
     5    14    23    32    41    50    59    68    77    86

The -table command should only print the table after adding the argument:

$ java MyCLIParser -table 7
     7     7     7     7     7     7     7     7     7     7
     7     8     9    10    11    12    13    14    15    16
     7     9    11    13    15    17    19    21    23    25
     7    10    13    16    19    22    25    28    31    34
     7    11    15    19    23    27    31    35    39    43
     7    12    17    22    27    32    37    42    47    52
     7    13    19    25    31    37    43    49    55    61
     7    14    21    28    35    42    49    56    63    70
     7    15    23    31    39    47    55    63    71    79
     7    16    25    34    43    52    61    70    79    88
$
     

Each column in the table is 6 spaces wide.

Error handling:

    If there is no argument after -table, print out an error message “Argument count mismatch”.
    If any argument after -table is not an integer, print out an error message “Argument type mismatch”.

Coding

Only after you have understood the problem and made a plan for solving it, should you begin coding.

Printing floating point

When you are printing out a double value, instead of System.out.println(value), use System.out.printf(“%.2f”, value);

    %f indicates that you are printing out a floating point.
    .2 indicates that you only want to print out 2 decimal points of the double value.

Error handling

We will eventually learn how to use exceptions in Java to ensure that our program still works when the user provides bad input (like providing a string instead of an integer). For now, however, we will check our input manually. Each of the CLI Parser functions expects to receive integer arguments, so to perform input validation you will need a function that can check a string and see whether it contains an integer. One approach is to make use of the java.util.Scanner class to check this. Consult the Java API documentation for Scanner for the methods hasNextInt() and nextInt().

Once you have a function that can check whether an arbitrary string represents an integer, loop through your inputs and check each one. Perform this check before you try to use the arguments in any computations, because the computations could cause an error and crash your program. If you find an argument that is not an integer, print your error message and return.

$ java MyCLIParser -add 3 cat
Argument type mismatch
$

Two-dimensional Array

Say that we create a 3×3 array:

int[][] array = new int[3][3];

We can then fill our array by specifying which row and which column we wish to fill:

array[0][1] = 4

Now, our array looks like this:

0 4 0
0 0 0
0 0 0

It would be overly verbose to fill our array using individual statements like this, one on each line, for each element of the array. Instead, try using two nested for loops to fill the array. You can also use two for loops to add the user's argument to each entry in the array, and to print the array.

To ensure that each column of the array prints out with the same width, use this formatting string:

System.out.format("%06d", array[i][j]);

Set Up

$ mkdir -p ~/cs180/project2
$ cd ~/cs180/project2

Requirements

    The class must be named MyCLIParser.
    You must implement all the features listed.
    You must handle all the error cases listed.
    You should write your own version of -help message describing each feature briefly.

Testing

You must write and submit JUnit test cases for your MyCLIParser class. You will create your own JUnit test called MYCLIParserTest.java and implement it based on what you have learned from Project 1.

For this project, you will need to create a test method for each feature. (e.g., testAdd())
Skeleton Code

To help you get started, we will provide you a skeleton code for MyCLIParser.java with main method and methods for each feature. We will also provide a skeleton for MyCLIParserTest.java that shows you how to capture command line output in a Junit test.
Rubric

    25% cli parsing for add and mul commands
    25% cli parsing for sub and div commands
    25% cli parsing for stats command
    25% cli parsing for table command
    +1% bonus for early submission (per day, max 5)

Submission Instructions

Compress MyCLIParser.java and MyCLIParserTest.java into a single .zip file: Compress both and only those two files, together. Submit the .zip file to WebCAT.

