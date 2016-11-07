#include <stdio.h>

int countOnes(unsigned int value) {
	int n = (sizeof(value) << 3);
	int numOnes = 0;
	for (int i = 0; i < n; i++) {
		unsigned int mask = (1 << i);
		if ((value & mask) != 0) {
			numOnes++;
		}
	}
	return numOnes;
}

int main() {
	printf("countOnes(0x30303)=%d\n", countOnes(0x30303));
}
