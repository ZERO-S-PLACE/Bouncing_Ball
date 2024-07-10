package org.zeros.bouncy_balls.DisplayUtil;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Applications.GameApplication.Controllers.P4_LevelSelection.LevelListCellController;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Model;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.util.Objects;

public class BackgroundImages {
    public static void setObstacleBackground(Area obstacle) {
        obstacle.getPath().setFill(Properties.OBSTACLE_COLOR());
        obstacle.getPath().setStrokeWidth(0);
    }
    public static void setObstacleBackground(Shape obstacle) {
        obstacle.setFill(Properties.OBSTACLE_COLOR());
        obstacle.setStrokeWidth(0);
    }
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
        String imagePath = Objects.requireNonNull(LevelListCellController.class.getResource("/Icons/General/CircleYellow.png")).toExternalForm();
        node.setStyle("-fx-fill: url('" + imagePath + "');");
    }

    public static void setBallCannotEnterBackground(Node node) {
        String imagePath = Objects.requireNonNull(LevelListCellController.class.getResource("/Icons/General/CircleRed.png")).toExternalForm();
        node.setStyle("-fx-fill: url('" + imagePath + "');");
    }

}
