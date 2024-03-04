	.data
a:
    70
    80
    40
    20
    10
    30
    50
    60
n:
    8
	.text
main:
    load %x0, $n, %x4
    addi %x0, 0, %x10
    jmp loop
loop:
    addi %x0, 0, %x5
    addi %x0, 1, %x6
    beq %x10, %x4, exit
    addi %x10, 1, %x10
    bne %x10, %x4, loops
loops:
    load %x5, $a, %x7
    load %x6, $a, %x8
    beq %x6, %x4, loop
    blt %x7, %x8, save
    addi %x6, 1, %x6
    addi %x5, 1, %x5
    bgt %x7, %x8, loops
    beq %x7, %x8, loops
save:
    store %x7, $a, %x6
    store %x8, $a, %x5
	addi %x5, 1, %x5
    addi %x6, 1, %x6
    beq %x6, %x4, loop
    blt %x6, %x4, loops
exit:
    end