package org.zeros.bouncy_balls.Controllers.P1_WelcomePanel;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.zeros.bouncy_balls.App.LevelCreatorApplication;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.DisplayUtil.NodeAnimations;
import org.zeros.bouncy_balls.Model.Model;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WelcomePanelController implements Initializable {
    public Button playButton;
    public Button creatorButton;
    public Circle leftCircle;
    public Circle rightCircle;
    public Circle centerCircle;
    public Button leaderboardButton;
    public Button settingsButton;
    public BorderPane settingsContainer;
    public BorderPane welcomePanel;
    public VBox buttonsContainer;
    private RotateTransition rotateTransition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCirclesFill();
        setRescaling();
        setEnterAnimation();
        setButtonFunctions();
        setToolTips();
    }

    private void setToolTips() {
        playButton.setTooltip(new CustomTooltip("Play a game"));
        settingsButton.setTooltip(new CustomTooltip("Application settings"));
        creatorButton.setTooltip(new CustomTooltip("Create custom game or simulation"));
        leaderboardButton.setTooltip(new CustomTooltip("Game leaderboard"));
    }

    private void setButtonFunctions() {
        playButton.setOnAction(event -> transitionToGameSelection());
        creatorButton.setOnAction(event -> transitionToLevelCreator());
        settingsButton.setOnAction(event -> transitionToSettings());
        leaderboardButton.setOnAction(event -> transitionToLeaderBoard());
    }

    private void transitionToLeaderBoard() {
        NodeAnimations.increaseBrightnessOnExit(leaderboardButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getLeaderboardPanel(), 0.1);

    }
    private void transitionToSettings() {
        NodeAnimations.increaseBrightnessOnExit(settingsButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getSettingsPanel(), 0.1);

    }
    private void transitionToGameSelection() {
        NodeAnimations.increaseBrightnessOnExit(playButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getLevelTypeChoicePanel(), 0.1);
    }
    private void transitionToLevelCreator() {
        NodeAnimations.increaseBrightnessOnExit(creatorButton);
        Thread t1 = new Thread(() -> Model.getInstance().controllers().getMainWindowController().changeTopLayer(new Pane(), 0.3));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> Platform.runLater(() -> {
            new LevelCreatorApplication().start(new Stage());
            Model.getInstance().getViewFactory().getWelcomePanel().getScene().getWindow().hide();
        })).start();
    }



    private void setEnterAnimation() {
        playButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(playButton, 0.25));
        playButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(playButton));
        creatorButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(creatorButton, 0.25));
        creatorButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(creatorButton));
        settingsButton.setOnMouseEntered(event -> {
            NodeAnimations.increaseBrightness(settingsButton, 0.25);
            startRotationAnimation(settingsButton);
        });
        settingsButton.setOnMouseExited(event -> {
            NodeAnimations.resetBrightness(settingsButton);
            stopRotationAnimation();
        });
        leaderboardButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(leaderboardButton, 0.25));
        leaderboardButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(leaderboardButton));
    }

    private void startRotationAnimation(Button button) {
        rotateTransition = new RotateTransition(Duration.seconds(7), button);
        rotateTransition.setByAngle(600);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.play();
    }

    private void stopRotationAnimation() {
        if (rotateTransition != null) {
            rotateTransition.stop();
        }
    }
    private void setCirclesFill() {
        String imagePath = Objects.requireNonNull(getClass().getResource("/Icons/General/BackBallBlue.png")).toExternalForm();
        ImagePattern imagePattern = new ImagePattern(new javafx.scene.image.Image(imagePath));
        rightCircle.setFill(imagePattern);
        centerCircle.setFill(imagePattern);
        leftCircle.setFill(imagePattern);
    }

    private void setRescaling() {
        centerCircle.radiusProperty().bind(welcomePanel.heightProperty().multiply(0.45));
        leftCircle.radiusProperty().bind(welcomePanel.heightProperty().multiply(0.2));
        rightCircle.radiusProperty().bind(welcomePanel.heightProperty().multiply(0.2));
        buttonsContainer.spacingProperty().bind(welcomePanel.heightProperty().multiply(0.04));
        playButton.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34));
        playButton.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34));
        creatorButton.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34 * 0.5));
        creatorButton.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34 * 0.5));
        leaderboardButton.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34 * 0.5));
        leaderboardButton.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34 * 0.5));
        settingsButton.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34 * 0.3));
        settingsButton.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34 * 0.3));
        settingsContainer.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34 / 2));
        settingsContainer.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34 / 2));
    }

}
