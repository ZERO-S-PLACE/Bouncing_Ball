package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;
import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.TrackingPane;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;

public class OvalPreview extends Preview{
    private final TrackingPane trackingPane= CreatorModel.getInstance().getViewFactory().getTrackingPane();
    private final Ellipse previewOval=new Ellipse();
    private final Point2D ovalCenter;
    private double ovalRadiusPicked;
    private boolean isOvalRadiusPicked;

    public OvalPreview(Point2D center) {
        this.ovalCenter = rescaleToLayout(center);
    }



    @Override
    public void start() {
        previewOval.setMouseTransparent(true);
        BackgroundImages.setObstacleBackground(previewOval);
        previewOval.setCenterX(ovalCenter.getX());
        previewOval.setCenterY(ovalCenter.getY());
        previewOval.setRadiusX(1);
        previewOval.setRadiusY(1);
        Platform.runLater(()->{
            if(!trackingPane.getChildren().contains(previewOval)){
                trackingPane.getChildren().add(previewOval);
            }
        });
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED,this::createOval);

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
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED,this::createOval);
        Platform.runLater(()->trackingPane.getChildren().remove(previewOval));
    }

    public void setOvalRadiusPicked(double ovalRadiusPicked) {
        this.ovalRadiusPicked = rescaleToLayout(ovalRadiusPicked);
        isOvalRadiusPicked=true;
    }
}
