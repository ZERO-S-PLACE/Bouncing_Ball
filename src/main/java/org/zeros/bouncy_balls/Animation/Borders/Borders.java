package org.zeros.bouncy_balls.Animation.Borders;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Calculations.Bounce;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;

public class Borders {
    private final AnimationProperties PROPERTIES;
    private final Animation animation;
    private final LinearEquation OX = new LinearEquation(0, 0);
    private final LinearEquation OY = new LinearEquation(0, Double.NaN);
    private final LinearEquation OY2;
    private final LinearEquation OX2;

    public Borders(Animation animation) {
        this.PROPERTIES = animation.getPROPERTIES();
        this.animation = animation;
        OY2 = new LinearEquation(animation.getPROPERTIES().getWIDTH(), Double.NaN);
        OX2 = new LinearEquation(0, animation.getPROPERTIES().getHEIGHT());
    }

    public boolean isInside(Point2D point) {
        return point.getX() >= 0 && point.getX() <= PROPERTIES.getWIDTH() && point.getY() >= 0 && point.getY() <= PROPERTIES.getHEIGHT();
    }

    public boolean isInside(Ball ball) {
        double radius = ball.getRadius();
        return ball.center().getX() >= radius && ball.center().getX() <= PROPERTIES.getWIDTH() - radius && ball.center().getY() >= radius && ball.center().getY() <= PROPERTIES.getHEIGHT() - radius;
    }

    public boolean isInside(Point2D center, double spanMax) {
        return center.getX() >= spanMax && center.getX() <= PROPERTIES.getWIDTH() - spanMax && center.getY() >= spanMax && center.getY() <= PROPERTIES.getHEIGHT() - spanMax;
    }

    public boolean ballFromWall(Ball ball) {

        Point2D newVelocity;
        if (ball.nextCenter().getX() <= ball.getRadius() && ball.velocity().getX() <= 0) {
            newVelocity = new Point2D(-ball.velocity().getX(), ball.velocity().getY());
            Bounce.setCenterAfterBounce(OY, ball, newVelocity);

        } else if (ball.nextCenter().getX() >= PROPERTIES.getWIDTH() - ball.getRadius() && ball.velocity().getX() >= 0) {
            newVelocity = new Point2D(-ball.velocity().getX(), ball.velocity().getY());
            Bounce.setCenterAfterBounce(OY2, ball, newVelocity);

        } else if (ball.nextCenter().getY() >= PROPERTIES.getHEIGHT() - ball.getRadius() && ball.velocity().getY() >= 0) {
            newVelocity = new Point2D(ball.velocity().getX(), -ball.velocity().getY());
            Bounce.setCenterAfterBounce(OX2, ball, newVelocity);

        } else if (ball.nextCenter().getY() <= ball.getRadius() && ball.velocity().getY() <= 0) {
            newVelocity = new Point2D(ball.velocity().getX(), -ball.velocity().getY());
            Bounce.setCenterAfterBounce(OX, ball, newVelocity);

        } else {
            return false;
        }
        return true;


    }

    public boolean moveToOtherSide(MovingObject obj) {
        if (obj.nextCenter().getX() <= -obj.getFurthestSpan() && obj.velocity().getX() <= 0) {
            obj.updateCenter(new Point2D(PROPERTIES.getWIDTH() + obj.getFurthestSpan(), obj.nextCenter().getY()));
            obj.updateNextCenter(obj.center());
            obj.updateVelocity(obj.velocity(), 1);
        } else if (obj.nextCenter().getX() >= PROPERTIES.getWIDTH() + obj.getFurthestSpan() && obj.velocity().getX() >= 0) {
            obj.updateCenter(new Point2D(-obj.getFurthestSpan(), obj.nextCenter().getY()));
            obj.updateNextCenter(obj.center());
            obj.updateVelocity(obj.velocity(), 1);
        } else if (obj.nextCenter().getY() >= PROPERTIES.getHEIGHT() + obj.getFurthestSpan() && obj.velocity().getY() >= 0) {
            obj.updateCenter(new Point2D(obj.nextCenter().getX(), -obj.getFurthestSpan()));
            obj.updateNextCenter(obj.center());
            obj.updateVelocity(obj.velocity(), 1);
        } else if (obj.nextCenter().getY() <= -obj.getFurthestSpan() && obj.velocity().getY() <= 0) {
            obj.updateCenter(new Point2D(obj.nextCenter().getX(), PROPERTIES.getHEIGHT() + obj.getFurthestSpan()));
            obj.updateNextCenter(obj.center());
            obj.updateVelocity(obj.velocity(), 1);
        } else {
            return false;
        }
        return true;
    }


}
