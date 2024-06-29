package org.zeros.bouncy_balls.Objects.SerializableObjects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;

import java.io.Serializable;
import java.util.ArrayList;

public class AreaSerializable implements Serializable {
    private final ArrayList<ArrayList<Point2DSerializable>> segmentPoints = new ArrayList<>();
    private final double lineWidth;
    private final String lineColor;
    private final String fillColor;

    public AreaSerializable(Area area) {
        this.lineColor = area.getPath().getStroke().toString();
        this.fillColor = area.getPath().getFill().toString();
        this.lineWidth = area.getPath().getStrokeWidth();
        for (int i = 0; i < area.getCorners().size() - 1; i++) {
            ArrayList<Point2DSerializable> points = new ArrayList<>();
            for (Point2D point : area.getSegmentPoints(i)) {
                points.add(new Point2DSerializable(point));
            }
            segmentPoints.add(points);
        }
    }

    public Area deserialize() {
        PolylineArea area = new PolylineArea(segmentPoints.getFirst().getFirst().deserialize());
        for (ArrayList<Point2DSerializable> points : segmentPoints) {
            if (points.size() == 2) {
                area.drawStraightSegmentTo(points.getLast().deserialize());
            } else if (points.size() == 3) {
                area.drawQuadCurveTo(points.get(1).deserialize(), points.getLast().deserialize());
            } else if (points.size() == 4) {
                area.drawCubicCurveTo(points.get(1).deserialize(), points.get(2).deserialize(), points.getLast().deserialize());
            }
        }
        area.closeAndSave();
        area.getPath().fillProperty().set(Paint.valueOf(fillColor));
        area.getPath().strokeProperty().set(Paint.valueOf(lineColor));
        area.getPath().strokeWidthProperty().set(lineWidth);
        return area;
    }

    public void rescale(double factor) {
        for (ArrayList<Point2DSerializable> points : segmentPoints) {
            points.replaceAll(point -> point.multiply(factor));
        }
    }

    public void rotate(double rotation, Point2D center) {
        for (ArrayList<Point2DSerializable> points : segmentPoints) {
            points.replaceAll(point -> new Point2DSerializable(VectorMath.rotatePoint(point.deserialize(), center, rotation)));
        }
    }
}
