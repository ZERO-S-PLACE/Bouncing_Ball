package org.zeros.bouncy_balls.Animation.InputOnRun;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation.Animation;

public abstract class InputOnRun {
    protected final AnchorPane panel;
    protected final EventHandler<MouseEvent> mouseInputHandler = this::onMouseClicked;
    protected final Animation animation;
    protected final EventHandler<MouseEvent> mouseMovedHandler = this::onMouseMoved;
    protected boolean centerPicked = false;
    protected Circle positionMarker;

    public InputOnRun(Animation animation, AnchorPane panel) {
        this.panel = panel;
        this.animation = animation;
    }

    public void insert() {
        configureMarkerAtCenterPick();
        panel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseInputHandler);
        panel.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
    }

    public void dismiss() {
        panel.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseInputHandler);
        panel.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
        Platform.runLater(() -> panel.getChildren().remove(positionMarker));
    }

    protected abstract void configureMarkerAtCenterPick();

    protected abstract void onMouseMoved(MouseEvent mouseEvent);

    protected abstract void onMouseClicked(MouseEvent mouseEvent);

    protected abstract void animateObjectArrival();


}
