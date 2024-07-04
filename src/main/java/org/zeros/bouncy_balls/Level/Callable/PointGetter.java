package org.zeros.bouncy_balls.Level.Callable;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

import java.util.concurrent.*;

public class PointGetter implements Callable<Point2D> {
    private final String message;
    private Double xValue;
    private Double yValue;
    private Point2D pickedPointValue;
    private String axis= " X:";
    public PointGetter(String message) {
        this.message=message;
    }

    @Override
    public Point2D call() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);
        Future<Point2D> pickedPoint = service.submit(new MouseInput(latch));

        Future<Double> valueEntered = service.submit(new NumberInput(message+axis, latch));
        try {
            if(xValue==null) {
                xValue = valueEntered.get();
            }else {
                yValue=valueEntered.get();
            }
            pickedPointValue = pickedPoint.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new InterruptedException("Dimension not get");
        }

        if (xValue != null) {
          if (yValue!=null)return new Point2D(xValue,yValue);
          else if(pickedPointValue == null) {
              axis=" Y:";
              return call();
          }
        }
        if (pickedPointValue != null) {
            return pickedPointValue.subtract(CreatorParameters.getDEFAULT_OFFSET_POINT());
        }
        return call();
    }
}
