package org.zeros.bouncy_balls.Animation.InputOnRun;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Path;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;

public class InputOnRunObstacle extends InputOnRun {
    private final Area obstacle;

    public InputOnRunObstacle(Area obstacle, Animation animation, AnchorPane panel) {
        super(animation, panel);
        this.obstacle = obstacle;
    }

    @Override
    protected void configureMarkerAtCenterPick() {
        obstacle.getPath().setOpacity(0.3);
        Platform.runLater(() -> panel.getChildren().add(obstacle.getPath()));

    }

    @Override
    protected void onMouseMoved(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        if (!centerPicked) {
            obstacle.move(pickedPoint);
        } else {
            rotateInput(pickedPoint);
        }
    }

    private void rotateInput(Point2D pickedPoint) {
        Path path = obstacle.getPath();
        Point2D trackedPoint = obstacle.getCorners().getFirst();
        double rotation = obstacle.getMassCenter().angle(trackedPoint, pickedPoint);
        rotation = rotation / 360 * 2 * Math.PI;
        LinearEquation line = new LinearEquation(obstacle.getMassCenter(), pickedPoint);
        if (line.distance(VectorMath.rotatePoint(trackedPoint, obstacle.getMassCenter(), rotation)) > line.distance(trackedPoint)) {
            rotation = -rotation;
        }

        obstacle.setRotation(rotation);
        if (!path.equals(obstacle.getPath())) {
            Platform.runLater(() -> {
                panel.getChildren().remove(path);
                if (!panel.getChildren().contains(obstacle.getPath())) {
                    panel.getChildren().add(obstacle.getPath());
                    obstacle.getPath().setOpacity(0.3);
                }

            });
        }
    }

    @Override
    protected void onMouseClicked(MouseEvent mouseEvent) {
        if (!centerPicked) {
            obstacle.move(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
            centerPicked = true;
        } else {
            rotateInput(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
            dismiss();
            new Thread(this::animateObjectArrival).start();
        }

    }


    @Override
    protected void animateObjectArrival() {
        for (int i = 0; i < 3; i++) {
            increaseOpacity();

            if (doesNotIntersectWithBall()) {
                obstacle.getPath().setOpacity(1);
                animation.getLevel().addObstacle(obstacle);
                Model.getInstance().getGamePanelController().addInputOnRun();
                return;
            }
            decreaseOpacity();
        }
        obstacle.move(new Point2D(-10000, -10000));
        Platform.runLater(() -> panel.getChildren().remove(obstacle.getPath()));

        animation.getLevel().addObstacleToAdd(obstacle);
        Model.getInstance().getGamePanelController().addInputOnRun();


    }

    private boolean doesNotIntersectWithBall() {
        boolean intersectsWithBall = false;
        for (MovingObject object : animation.getLevel().getMovingObjects()) {
            if (object.getType().equals(MovingObjectType.BALL)) {
                if (BindsCheck.intersectsWithObstacleExact((Ball) object, obstacle)) {
                    intersectsWithBall = true;
                }
            }
        }
        return !intersectsWithBall;
    }

    private void decreaseOpacity() {
        for (int i = 90; i > 10; i--) {
            obstacle.getPath().setOpacity((double) i / 100);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void increaseOpacity() {
        for (int i = 10; i < 90; i = i + 2) {
            obstacle.getPath().setOpacity((double) i / 100);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}







