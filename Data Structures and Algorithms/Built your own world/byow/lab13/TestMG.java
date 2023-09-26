package byow.lab13;

public class TestMG {

    /** Test generateRandomString method */
    public static void testRandStrAndDrawFrame() {
        MemoryGame mg = new MemoryGame(30, 30, 12);
        String randStr = mg.generateRandomString(8);
    }

    /** Test flashSquare method */
    public static void testFlashSquare() {
        MemoryGame mg = new MemoryGame(30, 30, 12);
        String randStr = mg.generateRandomString(6);
        mg.solicitNCharsInput(5);
    }

    public static void main(String[] args) {
        testFlashSquare();
    }
}
