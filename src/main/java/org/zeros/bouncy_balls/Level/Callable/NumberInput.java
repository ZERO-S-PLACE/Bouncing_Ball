package org.zeros.bouncy_balls.Level.Callable;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class NumberInput implements Callable<Double> {
    private final String message;
    private final CountDownLatch latch ;
    public NumberInput(String message, CountDownLatch latch) {
        this.message=message;
        this.latch=latch;
    }


    @Override
    public Double call()  {
        Platform.runLater(() -> CreatorModel.getInstance().controllers().getBottomPanelController().tipLabel.textProperty().setValue(message));
        Platform.runLater(() ->CreatorModel.getInstance().controllers().getBottomPanelController().setTextEntered(""));
        CreatorModel.getInstance().controllers().getBottomPanelController().textEnteredProperty().addListener(getTextInputListener());

        try {
            latch.await();
        } catch (InterruptedException e) {
            return null;
        }
        CreatorModel.getInstance().controllers().getBottomPanelController().textEnteredProperty().removeListener(getTextInputListener());
        CreatorModel.getInstance().getViewFactory().getTrackingPane().selectedPointProperty().set(null);
        return convertToDouble(CreatorModel.getInstance().controllers().getBottomPanelController().textEnteredProperty().get());
    }


    private  ChangeListener<String> getTextInputListener() {
        return (observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)&& !newValue.isEmpty()){
                if(convertToDouble(newValue)==null){
                    Platform.runLater(() -> CreatorModel.getInstance().controllers().getBottomPanelController().tipLabel.textProperty().setValue("Wrong value! "+message));
                }else {
                    latch.countDown();
                }
            }
        };
    }
    public static Double convertToDouble(String text) {
        double value;
        try {
            value = Double.parseDouble(text);
        } catch (Exception e) {
            return null;
        }
        return value;
    }
}
