package org.zeros.bouncy_balls.Animation.Animation;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Animation.Borders.Borders;
import org.zeros.bouncy_balls.Animation.Borders.BordersType;
import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Bounce;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.util.TreeSet;

public class Animation {
    private final Level level;
    private final TreeSet<Double> timesElapsed = new TreeSet<>();
    private Borders borders;
    private int mObj1;
    private String name;




    private final LongProperty timeElapsedNanos=new SimpleLongProperty(0);
    private final ObjectProperty<GameState> gameState=new SimpleObjectProperty<>();

    public Animation(Level level) {
        this.level = level;
        borders = new Borders(this);
        setName(level.getNAME());
        Model.getInstance().addAnimation(this);
        for (MovingObject object : level.getMovingObjects()) {
            object.setAnimation(this);
        }
        gameState.set(GameState.LOADED);
    }

    public void animate() {
        gameState.set(GameState.IN_PROGRESS);
        setFinalPositions();
        timer.start();
    }

    public void reloadBorders() {
        borders = new Borders(this);
    }

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            animateThis(now);
        }
    };

    public void pause() {
        gameState.set(GameState.PAUSED);
        previousTime=0;
        timer.stop();
    }
    public void resume(){
        gameState.set(GameState.IN_PROGRESS);
        timer.start();}
    private long previousTime=0;

    private void animateThis(long now) {
        if(previousTime!=0) {
            timeElapsedNanos.set(timeElapsedNanos.get() +(now-previousTime));
        }
        previousTime=now;
        searchForBounces();
        setFinalPositions();

        if (timeElapsedNanos.get() > level.PROPERTIES().getTIME()*1000000000 || allNeededEntered() || notAllowedEnter()) {
            setFinalState();
        }
    }

    private void setFinalState() {
        pause();
        if(notAllowedEnter()){
            gameState.set(GameState.LOST);
        }else if(level.getMovingObjectsHaveToEnter().isEmpty()){
            gameState.set(GameState.WON);
        }
    }

    private boolean notAllowedEnter() {
        if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
            for (MovingObject object : level.getMovingObjectsCannotEnter()) {
                if (AreasMath.isInsideArea(level.getTargetArea(), object.center())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean allNeededEntered() {
        if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME) && !level.getMovingObjectsHaveToEnter().isEmpty()) {
            for (MovingObject object : level.getMovingObjectsHaveToEnter()) {
                if (AreasMath.isInsideArea(level.getTargetArea(), object.center())) {
                    level.getMovingObjects().remove(object);
                    level.getMovingObjectsHaveToEnter().remove(object);
                    object.getShape().setVisible(false);
                }
            }
            return level.getMovingObjectsHaveToEnter().isEmpty();
        }
        return false;
    }

    private void searchForBounces() {
        singleBouncesCheck(0);
        int i = 0;
        while (!timesElapsed.isEmpty() && i < Properties.MAX_EVALUATIONS()) {

            double timeElapsed = timesElapsed.getFirst();

            singleBouncesCheck(timeElapsed);

            while (Math.abs(timesElapsed.getFirst() - timeElapsed) < 0.03 + 0.98 / Properties.MAX_EVALUATIONS() * i) {
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
        for (; mObj1 < level.getMovingObjects().size(); mObj1++) {
            if (level.getMovingObjects().get(mObj1).frameElapsed() <= frameElapsed) {

                if (crossesBorder()) {

                    timesElapsed.add(level.getMovingObjects().get(mObj1).frameElapsed());

                } else if (bouncedAgainstObstacle()) {

                    timesElapsed.add(level.getMovingObjects().get(mObj1).frameElapsed());

                } else if (bouncedByAnother(frameElapsed)) {

                    timesElapsed.add(level.getMovingObjects().get(mObj1).frameElapsed());
                }


            }
        }
    }

    private void setFinalPositions() {
        for (MovingObject movingObject : level.getMovingObjects()) {
            movingObject.nextFrame();
        }
    }

    private boolean crossesBorder() {
        boolean temp = false;
        if (!borders.isInside(level.getMovingObjects().get(mObj1).nextCenter(), level.getMovingObjects().get(mObj1).getFurthestSpan())) {
            switch (level.PROPERTIES().getBOUNDARIES()) {
                case BordersType.BOUNCING -> {
                    if (level.getMovingObjects().get(mObj1).getType().equals(MovingObjectType.BALL)) {
                        temp = borders.ballFromWall((Ball) level.getMovingObjects().get(mObj1));
                    }
                }
                case BordersType.CONNECTED -> temp = borders.moveToOtherSide(level.getMovingObjects().get(mObj1));


            }
        }
        return temp;
    }

    private boolean bouncedAgainstObstacle() {
        boolean temp = false;
        for (Area obstacle : level.getObstacles()) {
            if (BindsCheck.isInsideRoughBinds(level.getMovingObjects().get(mObj1), obstacle)) {
                if (level.getMovingObjects().get(mObj1).getType().equals(MovingObjectType.BALL)) {
                    if (BindsCheck.intersectsWithObstacleEdge(((Ball) level.getMovingObjects().get(mObj1)), obstacle)) {
                        temp = Bounce.ballFromObstacle(((Ball) level.getMovingObjects().get(mObj1)), obstacle);
                    }
                }
            }

        }

        return temp;
    }

    private boolean bouncedByAnother(double frameElapsed) {

        int closestObj = -1;
        double shortestTime = Double.MAX_VALUE;

        for (int j = 0; j < level.getMovingObjects().size(); j++) {
            if (level.getMovingObjects().get(j).frameElapsed() <= frameElapsed && j != mObj1) {
                double distance = level.getMovingObjects().get(mObj1).nextCenter().distance(level.getMovingObjects().get(j).nextCenter());
                double minDistanceAllow = level.getMovingObjects().get(mObj1).getFurthestSpan() + level.getMovingObjects().get(j).getFurthestSpan();
                Point2D intersection = level.getMovingObjects().get(mObj1).trajectory().intersection(level.getMovingObjects().get(j).trajectory());
                boolean trajectoriesIntersect;
                if (intersection != null) {
                    trajectoriesIntersect = BindsCheck.isBetweenPoints(intersection, level.getMovingObjects().get(mObj1).center(), level.getMovingObjects().get(mObj1).nextCenter()) && BindsCheck.isBetweenPoints(intersection, level.getMovingObjects().get(j).center(), level.getMovingObjects().get(j).nextCenter());
                    if ((distance <= minDistanceAllow || trajectoriesIntersect)) {
                        Double t = Bounce.calculateTimeToCollision((Ball) level.getMovingObjects().get(mObj1).clone(), (Ball) level.getMovingObjects().get(j).clone());
                        if (t != null) {
                            if (t < shortestTime) {
                                closestObj = j;
                                shortestTime = t;
                            }
                        }
                    }

                }
            }
        }
        if (closestObj >= 0) {
            return Bounce.twoBalls(((Ball) level.getMovingObjects().get(mObj1)), ((Ball) level.getMovingObjects().get(closestObj)));
        }
        return false;
    }

    public boolean hasFreePlace(Ball ball) {

        if (!borders.isInside(ball)) {
            return false;
        } else {
            for (MovingObject object : level.getMovingObjects()) {
                if (object.getType().equals(MovingObjectType.BALL) && !object.equals(ball)) {
                    if (object.center().distance(ball.center()) <= ((Ball) object).getRadius() + ball.getRadius()) {
                        return false;
                    }
                }
            }
            for (Area obstacle2 : level.getObstacles()) {
                if (BindsCheck.intersectsWithObstacleExact(ball, obstacle2)) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean hasFreePlace(Area obstacle) {

        if (!borders.isInside(obstacle)) return false;
        else if (intersectsWithBall(obstacle)) return false;
        else {
            for (Area obstacle2 : level.getObstacles()) {
                if (AreasMath.areasIntersect(obstacle, obstacle2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean intersectsWithBall(Area obstacle) {
        boolean intersectsWithBall = false;
        for (MovingObject object : this.getLevel().getMovingObjects()) {
            if (object.getType().equals(MovingObjectType.BALL)) {
                if (BindsCheck.intersectsWithObstacleExact((Ball) object, obstacle)) {
                    intersectsWithBall = true;
                }
            }
        }
        return intersectsWithBall;
    }

    public AnimationProperties getPROPERTIES() {
        return level.PROPERTIES();
    }

    public Level getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
    public LongProperty timeElapsedNanosProperty() {
        return timeElapsedNanos;
    }

    public ObjectProperty<GameState> gameStateProperty() {
        return gameState;
    }

    public void setName(String name) {
        if (Model.getInstance().getRunningAnimations().isEmpty()) {
            this.name = name;
            Model.getInstance().addAnimation(this);
        } else {
            for (Animation animation1 : Model.getInstance().getRunningAnimations()) {
                if (animation1.getName().equals(name)) {
                    setName(name + "_1");
                    return;
                }
            }
            this.name = name;
        }
    }

}

