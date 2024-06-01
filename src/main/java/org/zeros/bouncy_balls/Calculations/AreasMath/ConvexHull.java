package org.zeros.bouncy_balls.Calculations.AreasMath;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.LineSegment;

import java.util.ArrayList;
import java.util.Collections;

public class ConvexHull {
    public static ArrayList<Point2D> calculatePoints(ArrayList<Point2D> inputPoints){
        //calculating convex hull of set of points by Graham scan
        ArrayList<Point2D>points = new ArrayList<>(inputPoints);
        ArrayList<Point2D> hullPoints=new ArrayList<>();

        Point2D lowestPoint = calculateLowestPoint(points);
        points.remove(lowestPoint);
        ArrayList<Point2D> sortedPoints=sortByAngle(points, lowestPoint);
        sortedPoints.add(lowestPoint);
        hullPoints.add(lowestPoint);
        hullPoints.add(sortedPoints.getFirst());

            for (int i=1;i<sortedPoints.size();i++){
                hullPoints.add(sortedPoints.get(i));
                Point2D firstVector=hullPoints.getLast().subtract(hullPoints.get(hullPoints.size()-3));
                Point2D secondVector=hullPoints.get(hullPoints.size()-2).subtract(hullPoints.get(hullPoints.size()-3));
                while (secondVector.crossProduct(firstVector).getZ()<0&&hullPoints.size()>3){
                    hullPoints.remove(hullPoints.size()-2);
                    firstVector=hullPoints.getLast().subtract(hullPoints.get(hullPoints.size()-3));
                    secondVector=hullPoints.get(hullPoints.size()-2).subtract(hullPoints.get(hullPoints.size()-3));
                }
            }

     return hullPoints;
    }
    public static ArrayList<LineSegment> calculateLines(ArrayList<Point2D> inputPoints){
        ArrayList<Point2D>points =calculatePoints(inputPoints);
        ArrayList<LineSegment> lineSegments =new ArrayList<>();
        for (int i=1;i<points.size();i++){
            lineSegments.add(new LineSegment(points.get(i-1),points.get(i)));
        }
        return lineSegments;
    }



    private static Point2D calculateLowestPoint(ArrayList<Point2D> points) {
        ArrayList<Point2D>lowestPoints=new ArrayList<>();
        lowestPoints.add(points.getFirst());
        for (int i=1; i<points.size();i++){
            Point2D point=points.get(i);
            if(lowestPoints.getFirst().getY()>point.getY()){
                lowestPoints=new ArrayList<>();
                lowestPoints.add(point);
            }
            if(lowestPoints.getFirst().getY()==point.getY()){
                lowestPoints.add(point);
            }
        }
        if(lowestPoints.size()<2){return lowestPoints.getFirst();}
        Point2D lowestPoint=lowestPoints.getFirst();
        for (int i=1; i<lowestPoints.size();i++){
            Point2D point=lowestPoints.get(i);
            if(lowestPoint.getX()>point.getX()){
                lowestPoint=point;
            }
            if(lowestPoint.getX()==point.getX()){
                points.remove(point);
            }
        }
        return lowestPoint;
    }

    private static ArrayList<Point2D> sortByAngle(ArrayList<Point2D> points, Point2D referencePoint) {
        ArrayList<Double>cosines=new ArrayList<>();
        ArrayList<Point2D>sortedPoints=new ArrayList<>();

        for (Point2D point:points){
            Point2D vector=point.subtract(referencePoint);
            cosines.add(vector.getX()/vector.magnitude());
        }

        while (!points.isEmpty()){

            ArrayList<Point2D>highestCosPoints=new ArrayList<>();
            highestCosPoints.add(points.getFirst());
            double highestCos=cosines.getFirst();

            for (int i=1; i<points.size();i++){
                double cos=cosines.get(i);
                if(highestCos<cos){
                   highestCosPoints=new ArrayList<>();
                    highestCosPoints.add(points.get(i));
                    highestCos=cos;
                }
                if(highestCos==cos){
                  highestCosPoints.add(points.get(i));
                }
            }
            Point2D furthestPoint=highestCosPoints.getFirst();
            if(highestCosPoints.size()>1){
                double furthestDistance=furthestPoint.distance(referencePoint);

                for (int i=1; i<highestCosPoints.size();i++){
                    Point2D point=highestCosPoints.get(i);
                    double distance=point.distance(referencePoint);
                    if(distance>furthestDistance){
                        furthestDistance=distance;
                        furthestPoint=point;
                    }
                }
            }
            cosines.removeAll(Collections.singleton(highestCos));
            sortedPoints.add(furthestPoint);
            points.removeAll(highestCosPoints);
        }
//        for (Point2D point:sortedPoints){
////            Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren()
////                    .add(new Line(point.getX(),point.getY()
////                            ,referencePoint.getX(),referencePoint.getY())));
//
////            try {
////                Thread.sleep(900);
////            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
////            }
//
//        }
        return sortedPoints;


    }

    public static boolean hullsIntersects(ArrayList<LineSegment> hull1LineSegments, ArrayList<LineSegment> hull2LineSegments){
        for (LineSegment lineSegment1 : hull1LineSegments){
            for (LineSegment lineSegment2 : hull2LineSegments){
                Point2D intersection= lineSegment1.getEquation().intersection(lineSegment2.getEquation());
                if(intersection!=null){
                    if(BindsCheck.linesIntersect(lineSegment1, lineSegment2)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hullIntersectsWithLine(ArrayList<LineSegment> convexHull, LineSegment line) {
        return AreasMath.isInsideArea( convexHull,line.getPoint1() )|| AreasMath.isInsideArea( convexHull,line.getPoint2());
    }


}
