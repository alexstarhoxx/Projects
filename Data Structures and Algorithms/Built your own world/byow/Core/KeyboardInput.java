package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.event.KeyEvent;

public class KeyboardInput implements InputSource {
    @Override
    public boolean hasNextKey() {
        return StdDraw.hasNextKeyTyped();
    }

    @Override
    public char getNextKey() {
        return Character.toUpperCase(StdDraw.nextKeyTyped());
    }

    /** Check if the keyboard input has pressed keyboard A */
    public boolean isKeyPressedA() {
        return StdDraw.isKeyPressed(KeyEvent.VK_A);
    }

    /** Check if the keyboard input has pressed keyboard D */
    public boolean isKeyPressedD() {
        return StdDraw.isKeyPressed(KeyEvent.VK_D);
    }

    /** Check if the keyboard input has pressed keyboard W */
    public boolean isKeyPressedW() {
        return StdDraw.isKeyPressed(KeyEvent.VK_W);
    }

    /** Check if the keyboard input has pressed keyboard S */
    public boolean isKeyPressedS() {
        return StdDraw.isKeyPressed(KeyEvent.VK_S);
    }

    /** Check if the keyboard input has pressed keyboard N */
    public boolean isKeyPressedN() {
        return StdDraw.isKeyPressed(KeyEvent.VK_N);
    }

    /** Check if the keyboard input has pressed keyboard Q */
    public boolean isKeyPressedQ() {
        return StdDraw.isKeyPressed(KeyEvent.VK_Q);
    }

    /** Check if the keyboard input has pressed keyboard L */
    public boolean isKeyPressedL() {
        return StdDraw.isKeyPressed(KeyEvent.VK_L);
    }
}
