import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private boolean isSolvable;
    private int moveSolve;
    private MinPQ<SearchNode> pq = new MinPQ<>();
    private Stack<Board> stackBoard = new Stack<>();

    private class SearchNode implements Comparable<SearchNode>
    {
        Board board;
        int move;
        SearchNode predecessor;

        public SearchNode(Board board, int move, SearchNode predecessor) {
            this.board = board;
            this.move = move;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode that)
        {
            int priorityThis = this.board.manhattan() + this.move;
            int priorityThat = that.board.manhattan() + that.move;
            return priorityThis - priorityThat;
        }
    }



    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        this.isSolvable = false;
        this.moveSolve = -1;

        SearchNode sn = new SearchNode(initial, 0, null);
        this.pq.insert(sn);

        while(true)
        {
            SearchNode snCurrent = this.pq.delMin();

            if(snCurrent.board.isGoal())
            {
                this.isSolvable = true;
                this.moveSolve = snCurrent.move;

                while(true) {
                    this.stackBoard.push(snCurrent.board);
                    if (snCurrent.predecessor != null)
                        snCurrent = snCurrent.predecessor;
                    else
                        break;
                }
                break;
            }


            int moveNeighbor = snCurrent.move+1;
            for (Board boardNeighbor : snCurrent.board.neighbors() )
            {
                this.pq.insert(new SearchNode(boardNeighbor, moveNeighbor, snCurrent));
            }
        }
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return this.isSolvable;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return this.moveSolve;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if(this.isSolvable)
            return this.stackBoard;
        else
            return null;
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        // create initial board from file
        In in = new In("puzzle3x3-01.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}