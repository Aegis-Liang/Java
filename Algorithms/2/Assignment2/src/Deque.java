import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue
    private int n;               // number of elements on queue

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> prev;
        private Node<Item> next;
    }

    public Deque() {
        this.first = null;
        this.last = null;
        this.n = 0;
    }

    public boolean isEmpty() {
        return (this.first == null);
    }

    public int size() {
        return this.n;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item added is null");
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.prev = null;
        first.next = null;
        if (last == null)      // replace all the isEmpty() to check first or last
            last = first;
        else {
            oldfirst.prev = first;
            first.next = oldfirst;
        }
        n++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item added is null");
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.prev = null;
        last.next = null;
        if (first == null)    // replace all the isEmpty() to check first or last
            first = last;
        else {
            oldlast.next = last;
            last.prev = oldlast;
        }
        n++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        if (first != null)     // update origin first's next's prev is null if the next is not null
            first.prev = null;
        n--;
        if (first == null)  // replace all the isEmpty() to check first or last
            last = null;    // to avoid loitering
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = last.item;
        last = last.prev;
        if (last != null)      // update origin last's prev's next is null if the prev is not null
            last.next = null;
        n--;
        if (last == null)   // replace all the isEmpty() to check first or last
            first = null;   // to avoid loitering
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }

    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                deque.addFirst(item);
            else if (!deque.isEmpty())
                StdOut.print(deque.removeLast() + " ");

            StdOut.println("(" + deque.size() + " left on queue)");
        }

    }
}