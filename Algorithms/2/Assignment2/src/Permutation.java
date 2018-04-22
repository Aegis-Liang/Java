import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
//        In in = new In(args[0]);
//        int n = in.readInt();

        RandomizedQueue<String> randQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            randQueue.enqueue(str);
        }

        Iterator<String> randIter = randQueue.iterator();
        for(int i=0;i<n;i++)
            System.out.println(randIter.next());

    }
}
