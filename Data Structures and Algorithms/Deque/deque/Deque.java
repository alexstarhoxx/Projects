package deque;

public interface Deque<T> {
    /** Add an item to front of a deque circularly, assuming item is never null */
    void addFirst(T item);

    /** Add an item to back of a deque circularly, assuming item is never null */
    void addLast(T item);

    /** Remove item from front of a deque circularly and return it.
     *  If there is no item exists, return null
     */
    T removeFirst();

    /** Remove item from back of a deque circularly and return it.
     *  If there is no item exists, return null
     */
    T removeLast();

    /** Get the item with given index. If no such item exists, return null */
    T get(int index);

    /** Print items in the deque from front to last, separated by space.
     *  Once every item is printed, print out a new line.
     */
    void printDeque();

    /** Return number of items in a deque */
    int size();

    /** Return true if deque is empty, return false otherwise */
    default boolean isEmpty() {
        return size() == 0;
    }
}
