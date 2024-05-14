package org.zeros.bouncy_balls.Serialization.SerializableObjects;

import javafx.geometry.Point2D;

import java.io.Serializable;

public class Point2DSerializable implements Serializable {
    private final double x;
    private final double y;

    public Point2DSerializable(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2DSerializable(Point2D point2D) {
        this.x = point2D.getX();
        this.y = point2D.getY();
    }

    public Point2D deserialize() {
        return new Point2D(x, y);
    }
}
