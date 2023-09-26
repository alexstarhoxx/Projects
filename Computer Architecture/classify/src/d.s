read_row:
    # Save value of caller registers for calling conventions
    addi sp, sp -16
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw ra, 12(sp)
    
    add a0, x0, s0 # a0: The file desrciptor. The first argument of fread method, the result of fopen function.
    li a2, 4 # a3: The number of bytes to read from file. The third argument of fread method
    jal fread
    
    add t1, x0, a0 # the result of fread method. t1 will ALWAYS be the result of fread method in the following code
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16

read_row_exception:
    li t0, 4
    beq t1, t0, read_column
    li a0, 29
    j exit
    
read_column:
    # Save value of caller registers for calling conventions
    addi sp, sp -16
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw ra, 12(sp)
    
    add a0, x0, s0 # a0: The file desrciptor. The first argument of fread method, the result of fopen function.
    add a1, x0, a2 # a1: The pointer to an integer representing number of column. The second argument of fread method.
    li a2, 4 # a3: The number of bytes to read from file. The third argument of fread method
    jal fread
    
    add t1, x0, a0 # the result of fread method. t1 will ALWAYS be the result of fread method in the following code
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16

read_column_exception:
    li t0, 4
    beq t1, t0, malloc_for_matrix
    li a0, 29
    j exit    