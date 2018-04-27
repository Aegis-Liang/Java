import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ResizingArrayQueue;

public class FastCollinearPoints {
    private int numLineSegments;
    private ResizingArrayQueue<LineSegment> reqLineSegment = new ResizingArrayQueue<>();


    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException("Point array is null.");
        for (Point ls : points)
            if (ls == null) throw new java.lang.IllegalArgumentException("There is a point is null.");

        this.numLineSegments = 0;
        Point[] copyPoints = points.clone();

        this.numLineSegments = 0;

        Arrays.sort(copyPoints);

        for (int a = 0; a < copyPoints.length - 1; a++)
            if (copyPoints[a].compareTo(copyPoints[a + 1]) == 0)
                throw new java.lang.IllegalArgumentException("Repeat points");

        int length = points.length;
        // base point
        for (int i = 0; i < length - 3; i++) {
            Point pointI = copyPoints[i];

            //Point[] slopesSecondHalf = Arrays.copyOfRange(copyPoints, i + 1, length);
            Point[] slopes1stHalf = Arrays.copyOfRange(copyPoints, 0, i);
            Arrays.sort(slopes1stHalf, pointI.slopeOrder());
            double[] slopes = new double[slopes1stHalf.length];
            for (int a = 0; a < slopes.length; a++)
                slopes[a] = slopes1stHalf[a].slopeTo(pointI);
            Arrays.sort(slopes);

            Point[] slopes2ndHalf = Arrays.copyOfRange(copyPoints, i + 1, length);
            Arrays.sort(slopes2ndHalf, pointI.slopeOrder());


            // start point
            for (int j = 0; j < slopes2ndHalf.length; j++) {
                int count = 0;
                Point pointJ = slopes2ndHalf[j];
                double slopeJ = pointI.slopeTo(pointJ);

                int cursor = j + 1;
                while (cursor < slopes2ndHalf.length) {
                    // slope equals pointJ
                    Point pointCursor = slopes2ndHalf[cursor];
                    if (pointI.slopeTo(pointCursor) == slopeJ) {
                        count++;
                        cursor++;
                    } else
                        break;
                }
                // add to link list
                if (count >= 2) {
                    Point pointPrevious = slopes2ndHalf[cursor - 1];
//                    this.reqLineSegment.enqueue(new LineSegment(pointI, pointPrevious));
//                    this.numLineSegments++;
                    if (Arrays.binarySearch(slopes, slopeJ) < 0) {
                        this.reqLineSegment.enqueue(new LineSegment(pointI, pointPrevious));
                        this.numLineSegments++;
                    }


//                    this.llistLineSegment.add(new LineSegment(pointI, slopes[j]));
//                    this.numLineSegments++;
//                    for (int z = 0; z < count; z++) {
//                        this.llistLineSegment.add(new LineSegment(slopes[z + j], slopes[z + j + 1]));
//                        this.numLineSegments++;
//                    }
                }
                j += count;
            }
        }
    }

    public int numberOfSegments() {
        return this.numLineSegments;
    }

    public LineSegment[] segments() {
//        this.lineSegments = new LineSegment[this.numLineSegments];
//        for (int i = 0; i < this.numLineSegments; i++)
//            this.lineSegments[i] = this.resLineSegment.pop();
//        return this.lineSegments;

        LineSegment[] result = new LineSegment[this.numLineSegments];
        int count = 0;
        for (LineSegment ls : this.reqLineSegment) {
            result[count++] = ls;
        }

        return result;
    }


    public static void main(String[] args) {

        // read the n points from a file
//        In in = new In(args[0]);
        In in = new In("inputMy.txt");
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
