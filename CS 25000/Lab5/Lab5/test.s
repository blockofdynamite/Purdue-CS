@ hello.s
.data
	intro: .asciz "Hello, "	 	    @ read a string
	len = . - intro
	scan_pattern: .asciz "%s"	    @ scan pattern to be read by the scanf function
	storage: .space 8		    @ assign 100 bytes to be safe

	msg: .ascii "Please enter your name: "   @ save this as an ascii string.
	len = . - msg                       @ set len to the length of msg.

.text         	                            @ beginning of the program

.global main         	                    @ call to defininition of _start
.global scanf
.global printf

main:

	ldr	%r0, =msg
	bl	printf

	ldr	%r0, address_of_scan_patt
	ldr	%r1, address_of_storage
	bl	scanf

	ldr	%r0, =intro
	bl	printf

	mov     %r0, $0                     @ Set exit status to zero
	mov     %r7, $1                     @ Set the syscall to 1 (exit)
	svc     $0                          @ Call to kernel

address_of_scan_patt: .word scan_pattern
address_of_intro: .word intro
address_of_storage: .word storage
