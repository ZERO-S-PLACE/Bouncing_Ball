package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;

import java.util.ArrayList;

public class BindsCheck {
    public static boolean isInsideBorders(Point2D point){
        return point.getX() >= 0 && point.getX() <= Properties.getGAME_WIDTH() &&
                point.getY() >= 0 && point.getY() <= Properties.getGAME_HEIGHT();
    }
    public static boolean isInsideBorders(Ball ball){
        double radius =ball.getRadius();
        return ball.center().getX() >= radius && ball.center().getX() <= Properties.getGAME_WIDTH()-radius &&
                ball.center().getY() >= radius && ball.center().getY() <= Properties.getGAME_HEIGHT()-radius;
    }
    public static boolean isInsideBorders(Point2D center, double spanMax){
        return center.getX() >= spanMax && center.getX() <= Properties.getGAME_WIDTH()-spanMax &&
                center.getY() >= spanMax && center.getY() <= Properties.getGAME_HEIGHT()-spanMax;
    }

    public static boolean isInsideRoughBinds(MovingObject movingObject, Obstacle obstacle) {

        return movingObject.nextCenter().getX() + movingObject.getFurthestSpan() >= obstacle.getRoughMin().getX() &&
                movingObject.nextCenter().getX() - movingObject.getFurthestSpan() <= obstacle.getRoughMax().getX() &&
                movingObject.nextCenter().getY() + movingObject.getFurthestSpan() >= obstacle.getRoughMin().getY() &&
                movingObject.nextCenter().getY() - movingObject.getFurthestSpan() <= obstacle.getRoughMax().getY();
    }

    public static boolean isBetweenPoints(Point2D point,Point2D border1,Point2D border2){


            return point.getX()>=Math.min(border1.getX(),border2.getX())&&
                    point.getX()<=Math.max(border1.getX(),border2.getX())&&
                    point.getY()>=Math.min(border1.getY(),border2.getY())&&
                    point.getY()<=Math.max(border1.getY(),border2.getY());

    }

    public static boolean intersectsWithObstacle(Ball ball, Obstacle obstacle) {
        return (intersectWithCornersPolygon(ball, obstacle.getCornerLines(),obstacle.getCorners())||
                intersectWithCornersPolygon(ball, obstacle.getAllLines(),obstacle.getAllPoints()));
    }


    private static boolean intersectWithCornersPolygon(Ball ball,ArrayList<LinearEquation> lines,ArrayList<Point2D> points) {
        Point2D first= points.getFirst();
        LinearEquation infinity=new LinearEquation(Double.MAX_VALUE,Double.NaN);
        LinearEquation ray=new LinearEquation(ball.nextCenter(),new Point2D(0,0));
        Point2D second;
        LinearEquation edge;
        double intersections =0.0;
        for (int i = 0; i< points.size()-1; i++){
            second= points.get(i+1);
            edge=lines.get(i);
            if(edge.distance(ball.nextCenter())<=ball.getRadius()&&(
            BindsCheck.isBetweenPoints(edge.intersection(edge.perpendicularTroughPoint(ball.nextCenter())),first,second)||
            first.distance(ball.nextCenter())<=ball.getRadius())){
                    return true;
            }

            Point2D intersection=edge.intersection(ray);
                if(intersection!=null){
                if(BindsCheck.isBetweenPoints(intersection,first,second)&&
                        BindsCheck.isBetweenPoints(intersection, ball.nextCenter(),ray.intersection(infinity)))
                    {
                    if (intersection.equals(first) || intersection.equals(second)) {
                        intersections=intersections+0.5;
                    }
                    else {
                        intersections++;
                    }
                    }

                }
              first=second;
            }
        return intersections % 2 != 0;
    }
}
