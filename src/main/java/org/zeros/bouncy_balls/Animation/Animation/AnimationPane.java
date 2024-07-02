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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRun;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRunMovingObject;
import org.zeros.bouncy_balls.Animation.InputOnRun.InputOnRunObstacle;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;
import org.zeros.bouncy_balls.DisplayUtil.ClockMeasurement;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

public class AnimationPane {
    private final AnchorPane gameBackground;
    private HBox measurementsBox;
    private ClockMeasurement timeMeasurement;
    private ClockMeasurement movingObjectsToAddCounter;
    private ClockMeasurement obstaclesToAddCounter;
    private Button pauseButton;
    private Animation animation;
    private InputOnRun inputOnRun;
    private final EventHandler<MouseEvent> inputOnRunHandler = this::inputOnRunHandler;
    private final EventHandler<KeyEvent> escHandler = this::escHandler;

    public AnimationPane(String path,boolean rescale) {
        gameBackground = new AnchorPane();
        loadLevel(path);
        if(rescale) addRescaling();

    }

    public AnimationPane(Level level,boolean rescale) {
        gameBackground = new AnchorPane();
        animation = new Animation(level);
        if(rescale) addRescaling();

    }

    private void addRescaling() {
        gameBackground.sceneProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.heightProperty().removeListener(sizeChangeListener());
            }
            if (newValue != null) {
                reloadNodes(newValue.heightProperty().get() / animation.getLevel().PROPERTIES().getHEIGHT() * Properties.SIZE_FACTOR());
                newValue.heightProperty().addListener(sizeChangeListener());
            }
        }));
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

    public void startGame() {
        new Thread(animation::animate).start();
        addKeyEventsListeners();
    }

    private void addKeyEventsListeners() {
        gameBackground.addEventHandler(KeyEvent.KEY_PRESSED, escHandler);
        gameBackground.addEventHandler(MouseEvent.MOUSE_CLICKED, inputOnRunHandler);
    }

    private void escHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            if (inputOnRun == null) {
                pauseGame();
            } else {
                inputOnRun.dismiss();
            }
        }
    }

    public void pauseGame() {
        dismissInputOnRun();
        animation.pause();
        pauseButton.setVisible(false);
    }

    private void dismissInputOnRun() {
        if (inputOnRun != null) {
            inputOnRun.dismiss();
            inputOnRun = null;
        }
    }

    public void resume() {
        pauseButton.setVisible(true);
        animation.resume();
    }


    public void addGameOverlay() {
        addPauseButton();
        createMeasurementsBox();
        if ((animation.getPROPERTIES().getTYPE().equals(AnimationType.GAME))) {
            addMovingObjectsToAddCounter();
            addObstaclesToAddCounter();
            updateCountersValues();
        }
        addTimeMeasurement();
        rescaleOverlay();
    }

    private void createMeasurementsBox() {
        measurementsBox = new HBox();
        measurementsBox.setOpacity(0.7);
        AnchorPane.setRightAnchor(measurementsBox, 10.0);
        AnchorPane.setTopAnchor(measurementsBox, 10.0);
        gameBackground.getChildren().add(measurementsBox);
    }

    private void addObstaclesToAddCounter() {
        obstaclesToAddCounter = new ClockMeasurement(animation.getLevel().getObstaclesToAdd().size(), "O");
        measurementsBox.getChildren().add(obstaclesToAddCounter);
        obstaclesToAddCounter.getController().setMeasurementColorStart(Color.web("#BCBBAC"));
        obstaclesToAddCounter.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (inputOnRun == null) addNewObstacleOnRun();
        });
    }

    private void addMovingObjectsToAddCounter() {
        movingObjectsToAddCounter = new ClockMeasurement(animation.getLevel().getMovingObjectsToAdd().size(), "M");
        measurementsBox.getChildren().add(movingObjectsToAddCounter);
        movingObjectsToAddCounter.getController().setMeasurementColorStart(Color.web("#BCBBAC"));
        movingObjectsToAddCounter.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (inputOnRun == null) addNewMovingObjectOnRun();
        });
    }

    private void updateCountersValues() {
        obstaclesToAddCounter.getController().setValue(animation.getLevel().getObstaclesToAdd().size());
        movingObjectsToAddCounter.getController().setValue(animation.getLevel().getMovingObjectsToAdd().size());
    }

    private void addTimeMeasurement() {
        timeMeasurement = new ClockMeasurement(animation.getPROPERTIES().getTIME(), "T");
        measurementsBox.getChildren().add(timeMeasurement);
        timeMeasurement.setMouseTransparent(true);
        timeMeasurement.getController().setValueSubtracted(animation.getLevel().PROPERTIES().getTIME());
        animation.timeElapsedNanosProperty().addListener(((observable, oldValue, newValue) -> timeMeasurement.getController().setValueSubtracted((double) (newValue.longValue() / 1_000_000_000))));
    }

    private void addPauseButton() {
        pauseButton = new Button();
        pauseButton.getStyleClass().add("pause-button");
        pauseButton.setPrefSize(40, 40);
        gameBackground.getChildren().add(pauseButton);
        AnchorPane.setLeftAnchor(pauseButton, 10.0);
        AnchorPane.setTopAnchor(pauseButton, 10.0);
        pauseButton.setOnAction(e -> pauseGame());
    }

    private void inputOnRunHandler(MouseEvent mouseEvent) {
        if (inputOnRun == null) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                addNewMovingObjectOnRun();
            } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                addNewObstacleOnRun();
            }
        }
    }

    private void addNewObstacleOnRun() {
        if (!animation.getLevel().getObstaclesToAdd().isEmpty()) {
            measurementsBox.setMouseTransparent(true);
            Area obstacle = animation.getLevel().getObstaclesToAdd().getFirst();
            inputOnRun = new InputOnRunObstacle(obstacle, animation, gameBackground);
            new Thread(() -> inputOnRun.insert()).start();
            inputOnRun.finishedProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> {
                measurementsBox.setMouseTransparent(false);
                inputOnRun = null;
                updateCountersValues();
            })));
        }
    }

    private void addNewMovingObjectOnRun() {
        if (!animation.getLevel().getMovingObjectsToAdd().isEmpty()) {
            measurementsBox.setMouseTransparent(true);
            ComplexArea.addComplexAreaToPane(animation.getLevel().getInputArea(), Color.web("#435477"), gameBackground);
            MovingObject object = animation.getLevel().getMovingObjectsToAdd().getFirst();
            inputOnRun = new InputOnRunMovingObject(object, gameBackground);
            new Thread(() -> inputOnRun.insert()).start();
            inputOnRun.finishedProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    Platform.runLater(() -> {
                        measurementsBox.setMouseTransparent(false);
                        ComplexArea.addComplexAreaToPane(animation.getLevel().getInputArea(), Color.TRANSPARENT, getAnimationPane());
                        inputOnRun = null;
                        updateCountersValues();
                    });
                }
            }));
        }
    }


    public void reloadNodes(double scaleFactor) {
        rescaleAnimation(scaleFactor);
        animation.reloadBorders();
        setBackground();
        gameBackground.getChildren().removeAll(gameBackground.getChildren());
        reloadAnimationElements();
        if (pauseButton != null && measurementsBox != null) {
            gameBackground.getChildren().add(pauseButton);
            gameBackground.getChildren().add(measurementsBox);
            Platform.runLater(this::rescaleOverlay);
        }
    }

    private void rescaleAnimation(double scaleFactor) {
        if (scaleFactor > 0 && scaleFactor != 1) {
            try {
                animation.getLevel().rescale(scaleFactor);
            } catch (Exception e) {
                throw new RuntimeException("Animation cannot be rescaled");
            }
        }
    }

    private void rescaleOverlay() {
        measurementsBox.setSpacing(gameBackground.getHeight() / 50);
        timeMeasurement.getController().setSize(gameBackground.getHeight() / 6);
        if (movingObjectsToAddCounter != null) {
            movingObjectsToAddCounter.getController().setSize(gameBackground.getHeight() / 9);
        }
        if (obstaclesToAddCounter != null) {
            obstaclesToAddCounter.getController().setSize(gameBackground.getHeight() / 9);
        }
        updateCountersValues();
    }

    private void reloadAnimationElements() {
        ComplexArea.addComplexAreaToPane(animation.getLevel().getTargetArea(), Color.web("#081633"), gameBackground);
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


    public AnchorPane getAnimationPane() {
        return gameBackground;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Level getLevel() {
        return animation.getLevel();
    }
}