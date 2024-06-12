package org.zeros.bouncy_balls.App;

import javafx.application.Application;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Level.LevelCreator;
import org.zeros.bouncy_balls.Model.Model;

public class LevelCreatorApplication extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showLevelCreator();
        LevelCreator levelCreator=new LevelCreator();
        new Thread(levelCreator::create).start();

    }

}
