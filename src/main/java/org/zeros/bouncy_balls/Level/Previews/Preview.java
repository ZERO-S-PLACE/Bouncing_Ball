package org.zeros.bouncy_balls.Level.Previews;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.TrackingPane;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

public abstract class Preview {
    protected final TrackingPane trackingPane= CreatorModel.getInstance().getViewFactory().getTrackingPane();
    public abstract void start();
    public abstract void remove();
    protected static Point2D rescaleToLayout(Point2D point) {
        return point.multiply(1 / Properties.SIZE_FACTOR()).add(CreatorParameters.getDEFAULT_OFFSET_POINT());
    }
    protected static double rescaleToLayout(double value) {
        return value*(1 / Properties.SIZE_FACTOR());
    }
}
