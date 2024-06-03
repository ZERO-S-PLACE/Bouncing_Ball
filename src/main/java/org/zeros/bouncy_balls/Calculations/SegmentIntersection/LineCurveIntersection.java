package org.zeros.bouncy_balls.Calculations.SegmentIntersection;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.CurveSegment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.LineSegment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.Segment;

/*public class LineCurveIntersection extends SegmentIntersection {

    public LineCurveIntersection(LineSegment line, CurveSegment curve) {
        super(line,curve);
        intersectionPoints.addAll(BezierCurve.getIntersections(curve.getEquation(),line));
        intersectionPoints.removeIf(intersection -> VectorMath.containsPoint(intersection, endPoints));
        if(!intersectionPoints.isEmpty()) {
            secondSegmentSubsegments=curve.splitAtPoint(intersectionPoints.getFirst());
            firstSegmentSubsegments=line.splitAtPoint(intersectionPoints.getFirst());

            for (int i=1;i<intersectionPoints.size();i++){
                Point2D intersection=intersectionPoints.get(i);
                Segment segment1t= Segment.getSegmentWithPoint(firstSegmentSubsegments,intersection);
                Segment segment2t=Segment.getSegmentWithPoint(secondSegmentSubsegments,intersection);
                if(segment1t!=null&&segment2t!=null) {
                    firstSegmentSubsegments.remove(segment1t);
                    secondSegmentSubsegments.remove(segment2t);
                    firstSegmentSubsegments.addAll(segment1t.splitAtPoint(intersection));
                    secondSegmentSubsegments.addAll(segment2t.splitAtPoint(intersection));
                }
            }
        }else {
            firstSegmentSubsegments.add(line);
            secondSegmentSubsegments.add(curve);
        }
    }
}
*/