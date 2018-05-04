import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int[][] tiles;
    private int n;

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {                                      // (where blocks[i][j] = block in row i, column j)

        if (blocks == null)
            throw new java.lang.IllegalArgumentException("blocks is null!");

        this.n = blocks.length;            // no need to use sqrt
        this.tiles = new int[n][n];
        for (int r = 0; r < this.n; r++)
            for (int c = 0; c < this.n; c++)
                this.tiles[r][c] = blocks[r][c];
    }


    public int dimension()                 // board dimension n
    {
        return this.n;
    }

    public int hamming()                   // number of blocks out of place
    {
        int total = 0;
        for(int r=0; r<this.n;r++)
        {
            for(int c=0; c<this.n; c++)
            {
                if(this.tiles[r][c]==0)
                    continue;
                if(this.tiles[r][c]!=r*n+c+1)
                    total++;
            }
        }
        return total;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int total = 0;
        for (int r = 0; r < this.n; r++) {
            for (int c = 0; c < this.n; c++) {

                int tileNum = this.tiles[r][c];
                int manhattan;
                if (tileNum == 0)
                    manhattan = 0;
                else
                    manhattan = Math.abs((tileNum - 1) / n  - r) + Math.abs((tileNum - 1) % n - c);
                total += manhattan;
            }
        }
        return total;
    }


    public boolean isGoal()                // is this board the goal board?
    {
        for (int r = 0; r < this.n; r++) {
            for (int c = 0; c < this.n; c++) {
                if(r==this.n-1 && c==this.n-1)
                    continue;
                if(this.tiles[r][c] != r*n+c+1)
                    return false;
            }
        }
        return true;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        Board board2 = new Board(this.tiles);

        if(board2.tiles[0][0]==0 || board2.tiles[0][1]==0)
            this.swapTiles(board2, 1, 0, 1, 1);
        else
            this.swapTiles(board2, 0, 0, 0, 1);
        return board2;
    }

    private void swapTiles(Board board, int r1, int c1, int r2, int c2)
    {
        int tile1 = board.tiles[r1][c1];
        int tile2 = board.tiles[r2][c2];

        int tileTemp = tile1;
        board.tiles[r1][c1] = tile2;
        board.tiles[r2][c2] = tileTemp;
        return;
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board board2 = (Board) y;
        if (this.n != board2.n)
            return false;

        for (int r = 0; r < this.n; r++) {
            for (int c = 0; c < this.n; c++) {
                if (this.tiles[r][c] != board2.tiles[r][c])
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> boards = new Stack<>();

        int rZero = 0;
        int cZero = 0;
        // find zero tile
        outerloop:
        for (int r = 0; r < this.n; r++) {
            for (int c = 0; c < this.n; c++) {
                if (this.tiles[r][c] == 0) {
                    rZero = r;
                    cZero = c;
                    break outerloop;
                }
            }
        }

        if (rZero != 0) {
            Board board2 = new Board(this.tiles);
            this.swapTiles(board2, rZero, cZero, rZero - 1, cZero);
            boards.push(board2);
        }

        if (cZero != 0) {
            Board board2 = new Board(this.tiles.clone());
            this.swapTiles(board2, rZero, cZero, rZero, cZero - 1);
            boards.push(board2);
        }

        if (rZero != n - 1) {
            Board board2 = new Board(this.tiles);
            this.swapTiles(board2, rZero, cZero, rZero + 1, cZero);
            boards.push(board2);
        }

        if (cZero != n - 1) {
            Board board2 = new Board(this.tiles);
            this.swapTiles(board2, rZero, cZero, rZero, cZero + 1);
            boards.push(board2);
        }
        return boards;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", this.tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        // create initial board from file
        In in = new In("puzzle04.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        StdOut.println(initial.manhattan());
        StdOut.println(initial.hamming());
        StdOut.println(initial.twin());
        StdOut.println(initial);

        for(Board b : initial.neighbors())
        {
            StdOut.println(b);
        }

        StdOut.println(initial.twin());
    }
}