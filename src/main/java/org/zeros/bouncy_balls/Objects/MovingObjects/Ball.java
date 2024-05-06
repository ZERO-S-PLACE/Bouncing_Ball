package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation;

public class Ball extends MovingObject {

    public Ball(Point2D velocity, double mass, Point2D center, int radius, double friction, Animation animation) {
        super(MovingObjectType.BALL, new Circle(Math.max(radius, 1)), Math.max(radius, 1), velocity, mass, center, friction, animation);
        ((Circle) shape).setCenterX(centerPoint.getX());
        ((Circle) shape).setCenterY(centerPoint.getY());
    }

    @Override
    public void updateCenter(Point2D centerPoint) {
        super.updateCenter(centerPoint);
        ((Circle) shape).setCenterX(centerPoint.getX());
        ((Circle) shape).setCenterY(centerPoint.getY());
    }

    public double getRadius() {
        return ((Circle) shape).getRadius();
    }
}
