package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Model;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Objects.SerializableObjects.MovingObjectSerializable;


public abstract class MovingObject implements Cloneable {

    protected MovingObjectType type;
    protected AnimationProperties animationProperties;
    protected String animationName;
    protected Shape shape;
    protected double furthestSpan;
    protected double friction;
    protected double mass;
    protected LinearEquation trajectory;
    protected Point2D velocity;
    protected Point2D acceleration;
    protected Point2D centerPoint;
    protected Point2D nextCenterPoint;
    protected double frameElapsed;
    protected double charge = 0;

    protected MovingObject(Animation animation) {
        setAnimation(animation);
        this.velocity = new Point2D(0, 0);
        this.friction = animationProperties.getFRICTION();
        this.centerPoint = new Point2D(0, 0);
        this.frameElapsed = 1;
    }

    public void nextFrame() {
        updateCenter(nextCenter());
        updateNextCenter();
        updateVelocity();
        frameElapsed = 0.0;
    }

    public void rescale(double factor) {
        velocity = velocity.multiply(factor);
        acceleration = acceleration.multiply(factor);
        centerPoint = centerPoint.multiply(factor);
        furthestSpan = furthestSpan * factor;
        if (nextCenterPoint != null) {
            nextCenterPoint = nextCenterPoint.multiply(factor);
            trajectory = new LinearEquation(centerPoint, nextCenterPoint);
        }


    }

    public void updateVelocity(Point2D velocity, double frameElapsed) {
        this.velocity = velocity.add(frameAcceleration().multiply(frameElapsed - frameElapsed()));
        velocity = velocity.multiply((1 - friction) * (frameElapsed - frameElapsed()));
        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));
    }

    public void updateVelocity() {
        this.velocity = velocity.add(frameAcceleration().multiply(1 - frameElapsed()));
        velocity = velocity.multiply((1 - friction));
        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));
    }

    public void setInitialVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public Point2D velocity() {
        return velocity;
    }

    public Point2D acceleration() {
        return acceleration;
    }

    public Point2D frameVelocity() {
        return velocity.multiply(1 / Properties.FRAME_RATE());
    }

    public Point2D frameAcceleration() {
        return acceleration.multiply(1 / Properties.FRAME_RATE() * Properties.SIZE_FACTOR());
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

    public Animation getAnimation() {
        return Model.getInstance().getRunningAnimation(animationName);
    }

    public void setAnimation(Animation animation) {
        this.animationProperties = animation.getLevel().PROPERTIES();
        this.animationName = animation.getAnimationIdentifier();
        this.acceleration = new Point2D(0, animationProperties.getGRAVITY());
    }

    public double frameElapsed() {
        return frameElapsed;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Shape getShape() {
        return shape;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }


    @Override
    public MovingObject clone() {
        try {
            MovingObject clone = (MovingObject) super.clone();
            clone.type = this.getType();
            clone.animationProperties = this.animationProperties;
            clone.animationName = this.animationName;
            clone.shape = this.shape;
            clone.furthestSpan = this.furthestSpan;
            clone.friction = this.friction;
            clone.mass = this.mass;
            clone.trajectory = this.trajectory;
            clone.velocity = this.velocity;
            clone.acceleration = this.acceleration;
            clone.centerPoint = this.centerPoint;
            clone.nextCenterPoint = this.nextCenterPoint;
            clone.frameElapsed = 0;
            clone.charge = this.charge;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {

            return ((MovingObject) obj).getType().equals(this.type) && ((MovingObject) obj).centerPoint.equals(this.centerPoint) && ((MovingObject) obj).velocity.equals(this.velocity);


        }
        return false;
    }

    public MovingObject copy() {
        try {
            super.clone();
            MovingObjectSerializable temp =new MovingObjectSerializable(this);
            return temp.deserialize(getAnimation());
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}


