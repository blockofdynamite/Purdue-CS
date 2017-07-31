	.file	"mystrlen.c"
	.text
.globl mystrlen
	.type	mystrlen, @function

mystrlen:
    movq	$0, %rdx    # len: %rdx, len = 0

while:
    cmpb $0, (%rdi)   # the actual comparison
    jne continue      # jump to continue if not equal

    movq %rdx, %rax   # return the length
    ret

continue:
    addq  $1, %rdx    # len++
    add  	$1, %rdi    # s++
    jmp	while         # go back to the while loop
