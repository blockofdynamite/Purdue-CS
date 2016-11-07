@program to print out the product of n numbers
@------------------------------------------
.section        .rodata
promptA:
        .ascii  "Number: \0"
readA:
        .ascii "%d\0"
printLargest:
        .ascii  "Prodcuct = %d\n\0"
@-----------------------------------------
.section        .data
        .align 2

        .comm   a,4,4
@-----------------------------------------
.section        .text

addra:  .word a
addrPromptA:    .word promptA
addrReadA:      .word readA

.globl main

@-----------------------------------------
main:
    push    {lr}                @push the stack

    mov     r7, #1              @initialize to 0
    mov     r8, #1              @''

    b       loop                @go into loop to calculate

loop:
    ldr     r0, addrPromptA     @first parameter for printf
    bl      printf              @call printf

    ldr     r0, addrReadA       @load format to read a number
    ldr     r1, addra           @load storage for number into scanf
    bl      scanf               @call scanf

    ldr     r0, addra           @load the number into r0
    ldr     r0, [r0]            @dereference

    mov     r7, r8              @since mul won't allow two of the same register
    mul     r8, r7, r0          @multiply

    cmp     r0, #-1             @if (n != -1)
    bne     loop                @loop again

    b       done                @call done to print result

done:
    ldr     r0, =printLargest   @sentence to print with %d
    mov     r5, #-1             @move -1 into r5
    mul     r1, r8, r5          @multiply the result by -1 to account for the last multiply and move it to r1
    bl      printf              @call to printf

    pop     {pc}                @pop the stack
