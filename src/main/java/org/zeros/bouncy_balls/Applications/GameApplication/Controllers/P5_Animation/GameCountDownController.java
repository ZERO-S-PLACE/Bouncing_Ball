package org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P5_Animation;

import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class GameCountDownController implements Initializable {
    public Text countText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void countDownAndRun(boolean firstStart) {
        countText.setText("3");
        AnimationTimer animationTimer = new AnimationTimer() {
            final long DURATION = 1_200_000_000L;
            long startTime = 0;
            int step = 0;

            @Override
            public void handle(long now) {
                if (startTime == 0) {
                    startTime = now;
                }
                long timeElapsed = now - startTime;
                if (timeElapsed < DURATION) {
                    changeNumberAtTime(timeElapsed);
                } else {
                    this.stop();
                }
            }

            private void changeNumberAtTime(long timeElapsed) {
                if (timeElapsed > DURATION * 0.3 && step < 1) {
                    step++;
                    countText.setText("2");
                } else if (timeElapsed > DURATION * 0.6 && step < 2) {
                    step++;
                    countText.setText("1");
                } else if (timeElapsed > DURATION * 0.8 && step < 3) {
                    step++;
                    countText.setText("0");
                }
            }

            @Override
            public void stop() {
                super.stop();
                if (firstStart) {
                    Model.getInstance().getViewFactory().getCurrentAnimationPane().addGameOverlay();
                    Model.getInstance().getViewFactory().getCurrentAnimationPane().startGame();
                } else {
                    Model.getInstance().getViewFactory().getCurrentAnimationPane().resume();
                }
                Model.getInstance().controllers().getMainWindowController().changeTopLayer(null, 0.1);
                Model.getInstance().controllers().getMainWindowController().topLayer.setMouseTransparent(true);
            }
        };
        animationTimer.start();
    }

}
