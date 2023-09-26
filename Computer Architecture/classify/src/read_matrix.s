.globl read_matrix

.text
# ==============================================================================
# FUNCTION: Allocates memory and reads in a binary file as a matrix of integers
#
# FILE FORMAT:
#   The first 8 bytes are two 4 byte ints representing the # of rows and columns
#   in the matrix. Every 4 bytes afterwards is an element of the matrix in
#   row-major order.
# Arguments:
#   a0 (char*) is the pointer to string representing the filename
#   a1 (int*)  is a pointer to an integer, we will set it to the number of rows
#   a2 (int*)  is a pointer to an integer, we will set it to the number of columns
# Returns:
#   a0 (int*)  is the pointer to the matrix in memory
# Exceptions:
#   - If malloc returns an error,
#     this function terminates the program with error code 26
#   - If you receive an fopen error or eof,
#     this function terminates the program with error code 27
#   - If you receive an fclose error or eof,
#     this function terminates the program with error code 28
#   - If you receive an fread error or eof,
#     this function terminates the program with error code 29
# ==============================================================================
read_matrix:

    # Prologue
    # Save value of callee registers for calling conventions
    addi sp, sp, -20
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw s4, 16(sp)
    
open_file:

    # Save value of caller registers for calling conventions
    addi sp, sp -16
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw ra, 12(sp)
    
    li a1, 0 # a1: immediate 0 representing permission bits read-only for fopen method
    jal fopen
    
    add s0, x0, a0 # s0: the result returned by fopen method. The file desciptor.
    
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16
    
open_file_exception:
    li t0 -1 # t0: the value of t0 will be used as checking error(exception) for methods including the following code
    bne t0, s0, read_row # Error checking for fopen method
    li a0, 27
    j exit

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

malloc_for_matrix:

    lw s1, 0(a1) # s1: the value of row
    lw s2, 0(a2) # s2: the value of column
    mul s3, s1, s2 # s3: the result value of row multiplies column
    slli s3, s3, 2 # value of s3 * 4
    
    # Save value of caller registers for calling conventions
    addi sp, sp -16
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw ra, 12(sp)
   
    add a0, x0, s3 # a0: The size of memory we want to allocate(in bytes)
    jal malloc
    
    add s4, x0, a0 # s4: result of malloc method.
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16

malloc_matrix_exception:
    li t0, 0
    bne s4, t0, read_matrix_from_file
    li a0, 26
    j exit

read_matrix_from_file:
    # Save value of caller registers for calling conventions
    addi sp, sp -16
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw ra, 12(sp)
    
    add a0, x0, s0 # s0: the file descriptor. 
    add a1, x0, s4 # s4: the pointer to the allocated memories
    add a2, x0, s3 # s3: the result value of row multiplies column * 4, appearing in malloc_for_matrix section.
    jal fread

    add t1, x0, a0 # t1: the result of fread method
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16
    
read_matrix_exception:
    add t0, x0, s3
    beq t1, t0, close_file
    li a0, 29
    j exit

close_file:
    
    # Save value of caller registers for calling conventions
    addi sp, sp -8
    sw a0, 0(sp)
    sw ra, 4(sp)
   
    add a0, x0, s0 # a0: the file descriptor. The first argument of fclose method
    jal fclose
    
    add t2, x0, a0 # t2: result of fclose method.
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw ra, 4(sp)
    addi sp, sp, 8
    
close_file_exception:
    li t0, -1
    bne t2, t0 epilogue
    li a0, 28
    j exit

epilogue:
 
    # Epilogue
    add a0, x0, s4
    
    # Return the origin values of caller registers for calling conventions
    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw s4, 16(sp)
    addi sp, sp, 20
    
    jr ra
