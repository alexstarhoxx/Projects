package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serial;
import java.io.Serializable;

/**
 * This detector is used to check if any tile in the world is used.
 * @author Alex Ho
 */
public class Detector implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    private TETile[][] world;

    public Detector(TETile[][] w) {
        world = w;
    }

    /** Check if a tile is drawn */
    public boolean isDrawn(int x, int y) {
        return !world[x][y].description().equals("nothing");
    }

    /** Check if a tile is a floor */
    public boolean isFloor(int x, int y) {
        return world[x][y].description().equals("floor");
    }

    /** Check if a tile is a wall */
    public boolean isWall(int x, int y) {
        return world[x][y].description().equals("wall");
    }

    /** Check if a tile is a locked door a.k.a. a golden-colored wall */
    public boolean isLockedDoor(int x, int y) {
        return world[x][y].description().equals("locked door");
    }

    /** Get the value of x of locked door */
    public int xLockedDoor() {
        int x = 1;
        for (int i = 0; i < worldWidth(); i++) {
            for (int j = 0; j < worldHeight(); j++) {
                if (isLockedDoor(i, j)) {
                    x = i;
                }
            }
        }
        return x;
    }

    /** Get the value of y of locked door */
    public int yLockedDoor() {
        int y = 1;
        for (int i = 0; i < worldWidth(); i++) {
            for (int j = 0; j < worldHeight(); j++) {
                if (isLockedDoor(i, j)) {
                    y = j;
                }
            }
        }
        return y;
    }

    /** Check if a tile is an avatar */
    public boolean isAvatar(int x, int y) {
        return world[x][y].description().equals("you");
    }

    /** Return the height of the WORLD */
    private int worldHeight() {
        return world[0].length;
    }

    /** Return the width of the WORLD */
    private int worldWidth() {
        return world.length;
    }
}
