package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class TestConnection {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;

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

        Rooms room = new Rooms(5, 7, world);
        Hallways hw1 = new Hallways(5, 'y', 10, 10, world);
//        Hallways hw2 = new Hallways(2, 'x', world);

        // TurningHallWays thw = new TurningHallWays(5, 6, 10, 10, world);
//        TurningHallWays thw2 = new TurningHallWays(2, 4, world);
//        TurningHallWays thw3 = new TurningHallWays(5, 1, world);
//        TurningHallWays thw4 = new TurningHallWays(2, 3, world);
//        room.draw();
//        room.conTopEdgeWith(hw1);
//        room.conLeftEdgeWith(thw2);
//        room.conBottomEdgeWith(hw2);
//        hw1.connectXWith(hw2);
//        hw1.draw();
//        hw1.connectLeftWith(room);
//        hw1.connectBottomWith(room);
        //thw.draw7();
        //thw.conBottom7With(room);

        engine.renderFrame(world);
    }
}
