package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class Hallways {
    private int length;
    private char direction;
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

    /** Create a default hallway whose direction is x and at the position (0, 0) */
    public Hallways(int l, TETile[][] w) {
        length = l;
        direction = 'x';
        position = new Position(0, 0);
        world = w;
        detector = new Detector(w);
    }

    /** Create a default hallway whose position is (0, 0) */
    public Hallways(int l, char d, TETile[][] w) {
        length = l;
        direction = d;
        position = new Position(0, 0);
        world = w;
        detector = new Detector(w);
    }

    public Hallways(int l, char d, int x, int y, TETile[][] w) {
        length = l;
        direction = d;
        position = new Position(x, y);
        world = w;
        detector = new Detector(w);
    }


    /* DRAW HALLWAYS in THE WORLD */

    /** Draw a hallway in the map */
    public void draw() {
        if (direction == 'x') {
            drawTwoSidesWallsXDir();
            drawOneLineFloorsXDir();
        } else if (direction == 'y') {
            drawTwoSidesWallsYDir();
            drawOneLineFloorsYDir();
        }
    }

    /** Draw two sides of walls on x coordinate direction without
     *  drawing the floors
     */
    private void drawTwoSidesWallsXDir() {
        int x = position.x;
        int y = position.y;

        for (int i = x; i < x + length; i++) {
            world[i][y] = Tileset.WALL;
        }

        y = y + 2;
        for (int i = x; i < x + length; i++) {
            world[i][y] = Tileset.WALL;
        }
    }

    /** Draw a line of floors on x coordinate direction */
    private void drawOneLineFloorsXDir() {
        int x = position.x;
        int y = position.y + 1;

        for (int i = x; i < x + length; i++) {
            world[i][y] = Tileset.FLOOR;
        }
    }

    /** Draw a line of walls on y coordinate direction without
     *  drawing the floors
     */
    private void drawTwoSidesWallsYDir() {
        int x = position.x;
        int y = position.y;

        for (int i = y; i < y + length; i++) {
            world[x][i] = Tileset.WALL;
        }

        x = x + 2;
        for (int i = y; i < y + length; i++) {
            world[x][i] = Tileset.WALL;
        }
    }

    /** Draw a line of floors on y coordinate direction */
    private void drawOneLineFloorsYDir() {
        int x = position.x + 1;
        int y = position.y;

        for (int i = y; i < y + length; i++) {
            world[x][i] = Tileset.FLOOR;
        }
    }


    /* CONNECT ROOMS TO THIS HALLWAY */

    /** Connect rooms to hallways */
    public boolean connectWith(Rooms room) {
        boolean closed = false;
        if (direction == 'x' && !isConnectedLeft()) {
            closed = connectLeftWith(room);
        } else if (direction == 'x' && !isConnectedRight()) {
            closed = connectRightWith(room);
        } else if (direction == 'y' && !isConnectedTop()) {
            closed = connectTopWith(room);
        } else if (direction == 'y' && !isConnectedBottom()) {
            closed = connectBottomWith(room);
        }
        return closed;
    }

    /** Check if left side of horizon hallway is connected with other components */
    private boolean isConnectedLeft() {
        int x = position.x - 1;
        int y = position.y;
        return detector.isWall(x, y) && detector.isFloor(x, y + 1)
                && detector.isWall(x, y + 2);
    }

    /** Check if right side of horizon hallway is connected with other components */
    private boolean isConnectedRight() {
        int x = position.x + length;
        int y = position.y;
        return detector.isWall(x, y) && detector.isFloor(x, y + 1)
                && detector.isWall(x, y + 2);
    }

    /** Check if top side of vertical hallway is connected with other components */
    private boolean isConnectedTop() {
        int x = position.x;
        int y = position.y + length;
        return detector.isWall(x, y) && detector.isFloor(x + 1, y)
                && detector.isWall(x + 2, y);
    }

    /** Check if bottom side of vertical hallway is connected with other components */
    private boolean isConnectedBottom() {
        int x = position.x;
        int y = position.y - 1;
        return detector.isWall(x, y) && detector.isFloor(x + 1, y)
                && detector.isWall(x + 2, y);
    }

    /** Connect this hallway with a room on left side of this hallway randomly */
    private boolean connectLeftWith(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.height() - 2);

        int rX = position.x - room.width();
        int rY = position.y - num;
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeLeft();
            return true;
        }

        room.draw();

        int conPointX = position.x - 1;
        int conPointY = position.y + 1;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect this hallway with a room on right side of this hallway randomly */
    private boolean connectRightWith(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.height() - 2);

        int rX = position.x + length;
        int rY = position.y - num;
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeRight();
            return true;
        }

        room.draw();

        int conPointX = position.x + length;
        int conPointY = position.y + 1;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect this hallway with a room on bottom side of this hallway randomly */
    private boolean connectBottomWith(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.width() - 2);

        int rX = position.x - num;
        int rY = position.y - room.height();
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeBottom();
            return true;
        }

        room.draw();

        int conPointX = position.x + 1;
        int conPointY = position.y - 1;
        digWallToFloor(conPointX, conPointY);
        return false;
    }

    /** Connect this hallway with a room on top side of this hallway randomly */
    private boolean connectTopWith(Rooms room) {
        Random random = MapGenerator.random();
        int num = uniform(random, room.width() - 2);

        int rX = position.x - num;
        int rY = position.y + length;
        room.changePositionTo(rX, rY);

        if (room.isOutOfWorld() || room.isOverlap()) {
            closeTop();
            return true;
        }

        room.draw();

        int conPointX = position.x + 1;
        int conPointY = position.y + length;
        digWallToFloor(conPointX, conPointY);
        return false;
    }


    /* Check overlapping or being out of the world */

    /** Check if this hallway will overlap other components in the world if to be drawn */
    public boolean isOverlap() {
        boolean overlap = false;
        if (direction == 'x') {
            overlap = isOverlapX();
        } else if (direction == 'y') {
            overlap = isOverlapY();
        }
        return overlap;
    }

    private boolean isOverlapX() {
        boolean drawn = false;
        for (int x = position.x; x <= position.x + length - 1; x++) {
            for (int y = position.y; y <= position.y + 2; y++) {
                if (detector.isDrawn(x, y)) {
                    drawn = true;
                }
            }
        }
        return drawn;
    }

    private boolean isOverlapY() {
        boolean drawn = false;
        for (int y = position.y; y <= position.y + length - 1; y++) {
            for (int x = position.x; x <= position.x + 2; x++) {
                if (detector.isDrawn(x, y)) {
                    drawn = true;
                }
            }
        }
        return drawn;
    }

    /** Check if this hallway will be drawn out of the world */
    public boolean isOutOfWorld() {
        boolean outOfWorld = false;
        if (direction == 'x') {
            outOfWorld = position.x <= 0 || position.x + length > worldWidth() - 1;
        } else if (direction == 'y') {
            outOfWorld = position.y <= 0 || length + position.y >= worldHeight();
        }
        return outOfWorld;
    }


    /* Other useful methods */

    private void digWallToFloor(int x, int y) {
        world[x][y] = Tileset.FLOOR;
    }

    /** Close the top of a connected hallway */
    public void closeTop() {
        world[position.x + 1][position.y + length - 1] = Tileset.WALL;
    }

    /** Close the bottom of a connected hallway */
    public void closeBottom() {
        world[position.x + 1][position.y] = Tileset.WALL;
    }

    /** Close the left-most side of a connected hallway */
    public void closeLeft() {
        world[position.x][position.y + 1] = Tileset.WALL;
    }

    /** Close the right-most side of a connected hallway */
    public void closeRight() {
        world[position.x + length - 1][position.y + 1] = Tileset.WALL;
    }

    /** Change this turing hallways position to new location (NEWX, NEWY) */
    public void changePositionTo(int newX, int newY) {
        position.changePositionTo(newX, newY);
    }

    /** Change this hallway's direction to NEWDIR */
    public void changeDirTo(char newDir) {
        direction = newDir;
    }

    /** Return length of this hallway */
    public int length() {
        return length;
    }

    public int worldHeight() {
        return world[0].length;
    }

    public int worldWidth() {
        return world.length;
    }
}
