
all: test_mystring test_slist isbalanced countwords mysort bits

test_mystring: mystring.c test_mystring.c
	gcc -g -std=c99 -o test_mystring mystring.c test_mystring.c

test_slist: test_slist.c slist.c
	gcc -g -std=c99 -o test_slist test_slist.c slist.c

isbalanced: isbalanced.c
	gcc -g -std=c99 -o isbalanced isbalanced.c

countwords: countwords.c
	gcc -g -std=c99 -o countwords countwords.c

mysort: mysort.c
	gcc -std=gnu99 -g -o test_mysort test_mysort.c mysort.c

bits: bits.c
	gcc -std=gnu99 -g -o test_bits test_bits.c bits.c

org:
	cp test_mystring test_mystring.org
	cp test_slist test_slist.org
	cp isbalanced isbalanced.org
	cp countwords countwords.org
	cp mysort mysort.org
	cp bits bits.org

clean:
	rm -f test_mystring isbalanced test_slist countwords *.out

