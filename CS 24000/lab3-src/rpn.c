
#include <string.h>
#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include <math.h>

#include "rpn.h"
#include "nextword.h"
#include "stack.h"

double rpn_eval(char * fileName, double x) {
	double one, two, three;
	FILE* fd = fopen(fileName, "r");
	char* readWord;

	while ((readWord = nextword(fd)) != NULL) {
		if (!strcmp(readWord, "+")) {
			two = stack_pop();
			three = stack_pop();
			one = two + three;
			stack_push(one);
		} else if (!strcmp(readWord, "-")) {
			two = stack_pop();
			three = stack_pop();
			one = three - two;
			stack_push(one);
		} else if (!strcmp(readWord, "*")) {
			two = stack_pop();
			three = stack_pop();
			one = two * three;
			stack_push(one);
		} else if (!strcmp(readWord, "/")) {
			two = stack_pop();
			three = stack_pop();
			one = three / two;
			stack_push(one);
		} else if (!strcmp(readWord, "sin")) {
			two = stack_pop();
			stack_push(sin(two));
		} else if (!strcmp(readWord, "cos")) {
			two = stack_pop();
			stack_push(cos(two));
		} else if (!strcmp(readWord, "log")) {
			two = stack_pop();
			stack_push(log(two));
		} else if (!strcmp(readWord, "exp")) {
			two = stack_pop();
			stack_push(exp(two));
		} else if (!strcmp(readWord, "pow")) {
			two = stack_pop();
			three = stack_pop();
			stack_push(pow(three, two));
		} else if (!strcmp(readWord, "x")) {
			stack_push(x);
		} else {
			one = atof(readWord);
			stack_push(one);
		}

		if (stack_is_empty()) {
			printf("Stack underflow\n");
			exit(1);
		}

	}

	one = stack_pop();

	if (stack_top() != 0) {
		printf("Elements remain in the stack\n");
		exit(1);
	}

	return one;
}

