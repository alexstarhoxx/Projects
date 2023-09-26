package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import static edu.princeton.cs.introcs.StdDraw.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 65;
    private static final int HEIGHT = 35;
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File AVATAR_FILE = join(CWD, "avatar.txt");
    public static final File ENCOUNTER_FILE = join(CWD, "encounter.txt");

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        Menu menu = new Menu(WIDTH, HEIGHT);
        KeyboardInput kbi = new KeyboardInput();
        menu.display();
        while (true) {
            if (kbi.isKeyPressedN()) {
                newGameMode(menu);
                break;
            } else if (kbi.isKeyPressedL()) {
                loadGameMode();
                break;
            } else if (kbi.isKeyPressedQ()) {
                quitGamePage();
                return;
            }
        }
    }

    /** When you quit the game in the menu or during the game, it should pop up this page */
    private void quitGamePage() {
        clear(Color.BLACK);
        setPenColor(Color.ORANGE);
        String quit = "You quit the game, please close this window to exit~";
        text(WIDTH / 3.0, HEIGHT / 2.0, quit);
        show();
    }

    /** Displaying new game related menu to users if they type N in the main menu */
    private void newGameMode(Menu menu) {
        KeyboardInput kbi = new KeyboardInput();
        createNewFile(AVATAR_FILE);
        createNewFile(ENCOUNTER_FILE);
        while (true) {
            menu.displayAddingSeed();
            if (kbi.isKeyPressedS()) {
                break;
            }
        }

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        StringInput seedInput = new StringInput(menu.seed());
        seedInput.getNextKey(); // Jump the char N or n
        long seed = Long.parseLong(getSeed(seedInput));
        Avatar avatar = new Avatar(seed, finalWorldFrame);
        Encounter encounter = new Encounter(seed, finalWorldFrame);

        displayWorld(seed, finalWorldFrame);
        avatar.randomlyPlace();
        encounter.randomlyPlace();
        displayUI(avatar, encounter, finalWorldFrame);
    }

    /** Load the world state exactly as it was generated before
     *  if the users type L in the main manu.
     */
    private void loadGameMode() {
        if (!AVATAR_FILE.exists() || !ENCOUNTER_FILE.exists()) {
            return;
        }
        Avatar avatar = (Avatar) readObject(AVATAR_FILE);
        Encounter encounter = (Encounter) readObject(ENCOUNTER_FILE);


        TETile[][] finalWorldFrame = avatar.world();
        displayWorld(finalWorldFrame);
        displayUI(avatar, encounter, finalWorldFrame);
    }

    /** Displaying the WORLD */
    private void displayWorld(TETile[][] world) {
        ter.initialize(world.length, world[0].length);
        ter.renderFrame(world);
    }

    /** Displaying the world with the given long SEED */
    private void displayWorld(long seed, TETile[][] world) {
        ter.initialize(WIDTH, HEIGHT);
        generateWorld(seed, world);
        ter.renderFrame(world);
    }

    /**
     * Only generate the world but not use the renderer to display it.
     */
    private void generateWorld(long seed, TETile[][] world) {
        MapGenerator mg = new MapGenerator(seed, world);
        drawFullBlackOn(world);
        mg.generate();
    }

    /** Displaying UI (including placing the avatar) with
     *  the given String SEEDSTR and WOLRD
     */
    private void displayUI(Avatar avatar, Encounter encounter, TETile[][] world) {
        Detector detector = new Detector(world);
        KeyboardInput kbi = new KeyboardInput();
        String keyPressd = "";
        int pauseNum = 90;
        boolean meetGolden = false;

        do {
            // UI of HUD implementation
            setPenColor(Color.WHITE);
            int x = xMouse();
            int y = yMouse();
            if (detector.isFloor(x, y)) {
                textLeft(5, HEIGHT - 1, Tileset.FLOOR.description());
            } else if (detector.isWall(x, y)) {
                textLeft(5, HEIGHT - 1, Tileset.WALL.description());
            } else if (detector.isLockedDoor(x, y)) {
                textLeft(5, HEIGHT - 1, Tileset.LOCKED_DOOR.description());
            } else if (detector.isAvatar(x, y)) {
                textLeft(5, HEIGHT - 1, Tileset.AVATAR.description());
            }
            String dataTime = currentDataTime();
            textRight(WIDTH - 1, HEIGHT - 1, dataTime);
            show();

            // Avatar movement implementation
            if (kbi.isKeyPressedW()) {
                meetGolden = avatar.moveUp();
                pause(pauseNum); // Control over the avatar's speed
            } else if (kbi.isKeyPressedS()) {
                meetGolden = avatar.moveDown();
                pause(pauseNum);
            } else if (kbi.isKeyPressedA()) {
                meetGolden = avatar.moveLeft();
                pause(pauseNum);
            } else if (kbi.isKeyPressedD()) {
                meetGolden = avatar.moveRight();
                pause(pauseNum);
            }
            show();

            if (kbi.hasNextKey()) {
                keyPressd = keyPressd + kbi.getNextKey();
                if (containsColonQ(keyPressd)) {
                    writeObject(avatar, AVATAR_FILE);
                    writeObject(encounter, ENCOUNTER_FILE);
                    quitGamePage();
                    return;
                }
            }

            if (avatar.isMeet(encounter)) {
                encounter.hasMet();
                SmallNewGame sng = new SmallNewGame(avatar.seed(), WIDTH, HEIGHT);
                sng.display();
            }
            ter.renderFrame(world);
        } while (!meetGolden);

        int xGolden = detector.xLockedDoor();
        int yGolden = detector.yLockedDoor();
        world[xGolden][yGolden] = Tileset.UNLOCKED_DOOR;
        ter.renderFrame(world);

        pauseNum = 1000;
        pause(pauseNum);

        clear(Color.BLACK);
        setPenColor(Color.YELLOW);
        String win = "You found the door! You got a win!";
        text(WIDTH / 2.0, HEIGHT / 2.0, win);
        show();
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        StringInput strIn = new StringInput(input);
        char firstChar = strIn.getNextKey();
        Avatar avatar = null;
        Encounter encounter = null;

        if (firstChar == 'N') {
            long seed = Long.parseLong(getSeed(strIn));
            avatar = new Avatar(seed, finalWorldFrame);
            encounter = new Encounter(seed, finalWorldFrame);
            generateWorld(seed, finalWorldFrame);
            avatar.randomlyPlace();
            encounter.randomlyPlace();
        } else if (firstChar == 'L') {
            if (!AVATAR_FILE.exists()) {
                return finalWorldFrame;
            }
            avatar = (Avatar) readObject(AVATAR_FILE);
            finalWorldFrame = avatar.world(); // Get backed the previous drawn world.
            avatar.place();
        } else if (firstChar == 'Q') {
            return finalWorldFrame;
        }

        while (strIn.hasNextKey()) {
            char c = strIn.getNextKey();
            if (c == 'W') {
                avatar.moveUp();
            } else if (c == 'S') {
                avatar.moveDown();
            } else if (c == 'A') {
                avatar.moveLeft();
            } else if (c == 'D') {
                avatar.moveRight();
            } else if (c == ':' && strIn.getNextKey() == 'Q') {
                createNewFile(AVATAR_FILE);
                writeObject(avatar, AVATAR_FILE);
                return finalWorldFrame;
            }
        }
        return finalWorldFrame;
    }

    /** Guarantee that the value of x coordinate of the mouse should not out of the world */
    private int xMouse() {
        int x = (int) mouseX();
        if (x >= WIDTH) {
            return WIDTH - 1;
        }
        return x;
    }

    /** Guarantee that the value of y coordinate of the mouse should not out of the world */
    private int yMouse() {
        int y = (int) mouseY();
        if (y >= HEIGHT) {
            return HEIGHT - 1;
        }
        return y;
    }

    /**
     * Fill all tiles in the WORLD with black(Tile.NOTHING)
     * @param world a 2D-tiles world where all tiles will be displayed
     */
    private static void drawFullBlackOn(TETile[][] world) {
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    /** Get the seed number from input string INPUT */
    private String getSeed(StringInput strin) {
        String seed = "";
        while (strin.hasNextKey()) {
            char c = strin.getNextKey();
            if (c == 'S') {
                break;
            }
            seed += c;
        }
        return seed;
    }

    /** Verify that if the KEYSTR has String ':Q' or ':q' */
    private boolean containsColonQ(String keyStr) {
        return keyStr.contains(":Q") || keyStr.contains(":q");
    }

    /** Get the current date and time
     *  @source
     */
    private String currentDataTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /**
     * Return the concatentation of FIRST and OTHERS into a File designator,
     * analogous to the {@link java.nio.file.Paths #get(String, String[])} method.
     */
    public static File join(String first, String... others) {
        return Paths.get(first, others).toFile();
    }

    public static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    /** Create a new file with the given file F */
    public static void createNewFile(File f) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write an object O to file F
     * @source
     */
    static void writeObject(Object o, File f) {
        try {
            FileOutputStream fileOut = new FileOutputStream(f);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(o);
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read an object O from file F
     * @source
     */
    static Object readObject(File f) {
        try {
            FileInputStream fileIn = new FileInputStream(f);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object o = objectIn.readObject();
            objectIn.close();
            return o;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
