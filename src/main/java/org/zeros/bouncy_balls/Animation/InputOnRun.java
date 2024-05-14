package org.zeros.bouncy_balls.Animation;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;

public class InputOnRun {

    private final AnchorPane panel;
    private final int clicksCount = 0;    private final EventHandler<MouseEvent> mouseInputHandler = this::getMouseInput;
    private Circle positionMarker;
    private final EventHandler<MouseEvent> mouseMovedHandler = this::onMouseMoved;
    private final MovingObject object;
    private final Animation animation;
    public InputOnRun(MovingObject object, AnchorPane panel, Animation animation) {
        this.panel = panel;
        this.object = object;
        this.animation = animation;
        configureMarker();

        panel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseInputHandler);
        panel.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
    }

    private void configureMarker() {
        positionMarker = new Circle(3);
        positionMarker.setFill(Color.TRANSPARENT);
        positionMarker.setStroke(new Color(0, 0, 0, 0.3));
        positionMarker.setStrokeWidth(1);
        panel.getChildren().add(positionMarker);
    }

    private void onMouseMoved(MouseEvent mouseEvent) {
        positionMarker.setCenterX(mouseEvent.getX());
        positionMarker.setCenterY(mouseEvent.getY());
    }

    private void getMouseInput(MouseEvent mouseEvent) {
        panel.getChildren().remove(positionMarker);
        object.updateCenter(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
        object.updateNextCenter(object.center());

        if (animation.hasFreePlace((Ball) object)) {
            animation.getLevel().movingObjects().add(object);
            panel.getChildren().add(object.getShape());
        } else {
            animation.getLevel().movingObjectsToAdd().addFirst(object);
        }
        panel.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseInputHandler);
        panel.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
        Model.getInstance().getGamePanelController().addInputOnRun();

    }



}
