    .data
a:
    4525525
    .text
main:
    load %x0, $a, %x3
    add %x0, %x3, %x4 
    add %x0, %x3, %x8
    addi %x0, 0, %x5
    addi %x0, 0, %x6
    addi %x0, 0, %x9
    addi %x0, 0, %x11
    addi %x0, 0, %x12
    addi %x0, 65535, %x13
    addi %x0, 10, %x7
    blt %x3, %x7, notpal
countdigits:
    bgt %x4, %x7, rem
    blt %x4, %x7, countdigits2
countdigits2:
    store %x4, 65535, %x6
    addi %x9, 1, %x9
    subi %x6, 1, %x6
    divi %x8, 10, %x8 
    addi %x8, 0, %x4 
    bgt %x4, 1, countdigits
    subi %x9, 1, %x9
    add %x0, %x9, %x10
    addi %x0, 65535, %x11
    sub %x13, %x9, %x12
    jmp palcheck
rem:
    subi %x4, 10, %x4
    blt %x4, %x7, countdigits2  
    beq %x4, %x7, countdigits2  
    bgt %x4, %x7, rem 
palcheck:
    load %x11, 0, %x14
    load %x12, 0, %x15
    subi %x11, 1, %x11
    addi %x12, 1, %x12
    bne %x14, %x15, notpal
    bgt %x12, %11, pal
    beq %x12, %x11, pal
    beq %x14, %x15, palcheck
pal:
    addi %x0, 1, %x10     
    end
notpal:
    subi %x0, 1, %x10     
    end