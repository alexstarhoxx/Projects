package deque;

import java.util.Comparator;

/**
 * Create a class called MaxArrayDeque which has all methods of ArrayDeque
 * but has two additional methods and a new constructor.
 *
 * @author Alex Ho
 */
public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> com;

    /**
     * Create a MaxArrayDeque with no Comparator
     *
     * Uncomment this constructor only when you want to test implementations of this class.
     */
    /** public MaxArrayDeque() {
        com = null;
    } */

    /**
     * Create a MaxArrayDeque object with given Comparator c
     */
    public MaxArrayDeque(Comparator<T> c) {
        super();
        com = c;
    }

    // When you are to test implementations of this class, uncomment these comparators.
    /* public static Comparator<Integer> getIntComparator() {
        return new IntegerComparator();
    }

    public static Comparator<String> getNameComparator() {
        return new StringComparator();
    }

    public static Comparator<Double> getDoubleComparator() {
        return new DoubleComparator();
    }

    public static Comparator<Character> getCharComparator() {
        return new CharComparator();
    } */

    /**
     * Return the maximum element in the deque as governed
     * by previous given Comparator c. If deque is empty, return null.
     */
    public T max() {
        T maxItem = this.get(0);
        for (int i = 0; i < this.size(); i += 1) {
            T currentItem = get(i);
            if (com.compare(currentItem, maxItem) > 0) {
                maxItem = currentItem;
            }
        }
        return maxItem;
    }

    /**
     * Return the maximum element in the deque as governed
     * by parameter Comparator c. If deque is empty, return null.
     */
    public T max(Comparator<T> c) {
        T maxItem = this.get(0);
        for (int i = 0; i < this.size(); i += 1) {
            T currentItem = get(i);
            if (c.compare(currentItem, maxItem) > 0) {
                maxItem = currentItem;
            }
        }
        return maxItem;
    }

    private static class IntegerComparator implements Comparator<Integer> {
        public int compare(Integer i1, Integer i2) {
            return i1.compareTo(i2);
        }
    }

    private static class StringComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            return s1.compareTo(s2);
        }
    }

    private static class DoubleComparator implements Comparator<Double> {
        public int compare(Double d1, Double d2) {
            return d1.compareTo(d2);
        }
    }

    private static class CharComparator implements Comparator<Character> {
        public int compare(Character c1, Character c2) {
            return c1.compareTo(c2);
        }
    }
}
