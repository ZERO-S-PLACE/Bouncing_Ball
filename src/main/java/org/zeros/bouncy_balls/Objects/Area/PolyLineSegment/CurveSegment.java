package org.zeros.bouncy_balls.Objects.Area.PolyLineSegment;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
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
            return this.getEquation().getIntersectionsWithLine((LineSegment) segment2);
        } else {
            return BezierCurve.getIntersections(this.getEquation(), ((CurveSegment) segment2).getEquation());
        }

    }

    @Override
    public ArrayList<Segment> splitAtPoint(Point2D point) {
        ArrayList<Double> brakePoints=equation.getParameterAtPoint(point);
        ArrayList<Segment> splitSegments = new ArrayList<>();
        if(!brakePoints.isEmpty()) {
            ArrayList<BezierCurve> splitCurves = equation.getSubCurves(brakePoints);
            for (BezierCurve curve : splitCurves) {
                splitSegments.add(new CurveSegment(curve));
            }
        }else {
            Platform.runLater(()-> Model.getInstance().getLevelCreatorController().preview.getChildren().add(
                    new Circle(point.getX(),point.getY(),3))
            );
            throw new IllegalArgumentException("point is not on line");
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
            if (!equation.getParameterAtPoint(segment2.getPoints().getFirst()).isEmpty() && !equation.getParameterAtPoint(segment2.getPoints().getLast()).isEmpty()) {
                for (int i = 1; i < equation.getDegree(); i++) {
                    if (getEquation().getParameterAtPoint(((CurveSegment)segment2).getEquation().getPointAt(1 + (double) (i / ((CurveSegment) segment2).equation.getDegree()))).isEmpty()) {
                        return false;

                    }
                }
                return true;

            } else if (!((CurveSegment) segment2).getEquation().getParameterAtPoint(this.getPoints().getFirst()).isEmpty() && !((CurveSegment) segment2).getEquation().getParameterAtPoint(this.getPoints().getLast()).isEmpty()) {
                for (int i = 1; i < ((CurveSegment) segment2).equation.getDegree(); i++) {
                    if (((CurveSegment) segment2).getEquation().getParameterAtPoint(this.equation.getPointAt(1 + (double) (i / equation.getDegree()))).isEmpty()) {
                        return false;

                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected Segment copyValuesTo() {
        return new CurveSegment(equation);
    }

}
