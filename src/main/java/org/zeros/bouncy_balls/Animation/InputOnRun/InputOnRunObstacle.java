package org.zeros.bouncy_balls.Animation.InputOnRun;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Path;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

public class InputOnRunObstacle extends InputOnRun {
    private final Area obstacle;

    public InputOnRunObstacle(Area obstacle, Animation animation, AnchorPane panel) {
        super(animation, panel);
        this.obstacle = obstacle;
        obstacle.getPath().setFill(Properties.OBSTACLE_COLOR());
    }

    public void dismiss() {
        super.dismiss();
        Platform.runLater(() -> panel.getChildren().remove(obstacle.getPath()));
    }

    @Override
    protected void configureMarkerAtCenterPick() {
        obstacle.getPath().setOpacity(0.3);
        Platform.runLater(() -> panel.getChildren().add(obstacle.getPath()));
    }

    @Override
    protected void onMouseMoved(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR());
        if (!centerPicked) {
            obstacle.move(pickedPoint);
        } else {
            rotateInput(pickedPoint);
        }
    }

    private void rotateInput(Point2D pickedPoint) {
        Path path = obstacle.getPath();
        Point2D trackedPoint = obstacle.getCorners().getFirst();
        double rotation = calculatePickedRotation(pickedPoint, trackedPoint);
        obstacle.setRotation(rotation);

        Platform.runLater(() -> {
            panel.getChildren().remove(path);
            if (!panel.getChildren().contains(obstacle.getPath())) {
                panel.getChildren().add(obstacle.getPath());
                obstacle.getPath().setOpacity(path.getOpacity());
            }
        });
    }

    private double calculatePickedRotation(Point2D pickedPoint, Point2D trackedPoint) {
        double rotation = obstacle.getMassCenter().angle(trackedPoint, pickedPoint);
        rotation = rotation / 360 * 2 * Math.PI;
        LinearEquation line = new LinearEquation(obstacle.getMassCenter(), pickedPoint);
        if (line.distance(VectorMath.rotatePoint(trackedPoint, obstacle.getMassCenter(), rotation)) > line.distance(trackedPoint)) {
            rotation = -rotation;
        }
        return rotation;
    }

    @Override
    protected void onMouseClicked(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR());
        if (!centerPicked) {
            obstacle.move(pickedPoint);
            centerPicked = true;
        } else {
            rotateInput(pickedPoint);
            panel.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
            animateObjectArrival(obstacle.getPath());
        }
    }

    @Override
    protected boolean arrivalCondition() {
        return animation.hasFreePlace(obstacle);
    }
    @Override
    protected void onSucceededArrival() {
        animation.getLevel().addObstacle(obstacle);
        animation.getLevel().removeObstacleToAdd(obstacle);
        Platform.runLater(() -> panel.getChildren().add(obstacle.getPath()));
    }

}