package org.zeros.bouncy_balls.Level.Callable;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class MouseInput implements Callable<Point2D> {
    private final CountDownLatch latch;

    public MouseInput(CountDownLatch latch) {
        this.latch = latch;
    }

    private static ChangeListener<Point2D> mouseInputListener(CountDownLatch latch) {
        return (observable, oldValue, newValue) -> {
            CreatorModel.getInstance().controllers().getBottomPanelController().textEnteredProperty().set("");
            latch.countDown();
        };
    }

    @Override
    public Point2D call() {
        CreatorModel.getInstance().getViewFactory().getTrackingPane().selectedPointProperty().addListener(mouseInputListener(latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            return null;
        }
        CreatorModel.getInstance().getViewFactory().getTrackingPane().selectedPointProperty().removeListener(mouseInputListener(latch));
        return CreatorModel.getInstance().getViewFactory().getTrackingPane().selectedPointProperty().get();
    }
}
