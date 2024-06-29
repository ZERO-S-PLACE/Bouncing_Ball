package org.zeros.bouncy_balls.Animation.Borders;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Calculations.Bounce;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.LineSegment;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.util.ArrayList;

public class Borders {
    private final int WIDTH;
    private final int HEIGHT;
    private final LinearEquation OX = new LinearEquation(0, 0);
    private final LinearEquation OY = new LinearEquation(0, Double.NaN);
    private final LinearEquation OY2;
    private final LinearEquation OX2;
    private final ArrayList<LineSegment> boundaryLines = new ArrayList<>();

    public Borders(Animation animation) {
        AnimationProperties PROPERTIES = animation.getPROPERTIES();
        WIDTH = PROPERTIES.getWIDTH();
        HEIGHT = PROPERTIES.getHEIGHT();
        OY2 = new LinearEquation(WIDTH, Double.NaN);
        OX2 = new LinearEquation(0, HEIGHT);
        boundaryLines.add(new LineSegment(new Point2D(0, 0), new Point2D(0, HEIGHT)));
        boundaryLines.add(new LineSegment(new Point2D(0, HEIGHT), new Point2D(WIDTH, HEIGHT)));
        boundaryLines.add(new LineSegment(new Point2D(WIDTH, HEIGHT), new Point2D(WIDTH, 0)));
        boundaryLines.add(new LineSegment(new Point2D(WIDTH, 0), new Point2D(0, 0)));
    }

    public boolean isInside(Point2D point) {
        return point.getX() >= 0 && point.getX() <= WIDTH && point.getY() >= 0 && point.getY() <= HEIGHT;
    }

    public boolean isInside(Ball ball) {
        double radius = ball.getRadius();
        return ball.center().getX() >= radius && ball.center().getX() <= WIDTH - radius && ball.center().getY() >= radius && ball.center().getY() <= HEIGHT - radius;
    }

    public boolean isInside(Point2D center, double spanMax) {
        return center.getX() >= spanMax && center.getX() <= WIDTH - spanMax && center.getY() >= spanMax && center.getY() <= HEIGHT - spanMax;
    }

    public boolean ballFromWall(Ball ball) {

        Point2D newVelocity;
        if (ball.nextCenter().getX() <= ball.getRadius() && ball.velocity().getX() <= 0) {
            newVelocity = new Point2D(-ball.velocity().getX(), ball.velocity().getY());
            Bounce.setCenterAfterBounce(OY, ball, newVelocity);

        } else if (ball.nextCenter().getX() >= WIDTH - ball.getRadius() && ball.velocity().getX() >= 0) {
            newVelocity = new Point2D(-ball.velocity().getX(), ball.velocity().getY());
            Bounce.setCenterAfterBounce(OY2, ball, newVelocity);

        } else if (ball.nextCenter().getY() >= HEIGHT - ball.getRadius() && ball.velocity().getY() >= 0) {
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
            obj.updateCenter(new Point2D(WIDTH + obj.getFurthestSpan(), obj.nextCenter().getY()));
            obj.updateNextCenter(obj.center());
            obj.updateVelocity(obj.velocity(), 1);
        } else if (obj.nextCenter().getX() >= WIDTH + obj.getFurthestSpan() && obj.velocity().getX() >= 0) {
            obj.updateCenter(new Point2D(-obj.getFurthestSpan(), obj.nextCenter().getY()));
            obj.updateNextCenter(obj.center());
            obj.updateVelocity(obj.velocity(), 1);
        } else if (obj.nextCenter().getY() >= HEIGHT + obj.getFurthestSpan() && obj.velocity().getY() >= 0) {
            obj.updateCenter(new Point2D(obj.nextCenter().getX(), -obj.getFurthestSpan()));
            obj.updateNextCenter(obj.center());
            obj.updateVelocity(obj.velocity(), 1);
        } else if (obj.nextCenter().getY() <= -obj.getFurthestSpan() && obj.velocity().getY() <= 0) {
            obj.updateCenter(new Point2D(obj.nextCenter().getX(), HEIGHT + obj.getFurthestSpan()));
            obj.updateNextCenter(obj.center());
            obj.updateVelocity(obj.velocity(), 1);
        } else {
            return false;
        }
        return true;
    }


    public boolean isInside(Area obstacle) {
        for (Point2D point : obstacle.getCorners()) {
            if (!isInside(point)) return false;
        }
        for (Segment segment : boundaryLines) {
            for (Segment segment1 : obstacle.getSegments()) {
                if (!segment.getIntersectionsWith(segment1).isEmpty()) return false;
            }
        }
        return true;
    }
}
