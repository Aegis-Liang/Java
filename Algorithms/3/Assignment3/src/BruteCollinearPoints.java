import java.util.Arrays;
//import java.util.LinkedList;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private int numLineSegments;
    //    private LinkedList<LineSegment> llistLineSegment= new LinkedList<>();
    private ResizingArrayStack<LineSegment> resLineSegment = new ResizingArrayStack<>();

    public BruteCollinearPoints(Point[] points) {

        if(points == null) throw new java.lang.IllegalArgumentException("Point array is null.");
        for(Point ls : points)
            if (ls == null) throw new java.lang.IllegalArgumentException("There is a point is null.");

        this.numLineSegments = 0;
        Point[] copyPoints =  points.clone();

        Arrays.sort(copyPoints);
        for (int a=0; a<copyPoints.length-1;a++)
            if(copyPoints[a].compareTo(copyPoints[a+1]) == 0) throw new java.lang.IllegalArgumentException("Repeat points");

        int length = copyPoints.length;
        for (int i = 0; i < length - 3; i++) {
            Point pointI = copyPoints[i];
            for (int j = i + 1; j < length - 2; j++) {
                Point pointJ = copyPoints[j];
                double slopeJ = pointI.slopeTo(pointJ);
                for (int k = j + 1; k < length - 1; k++) {
                    Point pointK = copyPoints[k];
                    double slopeK = pointI.slopeTo(pointK);
                    if (slopeK != slopeJ)
                        continue;
                    for (int l = k + 1; l < length; l++) {
                        Point pointL = copyPoints[l];
                        double slopeL = pointI.slopeTo(pointL);
                        if (slopeL == slopeJ) {
//                            this.llistLineSegment.add(new LineSegment(pointI, pointL));
                            this.resLineSegment.push(new LineSegment(pointI, pointL));
                            this.numLineSegments++;
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.numLineSegments;
    }

    public LineSegment[] segments() {
//        this.lineSegments = new LineSegment[this.numLineSegments];
//        for(int i=0; i<this.numLineSegments;i++)
//            this.lineSegments[i] = this.llistLineSegment.pop();
//        return this.lineSegments;

//        LineSegment[] result = new LineSegment[this.numLineSegments];
//        for(int i=0; i<this.numLineSegments;i++)
//            result[i] = this.llistLineSegment.pop();
//        return result;

        LineSegment[] result = new LineSegment[this.numLineSegments];
        int count = 0;
        for (LineSegment ls : this.resLineSegment)
            result[count++] = ls;
        return result;
    }

    public static void main(String[] args) {

        // read the n points from a file
//        In in = new In(args[0]);
        In in = new In("input9.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}