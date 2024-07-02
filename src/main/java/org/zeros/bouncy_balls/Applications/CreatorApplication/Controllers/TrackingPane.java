package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

public class TrackingPane extends AnchorPane {
    private final Circle pickedPointSign = new Circle();
    private final ObjectProperty<Point2D> selectedPointProperty=new SimpleObjectProperty<>();
    private final ObjectProperty<Point2D> referencePointProperty=new SimpleObjectProperty<>();

    public TrackingPane() {
        BackgroundFill backgroundFill = new BackgroundFill(Color.TRANSPARENT, null, null);
        setBackground(new Background(backgroundFill));
        setOnMouseClicked(this::pointPicked);
        setPickPointSign();
    }
    private void setPickPointSign() {
        pickedPointSign.setFill(Color.TRANSPARENT);
        pickedPointSign.setStrokeWidth(0.2);
        pickedPointSign.setStroke(Color.BLACK);
        pickedPointSign.setRadius(3);
    }
    private void pointPicked(MouseEvent mouseEvent) {
        Point2D newPoint = new Point2D(mouseEvent.getX() * Properties.SIZE_FACTOR(), mouseEvent.getY() * Properties.SIZE_FACTOR());
        setSelectedPoint(newPoint);
    }

    public ObjectProperty<Point2D> selectedPointProperty() {
        return selectedPointProperty;
    }
    public ObjectProperty<Point2D> getReferencePointProperty() {
        return referencePointProperty;
    }

    public void setSelectedPoint(Point2D selectedPoint) {
        this.selectedPointProperty.set(selectedPoint);
        if (selectedPoint != null) {
            pickedPointSign.setCenterX(selectedPoint.getX() / Properties.SIZE_FACTOR());
            pickedPointSign.setCenterY(selectedPoint.getY() / Properties.SIZE_FACTOR());
            if (!getChildren().contains(pickedPointSign)) {
                getChildren().add(pickedPointSign);
            }
        } else {
            Platform.runLater(() -> getChildren().remove(pickedPointSign));
        }
    }

}
