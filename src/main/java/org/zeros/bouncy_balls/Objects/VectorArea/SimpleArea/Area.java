package org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.Calculations.Equations.Equation;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.CurveSegment;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.LineSegment;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.VectorArea.VectorArea;
import org.zeros.bouncy_balls.Objects.SerializableObjects.AreaSerializable;

import java.util.ArrayList;
import java.util.Random;

public class Area extends VectorArea implements Cloneable {

    protected Path path = new Path();
    protected Point2D roughMin;
    protected Point2D roughMax;
    protected ArrayList<Point2D> cornerPoints = new ArrayList<>();
    protected ArrayList<ArrayList<Point2D>> segmentPoints = new ArrayList<>();
    protected ArrayList<LinearEquation> cornerLines = new ArrayList<>();
    protected ArrayList<ArrayList<LinearEquation>> segmentLines = new ArrayList<>();
    protected ArrayList<Segment> segments=new ArrayList<>();
    protected double rotation = 0;
    protected Point2D massCenter;

    protected Area()  {
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
        this.segments=area.segments;
        this.rotation = area.getRotation();
        this.massCenter = area.getMassCenter();
        this.roughMin = area.roughMin;
        this.roughMax = area.roughMax;
    }
    protected void calculateRoughMassCenter() {
        Point2D sumPoint = new Point2D(0, 0);
        for (Point2D point : getAllPoints()) {
            sumPoint = sumPoint.add(point);
        }

        massCenter = sumPoint.multiply((double) 1 / getAllPoints().size());
    }

    protected void addStartPoint(Point2D point) {
        path.getElements().add(new MoveTo(point.getX()/ Properties.SIZE_FACTOR(), point.getY()/ Properties.SIZE_FACTOR()));
        cornerPoints.add(point);
    }



    protected void addStraightLineTo(Point2D point) {
        path.getElements().add(new LineTo(point.getX()/Properties.SIZE_FACTOR(), point.getY()/Properties.SIZE_FACTOR()));
        ArrayList<Point2D> temp = new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);
        segments.add(new LineSegment(temp.getFirst(),temp.getLast()));
    }


    protected void addQuadCurveTo(Point2D controlPoint, Point2D point) {
        path.getElements().add(new QuadCurveTo(controlPoint.getX()/Properties.SIZE_FACTOR(),
                controlPoint.getY()/Properties.SIZE_FACTOR(), point.getX()/Properties.SIZE_FACTOR(), point.getY()/Properties.SIZE_FACTOR()));

        ArrayList<Point2D> temp = new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(controlPoint);
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);
        segments.add(new CurveSegment(temp));
    }

    protected void addCubicCurveTo(Point2D controlPoint1, Point2D controlPoint2, Point2D point) {
        path.getElements().add(new CubicCurveTo(controlPoint1.getX()/Properties.SIZE_FACTOR(),
                controlPoint1.getY()/Properties.SIZE_FACTOR(), controlPoint2.getX()/Properties.SIZE_FACTOR()
                , controlPoint2.getY()/Properties.SIZE_FACTOR(), point.getX()/Properties.SIZE_FACTOR(), point.getY()/Properties.SIZE_FACTOR()));

        ArrayList<Point2D> temp = new ArrayList<>();
        temp.add(cornerPoints.getLast());
        temp.add(controlPoint1);
        temp.add(controlPoint2);
        temp.add(point);
        segmentPoints.add(temp);
        cornerPoints.add(point);
        segments.add(new CurveSegment(temp));
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
        path.setTranslateX(path.getTranslateX() + vector.getX()/Properties.SIZE_FACTOR());
        path.setTranslateY(path.getTranslateY() + vector.getY()/Properties.SIZE_FACTOR());
        calculateBoundaryLines();
        calculateRoughBinds();
    }

    protected void calculateBoundaryLines() {
        cornerLines = new ArrayList<>();
        segmentLines = new ArrayList<>();
        segments=new ArrayList<>();

        for (int i = 0; i < cornerPoints.size() - 1; i++) {

            ArrayList<LinearEquation> temp = new ArrayList<>();
            for (int j = 0; j < segmentPoints.get(i).size() - 1; j++) {
                temp.add(new LinearEquation(segmentPoints.get(i).get(j), segmentPoints.get(i).get(j + 1)));
            }

            segmentLines.add(temp);
            cornerLines.add(new LinearEquation(cornerPoints.get(i), cornerPoints.get(i + 1)));
            if(segmentPoints.get(i).size()==2){
                segments.add(new LineSegment(segmentPoints.get(i).getFirst(),segmentPoints.get(i).getLast()));
            }else {
                segments.add(new CurveSegment(segmentPoints.get(i)));
            }
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

    public ArrayList<Equation> getSegmentEquations() {
        ArrayList<Equation>equations=new ArrayList<>();
        for (Segment segment:segments){
            equations.add(segment.getEquation());
        }
        return equations;
    }
    public Equation getSegmentEquation(int segment) {
        return segments.get(segment).getEquation();
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }
    public Segment getSegment(int segment) {
        return segments.get(segment);
    }


    public boolean isEqualTo(Area area){
        if(this.segments.size()!=area.segments.size())return false;
        Segment firstSegment = null;
        for(Segment segment: area.segments){
            if(segment.isEqualTo(this.getSegment(0)))firstSegment=segment;
        }
        if(firstSegment==null)return false;

        int start=area.segments.indexOf(firstSegment);
            int incrementFactor;
            if(this.segments.get(1).isEqualTo(area.segments.get(turnToIndex(start+1,area.segments.size())))){
                incrementFactor=1;
            }else if(this.segments.get(1).isEqualTo(area.segments.get(turnToIndex(start-1,area.segments.size())))){
                incrementFactor=-1;
            }else return false;

            for (int i=2;i<segments.size();i++){
                if(!this.segments.get(i).isEqualTo(area.segments.get(turnToIndex(start+incrementFactor*i,area.segments.size())))){
                    return false;
                }
            }
            return true;




    }

    private int turnToIndex(int i, int size) {

        if(i>=size){
            while (i>=size){
                i=i-size;
            }
            return i;
        }
        if (i<0){
            while (i<0){
                i=i+size;
            }
            return i;
        }
        return i;
    }

    public Point2D getPointInside() {
        if(AreasMath.isInsideArea(this,massCenter))return massCenter;
        Random random=new Random();
        while (true){
            Point2D point=new Point2D(random.nextDouble(roughMin.getX(),roughMax.getX()),random.nextDouble(roughMin.getY(),roughMax.getY()));
            if(AreasMath.isInsideArea(this,point))return point;
        }
    }

    @Override
    public Area clone() {
        try {
            Area clone = (Area) super.clone();
            rewriteValues(clone);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}


