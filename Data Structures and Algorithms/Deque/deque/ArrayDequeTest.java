package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    /** For simple 8-length deque, add items forward and check get method */
    public void simpleGetAddItemsFor() {
        ArrayDeque<Integer> arrDeq = new ArrayDeque<>();

        for (int i = 0; i < 5; i += 1) {
            arrDeq.addFirst(i);
        }
        int actualItem = arrDeq.get(0);
        assertEquals(4, actualItem);
    }

    @Test
    /** For simple 8-length deque, add items backward and check get method */
    public void simpleGetAddItemsBack() {
        ArrayDeque<Integer> arrDeq = new ArrayDeque<>();

        for (int i = 0; i < 5; i += 1) {
            arrDeq.addFirst(i);
        }
        int actualItem = arrDeq.get(1);
        assertEquals(3, actualItem);
    }

    @Test
    /** For simple 8-length deque, remove items from front and check if it is circular */
    public void simpleRemoveItemsFront() {
        ArrayDeque<Integer> arrDeq = new ArrayDeque<>();

        for (int i = 0; i < 6; i += 1) {
            arrDeq.addFirst(i);
        }
        int removedItem = arrDeq.removeFirst();
        assertEquals(5, removedItem);

        removedItem = arrDeq.removeFirst();
        assertEquals(4, removedItem);

        removedItem = arrDeq.removeFirst();
        assertEquals(3, removedItem);

        removedItem = arrDeq.removeFirst();
        assertEquals(2, removedItem);
    }

    @Test
    /** For simple 8-length deque, remove items from back of the deque and check if it is circular */
    public void simpleRemoveItemsBack() {
        ArrayDeque<Integer> arrDeq = new ArrayDeque<>();

        for (int i = 0; i < 6; i += 1) {
            arrDeq.addLast(i);
        }
        int removedItem = arrDeq.removeLast();
        assertEquals(5, removedItem);

        removedItem = arrDeq.removeLast();
        assertEquals(4, removedItem);

        removedItem = arrDeq.removeLast();
        assertEquals(3, removedItem);

        removedItem = arrDeq.removeLast();
        assertEquals(2, removedItem);
    }

    @Test
    /** Create an empty deque, test isEmpty method; Add several items to test size method */
    public void addIsEmptySizeTest() {
        ArrayDeque<Integer> arrDeq = new ArrayDeque<>();

        assertTrue("arrDeq should be empty, but get false", arrDeq.isEmpty());

        arrDeq.addFirst(15);
        assertEquals(1, arrDeq.size());

        arrDeq.addLast(16);
        assertEquals(2, arrDeq.size());

        arrDeq.addLast(17);
        assertEquals(3, arrDeq.size());
    }

    @Test
    /** Apply several remove method on a deque, it should return null and size of deque should not change */
    public void removeEmptyDeque() {
        ArrayDeque<Integer> arrDeq = new ArrayDeque<>();

        arrDeq.addFirst(2);
        int removedItem = arrDeq.removeLast();
        assertEquals(2, removedItem);

        assertNull(arrDeq.removeFirst());
        arrDeq.removeLast();
        arrDeq.removeFirst();
        arrDeq.removeLast();
        arrDeq.removeLast();
        arrDeq.removeFirst();
        arrDeq.removeLast();

        assertEquals(0, arrDeq.size());
    }

    @Test
    /** Add some items and print them out */
    public void printDequeNoCircular() {
        ArrayDeque<Integer> arrDeq1 = new ArrayDeque<>();
        ArrayDeque<Integer> arrDeq2 = new ArrayDeque<>();

        /** Non-circular */
        arrDeq1.addFirst(2);
        arrDeq1.addFirst(1);
        arrDeq1.addLast(3);
        arrDeq1.printDeque();

        arrDeq2.addLast(10);
        arrDeq2.addFirst(9);
        arrDeq2.addLast(11);
        arrDeq2.printDeque();

        /* Circular */
        arrDeq1.addFirst(0);
        arrDeq1.addFirst(-1);
        arrDeq1.addFirst(-2);
        arrDeq1.printDeque();

        arrDeq2.addLast(12);
        arrDeq2.addLast(13);
        arrDeq2.addLast(14);
        arrDeq2.addLast(15);
        arrDeq2.printDeque();
    }

    @Test
    /* Check if you can create ArrayDeque with different parameterized types*/
    public void multipleParamTest() {
        ArrayDeque<String>  arrDeq1 = new ArrayDeque<>();
        ArrayDeque<Double>  arrDeq2 = new ArrayDeque<>();
        ArrayDeque<Boolean> arrDeq3 = new ArrayDeque<>();

        arrDeq1.addFirst("string");
        arrDeq2.addFirst(3.14159);
        arrDeq3.addFirst(true);

        String s = arrDeq1.removeFirst();
        double d = arrDeq2.removeFirst();
        boolean b = arrDeq3.removeFirst();
    }

    @Test
    /** Add enough items to resize deque and check if it creates successfully */
    public void resizeAddSmallTest() {

        ArrayDeque<Character> dq = new ArrayDeque<>();

        /** Create a deque filled with items */
        dq.addFirst('a');
        dq.addLast('b');
        dq.addLast('c');
        dq.addLast('d');
        dq.addLast('e');
        dq.addLast('f');
        dq.addLast('g');
        dq.addFirst('h');

        int size = dq.size();
        assertEquals(8, size);

        dq.addLast('i');
        size = dq.size();
        assertEquals(9, size);
        char item = dq.get(8);
        assertEquals('i', item);
        item = dq.get(0);
        assertEquals('h', item);
        item = dq.get(4);
        assertEquals('d', item);
        item = dq.get(2);
        assertEquals('b', item);

        dq.printDeque();

        /** Verify rest of the deque that should be null */
        for (int i = 9; i < 16; i += 1) {
            assertNull(dq.get(i));
        }
    }

    @Test
    /** Add larger number of items for testing resize */
    public void resizeAddLargeTest() {
        int N = 200;
        ArrayDeque<Integer> dq = new ArrayDeque<>();

        for (int i = 0; i < N; i += 1) {
            dq.addLast(i);
        }

        for (int j = 0; j < N; j += 1) {
            int item = dq.get(j);
            assertEquals(j, item);
        }

        dq.printDeque();
    }

    @Test
    /** Add and remove enough items to test resize method */
    public void resizeRemoveSmallTest() {
        ArrayDeque<Integer> dq = new ArrayDeque<>();

        for (int i = 0; i < 16; i += 1) {
            dq.addLast(i);
        }

        for (int j = 0; j < 13; j += 1) {
            dq.removeLast();
        }

        dq.printDeque();
        int item = dq.get(2);
        assertEquals(2, item);
        item = 3;
        assertEquals(3, item);
        assertNull(dq.get(4));
    }

    @Test
    /** Add and remove enough items to test resize method */
    public void resizeRemoveTest() {
        int N = 200;
        ArrayDeque<Integer> dq = new ArrayDeque<>();

        for (int i = 0; i < N; i += 1) {
            dq.addLast(i);
        }

        for (int j = 0; j < N - (256 / 4) + 1; j += 1) {
            dq.removeLast();
        }

        dq.printDeque();
    }

    @Test
    /** Create an array deque and some others, for testing equal method */
    public void equalArrayDeque() {
        ArrayDeque<Integer> arrq1 = new ArrayDeque<>();
        arrq1.addLast(5);
        arrq1.addFirst(34);
        arrq1.addLast(70);
        arrq1.addLast(77);
        arrq1.addFirst(2);
        /** Check rule of reflexive */
        assertTrue(arrq1.equals(arrq1));

        ArrayDeque<Integer> arrq2 = new ArrayDeque<>();
        arrq2.addLast(2);
        arrq2.addLast(34);
        arrq2.addLast(5);
        arrq2.addLast(70);
        arrq2.addLast(77);

        /** Check rule of symmetric */
        assertTrue(arrq1.equals(arrq2));
        assertTrue(arrq2.equals(arrq1));

        ArrayDeque<Integer> arrq3 = new ArrayDeque<>();
        arrq3.addFirst(98);
        arrq3.addLast(33);
        arrq3.addFirst(80);
        arrq3.addFirst(2);
        arrq3.addLast(75);
        assertFalse(arrq1.equals(arrq3));
        assertFalse(arrq2.equals(arrq3));

        ArrayDeque<Integer> arrq4 = new ArrayDeque<>();
        arrq4.addFirst(34);
        arrq4.addFirst(2);
        arrq4.addLast(5);
        arrq4.addLast(70);
        arrq4.addLast(77);
        /** Check rule of transitive */
        assertTrue(arrq2.equals(arrq4));
        assertTrue(arrq1.equals(arrq4));
    }

    @Test
    /** Create an array deque and a linked list deque to test equal method */
    public void equalDifferentDeque() {
        ArrayDeque<Integer> arrq1 = new ArrayDeque<>();
        arrq1.addLast(1);
        arrq1.addLast(2);
        arrq1.addFirst(8);
        arrq1.addFirst(90);

        LinkedListDeque<Integer> llk1 = new LinkedListDeque<>();
        llk1.addLast(1);
        llk1.addLast(2);
        llk1.addFirst(8);
        llk1.addFirst(90);
        assertTrue(arrq1.equals(llk1));
    }

    @Test
    /** Check if equals method return false if Object o is null */
    public void equalsTestNull() {
        ArrayDeque<Integer> arrq1 = new ArrayDeque<>();
        assertFalse(arrq1.equals(null));
    }

    @Test
    /** Create an ArrayDeque, add some integer items into it and use enhanced for loop
     *  in order to test for overridden iterator method of the array deque.
     */
    public void enhancedLoopTest() {
        ArrayDeque<Integer> arrq1 = new ArrayDeque<>();

        arrq1.addLast(5);
        arrq1.addLast(9);
        arrq1.addFirst(3);
        arrq1.addLast(10);
        arrq1.addFirst(-2);
        arrq1.addLast(27);

        // Should print out -2 3 5 9 10 27
        for (int item : arrq1) {
            System.out.print(item + " ");
        }
    }

    @Test
    /** Create an empty array deque and use enhanced for loop to test iterator method */
    public void emptyEnhancedLoopTest() {
        ArrayDeque<Integer> arrq1 = new ArrayDeque<>();

        // Should print out nothing
        for (int item : arrq1) {
            System.out.print(item + " ");
        }
    }

    @Test
    /** Create an array deque, add different types of items into it and use enhanced for loop
     *  for testing the generic type of iterator method in ArrayDeque class.
     */
    public void genericTest() {
        ArrayDeque<Double> arrq1 = new ArrayDeque<>();

        arrq1.addLast(5.3);
        arrq1.addLast(9.4);
        arrq1.addFirst(3.5);
        arrq1.addLast(10.6);
        arrq1.addFirst(-2.7);
        arrq1.addLast(27.8);

        // Should print out -2.7 3.5 5.3 9.4 10.6 27.8
        for (double item : arrq1) {
            System.out.print(item + " ");
        }

        ArrayDeque<Character> arrq2 = new ArrayDeque<>();

        arrq2.addLast('p');
        arrq2.addLast('e');
        arrq2.addFirst('u');
        arrq2.addLast('r');
        arrq2.addFirst('s');

        // Should print out 'super'
        for (char item : arrq2) {
            System.out.print(item);
        }
    }
}
