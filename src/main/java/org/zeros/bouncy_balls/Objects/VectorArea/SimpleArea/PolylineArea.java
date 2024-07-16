package org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.Segment;

import java.util.ArrayList;

public class PolylineArea extends Area {
    boolean editable = false;

    public PolylineArea(Point2D start) {
        super();
        startDrawingFromPoint(start);
    }

    public PolylineArea(ArrayList<Segment> segments) {
        super();

        for (Segment segment:segments){
            this.segments.add(segment.clone());
        }
        checkSegmentsContinuityAndMatchPoints();
        createAreaFromSegments();
    }

    private void checkSegmentsContinuityAndMatchPoints() {
        matchFirstSegments(segments.getFirst(), segments.get(1));
        for (int i = 2; i < segments.size(); i++) {
            matchSegments(segments.get(i - 1), segments.get(i));
        }
    }

    private void matchFirstSegments(Segment last, Segment first) {
        if (last.getPoints().getLast().distance(first.getPoints().getFirst())<= Properties.ACCURACY()) {
            return;
        }
        if (last.getPoints().getLast().distance(first.getPoints().getLast()) <= Properties.ACCURACY()) {
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
        else  {
            System.out.println(segments.indexOf(previous)+" "+segments.indexOf(next)+" "+segments.size());
            System.out.println(previous.getPoints().getFirst()+" "+previous.getPoints().getLast());
            System.out.println(next.getPoints().getFirst()+" "+next.getPoints().getLast());
            throw new IllegalArgumentException("Points doesn't match");
        }

    }

    private void createAreaFromSegments() {
        ArrayList<Segment>segments =new ArrayList<>(this.segments);
        startDrawingFromPoint(segments.getFirst().getPoints().getFirst());
        for (Segment segment : segments) {
            addSegment(segment.getPoints());
        }
        closeAndSave();

    }

    private void addSegment (ArrayList<Point2D> points ){
        switch (points.size()) {
            case 0, 1 -> throw new IllegalArgumentException("Not a segment ");
            case 2 -> addStraightLineTo(points.getLast());
            case 3 -> addQuadCurveTo(points.get(1), points.getLast());
            case 4 -> addCubicCurveTo(points.get(1), points.get(2), points.getLast());
            default -> throw new IllegalArgumentException("Curve degree not supported");
        }
    }

    private void startDrawingFromPoint(Point2D point) {
        editable = true;
        path = new Path();
        cornerPoints = new ArrayList<>();
        segmentPoints = new ArrayList<>();
        segments=new ArrayList<>();
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
        if(editable) {
            if (!cornerPoints.getLast().equals(cornerPoints.getFirst())) {
                if (cornerPoints.getLast().distance(cornerPoints.getFirst()) <= Properties.ACCURACY()) {
                    ArrayList<Point2D> points = segments.getLast().getPoints();
                    points.removeLast();
                    points.add(cornerPoints.getFirst());
                    removeLastSegment();
                    addSegment(points);
                } else {
                    addStraightLineTo(cornerPoints.getFirst());
                }
            }
            editable = false;
            calculateBoundaryLines();
            calculateRoughBinds();
            calculateRoughMassCenter();
        }
    }

    public void removeLastSegment() {
        if (editable && cornerPoints.size() > 1) {
            path.getElements().removeLast();
            segmentPoints.removeLast();
            cornerPoints.removeLast();
            segments.removeLast();
        }

    }
    public static boolean isSelfIntersecting(PolylineArea plObst) {

        if (!plObst.getSegments().isEmpty()) {
            for (Segment segment1 : plObst.getSegments()) {
                for (Segment segment2 : plObst.getSegments()) {
                    if (!segment1.equals(segment2)) {
                        ArrayList<Point2D> intersections = segment1.getIntersectionsWith(segment2);
                        for (Point2D intersection : intersections) {
                            if (!(intersection.distance(segment1.getPoints().getFirst()) <= Properties.ACCURACY()
                                    || intersection.distance(segment1.getPoints().getLast()) <= Properties.ACCURACY()
                                    || intersection.distance(segment2.getPoints().getFirst()) <=Properties.ACCURACY()
                                    || intersection.distance(segment2.getPoints().getLast()) <= Properties.ACCURACY())) {
                                return true;
                            }

                        }


                    }
                }
            }
        }
        return false;

    }

}
