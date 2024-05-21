package org.zeros.bouncy_balls.Objects.Area;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.CurveSegment;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.Segment;

import java.util.ArrayList;

public class OvalArea extends Area {
    private final double radiusX;
    private final double radiusY;

    public OvalArea(Point2D center, double radiusX, double radiusY, double rotation) {
        super();
        this.rotation = rotation;
        this.massCenter = center;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        calculateOval(center, radiusX, radiusY, rotation);
        calculateBoundaryLines();
        calculateRoughBinds();
        setRotation(rotation);
    }

    public double getRadiusX() {
        return radiusX;
    }

    public double getRadiusY() {
        return radiusY;
    }

    private void calculateOval(Point2D center, double radiusX, double radiusY, double rotation) {
        double quadOffset = 0.552284749831;//for creating circles and ellipses from 4 segments

        LinearEquation horizontal = new LinearEquation(0, center.getY());
        LinearEquation upperTan = horizontal.offsetLine(radiusY, new Point2D(0, center.getY() - 1));
        LinearEquation lowerTan = horizontal.offsetLine(radiusY, new Point2D(0, center.getY() + 1));
        LinearEquation vertical = horizontal.perpendicularTroughPoint(center);
        LinearEquation leftTan = vertical.offsetLine(radiusX, new Point2D(center.getX() - 1, 0));
        LinearEquation rightTan = vertical.offsetLine(radiusX, new Point2D(center.getX() + 1, 0));

        LinearEquation cPointsY_Upper = horizontal.offsetLine(radiusY * quadOffset, new Point2D(0, center.getY() - 1));
        LinearEquation cPointsY_Lower = horizontal.offsetLine(radiusY * quadOffset, new Point2D(0, center.getY() + 1));
        LinearEquation cPointsX_Left = vertical.offsetLine(radiusX * quadOffset, new Point2D(center.getX() - 1, 0));
        LinearEquation cPointsX_Right = vertical.offsetLine(radiusX * quadOffset, new Point2D(center.getX() + 1, 0));

        //calculating points clockwise;
        Point2D upperPoint = upperTan.intersection(vertical);
        Point2D control1 = upperTan.intersection(cPointsX_Right);
        Point2D control2 = rightTan.intersection(cPointsY_Upper);
        Point2D rightPoint = rightTan.intersection(horizontal);

        addStartPoint(upperPoint);
        addCubicCurveTo(control1, control2, rightPoint);

        control1 = rightTan.intersection(cPointsY_Lower);
        control2 = lowerTan.intersection(cPointsX_Right);
        Point2D lowerPoint = lowerTan.intersection(vertical);

        addCubicCurveTo(control1, control2, lowerPoint);

        control1 = lowerTan.intersection(cPointsX_Left);
        control2 = leftTan.intersection(cPointsY_Lower);
        Point2D leftPoint = leftTan.intersection(horizontal);

        addCubicCurveTo(control1, control2, leftPoint);

        control1 = leftTan.intersection(cPointsY_Upper);
        control2 = upperTan.intersection(cPointsX_Left);

        addCubicCurveTo(control1, control2, upperPoint);

    }


}
