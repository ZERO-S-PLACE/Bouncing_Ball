package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

public abstract class MovingObject {


    protected MovingObjectType type;
    protected Shape shape;
    protected ObjectProperty<Point2D> velocity;
    protected ObjectProperty<Point2D> acceleration;
    protected int mass;
    protected ObjectProperty<Point2D> centerPoint;


    public MovingObject(int startVelocityX,int startVelocityY, int mass, double startCenterX, double startCenterY) {
        this.velocity = new SimpleObjectProperty<>(new Point2D(startVelocityX,startVelocityY));
        this.acceleration = new SimpleObjectProperty<>(new Point2D(0,0));
        this.mass = mass;
        this.centerPoint = new SimpleObjectProperty<>(new Point2D(startCenterX,startCenterY));
    }



    public ObjectProperty<Point2D> velocityProperty() {
        return velocity;
    }

    public ObjectProperty<Point2D> accelerationProperty() {
        return acceleration;
    }

    public int getMass() {
        return mass;
    }

    public Shape getShape() {
        return shape;
    }

    public ObjectProperty<Point2D> centerPointProperty() {
        return centerPoint;
    }

    public MovingObjectType getType() {
        return type;
    }

}
