package org.zeros.bouncy_balls.Animation;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Bounce;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;

import java.util.TreeSet;

public class Animation {

    public final Level level;
    private final Borders borders;
    private final TreeSet<Double> timesElapsed = new TreeSet<>();
    private int mObj1;
    private String name;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            animateThis();
        }

    };

    public Animation(Level level) {
        this.level=level;
        borders = new Borders(this);
        setName(level.getNAME());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(Model.getInstance().getRunningAnimations().isEmpty()){
            this.name=name;
            Model.getInstance().addAnimation(this);
        }else {
            for (Animation animation1:Model.getInstance().getRunningAnimations()){
                if(animation1.getName().equals(name)){
                    setName(name+"_1");
                    return;
                }
            }
            this.name=name;
        }
    }

    public void animate() {
        setFinalPositions();
        timer.start();
    }

    public void pause() {
        timer.stop();
    }

    public void addMovingObject(MovingObject movingObject) {
        level.movingObjects().add(movingObject);
    }

    public void addObstacle(Obstacle obstacle) {
        level.obstacles().add(obstacle);
    }

    public void removeMovingObject(MovingObject movingObject) {
        level.movingObjects().remove(movingObject);
    }

    public void removeObstacle(Obstacle obstacle) {
        level.obstacles().remove(obstacle);
    }


    private void animateThis() {

        searchForBounces();
        setFinalPositions();

    }

    private void searchForBounces() {
        singleBouncesCheck(0);
        int i = 0;
        while (!timesElapsed.isEmpty() && i < level.PROPERTIES().getMAX_EVALUATIONS()) {
            double timeElapsed = timesElapsed.getFirst();

            singleBouncesCheck(timeElapsed);


            while (Math.abs(timesElapsed.getFirst() - timeElapsed) < 0.03 + 0.98 / level.PROPERTIES().getMAX_EVALUATIONS() * i) {
                timesElapsed.removeFirst();
                if (timesElapsed.isEmpty()) {
                    break;
                }
            }

            i++;
        }


    }


    private void singleBouncesCheck(double frameElapsed) {
        mObj1 = 0;
        for (; mObj1 < level.movingObjects().size(); mObj1++) {
            if (level.movingObjects().get(mObj1).frameElapsed() <= frameElapsed) {

                 if (crossesBorder()) {
                    timesElapsed.add(level.movingObjects().get(mObj1).frameElapsed());

                }

                 else if (bouncedAgainstObstacle()) {
                    timesElapsed.add(level.movingObjects().get(mObj1).frameElapsed());
                }

                 else if (bouncedByAnother(frameElapsed)) {
                    timesElapsed.add(level.movingObjects().get(mObj1).frameElapsed());
                }



            }
        }
    }


    private void setFinalPositions() {
        for (MovingObject movingObject : level.movingObjects()) {
            movingObject.nextFrame();
        }
    }

    private boolean crossesBorder() {
        boolean temp = false;
        if (!borders.isInside(level.movingObjects().get(mObj1).nextCenter(), level.movingObjects()
                .get(mObj1).getFurthestSpan())) {
            switch (level.PROPERTIES().getBOUNDARIES()) {
                case BordersType.BOUNCING -> {
                    if (level.movingObjects().get(mObj1).getType().equals(MovingObjectType.BALL)) {
                        temp = borders.ballFromWall((Ball) level.movingObjects().get(mObj1));
                    }
                }
                case BordersType.CONNECTED -> temp = borders.moveToOtherSide(level.movingObjects().get(mObj1));


            }
        }
        return temp;
    }

    private boolean bouncedAgainstObstacle() {
        boolean temp = false;
        for (Obstacle obstacle : level.obstacles()) {
            if (BindsCheck.isInsideRoughBinds(level.movingObjects().get(mObj1), obstacle)) {
                if (level.movingObjects().get(mObj1).getType().equals(MovingObjectType.BALL)) {
                    if (BindsCheck.intersectsWithObstacle(((Ball) level.movingObjects().get(mObj1)), obstacle)) {
                        temp = Bounce.ballFromObstacle(((Ball) level.movingObjects().get(mObj1)), obstacle);
                    }
                }
            }

        }

        return temp;
    }


    private boolean bouncedByAnother(double frameElapsed) {
        boolean temp = false;
        int closestObj;
        double minDistanceMeasured = Double.MAX_VALUE;

        for (int j = 0; j < level.movingObjects().size(); j++) {
            if (level.movingObjects().get(j).frameElapsed() <= frameElapsed&&j!=mObj1) {
                double distance = level.movingObjects().get(mObj1).nextCenter().distance(level.movingObjects().get(j).nextCenter());
                double minDistanceAllow = level.movingObjects().get(mObj1).getFurthestSpan() + level.movingObjects().get(j).getFurthestSpan();
                Point2D intersection = level.movingObjects().get(mObj1).trajectory().intersection(level.movingObjects().get(j).trajectory());
                boolean trajectoriesIntersect;
                if (intersection != null) {
                    trajectoriesIntersect = BindsCheck.isBetweenPoints(
                            intersection, level.movingObjects().get(mObj1).center(), level.movingObjects().get(mObj1).nextCenter())
                            && BindsCheck.isBetweenPoints(intersection, level.movingObjects().get(j).center(), level.movingObjects().get(j).nextCenter());
                    if ((distance <= minDistanceAllow || trajectoriesIntersect)) {
                        closestObj = j;
                        temp = Bounce.twoBalls(((Ball) level.movingObjects().get(mObj1)), ((Ball) level.movingObjects().get(closestObj)));

                    }
                }

            }
        }

        return temp;
    }



    public boolean hasFreePlace(Ball ball) {

        if (!borders.isInside(ball)) {
            return false;
        } else {
            for (MovingObject object : level.movingObjects()) {

                if (object.getType().equals(MovingObjectType.BALL)&&!object.equals(ball)) {
                    if (object.center().distance(ball.center()) <= ((Ball) object).getRadius() + ball.getRadius()) {
                        return false;
                    }
                }

            }
            for (Obstacle obstacle2 : level.obstacles()) {
                if (BindsCheck.intersectsWithObstacle(ball, obstacle2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public AnimationProperties getPROPERTIES() {
        return level.PROPERTIES();
    }

    public Borders getBorders() {
        return borders;
    }

}

