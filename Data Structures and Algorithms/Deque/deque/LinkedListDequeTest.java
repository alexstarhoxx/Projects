package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();

    }

    @Test
    /** Create a non-empty deque by using constructor */
    public void nonEmptyDequeCreateTest() {

        /** LinkedListDeque<Integer> lld1 = new LinkedListDeque<>(15);

        // should not be empty
        assertFalse("lld1 should not be empty upon initialization", lld1.isEmpty());
        assertEquals(1, lld1.size()); */

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }

    @Test
    /** To check if you can get expected value by using get or getRecursive method */
     public void getTest() {
         LinkedListDeque<Integer> llst = new LinkedListDeque<>();

         /** No such item right now, should return null */
         assertNull(llst.get(0));
         assertNull(llst.get(1));
         assertNull(llst.get(5));
         assertNull(llst.getRecursive(0));
         assertNull(llst.getRecursive(1));
         assertNull(llst.getRecursive(5));

         for (int i = 0; i < 100; i += 1) {
             llst.addLast(i);
         }

         int item = llst.get(7);
         int itemRecur = llst.getRecursive(7);
         assertEquals(7, item);
         assertEquals(7, itemRecur);

         item = llst.get(3);
         itemRecur = llst.getRecursive(3);
         assertEquals(3, item);
         assertEquals(3, itemRecur);

         item = llst.get(69);
         itemRecur = llst.getRecursive(69);
         assertEquals(69, item);
         assertEquals(item, itemRecur);

         /** If index is greater or equal to size, it should return null */
         assertNull(llst.get(101));
         assertNull(llst.get(200));
         assertNull(llst.getRecursive(101));
         assertNull(llst.getRecursive(200));
    }

    @Test
    /** Do randomize test on series of add methods and size method for total of N calls to these methods */
    public void addSizeRandomizedTest() {
        /** LinkedListDeque<Integer> llst1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> llst2 = new LinkedListDeque<>(1);

        assertTrue(llst1.isEmpty());
        assertFalse(llst2.isEmpty());

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randomVal = StdRandom.uniform(0, 100);
                llst1.addLast(randomVal);
                llst2.addLast(randomVal);
                int llst1Last = llst1.get(llst1.size() - 1);
                assertEquals(randomVal, llst1Last);
                int llst2Last = llst2.get(llst2.size() - 1);
                assertEquals(randomVal,llst2Last);
            } else if (operationNumber == 1) {
                // addFirst
                int randomVal = StdRandom.uniform(0, 100);
                llst1.addFirst(randomVal);
                llst2.addFirst(randomVal);
                int llst1First = llst1.get(0);
                int llst2First = llst2.get(0);
                assertEquals(randomVal, llst1First);
                assertEquals(randomVal, llst2First);
            }  else if (operationNumber == 2) {
                // size
                int size1 = llst1.size();
                int size2 = llst2.size();
                assertEquals(size1 + 1, size2);
            }
        } */
    }

    @Test
    /** Do randomize test on series of add methods and size method for total of N calls to these methods */
    public void removeSizeRandomizedTest() {
        /** LinkedListDeque<Integer> llst1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> llst2 = new LinkedListDeque<>(1);

        assertTrue(llst1.isEmpty());
        assertFalse(llst2.isEmpty());

        int N = 100000;

        for (int i = N / 2; i < N; i += 1) {
            llst1.addLast(i);
            llst2.addLast(i);
        }

        for (int j = 0; j < N / 2; j += 1) {
            llst1.addLast(j);
            llst2.addLast(j);
        }

        assertEquals(llst1.size() + 1, llst2.size());

        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // removeLast
                int llst1Last = llst1.removeLast();
                int llst2Last = llst2.removeLast();
                assertEquals(llst1Last, llst2Last);
            } else if (operationNumber == 1) {
                // removeFirst
                llst1.removeFirst();
                llst2.removeFirst();
            }  else if (operationNumber == 2) {
                // size
                int size1 = llst1.size();
                int size2 = llst2.size();
                assertEquals(size1 + 1, size2);
            }
        } */
    }

    @Test
    /** create a linked list deque and some others with same or different content, for testing equal method */
    public void equalTestLinkedList() {
        LinkedListDeque<Integer> llk1 = new LinkedListDeque<>();
        llk1.addFirst(15);
        llk1.addFirst(13);
        llk1.addLast(28);
        llk1.addLast(90);
        llk1.addLast(189);
        /** Check rule of reflexive **/
        assertTrue(llk1.equals(llk1));

        LinkedListDeque<Integer> llk2 = new LinkedListDeque<>();
        llk2.addLast(15);
        llk2.addFirst(13);
        llk2.addLast(28);
        llk2.addLast(90);
        llk2.addLast(189);
        /** Check rule of summetric */
        assertTrue(llk1.equals(llk2));
        assertTrue(llk2.equals(llk1));

        LinkedListDeque<Integer> llk3 = new LinkedListDeque<>();
        llk3.addFirst(15);
        llk3.addFirst(333);
        llk3.addLast(28);
        llk3.addLast(90);
        llk3.addLast(139);
        assertFalse(llk1.equals(llk3));
        assertFalse(llk2.equals(llk3));

        LinkedListDeque<Integer> llk4 = new LinkedListDeque<>();
        llk4.addFirst(15);
        llk4.addFirst(333);
        llk4.addLast(28);
        assertFalse(llk1.equals(llk4));
        assertFalse(llk2.equals(llk4));
        assertFalse(llk3.equals(llk4));

        LinkedListDeque<Integer> llk5 = new LinkedListDeque<>();
        llk5.addLast(13);
        llk5.addLast(15);
        llk5.addLast(28);
        llk5.addLast(90);
        llk5.addLast(189);
        /** Check rule of transitive */
        assertTrue(llk2.equals(llk5));
        assertTrue(llk1.equals(llk5));
    }

    @Test
    /** Create a linked list deque and an array deque for testing equal method */
    public void equalTestDifferentDeque() {
        LinkedListDeque<Integer> llk1 = new LinkedListDeque<>();
        llk1.addFirst(56);
        llk1.addFirst(37);
        llk1.addLast(10);
        llk1.addLast(54);
        llk1.addLast(99);

        ArrayDeque<Integer> arr = new ArrayDeque<>();
        arr.addFirst(56);
        arr.addFirst(37);
        arr.addLast(10);
        arr.addLast(54);
        arr.addLast(99);

        assertTrue(llk1.equals(arr));
    }

    @Test
    /** Test if equals method return false when Object o is null */
    public void equalsTestNull() {
        LinkedListDeque<Integer> llk1 = new LinkedListDeque<>();
        assertFalse(llk1.equals(null));
    }

    @Test
    /** Create an empty linked list and use enhanced for loop to test iterator method */
    public void emptyEnhancedLoopTest() {
        LinkedListDeque<Integer> llk1 = new LinkedListDeque<>();

        // Should print out nothing
        for (int item : llk1) {
            System.out.print(item + " ");
        }
    }

    @Test
    /** Create a linked list deque, add some items into it and use enhanced for loop
     *  for testing the implementation of iterator method in LinkListDeque class.
     */
    public void enhancedLoopTest() {
        LinkedListDeque<Integer> llk1 = new LinkedListDeque<>();

        llk1.addLast(5);
        llk1.addLast(9);
        llk1.addFirst(3);
        llk1.addLast(10);
        llk1.addFirst(-2);
        llk1.addLast(27);

        // Should print out -2 3 5 9 10 27
        for (int item : llk1) {
            System.out.print(item + " ");
        }
    }

    @Test
    /** Create a linked list deque, add different types of items into it and use enhanced for loop
     *  for testing the generic type of iterator method in LinkListDeque class.
     */
    public void genericTest() {
        LinkedListDeque<Double> llk1 = new LinkedListDeque<>();

        llk1.addLast(5.3);
        llk1.addLast(9.4);
        llk1.addFirst(3.5);
        llk1.addLast(10.6);
        llk1.addFirst(-2.7);
        llk1.addLast(27.8);

        // Should print out -2.7 3.5 5.3 9.4 10.6 27.8
        for (double item : llk1) {
            System.out.print(item + " ");
        }

        LinkedListDeque<Character> llk2 = new LinkedListDeque<>();

        llk2.addLast('p');
        llk2.addLast('e');
        llk2.addFirst('u');
        llk2.addLast('r');
        llk2.addFirst('s');

        // Should print out 'super'
        for (char item : llk2) {
            System.out.print(item);
        }
    }
}
