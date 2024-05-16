package org.zeros.bouncy_balls.Animation.InputOnRun;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.zeros.bouncy_balls.Animation.Animation.Animation;

public abstract class InputOnRun {
    protected final AnchorPane panel;
    protected final EventHandler<MouseEvent> mouseInputHandler = this::onMouseClicked;
    protected final Animation animation;
    protected final EventHandler<MouseEvent> mouseMovedHandler = this::onMouseMoved;
    protected boolean centerPicked = false;

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
    }

    protected abstract void configureMarkerAtCenterPick();

    protected abstract void onMouseMoved(MouseEvent mouseEvent);

    protected abstract void onMouseClicked(MouseEvent mouseEvent);

    protected abstract void animateObjectArrival();


}