package org.zeros.bouncy_balls.Objects.Obstacles;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Calculations.VectorMath;

import java.util.ArrayList;

public class Obstacle {

    protected Path path=new Path();
    protected Point2D roughMin;
    protected Point2D roughMax;
    protected ArrayList<Point2D> cornerPoints =new ArrayList<>();
    protected ArrayList<ArrayList<Point2D>> segmentPoints =new ArrayList<>();
    protected ArrayList<LinearEquation> cornerLines =new ArrayList<>();
    protected ArrayList<ArrayList<LinearEquation>> segmentLines =new ArrayList<>();
    protected ArrayList<Integer> controlPointsTotalCount =new ArrayList<>();
    protected ArrayList<Integer> curvedSegmentsTotalCount =new ArrayList<>();
    public ArrayList<LinearEquation> getCornerLines() {
        return cornerLines;
    }
    public ArrayList<LinearEquation> getSegmentLines(int segment) {
        return segmentLines.get(segment);
    }

    public Point2D getRoughMin() {
        return roughMin;
    }

    public Point2D getRoughMax() {
        return roughMax;
    }

    public ArrayList<Point2D> getSegmentPoints(int segment) {
        return segmentPoints.get(segment);
    }
    public Path getPath() {
        return path;
    }
    public ArrayList<Point2D> getCorners() {
        return cornerPoints;
    }
    public int controlPointsTotalCount(int segment) {
        return controlPointsTotalCount.get(segment);
    }
    public ArrayList<Point2D> getAllPoints() {
        ArrayList<Point2D> points=new ArrayList<>();
        points.add(segmentPoints.getFirst().getFirst());
        for (ArrayList<Point2D> iSegmentPoints:segmentPoints)
        {
            for (int i=1;i<iSegmentPoints.size();i++){
                points.add(iSegmentPoints.get(i));
            }
        }
        return points;
    }
    public ArrayList<LinearEquation> getAllLines() {
        ArrayList<LinearEquation> lines=new ArrayList<>();
        for (ArrayList<LinearEquation> iSegmentLines:segmentLines)
        {
            lines.addAll(iSegmentLines);
        }
        return lines;
    }

    protected Obstacle() {
        path.setFill(Color.BEIGE);
        path.setStroke(Color.BEIGE);
        path.setStrokeWidth(0);
    }
    protected void addStartPoint(Point2D point) {
        path.getElements().add(new MoveTo(point.getX(),point.getY()));
        cornerPoints.add(point);
    }
    protected void addStraightLineTo(Point2D point) {
        path.getElements().add(new LineTo(point.getX(), point.getY()));

        updateControlPointsCount(0);
        ArrayList<Point2D>temp=new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);
    }



    protected void addQuadCurveTo(Point2D controlPoint,Point2D point) {
        path.getElements().add(new QuadCurveTo(controlPoint.getX(), controlPoint.getY(),point.getX(),point.getY()));
        //path.getElements().add(new LineTo(controlPoint.getX(), controlPoint.getY()));
        //path.getElements().add(new LineTo(point.getX(), point.getY()));


        updateControlPointsCount(1);

        ArrayList<Point2D>temp=new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(controlPoint);
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);
    }
    protected void addCubicCurveTo(Point2D controlPoint1,Point2D controlPoint2,Point2D point) {
        path.getElements().add(new CubicCurveTo(controlPoint1.getX(), controlPoint1.getY(),
              controlPoint2.getX(), controlPoint2.getY(),point.getX(),point.getY()));
        //path.getElements().add(new LineTo(controlPoint1.getX(),controlPoint1.getY()));
        //path.getElements().add(new LineTo(controlPoint2.getX(),controlPoint2.getY()));
        //path.getElements().add(new LineTo(point.getX(),point.getY()));

        updateControlPointsCount(2);
        ArrayList<Point2D>temp=new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(controlPoint1);
        temp.add(controlPoint2);
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);
        
    }
    
    protected void calculateRoughBinds(){
        roughMin=new Point2D(Double.MAX_VALUE,Double.MAX_VALUE);
        roughMax=new Point2D(-Double.MAX_VALUE,-Double.MAX_VALUE);
        for(ArrayList<Point2D> points :segmentPoints) {
            searchForExtremeValues(points);
        }
        searchForExtremeValues(cornerPoints);

    }

    private void searchForExtremeValues(ArrayList<Point2D> pointsList) {
        for(Point2D point:pointsList){
            if(point.getX()<roughMin.getX()){
                roughMin=new Point2D(point.getX(),roughMin.getY());
            }
            if(point.getX()>roughMax.getX()){
                roughMax=new Point2D(point.getX(),roughMax.getY());
            }
            if(point.getY()<roughMin.getY()){
                roughMin=new Point2D(roughMin.getX(),point.getY());
            }
            if(point.getY()>roughMax.getY()){
                roughMax=new Point2D(roughMax.getX(),point.getY());
            }

        }
    }

    public int getCurvedSegmentsCount(int segment) {
        return curvedSegmentsTotalCount.get(segment);
    }

    private void updateControlPointsCount(int increase) {
        int curvedSegment=0;
        if(increase>0){
            curvedSegment=1;
        }
        if(controlPointsTotalCount.isEmpty()){
            controlPointsTotalCount.add(increase);
            curvedSegmentsTotalCount.add(curvedSegment);
        }
        else {
            controlPointsTotalCount.add(controlPointsTotalCount.getLast()+increase);
            curvedSegmentsTotalCount.add(curvedSegmentsTotalCount.getLast()+curvedSegment);
        }
    }
    public int controlPointsInSegment(int segment){

        if(segment>0) {
            return controlPointsTotalCount.get(segment) - controlPointsTotalCount.get(segment - 1);
        }
        return controlPointsTotalCount.get(segment);
    }
    public int controlPointsBeforeSegment(int segment){
            return controlPointsTotalCount.get(segment) - controlPointsInSegment(segment);
    }

    protected void rotateObstacle(double rotation, Point2D center) {

        Rotate rotate = new Rotate();
        rotate.setPivotX(center.getX());
        rotate.setPivotY(center.getY());

        path.getTransforms().add(rotate);
        path.rotateProperty().set(-rotation*360/Math.PI/2);

        segmentPoints.replaceAll(points -> VectorMath.rotatePoints(points, center, rotation));
        cornerPoints =VectorMath.rotatePoints(cornerPoints,center,rotation);

    }

    protected void calculateBoundaryLines() {
        cornerLines=new ArrayList<>();
        segmentLines=new ArrayList<>();

        for (int i = 0; i< cornerPoints.size()-1; i++){
            cornerLines.add(new LinearEquation(cornerPoints.get(i),cornerPoints.get(i+1)));
            ArrayList<LinearEquation> temp=new ArrayList<>();
            for (int j=0;j<segmentPoints.get(i).size()-1;j++) {
                temp.add(new LinearEquation(segmentPoints.get(i).get(j),segmentPoints.get(i).get(j+1)));
                }

             segmentLines.add(temp);
        }
    }

}

