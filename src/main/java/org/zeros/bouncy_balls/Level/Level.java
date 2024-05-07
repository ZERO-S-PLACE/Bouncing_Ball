package org.zeros.bouncy_balls.Level;

import org.zeros.bouncy_balls.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {
    public final AnimationProperties PROPERTIES;
    public final ArrayList<MovingObject> movingObjects = new ArrayList<>();
    public final ArrayList<Obstacle> obstacles = new ArrayList<>();
    public Level(AnimationProperties properties) {
        PROPERTIES = properties;
    }
}
