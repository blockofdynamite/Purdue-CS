.data
    .comm array 10000, 32
.text
    readInt:  .string "%d"
    printInt: .string "%d\n"
    sorted:   .string "Sorted:\n"

.globl main

main:
    mov  $0, %r13d    # store 0 in our length to begin with
    mov  $0, %r14d    # store 0 in our index of where we are

read:
    mov  $array, %edx # move the array constant into %edx
    mov  %r13d, %ecx  # move length into %ecx
    imul $32, %ecx    # multiply the length by 4
    add  %ecx, %edx   # add that to the array constant pointer

    jmp readInteger   # go get our int

readInteger:
    mov  $readInt, %edi # put our pattern in the first arg
    mov  %edx, %esi     # the array pointer for where we want to store
    mov  $0, %eax       # zero out the return register
    call scanf          # go get our int from scanf!

    jmp keepGoing       # continue reading?

keepGoing:
    cmp $-1, %eax       # if the number we get is -1 (or EOF)...
    je sortArray        # jump to sorting the array

    add  $1, %r13d      # increment the length...
    jmp  read           # then jump back to reading the next line/number

sortArray:
    mov  $0, %r14d    # set i equal to 0
    mov  %r13d, %r15d # move length to another working register
    sub  $1, %r15d    # subtract 1 from the length

outerLoop:
    cmp  %r14d, %r15d # if i equal to the length
    je  printArray    # jump to printing

    mov  $0, %r8d     # i = 0
    mov  %r13d, %r9d  # put length in %r9d
    sub  %r14d, %r9d  # subtract the index from length
    sub  $1, %r9d     # subtract 1 from (length - index)

innerLoop:
    cmp  %r8d, %r9d   # start of inner loop, using j, setting equal to i
    je  incrementOuterLoopCounter # increment i

    mov  $array, %edx # move the array constant into %edx
    mov  %r8d, %ecx   # move j into %ecx
    imul $32, %ecx    # multiply j by 4
    add  %ecx, %edx   # add j to the array pointer

    mov  %edx, %esi   # add 4 to the array pointer in a separate register...
    add  $32, %esi    # then add 4

    mov  (%edx), %eax # value at j
    mov  (%esi), %ebx # value at j + 1

    cmp  %eax, %ebx   # if (array[i] > array[i+1])
    jge  incrementInnerLoopCounter # increment j

swap:
    xchg  %edx, %esi  # swap the adresses
    mov  (%edx), %edx # store the actual values
    mov  (%esi), %esi # ''

    mov  %edx, array+(%ecx) # put the second value into first spot
    add  $32, %ecx           # move up an index
    mov  %esi, array+(%ecx) # put the first value into second spot

incrementInnerLoopCounter:
    add  $1, %r8d     # increment j
    jmp  innerLoop # jump back to inner loop

incrementOuterLoopCounter:
    add  $1, %r14d    # increment i
    jmp  outerLoop # jump back to outer loop

printArray:
    mov	$0, %r14d     # 0 out our index
    mov	$sorted, %edi # put our String constant in first arg
    mov	$0, %eax      # 0 out return register
    call printf      # print

    mov  $0, %r14d    # zero out our current index array
    jmp  printStart   # go to the first print

printStart:
    cmp  %r14d, %r13d # if index is not equal to the length
    jne  print        # we go and keep printing

    ret               # return if we're at the end of the array

print:
    mov  $array, %edx # move the array constant into %edx
    mov  %r14d, %ecx  # put our current index into %ecx
    imul $32, %ecx     # multiply the index by 4
    add  %ecx, %edx   # add the index*4 to the array pointer

    mov  $printInt, %edi # put our pattern in the first arg
    mov  %edx, %esi      # the array pointer for where we want to grab
    mov  (%esi), %esi    # put the value in the register that we want to print
    mov  $0, %eax        # zero out the return register
    call printf          # print!

    add  $1, %r14d       # increment index that we're at
    jmp  printStart      # print next number
