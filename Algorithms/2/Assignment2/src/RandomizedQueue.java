import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int n;
    private int first;      // index of first element of queue
    private int last;       // index of next available slot

    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
        first = 0;
        last = 0;
    }

    public boolean isEmpty() {
        return this.n == 0;
    }

    public int size() {
        return this.n;
    }

    // resize the underlying array
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = q[(first + i) % q.length];
        }
        q = temp;
        first = 0;
        last = n;
    }

    public void enqueue(Item item) {
        // double size of array if necessary and recopy to front of array
        if (n == q.length) resize(2 * q.length);   // double size of array if necessary
        q[last++] = item;                        // add item
        if (last == q.length) last = 0;          // wrap-around
        // "Wrap around" is the idiom for saying that something in limited room exceeds one end and is continued at the other end.
        n++;                        // add item
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");

        // switch first item and random item before dequeue
        int indexChose = (first + StdRandom.uniform(this.n)) % q.length; // forgot to % q.length
        Item temp = q[indexChose];
        q[indexChose] = q[first];
        q[first] = temp;


        Item item = q[first];
        q[first] = null;                            // to avoid loitering
        n--;
        first++;
        if (first == q.length) first = 0;           // wrap-around
        // shrink size of array if necessary
        if (n > 0 && n == q.length / 4) resize(q.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return q[first + StdRandom.uniform(this.n) % q.length];
    }

    public Iterator<Item> iterator() {
        for (int x = first; x < (last-1 + this.n) % q.length; x++) { // last maybe less than first, for getting correct loop count, add and mod q.length
            int indexChose = (first + StdRandom.uniform(x - first + this.n)) % q.length; // + q.length for avoiding x - first less than 0, cannot plus q.length since some items are null in the q
            Item temp = q[indexChose];
            q[indexChose] = q[x];
            q[x] = temp;
        }
        return new ArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = q[(i + first) % q.length];
            i++;
            return item;
        }
    }


    public static void main(String[] args) {
        RandomizedQueue<String> randQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                randQueue.enqueue(item);
            else if (!randQueue.isEmpty())
                StdOut.print(randQueue.dequeue() + " ");



            StdOut.println("(" + randQueue.size() + " left on queue)");
        }
        Iterator<String> randIter = randQueue.iterator();
        for(int i=0;i<3;i++)
            System.out.println(randIter.next());
    }
}
