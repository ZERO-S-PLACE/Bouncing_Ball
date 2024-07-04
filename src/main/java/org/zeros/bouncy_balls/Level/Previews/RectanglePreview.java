package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;

public class RectanglePreview extends Preview {
    private final Rectangle previewRectangle = new Rectangle();
    private final Point2D rectangleReference;
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
        previewRectangle.setX(rectangleReference.getX());
        previewRectangle.setY(rectangleReference.getY());
        previewRectangle.setHeight(1);
        previewRectangle.setWidth(1);
        Platform.runLater(() -> {
            if (!trackingPane.getChildren().contains(previewRectangle)) {
                trackingPane.getChildren().add(previewRectangle);
            }
        });
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, this::createRectangle);

    }

    @Override
    public void remove() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, this::createRectangle);
        Platform.runLater(() -> trackingPane.getChildren().remove(previewRectangle));
    }
}
