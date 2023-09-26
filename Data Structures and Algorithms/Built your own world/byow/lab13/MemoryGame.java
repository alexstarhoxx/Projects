package byow.lab13;

import static byow.Core.RandomUtils.*;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        rand = new Random(seed);
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    /** Generate random string of letters of length n */
    public String generateRandomString(int n) {
        String randStr = "";
        for (int i = 0; i < n; i++) {
            int randNum = uniform(rand, CHARACTERS.length);
            randStr = randStr + CHARACTERS[randNum];
        }
        return randStr;
    }

    /** Take the string S and display it in the center of the screen */
    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2.0, height / 2.0, s, 0);
        StdDraw.show();
        if (!gameOver) {
            int ranNum = uniform(rand, ENCOURAGEMENT.length);
            String round = "Round: " + this.round;
            String encourage = ENCOURAGEMENT[ranNum];
            StdDraw.text(3, height - 1, round, 0);
            StdDraw.text(width - 6, height - 1, encourage, 0);
            if (playerTurn) {
                StdDraw.text(width / 2.0, height - 1, "Type!", 0);
            } else {
                StdDraw.text(width / 2.0, height - 1, "Watch!", 0);
            }
            StdDraw.show();
        }

    }

    /** Display each character in LETTERS, making sure to blank the screen between each letters */
    public void flashSequence(String letters) {
        playerTurn = false;
        String letter;
        for (int i = 0; i < letters.length(); i++) {
            letter = Character.toString(letters.charAt(i));
            drawFrame(letter);
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.show();
            StdDraw.pause(500);
        }
    }

    /** Read n letters of player input */
    public String solicitNCharsInput(int n) {
        playerTurn = true;
        String str = "";
        for (int i = 0; i < n; i++) {
            if (StdDraw.hasNextKeyTyped()) {
                str = str + StdDraw.nextKeyTyped();
                drawFrame(str);
                StdDraw.pause(1000 * n);
            }
        }
        return str;
    }

    public void startGame() {
        round = 1;
        gameOver = false;

        while (true) {
            drawFrame("Round:" + round);
            String randStr = generateRandomString(round);
            flashSequence(randStr);
            String typedStr = solicitNCharsInput(round);
            StdDraw.pause(100);

            if (typedStr.equals(randStr)) {
                round += 1;
            } else {
                gameOver = true;
                break;
            }
        }
        drawFrame("Game Over! You made it to round " + round);
    }

}
