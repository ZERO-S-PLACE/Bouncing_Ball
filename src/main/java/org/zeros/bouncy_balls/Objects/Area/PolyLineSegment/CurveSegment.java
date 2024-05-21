package org.zeros.bouncy_balls.Objects.Area.PolyLineSegment;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Model.Properties;

import java.util.ArrayList;

public class CurveSegment extends Segment {

    private final BezierCurve equation;

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
    public ArrayList<Segment> splitAtPoint(Point2D point) {
        ArrayList<BezierCurve> splitCurves = equation.getSubCurves(equation.getParameterAtPoint(point, Properties.ACCURACY()));
        ArrayList<Segment> splitSegments = new ArrayList<>();
        for (BezierCurve curve : splitCurves) {
            splitSegments.add(new CurveSegment(curve));
        }
        return splitSegments;

    }
}
