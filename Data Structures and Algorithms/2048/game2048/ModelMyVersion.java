package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class ModelMyVersion extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public ModelMyVersion(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public ModelMyVersion(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        board.setViewingPerspective(side);

        /** Each column of a board will be tilted */
        for (int col = 0; col < board.size(); col += 1) {
            int[] scoreAndChange = tiltColumn(board, col);
            score = score + scoreAndChange[0];
            if (scoreAndChange[1] == 1) {
                changed = true;
            }
        }
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Tilt all tiles toward UP on one single column
     * and return gaining score
     */
    public static int[] tiltColumn(Board b, int col) {
        /** Set a baffle to prevent tile from
         * violating Rule 2 and move beyond limit.
         *
         * The first baffle of a column is the top tile.
         */
        int baffleIndex = b.size() - 1;

        /** The first value will be used to record accumulative score
         *  The second value will be used to record whether a board changed
         *  (0 indicates no change, 1 indicates change)
         */
        int[] scoreAndChange = new int[]{0, 0};

        /**
         * Tilt very tiles on a board
         * Begin on the second top row
         */
        for (int row = baffleIndex - 1; row >= 0; row -= 1) {
            Tile t = b.tile(col, row);
            Tile baffle = b.tile(col, baffleIndex);
            /** When tile is null, skip it */
            if (t == null) {
                continue;
            }

            /**
             * Each non-null tile have 3 possibilities.
             * 1. When baffle's value is null, tilt to baffle's position.
             * 2. When tile's value is equal to baffle's value,
             * tilt to baffle's position and get gaining score.
             * 3. When tile's value is not equal to baffle's value, change baffle
             * if baffle currently is null, then tilt tile to baffle's position.
             */
            if (baffle == null) {
                b.move(col, baffleIndex, t);
                scoreAndChange[1] = 1;
            } else if (baffle.value() == t.value()) {
                b.move(col, baffleIndex, t);
                baffleIndex = baffleIndex - 1;
                scoreAndChange[0] += t.value() * 2;
                scoreAndChange[1] = 1;
            } else {
                baffleIndex = baffleIndex - 1;
                baffle = b.tile(col, baffleIndex);
                if (baffle == null) {
                    b.move(col, baffleIndex, t);
                    scoreAndChange[1] = 1;
                }
            }
        }
        
        return scoreAndChange;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        /** Iterate through all tiles in Board b */
        for (int row = 0; row < b.size(); row += 1) {
            for (int col = 0; col < b.size(); col += 1) {
                if (b.tile(col, row) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        for (int row = 0; row < b.size(); row += 1) {
            for (int col = 0; col < b.size(); col += 1) {
                Tile currentTile = b.tile(col, row);
                /** When currentTile is a null, currentTile.value() will not
                 * be able to executed and will cause error.
                 */
                if (currentTile != null && currentTile.value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b)) {
            return true;
        }

        for (int row = 0; row < b.size(); row += 1) {
            for (int col = 0; col < b.size(); col += 1) {
                Tile currentTile = b.tile(col, row);
                if (sameAdjacentValue(currentTile, b)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** To help us judge whether there is a value of t's
     * adjacent(up, down, left, right) tiles that is as same as t one.
     * @param t
     * @return
     */
    public static boolean sameAdjacentValue(Tile t, Board b) {
        boolean sameValueInRow = sameValueRow(t, b);
        boolean sameValueInCol = sameValueCol(t, b);
        return sameValueInRow || sameValueInCol;
    }

    public static boolean sameValueRow(Tile t, Board b) {
        int startCol = startColIndex(t);
        int endCol = endColIndex(t, b);
        /** Compare values of t's left tile and right tile */
        for (int col = startCol; col < endCol; col += 1) {
            /** t should not be compared to itself */
            if (t.col() == col) {
                continue;
            }
            if (b.tile(col, t.row()).value() == t.value()) {
                return true;
            }
        }
        return false;
    }

    public static boolean sameValueCol(Tile t, Board b) {
        int startRow = startRowIndex(t);
        int endRow = endRowIndex(t, b);
        /** Compare values of t's up tile and down tile */
        for (int row = startRow; row < endRow; row += 1) {
            /** t should not be compared to itself */
            if (t.row() == row) {
                continue;
            }
            if (b.tile(t.col(), row).value() == t.value()) {
                return true;
            }
        }
        return false;
    }

    /** Helper methods of sameValueRow(sameValueCol) method */
    public static int startColIndex(Tile t) {
        if (t.col() == 0) {
            return 0;
        } else {
            return t.col() - 1;
        }
    }

    public static int endColIndex(Tile t, Board b) {
        if (t.col() == b.size() - 1) {
            return t.col();
        } else {
            return t.col() + 1;
        }
    }

    public static int startRowIndex(Tile t) {
        if (t.row() == 0) {
            return 0;
        } else {
            return t.row() - 1;
        }
    }

    public static int endRowIndex(Tile t, Board b) {
        if (t.row() == b.size() - 1) {
            return t.row();
        } else {
            return t.row() + 1;
        }
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
