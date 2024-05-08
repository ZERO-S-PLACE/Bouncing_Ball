package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;

public abstract class MovingObject {


    protected  MovingObjectType type;

    protected  Animation animation;
    protected  Shape shape;
    protected  double furthestSpan;
    protected double friction;
    protected double mass;
    protected LinearEquation trajectory;
    protected Point2D velocity;
    protected Point2D acceleration;
    protected Point2D centerPoint;
    protected Point2D nextCenterPoint;
    protected double frameElapsed;

    protected MovingObject(Animation animation) {
        this.animation = animation;
        this.velocity=new Point2D(0,0);
        this.mass=furthestSpan;
        this.friction=animation.PROPERTIES.getFRICTION();
        this.centerPoint=new Point2D(0,0);
        this.acceleration=new Point2D(0,animation.PROPERTIES.getGRAVITY());
    }
    public void nextFrame() {
        updateCenter(nextCenter());
        updateNextCenter();
        updateVelocity();
        frameElapsed = 0.0;

    }
    public void updateVelocity(Point2D velocity, double frameElapsed) {
        this.velocity = velocity.add(acceleration.multiply(frameElapsed - frameElapsed()));
        velocity = velocity.multiply(frameElapsed - frameElapsed());
        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));
    }

    public void updateVelocity() {

        this.velocity = velocity.add(acceleration.multiply(1 - frameElapsed()));
        velocity = velocity.multiply((1 - friction) * (1 - frameElapsed()));
        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));
    }
    public void setAnimation(Animation animation) {
        this.animation = animation;
        this.acceleration = new Point2D(0, animation.PROPERTIES.getGRAVITY());
    }

    public double getFriction() {return friction;}
    public void setFriction(double friction) {this.friction = friction;}
    public void setMass(double mass) {this.mass = mass;}
    public void setVelocity(Point2D velocity) {this.velocity = velocity;}
    public Point2D velocity() {return velocity;}
    public Point2D acceleration() {return acceleration;}
    public Point2D frameVelocity() {return velocity.multiply(1 / animation.PROPERTIES.getFRAME_RATE());}
    public Point2D frameAcceleration() {return acceleration.multiply(1 / animation.PROPERTIES.getFRAME_RATE());}
    public void updateAcceleration(Point2D acceleration) {this.acceleration = acceleration;}
    public Point2D center() {return centerPoint;}
    public MovingObjectType getType() {return type;}
    public LinearEquation trajectory() {return trajectory;}
    public double getFurthestSpan() {return furthestSpan;}
    public void updateCenter(Point2D centerPoint) {this.centerPoint = centerPoint;}
    public Point2D nextCenter() {return nextCenterPoint;}
    public void updateNextCenter(Point2D nextCenterPoint) {this.nextCenterPoint = nextCenterPoint;}
    public void updateNextCenter() {this.nextCenterPoint = centerPoint.add(frameVelocity());}
    public Animation getAnimation() {return animation;}
    public double frameElapsed() {return frameElapsed;}
    public double getMass() {return mass;}
    public Shape getShape() {return shape;}
}


