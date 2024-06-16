package org.zeros.bouncy_balls.DisplayUtil;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import org.zeros.bouncy_balls.Model.Properties;

public class NodeAnimations {

    public static void increaseBrightness(Node node, double value) {
        ColorAdjust colorAdjust = new ColorAdjust();
        node.setEffect(colorAdjust);
        colorAdjust.setBrightness(Math.abs(value));
        colorAdjust.setContrast(Math.abs(value));
    }

    public static void decreaseBrightness(Node node, double value) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-Math.abs(value));
        colorAdjust.setContrast(-Math.abs(value));
        node.setEffect(colorAdjust);
    }

    public static void increaseBrightnessOnExit(Node node) {
        ColorAdjust colorAdjust = new ColorAdjust();
        node.setEffect(colorAdjust);
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

    public static void resetBrightness(Node node) {
        node.setEffect(null);
    }

}
