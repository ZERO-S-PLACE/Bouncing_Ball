package org.zeros.bouncy_balls.Animation.InputOnRun;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.zeros.bouncy_balls.Animation.Animation.Animation;

public abstract class InputOnRun {

    protected final AnchorPane panel;
    protected final EventHandler<MouseEvent> mouseInputHandler = this::onMouseClicked;
    protected final Animation animation;
    protected final EventHandler<MouseEvent> mouseMovedHandler = this::onMouseMoved;
    protected final BooleanProperty finished = new SimpleBooleanProperty(false);
    protected final long waitForFreePlaceNanos = 2_500_000_000L;
    protected final int brightnessCyclesOnWait = 3;
    protected boolean centerPicked = false;
    protected AnimationTimer animationTimer;
    private boolean animationIsRunning = false;

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
        if (animationIsRunning) {
            animationTimer.stop();
        } else {
            panel.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseInputHandler);
            panel.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
            finishedProperty().setValue(true);
        }
    }

    protected abstract void configureMarkerAtCenterPick();

    protected abstract void onMouseMoved(MouseEvent mouseEvent);

    protected abstract void onMouseClicked(MouseEvent mouseEvent);

    protected void animateObjectArrival(Node node) {
        animationTimer = new AnimationTimer() {
            private long startTime = 0;
            private boolean succeeded = false;
            private int cycle = 0;

            @Override
            public void handle(long now) {
                if (startTime == 0) {
                    animationIsRunning = true;
                    startTime = now;
                }
                long timeElapsed = now - startTime;
                if (timeElapsed < waitForFreePlaceNanos) {
                    long cycleTime = waitForFreePlaceNanos / brightnessCyclesOnWait;
                    long currentCycleTime = now - startTime - cycle * cycleTime;
                    if (currentCycleTime > cycleTime) {
                        cycle++;
                        currentCycleTime = currentCycleTime - cycleTime;
                    }
                    double brightnessFactor = (double) currentCycleTime / cycleTime;
                    if (brightnessFactor <= 0.5) {
                        node.setOpacity(2 * brightnessFactor);
                    } else {
                        node.setOpacity(1 - 2 * (brightnessFactor - 0.5));
                    }

                } else {
                    this.stop();
                }
                if (arrivalCondition()) {
                    succeeded = true;
                    stop();
                }
            }

            @Override
            public void stop() {
                super.stop();
                animationIsRunning = false;
                dismiss();
                if (succeeded) {
                    onSucceededArrival();
                }
                node.setOpacity(1);
            }
        };
        animationTimer.start();
    }

    protected abstract void onSucceededArrival();

    protected abstract boolean arrivalCondition();

    public BooleanProperty finishedProperty() {
        return finished;
    }

}
