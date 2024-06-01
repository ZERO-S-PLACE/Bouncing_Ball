package org.zeros.bouncy_balls.Objects.Area.PolyLineSegment;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.Calculations.AreasMath.ConvexHull;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.Equations.Equation;
import org.zeros.bouncy_balls.Model.Properties;

import java.util.ArrayList;

public abstract class Segment implements Cloneable {
    private final SegmentType type;

    public SegmentType getType() {
        return type;
    }

    public Segment(SegmentType type) {
        this.type = type;
    }

    public abstract ArrayList<Point2D> getPoints();

    public abstract Equation getEquation();

    public abstract ArrayList<Point2D> getIntersectionsWith(Segment segment2);

    public abstract ArrayList<Segment> splitAtPoint(Point2D point);

    public boolean isEqualTo(Segment segment) {
        if (this.getPoints().size() != segment.getPoints().size()) return false;
        int incrementFactor;
        int start;
        if (this.getPoints().getFirst().equals(segment.getPoints().getFirst())) {
            incrementFactor = 1;
            start = 0;
        } else if (this.getPoints().getFirst().equals(segment.getPoints().getLast())) {
            incrementFactor = -1;
            start = this.getPoints().size() - 1;
        } else return false;

        for (int i = 1; i < this.getPoints().size(); i++) {
            if (!this.getPoints().get(i).equals(segment.getPoints().get(start + incrementFactor * i))) {
                return false;
            }
        }
        return true;
    }

    public abstract Point2D getTangentVectorPointingEnd(Point2D nextPoint);

    public abstract void reversePoints();

    public Point2D getOtherEnd(Point2D endPoint) {
        if (getPoints().getFirst().distance(endPoint) <= Properties.ACCURACY()) {
            return getPoints().getLast();
        }
        if (getPoints().getLast().distance(endPoint) <= Properties.ACCURACY()) {
            return getPoints().getFirst();
        }
        throw new IllegalArgumentException("Segment does not end at specified point");

    }

    public abstract boolean isOnBoundary(Point2D point);

    public abstract boolean overlapsWith(Segment segment2);

    @Override
    public Segment clone() {
        try {
            Segment clone = (Segment) super.clone();
            clone = copyValuesTo();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    protected abstract Segment copyValuesTo();

    public static Segment getSegmentWithPoint(ArrayList<Segment> segments, Point2D point) {
        for (Segment segment : segments) {
            if (segment.getType().equals(SegmentType.LINE)) {
                if (BindsCheck.isOnLine(point, (LineSegment) segment)) {
                    return segment;

                }
            }else  if (segment.getType().equals(SegmentType.CURVE)) {
                if (!((BezierCurve)segment.getEquation()).getParameterAtPoint(point).isEmpty()) {
                    return segment;
                }
            }

        }
        return null;

    }
}