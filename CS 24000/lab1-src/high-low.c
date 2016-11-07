#include <stdio.h>
#include <stdbool.h>

int main(int argc, char **argv) {
	
	printf("Welcome to the High Low game...\n");

	while (true) {
		
		printf("Think of a number between 1 and 100 and press press <enter>");
		getchar();

		int min = 1;
		int max = 100;
		
		while (true) {
			int mid = (min + max) / 2;
			printf("Is it higher than %d? (y/n)\n", mid);
			int answer = getchar();
			getchar();
			if (answer == 'y') {
				min = ++mid;
			}
			else if (answer == 'n') {
				max = --mid;
			}
			else {
				printf("Type y or n\n");
			}
			if (max < min){
				printf("\n>>>>>> The number is %d\n\n", min);
				break;
			}
		}
		
		printf("Do you want to continue playing (y/n)?");
		
		int continuePlaying;
		continuePlaying = getchar();
		getchar();
		
		if (continuePlaying == 'y') {
			continue;
		}

		else break;
	}
}

