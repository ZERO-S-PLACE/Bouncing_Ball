package org.zeros.bouncy_balls.Controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    public BorderPane bottomLayer;
    public BorderPane middleLayer;
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

    public void changeMiddleLayer(Pane pane, double wait) {
        changeLayer(middleLayer, pane, wait);
    }

    public void changeBottomLayer(Pane pane, double wait) {
        changeLayer(bottomLayer, pane, wait);
    }

    private void changeLayer(BorderPane layer, Pane pane, double wait) {
        new Thread(() -> {
            if (!layer.getChildren().isEmpty()) {
                clearLayer(layer, wait / 2);
            }
            Platform.runLater(() -> {
                layer.setOpacity(0);
                layer.setCenter(pane);
            });
            new Thread(() -> {
                try {
                    animatePaneAppear(layer, wait / 2);
                } catch (IOException | InterruptedException e) {
                    showFilesDamagedLabel();
                }
            }).start();
        }).start();

    }

    public void clearLayer(BorderPane pane, double wait) {
        Thread t1 = new Thread(() -> {
            try {
                animateLayerDisappear(pane, wait);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadBackgroundAnimation() {
        AnimationPane animationPane = Model.getInstance().getViewFactory().getBackgroundAnimation();
        bottomLayer.setOpacity(0);
        bottomLayer.setCenter(animationPane.getAnimationPane());
        animationPane.startGame();

        new Thread(() -> {
            try {
                animatePaneAppear(bottomLayer, 0.1);
            } catch (IOException | InterruptedException e) {
                showFilesDamagedLabel();
            }
        }).start();
    }

    private void animatePaneAppear(Pane pane, double wait) throws IOException, InterruptedException {
        Thread.sleep((long) (wait * 1000));
        for (int i = 1; i <= 30; i++) {
            pane.setOpacity((double) i / 30);
            Thread.sleep(30);
        }
    }

    public void animateLayerDisappear(BorderPane layer, double wait) throws IOException, InterruptedException {
        Thread.sleep((long) (wait * 1000));
        for (int i = 30; i >= 0; i--) {
            layer.setOpacity((double) i / 30);
            Thread.sleep(30);
        }
        Platform.runLater(() -> {
            layer.getChildren().removeAll(layer.getChildren());
            layer.setOpacity(1);
        });

    }

    private void showFilesDamagedLabel() {
        AnchorPane pane = new AnchorPane();
        topLayer.setCenter(pane);
        pane.getChildren().add(new Label("ERROR 404 "));
        pane.getChildren().add(new Label("Files are damaged, please reinstall"));
    }
}
