package org.zeros.bouncy_balls.Objects.Area.PolyLineSegment;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Exceptions.WrongValueException;
import org.zeros.bouncy_balls.Model.Properties;

import javax.security.auth.login.Configuration;
import java.util.ArrayList;
import java.util.Arrays;

public class LineSegment extends Segment {
    private Point2D point1;
    private Point2D point2;
    private LinearEquation equation;
    @Override
    public ArrayList<Point2D> getPoints() {
        return new ArrayList<>(Arrays.asList(point1,point2));
    }

    public LinearEquation getEquation() {
        return equation;
    }

    @Override
    public ArrayList<Point2D> getIntersectionsWith(Segment segment2)  {
        ArrayList<Point2D> intersections=new ArrayList<>();
         if(segment2.getType().equals(SegmentType.LINE)){
            if(BindsCheck.linesIntersect(this,(LineSegment) segment2)){
                intersections.add(equation.intersection(((LineSegment) segment2).equation));
            }
            return intersections;
        }
        else {
            return segment2.getIntersectionsWith(this);
        }
    }

    public LineSegment(Point2D point1, Point2D point2) {
        super(SegmentType.LINE);
        this.point1 = point1;
        this.point2 = point2;
        this.equation=new LinearEquation(point1,point2);
    }

    public Point2D getPoint1() {
        return point1;
    }

    public void setPoint1(Point2D point1) {
        this.point1 = point1;
        this.equation=new LinearEquation(point1,point2);
    }

    public Point2D getPoint2() {
        return point2;
    }

    public void setPoint2(Point2D point2) {
        this.point2 = point2;
        this.equation=new LinearEquation(point1,point2);
    }

    @Override
    public ArrayList<Segment> splitAtPoint(Point2D point) {
        if(equation.distance(point)<= Properties.ACCURACY()&& BindsCheck.isBetweenPoints(point,point1,point2)){
            ArrayList<Segment> split=new ArrayList<>();
            split.add(new LineSegment(point1,point));
            split.add(new LineSegment(point,point2));
            return split;

        }
        throw new IllegalArgumentException("Point "+point+" does not lay on the line");
    }

    @Override
    public Point2D getTangentVectorPointingEnd(Point2D nextPoint) {
        if(nextPoint.distance(point1)<=Properties.ACCURACY())return nextPoint.subtract(point2);
        if(nextPoint.distance(point2)<=Properties.ACCURACY())return nextPoint.subtract(point1);
        throw new IllegalArgumentException("Given point is not an end of a segment");

    }

    @Override
    public void reversePoints() {
        Point2D temp=point1;
        this.point1=point2;
        point2=temp;
    }
}
