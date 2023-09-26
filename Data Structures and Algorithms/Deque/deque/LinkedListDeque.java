package deque;

import java.util.Iterator;

/**
 * Create a Linked List Deque
 *
 * @author Alex Ho
 */
public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private final StuffNode sentinel;
    private int size;

    /**
     * Create an empty Linked List Deque
     */
    public LinkedListDeque() {
        sentinel = new StuffNode(null, null, null);
        sentinel.pre = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /**
     * Create a non-empty Linked List Deque
     *
     * Uncomment this constructor if you want to test implementations of this class.
     */
    /* public LinkedListDeque(T item) {
        sentinel = new StuffNode(null, null, null);
        sentinel.next = new StuffNode(sentinel, item, sentinel);
        sentinel.pre = sentinel.next;
        size += 1;
    } */

    @Override
    /** Add item to Linked List Deque in front, assuming item will never be null */
    public void addFirst(T item) {
        size += 1;
        sentinel.next.pre = new StuffNode(sentinel, item, sentinel.next);
        sentinel.next = sentinel.next.pre;
    }

    @Override
    /** Add item to the back of deque , assuming item will never be null */
    public void addLast(T item) {
        size += 1;
        sentinel.pre.next = new StuffNode(sentinel.pre, item, sentinel);
        sentinel.pre = sentinel.pre.next;
    }

    @Override
    /**
     * Remove and return the item at the front of the deque.
     * If no such item exists, return null.
     */ public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            size -= 1;
            T removedItem = sentinel.next.item;
            sentinel.next.next.pre = sentinel;
            sentinel.next = sentinel.next.next;
            return removedItem;
        }
    }

    @Override
    /**
     * Remove and return the item at the back of the deque.
     * If no such item exists, return null.
     */ public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            size -= 1;
            T removedItem = sentinel.pre.item;
            sentinel.pre = sentinel.pre.pre;
            sentinel.pre.next = sentinel;
            return removedItem;
        }
    }

    @Override
    /** Return item in given index using iteration. If no such item exists, return null */
    public T get(int index) {

        if (this.size() <= index) {
            return null;
        }

        StuffNode p = sentinel.next;
        for (int i = 0; i < index; i += 1) {
            p = p.next;
        }
        return p.item;
    }

    /**
     * Return item in given index using recursion. If no such item exists, return null
     */
    public T getRecursive(int index) {

        if (this.size() <= index) {
            return null;
        }

        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(StuffNode p, int index) {
        if (index == 0) {
            return p.item;
        } else {
            return getRecursiveHelper(p.next, index - 1);
        }
    }

    @Override
    /** Override equals method of Object Class, return whether the Object o is equal to Deque */
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
    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */ public void printDeque() {
        StuffNode p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    @Override
    /** Return number of items in deque */ public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkListDequeIterator();
    }

    private class StuffNode {
        private StuffNode pre;
        private T item;
        private StuffNode next;


        StuffNode(StuffNode p, T i, StuffNode n) {
            pre = p;
            item = i;
            next = n;
        }
    }

    private class LinkListDequeIterator implements Iterator<T> {
        private int wizPos;

        LinkListDequeIterator() {
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
