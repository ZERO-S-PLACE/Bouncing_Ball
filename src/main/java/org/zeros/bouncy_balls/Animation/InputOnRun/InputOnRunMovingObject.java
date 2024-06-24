package org.zeros.bouncy_balls.Animation.InputOnRun;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;

public class InputOnRunMovingObject extends InputOnRun {

    private final Circle[] trajectoryMarkers = new Circle[10];
    private final MovingObject object;
    private Circle velocityMarker;
    private Circle positionMarker;


    public InputOnRunMovingObject(MovingObject object, AnchorPane panel) {
        super(object.getAnimation(), panel);
        this.object = object;
        BackgroundImages.setBallStandardBackground(object.getShape());
    }


    @Override
    public void dismiss() {

        Platform.runLater(() -> {
            panel.getChildren().remove(velocityMarker);
            panel.getChildren().removeAll(trajectoryMarkers);
            positionMarker.setOpacity(1);
        });
        super.dismiss();

    }


    @Override
    protected void configureMarkerAtCenterPick() {
        if (object.getType().equals(MovingObjectType.BALL)) {
            positionMarker = (Circle) object.getShape();
            positionMarker.setOpacity(0.3);
            if(animation.getLevel().getMovingObjectsCannotEnter().contains(object)) {
                BackgroundImages.setBallCannotEnterBackground(positionMarker);
            }else if(animation.getLevel().getMovingObjectsHaveToEnter().contains(object)) {
                BackgroundImages.setBallHaveToEnterBackground(positionMarker);
            }else {
                BackgroundImages.setBallStandardBackground(positionMarker);
            }
        }
        Platform.runLater(() -> panel.getChildren().add(positionMarker));
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
            if (AreasMath.isInsideArea(animation.getLevel().getInputArea(), pickedPoint) ){
                positionMarker.setVisible(true);
                positionMarker.setCenterX(mouseEvent.getX());
                positionMarker.setCenterY(mouseEvent.getY());
            }else {
                positionMarker.setVisible(false);
            }
        } else {
            velocityMarker.setCenterX(mouseEvent.getX());
            velocityMarker.setCenterY(mouseEvent.getY());
            Ball ball = new Ball(object.getFurthestSpan(), animation);
            ball.updateCenter(object.center());
            ball.updateNextCenter(object.center());
            ball.setInitialVelocity(object.center().subtract(new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR())));

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
    }

    @Override
    protected void onMouseClicked(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR());
        if (!AreasMath.isInsideArea(animation.getLevel().getInputArea(), pickedPoint) && !centerPicked) return;
        if (!centerPicked) {
            object.updateCenter(pickedPoint);
            object.updateNextCenter(object.center());
            if (animation.hasFreePlace((Ball) object)) {
                object.getShape().setOpacity(0.7);
                positionMarker.setCenterX(mouseEvent.getX());
                positionMarker.setCenterY(mouseEvent.getY());
                centerPicked = true;
                configureMarkersAtVelocityPick();
            }
        } else {
            object.updateVelocity(object.center().subtract(new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR())), 1);
            animateObjectArrival();

        }
    }
    @Override
    protected void animateObjectArrival() {
        for (int i = 0; i < 4; i++) {
            increaseOpacity();
            if (animation.hasFreePlace((Ball) object)) {
                animation.getLevel().addMovingObject(object);
                animation.getLevel().removeMovingObjectToAdd(object);
                dismiss();
                return;
            }
            decreaseOpacity();
        }
        Platform.runLater(() -> panel.getChildren().remove(object.getShape()));
        object.updateCenter(new Point2D(-10000, -10000));

        dismiss();
    }

    private void decreaseOpacity() {
        for (int i = 90; i > 10; i--) {
            object.getShape().setOpacity((double) i / 100);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void increaseOpacity() {
        for (int i = 10; i < 90; i = i + 2) {
            object.getShape().setOpacity((double) i / 100);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
