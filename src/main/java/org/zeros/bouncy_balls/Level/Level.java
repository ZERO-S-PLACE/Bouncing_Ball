package org.zeros.bouncy_balls.Level;

import org.zeros.bouncy_balls.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Animation.AnimationType;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.Area.ComplexArea;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.SerializableObjects.AreaSerializable;
import org.zeros.bouncy_balls.Objects.SerializableObjects.LevelSerializable;
import org.zeros.bouncy_balls.Objects.SerializableObjects.MovingObjectSerializable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {
    private final AnimationProperties PROPERTIES;
    private final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private final ArrayList<Area> obstacles = new ArrayList<>();
    private final ArrayList<MovingObject> movingObjectsToAdd = new ArrayList<>();
    private final ArrayList<Area> obstaclesToAdd = new ArrayList<>();
    private String NAME = "New_Level";
    private ComplexArea inputArea;
    private ComplexArea targetArea;

    public Level(AnimationProperties properties) {
        PROPERTIES = properties;
    }

    public static Level load(String path) {
        LevelSerializable save = null;
        try (FileInputStream fileIn = new FileInputStream(path); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Object object = in.readObject();
            if (object instanceof LevelSerializable) {
                save = (LevelSerializable) object;
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        if (save == null) {
            return null;
        } else {
            return save.deserialize();
        }
    }

    public void rescale(double factor){
        if(factor>0&&factor!=1){
            PROPERTIES.setGRAVITY(factor*PROPERTIES.getGRAVITY());
            PROPERTIES.setWIDTH((int) (PROPERTIES.getWIDTH()*factor));
            PROPERTIES.setHEIGHT((int) (PROPERTIES.getHEIGHT()*factor));
            for (MovingObject object : movingObjects) {
                object.rescale(factor);
            }
            for (Area area : obstacles) {
                area.rescale(factor);
            }
            for (MovingObject object : movingObjectsToAdd) {
               object.rescale(factor);
            }
            for (Area area : obstaclesToAdd) {
                area.rescale(factor);
            }
            if (inputArea != null) {
                inputArea.rescale(factor);
            }
            if (targetArea != null) {
                targetArea.rescale(factor);
            }
        }


    }

    public ArrayList<MovingObject> movingObjects() {
        return movingObjects;
    }

    public ArrayList<Area> obstacles() {
        return obstacles;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public AnimationProperties PROPERTIES() {
        return PROPERTIES;
    }

    public ComplexArea getInputArea() {
        return inputArea;
    }

    public void setInputArea(ComplexArea inputArea) {
        if (PROPERTIES.getTYPE().equals(AnimationType.GAME)) {
            this.inputArea = inputArea;
        }
    }

    public ComplexArea getTargetArea() {
        return targetArea;
    }

    public void setTargetArea(ComplexArea targetArea) {
        if (PROPERTIES.getTYPE().equals(AnimationType.GAME)) {
            this.targetArea = targetArea;
        }
    }

    public ArrayList<MovingObject> movingObjectsToAdd() {
        if (PROPERTIES.getTYPE().equals(AnimationType.SIMULATION)) {
            return null;
        }
        return movingObjectsToAdd;
    }

    public ArrayList<Area> obstaclesToAdd() {
        if (PROPERTIES.getTYPE().equals(AnimationType.SIMULATION)) {
            return null;
        }
        return obstaclesToAdd;
    }
}
