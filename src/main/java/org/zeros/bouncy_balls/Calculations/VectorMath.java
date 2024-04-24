package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class VectorMath {

    public static Point2D rotatePoint(Point2D point,Point2D center,double angle){
        return rotateVector(point.subtract(center),angle).add(center);
    }
    public static Point2D rotateVector(Point2D vector, double angle) {
        return new Point2D(vector.getX() * Math.cos(angle) + vector.getY() * Math.sin(angle),
                -vector.getX() * Math.sin(angle) + vector.getY() * Math.cos(angle));
    }
    public static ArrayList<Point2D> rotatePoints(ArrayList<Point2D> points, Point2D center, double angle){
        if(points!=null) {
            points.replaceAll(point -> rotatePoint(point, center, angle));
            return points;
        }
        return null;

    }


}



