addi t0, x0, 9 # t0 = 9
andi t0, t0, 3 # t0 = t0 & 9
addi t1, t0, 12 # t1 = t0 + 12
andi t2, t1, 1 # t2 = t1 & 1
andi s0, t2, 0 # s0 = t2 & 0
addi s0, t1, -17
andi s1, s0, -1
andi t2, t1, 23
andi s1, t1, 0