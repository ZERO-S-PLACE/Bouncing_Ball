package org.zeros.bouncy_balls.Objects.Area;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import org.zeros.bouncy_balls.Model.Properties;
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
        checkSegmentsContinuityAndMatchPoints(segments);
        createAreaFromSegments(segments);
    }

    private void checkSegmentsContinuityAndMatchPoints(ArrayList<Segment> segments) {
        matchFirstSegments(segments.getLast(), segments.getFirst());
        for (int i = 1; i < segments.size(); i++) {
            matchSegments(segments.get(i - 1), segments.get(i));
        }
    }

    private void matchFirstSegments(Segment last, Segment first) {
        if (last.getPoints().getLast().distance(first.getPoints().getFirst())<= Properties.ACCURACY()) {
            return;
        }
        if ((last.getPoints().getLast().distance(first.getPoints().getLast()) <= Properties.ACCURACY())) {
            first.reversePoints();
        } else if (last.getPoints().getFirst().distance(first.getPoints().getFirst())<= Properties.ACCURACY()) {
            last.reversePoints();
        } else if (last.getPoints().getFirst().distance(first.getPoints().getLast())<= Properties.ACCURACY()) {
            last.reversePoints();
            first.reversePoints();
        }else {
        throw new IllegalArgumentException("Points doesn't match");
        }
    }


    private void matchSegments(Segment previous, Segment next) {
        if (previous.getPoints().getLast().distance(next.getPoints().getFirst())<= Properties.ACCURACY()) return;
        if (previous.getPoints().getLast().distance(next.getPoints().getLast())<= Properties.ACCURACY()) next.reversePoints();
        else {
            throw new IllegalArgumentException("Points doesn't match");
        }

    }

    private void createAreaFromSegments(ArrayList<Segment> segments) {
        startDrawingFromPoint(segments.getFirst().getPoints().getFirst());
        for (Segment segment : segments) {
            ArrayList<Point2D> points = segment.getPoints();
            switch (points.size()) {
                case 0, 1 -> throw new IllegalArgumentException("Not a segment ");
                case 2 -> addStraightLineTo(points.getLast());
                case 3 -> addQuadCurveTo(points.get(1), points.getLast());
                case 4 -> addCubicCurveTo(points.get(1), points.get(2), points.getLast());
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
