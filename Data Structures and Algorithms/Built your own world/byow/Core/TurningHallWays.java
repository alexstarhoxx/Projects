package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

import static byow.Core.RandomUtils.*;

/**
 * Create 4 types of turning of hallways at its corresponding position
 * by connecting a hallway on x direction and a hallway on y direction.
 * Both lengths of these hallways should greater or equal than 3 to make
 * an entire turning hallway.
 *
 * @author Alex Ho
 */
public class TurningHallWays {
    private int xlength;
    private int ylength;
    private String shape;
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

    /** Create a default turning hallways at position (0, 0) */
    public TurningHallWays(int xl, int yl, TETile[][] w) {
        xlength = validLength(xl);
        ylength = validLength(yl);
        shape = "L";
        position = new Position(0, 0);
        world = w;
        detector = new Detector(w);
    }

    public TurningHallWays(int xl, int yl, String s, int x, int y, TETile[][] w) {
        xlength = validLength(xl);
        ylength = validLength(yl);
        shape = s;
        position = new Position(x, y);
        world = w;
        detector = new Detector(w);
    }

    /** Valid if xlength or ylength smaller than 3 */
    private int validLength(int length) {
        if (length < 3) {
            length = 3;
        }
        return length;
    }

    public void drawL() {
        int x = position.x;
        int y = position.y;
        Hallways yHall = new Hallways(ylength, 'y', x, y, world);
        yHall.draw();

        x = x + 3;
        Hallways xHall = new Hallways(xlength, 'x', x, y, world);
        xHall.draw();
        digWallToFloor(position.x + 2, position.y + 1);
        fillFloorToWall(position.x + 1, position.y);
    }

    public void drawReverseL() {
        int x = position.x;
        int y = position.y;
        Hallways xHall = new Hallways(xlength, 'x', x, y, world);
        xHall.draw();

        x = x + xlength;
        Hallways yHall = new Hallways(ylength, 'y', x, y, world);
        yHall.draw();
        digWallToFloor(x, y + 1);
        fillFloorToWall(x + 1, y);
    }

    public void draw7() {
        int x = position.x;
        int y = position.y;
        Hallways xHall = new Hallways(xlength, 'x', x, y, world);
        xHall.draw();

        x = x + xlength;
        y = y - (ylength - 3);
        Hallways yHall = new Hallways(ylength, 'y', x, y, world);
        yHall.draw();
        digWallToFloor(position.x + xlength, position.y + 1);
        fillFloorToWall(position.x + xlength + 1, position.y + 2);
    }

    public void drawReverse7() {
        int x = position.x;
        int y = position.y;
        Hallways yHall = new Hallways(ylength, 'y', x, y, world);
        yHall.draw();

        x = x + 3;
        y = y + ylength - 3;
        Hallways xHall = new Hallways(xlength, 'x', x, y, world);
        xHall.draw();
        digWallToFloor(x - 1, y + 1);
        fillFloorToWall(x - 2, y + 2);
    }


    /* CONNECT ROOMS TO TURNING HALLWAYS */

    public boolean connectWith(Rooms room) {
        boolean closed = false;
        if (shape.equals("L") && !isConnectedTopL()) {
            closed = conTopLWith(room);
        } else if (shape.equals("L") && !isConnectedRightL()) {
            closed = conRightLWith(room);
        } else if (shape.equals("RL") && !isConnectedTopRL()) {
            closed = conTopReverLWith(room);
        } else if (shape.equals("RL") && !isConnectedLeftRL()) {
            closed = conLeftReverLWith(room);
        } else if (shape.equals("7") && !isConnectedLeft7()) {
            closed = conLeft7With(room);
        } else if (shape.equals("7") && !isConnectedBottom7()) {
            closed = conBottom7With(room);
        } else if (shape.equals("R7") && !isConnectedBottomR7()) {
            closed = conBottomRever7With(room);
        } else if (shape.equals("R7") && !isConnectedRightR7()) {
            closed = conRightRever7With(room);
        }
        return closed;
    }

    /** Check if right side of L-shaped turning hallway is connected with other components */
    private boolean isConnectedRightL() {
        int x = position.x + xlength + 3;
        int y = position.y;
        return detector.isWall(x, y) && detector.isFloor(x, y + 1)
                && detector.isWall(x, y + 2);
    }

    /** Check if top of L-shaped turning hallway is connected with other components */
    private boolean isConnectedTopL() {
        int x = position.x;
        int y = position.y + ylength;
        return detector.isWall(x, y) && detector.isFloor(x + 1, y)
                && detector.isWall(x + 2, y);
    }

    /** Check if top of reversed-L-shaped turning hallway is connected with other components */
    private boolean isConnectedTopRL() {
        int x = position.x + xlength;
        int y = position.y + ylength;
        return detector.isWall(x, y) && detector.isFloor(x + 1, y)
                && detector.isWall(x + 2, y);
    }

    /**
     * Check if left side of reversed-L-shaped turning hallway is connected
     * with other components
     */
    private boolean isConnectedLeftRL() {
        int x = position.x - 1;
        int y = position.y;
        return detector.isWall(x, y) && detector.isFloor(x, y + 1)
                && detector.isWall(x, y + 2);
    }

    /** Check if left side of 7-shaped turning hallway is connected with other components */
    private boolean isConnectedLeft7() {
        int x = position.x - 1;
        int y = position.y;
        return detector.isWall(x, y) && detector.isFloor(x, y + 1)
                && detector.isWall(x, y + 2);
    }

    /** Check if bottom of 7-shaped turning hallway is connected with other components */
    private boolean isConnectedBottom7() {
        int x = position.x + xlength;
        int y = position.y - ylength + 2;
        return detector.isWall(x, y) && detector.isFloor(x + 1, y)
                && detector.isWall(x + 2, y);
    }

    /**
     * Check if right side of reversed-7-shaped turning hallway is connected
     * with other components
     */
    private boolean isConnectedRightR7() {
        int x = position.x + xlength + 3;
        int y = position.y + ylength - 3;
        return detector.isWall(x, y) && detector.isFloor(x, y + 1)
                && detector.isWall(x, y + 2);
    }

    /** Check if bottom of reversed-7-shaped turning hallway is connected with other components */
    private boolean isConnectedBottomR7() {
        int x = position.x;
        int y = position.y - 1;
        return detector.isWall(x, y) && detector.isFloor(x + 1, y)
                && detector.isWall(x + 2, y);
    }


    /** Connect rooms to top of the turning hallway shaped L */
    public boolean conTopLWith(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.width() - 2);

        int rX = position.x - num;
        int rY = position.y + ylength - 1;

        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeTopL();
            return true;
        }

        room.draw();
        int conPointX = position.x + 1;
        int conPointY = rY;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect rooms to right side of the turning hallway shaped L */
    public boolean conRightLWith(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.height() - 2);

        int rX = position.x + xlength + 3;
        int rY = position.y - num;
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeRightL();
            return true;
        }

        room.draw();

        int conPointX = rX;
        int conPointY = position.y + 1;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect rooms to top of the turning hallway shaped reversed L */
    public boolean conTopReverLWith(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.width() - 2);

        int rX = position.x + xlength - num;
        int rY = position.y + ylength;
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeTopReverL();
            return true;
        }

        room.draw();

        int conPointX = position.x + xlength + 1;
        int conPointY = rY;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect rooms to left side of the turning hallway shaped reversed L */
    public boolean conLeftReverLWith(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.height() - 2);

        int rX = position.x - room.width();
        int rY = position.y - num;
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeLeftReverL();
            return true;
        }

        room.draw();

        int conPointX = position.x - 1;
        int conPointY = position.y + 1;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect rooms to the left side of the turning hallway shaped 7 */
    public boolean conLeft7With(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.height() - 2);

        int rX = position.x - room.width();
        int rY = position.y - num;
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeLeft7();
            return true;
        }

        room.draw();

        int conPointX = position.x - 1;
        int conPointY = position.y + 1;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect rooms to the bottom of the turning hallway shaped 7 */
    public boolean conBottom7With(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.width() - 2);

        int rX = position.x + xlength - num;
        int rY = position.y - (ylength - 3) - room.height();
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeBottom7();
            return true;
        }

        room.draw();

        int conPointX = position.x + xlength + 1;
        int conPointY = position.y - (ylength - 2);
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect rooms to the bottom of the turning hallway shaped reversed 7 */
    public boolean conBottomRever7With(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.width() - 2);

        int rX = position.x - num;
        int rY = position.y - room.height();
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeBottomRever7();
            return true;
        }

        room.draw();

        int conPointX = position.x + 1;
        int conPointY = position.y - 1;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect rooms to right side of the turning hallway shaped reversed 7 */
    public boolean conRightRever7With(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.height() - 2);

        int rX = position.x + xlength + 2;
        int rY = position.y + ylength - 3 - num;
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeRightRever7();
            return true;
        }

        room.draw();

        int conPointX = rX;
        int conPointY = position.y + ylength - 3 + 1;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Check if this turning hallway will overlap other components of the world */
    public boolean isOverlap() {
        boolean overlap = false;
        if (shape.equals("7")) {
            overlap = isOverlap7();
        } else if (shape.equals("R7")) {
            overlap = isOverlapR7();
        } else if (shape.equals("L")) {
            overlap = isOverlapL();
        } else if (shape.equals("RL")) {
            overlap = isOverlapRL();
        }
        return overlap;
    }

    private boolean isOverlap7() {
        boolean overlap = false;
        for (int x = position.x; x < position.x + xlength; x++) {
            for (int y = position.y; y < position.y + 3; y++) {
                if (detector.isDrawn(x, y)) {
                    overlap = true;
                    break;
                }
            }
        }

        int newX = position.x + xlength;
        int newY = position.y + 2;
        for (int x = newX; x < newX + 3; x++) {
            for (int y = newY; y > newY - ylength; y--) {
                if (detector.isDrawn(x, y)) {
                    overlap = true;
                    break;
                }
            }
        }
        return overlap;
    }

    private boolean isOverlapR7() {
        boolean overlap = false;
        for (int x = position.x; x < position.x + 3; x++) {
            for (int y = position.y; y < position.y + ylength; y++) {
                if (detector.isDrawn(x, y)) {
                    overlap = true;
                    break;
                }
            }
        }

        int newX = position.x + 3;
        int newY = position.y + ylength - 3;
        for (int x = newX; x < newX + xlength; x++) {
            for (int y = newY; y < newY + 3; y++) {
                if (detector.isDrawn(x, y)) {
                    overlap = true;
                    break;
                }
            }
        }
        return overlap;
    }

    private boolean isOverlapL() {
        boolean overlap = false;
        for (int x = position.x; x < position.x + 3; x++) {
            for (int y = position.y; y < position.y + ylength; y++) {
                if (detector.isDrawn(x, y))  {
                    overlap = true;
                    break;
                }
            }
        }

        int newX = position.x + 3;
        for (int x = newX; x < newX + xlength; x++) {
            for (int y = position.y; y < position.y + 3; y++) {
                if (detector.isDrawn(x, y)) {
                    overlap = true;
                    break;
                }
            }
        }
        return overlap;
    }

    private boolean isOverlapRL() {
        boolean overlap = false;
        for (int x = position.x; x < position.x + xlength; x++) {
            for (int y = position.y; y < position.y + 3; y++) {
                if (detector.isDrawn(x, y)) {
                    overlap = true;
                    break;
                }
            }
        }

        int newX = position.x + xlength;
        for (int x = newX; x < newX + 3; x++) {
            for (int y = position.y; y < position.y + ylength; y++) {
                if (detector.isDrawn(x, y)) {
                    overlap = true;
                    break;
                }
            }
        }
        return overlap;
    }


    /** Check if this turning hallway will be drawn out of the world */
    public boolean isOutOfWorld() {
        boolean outOfWorld = false;
        if (shape.equals("7")) {
            outOfWorld = position.x <= 0 || position.y + 2 >= worldHeight() - 1
                    || position.x + xlength + 2 > worldWidth() - 1
                    || position.y - (ylength - 3) <= 0;
        } else if (shape.equals("R7")) {
            outOfWorld = position.y <= 0 || position.x + xlength + 2 >= worldWidth() - 1
                    || position.x < 0 || position.y + ylength - 1 >= worldHeight() - 1;
        } else if (shape.equals("L")) {
            outOfWorld = position.y + ylength - 1 >= worldHeight() - 1 || position.x < 0
                    || position.y < 0 || position.x + xlength + 2 >= worldWidth() - 1;
        } else if (shape.equals("RL")) {
            outOfWorld = position.x <= 0 || position.x + xlength + 2 > worldWidth() - 1
                    || position.y < 0 || position.y + ylength - 1 >= worldHeight() - 1;
        }

        return outOfWorld;
    }

    private void digWallToFloor(int x, int y) {
        world[x][y] = Tileset.FLOOR;
    }

    private void fillFloorToWall(int x, int y) {
        world[x][y] = Tileset.WALL;
    }

    /** Close the top of a L-shaped turning hallway */
    public void closeTopL() {
        world[position.x + 1][position.y + ylength - 1] = Tileset.WALL;
    }

    /** Close the right side of a L-shaped turning hallway */
    public void closeRightL() {
        world[position.x + 2 + xlength][position.y + 1] = Tileset.WALL;
    }

    /** Close the top of a reversed-L-shaped turning hallway */
    public void closeTopReverL() {
        world[position.x + xlength + 1][position.y + ylength - 1] = Tileset.WALL;
    }

    /** Close the left side of a reversed-L-shaped turning hallway */
    public void closeLeftReverL() {
        world[position.x][position.y + 1] = Tileset.WALL;
    }

    /** Close the left side of a 7-shaped turning hallway */
    public void closeLeft7() {
        world[position.x][position.y + 1] = Tileset.WALL;
    }

    /** Close the bottom of a 7-shaped turning hallway */
    public void closeBottom7() {
        world[position.x + 1 + xlength][position.y - ylength + 3] = Tileset.WALL;
    }

    /** Close the bottom a reversed-7-shaped turning hallway */
    public void closeBottomRever7() {
        world[position.x + 1][position.y] = Tileset.WALL;
    }

    /** Close the right side of a reversed-7-shaped turning hallway */
    public void closeRightRever7() {
        world[position.x + xlength + 2][position.y + ylength - 2] = Tileset.WALL;
    }

    /** Change this turing hallways position to new location (NEWX, NEWY) */
    public void changePositionTo(int newX, int newY) {
        position.changePositionTo(newX, newY);
    }

    /** Return value of xlength */
    public int xlength() {
        return xlength;
    }

    /** Return value of ylength */
    public int ylength() {
        return ylength;
    }

    /** Return the shape of this turning hallway */
    public String shape() {
        return this.shape;
    }

    public int worldHeight() {
        return world[0].length;
    }

    public int worldWidth() {
        return world.length;
    }

    public void changeShapeTo(String newShape) {
        shape = newShape;
    }
}
