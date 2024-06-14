package org.zeros.bouncy_balls.DisplayUtil;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import org.zeros.bouncy_balls.Model.Properties;

public class NodeAnimations {

    public static void increaseBrightness(Button playButton, double value) {
        ColorAdjust colorAdjust = new ColorAdjust();
        playButton.setEffect(colorAdjust);
        colorAdjust.setBrightness(Math.abs(value));
        colorAdjust.setContrast(Math.abs(value));
    }

    public static void decreaseBrightness(Button playButton, double value) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-Math.abs(value));
        colorAdjust.setContrast(-Math.abs(value));
        playButton.setEffect(colorAdjust);
    }

    public static void increaseBrightnessOnExit(Button playButton) {
        ColorAdjust colorAdjust = new ColorAdjust();
        playButton.setEffect(colorAdjust);
        AnimationTimer animationTimer = new AnimationTimer() {
            long startTime = 0;

            @Override
            public void handle(long now) {
                if (startTime == 0) {
                    startTime = now;
                }
                long timeElapsed = now - startTime;
                if (timeElapsed < Properties.ANIMATION_DURATION()) {
                    if (timeElapsed < Properties.ANIMATION_DURATION() * 0.75) {
                        colorAdjust.setBrightness((double) timeElapsed / Properties.ANIMATION_DURATION());
                        colorAdjust.setContrast((double) timeElapsed / Properties.ANIMATION_DURATION());
                    } else {
                        colorAdjust.setBrightness(0.75 - (double) timeElapsed / Properties.ANIMATION_DURATION());
                        colorAdjust.setContrast(0.75 - (double) timeElapsed / Properties.ANIMATION_DURATION());
                    }
                } else {
                    this.stop();
                }
            }

        };
        animationTimer.start();

    }

    public static void resetBrightness(Button playButton) {
        playButton.setEffect(null);
    }

}
