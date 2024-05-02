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
import java.util.TreeSet;

public class Animation {
    public static Animation getInstance(){
        if(animation==null){
            animation=new Animation();
        }
        return animation;
    }
    private Animation(){}
    private static Animation animation;
    private final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int mObj1;
    private int maxEval=50;

    private final TreeSet<Double> timesElapsed=new TreeSet<>();
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
        searchForBounces();
        setCalculatedFinalCenters();
    }

    private void searchForBounces() {
        singleBouncesCheck(0);
        int i=0;
        while (!timesElapsed.isEmpty()&&i<maxEval){
            double timeElapsed=timesElapsed.getFirst();

                singleBouncesCheck(timeElapsed);


                while (Math.abs(timesElapsed.getFirst() - timeElapsed) < 0.03+0.98/maxEval*i) {
                    timesElapsed.removeFirst();
                    if(timesElapsed.isEmpty()){
                        break;
                    }
                }

            i++;
        }



    }

    private void singleBouncesCheck(double frameElapsed) {
        mObj1 = 0;
        for (; mObj1 < movingObjects.size(); mObj1++) {
            if (movingObjects.get(mObj1).frameElapsed() <= frameElapsed) {
                if (bouncesAgainstBorder()) {
                    timesElapsed.add(movingObjects.get(mObj1).frameElapsed());
                }
                if (bouncedAgainstObstacle()) {
                    timesElapsed.add(movingObjects.get(mObj1).frameElapsed());
                }
                 else if (bouncedByAnother(frameElapsed)) {
                    timesElapsed.add(movingObjects.get(mObj1).frameElapsed());
                }
            }
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
        return temp;
    }

    private boolean bouncedAgainstObstacle() {
        boolean temp = false;
        for (Obstacle obstacle : obstacles) {
            if(BindsCheck.isInsideRoughBinds(movingObjects.get(mObj1),obstacle)) {
                if (movingObjects.get(mObj1).getType().equals(MovingObjectType.BALL)) {
                    if (BindsCheck.intersectsWithObstacle(((Ball) movingObjects.get(mObj1)), obstacle)) {
                        temp = Bounce.ballFromObstacle(((Ball) movingObjects.get(mObj1)), obstacle);
                    }
                   /* if (BindsCheck.intersectsWithObstacle(((Ball) movingObjects.get(mObj1)), obstacle)) {
                        pause();
                        movingObjects.get(mObj1).getShape().fillProperty().set(Color.BLUE);
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        animate();
                    }
                    */


                }
           }

        }

        return temp;
    }


    private boolean bouncedByAnother(double frameElapsed) {
        boolean temp = false;
        int closestObj=-1;
        double minDistanceMeasured=Double.MAX_VALUE;

        for (int j = 0; j < movingObjects.size(); j++) {
            if (movingObjects.get(j).frameElapsed() <= frameElapsed) {
                double distance = movingObjects.get(mObj1).nextCenter().distance(movingObjects.get(j).nextCenter());
                double minDistanceAllow = movingObjects.get(mObj1).getFurthestSpan() + movingObjects.get(j).getFurthestSpan();
                Point2D intersection = movingObjects.get(mObj1).trajectory().intersection(movingObjects.get(j).trajectory());
                boolean trajectoriesIntersect;
                if (intersection != null) {
                    trajectoriesIntersect = BindsCheck.isBetweenPoints(intersection, movingObjects.get(mObj1).center(), movingObjects.get(mObj1).nextCenter()) &&
                            BindsCheck.isBetweenPoints(intersection, movingObjects.get(j).center(), movingObjects.get(j).nextCenter());


                    if ((distance <= minDistanceAllow || trajectoriesIntersect)) {
                        closestObj = j;
                        temp = Bounce.twoBalls(((Ball) movingObjects.get(mObj1)), ((Ball) movingObjects.get(closestObj)));

                    }
                }

            }
        }

        return temp;
    }

    public boolean hasFreePlace(Ball ball) {

        if (!BindsCheck.isInsideBorders(ball)) {
            return false;
        } else {
            for (MovingObject object : movingObjects) {

                if (object.getType().equals(MovingObjectType.BALL)) {
                    if (object.center().distance(ball.center()) <= ((Ball) object).getRadius() + ball.getRadius()) {
                       return false;
                    }
                }

            }
            for (Obstacle obstacle2 : obstacles) {
                if (BindsCheck.intersectsWithObstacle(ball,obstacle2)) {
                    return false;
                }
            }
        }
        return true;
    }


}

