package org.zeros.bouncy_balls.Objects.Obstacles;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

import java.util.ArrayList;

public class PolylineObstacle extends Obstacle {
    boolean editable = false;

    public PolylineObstacle(Point2D start) {

        super();
        startDrawingFromPoint(start);
    }
    private void startDrawingFromPoint(Point2D point) {
        editable = true;
        path = new Path();
        cornerPoints = new ArrayList<>();
        segmentPoints = new ArrayList<>();
        controlPointsTotalCount = new ArrayList<>();
        curvedSegmentsTotalCount = new ArrayList<>();
        path.setFill(Color.WHITE);
        path.setStroke(Color.WHITE);
        path.setStrokeWidth(1);
        addStartPoint(point);
    }

    public void drawStraightSegmentTo(Point2D point) {
        if (editable) {
            addStraightLineTo(point);
        }
    }

    public void drawQuadCurveTo(Point2D control, Point2D point) {
        if (editable) {
            addQuadCurveTo(control, point);
        }
    }

    public void drawCubicCurveTo(Point2D control1, Point2D control2, Point2D point) {
        if (editable) {
            addCubicCurveTo(control1, control2, point);
        }
    }

    public void closeAndSave() {
        if (!cornerPoints.getFirst().equals(cornerPoints.getLast())) {
            addStraightLineTo(cornerPoints.getFirst());
        }
        editable = false;
        calculateBoundaryLines();
        calculateRoughBinds();
    }
    public void removeLastSegment(){
        if (editable&&cornerPoints.size()>1) {
            path.getElements().removeLast();
            segmentPoints.removeLast();
            cornerPoints.removeLast();
        }

    }

}
