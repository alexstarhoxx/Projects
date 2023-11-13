add t0, x0, x0 # t0 will always be 0
addi t1, t0, 1 # t1 will always be 1
or s0, t0, t1 # s0 will be the result
or s0, t1, t0
or s0, t0, t0
or s0, t1, t1
addi t2, x0, 10
or s0, t2, t0
addi s1, x0, -33
or s0, s1, t2 # t2 > 0, s1 < 0
add s1, s1, t2
or s0, t2, s1
mul s1, s1, s1
or s0, s1, t2 # s1, t2 > 0
addi t2, x0, -6
addi s1, x0, -30
or s0, s1, t2 # s1, t2 < 0
or s0, t0, t2
or s0, s1, x0