package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Model.Model;

public abstract class MovingObject {

    protected  MovingObjectType type;
    protected AnimationProperties animationProperties;
    protected String animationName;
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
    protected double charge=0;

    protected MovingObject(Animation animation) {
        setAnimation(animation);
        this.velocity=new Point2D(0,0);
        this.friction=animationProperties.getFRICTION();
        this.centerPoint=new Point2D(0,0);
        this.frameElapsed=1;
    }
    public void nextFrame() {
        updateCenter(nextCenter());
        updateNextCenter();
        updateVelocity();
        frameElapsed = 0.0;
    }
    public void rescale(double factor) {
        velocity=velocity.multiply(factor);
        acceleration=acceleration.multiply(factor);
        centerPoint=centerPoint.multiply(factor);
        furthestSpan=furthestSpan*factor;
        if(nextCenterPoint!=null) {
            nextCenterPoint = nextCenterPoint.multiply(factor);
            trajectory = new LinearEquation(centerPoint, nextCenterPoint);
        }


    }
    public void updateVelocity(Point2D velocity, double frameElapsed) {
        this.velocity = velocity.add(frameAcceleration().multiply(frameElapsed - frameElapsed()));
        velocity = velocity.multiply((1 - friction)*(frameElapsed - frameElapsed()));
        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));
    }

    public void updateVelocity() {
        this.velocity = velocity.add(frameAcceleration().multiply(1 - frameElapsed()));
        velocity = velocity.multiply((1 - friction));
        this.trajectory = new LinearEquation(centerPoint, centerPoint.add(velocity));
    }
    public void setInitialVelocity(Point2D velocity){
        this.velocity=velocity;
    }
    public void setAnimation(Animation animation) {
        this.animationProperties = animation.level.PROPERTIES();
        this.animationName=animation.getName();
        this.acceleration=new Point2D(0,animationProperties.getGRAVITY());
    }

    public double getFriction() {return friction;}
    public void setFriction(double friction) {this.friction = friction;}
    public void setMass(double mass) {this.mass = mass;}
    public Point2D velocity() {return velocity;}
    public Point2D acceleration() {return acceleration;}
    public Point2D frameVelocity() {return velocity.multiply(1 / animationProperties.getFRAME_RATE());}
    public Point2D frameAcceleration() {return acceleration.multiply(1 / animationProperties.getFRAME_RATE());}
    public void updateAcceleration(Point2D acceleration) {this.acceleration = acceleration;}
    public Point2D center() {return centerPoint;}
    public MovingObjectType getType() {return type;}
    public LinearEquation trajectory() {return trajectory;}
    public double getFurthestSpan() {return furthestSpan;}
    public void updateCenter(Point2D centerPoint) {this.centerPoint = centerPoint;}
    public Point2D nextCenter() {return nextCenterPoint;}
    public void updateNextCenter(Point2D nextCenterPoint) {this.nextCenterPoint = nextCenterPoint;}
    public void updateNextCenter() {this.nextCenterPoint = centerPoint.add(frameVelocity());}
    public Animation getAnimation() {return Model.getInstance().getRunningAnimation(animationName);}
    public double frameElapsed() {return frameElapsed;}
    public double getMass() {return mass;}
    public Shape getShape() {return shape;}
    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }


}


