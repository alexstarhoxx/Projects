package byow.Core;

public class StringInput implements InputSource {
    private String input;
    private int index;

    public StringInput(String i) {
        input = i;
        index = 0;
    }

    @Override
    public boolean hasNextKey() {
        return index < input.length();
    }

    @Override
    public char getNextKey() {
        char c = Character.toUpperCase(input.charAt(index));
        index += 1;
        return c;
    }
}
