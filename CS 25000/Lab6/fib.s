@program to print out the nth fibonnaci number
@------------------------------------------
.section        .rodata
promptA:
    .ascii  "number: \0"        @prompt for number
readA:
    .ascii  "%d\0"              @scan for number
printLargest:
    .ascii  "fib = %d\n\0"      @print out number
@-----------------------------------------
.section        .data
    .align  4

    .comm   a,4,4              @4 byte word to store number
@-----------------------------------------
.section        .text

addra:  .word a                 @address of a
addrPromptA:    .word promptA   @address of prompt
addrReadA:      .word readA     @address of reader

.globl main

@-----------------------------------------
main:
    push    {lr}                @push  the return address  on the stack

    ldr     r0, addrPromptA     @first parameter for printf
    bl      printf              @call printf

    ldr     r0, addrReadA       @load format to read a number
    ldr     r1, addra           @load storage for number into scanf
    bl      scanf               @call scanf

    ldr     r0, addra           @load the number into first parameter
    ldr     r0, [r0]            @dereference
    mov     r1, #0              @past value
    mov     r2, #1              @sum

    b       fib                 @go into loop to calculate

fib:
    cmp     r0, #0              @if (n == 0)
    beq     done                @goto done
    add     r3, r2, r1          @r3 = r2 + r1
    mov     r1, r3              @past value = calculated value
    mov     r2, r3              @sum = calculated value
    sub     r0, r0, #1          @n -= 1
    b       fib

done:
    ldr     r0, =printLargest   @sentence to print with %d
    mov     r1, r2              @load number into memory
    bl      printf              @call to printf

    pop     {pc}                @pop the program counter off the stack
