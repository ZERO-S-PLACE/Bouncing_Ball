package org.zeros.bouncy_balls.Animation;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Calculations.Bounce;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;

public class Borders {
    private final AnimationProperties PROPERTIES;
    private final Animation animation;

    public Borders(Animation animation) {
        this.PROPERTIES = animation.getPROPERTIES();
        this.animation=animation;
        OY2 = new LinearEquation(animation.getPROPERTIES().WIDTH, Double.NaN);
         OX2 = new LinearEquation(0, animation.getPROPERTIES().HEIGHT);
    }

    private  final LinearEquation OX = new LinearEquation(0, 0);
    private  final LinearEquation OY = new LinearEquation(0, Double.NaN);
    private  final LinearEquation OY2;
    private  final LinearEquation OX2;

    public  boolean isInside(Point2D point){
        return point.getX() >= 0 && point.getX() <= PROPERTIES.WIDTH &&
                point.getY() >= 0 && point.getY() <= PROPERTIES.HEIGHT;
    }
    public  boolean isInside(Ball ball){
        double radius =ball.getRadius();
        return ball.center().getX() >= radius && ball.center().getX() <= PROPERTIES.WIDTH-radius &&
                ball.center().getY() >= radius && ball.center().getY() <= PROPERTIES.HEIGHT-radius;
    }
    public  boolean isInside(Point2D center, double spanMax){
        return center.getX() >= spanMax && center.getX() <= PROPERTIES.WIDTH-spanMax &&
                center.getY() >= spanMax && center.getY() <= PROPERTIES.HEIGHT-spanMax;
    }

    public  boolean ballFromWall(Ball ball) {

        Point2D newVelocity;
        if (ball.nextCenter().getX() <= ball.getRadius() && ball.velocity().getX() < 0) {
            newVelocity = new Point2D(-ball.velocity().getX(), ball.velocity().getY());
            Bounce.setCenterAfterBounce(OY, ball, newVelocity);
            ball.updateVelocity(newVelocity);
        } else if (ball.nextCenter().getX() >= PROPERTIES.WIDTH - ball.getRadius() && ball.velocity().getX() > 0) {
            newVelocity = new Point2D(-ball.velocity().getX(), ball.velocity().getY());
            Bounce.setCenterAfterBounce(OY2, ball, newVelocity);
            ball.updateVelocity(newVelocity);
        } else if (ball.nextCenter().getY() >= PROPERTIES.HEIGHT - ball.getRadius() && ball.velocity().getY() > 0) {
            newVelocity = new Point2D(ball.velocity().getX(), -ball.velocity().getY());
            Bounce.setCenterAfterBounce(OX2, ball, newVelocity);
            ball.updateVelocity(newVelocity);
        } else if (ball.nextCenter().getY() <= ball.getRadius() && ball.velocity().getY() < 0) {
            newVelocity = new Point2D(ball.velocity().getX(), -ball.velocity().getY());
            Bounce.setCenterAfterBounce(OX, ball, newVelocity);
            ball.updateVelocity(newVelocity);
        } else {
            return false;
        }
        return true;


    }
    public  boolean moveToOtherSide(MovingObject obj) {
        if (obj.nextCenter().getX() <= - obj.getFurthestSpan() && obj.velocity().getX() < 0) {
            obj.updateCenter(new Point2D(PROPERTIES.WIDTH+obj.getFurthestSpan(),obj.nextCenter().getY()));
            obj.updateNextCenter(obj.center());
            obj.setFrameElapsed(1);
        } else if (obj.nextCenter().getX() >= PROPERTIES.WIDTH + obj.getFurthestSpan() && obj.velocity().getX() > 0) {
            obj.updateCenter(new Point2D(-obj.getFurthestSpan(),obj.nextCenter().getY()));
            obj.updateNextCenter(obj.center());
            obj.setFrameElapsed(1);
        } else if (obj.nextCenter().getY() >= PROPERTIES.HEIGHT + obj.getFurthestSpan() && obj.velocity().getY() > 0) {
            obj.updateCenter(new Point2D(obj.nextCenter().getX(),-obj.getFurthestSpan()));
            obj.updateNextCenter(obj.center());
            obj.setFrameElapsed(1);
        } else if (obj.nextCenter().getY() <= -obj.getFurthestSpan() && obj.velocity().getY() < 0) {
            obj.updateCenter(new Point2D(obj.nextCenter().getX(),PROPERTIES.HEIGHT+obj.getFurthestSpan()));
            obj.updateNextCenter(obj.center());
            obj.setFrameElapsed(1);
        } else {
            return false;
        }
        return true;
    }


}
