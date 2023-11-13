# In this test, t0, t1 would be store values to be compared.
# s0, s1 or t2 will used to test if they will change if beq is true
# This test will check if the bne can be used for comparing
# positive and positive, negative and positive, negative and negative
# and x0 and other signed number or x0.

add s0, x0, x0
add s1, x0, x0
add t2, x0, x0
addi t0, x0, 14
addi t1, x0, 1023
bne  t0, t1, 16 # If bne is true, s0, s1 and t2 will be 0
addi s0, x0, 7
addi s1, x0, -10
sub t2, s0, s1
mul a2, t0, t0
addi a2, a2, 25

add s0, x0, x0
add s1, x0, x0
add t2, x0, x0
addi t0, x0, -23
addi t1, x0, 23
bne t0, t1, label1 # If bne is true, s0, s1, and t2 will be 0
addi s0, x0, 9
addi s1, x0, 12
label1: addi s2, x0, 11

add s0, x0, x0
add s1, x0, x0
add t2, x0, x0
addi t0, x0, 28
addi t1, x0, 28
bne t0, t1, 20 # If bne fails, s0, s1, t2 will not be 0
addi s0, x0, 9
addi s1, x0, 3
slt, t2, s1, s0
mul t2, t2, s1
addi s2, x0, 12

add s0, x0, x0
add s1, x0, x0
add t2, x0, x0
addi t0, x0, -2047
addi t1, x0, -2048
bne t0, t1, 20 # If bne is true, s0, s1 and t2 will be 0
addi a0, x0, 2
addi s0, x0, 2000
sll s1, s0, a0
mulh t2, s0, s1
addi s2, x0, -20
addi a0, x0, 2
sra s2, s2, a0


add s0, x0, x0
add s1, x0, x0
add t2, x0, x0
addi t0, x0, -888
addi t1, x0, -888
bne t0, t1, label2 # If bne fails, s0, s1 and t2 will not be 0
addi s0, x0, -5
addi s1, x0, -2000
mulhu t2, s0, s1
mulh, s0, s1, s1
label2: 
    addi s2, x0, -2040
    xori s2, s2, 1

add s0, x0, x0
add s1, x0, x0
add t2, x0, x0
add t0, x0, x0
addi t1, x0, 1
bne t0, t1, 16 # If bne is true, s0, s1 and t2 will be 0
addi s0, x0, 4
addi s1, x0, 8
addi t2, x0, 10
addi s2, x0, -3
slli, s2, x0, 31

add s0, x0, x0
add s1, x0, x0
add t2, x0, x0
add t0, x0, x0
add t1, x0, x0
bne t0, t1, label4 # If bne fails, s0, s1 and t2 will not be 0
addi s0, x0, 12
addi s1, x0, -5
addi t2, x0, 30
sra s1, s1, t2
label4: addi t2, x0, 99