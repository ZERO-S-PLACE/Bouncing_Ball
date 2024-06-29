package org.zeros.bouncy_balls.DisplayUtil;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Controllers.P4_LevelSelection.LevelListCellController;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;

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

    public static void setGameBackground(Pane pane, boolean borderVisible) {
        if (borderVisible) {
            pane.setStyle("-fx-background-color: #243253; ");
            Model.getInstance().controllers().getMainWindowController().mainPanel.setBackground(new Background(new BackgroundFill(Color.web("#A6D4ED"), null, null)));

        } else {
            pane.setStyle("-fx-background-color: #243253; ");
            Model.getInstance().controllers().getMainWindowController().mainPanel.setBackground(new Background(new BackgroundFill(Properties.BACKGROUND_COLOR(), null, null)));
        }
    }

    public static void setBallStandardBackground(Node node) {
        String imagePath = Objects.requireNonNull(LevelListCellController.class.getResource("/Icons/General/CircleGreen.png")).toExternalForm();
        node.setStyle("-fx-fill: url('" + imagePath + "');");
    }

    public static void setBallHaveToEnterBackground(Node node) {
        String imagePath = Objects.requireNonNull(LevelListCellController.class.getResource("/Icons/General/CircleBlue.png")).toExternalForm();
        node.setStyle("-fx-fill: url('" + imagePath + "');");
    }

    public static void setBallCannotEnterBackground(Node node) {
        String imagePath = Objects.requireNonNull(LevelListCellController.class.getResource("/Icons/General/CircleRed.png")).toExternalForm();
        node.setStyle("-fx-fill: url('" + imagePath + "');");
    }

}
