package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Calculations.LinearEquation;
import org.zeros.bouncy_balls.Model.Properties;

public abstract class MovingObject {

    protected MovingObjectType type;
    protected LinearEquation trajectory;
    protected Shape shape;
    protected Point2D velocity;
    protected Point2D acceleration;
    protected double mass;
    protected double furthestSpan;
    protected Point2D centerPoint;
    protected Point2D nextCenterPoint;
    protected boolean bounced;
    protected double frameElapsed;
    public double getFurthestSpan() {
        return furthestSpan;
    }






    public MovingObject(Point2D velocity, double mass, Point2D center) {
        this.velocity =velocity;
        this.acceleration = new Point2D(0,0);
        this.mass = mass;
        this.centerPoint = center;
        this.nextCenterPoint=centerPoint.add(frameVelocity());
        this.trajectory=new LinearEquation(centerPoint,centerPoint.add(velocity));
        this.frameElapsed=0.0;
        if(mass==0){
            this.mass=1;
        }
        else {
            this.mass=Math.abs(mass);
        }
    }
    public void updateCenter(Point2D centerPoint) {
        this.centerPoint = centerPoint;
        this.trajectory = new LinearEquation(centerPoint,centerPoint.add(velocity));
    }
    public void nextFrame(){
        updateCenter(nextCenter());
        updateNextCenter();
        frameElapsed=0.0;
        bounced=false;
    }


    public double frameElapsed() {
        return frameElapsed;
    }

    public void setFrameElapsed(double frameElapsed) {
        this.frameElapsed = frameElapsed;
    }

    public Point2D frameVelocity() {
        return velocity.multiply(1/ Properties.getFRAME_RATE());
    }

    public double getMass() {
        return mass;
    }

    public Shape getShape() {
        return shape;
    }

    public void setTrajectory(LinearEquation trajectory) {
        this.trajectory = trajectory;
    }

    public Point2D velocity() {
        return velocity;
    }

    public void updateVelocity(Point2D velocity) {
        this.velocity = velocity;
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
    public LinearEquation trajectory() {        return trajectory;    }

    public Point2D nextCenter() {
        return nextCenterPoint;
    }

    public void updateNextCenter(Point2D nextCenterPoint) {
        this.nextCenterPoint = nextCenterPoint;
    }
    public void updateNextCenter() {
        this.nextCenterPoint = centerPoint.add(frameVelocity());
    }

    public boolean isBounced() {
        return bounced;
    }

    public void setBounced(boolean bounced) {
        this.bounced = bounced;
    }

}
