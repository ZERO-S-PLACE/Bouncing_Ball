package org.zeros.bouncy_balls.Objects.Obstacles;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

import java.util.ArrayList;

public class PolylineObstacle extends Obstacle {
    boolean editable=false;

    public PolylineObstacle() {
        super();
    }

    public void startDrawingFromPoint(Point2D point){
        editable=true;
        path=new Path();
        cornerPoints =new ArrayList<>();
        segmentPoints =new ArrayList<>();
        controlPointsTotalCount =new ArrayList<>();
        curvedSegmentsTotalCount =new ArrayList<>();
        path.setFill(Color.BEIGE);
        path.setStroke(Color.BEIGE);
        path.setStrokeWidth(0);
        addStartPoint(point);
    }
    public void drawStraightSegmentTo(Point2D point){
        if(editable){
            addStraightLineTo(point);
        }
    }
    public void drawQuadCurveTo(Point2D control,Point2D point){
        if(editable){
            addQuadCurveTo(control,point);
        }
    }
    public void drawCubicCurveTo(Point2D control1, Point2D control2,Point2D point){
        if(editable){
            addCubicCurveTo(control1,control2,point);
        }
    }
    public void closeAndSave(){
        if(!cornerPoints.getFirst().equals(cornerPoints.getLast())){
            addStraightLineTo(cornerPoints.getFirst());
        }
        editable=false;
        calculateBoundaryLines();
        calculateRoughBinds();
    }

}
