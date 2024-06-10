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
    private AnchorPane welcomePanel;

    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainWindow.fxml"));
        createStage(loader);
    }

    public void showLevelCreator() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/2c_LevelCreator/LevelCreator.fxml"));
        loader.setController(Model.getInstance().controllers().getLevelCreatorController());
        createStage(loader);
    }

    public AnchorPane getGameView() throws IOException {
        if (gamePanel == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/5_AnimationPanel/GamePanel.fxml"));
                loader.setController(Model.getInstance().controllers().getGamePanelController());
                gamePanel = loader.load();
        }

        return gamePanel;
    }
    public AnchorPane getWelcomePanel() throws IOException {
        if (welcomePanel == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/1_WelcomePanel/WelcomePanel.fxml"));
                loader.setController(Model.getInstance().controllers().getWelcomePanelController());
                welcomePanel = loader.load();
        }

        return welcomePanel;
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