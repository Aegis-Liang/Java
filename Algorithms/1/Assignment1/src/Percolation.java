import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF list;
    private int size;
    private int top;
    private int bottom;
    private boolean[][] state;

    public Percolation(int n) {
        this.list = new WeightedQuickUnionUF(n * n + 2);
        this.size = n;
        this.top = n * n;
        this.bottom = n * n + 1;
        this.state = new boolean[size][size];
    }

    private int xyTo1D(int row, int col) {
        return this.size * (row - 1) + (col - 1);
    }

    private void checkIndex(int row, int col) {
        if (row < 1 || row > this.size)
            throw new IndexOutOfBoundsException("row index out of bounds");
        if (col < 1 || col > this.size)
            throw new IndexOutOfBoundsException("col index out of bounds");
    }

    private boolean validateAndOpen(int row, int col) {
        if (row < 1 || row > this.size || col < 1 || col > this.size)
            return false;
        if (this.isOpen(row, col))
            return true;
        else
            return false;
    }

    public void open(int row, int col) {
        this.checkIndex(row, col);
        int middleIndex = this.xyTo1D(row, col);
        int upIndex = this.xyTo1D(row - 1, col);
        int downIndex = this.xyTo1D(row + 1, col);
        int leftIndex = this.xyTo1D(row, col - 1);
        int rightIndex = this.xyTo1D(row, col + 1);
        boolean upOpen = this.validateAndOpen(row - 1, col);
        boolean downOpen = this.validateAndOpen(row + 1, col);
        boolean leftOpen = this.validateAndOpen(row, col - 1);
        boolean rightOpen = this.validateAndOpen(row, col + 1);

        if (this.isOpen(row, col))
            return;

        // set the state open
        this.state[row - 1][col - 1] = true;

        // first row or col
        if (row == 1)
            // connect top
            this.list.union(this.top, middleIndex);

        // we cannot use "else" here, both row == 1 and row == this.sie need to be verified,
        // otherwise n = 1 will result does not percolate.
        if (row == this.size)
            // connect bottom
            this.list.union(this.bottom, middleIndex);


        // connect 4 directions of the middle point
        if (upOpen)
            this.list.union(middleIndex, upIndex);
        if (downOpen)
            this.list.union(middleIndex, downIndex);
        if (leftOpen)
            this.list.union(middleIndex, leftIndex);
        if (rightOpen)
            this.list.union(middleIndex, rightIndex);
    }

    public boolean isOpen(int row, int col) {
        return this.state[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        return this.list.connected(this.top, this.xyTo1D(row, col));
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < this.size; i++)
            for (int j = 0; j < this.size; j++)
                if (this.state[i][j] == true)
                    count++;
        return count;
    }

    public boolean percolates() {
        return this.list.connected(this.top, this.bottom);
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}