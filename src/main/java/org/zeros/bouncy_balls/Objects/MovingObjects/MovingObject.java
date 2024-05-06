package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;

public abstract class MovingObject {

    protected final MovingObjectType type;
    protected final Animation animation;
    protected final Shape shape;
    protected final double furthestSpan;
    protected double friction;
    protected double mass;
    protected LinearEquation trajectory;
    protected Point2D velocity;
    protected Point2D acceleration;
    protected Point2D centerPoint;
    protected Point2D nextCenterPoint;
    protected double frameElapsed;

    protected MovingObject(MovingObjectType type, Shape shape, double furthestSpan, Point2D velocity, double mass, Point2D center, double friction, Animation animation) {
        this.type = type;
        this.shape = shape;
        this.animation = animation;
        this.furthestSpan = furthestSpan;
        this.velocity = velocity;
        this.acceleration = new Point2D(0, animation.PROPERTIES.GRAVITY);
        this.mass = mass;
        this.friction = friction;
        this.centerPoint = center;
        this.nextCenterPoint = centerPoint.add(frameVelocity());
        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));
        this.frameElapsed = 0.0;
        if (mass == 0) {
            this.mass = 1;
        } else {
            this.mass = Math.abs(mass);
        }

    }


    public void nextFrame() {
        updateCenter(nextCenter());
        updateNextCenter();
        updateVelocity();
        frameElapsed = 0.0;

    }


    public double frameElapsed() {
        return frameElapsed;
    }


    public Point2D frameVelocity() {
        return velocity.multiply(1 / animation.PROPERTIES.FRAME_RATE);
    }

    public double getMass() {
        return mass;
    }

    public Shape getShape() {
        return shape;
    }

    public Point2D velocity() {
        return velocity;
    }

    public void updateVelocity(Point2D velocity, double frameElapsed) {

        this.velocity = velocity;
        this.velocity = velocity.add(acceleration.multiply(frameElapsed - frameElapsed()));
        if (friction > 0 && friction < 1) {
            velocity = velocity.multiply(frameElapsed - frameElapsed());
        }

        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));
    }

    public void updateVelocity() {

        this.velocity = velocity.add(acceleration.multiply(1 - frameElapsed()));
        if (friction > 0 && friction < 1) {
            velocity = velocity.multiply((1 - friction) * (1 - frameElapsed()));
        }


        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));

    }

    public Point2D acceleration() {
        return acceleration;
    }

    public void updateAcceleration(Point2D acceleration) {
        this.acceleration = acceleration;
    }

    public Point2D center() {
        return centerPoint;
    }

    public MovingObjectType getType() {
        return type;
    }

    public LinearEquation trajectory() {
        return trajectory;
    }

    public double getFurthestSpan() {
        return furthestSpan;
    }

    public void updateCenter(Point2D centerPoint) {
        this.centerPoint = centerPoint;

    }

    public Point2D nextCenter() {
        return nextCenterPoint;
    }

    public void updateNextCenter(Point2D nextCenterPoint) {
        this.nextCenterPoint = nextCenterPoint;
    }

    public void updateNextCenter() {
        this.nextCenterPoint = centerPoint.add(frameVelocity());
    }


}
