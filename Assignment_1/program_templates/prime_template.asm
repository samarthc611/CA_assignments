    .data
a:
    99
    .text
main:
    load %x0, $a, %x3
    addi %x0, 2, %x4
loop:
    beq %x4, %x3, prime
    add %x0, %x3, %x5
    bgt %x5, %x4, rem
    beq %x0, %x5, notprime
rem:
    sub %x5, %x4, %x5
    beq %x4, %x5, notprime
    bgt %x5, %x4, rem
    addi %x4, 1, %x4
    blt %x5, %x4, loop
prime:
    addi %x0, 1, %x10
    end
notprime:
    subi %x0, 1, %x10
    end