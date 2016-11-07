@ test.s

.bss
	.comm buffer, 48	     	@ reserve 48 byte buffer to store name

.data

msg:
	.ascii	"Please enter your name: "
	msgLen = . - msg		@ set len to the length of msg

msg2:
	.ascii	"Hello "	     	@ to print out before printing name
	msg2Len = . - msg2	   	@ set len to the length of msg2

.text

.globl	main				@ call to definition of main

main:

	mov r0, $1			@ Writing to stdout
	ldr r1, =msg			@ Load message into register 1
	ldr r2, =msgLen			@ Load length into register 2
	mov r7, $4			@ Load syscall for writing
	svc $0				@ Call to kernel

	mov r7, $3			@ read syscall
	mov r0, $1			@ reading from stdin
	ldr r1, =buffer			@ loading buffer into register 1
	mov r2, $0x30			@ syscall for reading
	svc $0				@ Call to kernel

	mov r0, $1			@ print msg2
	ldr r1, =msg2			@ load message 2 into register 1
	ldr r2, =msg2Len		@ load message length into register 2
	mov r7, $4			@ Load syscall for writing
	svc $0				@ Call to kernel

	mov r0, $1			@ now print the user input
	ldr r1, =buffer			@ load buffer into register 1
	mov r2, $0x30			@ syscall for reading
	mov r7, $4			@ Load syscall for writing
	svc $0				@ Call to kernel

	mov     %r0, $0                     @ Set exit status to zero
	mov     %r7, $1                     @ Set the syscall to 1 (exit)
	swi     $0                          @ Call to kernel

.end

