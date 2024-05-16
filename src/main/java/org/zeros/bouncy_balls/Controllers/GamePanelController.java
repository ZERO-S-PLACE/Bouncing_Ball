package org.zeros.bouncy_balls.Controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRun;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRunMovingObject;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRunObstacle;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.Area.OvalArea;
import org.zeros.bouncy_balls.Objects.Area.RectangleArea;
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
        Platform.runLater(this::setUp);
    }

    private void setUp() {
        loadLevel("program_data/user_levels/try8.ser");
        animation.getLevel().PROPERTIES().setFRICTION(0.0001);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 2; j++) {
                Ball ball = new Ball(3, animation);
                ball.updateCenter(new Point2D(700 + 20 * i, 50 + 10 * j));
                ball.updateNextCenter(new Point2D(700 + 20 * i, 50 + 10 * j));
                animation.level.movingObjects().add(ball);
            }
        }
        animation.getLevel().PROPERTIES().setTIME(60);
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        animation.getLevel().movingObjectsToAdd().getLast().setInitialVelocity(new Point2D(300, 300));
        animation.getLevel().movingObjectsToAdd().add(new Ball(15, animation));
        animation.getLevel().movingObjectsToAdd().getLast().setInitialVelocity(new Point2D(90, 90));
        animation.getLevel().obstaclesToAdd().add(new OvalArea(new Point2D(-10000, -10000), 30, 70, 0));
        animation.getLevel().obstaclesToAdd().add(new RectangleArea(new Point2D(-20, -20), new Point2D(-30, -50), 0));
        animation.getLevel().obstaclesToAdd().add(new OvalArea(new Point2D(-10000, -10000), 30, 50, 0));


        addRescaleObserver();
        new Thread(this::startGame).start();

    }

    private void startGame() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(this::addInputOnRun);
        new Thread(animation::animate).start();
    }

    private void addRescaleObserver() {
        if (getScaleFactor() != 1) reloadNodes();
        gameBackground.getScene().widthProperty().addListener((observable, oldValue, newValue) -> reloadNodes());
        gameBackground.getScene().heightProperty().addListener((observable, oldValue, newValue) -> reloadNodes());

    }

    private synchronized double getScaleFactor() {
        double factor1 = gameBackground.getScene().getHeight() / animation.getLevel().PROPERTIES().getHEIGHT();
        double factor2 = gameBackground.getScene().getWidth() / animation.getLevel().PROPERTIES().getWIDTH();
        return Math.min(factor1, factor2);
    }

    public InputOnRun getInputOnRun() {
        return input;
    }

    public void addInputOnRun() {
        if (!animation.getLevel().movingObjectsToAdd().isEmpty()) {
            input = new InputOnRunMovingObject(animation.getLevel().movingObjectsToAdd().getFirst(), gameBackground);
            new Thread(() -> input.insert()).start();
            animation.getLevel().movingObjectsToAdd().removeFirst();
        } else if (!animation.getLevel().obstaclesToAdd().isEmpty()) {
            input = new InputOnRunObstacle(animation.getLevel().obstaclesToAdd().getFirst(), animation, gameBackground);
            new Thread(() -> input.insert()).start();
            animation.getLevel().obstaclesToAdd().removeFirst();
        } else {
            input = null;
        }
    }

    public void loadLevel(String path) {
        Level level = Level.load(path);
        if (level != null) {
            animation = new Animation(level);
        }
    }

    private synchronized void reloadNodes() {
        if (getScaleFactor() != 1) {
            animation.getLevel().rescale(getScaleFactor());
            animation.reloadBorders();
            gameBackground.getChildren().removeAll(gameBackground.getChildren());
            setBackground();
            for (Area obstacle : animation.getLevel().obstacles()) {
                gameBackground.getChildren().add(obstacle.getPath());
            }
            for (MovingObject object : animation.getLevel().movingObjects()) {
                gameBackground.getChildren().add(object.getShape());
            }
        }
    }

    private void setBackground() {
        gameBackground.setMinHeight(animation.getPROPERTIES().getHEIGHT());
        gameBackground.setMaxHeight(animation.getPROPERTIES().getHEIGHT());
        gameBackground.setMinWidth(animation.getPROPERTIES().getWIDTH());
        gameBackground.setMaxWidth(animation.getPROPERTIES().getWIDTH());
        gameBackground.backgroundProperty().setValue(new Background(new BackgroundFill(Color.rgb(249, 211, 165), new CornerRadii(0), new Insets(0))));
    }


}
