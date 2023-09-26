package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

import static byow.Core.RandomUtils.*;

/**
 * This is some sort of on screen representation controlled by the user.
 * The user can control it through using W, A, S and D keys on the keyboard.
 */
public class Avatar implements Serializable {
    private static final TETile LOOK = Tileset.AVATAR;
    private Position position;
    private long seed;
    private TETile[][] world;
    private Detector detector;

    private static class Position implements Serializable {
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

    public Avatar(long s, TETile[][] w) {
        seed = s;
        world = w;
        position = new Position(0, 0);
        detector = new Detector(w);
    }

    /** Place this avatar to its position */
    public void place() {
        world[position.x][position.y] = LOOK;
    }

    /** Place this avatar to the given position (x, y) */
    public void placeAt(int x, int y) {
        world[x][y] = LOOK;
    }

    /** Randomly place this avatar to the randomly generated world(map)
     *  and set this avatar's position.
     */
    public void randomlyPlace() {
        int x;
        int y;
        Random rand = new Random(seed);
        do {
            x = uniform(rand, worldWidth());
            y = uniform(rand, worldHeight() - 1);
        } while (!detector.isFloor(x, y));
        placeAt(x, y);
        position.x = x;
        position.y = y;
    }

    /** Move up one step, if it meets the golden-colored wall, return true */
    public boolean moveUp() {
        int x = position.x;
        int y = position.y;
        if (detector.isWall(x, y + 1)) {
            return false;
        } else if (detector.isLockedDoor(x, y + 1)) {
            return true;
        }

        world[x][y] = Tileset.FLOOR;
        world[x][y + 1] = LOOK;
        position.changePositionTo(x, y + 1);
        return false;
    }

    /** Move down one step, if it meets the golden-colored wall, return true */
    public boolean moveDown() {
        int x = position.x;
        int y = position.y;
        if (detector.isWall(x, y - 1)) {
            return false;
        } else if (detector.isLockedDoor(x, y - 1)) {
            return true;
        }

        world[x][y] = Tileset.FLOOR;
        world[x][y - 1] = LOOK;
        position.changePositionTo(x, y - 1);
        return false;
    }

    /** Move left one step, if it meets the golden-colored wall, return true */
    public boolean moveLeft() {
        int x = position.x;
        int y = position.y;
        if (detector.isWall(x - 1, y)) {
            return false;
        } else if (detector.isLockedDoor(x - 1, y)) {
            return true;
        }

        world[x][y] = Tileset.FLOOR;
        world[x - 1][y] = LOOK;
        position.changePositionTo(x - 1, y);
        return false;
    }

    /** Move right one step, if it meets the golden-colored wall, return true */
    public boolean moveRight() {
        int x = position.x;
        int y = position.y;
        if (detector.isWall(x + 1, y)) {
            return false;
        } else if (detector.isLockedDoor(x + 1, y)) {
            return true;
        }

        world[x][y] = Tileset.FLOOR;
        world[x + 1][y] = LOOK;
        position.changePositionTo(x + 1, y);
        return false;
    }

    /** Check if this avatar meets the encounter */
    public boolean isMeet(Encounter encounter) {
        return !encounter.met() && position.x == encounter.xPos()
                && position.y == encounter.yPos();
    }

    public TETile[][] world() {
        return world;
    }

    public int worldHeight() {
        return world[0].length;
    }

    public int worldWidth() {
        return world.length;
    }

    /** Return the seed of the encounter */
    public long seed() {
        return seed;
    }
}
