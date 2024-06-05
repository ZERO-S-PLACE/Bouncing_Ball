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

    public static double SIZE_FACTOR=Math.pow(10,5);

    public static double SIZE_FACTOR() {
        return SIZE_FACTOR;
    }

    public static void setSizeFactor(double sizeFactor) {
        SIZE_FACTOR = sizeFactor;
    }

    public static void setACCURACY(double ACCURACY) {
        Properties.ACCURACY = ACCURACY;
    }

    private static  double ACCURACY =Math.pow(0.1,5);


}
