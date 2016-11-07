@-------------------------------------------
@This file sorts an array using quick sort.
@The start of the array is passed in at r0
@The number of elements is passed in at r1
@-------------------------------------------
.global bubble
.equ datum_size, 4
.text

bubble:
	stmfd 	sp!, {r4, r5, r6, r7, r8, lr}	@ save variables to stack
	cmp		r1, #1							@ > 1
	ble		end_outer

    sub		r5, r1, #1						@ n-1 comparisons
    mov		r4, r0							@ initialize 
	mov		r6, #0

loop:
	ldr		r7, [r4], #datum_size			@ load one
	ldr		r8, [r4]						@ next one
	cmp		r7, r8							@ compare
	ble		no_swap							@ second > first

	mov		r6, #1							@ set keepGoing boolean
	sub		r4, r4, #datum_size				@ first element
	swp		r8, r8, [r4]					@ exchange value
	str		r8, [r4, #datum_size]!			@ store new r8

no_swap:
	subs 	r5, r5, #1						@ decrement counter
	bne		loop 							@ restart loop

end_inner:
	cmp		r6, #0							@ check keepGoing flag
	beq		end_outer						@ and leave if not set

	mov		r6, #0							@ clear keepGoing flag 
	mov		r4, r0							@ reset pointer
    sub 	r5, r1, #1						@ reset counter
	b		loop							@ start another iteration

end_outer:
    ldmfd   sp!, {r4, r5, r6, r7, r8, pc}	@ restore state

