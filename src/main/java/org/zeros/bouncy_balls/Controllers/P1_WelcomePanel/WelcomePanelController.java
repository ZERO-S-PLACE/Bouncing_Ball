package org.zeros.bouncy_balls.Controllers.P1_WelcomePanel;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.zeros.bouncy_balls.App.LevelCreatorApplication;
import org.zeros.bouncy_balls.Model.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomePanelController implements Initializable {
    public Button playButton;
    public Button creatorButton;
    public Button leaderboardButton;
    public Button settingsButton;
    public BorderPane settingsContainer;
    public BorderPane welcomePanel;
    public VBox buttonsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRescaling();
        setEnterAnimation();
        playButton.setOnAction(event -> transitionToGameSelection());
        creatorButton.setOnAction(event -> transitionToLevelCreator());

    }

    private void transitionToLevelCreator() {
        increaseBrightnessOnExit(playButton);
        Thread t1=new Thread(()->Model.getInstance().controllers().getMainWindowController().changeTopLayer(new Pane(),0.3));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(()-> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> {
                new LevelCreatorApplication().start(new Stage());
                Model.getInstance().getViewFactory().getWelcomePanel().getScene().getWindow().hide();
            });

        }).start();
    }

    private void transitionToGameSelection() {
        increaseBrightnessOnExit(playButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getWelcomePanel(),0.3);
    }

    private void setEnterAnimation() {
        playButton.setOnMouseEntered(event-> increaseBrightness(playButton));
        playButton.setOnMouseExited(event->decreaseBrightness(playButton));
        creatorButton.setOnMouseEntered(event-> increaseBrightness(creatorButton));
        creatorButton.setOnMouseExited(event->decreaseBrightness(creatorButton));
        settingsButton.setOnMouseEntered(event-> {
                    increaseBrightness(settingsButton);
                    startRotationAnimation(settingsButton);
                });
        settingsButton.setOnMouseExited(event->{decreaseBrightness(settingsButton);
        stopRotationAnimation(settingsButton);});
        leaderboardButton.setOnMouseEntered(event-> increaseBrightness(leaderboardButton));
        leaderboardButton.setOnMouseExited(event->decreaseBrightness(leaderboardButton));
    }
    RotateTransition rotateTransition;
    private void startRotationAnimation(Button button) {
        rotateTransition = new RotateTransition(Duration.seconds(7), button);
        rotateTransition.setByAngle(600);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.play();
    }

    private void stopRotationAnimation(Button button) {
        if(rotateTransition!=null) {
            rotateTransition.stop();
        }
    }

    private void increaseBrightness(Button playButton) {
        ColorAdjust colorAdjust=new ColorAdjust();
        playButton.setEffect(colorAdjust);
        colorAdjust.setBrightness(0.25);
        colorAdjust.setContrast(0.25);

    }
    private void increaseBrightnessOnExit(Button playButton) {

        ColorAdjust colorAdjust = new ColorAdjust();
        playButton.setEffect(colorAdjust);
        new Thread(()-> {
            for(int i=1;i<20;i++) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                colorAdjust.setBrightness(0.03*i);
                colorAdjust.setContrast(0.03*i);
            }
            for(int i=20;i>1;i--) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                colorAdjust.setBrightness(0.03*i);
                colorAdjust.setContrast(0.03*i);
            }
        }).start();
    }
    private void decreaseBrightness(Button playButton) {
        playButton.setEffect(null);
    }

    private void setRescaling() {
        /*welcomePanel.prefWidthProperty().addListener((obs, oldVal, newVal) -> {
            int newWidth= (int) Math.min((Integer) newVal,welcomePanel.getHeight()*widthToHightRatio);
            welcomePanel.setStyle("-fx-background-size: " + newWidth + "px "+ (int)(newWidth/widthToHightRatio) + "px ;");
        });

        welcomePanel.prefHeightProperty().addListener((obs, oldVal, newVal) -> {
                    int newHeight = (int) Math.min((Integer) newVal, welcomePanel.getWidth() / widthToHightRatio);
                    welcomePanel.setStyle("-fx-background-size: " + (int) (newHeight * widthToHightRatio) + "px " + newHeight + "px ;");
                });*/
        buttonsContainer.spacingProperty().bind(welcomePanel.heightProperty().multiply(0.04));
        playButton.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34));
        playButton.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34));
        creatorButton.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34*0.5));
        creatorButton.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34*0.5));
        leaderboardButton.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34*0.5));
        leaderboardButton.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34*0.5));
        settingsButton.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34*0.3));
        settingsButton.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34*0.3));
        settingsContainer.prefHeightProperty().bind(welcomePanel.heightProperty().multiply(0.34/2));
        settingsContainer.prefWidthProperty().bind(welcomePanel.heightProperty().multiply(0.34/2));
    }

}
