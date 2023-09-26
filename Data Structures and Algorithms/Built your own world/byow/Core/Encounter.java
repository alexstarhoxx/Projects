package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;


/**
 * The encounter the avatar will meet and play the game with it.
 */
public class Encounter implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    private static final TETile LOOK = Tileset.FLOWER;
    /** Check if this encounter has met with the avatar */
    private boolean met = false;
    private long seed;
    private Position position;
    private Detector detector;
    private TETile[][] world;


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

    public Encounter(long s, TETile[][] w) {
        seed = s;
        position = new Position(0, 0);
        detector = new Detector(w);
        world = w;
    }

    /** Randomly place this encounter to somewhere in the WORLD */
    public void randomlyPlace() {
        int x;
        int y;
        Random rand = new Random(seed);
        do {
            x = rand.nextInt(worldWidth());
            y = rand.nextInt(worldHeight());
        } while (!detector.isFloor(x, y));
        world[x][y] = LOOK;
        position.x = x;
        position.y = y;
    }

    /** Change met instance variable to true */
    public void hasMet() {
        met = true;
    }

    /** Return x value of this position */
    public int xPos() {
        return position.x;
    }

    /** Return y value of this position */
    public int yPos() {
        return position.y;
    }

    /** Return the height of the WORLD */
    private int worldHeight() {
        return world[0].length;
    }

    /** Return the width of the WORLD */
    private int worldWidth() {
        return world.length;
    }

    /** Return the seed of the encounter */
    public long seed() {
        return seed;
    }

    public boolean met() {
        return met;
    }
}
