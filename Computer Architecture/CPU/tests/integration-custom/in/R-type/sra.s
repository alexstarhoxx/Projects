addi t0, x0, 6
addi t1, x0, 3 # t1 will be shift amount
sra s0, t0, t1 # s0 will be the result, t0 > 0
addi t0, x0, -9
sra s0, t0, t1 # t0 < 0
addi t1, x0, 0
sra s0, t0, t1 # t1 = 0, t0 < 0
addi t1, x0, 31 
sra s0, t0, t1 # t1 = 31, t0 < 0
addi t0, x0, 7
sra s0, t0, t1 # t1 = 31, t0 > 0
add t1, x0, x0
sra s0, t0, t1 # t1 = 0, t0 > 0
addi t1, x0, 19 
sra s0, t0, t1 # t0 > 0
addi t0, x0, -42
sra s0, t0, t1 # t0 < 0