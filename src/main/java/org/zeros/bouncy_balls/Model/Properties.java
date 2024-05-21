package org.zeros.bouncy_balls.Model;

import javafx.scene.paint.Color;

public class Properties {
    private static  Color BACKGROUND_COLOR= Color.BEIGE;

    public static Color BACKGROUND_COLOR() {
        return BACKGROUND_COLOR;
    }

    public static void setBackgroundColor(Color backgroundColor) {
        BACKGROUND_COLOR = backgroundColor;
    }

    public static double ACCURACY() {
        return ACCURACY;
    }

    public static void setACCURACY(double ACCURACY) {
        Properties.ACCURACY = ACCURACY;
    }

    private static  double ACCURACY =0.00001;


}
