package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class Ball extends MovingObject{

    private final int radius;



    public Ball(Point2D velocity, double mass, Point2D center,int radius) {
        super(velocity,  mass,  center);
        type=MovingObjectType.BALL;

        if(radius==0){
            this.radius=radius=1;
        }
        else {
            this.radius=Math.abs(radius);
        }
        this.furthestSpan=radius;
        this.shape=new Circle(radius);
        ((Circle)shape).setCenterX(centerPoint.getX());
        ((Circle)shape).setCenterY(centerPoint.getY());


    }

    @Override
    public void updateCenter(Point2D centerPoint) {
        super.updateCenter(centerPoint);
        ((Circle)shape).setCenterX(centerPoint.getX());
        ((Circle)shape).setCenterY(centerPoint.getY());
    }

    public int getRadius() {
        return radius;
    }
}
