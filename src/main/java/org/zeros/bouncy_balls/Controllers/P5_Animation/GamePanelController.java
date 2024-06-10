package org.zeros.bouncy_balls.Controllers.P5_Animation;

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
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.OvalArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.RectangleArea;

import java.net.URL;
import java.util.ArrayList;
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
        loadLevel("program_data/user_levels/try21.ser");
        animation.getLevel().getMovingObjectsHaveToEnter().removeAll(animation.getLevel().getMovingObjectsHaveToEnter());

        animation.getLevel().addMovingObjectCannotEnter(animation.getLevel().getMovingObjects().getLast());
        animation.getLevel().addMovingObjectHaveToEnter(animation.getLevel().getMovingObjects().getFirst());
        animation.getLevel().PROPERTIES().setGRAVITY(1000*Properties.SIZE_FACTOR);
        animation.getLevel().PROPERTIES().setFRICTION(0.0001);
        animation.getLevel().PROPERTIES().setTIME(600);
        animation.getLevel().addMovingObjectToAdd(new Ball(5 * Properties.SIZE_FACTOR, animation));
        animation.getLevel().getMovingObjects().getLast().setInitialVelocity(new Point2D(300 * Properties.SIZE_FACTOR, 300 * Properties.SIZE_FACTOR));
        animation.getLevel().addMovingObjectToAdd(new Ball(5 * Properties.SIZE_FACTOR, animation));
        animation.getLevel().getMovingObjects().getLast().setInitialVelocity(new Point2D(90, 90));
        animation.getLevel().addObstacleToAdd(new OvalArea(new Point2D(-10000 * Properties.SIZE_FACTOR, -10000 * Properties.SIZE_FACTOR), 30, 70, 0));
        animation.getLevel().addObstacleToAdd(new RectangleArea(new Point2D(-20 * Properties.SIZE_FACTOR, -20 * Properties.SIZE_FACTOR), new Point2D(-30 * Properties.SIZE_FACTOR, -50 * Properties.SIZE_FACTOR), 0));
        animation.getLevel().addObstacleToAdd(new OvalArea(new Point2D(-10000 * Properties.SIZE_FACTOR, -10000 * Properties.SIZE_FACTOR), 30 * Properties.SIZE_FACTOR, 50 * Properties.SIZE_FACTOR, 0));


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
        if (getScaleFactor() != 1) {
            animation.getLevel().rescale(getScaleFactor());
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
    }

    private void setBackground() {
        gameBackground.setMinHeight(animation.getPROPERTIES().getHEIGHT() / Properties.SIZE_FACTOR());
        gameBackground.setMaxHeight(animation.getPROPERTIES().getHEIGHT() / Properties.SIZE_FACTOR());
        gameBackground.setMinWidth(animation.getPROPERTIES().getWIDTH() / Properties.SIZE_FACTOR());
        gameBackground.setMaxWidth(animation.getPROPERTIES().getWIDTH() / Properties.SIZE_FACTOR());
        gameBackground.backgroundProperty().setValue(new Background(new BackgroundFill(Color.rgb(249, 211, 165), new CornerRadii(0), new Insets(0))));
    }

    private void addComplexAreaPreview(ComplexArea complexArea, Color color) {

        ArrayList<ComplexAreaPart> included = complexArea.partAreas();
        addAreaLayer(included, color);

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
