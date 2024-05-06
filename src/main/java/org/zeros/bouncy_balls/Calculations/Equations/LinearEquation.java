package org.zeros.bouncy_balls.Calculations.Equations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.BindsCheck;

import static java.lang.Float.NaN;

public class LinearEquation {
    private boolean vertical = false;
    private double a;
    private double b;

    public LinearEquation(double a, double b) {
        this.a = a;
        this.b = b;
        if (Double.isNaN(b)) {
            this.vertical = true;
        }
        //System.out.println("Equation "+a +"X+"+b +"=y , Vertical :"+vertical);
    }

    public LinearEquation(Point2D c, Point2D d) {
        if (!c.equals(d)) {
            if (c.getX() != d.getX()) {
                this.a = (c.getY() - d.getY()) / (c.getX() - d.getX());
                this.b = c.getY() - a * c.getX();
            } else {
                this.a = c.getX();
                this.b = Double.NaN;
                vertical = true;
            }
            //System.out.println("Equation "+a +"X+"+b +"=y , Vertical :"+vertical);
        }
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public boolean isVertical() {
        return vertical;
    }

    public double getValue(double x) {
        if (!isVertical()) {
            return a * x + b;
        } else {
            return NaN;
        }

    }

    public Point2D getPoint(double x) {
        return new Point2D(x, getValue(x));
    }

    public double getArgument(double y) {
        if (a != 0) {
            return (y - b) / a;
        } else {
            return NaN;
        }

    }

    public LinearEquation perpendicularTroughPoint(Point2D e) {
        if (a != 0 && !isVertical()) {
            return new LinearEquation((-1) / a, e.getY() + e.getX() / a);
        } else if (a == 0 && !isVertical()) {
            return new LinearEquation(e.getX(), Double.NaN);
        } else {
            return new LinearEquation(0, e.getY());
        }
    }

    public Point2D intersection(LinearEquation equation) {
        if (a != equation.getA() && !equation.isVertical() && !this.isVertical()) {
            double x = (equation.getB() - b) / (a - equation.getA());
            double y = a * x + b;
            return new Point2D(x, y);
        } else if (equation.isVertical()) {
            return new Point2D(equation.getA(), this.getValue(equation.getA()));
        } else if (this.isVertical()) {
            return new Point2D(a, equation.getValue(a));
        }
        return null;

    }

    public LinearEquation offsetLine(double distance) {
        if (isVertical()) {
            return new LinearEquation(a + distance, Double.NaN);
        }

        return new LinearEquation(a, b + (distance * Math.sqrt(a * a + 1)));
    }

    public LinearEquation offsetLine(double distance, Point2D directionPoint) {
        distance = Math.abs(distance);
        if (this.distance(directionPoint) != 0) {
            if (isVertical() && directionPoint.getX() > a) {
                return new LinearEquation(a + distance, Double.NaN);
            }
            if (isVertical() && directionPoint.getX() < a) {
                return new LinearEquation(a - distance, Double.NaN);
            }

            if (directionPoint.getY() < this.getValue(directionPoint.getX())) {

                return new LinearEquation(a, b - (distance * Math.sqrt(a * a + 1)));
            }
            return new LinearEquation(a, b + (distance * Math.sqrt(a * a + 1)));
        }
        return offsetLine(distance);
    }

    public LinearEquation offsetLine(double distance, LinearEquation directionParallelLine) {
        if (this.getA() == directionParallelLine.getA()) {
            if (isVertical()) {
                return offsetLine(distance, new Point2D(directionParallelLine.getA(), 0));
            }
            return offsetLine(distance, directionParallelLine.getPoint(0));
        }

        return offsetLine(distance);
    }

    public Point2D mirrorPoint(Point2D point) {
        LinearEquation perpendicular = this.perpendicularTroughPoint(point);
        Point2D intersection = this.intersection(perpendicular);
        double offset = point.distance(intersection);
        LinearEquation second = this.offsetLine(offset);
        Point2D offsetPoint = second.intersection(perpendicular);
        if (!point.equals(offsetPoint)) {
            return second.intersection(perpendicular);
        }
        return perpendicular.intersection(this.offsetLine(-offset));


    }

    public LinearEquation parallelTroughPoint(Point2D point) {
        if (!isVertical()) {
            return new LinearEquation(a, point.getY() - a * point.getX());
        }
        return new LinearEquation(point.getX(), b);

    }

    public double pointDistance(Point2D point) {
        return this.perpendicularTroughPoint(point).intersection(this).distance(point);

    }

    public double angle() {
        if (isVertical()) {
            return Math.PI / 2;
        }
        return Math.atan(a);
    }

    public double distance(Point2D point) {


        return Math.abs(this.perpendicularTroughPoint(point).intersection(this).distance(point));
    }

    public boolean areOnDifferentSides(Point2D point1, Point2D point2) {

        if (point1.equals(point2)) {
            return true;
        }
        Point2D intersection = this.intersection(new LinearEquation(point1, point2));
        if (intersection != null) {
            return BindsCheck.isBetweenPoints(intersection, point1, point2);
        }
        return false;
    }


    @Override
    public String toString() {
        return "line:[y= " + a + " * x + " + b + " ]";
    }
}
