package org.zeros.bouncy_balls.DisplayUtil;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;

public class NodeAnimations {

    public static void increaseBrightness(Button playButton) {
        ColorAdjust colorAdjust=new ColorAdjust();
        playButton.setEffect(colorAdjust);
        colorAdjust.setBrightness(0.25);
        colorAdjust.setContrast(0.25);

    }
    public static void increaseBrightnessOnExit(Button playButton) {

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
    public static void decreaseBrightness(Button playButton) {
        playButton.setEffect(null);
    }

}
