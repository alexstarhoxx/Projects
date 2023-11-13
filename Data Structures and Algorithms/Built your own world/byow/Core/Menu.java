package byow.Core;

import static edu.princeton.cs.introcs.StdDraw.*;

import java.awt.Color;
import java.awt.Font;

/**
 * This class is used to generate the fronted menu. It does not implement
 * interactivity for users.
 * @author Alex Ho
 */
public class Menu {
    private int width;
    private int height;
    private static final int TILE_SIZE = 16;
    private String seed;

    public Menu(int w, int h) {
        width = w;
        height = h;
        seed = "";
        setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        setXscale(0, width);
        setYscale(0, height);
        clear(Color.BLACK);
        enableDoubleBuffering();
    }

    /** Set the menu and display it */
    public void display() {
        setPenColor(Color.WHITE);
        double xTitle = width / 2.0;
        double yTitle = height / 3.0 * 2;
        double offset = 8;
        Font title = new Font("Monaco", Font.BOLD, 35);
        Font content = new Font("Monaco", Font.BOLD, 16);
        String gameName = "CS61B: THE GAME";
        String newGame = "New Game (N)";
        String loadGame = "Load Game(L)";
        String quit = "Quit (Q)";
        placeText(gameName, title, xTitle, yTitle);
        setPenColor(Color.YELLOW);
        placeText(newGame, content, xTitle, yTitle - offset);
        placeText(loadGame, content, xTitle, yTitle - offset - 2);
        placeText(quit, content, xTitle, yTitle - offset - 4);
        show();
    }

    /** Place the given TEXT with FONT at position(X, Y) */
    private void placeText(String text, Font font, double x, double y) {
        setFont(font);
        text(x, y, text, 0);
    }

    /** Create a page for user to adding the seed. The page should
     *  show the seed value that the user has entered so far.
     */
    public void displayAddingSeed() {
        clear(Color.BLACK);
        setPenColor(Color.WHITE);
        int x = width / 2;
        int y = height / 3 * 2;
        Font title = new Font("Monaco", Font.BOLD, 20);
        String peys = "Please enter random seed!";
        placeText(peys, title, x, y);

        KeyboardInput kbi = new KeyboardInput();
        if (kbi.hasNextKey()) {
            this.seed = this.seed + kbi.getNextKey();
        }
        Font seedFont = new Font("Monaco", Font.BOLD, 16);
        setPenColor(Color.YELLOW);
        placeText(seed, seedFont, x, y - 5);
        show();
    }

    /** Return seed entered into the menu */
    public String seed() {
        return seed;
    }
}
