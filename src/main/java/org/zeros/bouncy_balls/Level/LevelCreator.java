package org.zeros.bouncy_balls.Level;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.Animation.Borders.BordersType;
import org.zeros.bouncy_balls.Calculations.AreasMath.SimpleComplexAreaBoolean;
import org.zeros.bouncy_balls.Calculations.AreasMath.SimpleSimpleAreaBoolean;
import org.zeros.bouncy_balls.Calculations.ConvexHull.ConvexHull;
import org.zeros.bouncy_balls.Controllers.P2c_LevelCreator.LevelCreatorController;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.SerializableObjects.LevelSerializable;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.OvalArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.RectangleArea;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class LevelCreator {

    private Level level;
    private Animation animation;
    private boolean objectInStartState = true;

    private static Double getTextValue(String text) {

        double value;
        try {
            value = Double.parseDouble(text);
        } catch (Exception e) {
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().messageLabel.textProperty().setValue("Wrong value"));
            return null;
        }
        return value;
    }

    private static String getStringInput(String message) {
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().messageLabel.textProperty().setValue(message));
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().setTextEntered(""));
        waitForInput();
        return Model.getInstance().controllers().getLevelCreatorController().getTextEntered();
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

    private static void addAreaLayer(ArrayList<ComplexAreaPart> included, Color color) {
        ArrayList<ComplexAreaPart> excluded = new ArrayList<>();

        for (ComplexAreaPart part : included) {
            part.area().getPath().setFill(color);
            excluded.addAll(part.excluded());
            if (!Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().contains(part.area().getPath())) {
                Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().remove(part.area().getPath()));
            }
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(part.area().getPath()));
        }
        ArrayList<ComplexAreaPart> included2 = new ArrayList<>();
        for (ComplexAreaPart part : excluded) {
            part.area().getPath().setFill(Color.WHITE);
            included2.addAll(part.excluded());
            if (!Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().contains(part.area().getPath())) {
                Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().remove(part.area().getPath()));
            }
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(part.area().getPath()));
        }
        if (!included2.isEmpty()) {
            addAreaLayer(included2, color);
        }
    }

    private static void checkingHullCalculations(Area obstacle) {
        ArrayList<Point2D> hull = ConvexHull.calculatePoints(obstacle.getAllPoints());
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(new Circle(hull.getFirst().getX(), hull.getFirst().getY(), 8)));

        for (Point2D point : obstacle.getAllPoints()) {
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(new Circle(point.getX(), point.getY(), 1)));
        }
        for (int i = 1; i < hull.size(); i++) {

            int finalI = i;
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(new Line(hull.get(finalI - 1).getX(), hull.get(finalI - 1).getY(), hull.get(finalI).getX(), hull.get(finalI).getY())));
            Circle circle = new Circle(hull.get(finalI).getX(), hull.get(finalI).getY(), 3);
            circle.setFill(Color.RED);
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(circle));
        }
    }

    public void create() {
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().removeAll(Model.getInstance().controllers().getLevelCreatorController().preview.getChildren()));
        level = new Level(getAnimationProperties());
        animation = new Animation(level);
        addComplexArea(true);
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
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().removeAll(Model.getInstance().controllers().getLevelCreatorController().preview.getChildren()));
            for (Area obstacle : animation.getLevel().getObstacles()) {
                Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(obstacle.getPath()));
            }
            for (MovingObject obj : animation.getLevel().getMovingObjects()) {
                obj.setAnimation(animation);
                Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(obj.getShape()));
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
            choice = (int) getNumber("What to do? 0-exit 1-add Obstacle 2-add Moving Object " + "3-add Obstacle to add in runtime 4-add Moving Object to add in runtime 5-add target area 6 -add input area");
            switch (choice) {
                case 1 -> {
                    objectInStartState = true;
                    addObstacles();
                }
                case 2 -> {
                    objectInStartState = true;
                    addMovingObjects();
                }
                case 3 -> {
                    if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
                        objectInStartState = false;
                        addObstacles();
                    }
                }
                case 4 -> {
                    if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
                        objectInStartState = false;
                        addMovingObjects();
                    }
                }
                case 5 -> {
                    if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
                        addComplexArea(true);
                    }
                }
                case 6 -> {
                    if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
                        addComplexArea(false);
                    }
                }

            }
        }
    }

    private void addComplexArea(boolean isTargetArea) {
        ComplexArea complexArea = new ComplexArea();
        if (isTargetArea) complexArea.setColor(new Color(0.5, 0.5, 0.5, 0.5));
        else complexArea.setColor(new Color(0.5, 0, 0, 0.5));
        while (true) {
            System.out.println("__________");
            System.out.println("Sub areas: " + complexArea.partAreas().size());
            System.out.println("Included areas: " + complexArea.getAllIncludedAreas().size());
            System.out.println("Excluded areas: " + complexArea.getAllExcludedAreas().size());

            switch ((int) getNumber("0-dismiss 1-include area, 2 exclude area ,3 intersection ,4 exclude currentArea from new 5- save")) {
                case 0 -> {
                    removeComplexAreaPreview(complexArea);
                    return;
                }
                case 1 -> {
                    Area areaToAdd = createNewArea();
                    removeObstaclePreview(areaToAdd);
                    removeComplexAreaPreview(complexArea);
                    SimpleComplexAreaBoolean boolMath = new SimpleComplexAreaBoolean(areaToAdd, complexArea);
                    complexArea = boolMath.sum();
                    addComplexAreaPreview(complexArea);
                }
                case 2 -> {
                    Area areaToExclude = createNewArea();
                    removeObstaclePreview(areaToExclude);

                    removeComplexAreaPreview(complexArea);
                    SimpleComplexAreaBoolean boolMath = new SimpleComplexAreaBoolean(areaToExclude, complexArea);
                    complexArea = boolMath.subtractAFromB();
                    addComplexAreaPreview(complexArea);
                }
                case 3 -> {
                    Area areaToExclude = createNewArea();
                    removeObstaclePreview(areaToExclude);

                    removeComplexAreaPreview(complexArea);
                    SimpleComplexAreaBoolean boolMath = new SimpleComplexAreaBoolean(areaToExclude, complexArea);
                    complexArea = boolMath.intersection();
                    addComplexAreaPreview(complexArea);
                }
                case 4 -> {
                    Area areaToExclude = createNewArea();
                    removeObstaclePreview(areaToExclude);
                    removeComplexAreaPreview(complexArea);
                    SimpleComplexAreaBoolean boolMath = new SimpleComplexAreaBoolean(areaToExclude, complexArea);
                    complexArea = boolMath.subtractBFromA();
                    addComplexAreaPreview(complexArea);
                }
                case 5 -> {
                    if (isTargetArea) {
                        level.setTargetArea(complexArea);
                    } else {
                        level.setInputArea(complexArea);
                    }
                    return;
                }
            }

        }
    }

    private void addComplexAreaPreview(ComplexArea complexArea) {
        Random random = new Random();
        Color color = new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
        ArrayList<ComplexAreaPart> included = complexArea.partAreas();
        addAreaLayer(included, color);

    }

    private void removeComplexAreaPreview(ComplexArea complexArea) {
        for (Area area : complexArea.getAllIncludedAreas()) {
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().remove(area.getPath()));
        }
        for (Area area : complexArea.getAllExcludedAreas()) {
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().remove(area.getPath()));
        }
    }

    private AnimationProperties getAnimationProperties() {

        int HEIGHT = (int) Math.abs((int) getNumber("Animation height:(px) ") * Properties.SIZE_FACTOR());
        int WIDTH = (int) Math.abs((int) getNumber("Animation width (px): ") * Properties.SIZE_FACTOR());

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

        Circle velocityShadow = new Circle(radius);
        velocityShadow.setOpacity(0.1);
        velocityShadow.setFill(Color.BLACK);
        updateBallVelocityShadow(ball, velocityShadow);
        addMovingObjectPreview(ball, velocityShadow);
        modifyBall(ball, velocityShadow);
        if (!animation.hasFreePlace(ball)) {
            removeMovingObjectPreview(ball, velocityShadow);
            if (agreeTo("Not enough space,try again?")) addMovingBall();
        } else if (agreeTo("Save?")) {
            saveMovingObject(ball, velocityShadow);
            if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
                if (agreeTo("should have function?")) {
                    setObjectFunction(ball);
                }
            }
        } else {
            removeMovingObjectPreview(ball, velocityShadow);
        }

    }

    private void setObjectFunction(Ball ball) {
        while (true) {
            switch ((int) getNumber(":0-no 1- have to enter target,2-cannot enter")) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    level.addMovingObjectHaveToEnter(ball);
                    return;
                }
                case 2 -> {
                    level.addMovingObjectCannotEnter(ball);
                    return;
                }
            }
        }
    }

    private void removeMovingObjectPreview(MovingObject obj, Shape velocityShadow) {
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().remove(obj.getShape()));
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().remove(velocityShadow));
    }

    private void addMovingObjectPreview(MovingObject obj, Shape velocityShadow) {
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(obj.getShape()));
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(velocityShadow));
    }

    private void saveMovingObject(MovingObject obj, Shape velocityShadow) {
        if (objectInStartState) {
            level.addMovingObject(obj);
        } else {
            removeMovingObjectPreview(obj, velocityShadow);
            level.addMovingObjectToAdd(obj);
        }

    }

    private void updateBallVelocityShadow(Ball ball, Circle velocityShadow) {
        velocityShadow.setRadius(ball.getRadius() / Properties.SIZE_FACTOR());
        velocityShadow.setCenterX(ball.center().add(ball.velocity()).getX() / Properties.SIZE_FACTOR());
        velocityShadow.setCenterY(ball.center().add(ball.velocity()).getY() / Properties.SIZE_FACTOR());
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
            updateBallVelocityShadow(ball, velocityShadow);
        }
    }

    private void addObstacles() {

        while (true) {
            Area obstacle;

            if (agreeTo(" Add new obstacle ? N/Y")) {
                obstacle = createNewArea();
                if (obstacle != null) {
                    if (agreeTo("Save?")) {//&&animation.hasFreePlace(obstacle)) {
                        saveObstacle(obstacle);
                        checkIntersections();
                        //checkingAreaBoolean();
                    } else {
                        removeObstaclePreview(obstacle);
                    }
                }

            } else return;


        }
    }

    /*private void checkingAreaBoolean() {
            if (level.getObstacles().size() >= 2) {


                            ArrayList<Area> subdivisions = AreasMath.areaSplit(level.getObstacles().getLast(),level.getObstacles().get(
                                    level.getObstacles().size()-2
                            ));

                            Random random = new Random();
                            for (Area area : subdivisions) {
                                if(! Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().contains(area.getPath())) {
                                    Platform.runLater(() -> {

                                        area.getPath().setFill(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
                                        Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(area.getPath());

                                    });
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }




                }

                removeObstaclePreview(level.getObstacles().getFirst());
                level.getObstacles().removeFirst();


            }
        }*/
    private void checkingAreaBoolean() {
        if (level.getObstacles().size() >= 2) {
            SimpleSimpleAreaBoolean simpleAreaBoolean = new SimpleSimpleAreaBoolean(level.getObstacles().getLast(), level.getObstacles().get(level.getObstacles().size() - 2));
            ComplexArea sum = simpleAreaBoolean.sum();
            ComplexArea intersection = simpleAreaBoolean.intersection();
            //ComplexArea difference1 = simpleAreaBoolean.subtractAfromB();
            //ComplexArea difference2 = simpleAreaBoolean.subtractBfromA();

            addComplexAreaPreview(intersection);
            addComplexAreaPreview(sum);

            // addComplexAreaPreview(difference1);
            //addComplexAreaPreview(difference2);

        }
    }

    private Area createNewArea() {
        Area area = null;
        switch ((int) getNumber(" Obstacle types: 0-rectangle 1-oval 2-poly line")) {
            case 0 -> area = getRectangle();
            case 1 -> area = getOval();
            case 2 -> area = getPolyLine();
        }
        return area;
    }

    private void checkIntersections() {
        ArrayList<Point2D> intersections = new ArrayList<>();
        for (Area area : level.getObstacles()) {
            for (Area area1 : level.getObstacles()) {
                for (Segment segment : area.getSegments()) {
                    for (Segment segment1 : area1.getSegments()) {
                        intersections.addAll(segment.getIntersectionsWith(segment1));
                    }

                }

                for (Point2D intersection : intersections) {
                    Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(new Circle(intersection.getX(), intersection.getY(), 1, Color.BLUE)));
                }


            }
        }
    }

    private void addObstaclePreview(Area obstacle) {
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().add(obstacle.getPath()));
    }

    private void removeObstaclePreview(Area obstacle) {
        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().remove(obstacle.getPath()));
    }

    private void saveObstacle(Area obstacle) {
        if (objectInStartState) {
            level.addObstacle(obstacle);
        } else {
            Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().preview.getChildren().remove(obstacle.getPath()));
            level.addObstacleToAdd(obstacle);
        }

    }

    private Area getRectangle() {
        RectangleArea rectangle = new RectangleArea(getPoint("Corner 1:"), getPoint("Corner 3:"), 0);
        return modifyRectangle(rectangle);
    }

    private RectangleArea modifyRectangle(RectangleArea rectangle) {
        while (true) {
            RectangleArea temp = rectangle;
            addObstaclePreview(temp);
            switch ((int) getNumber("Change property:0-no 1- corner 1,2-rotation,3-corner 3")) {
                case 0 -> {
                    return rectangle;
                }
                case 1 ->
                        rectangle = new RectangleArea(getPoint("Corner 1:"), rectangle.getCorners().get(2), rectangle.getRotation());
                case 2 ->
                        rectangle = new RectangleArea(rectangle.getCorners().getFirst(), rectangle.getCorners().get(2), getNumber("Rotation") / 360 * 2 * Math.PI);
                case 3 ->
                        rectangle = new RectangleArea(rectangle.getCorners().get(0), getPoint("Corner 3:"), rectangle.getRotation());
            }
            removeObstaclePreview(temp);
        }
    }

    private Area getOval() {
        Point2D center = getPoint("Center:");
        OvalArea oval = new OvalArea(center, getDimension("Radius X:", center), getDimension("Radius Y:", center), 0);
        return modifyOval(oval);
    }

    private OvalArea modifyOval(OvalArea oval) {
        while (true) {
            OvalArea temp = oval;
            addObstaclePreview(temp);
            switch ((int) getNumber("Change property:0-no 1- center ,2-rotation,3-radius X 4-radius Y")) {
                case 0 -> {
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
            removeObstaclePreview(temp);
        }

    }

    private Area getPolyLine() {
        Point2D start = getPoint("Start:");
        PolylineArea plObst = new PolylineArea(start);
        addObstaclePreview(plObst);
        return modifyPolyLine(plObst);


    }

    private Area modifyPolyLine(PolylineArea plObst) {
        while (true) {
            switch ((int) getNumber("Next segment:0-no 1- line ,2- quad curve,3-cubic curve 4-removeLast")) {
                case 0 -> {


                    plObst.drawStraightSegmentTo(plObst.getSegments().getFirst().getPoints().getFirst());
                    if (!isSelfIntersecting(plObst)) {
                        plObst.closeAndSave();
                        return plObst;
                    } else plObst.removeLastSegment();
                }
                case 1 -> plObst.drawStraightSegmentTo(getPoint("Next Point: "));
                case 2 -> plObst.drawQuadCurveTo(getPoint("Control Point: "), getPoint("Next Point: "));
                case 3 ->
                        plObst.drawCubicCurveTo(getPoint("Control Point 1: "), getPoint("Control Point 2: "), getPoint("Next Point: "));
                case 4 -> plObst.removeLastSegment();
            }
            if (isSelfIntersecting(plObst)) {
                plObst.removeLastSegment();
            }

        }


    }

    private boolean isSelfIntersecting(PolylineArea plObst) {
        if (!plObst.getSegments().isEmpty()) {
            Segment segment1 = plObst.getSegments().getLast();
            for (Segment segment2 : plObst.getSegments()) {
                if (!segment1.equals(segment2)) {
                    ArrayList<Point2D> intersections = segment1.getIntersectionsWith(segment2);
                    if (!(intersections.isEmpty() || (intersections.size() == 1 && (intersections.getFirst().distance(segment2.getPoints().getFirst()) <= Properties.ACCURACY() || intersections.getFirst().distance(segment2.getPoints().getLast()) <= Properties.ACCURACY())))) {
                        return true;
                    }
                }
            }
        }
        return false;

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

        double value = getNumber("Gravity strength 0-1000");
        if (value >= 0 && value < 1000) {
            return value;
        } else {
            return getGravity() * Properties.SIZE_FACTOR();
        }

    }

    private double getDimension(String message, Point2D reference) {
        Model.getInstance().controllers().getLevelCreatorController().setSelectedPoint(reference);
        while (true) {
            String userInput = getStringInput(message);
            if (Model.getInstance().controllers().getLevelCreatorController().getLastEvent() instanceof KeyEvent) {
                Double value = getTextValue(userInput);
                if (value != null) return Math.abs(value);
            } else {
                return Model.getInstance().controllers().getLevelCreatorController().getPickedDistance();
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
                        Platform.runLater(() -> Model.getInstance().controllers().getLevelCreatorController().messageLabel.textProperty().setValue("Wrong value"));

            }

        }


    }

    public Point2D getPoint(String message) {
        double x = getDimension(message + " X:", null);
        if (!(Model.getInstance().controllers().getLevelCreatorController().getLastEvent() instanceof KeyEvent)) {
            return Model.getInstance().controllers().getLevelCreatorController().getSelectedPoint();
        }
        double y = getDimension(message + " Y:", null);
        if (!(Model.getInstance().controllers().getLevelCreatorController().getLastEvent() instanceof KeyEvent)) {
            return Model.getInstance().controllers().getLevelCreatorController().getSelectedPoint();
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

    private void setPreviewBoundaries(int HEIGHT, int WIDTH) {
        Model.getInstance().controllers().getLevelCreatorController().preview.setMinSize(WIDTH / Properties.SIZE_FACTOR(), HEIGHT / Properties.SIZE_FACTOR());
        Model.getInstance().controllers().getLevelCreatorController().preview.setMaxSize(WIDTH / Properties.SIZE_FACTOR(), HEIGHT / Properties.SIZE_FACTOR());
        Model.getInstance().controllers().getLevelCreatorController().preview.backgroundProperty().set(Background.fill(Color.BEIGE));
    }
}
