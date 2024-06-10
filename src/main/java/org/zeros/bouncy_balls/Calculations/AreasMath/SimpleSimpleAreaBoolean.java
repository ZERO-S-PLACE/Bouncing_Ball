package org.zeros.bouncy_balls.Calculations.AreasMath;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleSimpleAreaBoolean extends AreaBoolean {

    private  Area areaA;
    private  Area areaB;
    private final CopyOnWriteArrayList<Area> subAreas=new CopyOnWriteArrayList<>();

    public SimpleSimpleAreaBoolean(Area areaA, Area areaB){
        this.areaA=areaA.clone();
        this.areaB=areaB.clone();
        try {
            subAreas.addAll(areaSplit(areaA,areaB));
        }catch (IllegalArgumentException e){

            subAreas.add(areaA);
            subAreas.add(areaB);
        }
    }
    public  ComplexArea sum() {

        ArrayList<Area> areasToInclude=new ArrayList<>();
        ArrayList<Area> areasToExclude=new ArrayList<>();
        if(!AreasMath.areasIntersect(areaA,areaB)){
            areasToInclude.add(areaA);
            areasToInclude.add(areaB);

        }
        else if(AreasMath.isInsideArea(areaA,areaB)){
            areasToInclude.add(areaA);
        }else if(AreasMath.isInsideArea(areaB,areaA)){
            areasToInclude.add(areaB);
        }else {
            for (Area subArea : subAreas) {
                boolean inA=AreasMath.isInsideArea(areaA, subArea.getPointInside());
                boolean inB=AreasMath.isInsideArea(areaB, subArea.getPointInside());
                if ( inA|| inB) {
                    areasToInclude.add(subArea);
                }else {
                    areasToExclude.add(subArea);
                }
            }

            simplifyConnectedAreas(areasToInclude);
            areasToInclude.removeIf(area->AreasMath.containsArea(area,areasToExclude));
            if(areasToInclude.size()>1&&AreasMath.areasIntersect(areasToInclude.getFirst(),areasToInclude.getLast())){
                for (Area area:areasToInclude) {
                    area.getPath().setFill(Color.RED);
                    area.getPath().setStrokeWidth(2);
                    area.getPath().setStroke(Color.BLACK);
                    Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(
                            area.getPath()
                    ));
                }

                throw new IllegalArgumentException("Areas simplified not properly");}



        }
        return new ComplexArea(areasToInclude,areasToExclude);
    }

   /* private void addPreview(ArrayList<Area> areasToInclude) {
        for (Area area:areasToInclude){
            area.getPath().setFill(Color.ALICEBLUE);
            area.getPath().setStrokeWidth(3);
            if(!Model.getInstance()
                    .getLevelCreatorController().preview.getChildren().contains(area.getPath())) {
                Platform.runLater(() -> Model.getInstance()
                        .getLevelCreatorController().preview.getChildren().add(area.getPath()));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }*/


    public ComplexArea intersection() {
        ArrayList<Area> areasToInclude=new ArrayList<>();
        ArrayList<Area> areasToExclude=new ArrayList<>();
        if(AreasMath.areasIntersect(areaA,areaB)) {
            if (AreasMath.isInsideArea(areaA, areaB)) {
                areasToInclude.add(areaB);
            } else if (AreasMath.isInsideArea(areaB, areaA)) {
                areasToInclude.add(areaA);
            } else {
                for (Area subArea : subAreas) {
                    boolean inA = AreasMath.isInsideArea(areaA, subArea.getPointInside());
                    boolean inB = AreasMath.isInsideArea(areaB, subArea.getPointInside());
                    if (inA && inB) {
                        areasToInclude.add(subArea);
                    }
                }

            }
        }

        return new ComplexArea(areasToInclude,areasToExclude);
    }
    public  ComplexArea subtractBFromA() {
        ArrayList<Area> areasToInclude=new ArrayList<>();
        ArrayList<Area> areasToExclude=new ArrayList<>();
        if(!AreasMath.areasIntersect(areaA,areaB)){
            areasToInclude.add(areaA);

        }
        else if(AreasMath.isInsideArea(areaA,areaB)){
            areasToInclude.add(areaA);
            areasToExclude.add(areaB);
        }else if(!AreasMath.isInsideArea(areaB,areaA)) {
            for (Area subArea : subAreas) {
                boolean inA = AreasMath.isInsideArea(areaA, subArea.getPointInside());
                boolean inB = AreasMath.isInsideArea(areaB, subArea.getPointInside());
                if (inA && !inB) {
                    areasToInclude.add(subArea);
                }
            }

        }
        return new ComplexArea(areasToInclude,areasToExclude);
    }
    public  ComplexArea subtractAFromB() {
        reverseAreasOrder();
        ComplexArea difference=subtractBFromA();
        reverseAreasOrder();
        return difference;
    }

    private void reverseAreasOrder() {
        Area temp=areaB;
        areaB=areaA;
        areaA=temp;
    }


    private void simplifyConnectedAreas(ArrayList<Area> tanAreas)throws IllegalArgumentException {
        System.out.println("Areas before simplify: "+tanAreas.size());

        for (Area subArea1:tanAreas){
            for (Area subArea2:tanAreas){
                if(!subArea1.equals(subArea2)){
                    if(haveCommonEdge(subArea1,subArea2)){
                        ArrayList<Area> temp=new ArrayList<>();
                        temp.add(subArea1);
                        temp.add(subArea2);
                        ArrayList<Area> combined=combineTangentAreas(temp);
                        if(combined.isEmpty())throw new IllegalArgumentException("Error combining areas");
                        tanAreas.remove(subArea1);
                        tanAreas.remove(subArea2);
                        tanAreas.addAll(combined);
                        simplifyConnectedAreas(tanAreas);
                        return;
                    }
                }

            }
        }

        System.out.println("Areas after simplify: "+tanAreas.size());

    }
    private ArrayList<Point2D> intersections = new ArrayList<>();
    public  ArrayList<Area> areaSplit(Area area1, Area area2) throws IllegalArgumentException {
        //My algorithm.
        //Areas should be not self intersecting to perform this operation
        //Finds subAreas of two areas intersections. Set also contains inner areas
        // which didn't belong to any of areas before intersections.
        ArrayList<Area> subAreas = new ArrayList<>();

        ArrayList<Segment> allSegments = getSegments(area1, area2);

        if (intersections.isEmpty()) {
            subAreas.add(area1);
            subAreas.add(area2);
            return subAreas;
        }




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

    private ArrayList<Segment> getSegments(Area area1, Area area2) {
        ArrayList<Segment> area1segments = new ArrayList<>(area1.getSegments());
        area1segments.replaceAll(Segment::clone);
        ArrayList<Segment> area2segments = new ArrayList<>(area2.getSegments());
        area2segments.replaceAll(Segment::clone);

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
        ArrayList<Segment> allSegments = area1segments;
        allSegments.addAll(area2segments);
        return allSegments;
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

    private static Area combineAreaFromSegments(ArrayList<Segment> segmentsToCombine) throws IllegalArgumentException {
        ArrayList<Segment>areaSegments=new ArrayList<>();
        areaSegments.add(segmentsToCombine.getFirst());
        segmentsToCombine.removeFirst();
        Point2D firstPoint=areaSegments.getFirst().getPoints().getFirst();
        Point2D lastPoint=areaSegments.getFirst().getPoints().getLast();
        while (lastPoint.distance(firstPoint)>=Properties.ACCURACY()*2){
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
/* private ArrayList<Point2D> intersections = new ArrayList<>();
    public  ArrayList<Area> areaSplit(Area area1, Area area2) throws IllegalArgumentException {
        intersections=new ArrayList<>();
        //My algorithm.
        //Areas should be not self intersecting to perform this operation
        //Finds subAreas of two areas intersections. Set also contains inner areas
        // which didn't belong to any of areas before intersections.
        ArrayList<Area> subAreas = new ArrayList<>();
        ArrayList<Segment> area1Segments=area1.getSegments();
        area1Segments.replaceAll(Segment::clone);
        ArrayList<Segment> area2Segments=area1.getSegments();
        area2Segments.replaceAll(Segment::clone);

        ArrayList<Segment> allSegments = getSubSegmentsAndIntersections(area1Segments, area2Segments);

        if (intersections.isEmpty()) {
            subAreas.add(area1);
            subAreas.add(area2);
            return subAreas;
        }




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


    private ArrayList<Segment> getSubSegmentsAndIntersections(ArrayList<Segment> area1segments, ArrayList<Segment> area2segments) {
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
                            return getSubSegmentsAndIntersections(area1segments,area2segments);

                        }

                    }


                }
            }
        area1segments.addAll(area2segments);
        return area1segments;
    }
*/