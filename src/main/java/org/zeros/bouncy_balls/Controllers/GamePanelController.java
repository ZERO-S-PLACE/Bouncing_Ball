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
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;
import org.zeros.bouncy_balls.Objects.Obstacles.PolylineObstacle;
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
         /*Obstacle obstacle=new RectangleObstacle(new Point2D(50,50),new Point2D(150,150), (double) 1 /20*Math.PI);
        animation.addObstacle(obstacle);
        gameBackground.getChildren().add(obstacle.getPath());*/

       Obstacle obstacle1=new RectangleObstacle(new Point2D(1200,700),new Point2D(1000,600),0.000
        );
        animation.addObstacle(obstacle1);
        gameBackground.getChildren().add(obstacle1.getPath());

        /*Obstacle obstacle3=new OvalObstacle(new Point2D(450,450),200,200,0  );
        animation.addObstacle(obstacle3);
        gameBackground.getChildren().add(obstacle3.getPath());*/
        /*Obstacle obstacle4=new OvalObstacle(new Point2D(200,200),40,200,Math.PI/3  );
        animation.addObstacle(obstacle4);
        gameBackground.getChildren().add(obstacle4.getPath());*/

        /*Obstacle obstacle41=new OvalObstacle(new Point2D(1050,350),200,200,Math.PI/4  );
        animation.addObstacle(obstacle41);
        gameBackground.getChildren().add(obstacle41.getPath());

        for (Point2D point2: obstacle41.getAllPoints()){
            gameBackground.getChildren().add(new Rectangle(point2.getX(),point2.getY(),2,2));

        }*/



        PolylineObstacle obstacle5=new PolylineObstacle();
        obstacle5.startDrawingFromPoint(new Point2D(200,100));
        obstacle5.drawStraightSegmentTo(new Point2D(600,100));
        obstacle5.drawStraightSegmentTo(new Point2D(600,600));
        obstacle5.drawStraightSegmentTo(new Point2D(100,600));
        obstacle5.drawStraightSegmentTo(new Point2D(100,500));
        obstacle5.drawStraightSegmentTo(new Point2D(500,500));
        obstacle5.drawStraightSegmentTo(new Point2D(500,400));
        obstacle5.drawStraightSegmentTo(new Point2D(300,300));
        obstacle5.drawStraightSegmentTo(new Point2D(200,150));

        //obstacle5.drawQuadCurveTo(new Point2D(100,500),new Point2D(500,500));
        //obstacle5.drawCubicCurveTo(new Point2D(500,400),new Point2D(300,300),new Point2D(200,150));

        obstacle5.closeAndSave();
        animation.addObstacle(obstacle5);
        gameBackground.getChildren().add(obstacle5.getPath());






        for (int i0=0; i0 < 40; i0++ ){
            for (int j0= 0; j0 < 40; j0++ ){

                redBall[i0][j0] = new Ball(new Point2D(300,150),3,new Point2D(650+12*i0,6+12*j0),5);
                redBall[i0][j0].getShape().fillProperty().set(Color.RED);
                animation.addMovingObject(redBall[i0][j0]);
                gameBackground.getChildren().add(redBall[i0][j0].getShape());
            }

        }


            for (int i = 0; i < 30; ) {

                int radius = random.nextInt(40)+5;
                ball[i] = new Ball(new Point2D(5 * random.nextInt(70), 5 * random.nextInt(100)), radius+1,
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
