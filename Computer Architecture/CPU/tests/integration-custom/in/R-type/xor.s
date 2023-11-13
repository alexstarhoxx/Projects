add t0, x0, x0
addi t1, x0, 1
xor s0, t0, t1 # s0 should be 1
xor s0, t1, t0 # s0 should be 1
addi t0, x0, 8
addi t1, x0, -3
xor s0, t0, t1
xor s0, t1, t0
mul t0, t1, t0
xor s0, t1, t0
xor s0, t0, t1
addi t0, x0, 4
addi t1, x0, 18
xor s0, t0, t1
xor, s0, t1, t0