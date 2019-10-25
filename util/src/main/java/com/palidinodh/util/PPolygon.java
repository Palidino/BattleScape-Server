package com.palidinodh.util;

public class PPolygon {
  private int numPoints = 0;
  private Point[] points = new Point[4];

  public PPolygon() {}

  public PPolygon(int[] xs, int[] ys) {
    for (int i = 0; i < xs.length; i++) {
      add(new Point(xs[i], ys[i]));
    }
  }

  public PPolygon(Point[] points) {
    for (Point p : points) {
      add(p);
    }
  }

  private void resize() {
    Point[] temp = new Point[2 * numPoints + 1];
    for (int i = 0; i <= numPoints; i++) {
      temp[i] = points[i];
    }
    points = temp;
  }

  public int size() {
    return numPoints;
  }

  public void add(Point p) {
    if (numPoints >= points.length - 1) {
      resize();
    }
    points[numPoints++] = p;
    points[numPoints] = points[0];
  }

  public double perimeter() {
    double sum = 0.0;
    for (int i = 0; i < numPoints; i++) {
      sum = sum + points[i].distanceTo(points[i + 1]);
    }
    return sum;
  }

  public double area() {
    double sum = 0.0;
    for (int i = 0; i < numPoints; i++) {
      sum = sum + points[i].x * points[i + 1].y - points[i].y * points[i + 1].x;
    }
    return 0.5 * sum;
  }

  public boolean contains(int x, int y) {
    return contains(new Point(x, y));
  }

  public boolean contains(Point p) {
    int winding = 0;
    for (int i = 0; i < numPoints; i++) {
      int ccw = Point.ccw(points[i], points[i + 1], p);
      if (points[i + 1].y > p.y && p.y >= points[i].y) {
        if (ccw == 1) {
          winding++;
        }
      }
      if (points[i + 1].y <= p.y && p.y < points[i].y) {
        if (ccw == -1) {
          winding--;
        }
      }
    }
    return winding != 0;
  }

  public static class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public double distanceTo(Point that) {
      if (that == null) {
        return Double.POSITIVE_INFINITY;
      }
      double dx = x - that.x;
      double dy = y - that.y;
      return Math.hypot(dx, dy);
    }

    public static int ccw(Point a, Point b, Point c) {
      double area2 = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
      if (area2 < 0) {
        return -1;
      } else if (area2 > 0) {
        return 1;
      } else {
        return 0;
      }
    }

    public static boolean collinear(Point a, Point b, Point c) {
      return ccw(a, b, c) == 0;
    }

    public static boolean between(Point a, Point b, Point c) {
      if (ccw(a, b, c) != 0) {
        return false;
      }
      if (a.x == b.x && a.y == b.y) {
        return a.x == c.x && a.y == c.y;
      } else if (a.x != b.x) {
        return a.x <= c.x && c.x <= b.x || a.x >= c.x && c.x >= b.x;
      } else {
        return a.y <= c.y && c.y <= b.y || a.y >= c.y && c.y >= b.y;
      }
    }
  }
}
