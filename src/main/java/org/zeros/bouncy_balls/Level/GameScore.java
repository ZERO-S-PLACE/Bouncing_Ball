package org.zeros.bouncy_balls.Level;

import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.Model.Model;

import java.io.*;

public class GameScore implements Serializable {

    private final String user;
    private final String subType;
    private final String levelName;
    private final double scoreValue;

    public GameScore(String user, String subType, String levelName, double scoreValue) {
        this.user = user;
        this.subType = subType;
        this.levelName = levelName;
        this.scoreValue = scoreValue;
    }

    public static GameScore load(String subType, String levelName) {
        String path = Level.getDirectoryPath(AnimationType.GAME, subType) + "/Scores/" + levelName + ".ser";
        GameScore save = null;
        try (FileInputStream fileIn = new FileInputStream(path); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Object object = in.readObject();
            if (object instanceof GameScore) {
                save = (GameScore) object;
            }
        } catch (ClassNotFoundException | IOException ignored) {

        }
        return save;
    }

    public void save() {
        String path = Level.getDirectoryPath(AnimationType.GAME, subType) + "/Scores/" + levelName + ".ser";
        try (FileOutputStream fileOut = new FileOutputStream(path);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static double getScore(Level level) {
        return level.getOneStarBound()+ level.getMovingObjectsCannotEnter().size()*300+
                level.getMovingObjectsToAdd().size()*400+ level.getObstaclesToAdd().size()*500
                +30*(level.PROPERTIES().getTIME()-
                (double) Model.getInstance().getViewFactory().getCurrentAnimationPane().getAnimation().getTimeElapsedNanos() /1_000_000_000);
    }

    public String getUser() {
        return user;
    }

    public String getSubType() {
        return subType;
    }

    public String getLevelName() {
        return levelName;
    }

    public double getScoreValue() {
        return scoreValue;
    }

}
