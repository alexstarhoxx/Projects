.globl relu

.text
# ==============================================================================
# FUNCTION: Performs an inplace element-wise ReLU on an array of ints
# Arguments:
#   a0 (int*) is the pointer to the array
#   a1 (int)  is the # of elements in the array
# Returns:
#   None
# Exceptions:
#   - If the length of the array is less than 1,
#     this function terminates the program with error code 36
# ==============================================================================
relu:
    # Prologue
    # Setting the variables
    add t0, x0, a0 # t0 will be the address of the array
    add t1, x0, a1 # t1 will be the length of the array
    li t2, 0 # t2 will be the i variable in loop
    
    # Check if the length of the array is valid
    li t4, 1
    bge t1, t4, loop_start
    li a0, 36
    j exit
    
loop_start:
    bge t2, t1, loop_end # i < length of array
    lw t3, 0(t0) #t3 will be the value on the array
    
    bge t3 x0 loop_continue
    sw x0, 0(t0) # Change the value on the array to 0 if it is smaller than 0

loop_continue:
    addi t0, t0, 4 # Let the pointer points to the next value of the array
    addi t2, t2, 1 # Increment i to continue the loop
    j loop_start

loop_end:
    # Epilogue
    jr ra
