package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;

import java.util.ArrayList;

public class BindsCheck {


    public static boolean isInsideRoughBinds(MovingObject movingObject, Obstacle obstacle) {

        return movingObject.nextCenter().getX() +2* movingObject.getFurthestSpan() >= obstacle.getRoughMin().getX() &&
                movingObject.nextCenter().getX() - 2*movingObject.getFurthestSpan() <= obstacle.getRoughMax().getX() &&
                movingObject.nextCenter().getY() + 2*movingObject.getFurthestSpan() >= obstacle.getRoughMin().getY() &&
                movingObject.nextCenter().getY() - 2*movingObject.getFurthestSpan() <= obstacle.getRoughMax().getY();
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


    public static boolean intersectWithCornersPolygon(Ball ball,ArrayList<LinearEquation> lines,ArrayList<Point2D> points) {
        Point2D first= points.getFirst();
        LinearEquation ray=new LinearEquation(ball.nextCenter(),new Point2D(0,0));
        Point2D second=new Point2D(Double.MAX_VALUE,Double.MAX_VALUE);
        LinearEquation edge;
        double intersections =0.0;
        for (int i = 0; i< lines.size(); i++){
            second= points.get(i+1);
            edge=lines.get(i);
            if(edge.distance(ball.nextCenter())<=ball.getRadius()){
                if(BindsCheck.isBetweenPoints(edge.intersection(edge.perpendicularTroughPoint(ball.nextCenter())),first,second))
                    return true;}
                if(ball.nextCenter().distance(first)<=ball.getRadius()){

                    return true;
                }


            Point2D intersection=edge.intersection(ray);
                if(intersection!=null){
                if(BindsCheck.isBetweenPoints(intersection,first,second)&&
                        BindsCheck.isBetweenPoints(intersection, ball.nextCenter(),ray.getPoint(Double.MAX_VALUE)))
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
        if(ball.nextCenter().distance(second)<=ball.getRadius()){

            return true;
        }

        return intersections % 2 != 0;
    }
    public static boolean intersectWithCurveBoundary(Ball ball,ArrayList<LinearEquation> lines,ArrayList<Point2D> points)
    {
        lines.add(new LinearEquation(points.getLast(),points.getFirst()));
        points.add(points.getFirst());
        boolean temp=intersectWithCornersPolygon( ball, lines, points);
        lines.removeLast();
        points.removeLast();
        return temp;


    }



}
