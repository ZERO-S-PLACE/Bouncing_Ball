package org.zeros.bouncy_balls.DisplayUtil;

import javafx.scene.Node;
import org.zeros.bouncy_balls.Controllers.P4_LevelSelection.LevelListCellController;

import java.util.Objects;

public class BackgroundImages {
    public static void setStarBackground(Node node, String color) {

        String imagePath = Objects.requireNonNull(LevelListCellController.class.getResource("/Icons/General/Star" + color + ".png")).toExternalForm();
        node.setStyle("-fx-background-image: url('" + imagePath + "');");
    }

    public static void setCircleBackground(Node node, String color) {
        String imagePath = Objects.requireNonNull(LevelListCellController.class.getResource("/Icons/General/Circle" + color + ".png")).toExternalForm();
        node.setStyle("-fx-background-image: url('" + imagePath + "');");
    }
}
