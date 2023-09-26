package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

import static byow.Core.RandomUtils.uniform;

/**
 * Create rooms with random width, height and shape.
 * To create a valid room, width and height should greater or equal to 3.
 * @author Alex Ho
 */
public class Rooms {
    private int width;
    private int height;
    private Position position;
    private TETile[][] world;
    private Detector detector;

    private static class Position {
        private int x;
        private int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void changePositionTo(int newX, int newY) {
            x = newX;
            y = newY;
        }
    }

    /** Create a room at default position (0, 0) */
    public Rooms(int wd, int h, TETile[][] w) {
        width = validLength(wd);
        height = validLength(h);
        position = new Position(0, 0);
        world = w;
        detector = new Detector(w);
    }

    public Rooms(int wd, int h, int x, int y, TETile[][] w) {
        width = validLength(wd);
        height = validLength(h);
        position = new Position(x, y);
        world = w;
        detector = new Detector(w);
    }

    private int validLength(int length) {
        if (length < 3) {
            return 3;
        }
        return length;
    }

    /* DRAW A ROOM IN THE WORLD */

    public void draw() {
        drawTopAndBottomWalls();
        drawLeftAndRightWalls();
        fillFloors();
    }

    private void fillFloors() {
        int x = position.x + 1;
        int y = position.y + 1;
        for (int i = x; i < x + width - 2; i++) {
            for (int j = y; j < y + height - 2; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    private void drawLeftAndRightWalls() {
        int x = position.x;
        int y = position.y;

        for (int i = y; i < y + height; i++) {
            world[x][i] = Tileset.WALL;
        }

        x = x + width - 1;
        for (int i = y; i < y + height; i++) {
            world[x][i] = Tileset.WALL;
        }
    }

    private void drawTopAndBottomWalls() {
        int x = position.x;
        int y = position.y;

        for (int i = x; i < x + width; i++) {
            world[i][y] = Tileset.WALL;
        }

        y = y + height - 1;
        for (int i = x; i < x + width; i++) {
            world[i][y] = Tileset.WALL;
        }
    }


    /* CONNECT ROOMS WITH HALLWAYS */

    /** Connect rooms with hallways randomly */
    public boolean connectWith(Hallways hw) {
        int num = -1;
        boolean closed = false;
        boolean[] connected = new boolean[4];
        connected[0] = isConnectedTop();
        connected[1] = isConnectedBottom();
        connected[2] = isConnectedLeft();
        connected[3] = isConnectedRight();

        // Randomly choose an unconnected side to do the connection
        while (true) {
            int ranNum = MapGenerator.random().nextInt(4);
            if (!connected[ranNum]) {
                num = ranNum;
                break;
            }
        }

        closed = callConHw(hw, num);
        return closed;
    }

    /** Call hallways-connection-related methods by passing its corresponding number NUM */
    private boolean callConHw(Hallways hw, int num) {
        switch (num) {
            case 0:
                return conTopEdgeWith(hw);
            case 1:
                return conBottomEdgeWith(hw);
            case 2:
                return conLeftEdgeWith(hw);
            default:
                return conRightEdgeWith(hw);
        }
    }

    /** The top line of this room will connect vertical hallways HW
     *  randomly. The result will be drawn on WORLD.
     */
    private boolean conTopEdgeWith(Hallways hw) {
        hw.changeDirTo('y'); // Make sure the direction is vertical
        Random random = MapGenerator.random();

        // Randomly choose a position to dig wall to floor
        int conPointX = position.x + 1 + uniform(random, this.width - 2);
        int conPointY = position.y + this.height - 1;
        hw.changePositionTo(conPointX - 1, conPointY + 1);

        if (hw.isOutOfWorld() || hw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        hw.draw();
        return false;
    }

    /** The bottom line of this room will connect vertical hallways HW
     *  randomly. The result will be drawn on WORLD.
     */
    private boolean conBottomEdgeWith(Hallways hw) {
        hw.changeDirTo('y'); // Make sure the direction is vertical
        Random random = MapGenerator.random();

        // Randomly choose a position to dig wall to floor
        int conPointX = position.x + 1 + uniform(random, this.width - 2);
        int conPointY = position.y;
        hw.changePositionTo(conPointX - 1, conPointY - hw.length());

        if (hw.isOutOfWorld() || hw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        hw.draw();
        return false;
    }

    /** The left line of this room will connect horizon hallways HW
     *  randomly. The result will be drawn on WORLD.
     */
    private boolean conLeftEdgeWith(Hallways hw) {
        hw.changeDirTo('x'); // Make sure the direction is horizon
        Random random = MapGenerator.random();

        int conPointX = position.x;
        // Randomly choose a position to dig wall to floor
        int conPointY = position.y + 1 + uniform(random, this.height - 2);

        hw.changePositionTo(conPointX - hw.length(), conPointY - 1);

        if (hw.isOutOfWorld() || hw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        hw.draw();
        return false;
    }

    /** The right line of this room will connect horizon hallways HW
     *  randomly. The result will be drawn on WORLD.
     */
    private boolean conRightEdgeWith(Hallways hw) {
        hw.changeDirTo('x'); // Make sure the direction is horizon
        Random random = MapGenerator.random();

        int conPointX = position.x + this.width - 1;
        // Randomly choose a position to dig wall to floor
        int conPointY = position.y + 1 + uniform(random, this.height - 2);

        hw.changePositionTo(conPointX + 1, conPointY - 1);

        if (hw.isOutOfWorld() || hw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        hw.draw();
        return false;
    }


    /* CONNECT ROOMS WITH TURNING HALLWAYS */

    /** Connect rooms with turning hallways randomly */
    public boolean connectWith(TurningHallWays thw) {
        int num = -1;
        boolean closed = false;
        boolean[] connected = new boolean[4];
        connected[0] = isConnectedTop();
        connected[1] = isConnectedBottom();
        connected[2] = isConnectedLeft();
        connected[3] = isConnectedRight();

        // Randomly choose an unconnected side to do the connection
        while (true) {
            int ranNum = MapGenerator.random().nextInt(4);
            if (!connected[ranNum]) {
                num = ranNum;
                break;
            }
        }

        closed = callConThw(thw, num);
        return closed;
    }

    /** Call turning-hallways-connection-related methods by passing
     *  its corresponding number NUM.
     */
    private boolean callConThw(TurningHallWays thw, int num) {
        switch (num) {
            case 0:
                return conTopEdgeWith(thw);
            case 1:
                return conBottomEdgeWith(thw);
            case 2:
                return conLeftEdgeWith(thw);
            default:
                return conRightEdgeWith(thw);
        }
    }

    /** Check if top of this room is connected with other components */
    private boolean isConnectedTop() {
        int y = position.y + height - 1;
        boolean connected = false;
        for (int x = position.x; x < position.x + width; x++) {
            if (detector.isLockedDoor(x, y) || detector.isFloor(x, y)) {
                connected = true;
            }
        }
        return connected;
    }

    /** Check if bottom side of this room is connected with other components */
    private boolean isConnectedBottom() {
        boolean connected = false;
        for (int x = position.x; x < position.x + width; x++) {
            if (detector.isLockedDoor(x, position.y) || detector.isFloor(x, position.y)) {
                connected = true;
            }
        }
        return connected;
    }

    /** Check if left side of this room is connected with other components */
    private boolean isConnectedLeft() {
        boolean connected = false;
        for (int y = position.y; y < position.y + height; y++) {
            if (detector.isLockedDoor(position.x, y) || detector.isFloor(position.x, y)) {
                connected = true;
            }
        }
        return connected;
    }

    /** Check if right side of this room is connected with other components */
    private boolean isConnectedRight() {
        int x = position.x + width - 1;
        boolean connected = false;
        for (int y = position.y; y < position.y + height; y++) {
            if (detector.isLockedDoor(x, y) || detector.isFloor(x, y)) {
                connected = true;
            }
        }
        return connected;
    }

    /** The top line of this room will connect turning hallways THW
     *  shaped 7 or reverse 7 randomly. The result will be drawn on WORLD.
     */
    public boolean conTopEdgeWith(TurningHallWays thw) {
        thw.changeShapeTo(randomTopShape());
        boolean closed = false;
        int rX = position.x + 1;
        int rY = position.y + this.height - 1;

        Random random = MapGenerator.random();
        // Randomly choose a position to dig wall to floor
        int conPointX = rX + uniform(random, this.width - 2);

        if (thw.shape().equals("7")) {
            closed = conTop7(thw, conPointX, rY);
        } else if (thw.shape().equals("R7")) {
            closed = conTopReverse7(thw, conPointX, rY);
        }
        return closed;
    }

    private String randomTopShape() {
        int num = MapGenerator.random().nextInt(2);
        switch (num) {
            case 0:
                return "7";
            default:
                return "R7";
        }
    }

    /** Connect top line of this room and turning hallways shaped 7
     *  The connecting point is the point to be dug from wall into floor.
     *  @param thw - the turning hallway to be connected to this room
     *  @param conPointX - the x value of connecting point of room and hallway.
     *  @param conPointY - the y value of connecting point of room and hallway.
     */
    private boolean conTop7(TurningHallWays thw, int conPointX, int conPointY) {
        int thwNewX = conPointX - thw.xlength() - 1;
        int thwNewY = conPointY + thw.ylength() - 2;
        thw.changePositionTo(thwNewX, thwNewY);

        // If HW will be drawn out of the world, do not draw it and make connection with this room
        if (thw.isOutOfWorld() || thw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        thw.draw7();
        return false;
    }

    /** Connect top line of this room and turning hallways shaped Reverse 7
     *  The connecting point is the point to be dug from wall into floor.
     *  @param thw - the turning hallway to be connected to this room
     *  @param conPointX - the x value of connecting point of room and hallway.
     *  @param conPointY - the y value of connecting point of room and hallway.
     */
    private boolean conTopReverse7(TurningHallWays thw, int conPointX, int conPointY) {
        int thwNewX = conPointX - 1;
        int thwNewY = conPointY + 1;
        thw.changePositionTo(thwNewX, thwNewY);

        // If HW will be drawn out of the world, do not draw it and make connection with this room
        if (thw.isOutOfWorld() || thw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        thw.drawReverse7();
        return false;
    }

    /** The bottom line of this room will connect turning hallways THW
     *  shaped L or reverse L randomly. The result will be drawn on WORLD.
     */
    public boolean conBottomEdgeWith(TurningHallWays thw) {
        thw.changeShapeTo(randomBottomShape());
        boolean closed = false;
        int rX = position.x + 1;
        int rY = position.y;

        Random random = MapGenerator.random();
        // Randomly choose a position to dig wall to floor
        int conPointX = rX + uniform(random, this.width - 2);

        if (thw.shape().equals("RL")) {
            closed = conBottomReverL(thw, conPointX, rY);
        } else if (thw.shape().equals("L")) {
            closed = conBottomL(thw, conPointX, rY);
        }
        return closed;
    }

    private String randomBottomShape() {
        int num = MapGenerator.random().nextInt(2);
        switch (num) {
            case 0:
                return "L";
            default:
                return "RL";
        }
    }

    /** Connect bottom line of this room and turning hallways shaped L
     *  The connecting point is the point to be dug from wall into floor.
     *  @param thw - the turning hallway to be connected to this room
     *  @param conPointX - the x value of connecting point.
     *  @param conPointY - the y value of connecting point.
     */
    private boolean conBottomL(TurningHallWays thw, int conPointX, int conPointY) {
        int thwNewX = conPointX - 1;
        int thwNewY = conPointY - thw.ylength();
        thw.changePositionTo(thwNewX, thwNewY);

        // If HW will be drawn out of the world, do not draw it and make connection with this room
        if (thw.isOutOfWorld() || thw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        thw.drawL();
        return false;
    }

    /** Connect bottom line of this room and turning hallways shaped Reverse L
     *  The connecting point is the point to be dug from wall into floor.
     *  @param thw - the turning hallway to be connected to this room
     *  @param conPointX - the x value of connecting point.
     *  @param conPointY - the y value of connecting point.
     */
    private boolean conBottomReverL(TurningHallWays thw, int conPointX, int conPointY) {
        int thwNewX = conPointX - thw.xlength() - 1;
        int thwNewY = conPointY - thw.ylength();
        thw.changePositionTo(thwNewX, thwNewY);

        // If HW will be drawn out of the world, do not draw it and make connection with this room
        if (thw.isOutOfWorld() || thw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        thw.drawReverseL();
        return false;
    }

    /** The left line of this room will connect turning hallways THW
     *  shaped L or reverse 7 randomly. The result will be drawn on WORLD.
     */
    public boolean conLeftEdgeWith(TurningHallWays thw) {
        thw.changeShapeTo(randomLeftShape());
        boolean closed = false;
        int rX = position.x;
        int rY = position.y + 1;

        Random random = MapGenerator.random();
        // Randomly choose a position to dig wall to floor
        int conPointY = rY + uniform(random, this.height - 2);

        if (thw.shape().equals("L")) {
            closed = conLeftL(thw, rX, conPointY);
        } else if (thw.shape().equals("R7")) {
            closed = conLeftRever7(thw, rX, conPointY);
        }
        return closed;
    }

    private String randomLeftShape() {
        int num = MapGenerator.random().nextInt(2);
        switch (num) {
            case 0:
                return "L";
            default:
                return "R7";
        }
    }

    /** Connect left line of this room and turning hallways shaped Reverse 7
     *  The connecting point is the point to be dug from wall into floor.
     *  @param thw - the turning hallway to be connected to this room
     *  @param conPointX - the x value of connecting point.
     *  @param conPointY - the y value of connecting point.
     */
    private boolean conLeftRever7(TurningHallWays thw, int conPointX, int conPointY) {
        int thwNewX = conPointX - (thw.xlength() + 2);
        int thwNewY = conPointY - (thw.ylength() - 2);
        thw.changePositionTo(thwNewX, thwNewY);

        // If HW will be drawn out of the world, do not draw it and make connection with this room
        if (thw.isOutOfWorld() || thw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        thw.drawReverse7();
        return false;
    }

    /** Connect left line of this room and turning hallways shaped L
     *  The connecting point is the point to be dug from wall into floor.
     *  @param thw - the turning hallway to be connected to this room
     *  @param conPointX - the x value of connecting point.
     *  @param conPointY - the y value of connecting point.
     */
    private boolean conLeftL(TurningHallWays thw, int conPointX, int conPointY) {
        int thwNewX = conPointX - (thw.xlength() + 2);
        int thwNewY = conPointY - 1;
        thw.changePositionTo(thwNewX, thwNewY);

        // If HW will be drawn out of the world, do not draw it and make connection with this room
        if (thw.isOutOfWorld() || thw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        thw.drawL();
        return false;
    }

    /** The right line of this room will connect turning hallways THW
     *  shaped 7 or reverse L randomly. The result will be drawn on WORLD.
     */
    public boolean conRightEdgeWith(TurningHallWays thw) {
        thw.changeShapeTo(randomRightShape());
        boolean closed = false;
        int rX = position.x + this.width - 1;
        int rY = position.y + 1;

        Random random = MapGenerator.random();
        // Randomly choose a position to dig wall to floor
        int conPointY = rY + uniform(random, this.height - 2);

        if (thw.shape().equals("7")) {
            closed = conRight7(thw, rX, conPointY);
        } else if (thw.shape().equals("RL")) {
            closed = conRightReverL(thw, rX, conPointY);
        }
        return closed;
    }

    private String randomRightShape() {
        int num = MapGenerator.random().nextInt(2);
        switch (num) {
            case 0:
                return "7";
            default:
                return "RL";
        }
    }

    /** Connect right line of this room and turning hallways shaped 7
     *  The connecting point is the point to be dug from wall into floor.
     *  @param thw - the turning hallway to be connected to this room
     *  @param conPointX - the x value of connecting point.
     *  @param conPointY - the y value of connecting point.
     */
    private boolean conRight7(TurningHallWays thw, int conPointX, int conPointY) {
        int thwNewX = conPointX + 1;
        int thwNewY = conPointY - 1;
        thw.changePositionTo(thwNewX, thwNewY);

        // If HW will be drawn out of the world, do not draw it and make connection with this room
        if (thw.isOutOfWorld() || thw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        thw.draw7();
        return false;
    }

    /** Connect right line of this room and turning hallways shaped reversed L
     *  The connecting point is the point to be dug from wall into floor.
     *  @param thw - the turning hallway to be connected to this room
     *  @param conPointX - the x value of connecting point.
     *  @param conPointY - the y value of connecting point.
     */
    private boolean conRightReverL(TurningHallWays thw, int conPointX, int conPointY) {
        int thwNewX = conPointX + 1;
        int thwNewY = conPointY - 1;
        thw.changePositionTo(thwNewX, thwNewY);

        // If HW will be drawn out of the world, do not draw it and make connection with this room
        if (thw.isOutOfWorld() || thw.isOverlap()) {
            return true;
        }

        digWallToFloor(conPointX, conPointY);
        thw.drawReverseL();
        return false;
    }


    /* Check overlapping or being out of the world */

    /** Check if this room will be drawn out of the world */
    public boolean isOutOfWorld() {
        int x = position.x;
        int y = position.y;
        return x < 0 || y < 0 || y + height > worldHeight() - 2
                || x + width > worldWidth() - 2;
    }

    /** Check if this room will overlap other components in the world if to be drawn */
    public boolean isOverlap() {
        boolean overlap = false;
        for (int y = position.y; y < position.y + height; y++) {
            for (int x = position.x; x < position.x + width; x++) {
                if (detector.isDrawn(x, y)) {
                    overlap = true;
                }
            }
        }
        return overlap;
    }


    /* Randomly add the golden-colored wall to the initialized room */

    /** Turn a random wall into a golden colored wall in a room */
    public void addGoldenWall() {
        int num = MapGenerator.random().nextInt(4);
        switch (num) {
            case 0:
                addTopGolden();
                break;
            case 1:
                addBottomGolden();
                break;
            case 2:
                addLeftGolden();
                break;
            default:
                addRightGolden();
                break;
        }
    }

    /** Turn a random wall of top of a room into a golden colored one */
    private void addTopGolden() {
        int y = position.y + height - 1;
        Random random = MapGenerator.random();
        int x = uniform(random, position.x + 1, position.x + width - 1);
        world[x][y] = Tileset.LOCKED_DOOR;
    }

    /** Turn a random wall of bottom of a room into a golden colored one */
    private void addBottomGolden() {
        int y = position.y;
        Random random = MapGenerator.random();
        int x = uniform(random, position.x + 1, position.x + width - 1);
        world[x][y] = Tileset.LOCKED_DOOR;
    }

    /** Turn a random wall of left side of a room into a golden colored one */
    private void addLeftGolden() {
        int x = position.x;
        Random random = MapGenerator.random();
        int y = uniform(random, position.y + 1, position.y + height - 1);
        world[x][y] = Tileset.LOCKED_DOOR;
    }

    /** Turn a random wall of right side of a room into a golden colored one */
    private void addRightGolden() {
        int x = position.x + width - 1;
        Random random = MapGenerator.random();
        int y = uniform(random, position.y + 1, position.y + height - 1);
        world[x][y] = Tileset.LOCKED_DOOR;
    }

    private void digWallToFloor(int x, int y) {
        world[x][y] = Tileset.FLOOR;
    }


    /* Other useful methods */

    /** Change this turing hallways position to new location (NEWX, NEWY) */
    public void changePositionTo(int newX, int newY) {
        position.changePositionTo(newX, newY);
    }

    /** Return weight of this room */
    public int width() {
        return this.width;
    }

    /** Return height of this room */
    public int height() {
        return this.height;
    }

    private int worldHeight() {
        return world[0].length;
    }

    private int worldWidth() {
        return world.length;
    }
}
