package org.zeros.bouncy_balls.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.Obstacles.RectangleObstacle;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GamePanelController implements Initializable {

    public AnchorPane gameBackground;

    private ObservableList <MovingObject> movingObjectsList= FXCollections.observableArrayList();
    private ObservableList <Obstacle> obstaclesList=FXCollections.observableArrayList();

    public ObservableList<MovingObject> getMovingObjectsList() {
        return movingObjectsList;
    }

    public ObservableList<Obstacle> getObstaclesList() {
        return obstaclesList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBackground();
        Animation animation = new Animation();
        Random random = new Random();
        Ball[] ball = new Ball[1000];
        Ball[][] redBall = new Ball[50][50];


        Obstacle obstacle=new RectangleObstacle(new Point2D(50,50),new Point2D(150,150), (double) 1 /20*Math.PI);
        animation.addObstacle(obstacle);
        gameBackground.getChildren().add(obstacle.getPath());

        Obstacle obstacle1=new RectangleObstacle(new Point2D(200,200),new Point2D(300,300),0.000
        );
        animation.addObstacle(obstacle1);
        gameBackground.getChildren().add(obstacle1.getPath());



        for (int i0=0; i0 < 1; i0++ ){
            for (int j0= 0; j0 < 1; j0++ ){

                redBall[i0][j0] = new Ball(new Point2D(0,0),0.01,new Point2D(400+5*i0,400+5*j0),2);
                redBall[i0][j0].getShape().fillProperty().set(Color.RED);
                animation.addMovingObject(redBall[i0][j0]);
                gameBackground.getChildren().add(redBall[i0][j0].getShape());
            }

        }


            for (int i = 0; i < 15; ) {

                int radius = random.nextInt(10)*10+5;
                ball[i] = new Ball(new Point2D(5 * random.nextInt(100), 5 * random.nextInt(100)), radius+1,
                        new Point2D(random.nextInt(700), random.nextInt(700)),radius+1);
                if (animation.hasFreePlace(ball[i])) {

                   animation.addMovingObject(ball[i]);
                    gameBackground.getChildren().add(ball[i].getShape());
                    i++;
                }
            }


            new Thread(animation::animate).start();



    }



    private void setBackground() {
        gameBackground.setMinHeight(Properties.getGAME_HEIGHT());
        gameBackground.setMaxHeight(Properties.getGAME_HEIGHT());
        gameBackground.setMinWidth(Properties.getGAME_WIDTH());
        gameBackground.setMaxWidth(Properties.getGAME_WIDTH());
        gameBackground.backgroundProperty().setValue(new Background(
                new BackgroundFill(Color.rgb(249,211,165),new CornerRadii(5),new Insets(0))));
    }
}
