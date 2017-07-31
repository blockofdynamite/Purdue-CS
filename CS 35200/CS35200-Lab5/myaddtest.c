
#include <stdio.h>
#include <assert.h>

int add(int a, int b, int c);

int main()
{
	int a = 9;
	int b = 4;
	int c = 2;

	assert(a+b+c==add(a,b,c));
	printf("myadd test passed.\n");
	return 0;
}
