package org.zeros.bouncy_balls.Objects.MovingObjects;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class Ball extends MovingObject{

    private final int radius;



    public Ball(int radius,int startVelocityX, int startVelocityY, int mass, double startCenterX, double startCenterY) {
        super(startVelocityX, startVelocityY, mass, startCenterX, startCenterY);
        type=MovingObjectType.BALL;
        this.radius = radius;
        this.shape=new Circle(radius);
        updateCircleCenter();

    }
    public void updateCircleCenter(){
        ((Circle)shape).setCenterX(centerPoint.get().getX());
        ((Circle)shape).setCenterY(centerPoint.get().getY());
    }


    public int getRadius() {
        return radius;
    }
}
