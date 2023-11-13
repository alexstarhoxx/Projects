# In this test, t0, t1 would be store values to be compared.
# s0 and s1 will used to test if it will change if beq is true
# This test will check if the beq can be used for comparing
# positive and positive, negative and positive, negative and negative
# and x0 and other signed number or x0.

addi t0, x0, 10
addi t1, x0, 10
add s0, x0, x0
add s1, x0, x0
beq t0, t1, 16
addi s0, x0, 20 # If beq is true, s0, s1 will be 0
addi s1, x0, -3
mul s1, s0, s1
addi s2, x0, 15
slli s2, x0, 2

add s0, x0, x0
add s1, x0, x0
addi t0, x0, -42
addi t1, x0, -42
beq t1, t0, label1
mul s0, t0, t1 # If beq is true, s0, s1 will be 0
slli, s1, t0, 2
label1:
    addi s2, x0, -1

add s0, x0, x0
add s1, x0, x0
addi t0, x0, 6
addi t1, x0, 5
beq t0, t1, 8
mul s0, t0, t1 # If beq is not true, s0 will not be 0 but 30
addi s2, x0, 9

add s0, x0, x0
add s1, x0, x0
addi t0, x0, -24
addi t1, x0, 24
beq t0, t1, 28
addi s0, x0, 10 # If beq is not true, s0, s1 will not be 0
addi, s1, x0, -10
mul s0, s0, s1
sub s1, s0, s1
addi s2, x0, 6
sra s1, s1, s2
mul s2, t0, t1

add s0, x0, x0
add s1, x0, x0
addi t0, x0, -27
addi t1, x0, -12
beq t0, t1, label2 # If beq is not true, s0, s1, a0 will not be 0
sub s0, t0, t1
addi a0, x0, 2
sll s1, t1, a0
label2:
    addi s2, x0, -8
    mulhu s2, t0, t1

add s0, x0, x0
add s1, x0, x0
add t1, x0, x0
beq t1, x0, 12 # If beq is true, s0, s1 will be 0
addi s0, x0, 19
addi s1, x0, 55
addi s2, x0, -100
mulhu s2, t1, s2

add s0, x0, x0
add s1, x0, x0
beq x0, x0, label3
addi s0, x0, -1 # If beq is true, s0, s1 will be 0
addi s1, x0, 5
mul s0, s0, s1
label3:
    addi s2, x0, 9