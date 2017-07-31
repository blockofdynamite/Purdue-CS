  .text
.globl addarray
  .type  addarray, @function

addarray:
    mov  $0, %rdx    # index: %rdx, index = 0
    mov  $0, %rax    # sum: %rax, sum = 0

while:
    cmp  %rdx, %rdi  # if (index < array length)
    jne  add				 # if index is not less than array

		ret              # return if equal

add:
    add  (%rsi), %rax  # sum = sum + array value
    add  $1, %rdx      # index += 1
    add  $4, %rsi      # array pointer = array pointer + 4
    jmp  while				 # go back to the while loop
