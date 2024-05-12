package org.zeros.bouncy_balls.Controllers;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelCreatorController implements Initializable {
    public static Object getLock() {
        return lock;
    }
    private static final Object lock=new Object();
    private Point2D selectedPoint;
    private double pickedDistance;
    private Event lastEvent;



    private String textEntered;
    private final Circle pickedPointSign=new Circle();
    public AnchorPane preview;
    public Label messageLabel;
    public TextField inputLabel;
    private final EventHandler<KeyEvent> confirmHandler = this::confirmTextInput;
    private final EventHandler<MouseEvent> pointPickedHandler = this::pointPicked;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputLabel.addEventHandler(KeyEvent.KEY_PRESSED,confirmHandler);
        preview.setOnMouseClicked(pointPickedHandler);
        setPickPointSign();

    }

    private void setPickPointSign() {
        pickedPointSign.setFill(Color.TRANSPARENT);
        pickedPointSign.setStrokeWidth(0.2);
        pickedPointSign.setStroke(Color.BLACK);
        pickedPointSign.setRadius(3);
    }

    private void confirmTextInput(KeyEvent event) {
        textEntered= inputLabel.textProperty().getValue();
        lastEvent=event;
        if(event.getCode().equals(KeyCode.ENTER)){
           synchronized (lock){
               lock.notifyAll();
           }
        }
    }

    public Event getLastEvent() {
        return lastEvent;
    }

    private void pointPicked(MouseEvent mouseEvent) {
        Point2D newPoint= new Point2D(mouseEvent.getX(),mouseEvent.getY());
        lastEvent=mouseEvent;
        if(selectedPoint!=null) {
            pickedDistance = newPoint.distance(selectedPoint);
        }else {
            if(!preview.getChildren().contains(pickedPointSign)) {
                preview.getChildren().add(pickedPointSign);
            }
        }
        setSelectedPoint(newPoint);
        selectedPoint=newPoint;
        synchronized (lock){
            lock.notifyAll();
        }
    }

    public Point2D getSelectedPoint() {
        return selectedPoint;
    }

    public void setSelectedPoint(Point2D selectedPoint) {
        this.selectedPoint = selectedPoint;
        if(selectedPoint!=null) {
            pickedPointSign.setCenterX(selectedPoint.getX());
            pickedPointSign.setCenterY(selectedPoint.getY());
        }else {
            Platform.runLater(()->preview.getChildren().remove(pickedPointSign));
        }
    }

    public double getPickedDistance() {
        return pickedDistance;
    }
    public String getTextEntered() {
        return textEntered;
    }

    public void setTextEntered(String textEntered) {
        this.textEntered = textEntered;
        inputLabel.textProperty().setValue(textEntered);
    }
}
