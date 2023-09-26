package byow.Core;

public interface InputSource {
    /** Check if the input source has next element to go through */
    boolean hasNextKey();
    /** Get the next element of the input source. All element must be capital */
    char getNextKey();
}
