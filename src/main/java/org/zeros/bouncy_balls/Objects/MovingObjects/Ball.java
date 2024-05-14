package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation;

public class Ball extends MovingObject {
    public Ball(double radius, Animation animation) {
        super(animation);
        this.type=MovingObjectType.BALL;
        this.shape=new Circle(Math.max(radius, 1));
        this.furthestSpan=Math.max(radius, 1);
        this.mass=furthestSpan;
        shape.fillProperty().set(Color.RED);
        ((Circle) shape).setCenterX(centerPoint.getX());
        ((Circle) shape).setCenterY(centerPoint.getY());
    }

    @Override
    public void updateCenter(Point2D centerPoint) {
        super.updateCenter(centerPoint);
        ((Circle) shape).setCenterX(centerPoint.getX());
        ((Circle) shape).setCenterY(centerPoint.getY());
    }
    @Override
    public void rescale(double factor) {
        super.rescale(factor);
        ((Circle) shape).setCenterX(centerPoint.getX());
        ((Circle) shape).setCenterY(centerPoint.getY());
        ((Circle) shape).setRadius(furthestSpan);
    }
    public void setRadius(double radius) {
        this.furthestSpan=radius;
        ((Circle) shape).setRadius(radius);
    }

    public double getRadius() {
        return ((Circle) shape).getRadius();
    }
}
