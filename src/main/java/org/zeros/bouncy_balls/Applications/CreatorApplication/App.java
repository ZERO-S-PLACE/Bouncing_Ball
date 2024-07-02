package org.zeros.bouncy_balls.Applications.CreatorApplication;

import javafx.application.Application;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        CreatorModel.getInstance().getViewFactory().showMainWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}