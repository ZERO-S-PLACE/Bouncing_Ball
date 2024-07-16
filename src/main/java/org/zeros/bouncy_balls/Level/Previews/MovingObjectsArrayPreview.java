package org.zeros.bouncy_balls.Level.Previews;


//work in progress...
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;

import java.util.ArrayList;
/*

public class MovingObjectsArrayPreview extends Preview{

        private final MovingObject objectPreview;
        private ArrayList<ArrayList<Shape>> copiesPreviews=new ArrayList<>();
        private final MovingObject spacingPicker;
        private  Point2D spaceingV=null;
        private Point2D spaceingU=null;
        private int countU=-1;
        private int countV=-1;
        private final EventHandler<MouseEvent> arrayCreationHandler =this::valueChanged;


        public MovingObjectsArrayPreview(MovingObject object) {
            this.objectPreview = object;
            objectPreview.updateCenter(objectPreview.center().add(CreatorParameters.getDEFAULT_OFFSET_POINT().multiply(Properties.SIZE_FACTOR())));
            this.spacingPicker =object.clone();
        }



        @Override
        public void start() {

            BackgroundImages.setBallStandardBackground(objectPreview.getShape());
            BackgroundImages.setBallStandardBackground(spacingPicker.getShape());
            objectPreview.getShape().setOpacity(0.9);
            spacingPicker.getShape().setOpacity(0.3);
            Platform.runLater(()->{
                if(!trackingPane.getChildren().contains(objectPreview.getShape())){
                    trackingPane.getChildren().add(objectPreview.getShape());
                }
                if(!trackingPane.getChildren().contains(spacingPicker.getShape())){
                    trackingPane.getChildren().add(spacingPicker.getShape());
                }
            });
            trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, arrayCreationHandler);

        }



        @Override
        public void pause() {
            trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, arrayCreationHandler);
        }

        @Override
        public void resume() {
            trackingPane.addEventHandler(MouseEvent.MOUSE_MOVED, arrayCreationHandler);
        }

        private void valueChanged(MouseEvent mouseEvent) {
            Point2D pointPicked=new Point2D(mouseEvent.getX(),mouseEvent.getY());
                spacingPicker.updateCenter(pointPicked);
        }

        @Override
        public void remove() {
            trackingPane.removeEventHandler(MouseEvent.MOUSE_MOVED, arrayCreationHandler);
            Platform.runLater(()->trackingPane.getChildren().remove(objectPreview.getShape()));
            Platform.runLater(()->trackingPane.getChildren().remove(spacingPicker.getShape()));
        }
        @Override
        public Shape getShape() {
            return objectPreview.getShape();
        }




}
*/