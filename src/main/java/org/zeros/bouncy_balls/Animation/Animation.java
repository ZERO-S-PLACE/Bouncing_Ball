package org.zeros.bouncy_balls.Animation;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Bounce;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;

import java.util.ArrayList;

public class Animation {
    private final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int mObj1;
    private int bouncesCount;
    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            animateThis();
        }

    };

    public void animate() {
        timer.start();
    }

    public void pause() {
        timer.stop();
    }

    public void addMovingObject(MovingObject movingObject) {
        movingObjects.add(movingObject);
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public void removeMovingObject(MovingObject movingObject) {
        movingObjects.remove(movingObject);
    }

    public void removeObstacle(Obstacle obstacle) {
        obstacles.remove(obstacle);
    }


    private void animateThis() {
        bouncesCount = 1;
        //while (boucesCount>0) {

        resetBounces();
        searchForBounces();
        //}

        setCalculatedFinalCenters();
    }

    private void searchForBounces() {
        mObj1 = 0;

        for (; mObj1 < movingObjects.size(); mObj1++) {
            movingObjects.get(mObj1).setBounced(bouncesAgainstBorder());
            if (movingObjects.get(mObj1).isBounced()) {
                bouncesCount++;
            }
            boolean temp = bouncedAgainstObstacle();
            if (temp) {
                bouncesCount++;
            }
            temp = bouncedByAnother();
            if (temp) {
                bouncesCount++;
            }

        }
    }

    private void resetBounces() {
        bouncesCount = 0;

        for (MovingObject movingObject : movingObjects) {
            movingObject.setBounced(false);
        }
    }

    private void setCalculatedFinalCenters() {
        for (MovingObject movingObject : movingObjects) {
            movingObject.nextFrame();
        }
    }

    private boolean bouncesAgainstBorder() {
        boolean temp = false;

        if (!BindsCheck.isInsideBorders(movingObjects.get(mObj1).nextCenter(), movingObjects.get(mObj1).getFurthestSpan())) {
            if (movingObjects.get(mObj1).getType().equals(MovingObjectType.BALL)) {
                temp = Bounce.ballFromWall((Ball) movingObjects.get(mObj1), movingObjects.get(mObj1).nextCenter());
            }
        }
        if (!movingObjects.get(mObj1).isBounced()) {
            movingObjects.get(mObj1).setBounced(temp);
        }
        return temp;
    }

    private boolean bouncedAgainstObstacle() {
        boolean temp = false;
        for (Obstacle obstacle : obstacles) {
            if(BindsCheck.isInsideRoughBinds(movingObjects.get(mObj1),obstacle)) {
                //if (movingObjects.get(mObj1).getType().equals(MovingObjectType.BALL)) {
                    if (BindsCheck.intersectsWithObstacle(((Ball) movingObjects.get(mObj1)), obstacle)) {
                        temp = Bounce.ballFromObstacle(((Ball) movingObjects.get(mObj1)), obstacle);
                    }
                //}
            }

        }
        if (!movingObjects.get(mObj1).isBounced()) {
            movingObjects.get(mObj1).setBounced(temp);
        }
        return temp;
    }


    private boolean bouncedByAnother() {
        boolean temp = false;
        //double closestDistance = Double.MAX_VALUE;
        int closestObj = -1;

        for (int j = mObj1 + 1; j < movingObjects.size(); j++) {
            double distance = movingObjects.get(mObj1).nextCenter().distance(movingObjects.get(j).nextCenter());
            double minDistanceAllow = movingObjects.get(mObj1).getFurthestSpan() + movingObjects.get(j).getFurthestSpan();
            Point2D intersection = movingObjects.get(mObj1).trajectory().intersection(movingObjects.get(j).trajectory());
            boolean trajectoriesIntersect = false;
            if (intersection != null) {
                trajectoriesIntersect = BindsCheck.isBetweenPoints(intersection, movingObjects.get(mObj1).center(), movingObjects.get(mObj1).nextCenter()) &&
                        BindsCheck.isBetweenPoints(intersection, movingObjects.get(j).center(), movingObjects.get(j).nextCenter());


                if ((distance <= minDistanceAllow || trajectoriesIntersect)){//&&minDistanceAllow<movingObjects.get(mObj1).center().distance(movingObjects.get(j).center())) {
                    //closestDistance = movingObjects.get(mObj1).center().distance(intersection);
                    closestObj = j;
                    temp = Bounce.twoBalls(((Ball) movingObjects.get(mObj1)), ((Ball) movingObjects.get(closestObj)));
                    bouncesCount++;
                    break;



                }
            }

        }

        return temp;
    }

    public boolean hasFreePlace(Ball ball) {
        boolean freePlace = true;
        if (!BindsCheck.isInsideBorders(ball)) {
            freePlace = false;
        } else {
            for (MovingObject object : movingObjects) {

                if (object.getType().equals(MovingObjectType.BALL)) {
                    if (object.center().distance(ball.center()) < ((Ball) object).getRadius() + ball.getRadius()) {
                        freePlace = false;
                        break;
                    }
                }

            }
            for (Obstacle obstacle2 : obstacles) {
                if (BindsCheck.intersectsWithObstacle(ball, obstacle2)) {
                    freePlace = false;
                    break;
                }
            }
        }
        return freePlace;
    }


}

