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
    public static int binomialCoefficient(int n, int k){
        n=Math.abs(n);
        k=Math.abs(k);
        if(n>=k) {
            return factorial(n)/(factorial(k)*factorial(n-k));
        }else{
            return factorial(k)/(factorial(n)*factorial(k-n));
        }

    }
    public static int factorial(int x){
            x=Math.abs(x);
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


}



