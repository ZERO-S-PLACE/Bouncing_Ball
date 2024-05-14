package org.zeros.bouncy_balls.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
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
    private final EventHandler<MouseEvent> startGameHandler = this::startGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gameBackground.addEventHandler(MouseEvent.MOUSE_CLICKED, startGameHandler);

    }

    private void startGame(MouseEvent mouseEvent) {
        gameBackground.removeEventHandler(MouseEvent.MOUSE_CLICKED, startGameHandler);
        loadLevel("program_data/user_levels/try4.ser");
        animation.getLevel().PROPERTIES().setTIME(60);
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(15, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(35, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(15, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(35, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        animation.getLevel().movingObjectsToAdd().add(new Ball(5, animation));
        addInputOnRun();
        addRescaleObserver();

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
        System.out.println(factor1+" "+factor2);

        return Math.min(factor1, factor2);

    }

    public void addInputOnRun() {
        if (!animation.getLevel().movingObjectsToAdd().isEmpty()) {
            new InputOnRun(animation.getLevel().movingObjectsToAdd().getFirst(), gameBackground, animation);
            animation.getLevel().movingObjectsToAdd().removeFirst();
        }
    }

    public void loadLevel(String path) {
        Level level = Level.load(path);
        if (level != null) {
            animation = new Animation(level);
        }

    }

    private synchronized void reloadNodes() {
        if(getScaleFactor()!=1) {

            animation.getLevel().rescale(getScaleFactor());
            animation.reloadBorders();
            gameBackground.getChildren().removeAll(gameBackground.getChildren());
            setBackground();
            //gameBackground.getScene().getWindow().setHeight(gameBackground.getHeight()+50);
            //gameBackground.getScene().getWindow().setWidth(gameBackground.getWidth()+5);
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
