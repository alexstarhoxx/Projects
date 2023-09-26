# BYOW Design Document

**Name**: Alex Ho

## Classes and Data Structures
### MapGenerator
This class, or its correponding object is used to generate the pseudorandom map. The main idea of how to use other classes to generate the map pseudorandomly lives here.

#### Fields
1. `private static Random random` - A `Random` object created to pass a seed on it and create pseudorandom sequences later.
2. `private static final int hwRange` - Integer range for length of the hallways.
3. `private static final int roomRange` - Integer range for length of height and width of rooms.
4. `private static final int thwRange` - Integer range for respective length of horizonal hallways and vertical hallways used to create a turning hallways.
5. `private TETile[][] world` -  A 2D array representing the world we are going to be created and rendered.
6. `private boolean closed` - Used to judge if the world is completely closed. i.e. the world can not be extended more because of the limitations of not drawing out of the edge of the world and rules of not overlapping with each other in terms of rooms, hallways and turning hallways.

The `hwRange`, `roomRange` and `thwRange` are set to let the map drawn more complex.

### hallways
This class represents one of the components composing of the random map: hallways. It has two direction of horizon and vertical. This class let `hallways` objects has 3 main behaviours:
* Draw a hallway in the world with given length and position.
* Connect other components(`Rooms`, `TurningHallways`) to a hallway itself.
* Judge if the hallway is drawn out of the world or overlap with other rooms or other components having generated in the world.

#### Fields
1. `private int length` - The length of this hallway.
2. `private char direction` - The direction of this hallway. x represents the horizon; y represents the vertical. 
3. `private Position position` - The position of this hallway.
4. `private TETile[][] world` - The world that this hallway will be drawn in.
5. `private Detector detector` - The detector used to detect if any positions of tiles in this hallway is drawn, is a floor or is a wall. 

This class has an inner class `Position`. That class used to store position. i.e. the values of x coorinate and y coorinate in the world of a `hallways` object.

### Rooms
This class represents a room and let a `Rooms` object has 4 main behaviour:
* Draw a room in the world with given length of width, height and position.
* Connect `hallways` or `TurningHallways` to a room.
* Judge if the room is drawn out of the world or overlap components having drawn in the world.
* Add the golden-colored wall to the initialized room. This method will be called at once just at the beginning of generating the random map.
A room with length of width and height equal or greater than 3 is seem to be a valid room. Otherwise it should be an invalid one. The position of left bottom corner will be seens as the position of the whole room.

#### Fields
1. `private int width` - The width of the room.
2. `private int height` - The height of the room.
3. `private Position position` The position of the room.
4. `private TETile[][] world` - The world that the room will be drawn in.
5. `private Detector detector` - The detector. Same function as `hallways` one.
The purpose of inner `Position` class is the same as Position inner class in `hallways` one. 

### TurningHallways
This class represent a turning hallways composing of a horizon hallway and a vertical hallway objects of `hallways`. With different position of horizon and vertical hallway, it can be shaped as 7, reversed 7, L or reversed L. Equivalently, this class also behave in 3 main ways:
* Draw a turning hallway with its corresponding shape, lengths of horizon hallway and vertical hallway and position.
* Connect only `Rooms` to the turning hallway.
* Judge if the turning hallway is drawn out of the world or overlap components having drawn in the world.
The purpose of inner `Position` class is the same as Position inner class in `hallways` one.
A valid turning hallway should have an over-or-equal-3-length horizon hallway and vertical hallway respectively.

#### Fields
1. `private int xlength` - The length of hallway directed at x coordinate.
2. `private int ylength` - The length of hallway directed at y coordinate.
3. `private String shape` - The shape of the turning hallway.
4. `private Position position` - The position of the turning hallway.
5. `private TETile[][] world` - The world that the turning hallway will be drawn in.
6.` private Detector detector` - The detector. Same function as `hallway` one.
The purpose of inner `Position` class is the same as Position inner class in `hallways` one. 

### Detector
This class is to create a object to help us detect whether a specific tile in the world is drawn or judge whether it is a floor or wall. It created to help solving the problem of overlapping and juding whether a component is connected with other components.

#### Fields
`private TETile[][] world` - The world is drawn by components and that the detector is going to make detection.


## Algorithms
### MapGenerator
1. `public void generate()` 
* Initially generate a room with random width and height at random position of center of the world. A golden-colored wall generated randomly on 4-side walls of the room. 
* Then, let the room connect hallways or turning hallways randomly from 4 sides from it and continuously extend the paths of created rooms, hallways and turning hallways until these are not able to be created(drawn out of the world or overlap with each other) and the whole path is closed.
2. `public void roomConnector(Rooms room, boolean closed)`
* For ROOM, it will be the host and other components(hallways and turning walls) must connect to it randomly from its sides. 
* If any connection is not able to be created, CLOSED will become true. Otherwise it will be false; 
* If CLOSED is true from the beginning, do not make any connection and return directly.
3. `public void hwConnector(hallways hw, boolean closed)`
* For HW, it will be the host and a room must connect to it randomly from its unconnected side. 
* If any connection is not able to be created, CLOSED will become true. Otherwise it will be false.
* If CLOSED is true from the beginning, do not make any connection and return directly.
4. `public void thwConnector(TurningHallways thw, boolean closed)`
* For THW, it will be the host and a room must connect to it randomly from its unconnected side. 
* If any connection is not able to be created, CLOSED will become true. Otherwise it will be false. 
* If CLOSED is true from the beginning, do not make any connection and return directly.

### Rooms
**Note that the position of a room will be the same as the position of its left-bottom corner wall!**

1. `private int validLength(int length)`
A helper method used to check if arguments passed into the constructor is equal or greater than 3. If not, return 3 directly. If it is, return LENGTH.

#### Draw a Room in the World
1. `public void draw()`
Draw this room with the given width, height and position in the given world.
* First it will draw the top and the bottom walls with the given position. Then it will draw the left and right sides of walls. Finally it will fill in the floors on the drawn frame of room. 
* With the given position, these 3 procedures are broken into 3 private helper method: `drawTopAndBottomWalls()`,`drawLeftAndRightWalls()` and `fillfloors` to complete.

#### Connect a Hallway to the Room
1. `public boolean connectWith(hallways hw)`
Connect a given hallway HW to this room randomly.
* Check which sides are not connected first by using a boolean array and a set of private helper methods. 0 indicates the whether the top side is connected. 1 indicates bottom; 2 indicates left side and 3 indicates right side.
* Among the unconnected sides, choose a random side to connect with the given hallway by using the random object.
* Use `callConHw(hallways, int)` method to choose a method for connecting a unconnected side.
2. `private boolean callConHw(hallways hw, int num)`
A helper method for `connectWith(hallways hw)` method. By passing the corresponding number NUM specified in the boolean array and the HW, it will choose room's corresponding side to connect with HW.

##### Check Whether a Side is Connected
A set of private helper methods for `public boolean connectWith(hallways hw)` method.
1. `boolean isConnectedTop()`
With the given room position, check if top of this room is connected with other components by checking whether there is a floor or a golden-colored wall on the top of this room since an unconnected side should not have any floor or a golden-colored wall but all ordinary walls.
2. `boolean isConnectedBottom()`
Same logic as `isConnectedTop()`. Except it checks for the bottom of this room.
3. `boolean isConnectedLeft()`
Same logic as `isConnectedTop()`. Except it checks for the left side of this room.
4. `boolean isConnectedRight()`
Same logic as `isConnectedTop()`. Except it checks for the right side of this room.


##### Connection methods
1. `private boolean conTopEdgeWith(hallways hw)`
A helper method for `connectWith(hallways hw)` method. It will help to connect HW to the top of this room. The result will draw in the `world`.
* Force HW to change its direction to y coordinate by using `changeDirTo(char)` in `hallways` class.
* Randomly choose a wall of top side for connection purpose. Record its position.
* With its position, calculating where the HW should be placed. Place the HW on the top side of the room first by changing. The bottom line of the HW should not overlap the top side of this room.
* At this time, check if the HW placed on that position is overlapped with other drawn components or will be drawn out of the world. If it fulfills any situation like that, directly return ture, which will indicate this room is closed and is not able to connect other components in this connecting direction.
* If not, use helper method `digWallToFloor` to change the choosen wall into a floor. Draw HW itself by using `draw()` in `hallways` class.
* Finally return false which indicates the whole path of the map is not closed and can keep extending by connecting other components.
2. `private boolean conBottomEdgeWith(hallways hw)`
Almost the same logic as `conTopEdgeWith(hallways hw)`. Except it connect HW to the bottom of this room and the calculations of placing position and choosed wall to dig are different.
3. `private boolean conLeftEdgeWith(hallways hw)`
Almost the same logic as `conTopEdgeWith(hallways hw)`. Except it connect HW to the left side of this room, forcing HW to change its direction to x coordinate and the calculations of placing position and choosed wall to dig are different.
4. `private boolean conRightEdgeWith(hallways hw)`
Almost the same logic as `conTopEdgeWith(hallways hw)`. Except it connect HW to the right side of this room, forcing HW to change its direction to x coordinate and the calculations of placing position and choosed wall to dig are different.

#### Connect a Turning Hallway to the Room
1. `public boolean connectWith(TurningHallWays thw)`
This method's logic and helper methods they used are almost the same as hallway one's `connectWith(hallways hw)` method. Except all `hallways hw` are changed to `TurningHallways thw`.

##### Check Whether a Side is Connected
You may find some `isConnected` private methods are used in in `connectedWith(TurningHallways thw)` method. That the same method used in connecting a hallway to the room section.

##### Connection method
1. `private boolean conTopEdgeWith(TurningHallways thw)`
A helper method for `connectWith(TurningHallways thw)` method. It will help to connect THW to the top of this room. The result will draw in the `world`.
* Force THW to change its shape to either 7 or reversed 7 by using helper method `randomTopShape()`.
* Randomly choose a wall of top side for connection purpose. Record its position.
* Call helper method to continue to the connection according to THW shape.
2. `private boolean conTop7(TurningHallways thw, int conPointX, int conPointY)`
A helper method for `conTopEdgeWith(TurningHallways thw)` method. It passes in the position of the previously choosen wall for connection purpose.
* With its position, calculating where the THW should be placed. Place the THW on the top side of the room first by changing. The bottom line of the THW should not overlap the top side of this room.
* At this time, check if the THW placed on that position is overlapped with other drawn components or will be drawn out of the world. If it fulfills any situation like that, directly return ture, which will indicate this room is closed and is not able to connect other components in this connecting direction.
* If not, use helper method `digWallToFloor` to change the choosen wall into a floor. Draw THW itself by using `draw()` in `hallways` class.
* Finally return false which indicates the whole path of the map is not closed and can keep extending by connecting other components.

**The rest of connection methods for different direcitions follow a very similar logic as these two methods shown above, except that the turning hallways shapes are different from each other:**
**Note that for top, it will only connect turning hallways shaped 7 or reversed 7. For bottom, only connecting shape L and reversed L. For the left side, only connecting shape L and reversed 7. For the right side, only connecting shape 7 and reversed L**


#### Check if the Room is Overlapped or ouf of the World
1. `public boolean isOutOfWorld()`
If neither these situations happen, the room will be considered to be drawn out of the world.
* The bottom line of the wall is drawn out of the world.
* The left side of the wall is drawn out of the world.
* The right side of the wall is drawn out of the world.
* The top side of the wall is drawn **on the top line of the world** or drawn out of the world.
2. `public boolean isOverlap()`
This method must be used before drawing the room. Check all the tiles within the room, including the walls. If any of the tile that will be drawn as part of the room has already drawn (or is not a 'nothing' tile), the method will consider that the room to be drawn will overlap other components in the world if it draws. 

#### Randomly Add the Golden-colored Wall
1. `public void addGoldenWall()`
Randomly choose a side to add the golden wall. At each side, it will randomly choose a wall except for the ends of the side and turn it into the golden-colored wall a.k.a. the locked door. It will use the private helper methods of `addTopGolden()`, `addBottomGolden()`, `addLeftGolden` and `addRightGolden`.

### Hallways
**Note that the position of a hallway will be the same as the position of its left-bottom corner wall!**

#### Draw a Hallway in the World
1. `public void draw()`

Based the direction of a hallway, draw it by calling the private helper methods `drawTwoSidesWallsXDir`, `drawTwoSidesWallsYDir` and `drawOneLineFloorsYDir`. The previous two methods will draw the same-length two sides walls of the hallway based on the position and direction. The third method will draw a line of floor between these two sides walls with the same length.

#### Connect a Room to the Hallway
1. `public boolean connectWith(Rooms room)`
Connect a room to the unconnected side of the hallway. It will
* Firstly, check out its direction.
* Then, based on its direction, check if it has an unconnected side.
* If it has, connect a room to that side.
With horizon direcition(x direction), its unconnected side will be either left or right.
With vertical direction(y direction), its unconnected side will be either top or bottom.

**Note that the following 'XXXX' should be replaced with 'Top', 'Bottom', 'Left' and 'Right'.** 

2. `private boolean isConnectedXXXX()`
The implemental logic of this set of private helper methods are very similar. Take `isConnectedLeft` as an example.
* Firstly, it will find out the position of left-bottom wall of the hallway.
* Secondly, based on that wall, see if its left side is still a wall, if its left up tile is a floor, and if its left-up-up tile is a wall.
If the hallway is connected with other components in the map, the left-most side should be available for the avatar to pass through, not let it to fall into the black tile or wall and at the same time fulfill the requirement of creating the map.

3. `private boolean connectXXXXWith(Rooms room)`
The implemental logic of this set of methods are similar. Take `connectLeftWith(Rooms room)` as an example.
* Create a random num that is ranged from 0 to the value of the room's height minus 2 in order to make sure that the hallway will connect room in a right way.
* Based on the random num and the position of this hallway, calculate the position of room to be placed. Change the room position correctly.
* Check if the room is going to be out of the map or overlap other components if it is drawn. If it is, make this hallway the left-most side's floor become a wall (to become closed) by calling `closedLeft()` and return true.
**Note that for this step, `isOutOfMap()` should really be ahead of `isOverlap()` method because only if the room to be drawn within the map will it overlaps other drawn components which are alreadly drawn in the map**
* If it can be drawn, calculate the position of the connecting point of the room and this hallway and change the tile at this position from wall to floor by `digWallToFloor(int x, int y)` method. The connecting point is the only entry through which the avatar go into hallway successfully from the room.

#### Check if the Room is Overlapped or ouf of the World
1. `public boolean isOutOfWorld()`
If neither these situations happen, the hallway will be considered to be drawn out of the world.
* The left side of the horizon hallway is drawn out of the world.
* The right side of the horizon hallway is drawn out of the world.
* The top of the vertical hallway is drawn **on the top line of the world** or drawn out of the world.
* The bottom of the vertical hallway is drawn out of the world.
2. `public boolean isOverlap()`
This method implementation is similar with `Rooms` one. Except it check all the tiles within the hallway.