package org.zeros.bouncy_balls.Level.Callable;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Level.Enums.PolyLineSegmentActions;
import org.zeros.bouncy_balls.Level.Previews.PolyLinePreview;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InputValues {
   private final static ArrayList<ExecutorService> servicesRunning=new ArrayList<>();
    public  double getDimension(String message, Point2D reference) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        servicesRunning.add(executorService);
        Future<Double> dimensionValue = executorService.submit(new DimensionGetter(reference, message));
        Double value=dimensionValue.get();
        if(value!=null)return value;
        throw new RuntimeException("Input exception");
    }
    public  Point2D getPoint(String message) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        servicesRunning.add(executorService);
        Future<Point2D> pointValue = executorService.submit(new PointGetter(message));
        Point2D value=pointValue.get();
        if(value!=null)return value.multiply(Properties.SIZE_FACTOR());
        throw new RuntimeException("Input exception");
    }
    public  boolean attachPolyLineSegment(PolylineArea area, PolyLinePreview polyLinePreview, PolyLineSegmentActions polyLineSegment) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //servicesRunning.add(executorService);
        Future<Boolean> attachNext = executorService.submit(new PolyLineInput(area, polyLinePreview, polyLineSegment));
        try {
            boolean finished= attachNext.get();
            return finished;
        } catch (InterruptedException | ExecutionException e) {
            polyLinePreview.dismissElement();
            return false;
        }
    }
    public  void terminateInput(){
        for (ExecutorService service:servicesRunning){
            service.shutdownNow();
        }
        servicesRunning.clear();
    }
}
