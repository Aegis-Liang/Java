import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private int numLineSegments;
    private LinkedList<LineSegment> llistLineSegment= new LinkedList<>();
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        this.numLineSegments = 0;


        Arrays.sort(points);
        int length = points.length;
        for (int i = 0; i < length - 3; i++) {
            Point pointI = points[i];
            for (int j = i + 1; j < length - 2; j++) {
                Point pointJ = points[j];
                double slopeJ = pointI.slopeTo(pointJ);
                for (int k = j + 1; k < length - 1; k++) {
                    Point pointK = points[k];
                    double slopeK = pointI.slopeTo(pointK);
                    if (slopeK != slopeJ)
                        continue;
                    for (int l = k + 1; l < length; l++) {
                        Point pointL = points[l];
                        double slopeL = pointI.slopeTo(pointL);
                        if (slopeL == slopeJ) {
                            this.llistLineSegment.add(new LineSegment(pointI, pointL));
                            this.numLineSegments++;
                        }
                    }
                }
            }
        }

        this.lineSegments = new LineSegment[this.numLineSegments];
        for (int i = 0; i < this.numLineSegments; i++)
            this.lineSegments[i] = this.llistLineSegment.pop();

    }

    public int numberOfSegments()
    {
        return this.numLineSegments;
    }

    public LineSegment[] segments()
    {
//        this.lineSegments = new LineSegment[this.numLineSegments];
//        for(int i=0; i<this.numLineSegments;i++)
//            this.lineSegments[i] = this.llistLineSegment.pop();
        return this.lineSegments;

//        LineSegment[] result = new LineSegment[this.numLineSegments];
//        for(int i=0; i<this.numLineSegments;i++)
//            result[i] = this.llistLineSegment.pop();
//        return result;
    }

    public static void main(String[] args) {

        // read the n points from a file
//        In in = new In(args[0]);
        In in = new In("input8.txt");
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