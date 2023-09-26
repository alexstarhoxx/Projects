package byow.Core;

import java.awt.*;
import java.awt.event.KeyEvent;

import static edu.princeton.cs.introcs.StdDraw.*;

public class TestMenu {
    public static void main(String[] args) {
        Menu menu = new Menu(60, 30);
        menu.display();
        while (true) {
            if (isKeyPressed(KeyEvent.VK_N)) {
                break;
            }
        }

        while (true) {
            menu.displayAddingSeed();
            if (isKeyPressed(KeyEvent.VK_S)) {
                break;
            }
        }

        Engine engine = new Engine();
        engine.interactWithInputString(menu.seed());

    }
}
