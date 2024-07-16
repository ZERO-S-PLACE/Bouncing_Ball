package org.zeros.bouncy_balls.Animation.InputOnRun;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;

public class InputOnRunMovingObject extends InputOnRun {
    private final Circle[] trajectoryMarkers = new Circle[10];
    private final MovingObject object;
    private final Point2D maxVelocity;
    private Circle velocityMarker;

    public InputOnRunMovingObject(MovingObject object, AnchorPane panel) {
        super(object.getAnimation(), panel);
        this.object = object;
        maxVelocity = object.velocity();
        BackgroundImages.setBallStandardBackground(object.getShape());
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Platform.runLater(() -> {
            panel.getChildren().remove(object.getShape());
            panel.getChildren().remove(velocityMarker);
            panel.getChildren().removeAll(trajectoryMarkers);
        });
    }

    @Override
    protected void configureMarkerAtCenterPick() {
        if (object.getType().equals(MovingObjectType.BALL)) {
            object.getShape().setOpacity(0.3);
            if (animation.getLevel().getMovingObjectsCannotEnter().contains(object)) {
                BackgroundImages.setBallCannotEnterBackground(object.getShape());
            } else if (animation.getLevel().getMovingObjectsHaveToEnter().contains(object)) {
                BackgroundImages.setBallHaveToEnterBackground(object.getShape());
            } else {
                BackgroundImages.setBallStandardBackground(object.getShape());
            }
        }
        Platform.runLater(() -> panel.getChildren().add(object.getShape()));
    }

    private void configureMarkersAtVelocityPick() {
        velocityMarker = new Circle(3);
        velocityMarker.setFill(Color.TRANSPARENT);
        velocityMarker.setStroke(Color.web("#A6D4ED"));
        velocityMarker.setStrokeWidth(2);
        for (int i = 0; i < trajectoryMarkers.length; i++) {
            trajectoryMarkers[i] = new Circle(object.getFurthestSpan() / 3);
            trajectoryMarkers[i].setFill(Color.TRANSPARENT);
            trajectoryMarkers[i].setStroke(Color.web("#A6D4ED"));
            trajectoryMarkers[i].setStrokeWidth(0.5);
        }
        Platform.runLater(() -> panel.getChildren().add(velocityMarker));
        Platform.runLater(() -> panel.getChildren().addAll(trajectoryMarkers));
    }

    @Override
    protected void onMouseMoved(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR());
        if (!centerPicked) {
            moveObject(mouseEvent, pickedPoint);
        } else {
            moveVelocityMarker(mouseEvent);
        }
    }

    private void moveVelocityMarker(MouseEvent mouseEvent) {
        velocityMarker.setCenterX(mouseEvent.getX());
        velocityMarker.setCenterY(mouseEvent.getY());
        Ball ball = new Ball(object.getFurthestSpan(), animation);
        ball.updateCenter(object.center());
        ball.updateNextCenter(object.center());
        ball.setInitialVelocity(getPickedVelocity(mouseEvent));
        updateTrajectoryMarkers(ball);
    }

    private void moveObject(MouseEvent mouseEvent, Point2D pickedPoint) {
        if (AreasMath.isInsideArea(animation.getLevel().getInputArea(), pickedPoint)) {
            object.getShape().setVisible(true);
            if (object.getType().equals(MovingObjectType.BALL)) {
                ((Circle) object.getShape()).setCenterX(mouseEvent.getX());
                ((Circle) object.getShape()).setCenterY(mouseEvent.getY());
            }
        } else {
            object.getShape().setVisible(false);
        }
    }

    private void updateTrajectoryMarkers(Ball ball) {
        for (int i = 0; i < trajectoryMarkers.length; i++) {
            int spacing = 8;
            for (int j = 0; j < spacing + 2 * i; j++) {
                ball.nextFrame();
            }
            if (ball.frameVelocity().magnitude() > 1) {
                trajectoryMarkers[i].setRadius(ball.frameVelocity().magnitude() / Properties.SIZE_FACTOR());
            } else {
                trajectoryMarkers[i].setRadius(1.5);
            }
            trajectoryMarkers[i].setCenterX(ball.center().getX() / Properties.SIZE_FACTOR());
            trajectoryMarkers[i].setCenterY(ball.center().getY() / Properties.SIZE_FACTOR());
        }
    }

    private Point2D getPickedVelocity(MouseEvent mouseEvent) {
        Point2D velocity = object.center().subtract(new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR()));
        if (velocity.magnitude() > maxVelocity.magnitude()) {
            velocity = velocity.multiply(maxVelocity.magnitude() / velocity.magnitude());
        }
        return velocity;
    }

    @Override
    protected void onMouseClicked(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR());
        if (!AreasMath.isInsideArea(animation.getLevel().getInputArea(), pickedPoint) && !centerPicked) return;
        if (!centerPicked) {
            object.updateCenter(pickedPoint);
            object.updateNextCenter(object.center());
            if (animation.hasFreePlace((Ball) (object))) {
                object.getShape().setOpacity(0.7);
                ((Circle) object.getShape()).setCenterX(mouseEvent.getX());
                ((Circle) object.getShape()).setCenterY(mouseEvent.getY());
                centerPicked = true;
                configureMarkersAtVelocityPick();
            }
        } else {
            object.updateVelocity(getPickedVelocity(mouseEvent), 1);
            object.getShape().setOpacity(1);
            animateObjectArrival(object.getShape());
        }
    }

    @Override
    protected boolean arrivalCondition() {
        return animation.hasFreePlace((Ball) object);
    }

    @Override
    protected void onSucceededArrival() {
        animation.getLevel().addMovingObject(object);
        animation.getLevel().removeMovingObjectToAdd(object);
        Platform.runLater(() -> panel.getChildren().add(object.getShape()));
    }
}
