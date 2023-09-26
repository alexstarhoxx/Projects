# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:

There are some differences between my implementation and the official one.
First of all, we differ in writing a single hexagon. In the last project, I found creating a type of object might be very clear and useful. Therefore, I created a `Hexagon` class and let it draw itself at position x and y. In contrast, the TA's implementation is based on the view of the God. What's more, our methods of drawing a hexagon are also different. I used iteration and relied heavliy on mathemethical ways to draw a hexagon, while TA used recursion to implement.
As for tessellating hexagons, we have the same big idea. We agreed on the idea of drawing a hexagon line first and then use that function to tesselate hexagons. However, like the differences of drawing a single hexagon, I preferred iteration and mathematical way while the TA took advantage of recursion.
Last but not least, although we do think that position is important, our way to solve this problem differs. While I'd like to pass x and y coordinate as parameters directly to functions, TA created a static inner class(which I am not familiar with) and made it like an anchor to anchor the position in the hexagon world.
From these various differences, I found some surprises. What the most important thing I have learned is what the TA has said:" *When you are stuck, stay calm. Step back and look for some observation to find out a clearer solution* ". This sentence works when TA was debugging in live. In addition, It reminds me of using recursion to solve problems, which I never done before except only for completing homework about recursion. Also, when debugging, the TA ran the code and see what's going on, using the result to reason the problem is another thing that I've learned. Lastly, TA's live demo also reminds me of getting better abstraction. For instance, the TA created `getUpRightNeighbor` and `getDownRightNeighbor` functions just in order to get the position of its neighbors. I also have these similar data but did not make them abstract.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:

I think the hexagon is like rooms and hallways, and tesselation like the process of linking these rooms and hallways together randomly.
Both tesselation and linking processes should consider the problem of position. For tesselation, position is obvious and easy to
work out, while linking should be more complex since there are lots of conditions to limit.

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:

I would like to think about generating rectangular rooms and hallways first. Then on the basis of these two methods, 
I would like to think about generating random-shape rooms, hallways with random-hights and an 'L' corner consisting of
the hallways. These kinds of processes are like the process of generating hexagons in this lab.

After all, I will consider how to link them together randomly and meet all the requirements. This process is like
the process of tessellating hexagon in this lab.

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer:

Hallway has 'fixed' width of 1 or 2 tiles and only have one shape of line. Besides, its two sides should be opened
and not closed by the wall tile, which implies that it should be reachable by two sides.

Room has random width and height and random shape. It can be either big or small, long or short. In addition, all of its
sides can be opened and not closed by the wall tile. It can have one side opened and other side closed to be a one-enter room,
or it can have many sides to open to make lots of road reachable to the room.