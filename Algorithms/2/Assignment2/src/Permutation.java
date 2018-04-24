import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
//        int k = 3;
        int c = 0;

//        In in = new In(args[0]);
//        int n = in.readInt();

//        RandomizedQueue<String> randQueue = new RandomizedQueue<>();
//        while (!StdIn.isEmpty()) {
//            String str = StdIn.readString();
//            randQueue.enqueue(str);
//        }

//        Iterator<String> randIter = randQueue.iterator();
//        for (int i = 0; i < n; i++)
//            System.out.println(randIter.next());
        RandomizedQueue<String> randQueue = new RandomizedQueue<>();
        for (int i = 0; i < k; i++) {
            String str = StdIn.readString();
            randQueue.enqueue(str);
            c++;
        }
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            int x = StdRandom.uniform(c + 1); // if you gonna use uniform, must to add 1 to avoid c = 0
            if (x < k) {
                randQueue.dequeue();
                randQueue.enqueue(str);
            }
            c++;
        }
        for (int i = 0; i < k; i++) {
            System.out.println(randQueue.dequeue());
        }
    }
}
