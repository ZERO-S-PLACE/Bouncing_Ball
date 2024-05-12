package org.zeros.bouncy_balls.Controllers;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;

import java.net.URL;
import java.util.ResourceBundle;

public class GamePanelController implements Initializable {

    public AnchorPane gameBackground;
    private Animation animation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Level level=Level.load("program_data/user_levels/try2.ser");
        if(level!=null) {
            animation = new Animation(level);
        }
        setBackground();
        for (MovingObject object:animation.getLevel().movingObjects()){
            gameBackground.getChildren().add(object.getShape());
        }
        for (Area obstacle:animation.getLevel().obstacles()){
            gameBackground.getChildren().add(obstacle.getPath());
        }
        new Thread(animation::animate).start();

    }


    private void setBackground() {
        gameBackground.setMinHeight(animation.getPROPERTIES().getHEIGHT());
        gameBackground.setMaxHeight(animation.getPROPERTIES().getHEIGHT());
        gameBackground.setMinWidth(animation.getPROPERTIES().getWIDTH());
        gameBackground.setMaxWidth(animation.getPROPERTIES().getWIDTH());
        gameBackground.backgroundProperty().setValue(new Background(new BackgroundFill(Color.rgb(249, 211, 165), new CornerRadii(5), new Insets(0))));
    }
}
