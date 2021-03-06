import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private boolean isSolvable;
    private int moveSolve;
    private Stack<Board> stackBoard = new Stack<>();

    private class SearchNode implements Comparable<SearchNode>
    {
        private Board board;
        private int move;
        private SearchNode predecessor;
        private int priority;
        private int manhattan;

        public SearchNode(Board board, int move, SearchNode predecessor) {
            if (board == null)
                throw new java.lang.IllegalArgumentException("blocks is null!");

            this.board = board;
            this.move = move;
            this.predecessor = predecessor;

            this.manhattan = this.board.manhattan();
            this.priority = this.manhattan + this.move;
        }

        public int compareTo(SearchNode that)
        {
//            int priorityThis = this.board.manhattan() + this.move;
//            int priorityThat = that.board.manhattan() + that.move;
//            return priorityThis - priorityThat;
            return this.priority - that.priority;
        }
    }



    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();

        this.isSolvable = false;
        this.moveSolve = -1;

        SearchNode sn = new SearchNode(initial, 0, null);
        pq.insert(sn);
        SearchNode snTwin = new SearchNode(initial.twin(), 0, null);
        pqTwin.insert(snTwin);

        while(true)
        {
            SearchNode snCurrent = pq.delMin();
            SearchNode snCurrentTwin = pqTwin.delMin();

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

            if(snCurrentTwin.board.isGoal())
            {
                this.isSolvable = false;
                this.moveSolve = -1;
                break;
            }


            int moveNeighbor = snCurrent.move+1;
            for (Board boardNeighbor : snCurrent.board.neighbors() )
            {
                if(snCurrent.predecessor==null || !snCurrent.predecessor.board.equals(boardNeighbor))
                    pq.insert(new SearchNode(boardNeighbor, moveNeighbor, snCurrent));
            }

            int moveNeighborTwin = snCurrentTwin.move+1;
            for (Board boardNeighbor : snCurrentTwin.board.neighbors() )
            {
                if(snCurrentTwin.predecessor==null || !snCurrentTwin.predecessor.board.equals(boardNeighbor))
                    pqTwin.insert(new SearchNode(boardNeighbor, moveNeighborTwin, snCurrentTwin));
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
        In in = new In("puzzle2x2-unsolvable1.txt");
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