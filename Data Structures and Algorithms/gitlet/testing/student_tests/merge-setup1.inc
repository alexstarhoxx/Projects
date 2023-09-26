# A setup for branch for testing merge command(without conflict version).
I definitions.inc
> init
<<<

+ a.txt applewug.txt
> add a.txt
<<<
> commit "added a"
<<<

+ p.txt pearwug.txt
+ t.txt treewug.txt
> add p.txt
<<<
> add t.txt
<<<
> commit "added p and t"
<<<

+ n.txt notwug.txt
+ o.txt orangewug.txt
> add n.txt
<<<
> add o.txt
<<<
> commit "added n and o"
<<<

# Create a new branch
> branch other
<<<

# Create 3 new commits in the master branch
+ a.txt !applewug.txt
> add a.txt
<<<
> commit "change a"
<<<

> rm n.txt
<<<
> commit "remove n"
<<<

+ why.txt whywug.txt
> add why.txt
<<<
> commit "added why"
<<<

# Create 2 new commits in other branch
> checkout other
<<<
+ w.txt wug.txt
> add w.txt
<<<
> commit "added w"
<<<

+ p.txt !pearwug.txt
> rm o.txt
<<<
> add p.txt
<<<
> commit "change p and remove o"
<<<

> checkout master
<<<
