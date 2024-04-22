package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;

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

    public static boolean isInsideRoughBinds(Point2D point, double span,Obstacle obstacle) {

        return point.getX()+span >= obstacle.getRoughMin().getX() && point.getX()-span <= obstacle.getRoughMax().getX() &&
                point.getY() +span>= obstacle.getRoughMin().getY() && point.getY()-span <= obstacle.getRoughMax().getY();

    }
    public static boolean isBetweenPoints(Point2D point,Point2D border1,Point2D border2){


            return point.getX()>=Math.min(border1.getX(),border2.getX())&&
                    point.getX()<=Math.max(border1.getX(),border2.getX())&&
                    point.getY()>=Math.min(border1.getY(),border2.getY())&&
                    point.getY()<=Math.max(border1.getY(),border2.getY());

    }

    public static boolean intersectsWithObstacle(Ball ball, Obstacle obstacle) {
        //calculating if nextCenter is inside obstacle based on Ray casting algorithm
        Point2D first=obstacle.getPointsList().getFirst();
        Point2D second;
        LinearEquation edge;

        double intersections =0.0;

        for (int i=1;i<obstacle.getPointsList().size();i++){
            second=obstacle.getPointsList().get(i);
            edge=new LinearEquation(first,second);
            if(first.distance(ball.nextCenter())<ball.getRadius()){
                return true;
            }
            Point2D closestPointOnLine=edge.perpendicularTroughPoint(ball.nextCenter()).intersection(edge);

           if(BindsCheck.isBetweenPoints(closestPointOnLine,first,second)&&
                   closestPointOnLine.distance(ball.nextCenter())<ball.getRadius()){
               return true;
           }

            LinearEquation ray=new LinearEquation(ball.nextCenter(),new Point2D(0,0));
            Point2D intersection=edge.intersection(ray);
                if(intersection!=null){
                if(BindsCheck.isBetweenPoints(intersection,first,second)&&
                        BindsCheck.isBetweenPoints(intersection,ball.nextCenter(),ray.intersection(new LinearEquation(Double.MAX_VALUE,Double.NaN))))
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

           return intersections%2!=0;
    }
}
