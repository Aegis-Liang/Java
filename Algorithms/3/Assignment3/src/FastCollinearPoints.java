import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int numLineSegments;
    private LinkedList<LineSegment> llistLineSegment = new LinkedList<>();
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        this.numLineSegments = 0;

        Arrays.sort(points);
        int length = points.length;
        // origin point
        for (int i = 0; i < length - 3; i++) {
            Point pointI = points[i];

            Point[] slopes = Arrays.copyOfRange(points, i + 1, length - 1);
            Arrays.sort(slopes, pointI.slopeOrder());

            // start point
            for (int j = 0; j < slopes.length; j++) {
                int count = 0;
                Point pointJ = points[i + 1 + j];
                double slopeJ = pointI.slopeTo(pointJ);

                int cursor = j + 1;
                while (cursor < slopes.length) {
                    // slope equals pointJ
                    Point pointCursor = points[i + 1 + cursor];
                    if (pointI.slopeTo(pointCursor) == slopeJ) {
                        count++;
                        cursor++;
                    } else
                        break;
                }
                // add to link list
                if (count >= 3) {
                    Point pointPrevious = points[i + 1 + cursor - 1];
                    this.llistLineSegment.add(new LineSegment(pointI, pointPrevious));
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.numLineSegments;
    }

    public LineSegment[] segments() {
        this.lineSegments = new LineSegment[this.numLineSegments];
        for (int i = 0; i < this.numLineSegments; i++)
            this.lineSegments[i] = this.llistLineSegment.pop();
        return this.lineSegments;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
