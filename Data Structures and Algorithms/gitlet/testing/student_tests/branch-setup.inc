# A setup for branch which is the same as test10 one's
I definitions.inc
I setup2.inc
+ Dog.txt notwug.txt
> add Dog.txt
<<<
> commit "Dog will not be a wug"
<<<
> branch cool-beans
<<<
> checkout cool-beans
<<<

# At this time, cool-beans branch should have Maths.txt, master branch should not have.
+ Maths.txt treewug.txt
> add Maths.txt
<<<
> commit "added maths"
<<<

# At this time, master branch should have English.txt, cool-beans branch should not have.
# Additionally, master branch should have new content for Cat.txt, cool-beans branch should not have
> checkout master
<<<
+ Cat.txt pearwug.txt
+ English.txt whywug.txt
> add Cat.txt
<<<
> add English.txt
<<<
> commit "Modify Cat and add English"
<<<