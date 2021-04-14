package com.csp.geometrics;

public class Line {
    private Point start;
    private Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public boolean intersects(Line anotherLine) {
        return doIntersect(this.start, this.end, anotherLine.start, anotherLine.end);
    }

    private static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {

        if((p1.equals(p2) && !q1.equals(q2)) ||
                (p1.equals(q2) && !q1.equals(p2)) ||
                (q1.equals(p2) && !p1.equals(q2)) ||
                (q1.equals(q2) && !p1.equals(p2))){
            return false;
        }

        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

    public static boolean onSegment(Point p, Point q, Point r) {
        return q.x < Math.max(p.x, r.x) && q.x > Math.min(p.x, r.x) &&
                q.y < Math.max(p.y, r.y) && q.y > Math.min(p.y, r.y);
    }

    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0;

        return (val > 0) ? 1 : 2;
    }


    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Line)
            return (start.equals(((Line)obj).start) && end.equals(((Line)obj).end)) ||
                    (start.equals(((Line)obj).end) && end.equals(((Line)obj).start));
        return false;

    }

    @Override
    public String toString() {
        return "["+start.toString() + " ; " + end.toString() + "]";
    }
}
