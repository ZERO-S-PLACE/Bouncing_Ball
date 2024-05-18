package org.zeros.bouncy_balls.Objects.SerializableObjects;

import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelSerializable implements Serializable {
    private final String NAME;
    private final AnimationProperties PROPERTIES;
    private final ArrayList<MovingObjectSerializable> movingObjects = new ArrayList<>();
    private final ArrayList<AreaSerializable> obstacles = new ArrayList<>();
    private final ArrayList<MovingObjectSerializable> movingObjectsToAdd = new ArrayList<>();
    private final ArrayList<AreaSerializable> obstaclesToAdd = new ArrayList<>();
    private ComplexAreaSerializable inputArea;
    private ComplexAreaSerializable targetArea;

    public LevelSerializable(Level level) {
        this.NAME = level.getNAME();
        this.PROPERTIES = level.PROPERTIES();
        for (MovingObject object : level.getMovingObjects()) {
            movingObjects.add(new MovingObjectSerializable(object));
        }
        for (Area area : level.getObstacles()) {
            obstacles.add(new AreaSerializable(area));
        }
        for (MovingObject object : level.getMovingObjectsToAdd()) {
            movingObjectsToAdd.add(new MovingObjectSerializable(object));
        }
        for (Area area : level.getObstaclesToAdd()) {
            obstaclesToAdd.add(new AreaSerializable(area));
        }
        if (level.getInputArea() != null) {
            inputArea = new ComplexAreaSerializable(level.getInputArea());
        }
        if (level.getTargetArea() != null) {
            targetArea = new ComplexAreaSerializable(level.getTargetArea());
        }
    }

    public Level deserialize() {
        Level level = new Level(PROPERTIES);
        Animation animation = new Animation(level);
        level.setNAME(NAME);
        for (MovingObjectSerializable object : movingObjects) {
            level.getMovingObjects().add(object.deserialize(animation));
        }
        for (AreaSerializable area : obstacles) {
            level.getObstacles().add(area.deserialize());
        }
        for (MovingObjectSerializable object : movingObjectsToAdd) {
            level.getMovingObjectsToAdd().add(object.deserialize(animation));
        }
        for (AreaSerializable area : obstaclesToAdd) {
            level.getObstaclesToAdd().add(area.deserialize());
        }
        if (inputArea != null) {
            level.setInputArea(inputArea.deserialize());
        }
        if (targetArea != null) {
            level.setTargetArea(targetArea.deserialize());
        }
        return level;
    }
}
