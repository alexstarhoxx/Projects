addi t0, x0, 6
addi t1, x0, 3 # t1 will be shift amount
srl s0, t0, t1 # s0 will be the result, t0 > 0
addi t0, x0, -9
srl s0, t0, t1 # t0 < 0
addi t1, x0, 0
srl s0, t0, t1 # t1 = 0, t0 < 0
addi t1, x0, 31 
srl s0, t0, t1 # t1 = 31, t0 < 0
addi t0, x0, 7
srl s0, t0, t1 # t1 = 31, t0 > 0
add t1, x0, x0
srl s0, t0, t1 # t1 = 0, t0 > 0
addi t1, x0, 19 
srl s0, t0, t1 # t0 > 0
addi t0, x0, -42
srl s0, t0, t1 # t0 < 0