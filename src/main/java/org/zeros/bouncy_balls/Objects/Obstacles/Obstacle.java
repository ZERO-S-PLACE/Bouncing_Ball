package org.zeros.bouncy_balls.Objects.Obstacles;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Calculations.LinearEquation;

import java.util.ArrayList;

public class Obstacle {

    protected Path path=new Path();

    protected Point2D roughMin;
    protected Point2D roughMax;
    protected ArrayList<Point2D> pointsList=new ArrayList<>();
    protected ArrayList<LinearEquation> linesList=new ArrayList<>();
    protected ArrayList<Boolean> isStraightSegment=new ArrayList<>();

    public ArrayList<LinearEquation> getLinesList() {
        return linesList;
    }

    public Path getPath() {
        return path;
    }
    public ArrayList<Point2D> getPointsList() {
        return pointsList;
    }
    public ArrayList<Boolean> getStraightSegmentList() {
        return isStraightSegment;
    }

    protected Obstacle() {
        path.setFill(Color.BEIGE);
        path.setStroke(Color.BEIGE);
        path.setStrokeWidth(0);
    }

    public Point2D getRoughMin() {
        return roughMin;
    }

    public Point2D getRoughMax() {
        return roughMax;
    }

    protected void updateRoughBinds(){
        roughMin=pointsList.getFirst();
        roughMax=pointsList.getFirst();

        for (Point2D point:pointsList){
            if(point.getX()<roughMin.getX()){
                roughMin=new Point2D(point.getX(),roughMin.getY());
            }
            if(point.getY()<roughMin.getY()){
                roughMin=new Point2D(roughMin.getX(),point.getY());
            }
            if(point.getX()>roughMax.getX()){
                roughMax=new Point2D(point.getX(),roughMax.getY());
            }
            if(point.getY()>roughMax.getY()){
                roughMax=new Point2D(roughMax.getX(),point.getY());
            }
        }
    }

}
