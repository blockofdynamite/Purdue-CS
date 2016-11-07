@-------------------------------------------
@This file sorts an array using quick sort.
@The start of the array is passed in at r0
@The number of elements is passed in at r1
@-------------------------------------------
	.text
	.global	quicksort
quicksort:
	cmp		r1, #1				@ check to see if number of elements is 1
	bxle	lr 					@ leave if it is, because already sorted
	stmfd	sp!, {r4, r5, r6, r7, r8, r9, r10, lr} @ push stack
	add	r3, r1, r1, lsr #31 	@ double the number we get, then shift it. 
	mov	r3, r3, asr #1			@ shift
	ldr	lr, [r0, r3, asl #2]	@ load numbers
	sub	r2, r1, #1				@ subtract 1 from the number we get, so that's it's 0-based. 
	mov	r7, r0					@ move the pointer to the beginning to an alternate register
	mov	r8, #1					@ load 1 into the register right after it. 
	b	.qsort 					@ call to the beginning of doing stuff :P
.increment:						@ this function increments r8 by 1
	add		r8, r8, #1			@ i++
.qsort:							@ this is the working part in the for loop "for (i = 0, j = n - 1;; i++, j--) { /* code */ }"
	sub		r4, r8, #1 			@ subtract 1 from the number we're passed
	mov		r5, r7				@ move to r5 for working purposes. 
	ldr		r9, [r7], #4		@ dereference the first number we're given
	cmp		lr, r9				@ if the number we're given is less than the partition, increment
	bgt		.increment
	mov		r3, r2, asl #2		@ adding the numbers 
	add		r6, r0, r3			@ setting up a comparison
	ldr		ip, [r0, r2, asl #2]@ setting up 
	cmp		lr, ip				@ checking a[i] < p
	bge		.loopTwo			@ going to loop
	sub		r3, r3, #4			@ subtract 4 from r3 pointer before going to next loop
	add		r3, r0, r3 			@ add the initial pointer and it before initial loop
.loopOne: 						@ the purpose of this loop is to set up the other side of our partition "while (p < a[j]) j--;"
	sub		r2, r2, #1			@ j--
	mov		r6, r3 				@ move to a working partiton
	ldr		ip, [r3], #-4 		@ compare the number 4 bytes left
	cmp		lr, ip 				@ if they're the same, 
	blt		.loopOne
.loopTwo:						@ this loop helps set up our partition
	cmp		r2, r4 				@ see if p < a[j]
	strgt	ip, [r7, #-4]		@ store on stack if greater than
	strgt	r9, [r6]			@ ' '
	subgt	r2, r2, #1			@ subtract 1 if it's greater than
	bgt		.increment  		@ call increment to increment our number
.finish:
	mov		r6, r1				@ this is setting up the call to the first quicksort call
	mov		r1, r4
	bl		quicksort			@ calling to quicksort with new number of numbers and partition
	rsb		r1, r4, r6			@ this is setting up the second call to quicksort
	mov		r0, r5
	bl		quicksort			@ calling to quicksort with new number of numbers and partition
	ldmfd	sp!, {r4, r5, r6, r7, r8, r9, r10, pc} @pop stack
