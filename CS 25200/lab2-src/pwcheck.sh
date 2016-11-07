#!/bin/bash

#DO NOT REMOVE THE FOLLOWING TWO LINES
git add $0 >> .local.git.out
git commit -a -m "Lab 2 commit" >> .local.git.out

password=$1 #Argument 1
passLength=$(head -n 1 $password)
length=${#passLength}

if (((length < 6) || (length > 32))) ; then
	echo "Error: Password length invalid."
else
	score=$length
	egrep -q "[#\$\+%@]" $password #check for symbols
	if [ $? -eq 0 ] ; then
		let score=score+5
	fi	
	egrep -q "[0-9]" $password #check for numbers
	if [ $? -eq 0 ] ; then
		let score=score+5
	fi	
	egrep -q "[a-zA-Z]" $password #check for letters
	if [ $? -eq 0 ] ; then
		let score=score+5
	fi	
	egrep -q "([A-Za-z0-9])\\1{1}" $password #check for 2 consecutive of the same letter
	if [ $? -eq 0 ] ; then
		let score=score-10
	fi
	egrep -q "[a-z][a-z][a-z]" $password #check for 3 consecutive lowercase letters
	if [ $? -eq 0 ] ; then
		let score=score-3
	fi	
	egrep -q "[A-Z][A-Z][A-Z]" $password #check for 3 consecutive uppercase letters 
	if [ $? -eq 0 ] ; then
		let score=score-3
	fi	
	egrep -q "[0-9][0-9][0-9]" $password #check for 3 consecutive numbers
	if [ $? -eq 0 ] ; then
		let score=score-3
	fi	
	echo "Password Score: $score"
fi