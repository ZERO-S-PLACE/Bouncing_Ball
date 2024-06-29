package org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Model.Properties;

import java.util.ArrayList;

public class CurveSegment extends Segment {

    private BezierCurve equation;

    public CurveSegment(ArrayList<Point2D> curvePoints) {
        super(SegmentType.CURVE);
        this.equation = new BezierCurve(curvePoints);
    }

    public CurveSegment(BezierCurve equation) {
        super(SegmentType.CURVE);
        this.equation = equation;
    }

    /*private static boolean isSubsegmentOf(Segment subSegment1, CurveSegment segment) {
        if (segment.getEquation().getParameterAtPoint(subSegment1.getPoints().getFirst()).isEmpty()) return false;
        if (segment.getEquation().getParameterAtPoint(subSegment1.getPoints().getLast()).isEmpty()) return false;
        ArrayList<Segment> subSegments = segment.splitAtPoint(subSegment1.getPoints().getFirst());
        for (Segment subSegmentT : subSegments) {
            if (!((CurveSegment) subSegmentT).getEquation().getParameterAtPoint(subSegment1.getPoints().getLast()).isEmpty()) {
                ArrayList<Segment> subSegments2 = subSegmentT.splitAtPoint(subSegment1.getPoints().getLast());
                for (Segment segment2 : subSegments2) {
                    if (segment2.isEqualTo(subSegment1)) return true;
                }


            }
        }
        return false;

    }*/

    @Override
    public ArrayList<Point2D> getPoints() {
        return equation.getPoints();
    }

    @Override
    public BezierCurve getEquation() {
        return equation;
    }

    @Override
    public ArrayList<Point2D> getIntersectionsWith(Segment segment2) {
        if (segment2.getType().equals(SegmentType.LINE)) {
            return BezierCurve.getIntersections(this.equation, (LineSegment) segment2);
        } else {
            return BezierCurve.getIntersections(this.getEquation(), ((CurveSegment) segment2).getEquation());
        }

    }


    public ArrayList<Segment> splitAtPoint(Point2D point) {

        ArrayList<Segment> splitSegments = new ArrayList<>();
        ArrayList<BezierCurve> splitCurves = equation.getSubCurves(point);
        for (BezierCurve curve : splitCurves) {
            splitSegments.add(new CurveSegment(curve));
        }

        return splitSegments;
    }
 

    public Point2D getTangentVectorPointingEnd(Point2D nextPoint) {
        if (nextPoint.distance(getPoints().getFirst()) <= Properties.ACCURACY()) {
            return getPoints().getFirst().subtract(getPoints().get(1));
        } else if (nextPoint.distance(getPoints().getLast()) <= Properties.ACCURACY()) {
            return getPoints().getLast().subtract(getPoints().get(getPoints().size() - 2));
        }
        throw new IllegalArgumentException("Given point is not an end of a segment");

    }

    @Override
    public void reversePoints() {
        ArrayList<Point2D> points = new ArrayList<>();
        for (int i = getPoints().size() - 1; i >= 0; i--) {
            points.add(getPoints().get(i));

        }
        this.equation = new BezierCurve(points);
    }

    @Override
    public boolean isOnBoundary(Point2D point) {
        return !equation.getParameterAtPoint(point).isEmpty();
    }

    @Override
    public boolean overlapsWith(Segment segment) {
        if (segment.getType().equals(SegmentType.CURVE)) {
            CurveSegment segment2 = (CurveSegment) segment;
            ArrayList<Point2D> commonEnds = new ArrayList<>();

            if (!equation.getParameterAtPoint(segment2.getPoints().getFirst()).isEmpty()) {
                if (VectorMath.doesNotContainPoint(segment2.getPoints().getFirst(), commonEnds)) {
                    commonEnds.add(segment2.getPoints().getFirst());
                }
            }
            if (!equation.getParameterAtPoint(segment2.getPoints().getLast()).isEmpty()) {
                if (VectorMath.doesNotContainPoint(segment2.getPoints().getLast(), commonEnds)) {
                    commonEnds.add(segment2.getPoints().getLast());
                }
            }
            if (!segment2.getEquation().getParameterAtPoint(getPoints().getFirst()).isEmpty()) {
                if (VectorMath.doesNotContainPoint(getPoints().getFirst(), commonEnds)) {
                    commonEnds.add(getPoints().getFirst());
                }
            }
            if (!segment2.getEquation().getParameterAtPoint(getPoints().getLast()).isEmpty()) {
                if (VectorMath.doesNotContainPoint(getPoints().getLast(), commonEnds)) {
                    commonEnds.add(getPoints().getLast());
                }
            }
            System.out.println("connected ends: " + commonEnds.size());

            switch (commonEnds.size()) {
                case 0, 1 -> {
                    return false;
                }
                case 2 -> {
                    return equation.getSubCurveExact(equation.getParameterAtPoint(commonEnds.getFirst()).getFirst(), equation.getParameterAtPoint(commonEnds.getLast()).getFirst()).isEqualTo(segment2.getEquation().getSubCurveExact(segment2.getEquation().getParameterAtPoint(commonEnds.getFirst()).getFirst(), segment2.getEquation().getParameterAtPoint(commonEnds.getLast()).getFirst()));
                }
                default -> {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Segment copyValuesTo() {
        return new CurveSegment(equation);
    }

}
