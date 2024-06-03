package org.zeros.bouncy_balls.Calculations.SegmentIntersection;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.CurveSegment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.Segment;

import java.util.ArrayList;

/*public class CurveCurveIntersection extends SegmentIntersection{
    public CurveCurveIntersection(CurveSegment segment1, CurveSegment segment2) {
        super(segment1,segment2);
        intersectionPoints.addAll(segment1.getIntersectionsWith(segment2));
        intersectionPoints.removeIf(intersection -> VectorMath.containsPoint(intersection, endPoints));
        if(!intersectionPoints.isEmpty()) {
            try {
                firstSegmentSubsegments=segment1.splitAtPoint(intersectionPoints.getFirst());
                secondSegmentSubsegments=segment1.splitAtPoint(intersectionPoints.getFirst());
            }


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
            firstSegmentSubsegments.add(segment1);
            secondSegmentSubsegments.add(segment2);
        }
    }
}
*/