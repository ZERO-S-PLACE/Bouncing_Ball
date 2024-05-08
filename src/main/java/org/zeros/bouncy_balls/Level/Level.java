package org.zeros.bouncy_balls.Level;

import org.zeros.bouncy_balls.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Animation.AnimationType;
import org.zeros.bouncy_balls.Objects.Areas.InputArea;
import org.zeros.bouncy_balls.Objects.Areas.TargetArea;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {
    public final AnimationProperties PROPERTIES;
    public final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    public final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private   InputArea inputArea;
    private TargetArea targetArea;
    public Level(AnimationProperties properties) {
        PROPERTIES = properties;
    }

    public InputArea getInputArea() {
        return inputArea;
    }

    public void setInputArea(InputArea inputArea) {
        if(PROPERTIES.getTYPE().equals(AnimationType.GAME)) {
            this.inputArea = inputArea;
        }
    }

    public TargetArea getTargetArea() {
        return targetArea;
    }

    public void setTargetArea(TargetArea targetArea) {
        if(PROPERTIES.getTYPE().equals(AnimationType.GAME)) {
            this.targetArea = targetArea;
        }
    }





}
