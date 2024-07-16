package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;

public class MovingBallPreview extends Preview {
    private final Circle ballPreview = new Circle();
    private final Circle velocityPreview = new Circle();
    private final Point2D ballCenter;
    private boolean isBallRadiusPicked = false;
    private final EventHandler<MouseEvent> ballCreationHandler = this::valueChanged;


    public MovingBallPreview(Point2D center) {
        this.ballCenter = rescaleToLayout(center);
    }


    @Override
    public void start() {
        ballPreview.setMouseTransparent(true);
        velocityPreview.setMouseTransparent(true);
        BackgroundImages.setBallStandardBackground(ballPreview);
        BackgroundImages.setBallStandardBackground(velocityPreview);
        velocityPreview.setOpacity(0.5);
        ballPreview.setCenterX(ballCenter.getX());
        ballPreview.setCenterY(ballCenter.getY());
        velocityPreview.setCenterX(ballCenter.getX());
        velocityPreview.setCenterY(ballCenter.getY());
        ballPreview.setRadius(1);
        Platform.runLater(() -> {
            if (!trackingPane.getChildren().contains(ballPreview)) {
                trackingPane.getChildren().add(ballPreview);
            }
        });
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, ballCreationHandler);

    }


    @Override
    public void pause() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, ballCreationHandler);
    }

    @Override
    public void resume() {
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, ballCreationHandler);
    }

    private void valueChanged(MouseEvent mouseEvent) {
        Point2D pointPicked = new Point2D(mouseEvent.getX(), mouseEvent.getY());

        if (!isBallRadiusPicked) {
            ballPreview.setRadius(ballCenter.distance(pointPicked));
        } else {
            velocityPreview.setCenterX(pointPicked.getX());
            velocityPreview.setCenterY(pointPicked.getY());
        }
    }

    @Override
    public void remove() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, ballCreationHandler);
        Platform.runLater(() -> trackingPane.getChildren().remove(ballPreview));
        Platform.runLater(() -> trackingPane.getChildren().remove(velocityPreview));
    }

    @Override
    public Shape getShape() {
        return ballPreview;
    }

    public void setRadius(double radius) {
        double radiusPicked = rescaleToLayout(radius);
        isBallRadiusPicked = true;
        velocityPreview.setRadius(radiusPicked);
        Platform.runLater(() -> {
            if (!trackingPane.getChildren().contains(velocityPreview)) {
                trackingPane.getChildren().add(velocityPreview);
            }
        });
    }
}
