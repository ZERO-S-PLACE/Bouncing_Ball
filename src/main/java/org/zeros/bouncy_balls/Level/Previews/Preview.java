package org.zeros.bouncy_balls.Level.Previews;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.TrackingPane;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

public abstract class Preview {
    protected final TrackingPane trackingPane = CreatorModel.getInstance().getViewFactory().getTrackingPane();
    protected final Color LINE_COLOR = Properties.OBSTACLE_COLOR().darker().darker();

    public static Point2D rescaleToLayout(Point2D point) {
        return point.multiply(1 / Properties.SIZE_FACTOR()).add(CreatorParameters.getDEFAULT_OFFSET_POINT());
    }

    public static Point2D rescaleToAnimation(Point2D point) {
        return point.subtract(CreatorParameters.getDEFAULT_OFFSET_POINT()).multiply(Properties.SIZE_FACTOR());
    }

    protected static double rescaleToLayout(double value) {
        return value * (1 / Properties.SIZE_FACTOR());
    }

    public abstract void start();

    public abstract void pause();

    public abstract void resume();

    public abstract void remove();

    public abstract Shape getShape();
}
