package org.zeros.bouncy_balls.Calculations.SegmentIntersection;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.CurveSegment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.LineSegment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.SegmentType;

import java.util.ArrayList;

public abstract class SegmentIntersection {

    ArrayList<Segment> firstSegmentSubsegments=new ArrayList<>();
    ArrayList<Segment> secondSegmentSubsegments=new ArrayList<>();
    ArrayList<Point2D> intersectionPoints=new ArrayList<>();
    protected SegmentIntersection() {}
    public static SegmentIntersection getSegmentIntersection(Segment segment1, Segment segment2) {
        if(segment1.getType().equals(SegmentType.LINE)&&segment2.getType().equals(SegmentType.LINE)){
            return new LineLineIntersection((LineSegment)segment1,(LineSegment)segment2);
        }
        if(segment1.getType().equals(SegmentType.CURVE)&&segment2.getType().equals(SegmentType.CURVE)){
            return new CurveCurveIntersection((CurveSegment)segment1,(CurveSegment)segment2);
        }
        if(segment1.getType().equals(SegmentType.CURVE)&&segment2.getType().equals(SegmentType.LINE)){
            return new LineCurveIntersection((LineSegment)segment2,(CurveSegment)segment1);
        }
        if(segment1.getType().equals(SegmentType.LINE)&&segment2.getType().equals(SegmentType.CURVE)){
            return new LineCurveIntersection((LineSegment) segment1,(CurveSegment)segment2);
        }
        throw new IllegalArgumentException("Given segment is not valid for this program");
    }

    public  ArrayList<Point2D> getIntersectionPoints(){
        return intersectionPoints;
    }

    public  ArrayList<Segment> getSubsegmentsOfFirstSegment(){
        return firstSegmentSubsegments;
    }


    public  ArrayList<Segment> getSubsegmentsOfSecondSegment(){
        return secondSegmentSubsegments;
    }
}
