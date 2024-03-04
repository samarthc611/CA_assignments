.data
n:
    19
    .text
main:
    load %x0, $n, %x3   
    addi %x0, 0, %x4  
    addi %x0, 1, %x5 
    addi %x0, 2, %x9
	subi %x0, 1, %x10
	store %x4, 65535, 0
	store %x5, 65535, %x10
	jmp loop
loop:
	beq %x9, %x3, exit
	subi %x10, 1, %x10
	add %x4, %x5, %x8
	store %x8, 65535, %x10
	addi %x9, 1, %x9
	add %x0, %x5, %x4
	add %x0, %x8, %x5
	jmp loop
exit:
	end