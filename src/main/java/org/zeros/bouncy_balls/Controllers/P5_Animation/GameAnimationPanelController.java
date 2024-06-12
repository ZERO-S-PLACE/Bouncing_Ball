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
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
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

public class GameAnimationPanelController implements Initializable {

    AnimationPane animationPane;

    public AnimationPane getAnimationPane() {
        return animationPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void loadAnimation(String path){
        animationPane=new AnimationPane(path);
    }


}
