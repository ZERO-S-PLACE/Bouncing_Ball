package org.zeros.bouncy_balls.Objects.Area;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.Segment;

import java.util.ArrayList;

public class PolylineArea extends Area {
    boolean editable = false;

    public PolylineArea(Point2D start) {
        super();
        startDrawingFromPoint(start);
    }
    public PolylineArea(ArrayList<Segment> segments) {
        super();
        checkSegmentsContinuity(segments);
        createAreaFromSegments(segments);
    }

    private void createAreaFromSegments(ArrayList<Segment> segments) {
        if(!segments.getFirst().getPoints().getFirst().equals(segments.getLast().getPoints().getLast())||segments.size()<3){
            throw new IllegalArgumentException("Segments path is not closed");
        }
        for (int i=1;i<segments.size();i++){
            if(segments.get(i-1).getPoints().getLast().equals(segments.get(i).getPoints().getFirst())){
                throw new IllegalArgumentException("Segments path is not closed");
            }
        }
    }

    private void checkSegmentsContinuity(ArrayList<Segment> segments) {
        startDrawingFromPoint(segments.getFirst().getPoints().getFirst());
        for(Segment segment:segments){
            ArrayList<Point2D> points =segment.getPoints();
            switch (points.size()){
                case 1->throw new IllegalArgumentException("Not a segment -1 Point");
                case 2->addStraightLineTo(points.getLast());
                case 3->addQuadCurveTo(points.get(1),points.getLast());
                case 4->addCubicCurveTo(points.get(1),points.get(2),points.getLast());
                default -> throw new IllegalArgumentException("Curve degree not supported");
            }
        }
        closeAndSave();

    }

    private void startDrawingFromPoint(Point2D point) {
        editable = true;
        path = new Path();
        cornerPoints = new ArrayList<>();
        segmentPoints = new ArrayList<>();
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
        calculateRoughMassCenter();
    }

    public void removeLastSegment() {
        if (editable && cornerPoints.size() > 1) {
            path.getElements().removeLast();
            segmentPoints.removeLast();
            cornerPoints.removeLast();
            segments.removeLast();
        }

    }

}
