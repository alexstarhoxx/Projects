package byow.Core;

import static byow.Core.RandomUtils.*;
import java.util.Random;

public class RandomTest {
    public static void main(String[] args) {
        Random r = new Random();
        for (int i = 0; i < 20; i++) {
            double num = gaussian(r);
            System.out.println(num);
        }
    }
}
