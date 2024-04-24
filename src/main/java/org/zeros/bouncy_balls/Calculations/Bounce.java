package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.Obstacles.Obstacle;

public class Bounce {
    private static final LinearEquation OX = new LinearEquation(0, 0);
    private static final LinearEquation OY = new LinearEquation(0, Double.NaN);
    private static final LinearEquation OY2 = new LinearEquation(Properties.getGAME_WIDTH(), Double.NaN);
    private static final LinearEquation OX2 = new LinearEquation(0, Properties.getGAME_HEIGHT());

    public static boolean ballFromWall(Ball ball, Point2D outCenter) {

        Point2D newVelocity;
        if (outCenter.getX() <= ball.getRadius() && ball.velocity().getX() < 0) {
            newVelocity = new Point2D(-ball.velocity().getX(), ball.velocity().getY());
            setCenterAfterBounce(OY, ball, newVelocity);
            ball.updateVelocity(newVelocity);
        } else if (outCenter.getX() >= Properties.getGAME_WIDTH() - ball.getRadius() && ball.velocity().getX() > 0) {
            newVelocity = new Point2D(-ball.velocity().getX(), ball.velocity().getY());
            setCenterAfterBounce(OY2, ball, newVelocity);
            ball.updateVelocity(newVelocity);
        } else if (outCenter.getY() >= Properties.getGAME_HEIGHT() - ball.getRadius() && ball.velocity().getY() > 0) {
            newVelocity = new Point2D(ball.velocity().getX(), -ball.velocity().getY());
            setCenterAfterBounce(OX2, ball, newVelocity);
            ball.updateVelocity(newVelocity);
        } else if (outCenter.getY() <= ball.getRadius() && ball.velocity().getY() < 0) {
            newVelocity = new Point2D(ball.velocity().getX(), -ball.velocity().getY());
            setCenterAfterBounce(OX, ball, newVelocity);
            ball.updateVelocity(newVelocity);
        } else {
            return false;
        }
        return true;


    }

    public static boolean twoBalls(Ball ball1, Ball ball2) {

        matchCurrentTime(ball1, ball2);

        Double t = calculateTimeToCollision(ball1, ball2);
        if (t == null) return false;

        Point2D centerAtBounce1 = ball1.center().add(ball1.frameVelocity().multiply(t).multiply((1 - ball1.frameElapsed())));
        Point2D centerAtBounce2 = ball2.center().add(ball2.frameVelocity().multiply(t).multiply((1 - ball1.frameElapsed())));
        LinearEquation bounceLine = new LinearEquation(centerAtBounce1, centerAtBounce2).perpendicularTroughPoint(centerAtBounce1).offsetLine(ball1.getRadius(), centerAtBounce2);

        // Velocity change calculation by vector decomposition
        Point2D v11 = ball1.velocity();
        Point2D v21 = ball2.velocity();
        double angle = bounceLine.angle();

        double m1 = ball1.getMass();
        double m2 = ball2.getMass();
        v11 = VectorMath.rotateVector(v11, angle);
        v21 = VectorMath.rotateVector(v21, angle);

        double vPerp12 = (v11.getY() * (m1 - m2) + 2 * m2 * v21.getY()) / (m1 + m2);
        double vPerp22 = (v21.getY() * (m2 - m1) + 2 * m1 * v11.getY()) / (m1 + m2);

        Point2D v12 = new Point2D(v11.getX(), vPerp12);
        Point2D v22 = new Point2D(v21.getX(), vPerp22);

        v12 = VectorMath.rotateVector(v12, -angle);
        v22 = VectorMath.rotateVector(v22, -angle);


        if (ball1.center().distance(ball1.nextCenter()) != 0) {
            ball1.setFrameElapsed(ball1.frameElapsed() + (1 - ball1.frameElapsed()) / ball1.center().distance(ball1.nextCenter()) * ball1.center().distance(centerAtBounce1));
        } else {
            ball1.setFrameElapsed(ball2.frameElapsed() + (1 - ball2.frameElapsed()) / ball2.center().distance(ball2.nextCenter()) * ball2.center().distance(centerAtBounce1));
        }
        ball2.setFrameElapsed(ball1.frameElapsed());

        ball1.updateCenter(centerAtBounce1);
        ball2.updateCenter(centerAtBounce2);
        ball1.updateVelocity(v12);
        ball2.updateVelocity(v22);
        ball1.updateNextCenter(ball1.center().add(ball1.frameVelocity().multiply(1 - ball1.frameElapsed())));
        ball2.updateNextCenter(ball2.center().add(ball2.frameVelocity().multiply(1 - ball2.frameElapsed())));
        return true;
    }
    
    private static Double calculateTimeToCollision(Ball ball1, Ball ball2) {
    /*equation describing when distance between middle points of the circles moving in
    velocities (Vx1,Vy1) and (Vx2,Vy2) with starting points(x1,y1),(x2,y2) is equal to d
    in dependence of time has form :

    t^2*((Vx1-Vx2)^2+(Vy1-Vy2)^2))+t*2*((Vx1-Vx2)*(X1-X2)+(Vy1-Vy2)*(Y1-Y2)+(X1-X2)^2+(Y1-Y2)^2-d^2)

    to solve this we have to find the smallest positive solution
    */
        double d = ball1.getRadius() + ball2.getRadius();
        double dVx = (ball1.frameVelocity().getX() - ball2.frameVelocity().getX()) * (1 - ball1.frameElapsed());
        double dVy = (ball1.frameVelocity().getY() - ball2.frameVelocity().getY()) * (1 - ball1.frameElapsed());
        double dX = ball1.center().getX() - ball2.center().getX();
        double dY = ball1.center().getY() - ball2.center().getY();
        double a = Math.pow(dVx, 2) + Math.pow(dVy, 2);
        double b = 2 * (dVx * dX + dVy * dY);
        double c = Math.pow(dX, 2) + Math.pow(dY, 2) - Math.pow(d, 2);
        QuadraticEquation equation = new QuadraticEquation(a, b, c);
        double t;
        if (equation.getSolution1() <= equation.getSolution2() && equation.getSolution1() >= 0) {
            t = equation.getSolution1();
        } else if (equation.getSolution1() >= 0) {
            t = equation.getSolution2();
        } else {
            return null;
        }
        return t;
    }

    private static void matchCurrentTime(Ball ball1, Ball ball2) {
        if (ball1.frameElapsed() > ball2.frameElapsed()) {
            ball2.updateCenter(ball2.center().add(ball2.frameVelocity().multiply(ball1.frameElapsed() - ball2.frameElapsed())));
            ball2.setFrameElapsed(ball1.frameElapsed());
        }
        if (ball1.frameElapsed() < ball2.frameElapsed()) {
            ball1.updateCenter(ball1.center().add(ball1.frameVelocity().multiply(ball2.frameElapsed() - ball1.frameElapsed())));
            ball1.setFrameElapsed(ball2.frameElapsed());
        }
    }


    public static boolean ballFromObstacle(Ball ball, Obstacle obstacle) {

        ResultBouncingSet result= new ResultBouncingSet(new Point2D(Double.MAX_VALUE, Double.MAX_VALUE),
                new Point2D(-Double.MAX_VALUE, -Double.MAX_VALUE),null);

        for (int i = 0; i < obstacle.getCornerLines().size(); i++) {

            if(obstacle.controlPointsInSegment(i)==0) {
                result=checkStraightLine(ball,obstacle,result,i);
            }
            else if(obstacle.controlPointsInSegment(i)==1) {
                result=checkBezierCurve(ball,obstacle,result,i);
            }
            else if(obstacle.controlPointsInSegment(i)==2) {
                result=checkBezierCurve(ball,obstacle,result,i);
            }
        }

        if (result.bounceLine() == null) {
            return false;
        } else {
            Point2D newVelocity = mirrorVelocityFromLine(ball.velocity(), result.bounceLine());
            setCenterAfterBounce(result.bounceLine(), ball, newVelocity);
            ball.updateVelocity(newVelocity);
            return true;
        }

    }

    private record ResultBouncingSet(Point2D bouncePointAtLine, Point2D bouncePointAtBall, LinearEquation bounceLine) { }


    private static ResultBouncingSet checkStraightLine(Ball ball,Obstacle obstacle,ResultBouncingSet result,int segment) {

        Point2D first=obstacle.getCorners().get(segment);
        Point2D second=obstacle.getCorners().get(segment+1);
        LinearEquation line = obstacle.getCornerLines().get(segment);

        LinearEquation standardTangent = line.parallelTroughPoint(ball.center())
                .offsetLine(ball.getRadius(), ball.nextCenter());
        Point2D tBouncePointAtBall = standardTangent.intersection(standardTangent.perpendicularTroughPoint(ball.center()));
        Point2D tBouncePointAtLine = line.intersection(ball.trajectory().parallelTroughPoint(tBouncePointAtBall));

        if (tBouncePointAtLine != null) {
            if (BindsCheck.isBetweenPoints(tBouncePointAtLine, first, second)
                    && tBouncePointAtBall.distance(tBouncePointAtLine)
                    < result.bouncePointAtBall.distance(result.bouncePointAtLine)) {
                return new ResultBouncingSet(tBouncePointAtLine,tBouncePointAtBall,line);
            } else if (ball.trajectory().distance(first) < ball.getRadius()) {
                return checkBounceFromCorner(ball, result, first);
            }
        }
        return result;
    }

    private static ResultBouncingSet checkBounceFromCorner(Ball ball, ResultBouncingSet result, Point2D corner) {

        Point2D tBouncePointAtBall;
        Point2D tBouncePointAtLine=corner;

        double distance = ball.trajectory().distance(corner);
        double offset = Math.sqrt(ball.getRadius() * ball.getRadius() - distance * distance);
        tBouncePointAtBall = ball.trajectory().parallelTroughPoint(corner)
                .intersection(ball.trajectory().perpendicularTroughPoint(ball.center()).offsetLine(offset,ball.nextCenter()));
        LinearEquation bounceLine=new LinearEquation(ball.center(),tBouncePointAtBall).perpendicularTroughPoint(corner);
        if (tBouncePointAtBall.distance(tBouncePointAtLine) <=
                result.bouncePointAtBall.distance(result.bouncePointAtLine)) {
            return new ResultBouncingSet(tBouncePointAtLine, tBouncePointAtBall,bounceLine);
        }

        return result;
    }
    private static ResultBouncingSet checkBezierCurve(Ball ball, Obstacle obstacle, ResultBouncingSet result, int segment) {

        Point2D first = obstacle.getCorners().get(segment);
        ResultBouncingSet tResult=result;


        for (int j = obstacle.controlPointsTotalCount(segment)-obstacle.controlPointsInSegment(segment);
             j<=obstacle.controlPointsTotalCount(segment); j++) {
            Point2D second;
            if( j == obstacle.controlPointsTotalCount(segment)){
                second= obstacle.getCorners().get(segment +1);
            }else {
                second= obstacle.getControlPoints().get(j);
            }

            LinearEquation line = obstacle.getCurvesLines().get(j+obstacle.getCurvedSegmentsCount(segment)-1);
            LinearEquation standardTangent = line.parallelTroughPoint(ball.center())
                    .offsetLine(ball.getRadius(), ball.nextCenter());
            Point2D tBouncePointAtBall = standardTangent.intersection(standardTangent.perpendicularTroughPoint(ball.center()));
            Point2D tBouncePointAtLine = line.intersection(ball.trajectory().parallelTroughPoint(tBouncePointAtBall));

            if (tBouncePointAtLine != null) {
                if (BindsCheck.isBetweenPoints(tBouncePointAtLine, first, second)
                        && tBouncePointAtBall.distance(tBouncePointAtLine)
                        < result.bouncePointAtBall.distance(result.bouncePointAtLine)) {
                    tResult= new ResultBouncingSet(tBouncePointAtLine,tBouncePointAtBall,line);
                } else if (ball.trajectory().distance(first) < ball.getRadius()) {
                    tResult= checkBounceFromCorner(ball, result, first);
                }
            }

            first=second;
        }
        return tResult;
    }




    private static void setCenterAfterBounce(LinearEquation bounceLine, Ball ball, Point2D newVelocity) {
        double velocityChange = ball.velocity().magnitude() / newVelocity.magnitude();
        LinearEquation tangent = bounceLine.parallelTroughPoint(ball.center()).offsetLine(ball.getRadius(), ball.nextCenter());
        Point2D bouncePointOnBall = tangent.intersection(tangent.perpendicularTroughPoint(ball.center()));
        Point2D bouncePointOnLine = ball.trajectory().parallelTroughPoint(bouncePointOnBall).intersection(bounceLine);

        double moveDistancePerpFull = bounceLine.distance(ball.center());
        if (bounceLine.areOnDifferentSides(ball.center(), ball.nextCenter())) {
            moveDistancePerpFull = moveDistancePerpFull + bounceLine.distance(ball.nextCenter());

        } else {
            moveDistancePerpFull = moveDistancePerpFull - bounceLine.distance(ball.nextCenter());
        }

        Point2D bounceMomentCenter = bounceLine.offsetLine(ball.getRadius(), ball.center()).intersection(bounceLine.perpendicularTroughPoint(bouncePointOnLine));

        double moveDistancePerpBeforeBounce = bounceLine.distance(ball.center()) - bounceLine.distance(bounceMomentCenter);
        LinearEquation newTangent = bounceLine.offsetLine((moveDistancePerpFull - moveDistancePerpBeforeBounce) / velocityChange, ball.center());
        LinearEquation newTrajectory = new LinearEquation(newVelocity, new Point2D(0, 0)).parallelTroughPoint(bouncePointOnLine);

        Point2D bouncePointAfter = newTangent.intersection(newTrajectory);
        Point2D finalCenter = newTangent.offsetLine(ball.getRadius(), bouncePointAfter.add(newVelocity)).intersection(newTangent.perpendicularTroughPoint(bouncePointAfter));


        ball.setBounced(true);
        ball.setFrameElapsed(ball.frameElapsed() + (1 - ball.frameElapsed()) * bounceMomentCenter.distance(ball.center()) / ball.center().distance(ball.nextCenter()));

        ball.updateCenter(bounceMomentCenter);
        ball.updateNextCenter(finalCenter);

    }

    private static Point2D mirrorVelocityFromLine(Point2D velocity, LinearEquation bounceLine) {

        Point2D newVelocity = VectorMath.rotateVector(velocity, bounceLine.angle());
        newVelocity = new Point2D(newVelocity.getX(), -newVelocity.getY());
        return VectorMath.rotateVector(newVelocity, -bounceLine.angle());
    }



}

