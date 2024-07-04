package org.zeros.bouncy_balls.Level;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.SerializableObjects.LevelSerializable;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.io.*;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Level implements Serializable {
    private final AnimationProperties PROPERTIES;
    private final List<MovingObject> movingObjects = new CopyOnWriteArrayList<>();
    private final List<Area> obstacles = new CopyOnWriteArrayList<>();
    private final List<MovingObject> movingObjectsToAdd = new CopyOnWriteArrayList<>();
    private final List<MovingObject> movingObjectsHaveToEnter = new CopyOnWriteArrayList<>();
    private final List<MovingObject> movingObjectsCannotEnter = new CopyOnWriteArrayList<>();
    private final List<Area> obstaclesToAdd = new CopyOnWriteArrayList<>();
    private final Lock movingObjectsLock = new ReentrantLock();
    private final Lock movingObjectsToAddLock = new ReentrantLock();
    private final Lock obstaclesLock = new ReentrantLock();
    private final Lock obstaclesToAddLock = new ReentrantLock();
    private final Lock inputAreaLock = new ReentrantLock();
    private final Lock targetAreaLock = new ReentrantLock();
    private final Lock movingObjectsHaveToEnterLock = new ReentrantLock();
    private final Lock movingObjectsCannotEnterLock = new ReentrantLock();
    private String NAME = "New_Level";
    private ComplexArea inputArea;
    private ComplexArea targetArea;

    private int oneStarBound = 2000;
    private int twoStarBound = 5000;


    private int threeStarBound = 10000;

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
    private void save(String name)throws FileSystemException {
        name = name.replace(" ", "_");
        name = name.replace(".", "_");
        setNAME(name);
        LevelSerializable save = new LevelSerializable(this);
        name = name + ".ser";
        if (PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
            name = "program_data/user_levels/" + name;
        } else {
            name = "program_data/user_simulations/" + name;
        }
        try (FileOutputStream fileOut = new FileOutputStream(name); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(save);
        } catch (IOException e) {
            throw new FileSystemException("Wrong name");
        }
    }

    public static String getDirectoryPath(AnimationType type, String subtype) {
        String directoryPath;
        if (!subtype.equals("User")) {
            if (type.equals(AnimationType.GAME)) {
                directoryPath = "program_data/default_levels/" + subtype;
            } else {
                directoryPath = "program_data/default_simulations/" + subtype;
            }
        } else {
            if (type.equals(AnimationType.GAME)) {
                directoryPath = "program_data/user_levels";
            } else {
                directoryPath = "program_data/user_simulations";
            }
        }
        return directoryPath;
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

        } finally {
            inputAreaLock.unlock();
        }
    }

    public void setInputArea(ComplexArea inputArea) {
        inputAreaLock.lock();
        try {
            if (PROPERTIES.getTYPE().equals(AnimationType.GAME)) {
                this.inputArea = inputArea;
            }
        } finally {
            inputAreaLock.unlock();
        }
    }

    public ComplexArea getTargetArea() {
        targetAreaLock.lock();
        try {
            return targetArea;

        } finally {
            targetAreaLock.unlock();
        }
    }

    public void setTargetArea(ComplexArea targetArea) {
        targetAreaLock.lock();
        try {
            if (PROPERTIES.getTYPE().equals(AnimationType.GAME)) {
                this.targetArea = targetArea;
            }
        } finally {
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

    public List<MovingObject> getMovingObjects() {
        movingObjectsLock.lock();
        try {
            return movingObjects;
        } finally {
            movingObjectsLock.unlock();
        }
    }

    public void removeMovingObjectToAdd(MovingObject obj) {
        obj.updateCenter(new Point2D(-10000, -10000));
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
            movingObjectsToAdd.addFirst(obj);
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

    public List<Area> getObstacles() {
        obstaclesLock.lock();
        try {
            return obstacles;
        } finally {
            obstaclesLock.unlock();
        }
    }

    public void addObstacleToAdd(Area obs) {
        obs.move(new Point2D(-10000, -10000));
        obstaclesToAddLock.lock();
        try {
            obstaclesToAdd.addFirst(obs);
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
            return obstaclesToAdd;
        } finally {
            obstaclesToAddLock.unlock();
        }
    }

    public void addMovingObjectHaveToEnter(MovingObject obj) {
        movingObjectsHaveToEnterLock.lock();
        try {
            movingObjectsHaveToEnter.add(obj);
        } finally {
            movingObjectsHaveToEnterLock.unlock();
        }
    }
    public List<MovingObject> getMovingObjectsHaveToEnter() {
        movingObjectsHaveToEnterLock.lock();
        try {
            return movingObjectsHaveToEnter;
        } finally {
            movingObjectsHaveToEnterLock.unlock();
        }
    }

    public void addMovingObjectCannotEnter(MovingObject obj) {
        movingObjectsCannotEnterLock.lock();
        try {
            movingObjectsCannotEnter.add(obj);
        } finally {
            movingObjectsCannotEnterLock.unlock();
        }
    }

    public List<MovingObject> getMovingObjectsCannotEnter() {
        movingObjectsCannotEnterLock.lock();
        try {
            return movingObjectsCannotEnter;
        } finally {
            movingObjectsCannotEnterLock.unlock();
        }
    }

    public int getOneStarBound() {
        return oneStarBound;
    }

    public void setOneStarBound(int oneStarBound) {
        this.oneStarBound = oneStarBound;
    }

    public int getTwoStarBound() {
        return twoStarBound;
    }

    public void setTwoStarBound(int twoStarBound) {
        this.twoStarBound = twoStarBound;
    }

    public int getThreeStarBound() {
        return threeStarBound;
    }

    public void setThreeStarBound(int threeStarBound) {
        this.threeStarBound = threeStarBound;
    }


}
