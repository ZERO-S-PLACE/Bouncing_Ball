package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

import java.util.ArrayList;

public class VectorMath {

    public static Point2D rotatePoint(Point2D point, Point2D center, double angle) {
        return rotateVector(point.subtract(center), angle).add(center);
    }
    public static Point2D rotateVector(Point2D vector, double angle) {
        return new Point2D(vector.getX() * Math.cos(angle) + vector.getY() * Math.sin(angle), -vector.getX() * Math.sin(angle) + vector.getY() * Math.cos(angle));
    }
    public static ArrayList<Point2D> rotatePoints(ArrayList<Point2D> points, Point2D center, double angle) {
        if (points != null) {
            points.replaceAll(point -> rotatePoint(point, center, angle));
            return points;
        }
        return null;
    }
    public static double binomialCoefficient(int n, int k) {
        if (n < 0 || k < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        if (n >= k) {

            return VectorMath.factorial(n) / VectorMath.factorial(k) / VectorMath.factorial(n - k);
        } else {

            return VectorMath.factorial(k) / VectorMath.factorial(n) / VectorMath.factorial(k - n);
        }

    }
    public static double factorial(int x) {

        if (x < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        int temp = 1;
        for (int i = 2; i <= x; i++) {
            temp = temp * i;
        }
        return temp;
    }

    public static double[] reverseArray(double[] array) {
        int start = 0;
        int end = array.length - 1;

        while (start < end) {
            double temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
        return array;
    }

    public static double counterClockWiseAngle(Point2D reference, Point2D vector) {
        double angle = reference.angle(vector);
        if (reference.crossProduct(vector).getZ() > 0) return 360 - angle;
        return angle;
    }


    public static boolean doesNotContainPoint(Point2D point, ArrayList<Point2D> points) {
        for (Point2D point2 : points) {
            if (point2.distance(point) <= Properties.ACCURACY() / 10) {
                return false;
            }
        }
        return true;
    }

    public static boolean rotationIsClockWise(Point2D start, Point2D end, Point2D center) {
        Point2D startLeadingVector=start.subtract(center);
        Point2D endLeadingVector=end.subtract(center);
        return startLeadingVector.crossProduct(endLeadingVector).getZ()>0;
    }
}



