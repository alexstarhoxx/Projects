addi t0, x0, 7
addi t1, x0, 6
mulhu t1, t1, t0
addi a0, x0, 9
sub t1, t1, a0 # t1 should be -3
mulhu t1, t1, t0
addi t0, x0, -30
addi t1, x0, -68
mulhu t1, t1, t0
addi t0, x0, 2047
addi t1, x0, -2048
mulhu, t1, t0, t1
add t1, t0, t1
mulhu, t1, t0, t1

addi s0, x0, -3
addi s1, x0, 7
mulhu s1, s1, s0