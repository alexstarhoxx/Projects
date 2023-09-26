package byow.Core;

import byow.TileEngine.TETile;
import java.util.Random;

import static byow.Core.RandomUtils.*;

/**
 * Generator used to generate the random map by using Hallways, Rooms and
 * TurningHallWays modules.
 * @author Alex Ho
 */
public class MapGenerator {
    private static Random random;
    private static final int HW_RANGE = 4;
    private static final int THW_RANGE = 4;
    private static final int ROOM_RANGE = 8;
    private TETile[][] world;
    private boolean closed;

    public MapGenerator(long s, TETile[][] w) {
        random = new Random(s);
        world = w;
        closed = false;
    }

    /** Initially generate a room randomly with a randomly positioned 
     *  golden-colored wall on its sides and then based on this room
     *  generate other components linking this room.
     */
    public void generate() {
        // Prevent rooms from drawing out of the world
        int randomRx = worldWidth() / 2 + randomInt(-5, 5);
        int randomRy = worldHeight() / 2 + randomInt(-5, 5);
        Rooms initRoom = new Rooms(randomInt(1, 3), randomInt(1, 3), randomRx, randomRy, world);
        initRoom.draw();
        initRoom.addGoldenWall();
        closed = false;
        roomConnector(initRoom);
    }

    /** Only for rooms who will connect neither hallways nor turning hallways randomly */
    public void roomConnector(Rooms room) {
        if (closed) {
            return;
        }

        int num = random.nextInt(4);
        if (num == 0 || num == 1 || num == 2) {

            for (int i = 0; i < 3; i++) {
                Hallways hw = new Hallways(randomHwRange(), world);
                closed = room.connectWith(hw);
                hwConnector(hw);
            }

        } else {

            for (int i = 0; i < 3; i++) {
                TurningHallWays thw = new TurningHallWays(randomThwRange(),
                        randomThwRange(), world);
                closed = room.connectWith(thw);
                thwConnector(thw);
            }
        }
    }

    /** Only for hallways who will connect neither rooms, hallways, turning hallways randomly */
    public void hwConnector(Hallways hw) {
        if (closed) {
            return;
        }

        Rooms room = new Rooms(randomRoomRange(), randomRoomRange(), world);
        closed = hw.connectWith(room);
        roomConnector(room);
    }

    /** Only for turning hallways who will connect random rooms */
    public void thwConnector(TurningHallWays thw) {
        if (closed) {
            return;
        }

        Rooms room = new Rooms(randomRoomRange(), randomRoomRange(), world);
        closed = thw.connectWith(room);
        roomConnector(room);
    }

    /** Return random integer ranged from 1(inclusive) to HWRANGE(exclusive) */
    private int randomHwRange() {
        return uniform(random, 1, HW_RANGE + 1);
    }

    /** Return random integer ranged from 1(inclusive) to THWRANGE(exclusive) */
    private int randomThwRange() {
        return uniform(random, 1, THW_RANGE + 1);
    }

    /** Return random integer ranged from 1(inclusive) to ROOMRANGE(exclusive) */
    private int randomRoomRange() {
        return uniform(random, 1, ROOM_RANGE + 1);
    }

    /** Return random integer ranged from 1(inclusive) to NUM(exclusive) */
    private int randomInt(int num) {
        return uniform(random, 1, num);
    }

    /** Return random integer ranged from NUM1(inclusive) to NUM2(exclusive) */
    private int randomInt(int num1, int num2) {
        return uniform(random, num1, num2);
    }

    /** Return a random shape for turning hallways. It includes 7, Reversed 7, L and Reversed L */
    private String randomShape() {
        int ranNum = random.nextInt(4);
        String shape;
        switch (ranNum) {
            case 0:
                shape = "7";
                break;
            case 1:
                shape = "R7";
                break;
            case 2:
                shape = "L";
                break;
            default:
                shape = "RL";
                break;
        }
        return shape;
    }

    public static Random random() {
        return random;
    }

    public int worldHeight() {
        return world[0].length;
    }

    public int worldWidth() {
        return world.length;
    }
}
