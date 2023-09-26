.globl dot

.text
# =======================================================
# FUNCTION: Dot product of 2 int arrays
# Arguments:
#   a0 (int*) is the pointer to the start of arr0
#   a1 (int*) is the pointer to the start of arr1
#   a2 (int)  is the number of elements to use
#   a3 (int)  is the stride of arr0
#   a4 (int)  is the stride of arr1
# Returns:
#   a0 (int)  is the dot product of arr0 and arr1
# Exceptions:
#   - If the length of the array is less than 1,
#     this function terminates the program with error code 36
#   - If the stride of either array is less than 1,
#     this function terminates the program with error code 37
# =======================================================
dot:

    # Prologue
    # Save values of saved registers for calling conventions
    addi sp, sp, -16
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)

    add t0, x0, a0 # t0 is the pointer to arr0
    add t1, x0, a1 # t1 is the pointer to arr1
    li t2 0 # t2 will be the index of the iteration
    li t3, 1 # t3 will be used to test excepetions
    li t4, 0 # t4 will be sum of products that we are going to get
    li t5, 4 # t5 indicates a word
    mul s2, t5, a3 # Offset to be added to arr0
    mul s3, t5, a4 # Offset to be added to arr1

exception_1:
    bge a2 t3 exception_2 
    li a0 36
    j exit

exception_2:
    bge a3 t3 exception_3
    li a0 37
    j exit
    
exception_3:
    bge a4 t3 loop_start
    li a0 37
    j exit

loop_start:
    bge t2 a2 loop_end
    lw s0, 0(t0)
    lw s1, 0(t1)
    mul s1, s0, s1
    add t4, t4, s1 # Update the sum of products

    addi t2, t2, 1
    # Update the pointers of arr0 and arr1 to point to the new elements
    add t0, t0, s2
    add t1, t1, s3
    j loop_start

loop_end:

    # Epilogue
    # Return back the previously-saved value of saved registers for calling convention
    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    addi sp, sp, 16
    
    add a0, x0, t4
    jr ra
