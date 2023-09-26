.globl write_matrix

.text
# ==============================================================================
# FUNCTION: Writes a matrix of integers into a binary file
# FILE FORMAT:
#   The first 8 bytes of the file will be two 4 byte ints representing the
#   numbers of rows and columns respectively. Every 4 bytes thereafter is an
#   element of the matrix in row-major order.
# Arguments:
#   a0 (char*) is the pointer to string representing the filename
#   a1 (int*)  is the pointer to the start of the matrix in memory
#   a2 (int)   is the number of rows in the matrix
#   a3 (int)   is the number of columns in the matrix
# Returns:
#   None
# Exceptions:
#   - If you receive an fopen error or eof,
#     this function terminates the program with error code 27
#   - If you receive an fclose error or eof,
#     this function terminates the program with error code 28
#   - If you receive an fwrite error or eof,
#     this function terminates the program with error code 30
# ==============================================================================
write_matrix:

    # Prologue
    # Saved values of callee registers for calling conventions
    addi sp, sp -12
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)

file_open:
    # Saved values of caller registers for calling conventions
    addi sp, sp, -20
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw a3, 12(sp)
    sw ra, 16(sp)
    
    li a1, 1 # The second argument of fopen method. Permission bit indicating write-only.
    jal fopen
    
    mv s0, a0 # s0: the file descriptor
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw a3, 12(sp)
    lw ra, 16(sp)
    addi, sp, sp, 20

file_open_exception:
    li t0, -1
    bne t0, s0, malloc_row_column
    li a0, 27
    j exit

malloc_row_column:
    # Saved values of caller registers for calling conventions
    addi sp, sp, -20
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw a3, 12(sp)
    sw ra, 16(sp)
    
    li a0, 8 # The first argument of malloc method. The total size of bytes to let malloc allocate
    jal malloc
    
    mv s2, a0 # s2: the pointer to buffer storing values of row and column
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw a3, 12(sp)
    lw ra, 16(sp)
    addi, sp, sp, 20

malloc_row_column_exception:
    li t0, 0
    bne t0, s2, write_row_column
    li a0, 26
    j exit

write_row_column:

    # Store values of row and column into memory with a pointer s2 points to them
    sw a2, 0(s2)
    sw a3, 4(s2)
    
    # Saved values of caller registers for calling conventions
    addi sp, sp, -20
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw a3, 12(sp)
    sw ra, 16(sp)
    
    mv a0, s0 # The first argument of fwrite method. The file desciptor.
    mv a1, s2 # The second argument. The pointer pointing to a block of memory storing values of row and column.
    li a2, 2 # The third argument. The number of elements to write to the file.
    li a3, 4 # The fourth argument. The size of each element.
    jal fwrite
    
    mv t1, a0 # t1: the result of fwrite method
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw a3, 12(sp)
    lw ra, 16(sp)
    addi, sp, sp, 20

write_row_column_exception:
    li t0, 2
    beq t1, t0 write_matrix_from_file
    li a0, 30
    j exit
    
write_matrix_from_file:

    mul s1, a2, a3 # s1: result of row * column
    
    # Saved values of caller registers for calling conventions
    addi sp, sp, -20
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw a3, 12(sp)
    sw ra, 16(sp)
    
    mv a0, s0 # The first argument of fwrite method. The file desciptor.
    mv a2, s1 # The third argument. The number of elements to write to the file.
    li a3, 4 # The fourth argument. The size of each element.
    jal fwrite
    
    mv t1, a0 # t1: the result of fwrite method
    # Return the origin values of caller registers for calling conventions
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw a3, 12(sp)
    lw ra, 16(sp)
    addi, sp, sp, 20

write_matrix_exception:
    mv t0, s1
    beq t1, s1 file_close
    li a0, 30
    j exit

file_close:
    
    # Saved values of caller registers for calling conventions
    addi sp, sp, -4
    sw ra, 0(sp)
    
    mv a0, s0 # The first argument of fwrite method. The file desciptor.
    jal fclose
    
    mv t2, a0 # t2: the result of fclose method
    # Return the origin values of caller registers for calling conventions
    lw ra, 0(sp)
    addi, sp, sp, 4

file_close_exception:
    li t0, -1
    bne t2, t0, epilogue
    li a0, 28
    j exit

epilogue:

    # Epilogue
    # Return the original values of callee registers back for calling conventions
    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    addi sp, sp, 12

    jr ra
