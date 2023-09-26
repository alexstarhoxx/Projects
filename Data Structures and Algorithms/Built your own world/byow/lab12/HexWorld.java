package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab12.Hexagon;

import java.awt.*;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 * @author Alex Ho
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    private static final int SEED = 8;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Draw a tesselation of hexagons with SIZE in the WORLD at position X and Y.
     * @param x x coordinate
     * @param y y coordinate
     * @param size size of all tessellated hexagons
     * @param world a 2D-tiles world where tesselation of hexagons will be displayed
     */
    public static void addTesselation(int x, int y, int size, TETile[][] world) {
        addHexagonLine(x - 2 * (2 * size - 1), y - 2 * size, size, 3, world);
        addHexagonLine(x - 2 * size + 1, y - size, size, 4, world);
        addHexagonLine(x, y, size, 5, world);
        addHexagonLine(x + 2 * size - 1, y - size, size, 4, world);
        addHexagonLine(x + 2 * (2 * size - 1), y - 2 * size, size, 3, world);
    }

    /**
     * Draw a hexagon with SIZE in the WORLD at position X and Y.
     * @param x x coordinate
     * @param y y coordinate
     * @param tile form of the tile
     * @param size size of a hexagon
     * @param world a 2D-tiles world where the hexagon will be displayed
     */
    public static void addHexagon(int x, int y, TETile tile, int size, TETile[][] world) {
        TETile t = tile;
        Hexagon hex = new Hexagon(size, t, world);
        hex.drawAt(x, y);
    }

    /**
     * Draw a vertical line consisting of NUM hexagons with the same SIZE in the WORLD
     * at position X and Y.
     * @param x x coordinate
     * @param y y coordinate
     * @param size size of each hexagon
     * @param num number of hexagons combined to consist of a vertical hexagon line
     * @param world a 2D-tiles world where the hexagon line will be displayed
     */
    public static void addHexagonLine(int x, int y, int size, int num, TETile[][] world) {
        for (int i = 0; i < num; i += 1) {
            addHexagon(x, y - i * size * 2, randomTile(), size, world);
        }
    }

    /**
     * Pick a RANDOM TILE with 25% of being a mountain, 25% of being a tree
     * 25% of being an avatar and 25% of being a flower.
     * @return a form of a tile
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(4);
        switch(tileNum) {
            case 0:
                return Tileset.MOUNTAIN;
            case 1:
                return Tileset.TREE;
            case 2:
                return Tileset.FLOWER;
            case 3:
                return Tileset.AVATAR;
            default:
                return Tileset.MOUNTAIN;
        }
    }

    /**
     * Fill all tiles in the WORLD with black(Tile.NOTHING)
     * @param world a 2D-tiles world where all tiles will be displayed
     */
    public static void drawFullBlackOn(TETile[][] world) {
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static void main(String[] args) {
        TERenderer engine = new TERenderer();
        engine.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        drawFullBlackOn(world);

        /** Draw single hexagons with different sizes
        TETile t = new TETile('a', Color.WHITE, Color.BLACK, "a letter");
        addHexagon(10, 10, t, 4, world);
        addHexagon(25, 10, t, 5, world);
        addHexagon(40, 10, t, 3, world);
        addHexagon(50, 10, t, 2, world); */

        addTesselation(29, 29, 3, world);

        engine.renderFrame(world);
    }
}
