package org.zeros.bouncy_balls.Animation;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.zeros.bouncy_balls.Calculations.Transition;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Calculations.Bounce;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;

public class Animation {
    private ObservableList<MovingObject> movingObjectsList;
    private boolean paused=false;

    public Animation(ObservableList<MovingObject> movingObjectsList) {

        this.movingObjectsList = movingObjectsList;
    }


    public void animate() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                animateThis();
                }
            };
        timer.start();

    }

    private void animateThis() {

        for (int i = 0; i < movingObjectsList.size(); i++) {
            MovingObject movingObject1 = movingObjectsList.get(i);
            boolean bounced = false;
            double furthestX = 0;
            double furthestY = 0;
            Point2D newCenter1 = calculateNewCenter(movingObject1);

            if (movingObject1.getType() == MovingObjectType.BALL) {

                if (movingObject1.getType() == MovingObjectType.BALL) {
                    furthestX = ((Ball) movingObject1).getRadius();
                    furthestY = furthestX;
                }
                if (!Transition.isInside(newCenter1, furthestX, furthestY)) {
                    Bounce.ballFromWall((Ball) movingObject1, newCenter1);
                    bounced = true;
                }
            }

            for (int j = i + 1; j < movingObjectsList.size(); j++) {

                MovingObject movingObject2 = movingObjectsList.get(j);
                Point2D newCenter2 = calculateNewCenter(movingObject2);
                if (movingObject1.getType().equals(MovingObjectType.BALL) &&
                        movingObject2.getType().equals(MovingObjectType.BALL)) {
                    double distance = newCenter2.distance(newCenter1);
                    double minDistance = ((Ball) movingObject1).getRadius() + ((Ball) movingObject2).getRadius();
                    if (distance <= minDistance) {
                        Bounce.twoBalls(((Ball)movingObject1), ((Ball)movingObject2));
                        bounced = true;;
                    }
                }
            }
            if (!bounced) {
                movingObject1.centerPointProperty().set(newCenter1);
                ((Ball)movingObject1).updateCircleCenter();
            }


        }
    }










    public static Point2D calculateNewCenter(MovingObject movingObject) {
        Point2D newCenter= movingObject.centerPointProperty().get();
        Point2D fVelocity = movingObject.velocityProperty().get();
        fVelocity=fVelocity.multiply(1/Properties.getFRAME_RATE());
        newCenter=newCenter.add(fVelocity);
        return newCenter;
    }

    public static void reverseMove(MovingObject movingObject) {
        Point2D newCenter= movingObject.centerPointProperty().get();
        Point2D fVelocity = movingObject.velocityProperty().get();
        fVelocity=fVelocity.multiply(1/Properties.getFRAME_RATE());
        newCenter=newCenter.subtract(fVelocity);
        movingObject.centerPointProperty().set(newCenter);
    }


}
