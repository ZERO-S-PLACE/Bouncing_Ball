package org.zeros.bouncy_balls.Applications.CreatorApplication;

import javafx.application.Application;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        CreatorModel.getInstance().getViewFactory().showMainWindow();
       new Thread(()-> {
           CreatorModel.getInstance().getLevelCreator().create();
       }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}