package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;

public class Transition {
    public static boolean isInside(Point2D point){
        return point.getX() >= 0 && point.getX() <= Properties.getGAME_WIDTH() &&
                point.getY() >= 0 && point.getY() <= Properties.getGAME_HEIGHT();
    }
    public static boolean isInside(Ball ball){
        double radius =ball.getRadius();
        return ball.centerPointProperty().get().getX() >= radius && ball.centerPointProperty().get().getX() <= Properties.getGAME_WIDTH()-radius &&
                ball.centerPointProperty().get().getY() >= radius && ball.centerPointProperty().get().getY() <= Properties.getGAME_HEIGHT()-radius;
    }
    public static boolean isInside(Point2D center, double xMax,double yMax){
        return center.getX() >= xMax && center.getX() <= Properties.getGAME_WIDTH()-xMax &&
                center.getY() >= yMax && center.getY() <= Properties.getGAME_HEIGHT()-yMax;
    }
}
