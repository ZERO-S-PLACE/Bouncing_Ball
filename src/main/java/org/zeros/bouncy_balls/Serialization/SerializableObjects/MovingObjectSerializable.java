package org.zeros.bouncy_balls.Serialization.SerializableObjects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObjectType;

import java.io.Serializable;

public class MovingObjectSerializable implements Serializable {

    protected MovingObjectType type;
    protected  double furthestSpan;
    protected double friction;
    protected double mass;
    protected Point2DSerializable velocity;
    protected Point2DSerializable centerPoint;
    protected double charge;
    protected String color;

    protected MovingObjectSerializable(MovingObject object) {
        this.type=object.getType();
        this.furthestSpan=object.getFurthestSpan();
        this.friction=object.getFriction();
        this.mass=object.getMass();
        this.velocity=new Point2DSerializable(object.velocity());
        this.centerPoint=new Point2DSerializable(object.center());
        this.charge=object.getCharge();
        this.color=object.getShape().fillProperty().getValue().toString();
    }

    public MovingObject deserialize(Animation animation){
        MovingObject object=null;
        if(this.type.equals(MovingObjectType.BALL)){
            object=new Ball(furthestSpan,animation);
            object.setFriction(friction);
            object.setMass(mass);
            object.setInitialVelocity(velocity.deserialize());
            object.updateCenter(centerPoint.deserialize());
            object.updateNextCenter(centerPoint.deserialize());
            object.setCharge(charge);
            object.getShape().setFill(Paint.valueOf(color));
        }
        return object;

    }





}
