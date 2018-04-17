import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] x;
    private final double CONFIDENCE_95 = 1.96;

    public PercolationStats(int n, int trials) {
        x = new double[trials];
        for (int i = 0; i < trials; i++) {
            int openCount = 0;
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int pos = StdRandom.uniform(n * n);
                // open and count it when it's not open, otherwise the openCount will result more larger
                if (!perc.isOpen((pos / n) + 1, pos % n + 1)) {
                    perc.open((pos / n) + 1, pos % n + 1);
                    openCount++;
                }
            }
            x[i] = (double) openCount / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(this.x);
    }

    public double stddev() {
        return StdStats.stddev(this.x);
    }

    public double confidenceLo() {
        return this.mean() - this.CONFIDENCE_95 * this.stddev() / x.length;
    }

    public double confidenceHi() {
        return this.mean() + this.CONFIDENCE_95 * this.stddev() / x.length;
    }


    public static void main(String[] args) {

        PercolationStats pStat = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean\t\t\t\t\t = " + pStat.mean());
        System.out.println("stddev\t\t\t\t\t = " + pStat.stddev());
        System.out.println("95% confidence interval\t = [" + pStat.confidenceLo() + ", " + pStat.confidenceHi() + "]");
    }
}


