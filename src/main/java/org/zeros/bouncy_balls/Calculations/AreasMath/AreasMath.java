package org.zeros.bouncy_balls.Calculations.AreasMath;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.Area.ComplexArea;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.LineSegment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.Area.PolylineArea;

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

    public static boolean isInsideArea(Area area, Area insideArea) {
        for (Point2D point : insideArea.getCorners()) {
            if (!isInsideArea(area, point)) return false;
        }
        for (Segment segment1 : area.getSegments()) {
            for (Segment segment2 : insideArea.getSegments()) {
                if(!segment1.overlapsWith(segment2)) {
                    if (!segment1.getIntersectionsWith(segment2).isEmpty()) return false;
                }
            }
        }
        return true;
    }

    public static boolean isInsideArea(Area area, Point2D point) {
        // calculations using ray tracing algorithm, edges included

        LineSegment ray = new LineSegment(point, new Point2D(Double.MAX_VALUE, Double.MAX_VALUE));
        double intersectionsCount = 0;
        for (Segment segment : area.getSegments()) {
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

    public static ComplexArea areaSum(Area area1, Area area2) {

        return null;
    }


    public static ComplexArea areaSum(ComplexArea areaC, Area areaS) {
        return null;
    }

    public static ComplexArea areaSum(ComplexArea areaC1, ComplexArea areaC2) {
        return null;
    }

    public static ArrayList<Area> areaSplit(Area area1, Area area2) {
        //My algorithm.
        //Areas should be not self intersecting to perform this operation
        //Finds subAreas of two areas intersections. Set also contains inner areas
        // which didn't belong to any of areas before intersections.
        ArrayList<Point2D> intersections = new ArrayList<>();
        ArrayList<Segment> area1segments = new ArrayList<>(area1.getSegments());
        ArrayList<Segment> area2segments = new ArrayList<>(area2.getSegments());
        ArrayList<Area> subAreas = new ArrayList<>();
        boolean newIntersectionsOccurred = true;
        while (newIntersectionsOccurred) {
            newIntersectionsOccurred = false;
            out:
            for (Segment segment1 : area1segments) {
                for (Segment segment2 : area2segments) {
                    ArrayList<Point2D> tempIntersections = segment1.getIntersectionsWith(segment2);
                    for (Point2D intersection : tempIntersections) {

                        if (!VectorMath.containsPoint(intersection, intersections)) {
                            intersections.add(intersection);
                            area1segments.addAll(segment1.splitAtPoint(intersection));
                            area2segments.addAll(segment2.splitAtPoint(intersection));
                            area1segments.remove(segment1);
                            area2segments.remove(segment2);
                            newIntersectionsOccurred = true;
                            break out;
                        }

                    }
                }
            }
        }
        if (intersections.isEmpty()) {
            subAreas.add(area1);
            subAreas.add(area2);
            return subAreas;
        }

        ArrayList<Segment> allSegments = area1segments;
        allSegments.addAll(area2segments);


        for (Point2D intersection : intersections) {
            ArrayList<Segment> segmentsAtIntersection = findSegmentsConnectedAtPoint(intersection, allSegments);
            for (Segment startSegment : segmentsAtIntersection) {
                ArrayList<Segment> otherSegments = new ArrayList<>(segmentsAtIntersection);
                otherSegments.remove(startSegment);
                ArrayList<Segment> subAreaSegments = new ArrayList<>();
                subAreaSegments.add(startSegment);
                subAreaSegments.add(rightMostSegment(intersection, subAreaSegments.getLast(), otherSegments));

                Point2D nextPoint = subAreaSegments.getLast().getOtherEnd(intersection);
                while (!subAreaSegments.getFirst().equals(subAreaSegments.getLast())) {
                    ArrayList<Segment> segmentsAtNextPoint = findSegmentsConnectedAtPoint(nextPoint, allSegments);
                    segmentsAtNextPoint.remove(subAreaSegments.getLast());
                    subAreaSegments.add(rightMostSegment(nextPoint, subAreaSegments.getLast(), segmentsAtNextPoint));
                    nextPoint = subAreaSegments.getLast().getOtherEnd(nextPoint);
                }
                subAreaSegments.removeLast();

                Area newArea;
                newArea = new PolylineArea(subAreaSegments);
                if (isAtomicArea(newArea, subAreas)) {
                    subAreas.removeIf(area -> AreasMath.isInsideArea(area, newArea.getPointInside()));
                    subAreas.add(newArea);
                }

            }

        }

        return subAreas;
    }

    private static boolean isAtomicArea(Area newArea, ArrayList<Area> subAreas) {
        for (Area area : subAreas) {
            if (AreasMath.isInsideArea(newArea,area.getPointInside())) return false;
        }
        return true;
    }

    private static boolean doesNotContainArea(ArrayList<Area> subAreas, Area newArea) {
        for (Area area : subAreas) {
            if (newArea.isEqualTo(area)) return false;
        }
        return true;
    }

    private static Segment rightMostSegment(Point2D connectionPoint, Segment lastSegment, ArrayList<Segment> segmentsAtNextPoint) {
        if (segmentsAtNextPoint.isEmpty()) throw new IllegalArgumentException("Wrong line subdivision - naked edge");
        if (segmentsAtNextPoint.size() == 1) return segmentsAtNextPoint.getFirst();
        Point2D referenceVector = lastSegment.getTangentVectorPointingEnd(connectionPoint);
        ArrayList<Double> angles = new ArrayList<>();
        for (Segment segment : segmentsAtNextPoint) {
            Point2D vector = segment.getTangentVectorPointingEnd(connectionPoint);
            angles.add(VectorMath.counterClockWiseAngle(referenceVector, vector));
        }
        double smallestAngle = Double.MAX_VALUE;

        for (double angle : angles) {
            if (angle < smallestAngle) smallestAngle = angle;

        }

        return segmentsAtNextPoint.get(angles.indexOf(smallestAngle));


    }


    private static ArrayList<Segment> findSegmentsConnectedAtPoint(Point2D point, ArrayList<Segment> segments) {
        ArrayList<Segment> connectedSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (segment.getPoints().getFirst().distance(point) <= Properties.ACCURACY() || segment.getPoints().getLast().distance(point) <= Properties.ACCURACY()) {
                connectedSegments.add(segment);
            }
        }
        return connectedSegments;

    }

}
