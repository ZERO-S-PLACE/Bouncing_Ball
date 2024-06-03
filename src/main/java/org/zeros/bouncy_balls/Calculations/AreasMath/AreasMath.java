package org.zeros.bouncy_balls.Calculations.AreasMath;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Exceptions.WrongValueException;
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
        //does not support tangent Areas
        for (Point2D point : insideArea.getCorners()) {
            if (!isInsideArea(area, point)) return false;
        }
        for (Segment segment1 : area.getSegments()) {
            for (Segment segment2 : insideArea.getSegments()) {
                    if (!segment1.getIntersectionsWith(segment2).isEmpty()) return false;
            }
        }
        return true;
    }

    public static boolean isInsideArea(ArrayList<? extends Segment> segments, Point2D point) {
        // calculations using ray tracing algorithm, edges included
        LineSegment ray = new LineSegment(point, new Point2D(10^100, 10^100));
        double intersectionsCount = 0;
        for (Segment segment : segments) {
            if (segment.isOnBoundary(point)) return true;
            ArrayList<Point2D> intersections = segment.getIntersectionsWith(ray);
            for (Point2D intersection : intersections) {
                if (intersection.distance(segment.getPoints().getLast())<=Properties.ACCURACY() || intersection.distance(segment.getPoints().getFirst())<=Properties.ACCURACY() ) {
                    intersectionsCount = intersectionsCount + 0.5;
                } else intersectionsCount++;
            }
        }
        return intersectionsCount % 2 != 0;
    }
    public static boolean isInsideArea(Area area, Point2D point) {
        return isInsideArea(area.getSegments(),point);
    }

    public static ArrayList<Area> areaSplit(Area area1, Area area2) throws IllegalArgumentException {
        //My algorithm.
        //Areas should be not self intersecting to perform this operation
        //Finds subAreas of two areas intersections. Set also contains inner areas
        // which didn't belong to any of areas before intersections.
        ArrayList<Point2D> intersections = new ArrayList<>();
        ArrayList<Segment> area1segments = new ArrayList<>(area1.getSegments());
        for (Segment segment:area1segments){segment.clone();}
        ArrayList<Segment> area2segments = new ArrayList<>(area2.getSegments());
        for (Segment segment:area2segments){segment.clone();}
        ArrayList<Area> subAreas = new ArrayList<>();
        boolean newIntersectionsOccurred = true;
        while (newIntersectionsOccurred) {
            newIntersectionsOccurred = false;
            out:
            for (Segment segment1 : area1segments) {
                for (Segment segment2 : area2segments) {
                  /*  SegmentIntersection intersection = SegmentIntersection.getSegmentIntersection(segment1,segment2);

                    if(!intersection.getIntersectionPoints().isEmpty()){

                            intersections.addAll(intersection.getIntersectionPoints());
                            area1segments.addAll(intersection.getSubsegmentsOfFirstSegment());
                            area2segments.addAll(intersection.getSubsegmentsOfSecondSegment());
                            area1segments.remove(segment1);
                            area2segments.remove(segment2);
                            newIntersectionsOccurred = true;
                            break out;
                        }*/
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
                Area newArea= new PolylineArea(subAreaSegments);
               if (isAtomicArea(newArea, subAreas)) {
                 subAreas.removeIf(area -> AreasMath.isInsideArea(area, newArea.getPointInside())
                         &&AreasMath.isInsideArea(area,newArea.getCorners().getFirst()));
                 subAreas.add(newArea);
               }
            }

        }
System.out.println("Sub areas count: "+subAreas.size());
        return subAreas;
    }

    private static boolean isAtomicArea(Area newArea, ArrayList<Area> subAreas) {
        for (Area area : subAreas) {
            if (AreasMath.isInsideArea(newArea,area.getPointInside())){
                if (AreasMath.isInsideArea(newArea,area.getCorners().getFirst())) {
                    return false;
                }
            }
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



    public static boolean haveCommonEdge(Area subArea1, Area subArea2) {
        for (Segment segment1:subArea1.getSegments()){
            for (Segment segment2:subArea2.getSegments()){
                if(segment1.isEqualTo(segment2))return true;

            }
        }
        return false;
    }
    public static ArrayList<Area> combineTangentAreas(ArrayList<Area>tanAreas) {
        ArrayList<Segment> segmentsToCombine=getUniqueSegments(tanAreas);
        ArrayList<Area> mergedAreas=new ArrayList<>();
        while (!segmentsToCombine.isEmpty()){
            mergedAreas.add(combineAreaFromSegments(segmentsToCombine));
        }
        return mergedAreas;

        }

    private static Area combineAreaFromSegments(ArrayList<Segment> segmentsToCombine) {
        ArrayList<Segment>areaSegments=new ArrayList<>();
        areaSegments.add(segmentsToCombine.getFirst());
        segmentsToCombine.removeFirst();
        Point2D firstPoint=areaSegments.getFirst().getPoints().getFirst();
        Point2D lastPoint=areaSegments.getFirst().getPoints().getLast();
        while (lastPoint.distance(firstPoint)>=Properties.ACCURACY()){
            ArrayList<Segment> segmentsAtPoint=findSegmentsConnectedAtPoint(lastPoint,segmentsToCombine);
            if(segmentsAtPoint.isEmpty())throw new IllegalArgumentException("Unconnected set");
            if(segmentsAtPoint.size()>1)throw new IllegalArgumentException("Self intersecting set");
            areaSegments.add(segmentsAtPoint.getFirst());
            segmentsToCombine.remove(segmentsAtPoint.getFirst());
            lastPoint=areaSegments.getLast().getOtherEnd(lastPoint);
        }
        return new PolylineArea(areaSegments);
    }

    private static ArrayList<Segment> getUniqueSegments(ArrayList<Area> tanAreas) {
        ArrayList<Segment>uniqueSegments=new ArrayList<>();
        for (Area area:tanAreas){
            for (Segment addSegment:area.getSegments()){
                if(uniqueSegments.isEmpty()){
                    uniqueSegments.add(addSegment);
                }else {
                    boolean wasInList=false;
                    for (Segment segment : uniqueSegments) {
                        if(addSegment.isEqualTo(segment)){
                            wasInList=true;
                            uniqueSegments.remove(segment);
                            break;
                        }
                    }
                    if(!wasInList){
                        uniqueSegments.add(addSegment);
                    }
                }
            }
        }
        return uniqueSegments;
    }
}
