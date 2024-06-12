package org.zeros.bouncy_balls.Animation.Animation;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRun;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRunMovingObject;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRunObstacle;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AnimationPane  {
    public AnchorPane getAnimationPane() {
        return gameBackground;
    }

    private AnchorPane gameBackground;
    private Animation animation;
    private InputOnRun input;

    public AnimationPane (String path){
        gameBackground=new AnchorPane();
        setUp(path);
    }

    private void setUp(String path) {
        loadLevel(path);
        reloadNodes();
        if(animation.getLevel().PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
            Platform.runLater(this::addInputOnRun);
        }


    }
    public void startGame() {
        addRescaleObserver();
        new Thread(animation::animate).start();
    }

    private void addRescaleObserver() {
        if (getScaleFactor() != 1) reloadNodes();
        gameBackground.getScene().widthProperty().addListener((observable, oldValue, newValue) -> reloadNodes());
        gameBackground.getScene().heightProperty().addListener((observable, oldValue, newValue) -> reloadNodes());

    }

    private synchronized double getScaleFactor() {
        double factor1 = gameBackground.getScene().getHeight() / animation.getLevel().PROPERTIES().getHEIGHT() * Properties.SIZE_FACTOR();
        double factor2 = gameBackground.getScene().getWidth() / animation.getLevel().PROPERTIES().getWIDTH() * Properties.SIZE_FACTOR();
        return Math.min(factor1, factor2);
    }


    public void addInputOnRun() {
        if (!animation.getLevel().getMovingObjectsToAdd().isEmpty()) {
            MovingObject object = animation.getLevel().getMovingObjectsToAdd().getFirst();
            input = new InputOnRunMovingObject(object, gameBackground);
            new Thread(() -> input.insert()).start();
            animation.getLevel().removeMovingObjectToAdd(object);
        } else if (!animation.getLevel().getObstaclesToAdd().isEmpty()) {
            Area obstacle = animation.getLevel().getObstaclesToAdd().getFirst();
            input = new InputOnRunObstacle(obstacle, animation, gameBackground);
            new Thread(() -> input.insert()).start();
            animation.getLevel().removeObstacleToAdd(obstacle);
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
        try {
            animation.getLevel().rescale(getScaleFactor());
        }catch (Exception ignored){}
            animation.reloadBorders();
            gameBackground.getChildren().removeAll(gameBackground.getChildren());
            setBackground();
        addComplexAreaPreview(animation.getLevel().getInputArea(), Color.GOLD);
        addComplexAreaPreview(animation.getLevel().getTargetArea(), Color.DARKGOLDENROD);
        for (Area obstacle : animation.getLevel().getObstacles()) {
            gameBackground.getChildren().add(obstacle.getPath());
        }
        for (MovingObject object : animation.getLevel().getMovingObjects()) {
            object.getShape().setFill(Color.GRAY);
            gameBackground.getChildren().add(object.getShape());
        }
        for (MovingObject object : animation.getLevel().getMovingObjectsHaveToEnter()) {
            object.getShape().setFill(Color.RED);
        }
        for (MovingObject object : animation.getLevel().getMovingObjectsCannotEnter()) {
            object.getShape().setFill(Color.BLACK);
        }


    }

    private void setBackground() {
        gameBackground.setMinHeight(animation.getPROPERTIES().getHEIGHT() / Properties.SIZE_FACTOR());
        gameBackground.setMaxHeight(animation.getPROPERTIES().getHEIGHT() / Properties.SIZE_FACTOR());
        gameBackground.setMinWidth(animation.getPROPERTIES().getWIDTH() / Properties.SIZE_FACTOR());
        gameBackground.setMaxWidth(animation.getPROPERTIES().getWIDTH() / Properties.SIZE_FACTOR());

    }

    private void addComplexAreaPreview(ComplexArea complexArea, Color color) {
        if(complexArea!=null) {

            ArrayList<ComplexAreaPart> included = complexArea.partAreas();
            addAreaLayer(included, color);
        }

    }

    private void addAreaLayer(ArrayList<ComplexAreaPart> included, Color color) {
        ArrayList<ComplexAreaPart> excluded = new ArrayList<>();

        for (ComplexAreaPart part : included) {
            part.area().getPath().setFill(color);
            excluded.addAll(part.excluded());
            if (!gameBackground.getChildren().contains(part.area().getPath())) {
                gameBackground.getChildren().remove(part.area().getPath());
            }
            gameBackground.getChildren().add(part.area().getPath());
        }
        ArrayList<ComplexAreaPart> included2 = new ArrayList<>();
        for (ComplexAreaPart part : excluded) {
            part.area().getPath().setFill(Color.WHITE);
            included2.addAll(part.excluded());
            if (!gameBackground.getChildren().contains(part.area().getPath())) {
                gameBackground.getChildren().remove(part.area().getPath());
            }
            gameBackground.getChildren().add(part.area().getPath());
        }
        if (!included2.isEmpty()) {
            addAreaLayer(included2, color);
        }
    }



}
