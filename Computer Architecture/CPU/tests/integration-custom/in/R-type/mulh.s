addi t0, x0, 4
addi t1, x0, 9
mulh t1, t1, t0
sub t0, t0, t0
mulh, t1, t1, t0
addi t0, x0, 2047
addi t1, x0, 2047
mulh t1, t1, t0
addi t0, x0, -2048
addi t1, x0, -2048
mulh t1, t0, t1
addi t1, t0, 2047
mulh t1, t1, t0
addi s0, x0, -3
addi s1, x0, 7
mulh s1, s0, s1
addi s1, x0, -20
mulh s1, s1, s0