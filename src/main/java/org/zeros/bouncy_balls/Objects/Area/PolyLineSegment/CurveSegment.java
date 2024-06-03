package org.zeros.bouncy_balls.Objects.Area.PolyLineSegment;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if (segment2.getType().equals(SegmentType.LINE)) {
            return BezierCurve.getIntersections(this.equation,(LineSegment) segment2);
        } else {
            return BezierCurve.getIntersections(this.getEquation(), ((CurveSegment) segment2).getEquation());
        }

    }

    @Override
    public ArrayList<Segment> splitAtPoint(Point2D point) {
       ArrayList<Double> brakePoints=equation.getParameterAtPoint(point);
        ArrayList<Segment> splitSegments = new ArrayList<>();
        ArrayList<BezierCurve> splitCurves = equation.getSubCurves(brakePoints);
            for (BezierCurve curve : splitCurves) {
                splitSegments.add(new CurveSegment(curve));
            }

        return splitSegments;

    }

    @Override
    public Point2D getTangentVectorPointingEnd(Point2D nextPoint) {
        if (nextPoint.distance(getPoints().getFirst()) <= Properties.ACCURACY()) {
            return getPoints().getFirst().subtract(getPoints().get(1));
        } else if (nextPoint.distance(getPoints().getLast()) <= Properties.ACCURACY()) {
            return getPoints().getLast().subtract(getPoints().get(getPoints().size()-2));
        }
            throw new IllegalArgumentException("Given point is not an end of a segment");

    }

    @Override
    public void reversePoints() {
        ArrayList<Point2D> points= new ArrayList<>();
        for (int i=getPoints().size()-1;i>=0;i--){
            points.add(getPoints().get(i));

        }
        this.equation = new BezierCurve(points);
    }

    @Override
    public boolean isOnBoundary(Point2D point) {
        return !equation.getParameterAtPoint(point).isEmpty();
    }

    @Override
    public boolean overlapsWith(Segment segment2) {
        if (segment2.getType().equals(SegmentType.CURVE)) {
            if(this.getPoints().getFirst().distance(this.getPoints().getLast())>
                    segment2.getPoints().getFirst().distance(segment2.getPoints().getLast())){
                return CurveSegment.isSubsegmentOf(segment2,this);
            }
            if(this.getPoints().getFirst().distance(this.getPoints().getLast())<
                    segment2.getPoints().getFirst().distance(segment2.getPoints().getLast())){
                return CurveSegment.isSubsegmentOf(this,(CurveSegment) segment2);
            }
            return isEqualTo(segment2);
        }
        return false;
    }

    private static boolean isSubsegmentOf(Segment subSegment1, CurveSegment segment) {
        if(segment.getEquation().getParameterAtPoint(subSegment1.getPoints().getFirst()).isEmpty()) return false;
        if(segment.getEquation().getParameterAtPoint(subSegment1.getPoints().getLast()).isEmpty()) return false;
        ArrayList<Segment> subSegments=segment.splitAtPoint(subSegment1.getPoints().getFirst());
        for (Segment subSegmentT:subSegments){
            if(!((CurveSegment)subSegmentT).getEquation().getParameterAtPoint(subSegment1.getPoints().getLast()).isEmpty()){
                ArrayList<Segment> subSegments2=subSegmentT.splitAtPoint(subSegment1.getPoints().getLast());
                for (Segment segment2:subSegments2){
                    if(segment2.isEqualTo(subSegment1))return true;
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
