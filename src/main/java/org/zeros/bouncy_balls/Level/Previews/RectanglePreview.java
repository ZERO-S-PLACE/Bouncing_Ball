package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;

public class RectanglePreview extends Preview {
    private final Rectangle previewRectangle = new Rectangle();
    private final Point2D rectangleReference;
    private final EventHandler<MouseEvent> rectangleCreationHandler = this::createRectangle;

    public RectanglePreview(Point2D corner) {
        rectangleReference = rescaleToLayout(corner);
    }

    private void createRectangle(MouseEvent mouseEvent) {
        previewRectangle.setX(Math.min(rectangleReference.getX(), mouseEvent.getX()));
        previewRectangle.setY(Math.min(rectangleReference.getY(), mouseEvent.getY()));
        previewRectangle.setWidth(Math.abs(rectangleReference.getX() - mouseEvent.getX()));
        previewRectangle.setHeight(Math.abs(rectangleReference.getY() - mouseEvent.getY()));
    }


    @Override
    public void start() {

        previewRectangle.setMouseTransparent(true);
        BackgroundImages.setObstacleBackground(previewRectangle);
        previewRectangle.setStroke(LINE_COLOR);
        previewRectangle.setStrokeWidth(0.2);
        previewRectangle.setX(rectangleReference.getX());
        previewRectangle.setY(rectangleReference.getY());
        previewRectangle.setHeight(1);
        previewRectangle.setWidth(1);
        Platform.runLater(() -> {
            if (!trackingPane.getChildren().contains(previewRectangle)) {
                trackingPane.getChildren().add(previewRectangle);
            }
        });
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, rectangleCreationHandler);

    }


    @Override
    public void pause() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, rectangleCreationHandler);
    }

    @Override
    public void resume() {
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, rectangleCreationHandler);
    }

    @Override
    public void remove() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, rectangleCreationHandler);
        Platform.runLater(() -> trackingPane.getChildren().remove(previewRectangle));
    }

    @Override
    public Shape getShape() {
        return previewRectangle;
    }


}
