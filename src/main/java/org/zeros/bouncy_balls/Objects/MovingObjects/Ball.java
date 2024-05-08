package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation;

public class Ball extends MovingObject {

    public Ball(int radius, Animation animation) {
        super(animation);
        this.type=MovingObjectType.BALL;
        this.shape=new Circle(Math.max(radius, 1));
        this.furthestSpan=Math.max(radius, 1);
        ((Circle) shape).setCenterX(centerPoint.getX());
        ((Circle) shape).setCenterY(centerPoint.getY());
    }

    @Override
    public void updateCenter(Point2D centerPoint) {
        super.updateCenter(centerPoint);
        ((Circle) shape).setCenterX(centerPoint.getX());
        ((Circle) shape).setCenterY(centerPoint.getY());
    }
    public void setRadius(double radius) {
        this.furthestSpan=radius;
        ((Circle) shape).setRadius(radius);
    }

    public double getRadius() {
        return ((Circle) shape).getRadius();
    }
}
