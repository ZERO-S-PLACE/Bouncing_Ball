package org.zeros.bouncy_balls.Objects.Area;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;

public class RectangleArea extends Area {


    public RectangleArea(Point2D corner1, Point2D corner3, double rotation) {
        super();
        this.rotation=rotation;
        calculateRectanglePointPointRotation(corner1, corner3, rotation);
        calculateBoundaryLines();
        calculateRoughBinds();
    }

    private void calculateRectanglePointPointRotation(Point2D corner1, Point2D corner3, double rotation) {
        Point2D corner4;
        Point2D corner2;
        if (rotation % (Math.PI / 2) != 0) {
            LinearEquation side1 = new LinearEquation(Math.tan(rotation), 0);
            side1 = side1.parallelTroughPoint(corner1);
            LinearEquation side2 = side1.parallelTroughPoint(corner3);
            LinearEquation side3 = new LinearEquation(-1 / Math.tan(rotation), 0);
            side3 = side3.parallelTroughPoint(corner1);
            LinearEquation side4 = side3.parallelTroughPoint(corner3);
            corner2 = side3.intersection(side2);
            corner4 = side4.intersection(side1);
        } else {
            corner2 = new Point2D(corner3.getX(), corner1.getY());
            corner4 = new Point2D(corner1.getX(), corner3.getY());
        }

        addStartPoint(corner1);
        addStraightLineTo(corner2);
        addStraightLineTo(corner3);
        addStraightLineTo(corner4);
        addStraightLineTo(corner1);

    }


}
