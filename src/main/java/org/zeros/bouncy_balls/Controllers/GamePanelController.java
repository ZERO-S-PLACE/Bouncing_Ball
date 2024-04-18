package org.zeros.bouncy_balls.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Calculations.Transition;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;
import org.zeros.bouncy_balls.Model.Properties;
import java.net.URL;
import java.util.Objects;
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

        Random random = new Random();
        Ball[] ball = new Ball[300];
        Ball[][] redBall = new Ball[50][50];

        for (int i0=0; i0 < 30; i0++ ){
            for (int j0= 0; j0 < 30; j0++ ){

                redBall[i0][j0] = new Ball(1,0,0,1,300+4*i0,300+4*j0);
                redBall[i0][j0].getShape().fillProperty().set(Color.RED);
                movingObjectsList.add(redBall[i0][j0]);
                gameBackground.getChildren().add(redBall[i0][j0].getShape());
            }

        }


            for (int i = 0; i < 300; ) {
                boolean freePlace = true;
                int radius = random.nextInt(2);
                ball[i] = new Ball(radius, 5 * random.nextInt(10), 5 * random.nextInt(10), random.nextInt(1000),
                        random.nextInt(700), random.nextInt(700));


                if (!Transition.isInside(ball[i])) {
                    freePlace = false;

                } else {
                    for (MovingObject object : movingObjectsList) {
                        {
                            if (object.getType().equals(MovingObjectType.BALL)) {
                                if (object.centerPointProperty().get().distance(ball[i].centerPointProperty().get()) <
                                        ((Ball) object).getRadius() + ball[i].getRadius()) {
                                    freePlace = false;
                                }
                            }
                        }
                    }
                }
                if (freePlace) {

                    movingObjectsList.add(ball[i]);
                    gameBackground.getChildren().add(ball[i].getShape());
                    i++;
                }
            }


            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Animation animation = new Animation(movingObjectsList);
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
