import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private RedBlackBST<Point2D, Point2D> rb;

    public PointSET()                               // construct an empty set of points
    {
        this.rb = new RedBlackBST<>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return this.rb.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return this.rb.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        this.rb.put(p, p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        return this.rb.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for(Point2D p : this.rb.keys())
        {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        Point2D lo = new Point2D(rect.xmin(), rect.ymin());
        Point2D hi = new Point2D(rect.xmax(), rect.ymax());

        Stack<Point2D> stack = new Stack<>();

        for(Point2D p : this.rb.keys(lo, hi))
            if(p.x()>=rect.xmin() && p.x()<=rect.xmax()&& p.y()>=rect.ymin()&& p.y()<=rect.ymax())
                stack.push(p);

        return stack;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if(this.rb.isEmpty())
            return null;

        Point2D result = null;
        double distance = Double.POSITIVE_INFINITY;

        for(Point2D p2 : this.rb.keys())
        {
            double newDistance = p2.distanceTo(p);
            if(newDistance < distance) {
                result = new Point2D(p2.x(), p2.y());
                distance = newDistance;
            }
        }
        return result;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        // initialize the data structures from file
//        String filename = args[0];
        In in = new In("input10K.txt");
        PointSET brute = new PointSET();
//        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
            brute.insert(p);
        }

        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
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
            brute.draw();

            // draw the rectangle
            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                    Math.max(x0, x1), Math.max(y0, y1));
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();

            // draw the range search results for brute-force data structure in red
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            for (Point2D p : brute.range(rect))
                p.draw();

//            // draw the range search results for kd-tree in blue
//            StdDraw.setPenRadius(.02);
//            StdDraw.setPenColor(StdDraw.BLUE);
//            for (Point2D p : kdtree.range(rect))
//                p.draw();

            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}