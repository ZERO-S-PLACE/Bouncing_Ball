package org.zeros.bouncy_balls.Level;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.Animation.Borders.BordersType;
import org.zeros.bouncy_balls.Controllers.LevelCreatorController;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.Area.OvalArea;
import org.zeros.bouncy_balls.Objects.Area.PolylineArea;
import org.zeros.bouncy_balls.Objects.Area.RectangleArea;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.SerializableObjects.LevelSerializable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class LevelCreator {

    private Level level;
    private Animation animation;

    private static Double getTextValue(String text) {

        double value;
        try {
            value = Double.parseDouble(text);
        } catch (Exception e) {
            Platform.runLater(() -> Model.getInstance().getLevelCreatorController().messageLabel.textProperty().setValue("Wrong value"));
            return null;
        }
        return value;
    }

    private static String getStringInput(String message) {
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().messageLabel.textProperty().setValue(message));
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().setTextEntered(""));
        waitForInput();
        return Model.getInstance().getLevelCreatorController().getTextEntered();
    }

    private static void waitForInput() {
        Object lock = LevelCreatorController.getLock();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void create() {
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().removeAll(Model.getInstance().getLevelCreatorController().preview.getChildren()));
        level = new Level(getAnimationProperties());
        animation = new Animation(level);
        addElements();
        simulateAnimation();
        if (agreeTo("Save level ? Y/N")) {
            saveLevel(getStringInput("Enter name: "));
        }
        if (agreeTo("Create next ? Y/N")) {
            this.create();
        } else Platform.exit();
    }

    private void simulateAnimation() {
        if (agreeTo("Check animation?")) {
            animation = new Animation(level);
            Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().removeAll(Model.getInstance().getLevelCreatorController().preview.getChildren()));
            for (Area obstacle : animation.getLevel().getObstacles()) {
                Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().add(obstacle.getPath()));
            }
            for (MovingObject obj : animation.getLevel().getMovingObjects()) {
                obj.setAnimation(animation);
                Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().add(obj.getShape()));
            }
            new Thread(() -> animation.animate()).start();
            while (true) {
                if (agreeTo("Stop animation?")) {
                    animation.pause();
                    break;
                }
            }
        }
    }

    private void addElements() {
        int choice = 1;
        while (choice != 0) {
            choice = (int) getNumber("What to do? 0-exit 1-add Obstacle 2-add Moving Object 3-add target area 4 -add input area");
            switch (choice) {
                case 1 -> addObstacles();
                case 2 -> addMovingObjects();
                //case 3->addTargetArea();
                //case 4->addInputArea();
            }
        }
    }

    private AnimationProperties getAnimationProperties() {

        int HEIGHT = Math.abs((int) getNumber("Animation height:(px) "));
        int WIDTH = Math.abs((int) getNumber("Animation width (px): "));

        AnimationProperties properties = new AnimationProperties(HEIGHT, WIDTH);

        if (agreeTo("Custom properties Y/N")) {
            properties.setTIME(Math.abs(getNumber("Time of animation: ")));
            properties.setGRAVITY(getGravity());
            properties.setBOUNDARIES(getBordersType());
            properties.setFRICTION(getNumber("Friction 0-1"));
            if (properties.getFRICTION() > 1 || properties.getFRICTION() < 0) {
                properties.setFRICTION(0);
            }
            if (agreeTo("Is this simulation(Y) or game(N)? ")) {
                properties.setTYPE(AnimationType.SIMULATION);
            }
            if (agreeTo("Custom speed Y/N")) {
                properties.setFRAME_RATE(Math.abs((int) getNumber("Frame rate: ")));
                properties.setMAX_EVALUATIONS(Math.abs((int) getNumber("Max evaluations per frame: ")));
            }
        }
        setPreviewBoundaries(HEIGHT, WIDTH);
        return properties;
    }

    private void addMovingObjects() {
        while (true) {
            if (agreeTo(" Add new moving object ? N/Y")) {
                addMovingBall();
            } else return;
        }
    }

    private void addMovingBall() {

        Point2D center = getPoint("Center:");
        int radius = (int) getDimension("Radius", center);
        Ball ball = new Ball(radius, animation);
        ball.updateCenter(center);
        ball.updateNextCenter(ball.center());
        level.addMovingObject(ball);
        Circle velocityShadow = new Circle(radius);
        velocityShadow.setOpacity(0.1);
        velocityShadow.setFill(Color.BLACK);
        updateVelocityShadow(velocityShadow);
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().add(velocityShadow));
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().add(ball.getShape()));

        modifyBall(ball, velocityShadow);
        if (!animation.hasFreePlace(ball)) {
            removeBall(ball, velocityShadow);
            if (agreeTo("Not enough space,try again?")) addMovingBall();
        } else if (!agreeTo("Save?")) {
            removeBall(ball, velocityShadow);
        }

    }

    private void removeBall(Ball ball, Circle velocityShadow) {
        level.removeMovingObject(ball);
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().remove(ball.getShape()));
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().remove(velocityShadow));
    }

    private void updateVelocityShadow(Circle velocityShadow) {
        Ball ball = ((Ball) level.getMovingObjects().getLast());
        velocityShadow.setRadius(ball.getRadius());
        velocityShadow.setCenterX(ball.center().add(ball.velocity()).getX());
        velocityShadow.setCenterY(ball.center().add(ball.velocity()).getY());
    }

    private void modifyBall(Ball ball, Circle velocityShadow) {
        while (true) {
            switch ((int) getNumber("Change property:0-no 1- velocity,2-friction,3-mass,4-radius, 5-center")) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    ball.setInitialVelocity(getPoint("NextCenter: ").subtract(ball.center()));
                    ball.updateNextCenter(ball.center());
                }
                case 2 -> ball.setFriction(getNumber("Friction: "));
                case 3 -> ball.setMass(getNumber("Mass: "));
                case 4 -> ball.setRadius(getDimension("Radius: ", ball.center()));
                case 5 -> {
                    ball.updateCenter(getPoint("Center:"));
                    ball.updateNextCenter(ball.center());
                }
            }
            updateVelocityShadow(velocityShadow);
        }
    }

    private void addObstacles() {
        while (true) {
            if (agreeTo(" Add new obstacle ? N/Y")) {
                switch ((int) getNumber(" Obstacle types: 0-rectangle 1-oval 2-poly line")) {
                    case 0 -> addRectangle();
                    case 1 -> addOval();
                    case 2 -> addPolyLine();
                }
            } else return;
        }
    }

    private void addObstacle(Area obstacle) {
        level.addObstacle(obstacle);
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().add(obstacle.getPath()));
    }

    private void removeObstacle(Area obstacle) {
        level.removeObstacle(obstacle);
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().remove(obstacle.getPath()));
    }

    private void addRectangle() {
        RectangleArea rectangle = new RectangleArea(getPoint("Corner 1:"), getPoint("Corner 3:"), 0);
        rectangle = modifyRectangle(rectangle);
        addObstacle(rectangle);
        if (!agreeTo("Save?")) {
            removeObstacle(rectangle);
        }
    }

    private RectangleArea modifyRectangle(RectangleArea rectangle) {
        while (true) {
            RectangleArea temp = rectangle;
            addObstacle(temp);
            switch ((int) getNumber("Change property:0-no 1- corner 1,2-rotation,3-corner 3")) {
                case 0 -> {
                    removeObstacle(temp);
                    return rectangle;
                }
                case 1 ->
                        rectangle = new RectangleArea(getPoint("Corner 1:"), rectangle.getCorners().get(2), rectangle.getRotation());
                case 2 ->
                        rectangle = new RectangleArea(rectangle.getCorners().getFirst(), rectangle.getCorners().get(2), getNumber("Rotation") / 360 * 2 * Math.PI);
                case 3 ->
                        rectangle = new RectangleArea(rectangle.getCorners().get(0), getPoint("Corner 3:"), rectangle.getRotation());
            }
            removeObstacle(temp);
        }
    }

    private void addOval() {
        Point2D center = getPoint("Center:");
        OvalArea oval = new OvalArea(center, getDimension("Radius X:", center), getDimension("Radius Y:", center), 0);
        oval = modifyOval(oval);
        addObstacle(oval);
        if (!agreeTo("Save?")) {
            removeObstacle(oval);
        }
    }

    private OvalArea modifyOval(OvalArea oval) {
        while (true) {
            OvalArea temp = oval;
            addObstacle(temp);
            switch ((int) getNumber("Change property:0-no 1- center ,2-rotation,3-radius X 4-radius Y")) {
                case 0 -> {
                    removeObstacle(temp);
                    return oval;
                }
                case 1 ->
                        oval = new OvalArea(getPoint("Center:"), oval.getRadiusX(), oval.getRadiusY(), oval.getRotation());
                case 2 ->
                        oval = new OvalArea(oval.getMassCenter(), oval.getRadiusX(), oval.getRadiusY(), getNumber("Rotation") / 360 * 2 * Math.PI);
                case 3 ->
                        oval = new OvalArea(oval.getMassCenter(), getDimension("Radius X:", oval.getMassCenter()), oval.getRadiusY(), oval.getRotation());
                case 4 ->
                        oval = new OvalArea(oval.getMassCenter(), oval.getRadiusX(), getDimension("Radius Y:", oval.getMassCenter()), oval.getRotation());
            }
            removeObstacle(temp);
        }

    }

    private void addPolyLine() {
        Point2D start = getPoint("Start:");
        PolylineArea plObst = new PolylineArea(start);
        addObstacle(plObst);
        modifyPolyLine(plObst);
        if (!agreeTo("Save?")) {
            removeObstacle(plObst);
        }


    }

    private void modifyPolyLine(PolylineArea plObst) {
        while (true) {
            switch ((int) getNumber("Next segment:0-no 1- line ,2- quad curve,3-cubic curve 4-removeLast")) {
                case 0 -> {
                    plObst.closeAndSave();
                    return;
                }
                case 1 -> plObst.drawStraightSegmentTo(getPoint("Next Point: "));
                case 2 -> plObst.drawQuadCurveTo(getPoint("Control Point: "), getPoint("Next Point: "));
                case 3 ->
                        plObst.drawCubicCurveTo(getPoint("Control Point 1: "), getPoint("Control Point 2: "), getPoint("Next Point: "));
                case 4 -> plObst.removeLastSegment();
            }
        }


    }

    private void setPreviewBoundaries(int HEIGHT, int WIDTH) {
        Model.getInstance().getLevelCreatorController().preview.setMinSize(WIDTH, HEIGHT);
        Model.getInstance().getLevelCreatorController().preview.setMaxSize(WIDTH, HEIGHT);
        Model.getInstance().getLevelCreatorController().preview.backgroundProperty().set(Background.fill(Color.BEIGE));
    }

    private BordersType getBordersType() {
            int value = (int) getNumber("Border types: 0-bouncing, 1-connected, 2-infinite");
            switch (value) {
                case 0 -> {
                    return BordersType.BOUNCING;
                }
                case 1 -> {
                    return BordersType.CONNECTED;
                }
                case 2 -> {
                    return BordersType.INFINITE;
                }
                default -> {
                    return getBordersType();
                }
            }

    }

    private double getGravity() {

            double value = getNumber("Gravity strength");
            if (value > 0 && value < 100) {
                return value;
            } else {
                return getGravity();
            }

    }

    private double getDimension(String message, Point2D reference) {
        Model.getInstance().getLevelCreatorController().setSelectedPoint(reference);
        while (true) {
            String userInput = getStringInput(message);
            if (Model.getInstance().getLevelCreatorController().getLastEvent() instanceof KeyEvent) {
                Double value = getTextValue(userInput);
                if (value != null) return Math.abs(value);
            } else {
                return Model.getInstance().getLevelCreatorController().getPickedDistance();
            }
        }
    }

    public double getNumber(String message) {
        while (true) {
            String userInput = getStringInput(message);
            Double value = getTextValue(userInput);
            if (value != null) return value;
        }

    }

    public boolean agreeTo(String message) {
        while (true) {
            switch (getStringInput(message)) {
                case "Y", "y", "1" -> {
                    return true;
                }
                case "N", "n", "0" -> {
                    return false;
                }
                default ->
                        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().messageLabel.textProperty().setValue("Wrong value"));

            }

        }


    }

    public Point2D getPoint(String message) {
        double x = getDimension(message + " X:", null);
        if (!(Model.getInstance().getLevelCreatorController().getLastEvent() instanceof KeyEvent)) {
            return Model.getInstance().getLevelCreatorController().getSelectedPoint();
        }
        double y = getDimension(message + " Y:", null);
        if (!(Model.getInstance().getLevelCreatorController().getLastEvent() instanceof KeyEvent)) {
            return Model.getInstance().getLevelCreatorController().getSelectedPoint();
        }
        return new Point2D(x, y);
    }

    private void saveLevel(String name) {
        name = name.replace(" ", "_");
        name = name.replace(".", "_");
        level.setNAME(name);
        LevelSerializable save = new LevelSerializable(level);
        name = name + ".ser";
        if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
            name = "program_data/user_levels/" + name;
        } else {
            name = "program_data/user_simulations/" + name;
        }
        try (FileOutputStream fileOut = new FileOutputStream(name); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(save);
        } catch (IOException e) {
            e.printStackTrace();
            if (agreeTo("Saving failed, try again?")) {
                saveLevel(getStringInput("Enter new name:"));
            }
        }
    }
}
