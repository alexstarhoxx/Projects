package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class TestMap {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

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

        MapGenerator mg = new MapGenerator(2783, world);
        mg.generate();
        System.out.print(TETile.toString(world));

//        Rooms room = new Rooms(5, 5, 45, 5, world);
//        Hallways block = new Hallways(2, 'y', 4, 35, world);
//        block.draw();
//        room.draw();
//        Hallways hw = new Hallways(4, world);
//        room.conTopEdgeWith(hw);
//        Detector detector = new Detector(world);
//        TurningHallWays thw = new TurningHallWays(7, 10, "7", world);
//        room.conRightEdgeWith(thw);

        // For testing room.isOutofWorld method
//        Rooms room = new Rooms(5, 5, 57, 36,  world);
        // room.draw();
//        System.out.print(room.isOutOfWorld());

//        TurningHallWays thw = new TurningHallWays(3, 3, "R7", 28, 40, world);
//        System.out.println(thw.isOutOfWorld());

        engine.renderFrame(world);
    }
}
