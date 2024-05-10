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
    private String NAME="New_Level";
    public AnimationProperties PROPERTIES() {
        return PROPERTIES;
    }
    private final AnimationProperties PROPERTIES;
    private final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private   InputArea inputArea;
    private TargetArea targetArea;
    public Level(AnimationProperties properties) {
        PROPERTIES = properties;
    }

    public ArrayList<MovingObject> movingObjects() {
        return movingObjects;
    }

    public ArrayList<Obstacle> obstacles() {
        return obstacles;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
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
