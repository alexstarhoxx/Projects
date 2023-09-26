.globl classify

.text
# =====================================
# COMMAND LINE ARGUMENTS
# =====================================
# Args:
#   a0 (int)        argc
#   a1 (char**)     argv
#   a1[1] (char*)   pointer to the filepath string of m0
#   a1[2] (char*)   pointer to the filepath string of m1
#   a1[3] (char*)   pointer to the filepath string of input matrix
#   a1[4] (char*)   pointer to the filepath string of output file
#   a2 (int)        silent mode, if this is 1, you should not print
#                   anything. Otherwise, you should print the
#                   classification and a newline.
# Returns:
#   a0 (int)        Classification
# Exceptions:
#   - If there are an incorrect number of command line args,
#     this function terminates the program with exit code 31
#   - If malloc fails, this function terminates the program with exit code 26
#
# Usage:
#   main.s <M0_PATH> <M1_PATH> <INPUT_PATH> <OUTPUT_PATH>
classify:
    addi sp, sp, -48
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw s4, 16(sp)
    sw s5, 20(sp)
    sw s6, 24(sp)
    sw s7, 28(sp)
    sw s8, 32(sp)
    sw s9, 36(sp)
    sw s10, 40(sp)
    sw s11, 44(sp)

incorrect_number_of_commands_check:
    li t0, 5
    beq t0, a0, malloc_row_m0
    li a0, 31
    j exit

# Read pretrained m0
malloc_row_m0:
   
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)

    li a0, 4
    jal malloc

    mv s0, a0 # s0 is the pointer to the integer representing row of m0
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12

malloc_exception_m0_row:
    li t0, 0
    bne t0, s0, malloc_column_m0
    li a0, 26
    j exit

malloc_column_m0:
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)

    li a0, 4
    jal malloc

    mv s1, a0 # s1 is the pointer to the integer representing column of m0
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12
    
malloc_exception_m0_column:
    li t0, 0
    bne t0, s1, read_m0
    li a0, 26
    j exit

read_m0:
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)
    
    lw a0, 4(a1) # the pointer to the name of the first matrix m0
    mv a1, s0 # the pointer to an integer representing number of rows of m0
    mv a2, s1 # the pointer to an integer representing number of columns of m0
    jal read_matrix
    
    mv s6, a0 # s6: the pointer to m0 matrix in the memory
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12
    
# Read pretrained m1
malloc_row_m1:
   
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)

    li a0, 4
    jal malloc

    mv s2, a0 # s2 is the pointer to the integer representing row of m1
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12

malloc_exception_m1_row:
    li t0, 0
    bne t0, s2, malloc_column_m1
    li a0, 26
    j exit

malloc_column_m1:
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)

    li a0, 4
    jal malloc

    mv s3, a0 # s3 is the pointer to the integer representing column of m1
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12

malloc_exception_m1_column:
    li t0, 0
    bne t0, s3, read_m1
    li a0, 26
    j exit

read_m1:
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)
    
    lw a0, 8(a1)
    mv a1, s2
    mv a2, s3
    jal read_matrix
    
    mv s7, a0 # s7: the pointer to m1 matrix in the memory
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12

# Read input matrix
malloc_row_input:
   
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)

    li a0, 4
    jal malloc

    mv s4, a0 # s4 is the pointer to the integer representing row of input
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12

malloc_exception_input_row:
    li t0, 0
    bne t0, s4, malloc_column_input
    li a0, 26
    j exit

malloc_column_input:
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)

    li a0, 4
    jal malloc

    mv s5, a0 # s5 is the pointer to the integer representing column of input
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12

malloc_exception_input_column:
    li t0, 0
    bne t0, s5, read_input
    li a0, 26
    j exit

read_input:
    addi sp, sp, -12
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw ra, 8(sp)
    
    lw a0, 12(a1)
    mv a1, s4
    mv a2, s5
    jal read_matrix
    
    mv s8, a0 # s8: the pointer to input matrix in the memory
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12

# Compute h = matmul(m0, input)
malloc_matmul_h:
    lw t1, 0(s0) # n of first matrix, m0. This will also be number of rows of h matrix.
    lw t2, 0(s5) # k of second matrix, input. This will also be number of columns of h matrix.
    mul t1, t1, t2 # t1: n * k, also the size of matrix h
    mv t2, t1 # t2 = t1 = n * k now
    slli t2, t2, 2 # The number of space to be allocated: t2 = n * k * 4 

    addi sp, sp, -16
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw t1, 8(sp) # Saved the value of t1 because value of t1 will be used in relu method
    sw ra, 12(sp)

    mv a0, t2
    jal malloc

    mv s9, a0 # s9 will be the pointer to the result of matmul(m0, input), a.k.a. h matrix
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw t1, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16
    
malloc_matmul_h_exception:
    li t0, 0
    bne t0, s9, matmul_h
    li a0, 26
    j exit
    
matmul_h:
    addi sp, sp, -16
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw t1, 8(sp)
    sw ra, 12(sp)

    mv a0, s6 # pointer to m0 in memory
    lw a1, 0(s0) # number of rows of m0
    lw a2, 0(s1) # number of columns of m0
    mv a3, s8 # pointer to input in memory
    lw a4, 0(s4) # number of rows of input
    lw a5, 0(s5) # number of columns of input
    mv a6, s9 # pointer to result of matmul(m0, input), a.k.a. h matrix
    jal matmul

    lw a1, 0(sp)
    lw a2, 4(sp)
    lw t1, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16

# Compute h = relu(h)
relu_matmul_of_m0_input:
    addi sp, sp, -16
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw t1, 8(sp)
    sw ra, 12(sp)

    mv a0, s9 # pointer to h matrix, the result of matmul(m0, input)
    mv a1, t1 # value of n * k(row * column) of h matrix
    jal relu
    
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw t1, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16

# Compute o = matmul(m1, h)
malloc_matmul_o:
    lw t3, 0(s2) # n of first matrix, m1
    lw t4, 0(s5) # k of second matrix, h
    mul t3, t3, t4 # n * k, also the size of matrix o
    mv t4, t3 # t4 = t3 = n * k now
    slli t4, t4, 2 # The number of space to be allocated: t1 = n * k * 4 

    addi sp, sp, -20
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw t1, 8(sp) # Saved the value of t1 because value of t1 will be used in freeing memory
    sw t3, 12(sp) # Saved the value of t3 because value of t3 will be used in freeing memory
    sw ra, 16(sp)

    mv a0, t4
    jal malloc

    mv s10, a0 # s10 will be the pointer to the result of o, matmul(m1, h)
    lw a1, 0(sp)
    lw a2, 4(sp)
    lw t1, 8(sp)
    lw t3, 12(sp)
    lw ra, 16(sp)
    addi sp, sp, 20
    
malloc_matmul_o_exception:
    li t0, 0
    bne t0, s10, matmul_o
    li a0, 26
    j exit
    
matmul_o:
    addi sp, sp, -20
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw t1, 8(sp)
    sw t3, 12(sp)
    sw ra, 16(sp)

    mv a0, s7 # pointer to m1 in memory
    lw a1, 0(s2) # number of rows of m1
    lw a2, 0(s3) # number of columns of m1
    mv a3, s9 # pointer to h(result of matmul(m0, input)) in memory
    lw a4, 0(s0) # number of rows of h, which equals to number of rows of m0
    lw a5, 0(s5) # number of columns of h, which equals to number of columns of input
    mv a6, s10 # pointer to result of o, a.k.a matmul(m1, h)
    jal matmul

    lw a1, 0(sp)
    lw a2, 4(sp)
    lw t1, 8(sp)
    lw t3, 12(sp)
    lw ra, 16(sp)
    addi sp, sp, 20

    # Write output matrix o
write_matrix_o:
    addi sp, sp, -20
    sw a1, 0(sp)
    sw a2, 4(sp)
    sw t1, 8(sp)
    sw t3, 12(sp)
    sw ra, 16(sp)

    lw a0, 16(a1) # the pointer to the name of output file
    mv a1, s10 # the pointer to matrix o 
    lw a2, 0(s2) # the number of rows of o, which equals to number of rows of matrix m1
    lw a3, 0(s5) # the number of columns of o, which equals to number of columns of matrix h
    jal write_matrix

    lw a1, 0(sp)
    lw a2, 4(sp)
    lw t1, 8(sp)
    lw t3, 12(sp)
    lw ra, 16(sp)
    addi sp, sp, 20

# Compute and return argmax(o)
compute_argmax:
    addi sp, sp, -16
    sw a2, 0(sp)
    sw t1, 4(sp)
    sw t3, 8(sp)
    sw ra, 12(sp)

    mv a0, s10 # the pointer to matrix o
    mv a1, t3 # the size of matrix o, also the length of the array representing the matrix o
    jal argmax

    mv s11, a0 # the result of argmax
    lw a2, 0(sp)
    lw t1, 4(sp)
    lw t3, 8(sp)
    lw ra, 12(sp)
    addi sp, sp, 16

# If enabled, print argmax(o) and newline
print_or_not_print:
    li t0, 0
    bne t0, a2, free_all
    j print_result
    
print_result:
    # call print_int
    addi sp, sp, -12
    sw t1, 0(sp)
    sw t3, 4(sp)
    sw ra, 8(sp)

    mv a0, s11 # the result of argmax
    jal print_int

    lw t1, 0(sp)
    lw t3, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12
    
    # call print_char
    li t0, '\n'
    addi sp, sp, -12
    sw t1, 0(sp)
    sw t3, 4(sp)
    sw ra, 8(sp)

    mv a0, t0
    jal print_char

    lw t1, 0(sp)
    lw t3, 4(sp)
    lw ra, 8(sp)
    addi sp, sp, 12
    
free_all:

    # free s0
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s0
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s1
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s1
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s2
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s2
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s3
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s3
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s4
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s4
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    ebreak
    # free s5
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s5
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s6
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s6
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s7
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s7
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s8
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s8
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s9
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s9
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4
    
    # free s10
    addi sp, sp -4
    sw ra, 0(sp)
    
    mv a0 s10
    jal free
    
    lw ra, 0(sp)
    addi sp, sp, 4

epilogue:
    mv a0, s11

    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw s4, 16(sp)
    lw s5, 20(sp)
    lw s6, 24(sp)
    lw s7, 28(sp)
    lw s8, 32(sp)
    lw s9, 36(sp)
    lw s10, 40(sp)
    lw s11, 44(sp)
    addi sp, sp, 48
    
    jr ra
