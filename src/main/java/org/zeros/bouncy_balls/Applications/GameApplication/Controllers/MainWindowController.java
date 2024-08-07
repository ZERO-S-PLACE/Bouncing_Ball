package org.zeros.bouncy_balls.Applications.GameApplication.Controllers;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Model;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    public BorderPane bottomLayer;
    public BorderPane topLayer;
    public BorderPane mainPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPanel.setBackground(new Background(new BackgroundFill(Properties.BACKGROUND_COLOR(), null, null)));
    }

    public void loadStartNodes() throws IOException {
        loadBackgroundAnimation();
        changeTopLayer(Model.getInstance().getViewFactory().getWelcomePanel(), 2);
    }

    public void changeTopLayer(Pane pane, double wait) {
        changeLayer(topLayer, pane, wait);
    }

    public void changeBottomLayer(Pane pane, double wait) {
        changeLayer(bottomLayer, pane, wait);
    }

    private void changeLayer(BorderPane layer, Pane pane, double wait) {
        if (!layer.getChildren().isEmpty()) {
            animateLayerChange(layer, pane, wait / 2);
        } else {
            Platform.runLater(() -> {
                layer.setOpacity(0);
                layer.setCenter(pane);
            });
            animateLayerAppear(layer, wait);
        }
    }


    public void loadBackgroundAnimation() {
        AnimationPane animationPane = Model.getInstance().getViewFactory().getBackgroundAnimation();
        bottomLayer.setOpacity(0);
        bottomLayer.setCenter(animationPane.getAnimationPane());
        animationPane.startGame();
        animateLayerAppear(bottomLayer, 0.1);
    }

    private void animateLayerAppear(Pane pane, double wait) {
        AnimationTimer animationTimer = new AnimationTimer() {
            final long waitTime = (long) wait * (10 ^ 9);
            long startTime = 0;

            @Override
            public void handle(long now) {
                if (startTime == 0) {
                    startTime = now;
                }
                long timeElapsed = now - startTime;
                if (timeElapsed < Properties.ANIMATION_DURATION() + waitTime) {
                    if (timeElapsed > waitTime) {
                        pane.setOpacity((double) (timeElapsed - waitTime) / Properties.ANIMATION_DURATION());
                    }
                } else {
                    this.stop();
                }
            }

        };
        animationTimer.start();

    }

    public void animateLayerChange(BorderPane pane, Pane layer, double wait) {
        AnimationTimer animationTimer = new AnimationTimer() {
            final long waitTime = (long) wait * (10 ^ 9);
            long startTime = 0;

            @Override
            public void handle(long now) {
                if (startTime == 0) {
                    startTime = now;
                }
                long timeElapsed = now - startTime;
                if (timeElapsed < Properties.ANIMATION_DURATION() + waitTime) {
                    if (timeElapsed > waitTime) {
                        pane.setOpacity(1 - (double) (timeElapsed - waitTime) / Properties.ANIMATION_DURATION());
                    }
                } else {
                    this.stop();
                }
            }

            @Override
            public void stop() {
                super.stop();
                pane.setOpacity(0);
                pane.setCenter(layer);
                animateLayerAppear(pane, 0);
            }
        };
        animationTimer.start();

    }

}
