package org.zeros.bouncy_balls.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Model;

import java.io.IOException;
import java.util.Objects;

public class ViewFactory {
    private Scene scene;
    private BorderPane welcomePanel;
    private BorderPane levelTypeChoicePanel;
    private BorderPane leaderboardPanel;
    private BorderPane settingsPanel;
    private BorderPane levelSubtypePanel;
    private BorderPane levelSelectionPanel;
    private BorderPane gameCountDownPanel;
    private BorderPane gamePausedPanel;
    private AnimationPane gameAnimation;
    private AnimationPane backAnimationPane;

    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainWindow.fxml"));
        loader.setController(Model.getInstance().controllers().getMainWindowController());
        createStage(loader);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/CustomNodes/Tooltip.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/P5_AnimationPanel/GameAnimation.css")).toExternalForm());

    }

    public void showLevelCreator() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/2c_LevelCreator/LevelCreator.fxml"));
        loader.setController(Model.getInstance().controllers().getLevelCreatorController());
        createStage(loader);
    }

    public BorderPane getWelcomePanel() {
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

    public BorderPane getLeaderboardPanel() {
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

    public BorderPane getSettingsPanel() {
        if (settingsPanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/2d_Settings/SettingsPanel.fxml"));
            loader.setController(Model.getInstance().controllers().getSettingsController());
            try {
                settingsPanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return settingsPanel;

    }

    public BorderPane getLevelSubtypePanel(AnimationType type) {
        if (levelSubtypePanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/3_LevelSubtypeChoice/LevelSubtypeChoicePanel.fxml"));
            Model.getInstance().controllers().getLevelSubtypeChoiceController().setAnimationType(type);
            loader.setController(Model.getInstance().controllers().getLevelSubtypeChoiceController());
            try {
                levelSubtypePanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Model.getInstance().controllers().getLevelSubtypeChoiceController().setAnimationType(type);
        }
        return levelSubtypePanel;
    }

    public BorderPane getNewLevelSelectionPanel(AnimationType type, String subtype) {
        if (levelSelectionPanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/4_LevelSelection/LevelSelectionPanel.fxml"));
            Model.getInstance().controllers().getLevelSelectionController().loadLevelsList(type, subtype);
            loader.setController(Model.getInstance().controllers().getLevelSelectionController());
            try {
                levelSelectionPanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Model.getInstance().controllers().getLevelSelectionController().loadLevelsList(type, subtype);
        }

        return levelSelectionPanel;
    }

    public BorderPane getLevelSelectionPanel() {
        return levelSelectionPanel;
    }

    public BorderPane getGamePausedPanel() {
        if (gamePausedPanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/5_AnimationPanel/GamePausedPanel.fxml"));
            loader.setController(Model.getInstance().controllers().getGamePausedController());
            try {
                gamePausedPanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return gamePausedPanel;
    }

    public BorderPane getCountDownPanel() {
        if (gameCountDownPanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/5_AnimationPanel/GameCountDownPanel.fxml"));
            loader.setController(Model.getInstance().controllers().getGameCountDownController());
            try {
                gameCountDownPanel = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return gameCountDownPanel;
    }

    public AnimationPane getNewAnimationPane(Level level) {
        gameAnimation = new AnimationPane(level);
        BackgroundImages.setGameBackground(gameAnimation.getAnimationPane(), true);
        return gameAnimation;
    }

    public AnimationPane getCurrentAnimationPane() {
        return gameAnimation;
    }


    private void createStage(FXMLLoader loader) {

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

    public AnimationPane getBackgroundAnimation() {
        if (backAnimationPane == null) {
            backAnimationPane = new AnimationPane("program_data/background_animations/A1.ser");
        }
        BackgroundImages.setGameBackground(backAnimationPane.getAnimationPane(), false);

        return backAnimationPane;
    }
}