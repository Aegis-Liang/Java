import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect, Node lb, Node rt)
        {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    private Node root;
    private Stack stack;

    public KdTree()                               // construct an empty set of points
    {
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return this.size() == 0;
    }


    public int size()                             // number of points in the set
    {
        if(this.root == null)
            return 0;
        return size(this.root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return 1 + size(x.lb) + size(x.rt);
    }


    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException("calls insert() with a null p");

        this.root = insert(this.root, p, new RectHV(0, 0, 1,1), 0);
    }

    private Node insert(Node x, Point2D p, RectHV rect, int level)
    {
        // Remark: Only root could be null in the coming recursion
        if (x == null)
            return new Node(p, rect, null, null);
        else {
            if (level % 2 == 0) {
                if (p.x() < x.p.x())
                    x.lb = insert(x.lb, p, new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax()), level + 1);
                else
                    x.rt = insert(x.rt, p, new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax()), level + 1);
            } else {
                if (p.y() < x.p.y())
                    x.lb = insert(x.lb, p, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y()), level + 1);
                else
                    x.rt = insert(x.rt, p, new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax()), level + 1);
            }
        }


        return x;

    }


    public boolean contains(Point2D p)            // does the set contain point p?
    {
        return this.get(this.root, p, 0)!= null;
    }

    private Point2D get(Node x, Point2D p, int level)
    {
        if (x==null) return null;
        if (x.p.equals(p)) return p;
        if (level % 2 == 0) {
            if (p.x() < x.p.x())
                return get(x.lb, p, level+1);
            else
                return get(x.rt, p, level+1);
        }
        else
        {
            if (p.y() < x.p.y())
                return get(x.lb, p, level+1);
            else
                return get(x.rt, p, level+1);
        }
    }

    public void draw()                         // draw all points to standard draw
    {
        this.draw(this.root);
    }

    private void draw(Node x) {
//        StdDraw.setPenRadius(.005);
//        StdDraw.setPenColor(StdDraw.BLUE);
        x.p.draw();
//        StdDraw.setPenRadius(.002);
//        StdDraw.setPenColor(StdDraw.PINK);
        x.rect.draw();

        if (x.lb != null)
            this.draw(x.lb);
        if (x.rt != null)
            this.draw(x.rt);
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        this.stack = new Stack<>();
        this.range(this.root, rect, 0);
        return this.stack;
    }

    private void range(Node x, RectHV rect, int level) {
        if (x == null) return;
        if (x.p.x() >= rect.xmin() && x.p.x() <= rect.xmax() && x.p.y() >= rect.ymin() && x.p.y() <= rect.ymax())
            this.stack.push(x.p);
        if (level % 2 == 0) {
            if (x.p.x() >= rect.xmin() && x.p.x() >= rect.xmax())
                this.range(x.lb, rect, level + 1);
            else if (x.p.x() <= rect.xmin() && x.p.x() <= rect.xmax())
                this.range(x.rt, rect, level + 1);
            else {
                this.range(x.lb, rect, level + 1);
                this.range(x.rt, rect, level + 1);
            }
        } else {
            if (x.p.y() >= rect.ymin() && x.p.y() >= rect.ymax())
                this.range(x.lb, rect, level + 1);
            else if (x.p.y() <= rect.ymin() && x.p.y() <= rect.ymax())
                this.range(x.rt, rect, level + 1);
            else {
                this.range(x.lb, rect, level + 1);
                this.range(x.rt, rect, level + 1);
            }
        }
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        return null;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        // initialize the data structures from file
//        String filename = args[0];
        In in = new In("input10.txt");
//        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
//            brute.insert(p);
        }

        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
//        brute.draw();
        kdtree.draw();
        StdDraw.show();

        /*
        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
//            query = new Point2D(0.1,0.1);
//            StdDraw.setPenColor(StdDraw.GREEN);
//            query.draw();
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);

//            // draw in blue the nearest neighbor (using kd-tree algorithm)
//            StdDraw.setPenColor(StdDraw.BLUE);
//            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
        */


        // process range search queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // user starts to drag a rectangle
            if (StdDraw.isMousePressed() && !isDragging) {
                x0 = x1 = StdDraw.mouseX();
                y0 = y1 = StdDraw.mouseY();
                isDragging = true;
            }

            // user is dragging a rectangle
            else if (StdDraw.isMousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
            }

            // user stops dragging rectangle
            else if (!StdDraw.isMousePressed() && isDragging) {
                isDragging = false;
            }

            // draw the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
//            brute.draw();
            kdtree.draw();


            // draw the rectangle
            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                    Math.max(x0, x1), Math.max(y0, y1));
//            RectHV rect = new RectHV(0,0, 0.5, 1);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();

//            // draw the range search results for brute-force data structure in red
//            StdDraw.setPenRadius(0.03);
//            StdDraw.setPenColor(StdDraw.RED);
//            for (Point2D p : brute.range(rect))
//                p.draw();

            // draw the range search results for kd-tree in blue
            StdDraw.setPenRadius(.02);
            StdDraw.setPenColor(StdDraw.BLUE);
            for (Point2D p : kdtree.range(rect))
                p.draw();

            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}