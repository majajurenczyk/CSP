package com.csp.geometrics;

import java.util.ArrayList;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point p) {
        return Math.sqrt(Math.pow((this.x - p.x), 2) + Math.pow((this.y - p.y), 2));
    }

    public Point getClosestPoint(ArrayList<Point> points){
        Point closest = points.get(0);
        for (Point p: points) {
            if(this.distance(closest) == 0 || (this.distance(p) < this.distance(closest) && this.distance(p) != 0)){
                closest = p;
            }
        }
        return closest;
    }

    @Override
    public String toString() {
        return "(" + this.x + " , " + this.y +")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Point)
            return this.x == ((Point)obj).x && this.y == ((Point)obj).y;
        return false;
    }
}
