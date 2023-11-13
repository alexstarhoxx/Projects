addi t0, x0, 3
addi t1, x0, 2
sll t0, t0, t1 # t0 = 3 * 4 = 12
addi s0, x0, 2
addi s1, x0, 0
sll s0, s0, s1 # s0 = 2 * 2 ** 0 = 2
addi s2, x0, 16
mul t1, t1, s2 # t1 = 32
addi a0, x0, 1
sub t1, t1, a0 # t1 = 31
sll t0, t0, t1 # t0 = 3 * 2 ** 31
add t0, x0, x0
add t1, x0, x0
addi t0, t0, 5
addi t1, t1, 10
sll t1, t1, t0 # t1 = 10 * 2 ** 5