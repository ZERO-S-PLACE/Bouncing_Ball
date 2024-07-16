package org.zeros.bouncy_balls.Objects.SerializableObjects;

import javafx.scene.paint.Paint;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;

import java.io.Serializable;

public class MovingObjectSerializable implements Serializable {

    private final MovingObjectType type;
    private final double furthestSpan;
    private final double friction;
    private final double mass;
    private final Point2DSerializable velocity;
    private final Point2DSerializable centerPoint;
    private final double charge;


    public MovingObjectSerializable(MovingObject object) {
        this.type = object.getType();
        this.furthestSpan = object.getFurthestSpan();
        this.friction = object.getFriction();
        this.mass = object.getMass();
        this.velocity = new Point2DSerializable(object.velocity());
        this.centerPoint = new Point2DSerializable(object.center());
        this.charge = object.getCharge();

    }

    public MovingObject deserialize(Animation animation) {
        MovingObject object = null;
        if (this.type.equals(MovingObjectType.BALL)) {
            object = new Ball(furthestSpan, animation);
            object.setFriction(friction);
            object.setMass(mass);
            object.setInitialVelocity(velocity.deserialize());
            object.updateCenter(centerPoint.deserialize());
            object.updateNextCenter(centerPoint.deserialize());
            object.setCharge(charge);

        }
        return object;

    }


}
