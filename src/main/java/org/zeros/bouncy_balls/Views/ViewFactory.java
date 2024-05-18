package org.zeros.bouncy_balls.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Model.Model;

import java.io.IOException;

public class ViewFactory {

    private AnchorPane gamePanel;

    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainWindow.fxml"));
        createStage(loader);

    }

    public void showLevelCreator() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LevelCreator.fxml"));
        loader.setController(Model.getInstance().getLevelCreatorController());
        createStage(loader);
    }

    public AnchorPane getGameView() {

        if (gamePanel == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/GamePanel.fxml"));
                loader.setController(Model.getInstance().getGamePanelController());
                gamePanel = loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return gamePanel;
    }


    private void createStage(FXMLLoader loader) {
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(ViewFactory.class.getResource("/Icons/ProgramIcon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Zeros Paint");
        stage.show();
    }

}