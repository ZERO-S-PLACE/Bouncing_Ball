package org.zeros.bouncy_balls.DisplayUtil;

import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;

public class CustomScrollPaneSkin extends ScrollPaneSkin {

    public CustomScrollPaneSkin(ScrollPane scrollPane) {
        super(scrollPane);
        ScrollBar vBar = (ScrollBar) scrollPane.lookup(".scroll-bar:vertical");
        if (vBar != null) {
            vBar.translateXProperty().bind(scrollPane.widthProperty().negate());
        }
    }
}
