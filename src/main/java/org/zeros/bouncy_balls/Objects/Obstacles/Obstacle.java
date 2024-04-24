package org.zeros.bouncy_balls.Objects.Obstacles;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.zeros.bouncy_balls.Calculations.LinearEquation;

import java.util.ArrayList;

public class Obstacle {

    protected Path path=new Path();
    protected Point2D roughMin;
    protected Point2D roughMax;
    protected ArrayList<Point2D> cornersList =new ArrayList<>();
    protected ArrayList<Point2D> controlPointsList =new ArrayList<>();
    protected ArrayList<LinearEquation> cornerConnectingLinesList =new ArrayList<>();
    protected ArrayList<LinearEquation> simplifiedCurvesLinesList =new ArrayList<>();
    protected ArrayList<Integer> controlPointsTotalCount =new ArrayList<>();
    protected ArrayList<Integer> curvedSegmentsTotalCount =new ArrayList<>();
    public ArrayList<LinearEquation> getCornerLines() {
        return cornerConnectingLinesList;
    }
    public ArrayList<LinearEquation> getCurvesLines() {
        return simplifiedCurvesLinesList;
    }

    public Point2D getRoughMin() {
        return roughMin;
    }

    public Point2D getRoughMax() {
        return roughMax;
    }

    public ArrayList<Point2D> getControlPoints() {
        return controlPointsList;
    }
    public Path getPath() {
        return path;
    }
    public ArrayList<Point2D> getCorners() {
        return cornersList;
    }
    public int controlPointsTotalCount(int segment) {
        return controlPointsTotalCount.get(segment);
    }

    protected Obstacle() {
        path.setFill(Color.BEIGE);
        path.setStroke(Color.BEIGE);
        path.setStrokeWidth(0);
    }
    protected void addStartPoint(Point2D point) {
        path.getElements().add(new MoveTo(point.getX(),point.getY()));
        cornersList.add(point);
    }
    protected void addStraightLineTo(Point2D point) {
        path.getElements().add(new LineTo(point.getX(), point.getY()));
        updateControlPointsCount(0);
        cornerConnectingLinesList.add(new LinearEquation(cornersList.getLast(),point));
        cornersList.add(point);
    }



    protected void addQuadCurveTo(Point2D controlPoint,Point2D point) {
        //path.getElements().add(new QuadCurveTo(controlPoint.getX(), controlPoint.getY(),point.getX(),point.getY()));
        path.getElements().add(new LineTo(controlPoint.getX(), controlPoint.getY()));
        path.getElements().add(new LineTo(point.getX(), point.getY()));

        controlPointsList.add(controlPoint);
        updateControlPointsCount(1);
        cornerConnectingLinesList.add(new LinearEquation(cornersList.getLast(),point));
        simplifiedCurvesLinesList.add(new LinearEquation(cornersList.getLast(),controlPoint));
        simplifiedCurvesLinesList.add(new LinearEquation(controlPoint,point));
        cornersList.add(point);
    }
    protected void addCubicCurveTo(Point2D controlPoint1,Point2D controlPoint2,Point2D point) {
       // path.getElements().add(new CubicCurveTo(controlPoint1.getX(), controlPoint1.getY(),
       //         controlPoint2.getX(), controlPoint2.getY(),point.getX(),point.getY()));
        path.getElements().add(new LineTo(controlPoint1.getX(),controlPoint1.getY()));
        path.getElements().add(new LineTo(controlPoint2.getX(),controlPoint2.getY()));
        path.getElements().add(new LineTo(point.getX(),point.getY()));

        controlPointsList.add(controlPoint1);
        controlPointsList.add(controlPoint2);
        updateControlPointsCount(2);
        cornerConnectingLinesList.add(new LinearEquation(cornersList.getLast(),point));

        simplifiedCurvesLinesList.add(new LinearEquation(cornersList.getLast(),controlPoint1));
        simplifiedCurvesLinesList.add(new LinearEquation(controlPoint1,controlPoint2));
        simplifiedCurvesLinesList.add(new LinearEquation(controlPoint2,point));
        cornersList.add(point);
        
    }
    
    protected void calculateRoughBinds(){
        roughMin=new Point2D(Double.MAX_VALUE,Double.MAX_VALUE);
        roughMax=new Point2D(-Double.MAX_VALUE,-Double.MAX_VALUE);
        searchForExtremeValues(controlPointsList);
        searchForExtremeValues(cornersList);

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
            controlPointsTotalCount.add(0);
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

}
