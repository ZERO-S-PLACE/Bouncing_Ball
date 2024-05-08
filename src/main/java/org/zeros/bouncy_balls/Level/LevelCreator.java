package org.zeros.bouncy_balls.Level;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Animation.AnimationType;
import org.zeros.bouncy_balls.Animation.BordersType;
import org.zeros.bouncy_balls.Controllers.LevelCreatorController;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.Obstacles.RectangleObstacle;

public class LevelCreator {

    private Level level;
    private Animation animation;
    public void create() {
        Model.getInstance().getLevelCreatorController().preview.getChildren().removeAll();
        AnimationProperties properties=getAnimationProperties();
        level=new Level(properties);
        animation=new Animation(level);
        int choice=1;
        while (choice!=0) {
            choice=(int) getNumber("What to do? 0-exit 1-add Obstacle 2-add Moving Object 3-add target area 4 -add input area");
            switch (choice) {
                case 1->addObstacles();
                case 2->addMovingObjects();
                //case 3->addTargetArea();
                //case 4->addInputArea();
            }
        }
        if(agreeTo("Save level ? Y/N")){saveLevel(getName("Enter name: "));}
        if(agreeTo("Create next ? Y/N")){this.create();}


    }
    private AnimationProperties getAnimationProperties() {

        int HEIGHT= (int) getDimension("Animation height:(px) ");
        int WIDTH= (int) getDimension("Animation width (px): ");

        AnimationProperties properties= new AnimationProperties(HEIGHT,WIDTH);

        if(agreeTo("Custom properties Y/N")) {
            properties.setTime(getDimension("Time of animation: "));
            properties.setGRAVITY(getGravity());
            properties.setBOUNDARIES(getBordersType());
            if (agreeTo("Is this simulation(Y) or game(N)? ")) {
                properties.setTYPE(AnimationType.SIMULATION);
            }
            if (agreeTo("Custom speed Y/N")) {
                properties.setFRAME_RATE(getDimension("Frame rate: "));
                properties.setMAX_EVALUATIONS((int) getDimension("Max evaluations per frame: "));
            }
        }
        setPreviewBoundaries(HEIGHT,WIDTH);
        return new AnimationProperties(HEIGHT,WIDTH);
    }


    private void addMovingObjects() {
        while (true){
            if (agreeTo(" Add new moving object ? N/Y")){
                addMovingBall();
            }else return;
        }
    }

    private void addMovingBall() {
        int radius=(int) getNumber("Radius");
        Ball ball=new Ball(radius,animation);
        ball.updateCenter(getPoint("Center:"));
        level.movingObjects.add(ball);
        Circle velocityShadow=new Circle(radius);
        velocityShadow.setOpacity(0.1);
        velocityShadow.setFill(Color.BLACK);
        updateVelocityShadow(velocityShadow);
        Platform.runLater(()->Model.getInstance().getLevelCreatorController().preview.getChildren().add(
                velocityShadow));

        Platform.runLater(()->Model.getInstance().getLevelCreatorController().preview.getChildren().add(
               ball.getShape()));

        modifyBall(ball,velocityShadow);
        if (!animation.hasFreePlace(ball)) {
            removeLastMovingObject();
            if(agreeTo("Not enough space,try again?")) addMovingBall();
            else return;
        }
            if (!agreeTo("Save?")) {
                removeLastMovingObject();
            }

        }

    private void removeLastMovingObject() {
        level.movingObjects.removeLast();
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().removeLast());
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().removeLast());
    }

    private void updateVelocityShadow(Circle velocityShadow) {
        Ball ball=((Ball) level.movingObjects.getLast());
        velocityShadow.setRadius(ball.getRadius());
        velocityShadow.setCenterX(ball.center().add(ball.velocity()).getX());
        velocityShadow.setCenterY(ball.center().add(ball.velocity()).getY());
    }

    private void modifyBall(Ball ball,Circle velocityShadow) {
        while (true) {
            switch ((int)getNumber("Change property:0-no 1- velocity,2-friction,3-mass,4-radius, 5-center")) {
                case 0 -> {return;}
                case 1-> ball.setVelocity(getPoint("NextCenter: ").subtract(ball.center()));
                case 2-> ball.setFriction(getNumber("Friction: "));
                case 3-> ball.setMass(getNumber("Mass: "));
                case 4-> ball.setRadius(getNumber("Radius: "));
                case 5-> ball.updateCenter(getPoint("Center:"));
            }
            updateVelocityShadow(velocityShadow);
        }
    }


    private void addObstacles() {
        while (true){
            if (agreeTo(" Add new obstacle ? N/Y")){
                switch ((int) getNumber(" Obstacle types: 0-rectangle 1-oval 2-poly line")){
                    case 0->addRectangle();
                    case 1->addOval();
                    case 2->addPolyline();
                }
            }else return;
        }
    }

    private void addRectangle() {
        RectangleObstacle rectangle =new RectangleObstacle(getPoint("Corner 1:"),
                getPoint("Corner 3:"),getDimension("Rotation")/360*2*Math.PI);

        level.obstacles.add(rectangle);
        Platform.runLater(()->Model.getInstance().getLevelCreatorController().preview.getChildren().add(
                rectangle.getPath()));

        if (!agreeTo("Save?")) {
            removeLastMovingObject();
        }
    }


    private void removeLastObstacle() {
        level.obstacles.removeLast();
        Platform.runLater(() -> Model.getInstance().getLevelCreatorController().preview.getChildren().removeLast());

    }

    private void addOval() {
    }

    private void addPolyline() {
    }



    private void setPreviewBoundaries(int HEIGHT, int WIDTH) {
        Model.getInstance().getLevelCreatorController().preview.setMinSize(WIDTH,HEIGHT);
        Model.getInstance().getLevelCreatorController().preview.setMaxSize(WIDTH,HEIGHT);
        Model.getInstance().getLevelCreatorController().preview.backgroundProperty().set(Background.fill(Color.BEIGE));
    }

    private BordersType getBordersType() {
        while (true){
            int value=(int) getNumber("Border types: 0-bouncing, 1-connected, 2-infinite");
            switch (value){
                case 0 -> {return BordersType.BOUNCING;}
                case 1 -> {return BordersType.CONNECTED;}
                case 2 -> {return BordersType.INFINITE;}
                default -> System.out.println("Wrong value");
            }
            }
        }
    private double getGravity() {
        while (true){
            double value= getNumber("Gravity strength");
            if(value>0&&value<100){
                return value;
            }else {
                System.out.println("Wrong value");
            }
        }
    }
    private double getDimension(String message) {
        return Math.abs(getNumber(message));
    }
    public double getNumber(String message){
        while (true) {
            String userInput = getStringInput(message);
            double value;
            try {
             value=Double.parseDouble(userInput);
            }catch (Exception e){
                Platform.runLater(()->Model.getInstance().getLevelCreatorController().messageLabel.textProperty().setValue("Wrong value"));
                continue;
            }
            return value;
        }

    }




    public boolean agreeTo(String message){
        while (true) {
            switch (getStringInput(message)){
                case "Y","y","1" -> {return true;}
                case "N","n","0" -> {return false;}
                default -> Platform.runLater(()->Model.getInstance().getLevelCreatorController()
                        .messageLabel.textProperty().setValue("Wrong value"));

            }

        }


    }
    public String getName(String message){
        return getStringInput(message);
    }
    public Point2D getPoint(String message){
        return new Point2D(getNumber(message +" X:"),getNumber(message +" Y:"));
    }
    private static String getStringInput(String message) {
        Platform.runLater(()->Model.getInstance().getLevelCreatorController().messageLabel.textProperty().setValue(message));
        Platform.runLater(()->Model.getInstance().getLevelCreatorController().inputLabel.textProperty().setValue(""));
        waitForInput();
        return Model.getInstance().getLevelCreatorController().inputLabel.textProperty().get();
    }
    private static void waitForInput() {
        Object lock= LevelCreatorController.getLock();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void saveLevel(String name) {
    }
}
