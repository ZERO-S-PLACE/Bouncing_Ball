package org.zeros.bouncy_balls.Objects.Area;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Objects.SerializableObjects.AreaSerializable;

import java.util.ArrayList;

public class Area {

    protected Path path = new Path();
    protected Point2D roughMin;
    protected Point2D roughMax;
    protected ArrayList<Point2D> cornerPoints = new ArrayList<>();
    protected ArrayList<ArrayList<Point2D>> segmentPoints = new ArrayList<>();
    protected ArrayList<LinearEquation> cornerLines = new ArrayList<>();
    protected ArrayList<ArrayList<LinearEquation>> segmentLines = new ArrayList<>();
    protected double rotation = 0;
    protected Point2D massCenter;

    protected Area() {
        path.setFill(Color.WHITE);
        path.setStroke(Color.WHITE);
        path.setStrokeWidth(0);
    }


    public void rescale(double factor) {
        AreaSerializable temp = new AreaSerializable(this);
        temp.rescale(factor);
        Area area = temp.deserialize();
        rewriteValues(area);

    }

    private void rewriteValues(Area area) {
        this.path = area.getPath();
        this.segmentPoints = area.segmentPoints;
        this.cornerPoints = area.cornerPoints;
        this.cornerLines = area.getCornerLines();
        this.segmentLines = area.getSegmentLines();
        this.rotation = area.getRotation();
        this.massCenter = area.getMassCenter();
        this.roughMin = area.roughMin;
        this.roughMax = area.roughMax;
    }


    protected void addStartPoint(Point2D point) {
        path.getElements().add(new MoveTo(point.getX(), point.getY()));
        cornerPoints.add(point);
    }

    protected void calculateRoughMassCenter() {
        Point2D sumPoint = new Point2D(0, 0);
        for (Point2D point : getAllPoints()) {
            sumPoint = sumPoint.add(point);
        }

        massCenter = sumPoint.multiply((double) 1 / getAllPoints().size());
    }

    protected void addStraightLineTo(Point2D point) {
        path.getElements().add(new LineTo(point.getX(), point.getY()));
        ArrayList<Point2D> temp = new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);
    }


    protected void addQuadCurveTo(Point2D controlPoint, Point2D point) {
        path.getElements().add(new QuadCurveTo(controlPoint.getX(), controlPoint.getY(), point.getX(), point.getY()));

        ArrayList<Point2D> temp = new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(controlPoint);
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);
    }

    protected void addCubicCurveTo(Point2D controlPoint1, Point2D controlPoint2, Point2D point) {
        path.getElements().add(new CubicCurveTo(controlPoint1.getX(), controlPoint1.getY(), controlPoint2.getX(), controlPoint2.getY(), point.getX(), point.getY()));

        ArrayList<Point2D> temp = new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(controlPoint1);
        temp.add(controlPoint2);
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);

    }

    protected void calculateRoughBinds() {
        roughMin = new Point2D(Double.MAX_VALUE, Double.MAX_VALUE);
        roughMax = new Point2D(-Double.MAX_VALUE, -Double.MAX_VALUE);
        for (ArrayList<Point2D> points : segmentPoints) {
            searchForExtremeValues(points);
        }
        searchForExtremeValues(cornerPoints);

    }

    private void searchForExtremeValues(ArrayList<Point2D> pointsList) {
        for (Point2D point : pointsList) {
            if (point.getX() < roughMin.getX()) {
                roughMin = new Point2D(point.getX(), roughMin.getY());
            }
            if (point.getX() > roughMax.getX()) {
                roughMax = new Point2D(point.getX(), roughMax.getY());
            }
            if (point.getY() < roughMin.getY()) {
                roughMin = new Point2D(roughMin.getX(), point.getY());
            }
            if (point.getY() > roughMax.getY()) {
                roughMax = new Point2D(roughMax.getX(), point.getY());
            }

        }
    }

    public void move(Point2D newCenter) {
        Point2D vector = newCenter.subtract(massCenter);
        massCenter = newCenter;
        for (ArrayList<Point2D> points : segmentPoints) {
            points.replaceAll(point2D -> point2D.add(vector));
        }
        cornerPoints.replaceAll(point2D -> point2D.add(vector));
        path.setTranslateX(path.getTranslateX() + vector.getX());
        path.setTranslateY(path.getTranslateY() + vector.getY());
        calculateBoundaryLines();
        calculateRoughBinds();
    }

    protected void calculateBoundaryLines() {
        cornerLines = new ArrayList<>();
        segmentLines = new ArrayList<>();

        for (int i = 0; i < cornerPoints.size() - 1; i++) {

            ArrayList<LinearEquation> temp = new ArrayList<>();
            for (int j = 0; j < segmentPoints.get(i).size() - 1; j++) {
                temp.add(new LinearEquation(segmentPoints.get(i).get(j), segmentPoints.get(i).get(j + 1)));
            }

            segmentLines.add(temp);
            cornerLines.add(new LinearEquation(cornerPoints.get(i), cornerPoints.get(i + 1)));
        }
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        if (rotation != 0) {
            AreaSerializable temp = new AreaSerializable(this);
            temp.rotate(rotation, massCenter);
            Area area = temp.deserialize();
            rewriteValues(area);

        }
    }

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

    public Point2D getMassCenter() {
        return massCenter;
    }

    public ArrayList<Point2D> getAllPoints() {
        ArrayList<Point2D> points = new ArrayList<>();
        points.add(segmentPoints.getFirst().getFirst());
        for (ArrayList<Point2D> iSegmentPoints : segmentPoints) {
            for (int i = 1; i < iSegmentPoints.size(); i++) {
                points.add(iSegmentPoints.get(i));
            }
        }
        return points;
    }

    public ArrayList<ArrayList<Point2D>> getSegmentPoints() {
        return segmentPoints;
    }

    public ArrayList<LinearEquation> getAllLines() {
        ArrayList<LinearEquation> lines = new ArrayList<>();
        for (ArrayList<LinearEquation> iSegmentLines : segmentLines) {
            lines.addAll(iSegmentLines);
        }
        return lines;
    }

    public ArrayList<ArrayList<LinearEquation>> getSegmentLines() {
        return segmentLines;
    }


}


