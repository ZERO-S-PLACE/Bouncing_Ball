package org.zeros.bouncy_balls.Calculations.AreasMath;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.LineSegment;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.util.ArrayList;

public class AreasMath {
    public static boolean areasIntersect(Area area1, Area area2) {
        for (Point2D point : area1.getCorners()) {
            if (isInsideArea(area2, point)) return true;
        }
        for (Point2D point : area2.getCorners()) {
            if (isInsideArea(area1, point)) return true;
        }
        for (Segment segment1 : area1.getSegments()) {
            for (Segment segment2 : area2.getSegments()) {
                if (!segment1.getIntersectionsWith(segment2).isEmpty()) return true;
            }
        }
        return false;
    }

    public static boolean isInsideArea(Area outerArea, Area insideArea) {
        //does not support tangent Areas
        for (Point2D point : insideArea.getCorners()) {
            if (!isInsideArea(outerArea, point)) return false;
        }
        for (Segment segment1 : outerArea.getSegments()) {
            for (Segment segment2 : insideArea.getSegments()) {
                if (!segment1.getIntersectionsWith(segment2).isEmpty()) return false;
            }
        }
        return true;
    }

    public static boolean isInsideArea(ComplexArea area, Point2D point) {
        for (ComplexAreaPart part : area.partAreas()) {
            if (part.isInside(point)) return true;
        }
        return false;
    }

    public static boolean isInsideArea(ArrayList<? extends Segment> segments, Point2D point) {
        // calculations using ray tracing algorithm, edges included
        LineSegment ray = new LineSegment(point, new Point2D(10 ^ 100, 10 ^ 100));
        double intersectionsCount = 0;
        for (Segment segment : segments) {
            if (segment.isOnBoundary(point)) return true;
            ArrayList<Point2D> intersections = segment.getIntersectionsWith(ray);
            for (Point2D intersection : intersections) {
                if (intersection.distance(segment.getPoints().getLast()) <= Properties.ACCURACY() || intersection.distance(segment.getPoints().getFirst()) <= Properties.ACCURACY()) {
                    intersectionsCount = intersectionsCount + 0.5;
                } else intersectionsCount++;
            }
        }
        return intersectionsCount % 2 != 0;
    }

    public static boolean isInsideArea(Area area, Point2D point) {
        return isInsideArea(area.getSegments(), point);
    }
    public static boolean containsArea(Area area, ArrayList<Area> areas) {
        for (Area area1 : areas) {
            if (area.isEqualTo(area1)) return true;
        }
        return false;
    }
}
