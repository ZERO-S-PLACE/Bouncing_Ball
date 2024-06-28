package org.zeros.bouncy_balls.Objects.SerializableObjects;

import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelSerializable implements Serializable {
    private final String NAME;
    private final AnimationProperties PROPERTIES;
    private final ArrayList<MovingObjectSerializable> movingObjects = new ArrayList<>();
    private final ArrayList<AreaSerializable> obstacles = new ArrayList<>();
    private final ArrayList<MovingObjectSerializable> movingObjectsToAdd = new ArrayList<>();
    private final ArrayList<MovingObjectSerializable> movingObjectsHaveToEnter = new ArrayList<>();
    private final ArrayList<MovingObjectSerializable> movingObjectsCannotEnter = new ArrayList<>();
    private final ArrayList<AreaSerializable> obstaclesToAdd = new ArrayList<>();
    private ComplexAreaSerializable inputArea;
    private ComplexAreaSerializable targetArea;
    private final int oneStarBound;
    private final int twoStarBound;
    private final int threeStarBound;

    public LevelSerializable(Level level) {
        this.NAME = level.getNAME();
        this.PROPERTIES = level.PROPERTIES();
        this.oneStarBound=level.getOneStarBound();
        this.twoStarBound=level.getTwoStarBound();
        this.threeStarBound=level.getThreeStarBound();
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
        for (MovingObject object : level.getMovingObjectsHaveToEnter()) {
            movingObjectsHaveToEnter.add(new MovingObjectSerializable(object));
        }
        for (MovingObject object : level.getMovingObjectsCannotEnter()) {
            movingObjectsCannotEnter.add(new MovingObjectSerializable(object));
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
        level.setOneStarBound(oneStarBound);
        level.setTwoStarBound(twoStarBound);
        level.setThreeStarBound(threeStarBound);
        for (MovingObjectSerializable object : movingObjects) {
            level.getMovingObjects().add(object.deserialize(animation));
        }
        for (AreaSerializable area : obstacles) {
            level.getObstacles().add(area.deserialize());
        }
        for (MovingObjectSerializable object : movingObjectsToAdd) {
            level.getMovingObjectsToAdd().add(object.deserialize(animation));
        }
        for (MovingObjectSerializable object : movingObjectsHaveToEnter) {
            level.getMovingObjectsHaveToEnter().add(getEqualObject(level, object.deserialize(animation)));
        }
        for (MovingObjectSerializable object : movingObjectsCannotEnter) {
            level.getMovingObjectsCannotEnter().add(getEqualObject(level, object.deserialize(animation)));
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

    private static MovingObject getEqualObject(Level level, MovingObject objToFind) {
        for(MovingObject object1: level.getMovingObjects()){
            if(object1.equals(objToFind)){
               return object1;
            }
        }
        for(MovingObject object1: level.getMovingObjectsToAdd()){
            if(object1.equals(objToFind)){
                return object1;
            }
        }
        throw new IllegalArgumentException("Given level does not contain that object");
    }
}
