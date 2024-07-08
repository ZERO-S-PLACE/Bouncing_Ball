package org.zeros.bouncy_balls.Animation.Animation;

import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Animation.Borders.Borders;
import org.zeros.bouncy_balls.Animation.Borders.BordersType;
import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Bounce;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Model;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.util.TreeSet;

public class Animation {
    private final Level level;
    private final TreeSet<Double> timesElapsed = new TreeSet<>();
    private final LongProperty timeElapsedNanos = new SimpleLongProperty(0);
    private final ObjectProperty<GameState> gameState = new SimpleObjectProperty<>();



    private Borders borders;
    private int movingObjectCurrent;
    private String animationIdentifier;
    private long lastTimeMeasuredNanos = 0;

    public Animation(Level level) {
        this.level = level;
        borders = new Borders(level.PROPERTIES());
        setAnimationIdentifier(level.getNAME());
        Model.getInstance().addAnimation(this);
        for (MovingObject object : level.getMovingObjects()) {
            object.setAnimation(this);
        }
        gameState.set(GameState.LOADED);
    }
    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            animateThis(now);
        }
    };

    public void animate() {
        gameState.set(GameState.IN_PROGRESS);
        setFinalPositions();
        timer.start();
    }

    public void pause() {
        if (gameState.get().equals(GameState.IN_PROGRESS)) {
            gameState.set(GameState.PAUSED);
            lastTimeMeasuredNanos = 0;
            timer.stop();
        }
    }
    public void resume() {
        if (!gameState.get().equals(GameState.LOST) || !gameState.get().equals(GameState.LOST) && !gameState.get().equals(GameState.FINISHED)) {
            gameState.set(GameState.IN_PROGRESS);
            timer.start();
        }
    }

    private void animateThis(long now) {
        updateTimeElapsed(now);

        searchForBounces();

        setFinalPositions();
        checkEndConditions();
    }



    private void updateTimeElapsed(long now) {
        if (lastTimeMeasuredNanos != 0) {
            timeElapsedNanos.set(timeElapsedNanos.get() + (now - lastTimeMeasuredNanos));
        }
        lastTimeMeasuredNanos = now;
    }
    private void setFinalPositions() {
        for (MovingObject movingObject : level.getMovingObjects()) {
            movingObject.nextFrame();
        }
    }
    private void checkEndConditions() {
        if (timeElapsedNanos.get() > level.PROPERTIES().getTIME() * 1000000000 || allNeededEntered() || notAllowedEnter()) {
            timer.stop();
            setFinalState();
        }
    }

    private void setFinalState() {
        if (level.PROPERTIES().getTYPE().equals(AnimationType.SIMULATION)) {
            gameState.set(GameState.FINISHED);
        } else if (notAllowedEnter()) {
            gameState.set(GameState.LOST);
        } else if (level.getMovingObjectsHaveToEnter().isEmpty()) {
            gameState.set(GameState.WON);
        } else {
            gameState.set(GameState.LOST);
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
        movingObjectCurrent = 0;
        for (; movingObjectCurrent < level.getMovingObjects().size(); movingObjectCurrent++) {
            if (level.getMovingObjects().get(movingObjectCurrent).frameElapsed() <= frameElapsed) {

                if (crossesBorder()) {
                    timesElapsed.add(level.getMovingObjects().get(movingObjectCurrent).frameElapsed());
                } else if (bouncedAgainstObstacle()) {
                    timesElapsed.add(level.getMovingObjects().get(movingObjectCurrent).frameElapsed());
                } else if (bouncedByAnother(frameElapsed)) {
                    timesElapsed.add(level.getMovingObjects().get(movingObjectCurrent).frameElapsed());
                }
            }
        }
    }



    private boolean crossesBorder() {
        boolean temp = false;
        if (!borders.isInside(level.getMovingObjects().get(movingObjectCurrent).nextCenter(), level.getMovingObjects().get(movingObjectCurrent).getFurthestSpan())) {
            switch (level.PROPERTIES().getBOUNDARIES()) {
                case BordersType.BOUNCING -> {
                    if (level.getMovingObjects().get(movingObjectCurrent).getType().equals(MovingObjectType.BALL)) {
                        temp = borders.ballFromWall((Ball) level.getMovingObjects().get(movingObjectCurrent));
                    }
                }
                case BordersType.CONNECTED -> temp = borders.moveToOtherSide(level.getMovingObjects().get(movingObjectCurrent));
            }
        }
        return temp;
    }

    private boolean bouncedAgainstObstacle() {
        boolean temp = false;
        for (Area obstacle : level.getObstacles()) {
            if (BindsCheck.isInsideRoughBinds(level.getMovingObjects().get(movingObjectCurrent), obstacle)) {
                if (level.getMovingObjects().get(movingObjectCurrent).getType().equals(MovingObjectType.BALL)) {
                    if (BindsCheck.intersectsWithObstacleEdge(((Ball) level.getMovingObjects().get(movingObjectCurrent)), obstacle)) {
                        temp = Bounce.ballFromObstacle(((Ball) level.getMovingObjects().get(movingObjectCurrent)), obstacle);
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
            if (level.getMovingObjects().get(j).frameElapsed() <= frameElapsed && j != movingObjectCurrent) {
                double distance = level.getMovingObjects().get(movingObjectCurrent).nextCenter().distance(level.getMovingObjects().get(j).nextCenter());
                double minDistanceAllow = level.getMovingObjects().get(movingObjectCurrent).getFurthestSpan() + level.getMovingObjects().get(j).getFurthestSpan();
                Point2D intersection = level.getMovingObjects().get(movingObjectCurrent).trajectory().intersection(level.getMovingObjects().get(j).trajectory());
                boolean trajectoriesIntersect;
                if (intersection != null) {
                    trajectoriesIntersect = BindsCheck.isBetweenPoints(intersection, level.getMovingObjects().get(movingObjectCurrent).center(), level.getMovingObjects().get(movingObjectCurrent).nextCenter()) && BindsCheck.isBetweenPoints(intersection, level.getMovingObjects().get(j).center(), level.getMovingObjects().get(j).nextCenter());
                    if ((distance <= minDistanceAllow || trajectoriesIntersect)) {
                        Double t = Bounce.calculateTimeToCollision((Ball) level.getMovingObjects().get(movingObjectCurrent).clone(), (Ball) level.getMovingObjects().get(j).clone());
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
            return Bounce.twoBalls(((Ball) level.getMovingObjects().get(movingObjectCurrent)), ((Ball) level.getMovingObjects().get(closestObj)));
        }
        return false;
    }

    public boolean hasFreePlace(Ball ball) {

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

    public String getAnimationIdentifier() {
        return animationIdentifier;
    }

    public void setAnimationIdentifier(String animationIdentifier) {
        if (Model.getInstance().getRunningAnimations().isEmpty()) {
            this.animationIdentifier = animationIdentifier;
            Model.getInstance().addAnimation(this);
        } else {
            for (Animation animation1 : Model.getInstance().getRunningAnimations()) {
                if (animation1.getAnimationIdentifier().equals(animationIdentifier)) {
                    setAnimationIdentifier(animationIdentifier + "_1");
                    return;
                }
            }
            this.animationIdentifier = animationIdentifier;
        }
    }

    public LongProperty timeElapsedNanosProperty() {
        return timeElapsedNanos;
    }

    public ObjectProperty<GameState> gameStateProperty() {
        return gameState;
    }

    public long getTimeElapsedNanos() {
        return timeElapsedNanos.get();
    }

    public void reloadBorders() {
        borders = new Borders(level.PROPERTIES());
    }
    public Borders getBorders() {
        return borders;
    }




}

