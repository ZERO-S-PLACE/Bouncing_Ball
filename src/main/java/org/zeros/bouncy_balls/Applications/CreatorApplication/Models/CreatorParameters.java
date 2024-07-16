package org.zeros.bouncy_balls.Applications.CreatorApplication.Models;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;

public class CreatorParameters {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 500;
    private static final int DEFAULT_X_OFFSET = ((int) Screen.getPrimary().getVisualBounds().getWidth() - DEFAULT_WIDTH - 400) / 2;
    private static final int DEFAULT_Y_OFFSET = ((int) Screen.getPrimary().getVisualBounds().getHeight() - DEFAULT_HEIGHT - 100) / 2;
    private static final KeyCode CONFIRM_KEY_CODE = KeyCode.ENTER;
    private static final KeyCode ESCAPE_KEY_CODE = KeyCode.ESCAPE;

    public static int getDEFAULT_X_OFFSET() {
        return DEFAULT_X_OFFSET;
    }

    public static int getDEFAULT_Y_OFFSET() {
        return DEFAULT_Y_OFFSET;
    }

    public static Point2D getDEFAULT_OFFSET_POINT() {
        return new Point2D(CreatorParameters.getDEFAULT_X_OFFSET(), CreatorParameters.getDEFAULT_Y_OFFSET());
    }

    public static KeyCode CONFIRM_KEY_CODE() {
        return CONFIRM_KEY_CODE;
    }

    public static KeyCode ESCAPE_KEY_CODE() {
        return ESCAPE_KEY_CODE;
    }

}
