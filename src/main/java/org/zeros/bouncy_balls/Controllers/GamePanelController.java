package org.zeros.bouncy_balls.Controllers;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Animation.InputOnRun;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;

import java.net.URL;
import java.util.ResourceBundle;

public class GamePanelController implements Initializable {

    public AnchorPane gameBackground;
    private Animation animation;
    private InputOnRun input;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadLevel("program_data/user_levels/try3.ser");

        animation.getLevel().PROPERTIES().setTime(60);
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(15, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(35, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(15, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(35, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(15, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(15, animation));
        addInputOnRun();


        new Thread(animation::animate).start();

    }

    public void addInputOnRun() {
        if (!animation.getLevel().movingObjectsToAdd().isEmpty()) {
            input = new InputOnRun(animation.getLevel().movingObjectsToAdd().getFirst(), gameBackground, animation);
            animation.getLevel().movingObjectsToAdd().removeFirst();
        } else {
            input = null;
        }
    }

    public void loadLevel(String path) {
        Level level = Level.load(path);

        if (level != null) {
            animation = new Animation(level);
        }
        setBackground();
        for (Area obstacle : animation.getLevel().obstacles()) {
            gameBackground.getChildren().add(obstacle.getPath());
        }
        for (MovingObject object : animation.getLevel().movingObjects()) {
            gameBackground.getChildren().add(object.getShape());
        }
    }


    private void setBackground() {
        gameBackground.setMinHeight(animation.getPROPERTIES().getHEIGHT());
        gameBackground.setMaxHeight(animation.getPROPERTIES().getHEIGHT());
        gameBackground.setMinWidth(animation.getPROPERTIES().getWIDTH());
        gameBackground.setMaxWidth(animation.getPROPERTIES().getWIDTH());
        gameBackground.backgroundProperty().setValue(new Background(new BackgroundFill(Color.rgb(249, 211, 165), new CornerRadii(5), new Insets(0))));
    }

}
