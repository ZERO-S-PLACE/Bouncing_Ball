package org.zeros.bouncy_balls.DisplayUtil;

import javafx.scene.control.Tooltip;

public class CustomTooltip extends Tooltip {
    public CustomTooltip (String message){
        super(message);
        this.getStyleClass().add("custom-tooltip");
    }
}
