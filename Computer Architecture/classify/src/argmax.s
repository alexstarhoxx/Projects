.globl argmax

.text
# =================================================================
# FUNCTION: Given a int array, return the index of the largest
#   element. If there are multiple, return the one
#   with the smallest index.
# Arguments:
#   a0 (int*) is the pointer to the start of the array
#   a1 (int)  is the # of elements in the array
# Returns:
#   a0 (int)  is the first index of the largest element
# Exceptions:
#   - If the length of the array is less than 1,
#     this function terminates the program with error code 36
# =================================================================
argmax: 
    # Prologue
    add t0, x0, a0 # t0 will be the pointer to the array
    add t1, x0, a1 # t1 will be the length of the array
    li t2, 0 # t2 is the index i for for loop
    lw t3, 0(t0) # t3 is the max value. Its default value will be the first element arr[0] in the array
    li, t5, 0 # Index that notes the indice of the max value
    
    # Check if the length of the array is less than 1
    li t4 1
    bge t1 t4 loop_start
    li a0 36
    j exit

loop_start:
    bge t2 t1 loop_end
    lw t4, 0(t0) # The element that the pointer is currently pointing to
    
    bge t3, t4, loop_continue
    add t3, x0, t4 # Update the maximum value
    add t5, x0, t2 # Update the indice of max value

loop_continue:
    addi t0, t0, 4 # Advance the pointer to the next element
    addi t2, t2, 1
    j loop_start

loop_end:
    # Epilogue
    add a0, x0, t5
    jr ra
