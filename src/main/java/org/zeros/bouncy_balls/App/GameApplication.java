package org.zeros.bouncy_balls.App;

import javafx.application.Application;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Model.Model;

import java.io.IOException;

public class GameApplication extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showMainWindow();
        try {
            Model.getInstance().controllers().getMainWindowController().loadStartNodes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}