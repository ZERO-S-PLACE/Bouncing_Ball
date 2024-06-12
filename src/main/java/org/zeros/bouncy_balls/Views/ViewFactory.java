package org.zeros.bouncy_balls.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;

import java.io.IOException;
import java.util.Objects;

public class ViewFactory {
    private Stage stage;
    private Scene scene;

    private AnchorPane gameAnimationPanel;
    private BorderPane welcomePanel;
    private BorderPane levelTypeChoicePanel;
    private BorderPane leaderboardPanel;
    private BorderPane levelSubtypePanel;
    private BorderPane levelSelectionPanel;
    private BorderPane gamePanel;

    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainWindow.fxml"));
        loader.setController(Model.getInstance().controllers().getMainWindowController());
        createStage(loader);
       scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/General/tooltip.css")).toExternalForm());


    }

    public void showLevelCreator() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/2c_LevelCreator/LevelCreator.fxml"));
        loader.setController(Model.getInstance().controllers().getLevelCreatorController());
        createStage(loader);
    }

    public AnchorPane getGameView()  {
        if (gameAnimationPanel == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/5_AnimationPanel/GameAnimationPanel.fxml"));
                loader.setController(Model.getInstance().controllers().getGamePanelController());
            try {
                gameAnimationPanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return gameAnimationPanel;
    }
    public BorderPane getWelcomePanel()  {
        if (welcomePanel == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/1_WelcomePanel/WelcomePanel.fxml"));
                loader.setController(Model.getInstance().controllers().getWelcomePanelController());
            try {
                welcomePanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return welcomePanel;
    }
    public BorderPane getLevelTypeChoicePanel() {
        if (levelTypeChoicePanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/2a_LevelTypeChoice/LevelTypeChoicePanel.fxml"));
            loader.setController(Model.getInstance().controllers().getLevelTypeChoiceController());
            try {
                levelTypeChoicePanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return levelTypeChoicePanel;
    }
    public BorderPane getLeaderboardPanel()  {
        if (leaderboardPanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/2b_Leaderboard/LeaderboardPanel.fxml"));
            loader.setController(Model.getInstance().controllers().getLeaderboardPanelController());
            try {
                leaderboardPanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return leaderboardPanel;
    }
    public BorderPane getLevelSubtypePanel()  {
        if (levelSubtypePanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/3_LevelSubtypeChoice/LevelSubtypeChoicePanel.fxml"));
            loader.setController(Model.getInstance().controllers().getLevelSubtypeChoiceController());
            try {
                levelSubtypePanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return levelSubtypePanel;
    }

    private void createStage(FXMLLoader loader) {

        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(ViewFactory.class.getResource("/Icons/ProgramIcon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Zeros Paint");
        stage.show();
    }

    public AnimationPane getBackgroundAnimation() {
        AnimationPane animationPane=new AnimationPane("program_data/background_animations/A1.ser");

        animationPane.getAnimationPane().setBackground(new Background(new BackgroundFill(Properties.BACKGROUND_COLOR(),null,null)));
        return animationPane;
    }
}