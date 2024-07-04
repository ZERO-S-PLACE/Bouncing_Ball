package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

import java.util.ArrayList;
import java.util.List;

public class PolyLinePreview extends Preview {
    private final Path prewievPath = new Path();
    private final Line linePreview = new Line();
    private final QuadCurve quadCurvePreview = new QuadCurve();
    private final CubicCurve cubicCurvePreview = new CubicCurve();
    private final ArrayList<ArrayList<Point2D>> pickedPoints = new ArrayList<>();
    private ArrayList<Point2D> currentElement = new ArrayList<>();
    private final EventHandler<MouseEvent> elementCreationHandler = this::pathElementCreationPreview;

    public PolyLinePreview(Point2D startPoint) {
        startPoint = rescaleToLayout(startPoint);
        currentElement.add(startPoint);
        configurePathPreview(startPoint);
    }

    private void configurePathPreview(Point2D startPoint) {
        prewievPath.setFill(Color.TRANSPARENT);
        prewievPath.setStroke(Properties.OBSTACLE_COLOR());
        prewievPath.setStrokeWidth(2);
        linePreview.setFill(Color.TRANSPARENT);
        linePreview.setStroke(Properties.OBSTACLE_COLOR());
        linePreview.setStrokeWidth(2);
        linePreview.setVisible(false);
        quadCurvePreview.setFill(Color.TRANSPARENT);
        quadCurvePreview.setStroke(Properties.OBSTACLE_COLOR());
        quadCurvePreview.setStrokeWidth(2);
        linePreview.setVisible(false);
        cubicCurvePreview.setFill(Color.TRANSPARENT);
        cubicCurvePreview.setStroke(Properties.OBSTACLE_COLOR());
        cubicCurvePreview.setStrokeWidth(2);
        linePreview.setVisible(false);
        prewievPath.getElements().add(new MoveTo(startPoint.getX(), startPoint.getY()));
        prewievPath.setMouseTransparent(true);
        linePreview.setMouseTransparent(true);
        quadCurvePreview.setMouseTransparent(true);
        cubicCurvePreview.setMouseTransparent(true);
    }

    @Override
    public void start() {
        Platform.runLater(() -> {
            trackingPane.getChildren().add(prewievPath);
            trackingPane.getChildren().add(linePreview);
            trackingPane.getChildren().add(quadCurvePreview);
            trackingPane.getChildren().add(cubicCurvePreview);
        });
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, elementCreationHandler);
        setStartPoints();
    }

    private void pathElementCreationPreview(MouseEvent mouseEvent) {
        switch (currentElement.size()) {
            case 1 -> updateLineCoordinates(mouseEvent);
            case 2 -> updateQuadCurveCoordinates(mouseEvent);
            case 3 -> updateCubicCurveCoordinates(mouseEvent);
        }
    }

    private void updateLineCoordinates(MouseEvent mouseEvent) {
        linePreview.setEndX(mouseEvent.getX());
        linePreview.setEndY(mouseEvent.getY());
    }

    private void updateQuadCurveCoordinates(MouseEvent mouseEvent) {
        quadCurvePreview.setEndX(mouseEvent.getX());
        quadCurvePreview.setEndY(mouseEvent.getY());
    }

    private void updateCubicCurveCoordinates(MouseEvent mouseEvent) {
        cubicCurvePreview.setEndX(mouseEvent.getX());
        cubicCurvePreview.setEndY(mouseEvent.getY());
    }

    public void addPoint(Point2D point) {
        point = rescaleToLayout(point);
        currentElement.add(point);
        switch (currentElement.size()) {
            case 2 -> {
                quadCurvePreview.setStartX(currentElement.getFirst().getX());
                quadCurvePreview.setStartY(currentElement.getFirst().getY());
                quadCurvePreview.setControlX(point.getX());
                quadCurvePreview.setControlY(point.getY());
                quadCurvePreview.setEndX(point.getX());
                quadCurvePreview.setEndY(point.getY());
                linePreview.setVisible(false);
                quadCurvePreview.setVisible(true);
                cubicCurvePreview.setVisible(false);
            }
            case 3 -> {
                cubicCurvePreview.setStartX(currentElement.getFirst().getX());
                cubicCurvePreview.setStartY(currentElement.getFirst().getY());
                cubicCurvePreview.setControlX1(currentElement.get(1).getX());
                cubicCurvePreview.setControlY1(currentElement.get(1).getY());
                cubicCurvePreview.setControlX2(point.getX());
                cubicCurvePreview.setControlY2(point.getY());
                cubicCurvePreview.setEndX(point.getX());
                cubicCurvePreview.setEndY(point.getY());
                linePreview.setVisible(false);
                quadCurvePreview.setVisible(false);
                cubicCurvePreview.setVisible(true);
            }
            default -> throw new IllegalArgumentException("too many points");
        }
    }

    public void nextElement(Point2D point) {
        point = rescaleToLayout(point);
        currentElement.add(point);
        pickedPoints.add(new ArrayList<>(currentElement));
        addPreviousElementToPath();
        currentElement = new ArrayList<>(List.of(point));
        setStartPoints();
    }

    public void removeLast() {
        if (!pickedPoints.isEmpty()) {
            currentElement = new ArrayList<>(List.of(pickedPoints.getLast().getFirst()));
            pickedPoints.removeLast();
        } else {
            currentElement = new ArrayList<>(List.of(currentElement.getFirst()));
        }
        if (prewievPath.getElements().size() > 1) prewievPath.getElements().removeLast();
        setStartPoints();
    }

    private void setStartPoints() {
        linePreview.setStartX(currentElement.getFirst().getX());
        linePreview.setStartY(currentElement.getFirst().getY());
        linePreview.setEndX(currentElement.getFirst().getX());
        linePreview.setEndY(currentElement.getFirst().getY());
        quadCurvePreview.setStartX(currentElement.getFirst().getX());
        quadCurvePreview.setStartY(currentElement.getFirst().getY());
        cubicCurvePreview.setStartX(currentElement.getFirst().getX());
        cubicCurvePreview.setStartY(currentElement.getFirst().getY());
        linePreview.setVisible(true);
        quadCurvePreview.setVisible(false);
        cubicCurvePreview.setVisible(false);
    }

    private void addPreviousElementToPath() {
        switch (currentElement.size()) {
            case 2 -> addLineToPath();
            case 3 -> addQuadCurveToPath();
            case 4 -> addCubicCurveToPath();
            default -> throw new IllegalArgumentException("wrong size");
        }
    }

    private void addLineToPath() {
        prewievPath.getElements().add(new LineTo(currentElement.getLast().getX(), currentElement.getLast().getY()));
    }

    private void addQuadCurveToPath() {
        prewievPath.getElements().add(new QuadCurveTo(currentElement.get(1).getX(), currentElement.get(1).getY(), currentElement.getLast().getX(), currentElement.getLast().getY()));
    }

    private void addCubicCurveToPath() {
        prewievPath.getElements().add(new CubicCurveTo(currentElement.get(1).getX(), currentElement.get(1).getY(), currentElement.get(2).getX(), currentElement.get(2).getY(), currentElement.getLast().getX(), currentElement.getLast().getY()));
    }

    @Override
    public void remove() {

        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, elementCreationHandler);
        Platform.runLater(() -> {
            trackingPane.getChildren().remove(prewievPath);
            trackingPane.getChildren().remove(linePreview);
            trackingPane.getChildren().remove(quadCurvePreview);
            trackingPane.getChildren().remove(cubicCurvePreview);
            Platform.requestNextPulse();
        });

    }

    public void dismissElement() {
        currentElement = new ArrayList<>(List.of(currentElement.getFirst()));
        setStartPoints();
    }
}
