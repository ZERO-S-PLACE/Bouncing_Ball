package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Model.Properties;

public class Ball extends MovingObject {
    public Ball(double radius, Animation animation) {
        super(animation);
        this.type=MovingObjectType.BALL;
        this.shape=new Circle(Math.max(radius/Properties.SIZE_FACTOR(), 1));
        this.furthestSpan=Math.max(radius, 1);
        this.mass=furthestSpan;
        shape.fillProperty().set(Color.RED);
        ((Circle) shape).setCenterX(centerPoint.getX()/ Properties.SIZE_FACTOR());
        ((Circle) shape).setCenterY(centerPoint.getY()/Properties.SIZE_FACTOR());
    }

    @Override
    public void updateCenter(Point2D centerPoint) {
        super.updateCenter(centerPoint);
        ((Circle) shape).setCenterX(centerPoint.getX()/Properties.SIZE_FACTOR());
        ((Circle) shape).setCenterY(centerPoint.getY()/Properties.SIZE_FACTOR());
    }
    @Override
    public void rescale(double factor) {
        super.rescale(factor);
        ((Circle) shape).setCenterX(centerPoint.getX()/Properties.SIZE_FACTOR());
        ((Circle) shape).setCenterY(centerPoint.getY()/Properties.SIZE_FACTOR());
        ((Circle) shape).setRadius(furthestSpan/Properties.SIZE_FACTOR());
    }
    public void setRadius(double radius) {
        this.furthestSpan=radius;
        ((Circle) shape).setRadius(radius);
    }

    public double getRadius() {
        return ((Circle) shape).getRadius()*Properties.SIZE_FACTOR();
    }
}
