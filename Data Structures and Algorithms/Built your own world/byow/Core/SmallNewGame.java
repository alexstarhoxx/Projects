package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static edu.princeton.cs.introcs.StdDraw.pause;

/**
 * When an avatar meets an encounter, it will trigger this object
 * to make a small new game for users to complete.
 * @author Alex Ho
 */
public class SmallNewGame {
    private long seed;
    private int width;
    private int height;
    private TETile[][] world;

    public SmallNewGame(long s, int w, int h) {
        seed = s;
        width = w;
        height = h;
        world = new TETile[width][height];
    }

    public void display() {
        displayPromt();

        int midx = width / 2;
        int midy = height / 2;
        drawFullBlackOnWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        Rooms room = new Rooms(midx / 3, midy / 2, midx, midy, world);
        Avatar avatar = new Avatar(seed, world);
        int numOfEncounter = 10;
        int numFlowers = numOfEncounter;
        Encounter[] encounters = setUpArrayOfEncounters(numOfEncounter);

        room.draw();
        avatar.randomlyPlace();


        for (int i = 0; i < numOfEncounter; i++) {
            encounters[i].randomlyPlace();
        }
        ter.renderFrame(world);

        KeyboardInput kbi = new KeyboardInput();
        do {
            // Avatar movement implementation
            if (kbi.isKeyPressedW()) {
                avatar.moveUp();
                pause(100); // Control over the avatar's speed
            } else if (kbi.isKeyPressedS()) {
                avatar.moveDown();
                pause(100);
            } else if (kbi.isKeyPressedA()) {
                avatar.moveLeft();
                pause(100);
            } else if (kbi.isKeyPressedD()) {
                avatar.moveRight();
                pause(100);
            }

            for (int i = 0; i < numOfEncounter; i++) {
                if (avatar.isMeet(encounters[i])) {
                    encounters[i].hasMet();
                    numFlowers -= 1;
                }
            }
            ter.renderFrame(world);
        } while (numFlowers != 0);
    }

    private void displayPromt() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.PINK);

        int midx = width / 2;
        int midy = height / 2;
        String text = "Oh! It seems like you are stepping on someone's flower jungle!"
                + "They are very angry!";
        String hint = "Please pick up 10 flowers for them to show your apology..."
                + "Or you may not leave this place!";
        StdDraw.text(midx, midy, text);
        StdDraw.text(midx, midy - 2, hint);
        StdDraw.show();
        StdDraw.pause(1000);
    }

    /** Set up an N-length array filled with encounters */
    private Encounter[] setUpArrayOfEncounters(int n) {
        Encounter[] arr = new Encounter[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Encounter(seed, world);
        }
        return arr;
    }

    /**
     * Fill all tiles in the world with black(Tile.NOTHING)
     */
    private void drawFullBlackOnWorld() {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
}
