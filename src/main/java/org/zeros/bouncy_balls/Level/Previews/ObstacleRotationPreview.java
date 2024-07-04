package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

public class ObstacleRotationPreview extends Preview {

    @Override
    public void start() {

    }

    @Override
    public void remove() {

    }

   /* @Override
    protected void onMouseMoved(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR());
        if (!centerPicked) {
            obstacle.move(pickedPoint);
        } else {
            rotateInput(pickedPoint,obstacle,panel);
        }
    }

    private static void rotateInput(Point2D pickedPoint, Area obstacle, Pane panel) {
        Path path = obstacle.getPath();
        double rotation = calculatePickedRotation(pickedPoint, obstacle);
        obstacle.setRotation(rotation);
        Platform.runLater(() -> {
            panel.getChildren().remove(path);
            if (!panel.getChildren().contains(obstacle.getPath())) {
                panel.getChildren().add(obstacle.getPath());
                obstacle.getPath().setOpacity(path.getOpacity());
            }
        });
    }

    private static double calculatePickedRotation(Point2D pickedPoint, Area obstacle) {
        Point2D trackedPoint = obstacle.getCorners().getFirst();
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
            rotateInput(pickedPoint,obstacle,panel);
            panel.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
            animateObjectArrival(obstacle.getPath());
        }
    }*/


}
