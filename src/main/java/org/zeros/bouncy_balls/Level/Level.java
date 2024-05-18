package org.zeros.bouncy_balls.Level;

import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.Area.ComplexArea;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.SerializableObjects.LevelSerializable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Level implements Serializable {
    private final AnimationProperties PROPERTIES;
    private final List<MovingObject> movingObjects = new CopyOnWriteArrayList<>();
    private final List<Area> obstacles = new CopyOnWriteArrayList<>();
    private final List<MovingObject> movingObjectsToAdd = new CopyOnWriteArrayList<>();
    private final List<Area> obstaclesToAdd = new CopyOnWriteArrayList<>();
    private String NAME = "New_Level";
    private ComplexArea inputArea;
    private ComplexArea targetArea;
    private final Lock movingObjectsLock = new ReentrantLock();
    private final Lock movingObjectsToAddLock = new ReentrantLock();
    private final Lock obstaclesLock = new ReentrantLock();
    private final Lock obstaclesToAddLock = new ReentrantLock();
    private final Lock inputAreaLock = new ReentrantLock();
    private final Lock targetAreaLock = new ReentrantLock();

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

    public void rescale(double factor) {
        if (factor > 0 && factor != 1) {
            PROPERTIES.setGRAVITY(factor * PROPERTIES.getGRAVITY());
            PROPERTIES.setWIDTH((int) (PROPERTIES.getWIDTH() * factor));
            PROPERTIES.setHEIGHT((int) (PROPERTIES.getHEIGHT() * factor));
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
        inputAreaLock.lock();
        try {
            return inputArea;

        }finally {
            inputAreaLock.unlock();
        }
    }

    public void setInputArea(ComplexArea inputArea) {
        inputAreaLock.lock();
        try {
            if (PROPERTIES.getTYPE().equals(AnimationType.GAME)) {
                this.inputArea = inputArea;
            }
        }finally {
            inputAreaLock.unlock();
        }
    }

    public ComplexArea getTargetArea() {
        targetAreaLock.lock();
        try {
                return targetArea;

        }finally {
            targetAreaLock.unlock();
        }
    }

    public void setTargetArea(ComplexArea targetArea) {
        targetAreaLock.lock();
        try {
            if (PROPERTIES.getTYPE().equals(AnimationType.GAME)) {
                this.targetArea = targetArea;
            }
        }finally {
            targetAreaLock.unlock();
        }
    }

    public void addMovingObject(MovingObject obj) {
        movingObjectsLock.lock();
        try {
            movingObjects.add(obj);
        } finally {
            movingObjectsLock.unlock();
        }
    }

    public void removeMovingObject(MovingObject obj) {
        movingObjectsLock.lock();
        try {
            movingObjects.remove(obj);
        } finally {
            movingObjectsLock.unlock();
        }
    }

    public List<MovingObject> getMovingObjects() {
        movingObjectsLock.lock();
        try {
            return movingObjects;
        } finally {
            movingObjectsLock.unlock();
        }
    }
    public void removeMovingObjectToAdd(MovingObject obj) {
        movingObjectsToAddLock.lock();
        try {
            movingObjectsToAdd.remove(obj);
        } finally {
            movingObjectsToAddLock.unlock();
        }
    }

    public void addMovingObjectToAdd(MovingObject obj) {
        movingObjectsToAddLock.lock();
        try {
            movingObjectsToAdd.add(obj);
        } finally {
            movingObjectsToAddLock.unlock();
        }
    }
    public List<MovingObject> getMovingObjectsToAdd() {
        movingObjectsToAddLock.lock();
        try {
            return movingObjectsToAdd;
        } finally {
            movingObjectsToAddLock.unlock();
        }
    }
    public void addObstacle(Area obs) {
        obstaclesLock.lock();
        try {
            obstacles.add(obs);
        } finally {
            obstaclesLock.unlock();
        }
    }

    public void removeObstacle(Area obs) {
        obstaclesLock.lock();
        try {
            obstacles.remove(obs);
        } finally {
            obstaclesLock.unlock();
        }
    }

    public List<Area> getObstacles() {
        obstaclesLock.lock();
        try {
            return obstacles;
        } finally {
            obstaclesLock.unlock();
        }
    }
    public void addObstacleToAdd(Area obs) {
        obstaclesToAddLock.lock();
        try {
            obstaclesToAdd.add(obs);
        } finally {
            obstaclesToAddLock.unlock();
        }
    }

    public void removeObstacleToAdd(Area obs) {
        obstaclesToAddLock.lock();
        try {
            obstaclesToAdd.remove(obs);
        } finally {
            obstaclesToAddLock.unlock();
        }
    }

    public List<Area> getObstaclesToAdd() {
        obstaclesToAddLock.lock();
        try {
            return new ArrayList<>(obstaclesToAdd);
        } finally {
            obstaclesToAddLock.unlock();
        }
    }



}
