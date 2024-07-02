package org.zeros.bouncy_balls.Level;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.Animation.Borders.BordersType;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Calculations.AreasMath.SimpleComplexAreaBoolean;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.SerializableObjects.LevelSerializable;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.OvalArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.RectangleArea;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class LevelCreator {
    private Level level;
    private Animation animation;
    private boolean objectInStartState = true;
    private Thread textInputThread;
    private Thread mouseInputThread;



    public LevelCreator() {

    }

    private static Double convertToDouble(String text) {
        double value;
        try {
            value = Double.parseDouble(text);
        } catch (Exception e) {
            Platform.runLater(() -> CreatorModel.getInstance().controllers().getBottomPanelController().inputTextField.textProperty().setValue("Wrong value"));
            return null;
        }
        return value;
    }
private String textInput=null;
    private  String getStringInput(String message) {
        Platform.runLater(() -> CreatorModel.getInstance().controllers().getBottomPanelController().tipLabel.textProperty().setValue(message));
        Platform.runLater(() ->CreatorModel.getInstance().controllers().getBottomPanelController().setTextEntered(""));
        CreatorModel.getInstance().controllers().getBottomPanelController().textEnteredProperty().addListener(getTextInputListener());

        try {
            inputLock.lock();
            inputLock.wait();
        }catch (Exception e){
            throw new RuntimeException("Error getting String input");
        }

        CreatorModel.getInstance().controllers().getBottomPanelController().textEnteredProperty().removeListener(getTextInputListener());
        System.out.println("after lock"+textInput);
        return textInput;
    }


    private  ChangeListener<String> getTextInputListener() {
        return (observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)&& !newValue.isEmpty()){
                textInput=newValue;
                System.out.println("in listener"+textInput);
                //inputLock.unlock();
            }
        };
    }


    private double getDimension(String message, Point2D reference) {
        CreatorModel.getInstance().controllers().getBottomPanelController().tipLabel.setText(message);
        CreatorModel.getInstance().getViewFactory().getTrackingPane().getReferencePointProperty().set(reference);





       /* CreatorModel.getInstance().getViewFactory().getTrackingPane().setReferencePoint(reference);
        while (true) {
            String userInput = getStringInput(message);
            if (Model.getInstance().controllers().getLevelCreatorController().getLastEvent() instanceof KeyEvent) {
                Double value = getTextValue(userInput);
                if (value != null) return Math.abs(value);
            } else {
                return Model.getInstance().controllers().getLevelCreatorController().getPickedDistance();
            }
        }*/
        return 0;
    }

    public double getNumber(String message) {
        while (true) {
            String userInput = getStringInput(message);
            Double value = convertToDouble(userInput);
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
                        Platform.runLater(() -> CreatorModel.getInstance().controllers().getBottomPanelController().tipLabel.textProperty().setValue("Wrong value"));

            }

        }


    }

    public Point2D getPoint(String message) {
      /*  double x = getDimension(message + " X:", null);
        if (!(Model.getInstance().controllers().getLevelCreatorController().getLastEvent() instanceof KeyEvent)) {
            return Model.getInstance().controllers().getLevelCreatorController().getSelectedPoint();
        }
        double y = getDimension(message + " Y:", null);
        if (!(Model.getInstance().controllers().getLevelCreatorController().getLastEvent() instanceof KeyEvent)) {
            return Model.getInstance().controllers().getLevelCreatorController().getSelectedPoint();
        }
        return new Point2D(x, y);*/
        return new Point2D(0,0);
    }
    public void create() {

        level = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getLevel();
        animation = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getAnimation();
        getStringInput("Anyway");
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

                }
                case 1 -> {
                    Area areaToAdd = createNewArea();

                    SimpleComplexAreaBoolean boolMath = new SimpleComplexAreaBoolean(areaToAdd, complexArea);
                    complexArea = boolMath.sum();
                }
                case 2 -> {
                    Area areaToExclude = createNewArea();



                    SimpleComplexAreaBoolean boolMath = new SimpleComplexAreaBoolean(areaToExclude, complexArea);
                    complexArea = boolMath.subtractAFromB();

                }
                case 3 -> {
                    Area areaToExclude = createNewArea();



                    SimpleComplexAreaBoolean boolMath = new SimpleComplexAreaBoolean(areaToExclude, complexArea);
                    complexArea = boolMath.intersection();

                }
                case 4 -> {
                    Area areaToExclude = createNewArea();

                    SimpleComplexAreaBoolean boolMath = new SimpleComplexAreaBoolean(areaToExclude, complexArea);
                    complexArea = boolMath.subtractBFromA();
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

        modifyBall(ball, velocityShadow);
        if (!animation.hasFreePlace(ball)) {
            if (agreeTo("Not enough space,try again?")) addMovingBall();
        } else if (agreeTo("Save?")) {
            saveMovingObject(ball, velocityShadow);
            if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
                if (agreeTo("should have function?")) {
                    setObjectFunction(ball);
                }
            }
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



    private void saveMovingObject(MovingObject obj, Shape velocityShadow) {
        if (objectInStartState) {
            level.addMovingObject(obj);
        } else {
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

                    }
                }

            } else return;


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

    private void saveObstacle(Area obstacle) {
        if (objectInStartState) {
            level.addObstacle(obstacle);
        } else {
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
        }

    }

    private Area getPolyLine() {
        Point2D start = getPoint("Start:");
        PolylineArea plObst = new PolylineArea(start);
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
