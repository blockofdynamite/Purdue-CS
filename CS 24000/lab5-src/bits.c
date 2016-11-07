#include <stdlib.h>
#include <stdio.h>

// It prints the bits in bitmap as 0s and 1s.
void printBits(unsigned int bitmap) {
	unsigned i;
    for (i = 1 << 31; i > 0; i = i / 2)
        (bitmap & i) ? printf("1") : printf("0");
   printf("\n");
   printf("10987654321098765432109876543210\n");
}


// Sets the ith bit in *bitmap with the value in bitValue (0 or 1)
void setBitAt( unsigned int *bitmap, int i, int bitValue ) {
	*bitmap ^= (-bitValue ^ *bitmap) & (1 << i);
}

// It returns the bit value (0 or 1) at bit i
int getBitAt( unsigned int bitmap, unsigned int i) {
	return (bitmap >> i) & 1;
}

// It returns the number of bits with a value "bitValue".
// if bitValue is 0, it returns the number of 0s. If bitValue is 1, it returns the number of 1s.
int countBits( unsigned int bitmap, unsigned int bitValue) {
	int numOfBits = 0;
	for (int i = 0; i < 32; i++) {
		if (((bitmap >> i) & 1) == bitValue) {
			numOfBits++;
		}
	}
	return numOfBits;
}

// It returns the number of largest consecutive 1s in "bitmap".
// Set "*position" to the beginning bit index of the sequence.
int maxContinuousOnes(unsigned int bitmap, int * position) {
	// Add your code here
}


