package org.zeros.bouncy_balls.Level.Callable;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

import java.util.concurrent.*;

public class DimensionGetter implements Callable<Double> {
    private final Point2D reference;
    private final String message;

    public DimensionGetter(Point2D reference, String message) {
        this.reference = reference.multiply(1 / Properties.SIZE_FACTOR()).add(CreatorParameters.getDEFAULT_OFFSET_POINT());
        this.message = message;
    }

    @Override
    public Double call() throws InterruptedException {

        CreatorModel.getInstance().getViewFactory().getTrackingPane().getReferencePointProperty().set(reference);
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);
        Future<Point2D> pickedPoint = service.submit(new MouseInput(latch));
        Future<Double> numberEntered = service.submit(new NumberInput(message, latch));
        Double numberEnteredValue;
        Point2D pickedPointValue;
        try {
            numberEnteredValue = numberEntered.get();
            pickedPointValue = pickedPoint.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new InterruptedException("Dimension not get");
        }
        CreatorModel.getInstance().getViewFactory().getTrackingPane().getReferencePointProperty().set(null);

        if (numberEnteredValue != null) {
            if (numberEnteredValue > 0) return numberEnteredValue * Properties.SIZE_FACTOR();
        }
        if (pickedPointValue != null) {
            return pickedPointValue.distance(reference) * Properties.SIZE_FACTOR();
        }
        return null;
    }
}
