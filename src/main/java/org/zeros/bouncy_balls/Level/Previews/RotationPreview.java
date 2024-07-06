package org.zeros.bouncy_balls.Level.Previews;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

public class RotationPreview extends Preview {

    private final Shape shape;
    private final EventHandler< MouseEvent> mouseMovedHandler=this::onMouseMoved;
    private Point2D center;
    private Point2D lastPoint;
    private Rotate rotate;

    public RotationPreview(Area area, Point2D center,Point2D reference) {
        this.center=rescaleToLayout(center);
        this.lastPoint=rescaleToLayout(reference);
        this.shape =area.getPath();
        shape.setLayoutX(shape.getLayoutX()+CreatorParameters.getDEFAULT_X_OFFSET());
        shape.setLayoutY(shape.getLayoutY()+CreatorParameters.getDEFAULT_Y_OFFSET());
        rotate=new Rotate();
        rotate.setPivotX(this.center.getX()-shape.getLayoutX());
        rotate.setPivotY(this.center.getY()-shape.getLayoutY());
        rotate.setAngle(0);
    }

    @Override
    public void start() {
        Platform.runLater(()->trackingPane.getChildren().add(shape));
        shape.getTransforms().add(rotate);
        trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED,mouseMovedHandler);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void remove() {
        shape.getTransforms().remove(rotate);
        Platform.runLater(()->trackingPane.getChildren().remove(shape));
        trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED,mouseMovedHandler);
    }


    protected void onMouseMoved(MouseEvent mouseEvent) {
        Point2D pickedPoint = new Point2D(mouseEvent.getX() , mouseEvent.getY());

        if(VectorMath.rotationIsClockWise(lastPoint,pickedPoint,center)) {
            rotate.setAngle( center.angle(pickedPoint, lastPoint));
        }
        else {
            rotate.setAngle(-center.angle(pickedPoint, lastPoint));
        }

    }
    @Override
    public Shape getShape() {
        return shape;
    }
    public double getRotation(){
        return rotate.getAngle();
    }

}
