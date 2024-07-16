package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

public class MovePreview extends Preview {
    private final Shape shape;
    private final Point2D reference;
    private final Point2D initial;
    private final EventHandler<MouseEvent> mouseMovedHandler = this::onMouseMoved;

    public MovePreview(Area area, Point2D reference) {
        this.reference = rescaleToLayout(reference);
        this.shape = area.getPath();
        initial = new Point2D(shape.getLayoutX(), shape.getLayoutY());
    }

    public MovePreview(Shape shape, Point2D reference) {
        this.reference = rescaleToLayout(reference);
        this.shape = shape;

        initial = new Point2D(shape.getLayoutX(), shape.getLayoutY());
    }

    protected void onMouseMoved(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        shape.setLayoutX(initial.getX() + CreatorParameters.getDEFAULT_X_OFFSET() + pickedPoint.getX() - reference.getX());
        shape.setLayoutY(initial.getY() + CreatorParameters.getDEFAULT_Y_OFFSET() + pickedPoint.getY() - reference.getY());
    }

    @Override
    public void start() {
        shape.setSmooth(true);
        shape.setLayoutX(initial.getX() + CreatorParameters.getDEFAULT_X_OFFSET());
        shape.setLayoutY(initial.getY() + CreatorParameters.getDEFAULT_Y_OFFSET());
        Platform.runLater(() -> {
            if (!trackingPane.getChildren().contains(shape)) {
                trackingPane.getChildren().add(shape);
            }
        });
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);

    }

    @Override
    public void pause() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
    }

    @Override
    public void resume() {
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
    }

    @Override
    public void remove() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
        shape.setLayoutX(initial.getX());
        shape.setLayoutY(initial.getY());
        Platform.runLater(() -> trackingPane.getChildren().remove(shape));
    }

    @Override
    public Shape getShape() {
        return shape;
    }
}
