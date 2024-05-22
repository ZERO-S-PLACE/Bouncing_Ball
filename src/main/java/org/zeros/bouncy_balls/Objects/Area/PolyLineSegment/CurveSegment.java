package org.zeros.bouncy_balls.Objects.Area.PolyLineSegment;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;

import java.util.ArrayList;
import java.util.Collections;

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
        if(segment2.getType().equals(SegmentType.LINE)){
            return this.getEquation().getIntersectionsWithLine((LineSegment) segment2);
        }
        else {
            return BezierCurve.getIntersections(this.getEquation(), ((CurveSegment) segment2).getEquation());
        }

    }

    @Override
    public ArrayList<Segment> splitAtPoint(Point2D point) {
        ArrayList<BezierCurve> splitCurves = equation.getSubCurves(equation.getParameterAtPoint(point).getFirst());
        ArrayList<Segment> splitSegments = new ArrayList<>();
        for (BezierCurve curve : splitCurves) {
            splitSegments.add(new CurveSegment(curve));
        }
        return splitSegments;

    }

    @Override
    public Point2D getTangentVectorPointingEnd(Point2D nextPoint) {
        LinearEquation tangent;
        LinearEquation secondTangent;
        if(nextPoint.distance(getPoints().getFirst())<=Properties.ACCURACY()) {
            tangent=equation.getTangentAt(0);
            secondTangent=equation.getTangentAt(0.05);
        }else if (nextPoint.distance(getPoints().getLast())<=Properties.ACCURACY()){
            tangent=equation.getTangentAt(1);
            secondTangent=equation.getTangentAt(0.95);
        }else {
            throw new IllegalArgumentException("Given point is not an end of a segment");
        }

        return nextPoint.subtract(tangent.intersection(secondTangent));


    }

    @Override
    public void reversePoints() {
        ArrayList<Point2D> points=equation.getPoints();
        Collections.reverse(points);
        this.equation=new BezierCurve(points);
    }
}
