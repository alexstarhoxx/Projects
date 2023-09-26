package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * A class to test all components are written properly,
 * including rooms, floors, 'L' turns.
 * @author Alex Ho
 */
public class TestComponent {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

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

//        Rooms room = new Rooms(5, 5, 20, 20, world);
//        room.draw();
//
//        Hallways hallway = new Hallways(5, 'y', 10, 10, world);
//        hallway.draw();

//        TurningHallWays thw = new TurningHallWays(20, 10, 10, 10, world);
//        thw.drawL();

//        TurningHallWays thw7 = new TurningHallWays(5, 10, 10, 10, world);
//        thw7.draw7();

        //TurningHallWays thwReverseL = new TurningHallWays(15, 10, 20, 0, world);
        //thwReverseL.drawReverseL();

//        TurningHallWays thwReverse7 = new TurningHallWays(40, 15, 5, 10, world);
//        thwReverse7.drawReverse7();
        engine.renderFrame(world);

    }
}
