.globl matmul

.text
# =======================================================
# FUNCTION: Matrix Multiplication of 2 integer matrices
#   d = matmul(m0, m1)
# Arguments:
#   a0 (int*)  is the pointer to the start of m0
#   a1 (int)   is the # of rows (height) of m0 -> n
#   a2 (int)   is the # of columns (width) of m0 -> m
#   a3 (int*)  is the pointer to the start of m1
#   a4 (int)   is the # of rows (height) of m1 -> m
#   a5 (int)   is the # of columns (width) of m1 -> k
#   a6 (int*)  is the pointer to the the start of d
# Returns:
#   None (void), sets d = matmul(m0, m1)
# Exceptions:
#   Make sure to check in top to bottom order!
#   - If the dimensions of m0 do not make sense,
#     this function terminates the program with exit code 38
#   - If the dimensions of m1 do not make sense,
#     this function terminates the program with exit code 38
#   - If the dimensions of m0 and m1 don't match,
#     this function terminates the program with exit code 38
# =======================================================
matmul:

    # Save value of saved registers for calling conventions
    addi sp, sp -8
    sw s0, 0(sp)
    sw s1, 4(sp)

    # Error checks
    li t0, 1 # Here t0 is used to check errors
    blt a1, t0, error
    blt a2, t0, error
    blt a4, t0, error
    blt a5, t0, error
    bne a2, a4, error
    
    # Prologue
    add t0, x0, a0 # t0 will be the pointer of the first array
    add t1, x0, a3 # t1 will be the pointer of the second array
    add t2, x0, a6 # t2 will be the pointer of the target array indicatin the new n x k matrix
    li s0, 0 # The index of the outer loop i
    li s1, 0 # The index of the inner loop j


outer_loop_start:
    bge s0, a1, outer_loop_end
inner_loop_start:
    bge s1, a5, inner_loop_end
    
    # Before calling other methods, save the values of temporary and argument registers
    addi sp, sp, -40
    sw a0, 0(sp)
    sw a1, 4(sp)
    sw a2, 8(sp)
    sw a3, 12(sp)
    sw a4, 16(sp)
    sw a5, 20(sp)
    sw t0, 24(sp)
    sw t1, 28(sp)
    sw t2, 32(sp)
    sw ra, 36(sp)
    
    # Calling fucntion dot to calculate the result of row i, column j
    add a0, x0, t0
    add a1, x0, t1
    addi a3, x0, 1
    add a4, x0, a5
    jal dot
    add t3, x0, a0 # t3 now will be the result of dot method

    # After calling other methods, return back the original values of temporary and argument registers
    lw a0, 0(sp)
    lw a1, 4(sp)
    lw a2, 8(sp)
    lw a3, 12(sp)
    lw a4, 16(sp)
    lw a5, 20(sp)
    lw t0, 24(sp)
    lw t1, 28(sp)
    lw t2, 32(sp)
    lw ra, 36(sp)
    addi sp, sp, 40
    
    sw t3, 0(t2) # Save the result of dot method to the new metrix
    addi t2, t2, 4 # Advance to the next element for the pointer of target array indicating the new n x k matrix
    addi t1, t1, 4 # Advance to the next elelment for the pointer of the second array
    
    addi s1, s1, 1
    j inner_loop_start

inner_loop_end:
    li t3, 4
    mul t3, t3, a2
    add t0, t0, t3 # Advance the pointer of the first array to the first element of the next column
    li s1, 0 # Beaware that s1 a.k.a j value should be updated back to 0 to make the next period of inner loop!
    add t1, x0, a3 # Beaware that the pointer of the second array should point back to its first element!
    addi s0, s0, 1
    j outer_loop_start

outer_loop_end:
    
    # Return values of saved registers back for calling convention
    lw s0, 0(sp)
    lw s1, 4(sp)
    addi sp, sp, 8
    
    # Epilogue
    jr ra

error:
    li a0, 38
    j exit