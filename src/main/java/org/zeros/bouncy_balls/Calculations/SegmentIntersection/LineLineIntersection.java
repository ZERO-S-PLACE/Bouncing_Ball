package org.zeros.bouncy_balls.Calculations.SegmentIntersection;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.LineSegment;

public class LineLineIntersection extends SegmentIntersection{
    public LineLineIntersection(LineSegment segment1, LineSegment segment2) {
        super(segment1,segment2);
        Point2D intersection=segment1.getEquation().intersection(segment2.getEquation());
        if(intersection!=null) {
            if (!VectorMath.containsPoint(intersection,endPoints)) {
                if (BindsCheck.isOnLine(intersection, segment1) && BindsCheck.isOnLine(intersection, segment2)) {
                    intersectionPoints.add(intersection);
                    firstSegmentSubsegments.addAll(segment1.splitAtPoint(intersection));
                    secondSegmentSubsegments.addAll(segment2.splitAtPoint(intersection));
                    return;
                }
            }
        }
        firstSegmentSubsegments.add(segment1);
        secondSegmentSubsegments.add(segment2);

    }
}
