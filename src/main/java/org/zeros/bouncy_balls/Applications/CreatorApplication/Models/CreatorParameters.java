package org.zeros.bouncy_balls.Applications.CreatorApplication.Models;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class CreatorParameters {
    private static final int DEFAULT_WIDTH =800;
    private static final int DEFAULT_HEIGHT =500;
    private static final int DEFAULT_X_OFFSET =((int)Screen.getPrimary().getVisualBounds().getWidth()-DEFAULT_WIDTH-400)/2;
    private static final int DEFAULT_Y_OFFSET =((int)Screen.getPrimary().getVisualBounds().getHeight()-DEFAULT_HEIGHT-100)/2;
    private static final KeyCode SWITCH_KEY_CODE=KeyCode.S;
    private static final KeyCode CLOSE_KEY_CODE=KeyCode.C;
    private static final double DEFAULT_LINE_WEIGHT=3.0;
    private static final Color sidesOverlayColor= Color.rgb(16, 31, 35);
    public static int getDEFAULT_WIDTH() {
        return DEFAULT_WIDTH;
    }

    public static int getDEFAULT_HEIGHT() {
        return DEFAULT_HEIGHT;
    }
    public static int getDEFAULT_X_OFFSET() {
        return DEFAULT_X_OFFSET;
    }
    public static int getDEFAULT_Y_OFFSET() {
        return DEFAULT_Y_OFFSET;
    }
    public static Point2D getDEFAULT_OFFSET_POINT(){return new Point2D(CreatorParameters.getDEFAULT_X_OFFSET(),CreatorParameters.getDEFAULT_Y_OFFSET());}
    public static KeyCode getSWITCH_KEY_CODE() {
        return SWITCH_KEY_CODE;
    }

    public static KeyCode getCLOSE_KEY_CODE() {
        return CLOSE_KEY_CODE;
    }
    public static double getDEFAULT_LINE_WEIGHT() {
        return DEFAULT_LINE_WEIGHT;
    }
    public static Color getSidesOverlayColor() {
        return sidesOverlayColor;
    }
}
