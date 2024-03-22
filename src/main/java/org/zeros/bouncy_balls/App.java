package org.zeros.bouncy_balls;

import javafx.application.Application;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Model.Model;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Model.getInstance().getViewFactory().showMainWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}