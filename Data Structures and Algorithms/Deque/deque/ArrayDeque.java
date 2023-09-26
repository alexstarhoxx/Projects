package deque;

import java.util.Iterator;

/**
 * Create a deque based on array.
 * <p>
 * Invariants:
 * 1. size will always be number of items of the circular array-based deque.
 * 2. next item will always be added into the position of nextFirst or nextLast.
 *
 * @author Alex Ho
 */
public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    /**
     * Create an empty circular array-based deque, initialized with length of 8
     */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 3;
        nextLast = 4;
        size = 0;
    }

    @Override
    /** Add an item to front of a deque circularly, assuming item is never null */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = nextForward(nextFirst);
        size += 1;
    }

    @Override
    /** Add an item to back of a deque circularly, assuming item is never null */
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = nextBackward(nextLast);
        size += 1;
    }

    /**
     * Scan through all items within a deque so that we can return a deque whose
     * index 0 is the first item in the deque.
     */
    private T[] scanItems() {
        T[] scanned = (T[]) new Object[this.size()];
        int start = nextBackward(nextFirst);
        int index = 0;
        while (index != this.size()) {
            scanned[index] = items[start];
            index += 1;
            start = nextBackward(start);
        }
        return scanned;
    }

    @Override
    /** Remove item from front of a deque circularly and return it.
     *  If there is no item exists, return null
     */ public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        nextFirst = nextBackward(nextFirst);
        T removedItem = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;

        if (4 * size < items.length && size > 8) {
            resize(items.length / 2);
        }

        return removedItem;
    }

    @Override
    /** Remove item from back of a deque circularly and return it.
     *  If there is no item exists, return null
     */ public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        nextLast = nextForward(nextLast);
        T removedItem = items[nextLast];
        items[nextLast] = null;
        size -= 1;

        if (4 * size < items.length && items.length > 8) {
            resize(items.length / 2);
        }

        return removedItem;
    }

    /**
     * If deque is full or usage rate is less than 0.25, resize deque
     */
    private void resize(int capacity) {
        /** Copy all items into resized array */
        T[] resized = (T[]) new Object[capacity];
        T[] scanned = scanItems();
        System.arraycopy(scanned, 0, resized, 0, this.size());
        items = resized;

        /** Update nextFirst and nextLast */
        this.nextFirst = items.length - 1;
        this.nextLast = size;
    }

    /**
     * Move index forward, if nextFirst is at beginning of the array, circular it to the end
     */
    private int nextForward(int nextPointer) {
        if (nextPointer == 0) {
            return items.length - 1;
        }
        return nextPointer - 1;
    }

    /**
     * Move index backward, if nextLast is at ending of the array, circular it to the front
     */
    private int nextBackward(int nextPointer) {
        if (nextPointer == items.length - 1) {
            return 0;
        }
        return nextPointer + 1;
    }

    @Override
    /** Override equals method of Object class, return whether the Object o is equal to Deque */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        boolean sameDeque = o instanceof Deque;
        if (!sameDeque) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (other.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i += 1) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    /** Get the item with given index. If no such item exists, return null */
    public T get(int index) {
        if (index >= size) {
            return null;
        }

        T[] scanned = scanItems();

        return scanned[index];
    }

    @Override
    /** Print items in the deque from front to last, separated by space.
     *  Once every item is printed, print out a new line.
     */ public void printDeque() {
        if (isEmpty()) {
            return;
        }
        int index = nextBackward(nextFirst);
        while (index != nextLast) {
            System.out.print(items[index] + " ");
            index = nextBackward(index);
        }
        System.out.println();
    }

    @Override
    /** Return number of items in a deque */ public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        ArrayDequeIterator() {
            wizPos = 0;
        }

        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        @Override
        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }

    }
}
