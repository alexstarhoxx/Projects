# BYOW Design Document

> This is a project originated from Project 3 of CS61B, University of California, Berkeley. The specification can be seen from [this linked](https://sp21.datastructur.es/materials/proj/proj3/proj3). 

**Name**: Alex Ho

## Classes and Data Structures
### Main
This is the main entry point for the program. This class simply parses the command line inputs, and lets the byow.Core.Engine class take over in either keyboard or input string mode.

### Engine
This is the whole implementation logics of this game. It implements keyboard and input string mode respectively by using other classes within byow.Core package.

#### Fields
1. `TERenderer ter` - a tile renderer given by course staff within byow.TileEngine package.
2. `private static final int WIDTH` - The width of game interface.
3. `private static final int HEIGHT` - The height of game interface.
4. `public static final File CWD` - The directory path of the current working directory.
5. `public static final File AVATAR_FILE` - The file path of avatar text file which used to store state of avatar to implement persistence of the game.
6. `public static final File ENCOUTNER_FILE` - The file path of encounter text file which used to store state of encounter to implement persistence of the game.
---
These following classes are all for creating a random map in the world.
### MapGenerator
This class, or its correponding object is used to generate the pseudorandom map. The main idea of how to use other classes to generate the map pseudorandomly lives here.

#### Fields
1. `private static Random random` - A `Random` object created to pass a seed on it and create pseudorandom sequences later.
2. `private static final int hwRange` - Integer range for length of the hallways.
3. `private static final int roomRange` - Integer range for length of height and width of rooms.
4. `private static final int thwRange` - Integer range for respective length of horizonal hallways and vertical hallways used to create a turning hallways.
5. `private TETile[][] world` -  A 2D array representing the world we are going to be created and rendered to display an interface of this game. Map will be drawn in this world.
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

---
These following classes are all for adding interactivity to the generate world.
### Avatar
This class is to create an object which is enable to move around freely within the generated map. It will be put in a random place within th map and can move up, down, left and right freely.

#### Fields
1. `private static final TETile LOOK = Tileset.AVATAR` - This represents the appearance of an avatar object.
2. `private Position position` - The position of an avatar. This position should be within the generated map.
3. `private long seed` - The long integer used to generate the pseudorandom map `world`
4. `private TETile[][] world` -  A 2D array representing the world we are going to be created and rendered to display an interface of this game. Map will be drawn in this world.
5. `private Detector detector` - The detector used to detect if any positions of this avatar to be placed is drawn, is a floor or is an encounter. 

### Encounter
This class is to create an encounter that will encounter with the avatar. The encounter will be static, and will be placed randomly within the map. Once an avatar meets the encounter, a small game will be triggered.

#### Fields
1. `private static final TETile LOOK = Tileset.FLOWER` - This represents the appearance of an encounter object.
2. `private Position position` - The position of an encounter. This position should be within the generated map.
3. `private long seed` - The long integer used to generate the pseudorandom map `world`
4. `private TETile[][] world` - The generated map
5. `private Detector detector` - The detector used to detect if any positions of this encounter to be placed is drawn or is a floor.
6. `private boolean met` - A state used to check if this encounter has met with the avatar.

### Menu
This is one of the UI elements in this game. When the user starts to play, an UI interface will be displayed first to instruct the user to start the game. 

#### Fields
1. `private int width` - The width of this interface
2. `private int height` - The height of this interface
3. `private static final int TILE_SIZE` - The tile size to set in order to use `StdDraw` library to draw this interface.
4. `private String seed` - The long integer used to generate the pseudorandom map `world`

### Small New Game
This class, or its corresponding object is used to trigger a small game when the avatar meets an encounter within the generated map. The user must win this small game by picking up 10 flowers to return the generated map(The main game). It will:
* Display a page for prompting the user what happen when the avatar meets an encounter.
* Display the interface and interactivity of this small game.

#### Fields
1. `private long seed` - The long integer used to generate the pseudorandom map `world`
2. `private int width` - The width of this interface
3. `private int height` - The height of this interface
4. `private TETile[][] world` -  A 2D array representing the world we are going to be created and rendered to display an interface of this game. Map will be drawn in this world.

### InputSource
This is an interface of `KeyboardInput` and `StringInput` classes.

### Keyboard Input
This class is used to monitor and grab input typed by users using keyboard.

### String Input
This class is used to monitor and grab the String input typed in command line by users.

#### Fields
1. `private String input` - The string input the users typed in command line.
2. `private int index` - The indices of the string input.

## Algorithms - Part 0: Main logic of algorithms



## Algorithms - Part I: Generate a psedorandom map

The generated map consists of rooms, hallways and turning hallways. The interactivity includes avatar, a small new game and encounter. You can find the corresponding classes and their methods specification on the `Core` directory or the following parts. **The main algorithm of creating a random map is on the `MapGenerator` class.**

### MapGenerator
1. `public void generate()` 
* Initially generate a room with random width and height at random position of center of the world. A golden-colored wall generated randomly on 4-side walls of the room. 
* Then, let the room connect hallways or turning hallways randomly from 4 sides from it and continuously extend the paths of created rooms, hallways and turning hallways until these are not able to be created(drawn out of the world or overlap with each other) and the whole path is closed.
2. `public void roomConnector(Rooms room)`
* For ROOM, it will be the host and other components(hallways and turning walls) must connect to it randomly from its sides. 
* If any connection is not able to be created, instance variable CLOSED will become true. Otherwise it will be false; 
* If CLOSED is true from the beginning, do not make any connection and return directly.
3. `public void hwConnector(hallways hw)`
* For HW, it will be the host and a room must connect to it randomly from its unconnected side. 
* If any connection is not able to be created, CLOSED will become true. Otherwise it will be false.
* If CLOSED is true from the beginning, do not make any connection and return directly.
4. `public void thwConnector(TurningHallways thw)`
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
The direction of a hallway should either be horizon or vertical:
* horizon: <br>
  ######## <br>
  XXXXXXXX <br>
  P#######
* vertical: <br>
  #X# <br>
  #X# <br>
  #X# <br>
  #X# <br>
  #X# <br>
  PX# <br>

**Note that the position of a hallway will be the same as the position of its left-bottom corner wall labelled as 'P'!**

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

### Turning Hallways
The shape of a turning hallway can be one of the shapes shown as follows:
* "L" shape <p>
  #X# <br>
  #X# <br>
  #X##### <br>
  #XXXXXX <br>
  P###### <br>

* "Reversed L" shape <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;#X# <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;#X# <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;#X# <br>
 #####X# <br>
 XXXXXX# <br>
 P###### <br>

* "7" shape <br>
    \####### <br>
    XXXXXX# <br>
    \#####X# <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;#X# <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PX# <br>
* "Reversed 7" shape <br>
    \######## <br>
    #XXXXXXX <br>
    \#X###### <br>
    #X# <br>
    PX#

**Note that All "P"s on these shapes represents its corresponding position.**

#### Draw a Turning Hallway in the World
1. `public void drawXXXX()` Draw a turning hallway based on its instance variable `shape` and `position` on the world. Each turning hallway is drawn by drawing a horizon and a vertical `Hallway` first and connect them together in accordance with its own shape. <br>
**Note that "XXXX" here refers to one of the 4 shapes**

#### Connect a Room to a Drawn Turning Hallway
1. `public boolean connectWith(Rooms room)` Connect a room to a drawn turning hallway based on the `shape` of this turning hallway. 
There will have 4 cases here: 
* 'L' shape: The room should either be connected on top of the turning hallway, or the right side of it.
* 'Reversed L' shape: The room should either be connected on top of the turning hallway, or the left side of it.
* '7' shape: The room should either be connected on bottom of the turning hallway, or the left side of it.
* 'Reversed 7' shape: The room should either be connected on bottom of the turning hallway, or the right side of it.
If the room can not be drawn on these above senario, do nothing and return variable `closed` which indicates whether this turning hallway can be connected to other components. 

2. `isConnectedDDDSSS()` 'DDD' refers to 'direction', which can be left, right, top and bottom; 'SSS' refers to 'shape' of this turnin g hallway. The implementation of this set of methods are very similar. Take `isConnectedRightL()` as an example:
* The method tends to check if the right side of this L shaped turning hallway has already been connected with room or a hallway.
*   #X# <br>
  #X# <br>
  #X#####C1 <br>
  #XXXXXXC2 <br>
  #######C3 <br>
As for this given turning hallway, this method will check whether the position of C1 and C3 is a wall and position of C2 is a floor. If these 3 conditions are fulfilled at the same time, it will return ture. Otherwise, return false.

3. `public boolean conDDDSSSWith(Rooms room)` 'DDD' refers to 'direction', which can be left, right, top and bottom; 'SSS' refers to the 'shape' of this turning hallway. The implementation of this set of methods are very similar. Take `conTopL()` as an example:
* This method tends to connect a room to the drawn turning hallway.
* Firstly, it will create a random number ranged from 0 to room's width minus 2 in order to let this turning hallway be connected to a random place of bottom side of the room.
* Based on the random number, determine a new position of the room and change `room`'s instance variable `position` to the new position.
* Check if the room is going to be out of the map or overlap other components if it is drawn. If it is, make this turning hallway the top floor become a wall (to become closed) by calling closedTopL() and return true. **Note that for this step, isOutOfMap() should really be ahead of isOverlap() method because only if the room to be drawn within the map will it overlaps other drawn components which are alreadly drawn in the map**
* If it can be drawn, draw the given room.
* Based on this turning hallway's position, find out the position of the wall needed to be digged out to a floor, calling `digWallToFloor(int x, int y)` in order to successfully connect room and turning hallway. Return false indicating that this connection successed and the map has not closed yet.

#### Check if a Turning Hallway is Overlapped or ouf of the World

4. `public boolean isOverlap()` Check if this turning hallway overlaps other drawn components in the world if it is drawn. Though it has several private methods, their implementation principles are the same. They all use instance variable `detector` who will call `isDrawn(int x, int y)` method to check whether position (x, y) has drawn. If positions of every floors and every walls of this turning hallway has not been drawn yet, it indicates that this turning hallway does not overlap other drawn components. Otherwise, it overlaps.

5. `public boolean isOutOfWorld()` Check if this turning hallway will be drawn out of the world based on its position and shape. This method will be used when a turning hallway attempt to connect with a drawn room. It will return if any sides of the turning hallway is drawn out of one of the 4 sides of the world.

6. `public boolean closeDDDSSS()` 'DDD' refers to 'direction', which can be left, right, top and bottom; 'SSS' refers to the 'shape' of this turning hallway. The implementation of this set of methods are very similar. Take `closeTopL()` as an example:
* It will turn the top floor into a wall to make top of this turning wall encapsulated. 

## Algorithm - Part II: Iteractivity
The algorithms of implementing UI elements and avatars movements will be interpreted in this part.

### Avatar
An avatar has three main behaviours:
* Placed within the map
* Moving around within the map
* See if it meets an encounter

1. `public void randomlyPlace` - With the seed used to generated the pseudorandom map, a random position will be determined iteratively until the calculated random position is a floor. Then, place the floor at that position with this avatar.
2. `public boolean moveDDD` - 'DDD' refers to 'direction' which can be left, right, up and down. This set of methods has a smiliar logic:
* Get the position of this avatar
* Check if the position that the avatar will move into is a wall or a golden-colored wall. If it is either of two, return directly
* Or, change the appearance of map by setting avatar's original position to floor and position to be moved into to avatar's appearance.
* Update new position of the avatar.
3. `public boolean isMeet(Encounter encounter)` - Check if the encounter has already met the avatar before by calling `encounter.met()` and if the encounter and the avatar are in the same position.

### Encounter
1. `public void randomlyPlace` - With the seed used to generated the pseudorandom map, a random position will be determined iteratively until the calculated random position is a floor. Then, place the floor at that position with this encounter.
2. `public void hasMet()` - If an avatar has met with the encounter, change the state of encounter (instance variable `met`) to be true.

### Menu
An menu object will display two interfaces:
* The start menu displayed first to instruct user to type in a letter to start the game
* An interface telling user to add a seed used to generate a psuedorandom map.

In constructor, the basic setting, including size of and the background color of the interface, for drawing an interface will be set first. Then, two methods will be called to display two interfaces.

1. `public void display() ` This method will display the start menu. All methods are called based on the `StdDraw` library. As the constructor has set the background color to black, color pen, font and position will be set to write corresponding texts and show them off on the interface
2. `public void displayAddingSeed` This method will firstly clear the previous start menu, and then set new color pen, font and position to write correspoinding texts. Note that this interface should show the seed value that the user has entered so far.

### Small New Game
An small new game object will:
* Display a prompt page for users to tell them what happen when the avatar meets an encounter
* Display the interface of small game, add movement for avatar in this small game and end this small game if the avatar has picked up 10 flowers.

1. `private void displayPromt()` Using `StdDraw` library, clear the previous interface and set font and pen color. Draw the texts in the middle place of the interface and show them off. Let texts pause 1000 ms.
2. `public void display()`
* Show the prompt page for calling `displayPromt()` for a while.
* Draw a small room in the middle of the interface and draw an avatar in the random place of the drawn room.
* Draw 10 flowers randomly within the drawn room.
* Monitoring the movement of the avatar and if all flowers are picked up. If all have picked up, end this small game.