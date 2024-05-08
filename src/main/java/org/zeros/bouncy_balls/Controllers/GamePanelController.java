package org.zeros.bouncy_balls.Controllers;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;
import org.zeros.bouncy_balls.Objects.Obstacles.OvalObstacle;
import org.zeros.bouncy_balls.Objects.Obstacles.PolylineObstacle;
import org.zeros.bouncy_balls.Objects.Obstacles.RectangleObstacle;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GamePanelController implements Initializable {

    public AnchorPane gameBackground;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setBackground();
        //new Thread(animation::animate).start();

    }
/*

    private void setBackground() {
        gameBackground.setMinHeight(animation.PROPERTIES.getHEIGHT());
        gameBackground.setMaxHeight(animation.PROPERTIES.getHEIGHT());
        gameBackground.setMinWidth(animation.PROPERTIES.getWIDTH());
        gameBackground.setMaxWidth(animation.PROPERTIES.getWIDTH());
        gameBackground.backgroundProperty().setValue(new Background(new BackgroundFill(Color.rgb(249, 211, 165), new CornerRadii(5), new Insets(0))));
    }*/
}
