package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Animation.Animation;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;

public class Bounce {
    public static void ballFromWall(Ball ball, Point2D outCenter){
        Point2D velocity = ball.velocityProperty().get();

        if(outCenter.getX()<= ball.getRadius()&&velocity.getX()<0){
            ball.velocityProperty().set(new Point2D(-velocity.getX(),velocity.getY()));
        }
        else if(outCenter.getX()>= Properties.getGAME_WIDTH()-ball.getRadius()&&velocity.getX()>0){
            ball.velocityProperty().set(new Point2D(-velocity.getX(),velocity.getY()));
        }else if(outCenter.getY()>= Properties.getGAME_HEIGHT()-ball.getRadius()&&velocity.getY()>0){
            ball.velocityProperty().set(new Point2D(velocity.getX(),-velocity.getY()));
        }else if(outCenter.getY()<= ball.getRadius()&&velocity.getY()<0){
            ball.velocityProperty().set(new Point2D(velocity.getX(),-velocity.getY()));
        }


    }





























   static LinearEquation OX= new LinearEquation(0,0);
static LinearEquation OY=new LinearEquation(0,Double.NaN);
static LinearEquation OY2= new LinearEquation(Properties.getGAME_WIDTH(),Double.NaN);
static LinearEquation OX2= new LinearEquation(0,Properties.getGAME_HEIGHT());

    private static LinearEquation findClosestEdge(Ball ball,  LinearEquation trajectory) {

        Point2D oldCenter=ball.centerPointProperty().get();
        Point2D velocity =ball.velocityProperty().get();



        double a = oldCenter.distance(OX.intersection(trajectory));
        double b = oldCenter.distance(OY.intersection(trajectory));
        double c = oldCenter.distance(OX2.intersection(trajectory));
        double d = oldCenter.distance(OY2.intersection(trajectory));


        double min=Math.min(a,Math.min(b,Math.min(c,d)));
        if(min==a&&velocity.getY()<0){return OX;}
        if(min==b&&velocity.getX()<0){return OY;}
        if(min==c&&velocity.getY()>0){return OX2;}
        else{return OY2;}

    }


    public static boolean twoBalls(Ball ball1, Ball ball2) {
        // obliczenie zderzenia przez rozłożenie wektora na składowe równoległe do osi
        Point2D v11=ball1.velocityProperty().get();
        Point2D v21=ball2.velocityProperty().get();

        LinearEquation collisionAxis = new LinearEquation(ball1.centerPointProperty().get(),ball2.centerPointProperty().get());
        LinearEquation collisionParallel= collisionAxis.perpendicularTroughPoint(ball1.centerPointProperty().get());
        double angle=collisionParallel.angle();
        double m1=ball1.getMass();
        double m2=ball2.getMass();


        double vPerp11=-v11.getX()*Math.sin(angle)+v11.getY()*Math.cos(angle);
        double vPar1=v11.getX()*Math.cos(angle)+v11.getY()*Math.sin(angle);
        double vPerp21=-v21.getX()*Math.sin(angle)+v21.getY()*Math.cos(angle);
        double vPar2=v21.getX()*Math.cos(angle)+v21.getY()*Math.sin(angle);
        //obliczenie nowej prędkości prostopadłej


            double vPerp12 = (vPerp11 * (m1 - m2) + 2 * m2 * vPerp21) / (m1 + m2);
            double vPerp22 = (vPerp21 * (m2 - m1) + 2 * m1 * vPerp11) / (m1 + m2);

            double vX1 = vPar1 * Math.cos(angle) - vPerp12 * Math.sin(angle);
            double vY1 = vPar1 * Math.sin(angle) + vPerp12 * Math.cos(angle);

            double vX2 = vPar2 * Math.cos(angle) - vPerp22 * Math.sin(angle);
            double vY2 = vPar2 * Math.sin(angle) + vPerp22 * Math.cos(angle);



            ball1.velocityProperty().setValue(new Point2D(vX1, vY1));
            ball2.velocityProperty().setValue(new Point2D(vX2, vY2));
            return true;










    }
}
/*System.out.println("BOUNCE");
        Point2D oldCenter=ball.centerPointProperty().get();
        System.out.println("oldCenter"+oldCenter);
        System.out.println("oldVelocity"+ball.velocityProperty().get());

        LinearEquation trajectory = new LinearEquation(oldCenter,outCenter);
        LinearEquation bounceLine = findClosestEdge(ball,trajectory);

        LinearEquation tangent=bounceLine.parallelTroughPoint(oldCenter).offsetLine(ball.getRadius());
        Point2D tangencyPoint=tangent.intersection(tangent.perpendicularTroughPoint(oldCenter));
        if(bounceLine.pointDistance(tangencyPoint)>bounceLine.pointDistance(oldCenter))
        {
             tangent=bounceLine.parallelTroughPoint(oldCenter).offsetLine(-ball.getRadius());
             tangencyPoint=tangent.intersection(tangent.perpendicularTroughPoint(oldCenter));
        }


        Point2D bouncePoint=trajectory.parallelTroughPoint(tangencyPoint).intersection(bounceLine);



        ball.centerPointProperty().set(bounceLine.perpendicularTroughPoint(bouncePoint).mirrorPoint(oldCenter));

        ball.velocityProperty().set(bounceLine.parallelTroughPoint(new Point2D(0,0)).mirrorPoint(ball.velocityProperty().get()));

        System.out.println("newCenter"+ball.centerPointProperty().get());
        System.out.println("newVelocity"+ball.velocityProperty().get());
*/
