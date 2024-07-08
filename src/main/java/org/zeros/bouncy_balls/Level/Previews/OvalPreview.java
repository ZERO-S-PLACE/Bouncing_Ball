package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.TrackingPane;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;

public class OvalPreview extends Preview{
    private final Ellipse previewOval=new Ellipse();
    private final Point2D ovalCenter;
    private double ovalRadiusPicked;
    private boolean isOvalRadiusPicked;
    private final EventHandler<MouseEvent> createOvalHandler=this::createOval;


    public OvalPreview(Point2D center) {
        this.ovalCenter = rescaleToLayout(center);
    }



    @Override
    public void start() {
        previewOval.setMouseTransparent(true);
        BackgroundImages.setObstacleBackground(previewOval);
        previewOval.setStroke(LINE_COLOR);
        previewOval.setStrokeWidth(0.2);
        previewOval.setCenterX(ovalCenter.getX());
        previewOval.setCenterY(ovalCenter.getY());
        previewOval.setRadiusX(1);
        previewOval.setRadiusY(1);
        Platform.runLater(()->{
            if(!trackingPane.getChildren().contains(previewOval)){
                trackingPane.getChildren().add(previewOval);
            }
        });
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, createOvalHandler);

    }



    @Override
    public void pause() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, createOvalHandler);
    }

    @Override
    public void resume() {
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, createOvalHandler);
    }

    private void createOval(MouseEvent mouseEvent) {

        if(!isOvalRadiusPicked){
            previewOval.setRadiusX(Math.abs(ovalCenter.getX()-mouseEvent.getX()));
            previewOval.setRadiusY(previewOval.getRadiusX());

        }else {
            previewOval.setRadiusX(ovalRadiusPicked);
            previewOval.setRadiusY(Math.abs(ovalCenter.getY()-mouseEvent.getY()));
        }
    }
    @Override
    public void remove() {
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, createOvalHandler);
        Platform.runLater(()->trackingPane.getChildren().remove(previewOval));
    }

    public void setOvalRadiusPicked(double ovalRadiusPicked) {
        this.ovalRadiusPicked = rescaleToLayout(ovalRadiusPicked);
        isOvalRadiusPicked=true;
    }
    @Override
    public Shape getShape() {
        return previewOval;
    }

}
