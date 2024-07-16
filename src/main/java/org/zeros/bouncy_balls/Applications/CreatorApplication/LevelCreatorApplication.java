package org.zeros.bouncy_balls.Applications.CreatorApplication;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

import java.io.IOException;

public class LevelCreatorApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    public static void openNewLevelCreator() {

        new Thread(() -> Platform.runLater(() -> {
            try {
                new LevelCreatorApplication().start(new Stage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })).start();
    }

    @Override
    public void start(Stage stage) throws IOException {
        CreatorModel.getInstance().getViewFactory().showMainWindow();
        new Thread(() -> {
            CreatorModel.getInstance().controllers().getLevelEditionController().addEditableElements();
            CreatorModel.getInstance().getLevelCreator().create();
            CreatorModel.getInstance().controllers().getLevelEditionController().addGeneralSettingsPanel();
        }).start();
    }
}