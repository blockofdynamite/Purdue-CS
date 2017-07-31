		.text
.globl add
		.type	add, @function
add:								# a=%rdi b=%rsi c=%rdx ret=%rax
		movq %rdi, %rax # a into %rax (the return register)
		addq %rsi, %rax # a = a + b
		addq %rdx, %rax	# a = a + c
		ret							# return a (which is in %rax)
