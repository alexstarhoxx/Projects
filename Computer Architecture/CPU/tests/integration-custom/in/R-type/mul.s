addi t0, x0, 20
addi t1, x0, 2
mul t1, t0, t1 # t1 should be 40
addi t0, t0, -35
addi t1, t0, 7
mul t1, t1, t0 # t1 should be 120
addi s0, x0, 24
mul s1, s0, t0 # s1 should be -350
slli t1, t1, 2 # t1 should be 1440
srli t1, t1, 2
mul s1, t0, t0 # s1 should be 225
mul t0, x0, t0 # t0 should be 0
mul t1, t0, s1 # t1 should be 0
mul s1, x0, x0 # s1 should be 0
mul s2, s1, s1 # s2 should be 0
addi s1, s1, 45
addi s2, s1, -20
mul t0, t0, s1 # t0 should be 0
mul t1, s1, s2 # t1 should be -900

addi t0, x0, 1550
addi t1, x0, 2
mul t1, t1, t0
addi t0, x0, 2047
addi t1, x0, 2020
mul t1, t0, t1
addi t1, x0, -2048
mul t1, t0, t1

addi s0, x0, -3
addi s1, x0, 7
mulhu s1, s1, s0