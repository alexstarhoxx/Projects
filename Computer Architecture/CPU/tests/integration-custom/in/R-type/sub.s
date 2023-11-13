addi t0, x0, 10
addi t1, x0, 30
sub t1, t1, t0 # t1 = 30 - 10 = 20
sub t0, t0, t1 # t0 = 10 - 20 = -10
addi s0, t0, -17
sub s1, s0, t1 # s1 = -27 - 20 = -47
add s2, s1, t1
add s2, s2, t1 # s2 = -7
sub t0, s1, s2 # t0 = -47 - (-7) = -40
sub s1, s1, s1 # s1 = 0
mul s1, s2, t0 # s1 = 280
sub s2, s1, s2 # s2 = 273
sub s2, s2, s1 # s2 = -7
sub t1, t1, s2 # t1 = 20 - (-7) = 27