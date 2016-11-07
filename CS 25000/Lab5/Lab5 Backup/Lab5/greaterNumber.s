.section        .rodata
promptA:
        .ascii  "a: \000"
promptB:
        .ascii  "b: \000"
readA:
        .ascii "%d\000"
readB:
        .ascii "%d\000"
printLargest:
        .ascii  "largest=%d\n\000"

.section        .data
        .align 2

        .comm   a,4,4
        .comm   b,4,4

        .text

addra:  .word a
addrb:  .word b
addrPromptA:    .word promptA
addrPromptB:    .word promptB
addrReadA:      .word readA
addrReadB:      .word readB
addrPrintLargest:       .word printLargest

.globl main

main:
    stmfd sp!, {fp, lr}

    ldr     r0, addrPromptA
    bl      printf

    ldr     r0, addrReadA
    ldr     r1, addra
    bl      scanf                              

    ldr     r0, addrPromptB
    bl      printf

    ldr     r0, addrReadB
    ldr     r1, addrb
    bl      scanf

    ldr     r0,addra
    ldr     r0, [r0]
    ldr     r1, addrb
    ldr     r1, [r1]

    cmp     r0, r1  
    movgt   r1, r0   @ if (r0 > r1) r1 = r0
    ldr     r0, addrPrintLargest
    bl      printf
