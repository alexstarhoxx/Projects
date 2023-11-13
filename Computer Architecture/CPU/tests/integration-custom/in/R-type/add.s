addi t0, x0, 1
addi t1, x0, 20
add t2, t1, t0 # t2 should be 21
addi s0, x0, -3
addi s1, x0, -7
add s1, s0, s1 # s1 should be -10
add t2, t2, s1 # t2 should be 11
slli s0, t2, 2 # s0 should be 121