package org.zeros.bouncy_balls.Animation.Animation;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRun;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRunMovingObject;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRunObstacle;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.util.ArrayList;

public class AnimationPane {
    private final AnchorPane gameBackground;
    private Animation animation;
    private InputOnRun input;
    private final EventHandler<MouseEvent> inputOnRunHandler = this::inputOnRunHandler;
    private BorderPane clockContainer;
    private BorderPane pauseContainer;
    private Button pauseButton;
    private final EventHandler<KeyEvent> escHandler = this::escHandler;

    public AnimationPane(String path) {
        gameBackground = new AnchorPane();
        loadLevel(path);
        setUp();

    }
    public AnimationPane(Level level) {
        gameBackground = new AnchorPane();
        animation = new Animation(level);
        setUp();
    }

    public AnchorPane getAnimationPane() {
        return gameBackground;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Level getLevel() {
        return animation.getLevel();
    }

    public void addGameOverlay() {
        pauseContainer = new BorderPane();
        pauseButton = new Button();
        pauseButton.getStyleClass().add("pause-button");
        pauseButton.setPrefSize(40, 40);
        pauseContainer.setCenter(pauseButton);
        gameBackground.getChildren().add(pauseContainer);
        AnchorPane.setLeftAnchor(pauseContainer, 10.0);
        AnchorPane.setTopAnchor(pauseContainer, 10.0);
        pauseButton.setOnAction(e -> pauseGame());
    }

    public void pauseGame() {
        if (input != null) {
            input.dismiss();
            input = null;
        }
        animation.pause();
        Model.getInstance().controllers().getGamePausedController().setConfigurationAtPaused();
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getGamePausedPanel(), 0.1);
        Model.getInstance().controllers().getMainWindowController().topLayer.setMouseTransparent(false);
        Model.getInstance().controllers().getMainWindowController().middleLayer.setMouseTransparent(false);
        pauseButton.setVisible(false);
    }

    public void resume() {
        pauseButton.setVisible(false);
        animation.resume();
    }

    private void setUp() {
        gameBackground.sceneProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.heightProperty().removeListener(sizeChangeListener());
            }
            if (newValue != null) {
                newValue.heightProperty().addListener(sizeChangeListener());
                reloadNodes(newValue.heightProperty().get() / animation.getLevel().PROPERTIES().getHEIGHT() * Properties.SIZE_FACTOR());
            }
        }));

    }

    public void startGame() {
        new Thread(animation::animate).start();
        addKeyEventsListeners();
    }

    private void addKeyEventsListeners() {
        gameBackground.addEventHandler(KeyEvent.KEY_PRESSED, escHandler);
        gameBackground.addEventHandler(MouseEvent.MOUSE_CLICKED, inputOnRunHandler);
    }

    private void inputOnRunHandler(MouseEvent mouseEvent) {
        if (input == null) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                addNewMovingObjectOnRun();
            } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                addNewObstacleOnRun();

            }
        }
    }

    private void addNewObstacleOnRun() {
        if (!animation.getLevel().getObstaclesToAdd().isEmpty()) {
            Area obstacle = animation.getLevel().getObstaclesToAdd().getFirst();
            input = new InputOnRunObstacle(obstacle, animation, gameBackground);
            new Thread(() -> input.insert()).start();
            input.finishedProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) input = null;
            }));
        }
    }

    private void addNewMovingObjectOnRun() {
        if (!animation.getLevel().getMovingObjectsToAdd().isEmpty()) {
            addComplexAreaPreview(animation.getLevel().getInputArea(), Color.web("#435477"));
            MovingObject object = animation.getLevel().getMovingObjectsToAdd().getFirst();
            input = new InputOnRunMovingObject(object, gameBackground);
            new Thread(() -> input.insert()).start();
            input.finishedProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    Platform.runLater(() -> {
                        addComplexAreaPreview(animation.getLevel().getInputArea(), Color.TRANSPARENT);
                        input = null;
                    });
                }
            }));
        }
    }

    private void escHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            if (input == null) {
                pauseGame();
            } else {
                input.dismiss();
            }
        }
    }


    private ChangeListener<Number> sizeChangeListener() {
        return (observable, oldValue, newValue) -> reloadNodes(newValue.doubleValue() / animation.getLevel().PROPERTIES().getHEIGHT() * Properties.SIZE_FACTOR());
    }


    public void loadLevel(String path) {
        Level level = Level.load(path);
        if (level != null) {
            animation = new Animation(level);
        }
    }

    private void reloadNodes(double scaleFactor) {
        if (scaleFactor > 0 && scaleFactor != 1) {
            try {
                animation.getLevel().rescale(scaleFactor);
            } catch (Exception ignored) {
            }
        }
        animation.reloadBorders();
        gameBackground.getChildren().removeAll(gameBackground.getChildren());
        setBackground();
        addComplexAreaPreview(animation.getLevel().getTargetArea(), Color.web("#081633"));
        for (Area obstacle : animation.getLevel().getObstacles()) {
            gameBackground.getChildren().add(obstacle.getPath());
            obstacle.getPath().setFill(Properties.OBSTACLE_COLOR());
        }

        for (MovingObject object : animation.getLevel().getMovingObjects()) {
            BackgroundImages.setBallStandardBackground(object.getShape());
            gameBackground.getChildren().add(object.getShape());
        }
        for (MovingObject object : animation.getLevel().getMovingObjectsHaveToEnter()) {
            BackgroundImages.setBallHaveToEnterBackground(object.getShape());
        }
        for (MovingObject object : animation.getLevel().getMovingObjectsCannotEnter()) {
            BackgroundImages.setBallCannotEnterBackground(object.getShape());
        }


    }

    private void setBackground() {
        gameBackground.setMinHeight(animation.getPROPERTIES().getHEIGHT() / Properties.SIZE_FACTOR());
        gameBackground.setMaxHeight(animation.getPROPERTIES().getHEIGHT() / Properties.SIZE_FACTOR());
        gameBackground.setMinWidth(animation.getPROPERTIES().getWIDTH() / Properties.SIZE_FACTOR());
        gameBackground.setMaxWidth(animation.getPROPERTIES().getWIDTH() / Properties.SIZE_FACTOR());

    }

    private void addComplexAreaPreview(ComplexArea complexArea, Color color) {
        if (complexArea != null) {
            ArrayList<ComplexAreaPart> included = complexArea.partAreas();
            addAreaLayer(included, color);
        }

    }

    private void addAreaLayer(ArrayList<ComplexAreaPart> included, Color color) {
        ArrayList<ComplexAreaPart> excluded = new ArrayList<>();

        for (ComplexAreaPart part : included) {
            part.area().getPath().setFill(color);
            excluded.addAll(part.excluded());
            Platform.runLater(() -> {
                gameBackground.getChildren().remove(part.area().getPath());
                gameBackground.getChildren().add(part.area().getPath());
            });
        }
        ArrayList<ComplexAreaPart> included2 = new ArrayList<>();
        for (ComplexAreaPart part : excluded) {
            part.area().getPath().setFill(Properties.BACKGROUND_COLOR());
            included2.addAll(part.excluded());
            Platform.runLater(() -> {
                gameBackground.getChildren().remove(part.area().getPath());
                gameBackground.getChildren().add(part.area().getPath());
            });
        }
        if (!included2.isEmpty()) {
            addAreaLayer(included2, color);
        }
    }


}


