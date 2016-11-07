@strlen
@------------------------------------------
.section        .rodata
promptA:
        .ascii  "string: \0"	@prompt for string
readA:
        .ascii "%s\0"			@input for scanf
printLength:
        .ascii  "Length = %d\n\0" @to print out length
@-----------------------------------------
.section        .data
        .align 4				@4 byte words

        .comm   a,50,50			@storage for up to a 50 byte string
@-----------------------------------------
.section        .text

addra:  .word a 				@address for the string to store
addrPromptA:    .word promptA 	@address for prompt
addrReadA:      .word readA 	@address for input to scanf
addrPrintLength:.word printLength @address for print at end

.globl main

@-----------------------------------------
main:
    push    {lr}                @push the stack

    ldr     r0, addrPromptA     @first parameter for printf
    bl      printf              @call printf

    ldr     r0, addrReadA       @load format to read a number
    ldr     r1, addra           @load storage for number into scanf
    bl      scanf               @call scanf

    ldr     r0, addra           @load the number into first parameter
    mov		r1, #0				@load 0 into r1
    b       loop                @go into loop to calculate

loop:
    ldrb	r2, [r0], #1		@r2 = *(r0++)
	add 	r1, r1, #1 			@rt += 1
	cmp		r2, #0	 			@if (r2 != NULL) 
    bne		loop				@loop again

done:
    ldr     r0, addrPrintLength @sentence to print with %d
    add 	r1, r1, #-1			@remove the newline character count 
    bl      printf              @call to printf

    pop     {pc}                @pop the stack
