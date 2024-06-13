package org.zeros.bouncy_balls.Model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Properties {
    private static final ArrayList<String> animationGenres=new ArrayList<>(List.of("Simple","Friction","Gravity","Electrostatic","Space","User"));
    private static final ArrayList<String> animationGenresDescriptions=new ArrayList<>(List.of(
            "Animation of simple bounces without gravity or friction",
            "Animation of bounces of objects considering friction",
            "Animation of objects moves in homogeneous gravitational field",
            "Animation of objects moves considering their charges",
            "Animation of moves in gravitational fields",
            "User levels"));

    public static ArrayList<String> getAnimationGenres() {
        return animationGenres;
    }
    public static ArrayList<String> getAnimationGenresDescriptions() {
        return animationGenresDescriptions;
    }
    private static double SIZE_FACTOR = Math.pow(10, 5);
    private static Color BACKGROUND_COLOR = Color.web("#243253");
    private static double ACCURACY = Math.pow(0.1, 3);
    private static double FRAME_RATE=120;
    private static int MAX_EVALUATIONS=150;



    public static double FRAME_RATE() {
        return FRAME_RATE;
    }

    public static void setFRAME_RATE(double FRAME_RATE) {
        Properties.FRAME_RATE = FRAME_RATE;
    }

    public static int MAX_EVALUATIONS() {
        return MAX_EVALUATIONS;
    }

    public void setMAX_EVALUATIONS(int MAX_EVALUATIONS) {
        Properties.MAX_EVALUATIONS = MAX_EVALUATIONS;
    }

    public static Color BACKGROUND_COLOR() {
        return BACKGROUND_COLOR;
    }

    public static void setBackgroundColor(Color backgroundColor) {
        BACKGROUND_COLOR = backgroundColor;
    }

    public static double ACCURACY() {
        return ACCURACY;
    }

    public static double SIZE_FACTOR() {
        return SIZE_FACTOR;
    }

    public static void setSizeFactor(double sizeFactor) {
        SIZE_FACTOR = sizeFactor;
    }

    public static void setACCURACY(double ACCURACY) {
        Properties.ACCURACY = ACCURACY;
    }


}
