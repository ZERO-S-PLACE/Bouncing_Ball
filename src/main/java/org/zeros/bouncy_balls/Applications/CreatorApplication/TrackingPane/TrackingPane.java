package org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jetbrains.annotations.NotNull;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

public class TrackingPane extends AnchorPane {
    public void setOrthoType(OrthoType orthoType) {
        this.orthoType = orthoType;
    }

    private OrthoType orthoType=OrthoType.NONE;
    private final Circle referencePointSign = new Circle();
    private final Circle previousPointSign = new Circle();
    private final ObjectProperty<Point2D> selectedPointProperty=new SimpleObjectProperty<>();
    private final ObjectProperty<Point2D> referencePointProperty=new SimpleObjectProperty<>();
    private final ObjectProperty<Point2D> previousPointProperty=new SimpleObjectProperty<>();




    public TrackingPane() {
        setFocusTraversable(false);
        BackgroundFill backgroundFill = new BackgroundFill(Color.TRANSPARENT, null, null);
        setBackground(new Background(backgroundFill));
        setOnMouseClicked(this::pointPicked);
        selectedPointProperty.addListener(selectedPointListener());
        referencePointProperty.addListener(referencePointListener());
        previousPointProperty.addListener(previousPointListener());
        CreatorModel.getInstance().getViewFactory().getScene().addEventHandler(KeyEvent.KEY_PRESSED,this::orthoKeyPressHandler);
        CreatorModel.getInstance().getViewFactory().getScene().addEventHandler(KeyEvent.KEY_RELEASED,this::orthoKeyReleaseHandler);
        setPickPointSign(previousPointSign,Color.RED,3);
        setPickPointSign(referencePointSign,Color.BLUE,3);
        getChildren().add(previousPointSign);
        getChildren().add(referencePointSign);
    }
    private ChangeListener<Point2D> selectedPointListener() {
        return (observable, oldValue, newValue) -> {
            if (oldValue != null) {
               previousPointProperty.set(oldValue);
            }
        };
    }

    private ChangeListener<Point2D> referencePointListener() {
        return (observable, oldValue, newValue) -> {
            if (newValue != null) {
                referencePointSign.setCenterX(newValue.getX());
                referencePointSign.setCenterY(newValue.getY());
                referencePointSign.setVisible(true);
            } else {
                referencePointSign.setVisible(false);
            }
        };
    }
    private ChangeListener<Point2D> previousPointListener() {
        return (observable, oldValue, newValue) -> {
            if (newValue != null) {
                previousPointSign.setVisible(true);
                previousPointSign.setCenterX(newValue.getX());
                previousPointSign.setCenterY(newValue.getY());
            }else {
                previousPointSign.setVisible(false);
            }
        };
    }

    private void orthoKeyPressHandler(KeyEvent event) {
        if(event.getCode().equals(KeyCode.SHIFT))orthoType=OrthoType.ORTHO;
    }
    private void orthoKeyReleaseHandler(KeyEvent event) {
        if(event.getCode().equals(KeyCode.SHIFT))orthoType=OrthoType.NONE;
    }

    private void setPickPointSign(Circle circle,Color color,double radius) {
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeWidth(radius/4);
        circle.setStroke(color);
        circle.setRadius(radius);
        circle.setVisible(false);
    }
    private void pointPicked(MouseEvent mouseEvent) {

        setSelectedPoint( new Point2D(mouseEvent.getX() , mouseEvent.getY()));
    }
    public ObjectProperty<Point2D> selectedPointProperty() {
        return selectedPointProperty;
    }
    public ObjectProperty<Point2D> getReferencePointProperty() {
        return referencePointProperty;
    }


    private void setSelectedPoint(Point2D selectedPoint) {
        Point2D directReference=getDirectReference();
        switch (orthoType){
            case NONE -> selectedPointProperty.set(selectedPoint);
            case ORTHO_Y -> selectedPointProperty.set(new Point2D(selectedPoint.getX(),directReference.getY()));
            case ORTHO_X -> selectedPointProperty.set(new Point2D(directReference.getX(),selectedPoint.getY()));
            case ORTHO -> {
                if(Math.abs(selectedPoint.getX()-directReference.getX())<=Math.abs(selectedPoint.getY()-directReference.getY())){
                    selectedPointProperty.set(new Point2D(directReference.getX(),selectedPoint.getY()));
                }else {
                    selectedPointProperty.set(new Point2D(selectedPoint.getX(),directReference.getY()));}
                }

        }
    }

    private Point2D getDirectReference() {
        if(referencePointProperty.get()!=null)return referencePointProperty.get();
        else if(previousPointProperty.get()!=null)return previousPointProperty.get();
        return new Point2D(0,0).add(CreatorParameters.getDEFAULT_OFFSET_POINT().multiply(Properties.SIZE_FACTOR()));
    }
    public ObjectProperty<Point2D> previousPointProperty() {
        return previousPointProperty;
    }

}
