package org.zeros.bouncy_balls.Calculations.AreasMath;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.Area.ComplexArea;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.LineSegment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.Segment;

import java.util.ArrayList;

public class AreasMath {
    public static boolean areasIntersect(Area area1, Area area2) {
        for (Point2D point:area1.getCorners()){
            if(isInsideArea(area2,point))return true;
        }
        for (Point2D point:area2.getCorners()){
            if(isInsideArea(area1,point))return true;
        }
        for (Segment segment1: area1.getSegments()){
            for (Segment segment2: area2.getSegments()){
                if(segment1.intersectsWith(segment2))return true;
            }
        }
        return false;
    }
    public static boolean isInsideArea(Area area, Area insideArea) {
        for (Point2D point:insideArea.getCorners()){
            if(!isInsideArea(area,point))return false;
        }
        for (Segment segment1: area.getSegments()){
            for (Segment segment2: insideArea.getSegments()){
                if(segment1.intersectsWith(segment2))return false;
            }
        }
        return true;
    }

    private static boolean isInsideArea( Area area,Point2D point) {
        // calculations using ray tracing algorithm, edges Included
        //What if point is on edge?
        LineSegment ray=new LineSegment(point,new Point2D(Double.MAX_VALUE,Double.MAX_VALUE));
        double intersectionsCount=0;
        for(Segment segment:area.getSegments()){
            ArrayList<Point2D>intersections=segment.getIntersectionsWith(ray);
            for (Point2D intersection:intersections){
                if(intersection.equals(segment.getPoints().getLast())||intersection.equals(segment.getPoints().getFirst())){
                    intersectionsCount=intersectionsCount+0.5;
                }
                else intersectionsCount++;
            }
        }
        return intersectionsCount % 2 != 0;
    }

    public static ComplexArea areaSum(Area area1,Area area2){

        return null;
    }



    public static ComplexArea areaSum(ComplexArea areaC, Area areaS){
        return null;
    }
    public static ComplexArea areaSum(ComplexArea areaC1, ComplexArea areaC2){
        return null;
    }

    public static ArrayList<Area> areaSplit(Area area1,Area area2){
        //My algorithm.
        ArrayList<Point2D> intersections=new ArrayList<>();
        ArrayList<Segment> area1segments = new ArrayList<>(area1.getSegments());
        ArrayList<Segment> area2segments = new ArrayList<>(area2.getSegments());
        boolean newIntersectionsOccurred=true;
        while (newIntersectionsOccurred){
            newIntersectionsOccurred=false;
            out:for (Segment segment1:area1segments){
                for (Segment segment2:area2segments){
                    if(segment1.intersectsWith(segment2)){
                        Point2D intersection=segment1.getIntersectionsWith(segment2).getFirst();
                        intersections.add(intersection);
                        area2segments.addAll(segment1.splitAtPoint(intersection));
                        area2segments.addAll(segment2.splitAtPoint(intersection));
                        area1segments.remove(segment1);
                        area2segments.remove(segment2);
                        newIntersectionsOccurred=true;
                        break out;
                    }
                }
            }
        }
        ArrayList<Segment> allSegments= area1segments;
        allSegments.addAll(area2segments);




return null;
    }

}
