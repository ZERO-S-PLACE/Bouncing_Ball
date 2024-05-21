package org.zeros.bouncy_balls.Objects.Area.PolyLineSegment;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.Equations.Equation;

import java.util.ArrayList;

public abstract class Segment {
    private final SegmentType type;

    public SegmentType getType() {
        return type;
    }

    public Segment(SegmentType type) {
        this.type = type;
    }
    public abstract ArrayList<Point2D> getPoints();
    public abstract Equation getEquation();

    public boolean intersectsWith(Segment segment2) {
        if(this.getType().equals(SegmentType.LINE)&&segment2.getType().equals(SegmentType.LINE)){
            return BindsCheck.linesIntersect((LineSegment) this, (LineSegment) segment2);
        }
        else if(this.getType().equals(SegmentType.LINE)){
            return !((CurveSegment) segment2).getEquation().getIntersectionsWithLine((LineSegment) this).isEmpty();
        }else if(segment2.getType().equals(SegmentType.LINE)){
            return !((CurveSegment) this).getEquation().getIntersectionsWithLine((LineSegment) segment2).isEmpty();
        }
        else {
            return !BezierCurve.getIntersections(((CurveSegment) this).getEquation(), ((CurveSegment) segment2).getEquation()).isEmpty();
        }

    }
    public ArrayList<Point2D> getIntersectionsWith(Segment segment2) {
        ArrayList<Point2D> intersections =new ArrayList<>();
        if(this.getType().equals(SegmentType.LINE)&&segment2.getType().equals(SegmentType.LINE)){
            if(BindsCheck.linesIntersect((LineSegment) this,(LineSegment) segment2)) {
                intersections.add(((LineSegment) this).getEquation().intersection(((LineSegment) segment2).getEquation()));

            }
        }
        else if(this.getType().equals(SegmentType.LINE)){
            intersections=((CurveSegment) segment2).getEquation().getIntersectionsWithLine((LineSegment) this);

        }else if(segment2.getType().equals(SegmentType.LINE)){
            intersections=((CurveSegment) this).getEquation().getIntersectionsWithLine((LineSegment) segment2);
        }
        else {
            intersections=BezierCurve.getIntersections(((CurveSegment) this).getEquation(), ((CurveSegment) segment2).getEquation());
        }
        return intersections;
    }
    public abstract ArrayList<Segment> splitAtPoint(Point2D point);
}
