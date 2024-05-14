package org.zeros.bouncy_balls.Animation.InputOnRun;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;

public class InputOnRunMovingObject extends InputOnRun {

    private final Circle[] trajectoryMarkers = new Circle[10];
    private final MovingObject object;
    private Circle velocityMarker;
    private int spacing = 8;

    public InputOnRunMovingObject(MovingObject object, AnchorPane panel) {
        super(object.getAnimation(),panel);
        this.object = object;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public void insert() {
        configureMarkerAtCenterPick();
        panel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseInputHandler);
        panel.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
    }
    @Override
    public void dismiss() {
        super.dismiss();
        Platform.runLater(() -> {
            panel.getChildren().remove(velocityMarker);
            panel.getChildren().removeAll(trajectoryMarkers);
        });
    }



    @Override
    protected void configureMarkerAtCenterPick() {
        if (object.getType().equals(MovingObjectType.BALL)) {
            positionMarker = new Circle(-10000, -10000, object.getFurthestSpan());
            positionMarker.setFill(object.getShape().getFill());
            positionMarker.setOpacity(0.3);
        }
        Platform.runLater(() -> panel.getChildren().add(positionMarker));
    }
    private void configureMarkersAtVelocityPick() {
        velocityMarker = new Circle(3);
        velocityMarker.setFill(Color.TRANSPARENT);
        velocityMarker.setStroke(new Color(0, 0, 0, 0.5));
        velocityMarker.setStrokeWidth(2);
        for (int i = 0; i < trajectoryMarkers.length; i++) {
            trajectoryMarkers[i] = new Circle(object.getFurthestSpan() / 3);
            trajectoryMarkers[i].setFill(Color.TRANSPARENT);
            trajectoryMarkers[i].setStroke(new Color(0.3, 0.3, 0.3, 0.3));
            trajectoryMarkers[i].setStrokeWidth(0.5);
        }
        Platform.runLater(() -> panel.getChildren().add(velocityMarker));
        Platform.runLater(() -> panel.getChildren().addAll(trajectoryMarkers));
    }

    @Override
    protected void onMouseMoved(MouseEvent mouseEvent) {
        if (!centerPicked) {
            positionMarker.setCenterX(mouseEvent.getX());
            positionMarker.setCenterY(mouseEvent.getY());
        } else {
            velocityMarker.setCenterX(mouseEvent.getX());
            velocityMarker.setCenterY(mouseEvent.getY());
            Ball ball = new Ball(object.getFurthestSpan(), animation);
            ball.updateCenter(object.center());
            ball.updateNextCenter(object.center());
            ball.setInitialVelocity(object.center().subtract(new Point2D(mouseEvent.getX(), mouseEvent.getY())));

            for (int i = 0; i < trajectoryMarkers.length; i++) {
                for (int j = 0; j < spacing + 2 * i; j++) {
                    ball.nextFrame();
                }
                if (ball.frameVelocity().magnitude() > 1) {
                    trajectoryMarkers[i].setRadius(ball.frameVelocity().magnitude());
                } else {
                    trajectoryMarkers[i].setRadius(1.5);
                }
                trajectoryMarkers[i].setCenterX(ball.center().getX());
                trajectoryMarkers[i].setCenterY(ball.center().getY());
            }

        }
    }

    @Override
    protected void onMouseClicked(MouseEvent mouseEvent) {
        if (!centerPicked) {
            object.updateCenter(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
            object.updateNextCenter(object.center());

            if (animation.hasFreePlace((Ball) object)) {
                positionMarker.setCenterX(mouseEvent.getX());
                positionMarker.setCenterY(mouseEvent.getY());
                centerPicked = true;
                configureMarkersAtVelocityPick();
            }
        } else {
            object.updateVelocity(object.center().subtract(new Point2D(mouseEvent.getX(), mouseEvent.getY())), 1);
            dismiss();
            new Thread(this::animateObjectArrival).start();

        }

    }


    @Override
    protected void animateObjectArrival() {
        Platform.runLater(() ->
            panel.getChildren().add(object.getShape()));
        while (true) {
            if (animation.hasFreePlace((Ball) object)) {
                animation.getLevel().movingObjects().add(object);
                Model.getInstance().getGamePanelController().addInputOnRun();
                return;
            }
            for (int i = 0; i < 20; i++) {
                object.getShape().setOpacity((double) i / 20);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }




}
