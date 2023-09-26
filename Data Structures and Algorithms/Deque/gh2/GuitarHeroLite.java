package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHeroLite {
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    /** Create an array that will contains 37-notes' GuitarStrings */
    public static GuitarString[] createGuitarString(int length) {
        GuitarString[] gsArray = new GuitarString[length];
        for (int i = 0; i < length; i += 1) {
            double n = (i - 24) / 12.0;
            double f = 440 * Math.pow(2, n);
            gsArray[i] = new GuitarString(f);
        }
        return gsArray;
    }

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarString stringA = new GuitarString(CONCERT_A);
        GuitarString stringC = new GuitarString(CONCERT_C);

        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] gsArray = createGuitarString(keyboard.length());
        GuitarString stringSomething = gsArray[0];

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'a') {
                    stringA.pluck();
                } else if (key == 'c') {
                    stringC.pluck();
                } else {
                    int index = keyboard.indexOf(key);
                    if (index == -1) {
                        continue;
                    }
                    stringSomething = gsArray[index];
                    stringSomething.pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = stringA.sample() + stringC.sample() + stringSomething.sample();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            stringA.tic();
            stringC.tic();
            stringSomething.tic();
        }
    }
}

