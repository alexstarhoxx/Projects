addi t0, x0, 10
addi t1, x0, 8
slt s0, t1, t0 # s0 should be 1
sub t1, t1, t0 # t1 = -2
slt s0, t0, t1 # s0 should be 0
slt s0, t1, t0 # s0 should be 1
addi t0, x0, -2
slt s0, t1, t0 # s0 should be 0
slt s0, t0, t1 # s0 should be 0
addi t0, x0, -8
slt s0, t0, t1 # s0 should be 1
slt s0, t1, t0 # s0 should be 0
mul t0, t1, t0 # t0 = 16
add t1, x0, t0 # t1 = 16
slt s0, t1, t0 # s0 should be 0
slt s0, t0, t1 # s0 should be 0