addi t0, x0, 12
slti t1, t0, 13
ori, t1, t1, 0 # t1 should be 1
slti s0, t1, 0
andi s0, s0, 1 # s0 should be 0
addi t2, x0, -19
slti t2, t2, 19
xori t2, t2, 0 # t2 should be 1
addi s1, x0, 4
slli, s1, s1, 2
slti, s1, s1, 16 # s1 should be 0
slti, s1, s1, 17 # s1 should be 1
slti, s0, s1, -10 # s0 should be 0
addi t1, x0, -18
slti t1, t1, -7 # t1 should be 1