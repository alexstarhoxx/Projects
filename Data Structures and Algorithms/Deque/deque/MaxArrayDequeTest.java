package deque;

import java.util.Comparator;

import org.junit.Test;
import static org.junit.Assert.*;


public class MaxArrayDequeTest {

    @Test
    /**
     * Create different types of MaxArrayDeques with
     * default different kinds of comparators
     * and use max() to return their max items
     */
    public void TestMaxNoParam() {
        /** Comparator<Integer> integercom = MaxArrayDeque.getIntComparator();
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(integercom);

        mad.addFirst(4);
        mad.addFirst(89);
        mad.addLast(0);
        mad.addFirst(32);
        mad.addLast(76);
        int maxNum = mad.max();
        assertEquals(89, maxNum);

       Comparator<Character> stringcom = MaxArrayDeque.getCharComparator();
       MaxArrayDeque<Character> mad2 = new MaxArrayDeque<>(stringcom);

       mad2.addLast('H');
       mad2.addFirst('y');
       mad2.addFirst('p');
       mad2.addLast('e');
       mad2.addFirst('b');
       mad2.addFirst('o');
       mad2.addLast('y');
       char maxChar = mad2.max();
       assertEquals('y', maxChar);

       Comparator<Double> doublecom = MaxArrayDeque.getDoubleComparator();
       MaxArrayDeque<Double> mad3 = new MaxArrayDeque<>(doublecom);

       mad3.addLast(5.6);
       mad3.addLast(0.99);
       mad3.addFirst(2.25);
       mad3.addLast(3.1415);
       mad3.addLast(99.8);
       mad3.addFirst(30.1);
       double maxDouble = mad3.max();
       assertEquals(99.8, maxDouble, 0.0); */
    }

    @Test
    /** Create null deques and use max() method to test if it will return null */
    public void TestMaxNoParamNull() {
        /** Comparator<Integer> integercom = MaxArrayDeque.getIntComparator();
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(integercom);

        assertNull(mad.max());


        Comparator<Character> charcom = MaxArrayDeque.getCharComparator();
        MaxArrayDeque<Character> mad2 = new MaxArrayDeque<>(charcom);

        assertNull(mad2.max()); */
    }

    @Test
    /**
     * Create MaxArrayDeques with default different kinds of comparators
     * and use max(Comparator<T> c) to return their max items
     */
    public void TestMaxWithParam() {
        /** Comparator<Integer> integercom = MaxArrayDeque.getIntComparator();
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>();

        mad.addFirst(4);
        mad.addFirst(22);
        mad.addLast(100);
        mad.addFirst(32);
        mad.addLast(736);
        mad.addFirst(-13);
        int maxNum = mad.max(integercom);
        assertEquals(736, maxNum);

        Comparator<Character> stringcom = MaxArrayDeque.getCharComparator();
        MaxArrayDeque<Character> mad2 = new MaxArrayDeque<>();

        mad2.addLast('A');
        mad2.addFirst('t');
        mad2.addFirst('t');
        mad2.addLast('e');
        mad2.addFirst('n');
        mad2.addFirst('t');
        mad2.addLast('i');
        mad2.addFirst('o');
        mad2.addFirst('n');
        char maxChar = mad2.max(stringcom);
        assertEquals('t', maxChar);

        Comparator<Double> doublecom = MaxArrayDeque.getDoubleComparator();
        MaxArrayDeque<Double> mad3 = new MaxArrayDeque<>();

        mad3.addLast(5.6);
        mad3.addLast(0.99);
        mad3.addFirst(2.25);
        mad3.addLast(3.1415);
        mad3.addLast(99.8);
        mad3.addFirst(30.1);
        double maxDouble = mad3.max(doublecom);
        assertEquals(99.8, maxDouble, 0.0); */
    }

    @Test
    /** Create null deques and use max(Comparator c) method to test if it will return null */
    public void TestMaxWithParamNull() {
        /** Comparator<Character> charcom = MaxArrayDeque.getCharComparator();
        MaxArrayDeque<Character> mad = new MaxArrayDeque<>();

        assertNull(mad.max(charcom));

        Comparator<String> stringcom = MaxArrayDeque.getNameComparator();
        MaxArrayDeque<String> mad2 = new MaxArrayDeque<>();

        assertNull(mad2.max(stringcom));*/
    }
}

