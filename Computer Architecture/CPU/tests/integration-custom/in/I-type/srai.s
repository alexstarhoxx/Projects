addi t0, x0, -38 # t0 = -38
srai t0, t0, 4
xori t0, t0, 3
addi t1, t0, 50
andi t2, t1, 98
srai t1, t1, 0
srai t1, t1, 17
srai t2, t2, 31
addi t1, x0, -102 # t1 = -102
srai t1, t1, 0
srai t1, t1, 31
addi s0, t1, 8
addi s1, t2, 430
srai s1, s1, 24
srai s0, s0, 8
xori s1, s0, 0
ori, s0, s1, 87
srai t1, s0, 1
srai t0, s1, 18
