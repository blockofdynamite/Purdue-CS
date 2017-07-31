  .text
.globl fact
  .type  fact, @function

# %xmm0 is our return value, or the product of factorial
# %xmm1 is our current factorial number
# %xmm2 is a scratch, usually 1

# We have to use %xmmX registers because doubles and the internet said so
# since they are required for the 'scalar double' operations
# which the internet also said to use

# factorial number refers to our *current* factorial number
# (the base we are working off as we continue)
# not the one passed in

fact:
  cvtsi2sd   %rdi,  %xmm1  # convert factorial number to a double and store in %xmm1
  movq       $1,    %rsi   # put our sum in rsi
  cvtsi2sdq  %rsi,  %xmm0  # convert 1 and store it in %xmm0

  movq       $0,    %rdi   # store 0 in %rdi
  cvtsi2sdq  %rdi,  %xmm2  # convert 0 to double and store in %xmm2
  ucomisd    %xmm2, %xmm1  # compare 0 and the factorial number we were given
  jne        while         # jump to while loop if fact != 0

  cvtsi2sdq  %rsi,  %xmm0  # convert 1 to a double...
  ret                      # and return it

while:
  movq       $1,    %rsi   # store 1 in %rsi and...
  cvtsi2sdq  %rsi,  %xmm2  # convert it to a double and store it in %xmm2, then...
  ucomisd    %xmm2, %xmm1  # compare 1 with our current factorial number, and...
  jne        continue      # jump to continue if not equal

  ret                      # this will be reached if our factorial number == 1

continue:
  mulsd    %xmm1,   %xmm0  # sum = sum * factorial number
  subsd    %xmm2,   %xmm1  # subtract 1 from our current factorial number
  jmp      while           # start loop again
