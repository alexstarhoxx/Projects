package byow.lab12;

import byow.TileEngine.TETile;

import java.awt.*;

/**
 * A hexagon object consists of the same tiles forms with a given size
 * @author Alex Ho
 */
public class Hexagon {
    private int size;
    private TETile tile;
    private TETile[][] world;

    /**
     * Full constructor for hexagon objects
     * @param s The size of the hexagon object
     * @param t The tile form of the object
     */
    public Hexagon(int s, TETile t, TETile[][] w) {
        size = s;
        tile = t;
        world = w;
    }

    /**
     * Draw a hexagon object at location x, y. x and y must be the position of the spot
     * in the top left corner.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void drawAt(int x, int y) {
         int floor = 0;
         for (int length = size; length <= 3 * size - 2; length += 2) {
             drawLine(x - floor, y - floor, length);
             floor += 1;
         }

         for (int length = 3 * size - 2; length >= size; length -= 2) {
             drawLine(x - 2 * size + 1 + floor, y - floor, length);
             floor += 1;
         }
    }

    /**
     * Draw a line at position x and y with LENGTH
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param length length of this line
     */
    private void drawLine(int x, int y, int length) {
        for (int i = x; i < x + length; i += 1) {
            world[i][y] = tile;
        }
    }
}
