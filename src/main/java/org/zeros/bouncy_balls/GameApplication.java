package org.zeros.bouncy_balls;

import javafx.application.Application;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Model.Model;

public class GameApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showMainWindow();
    }
}