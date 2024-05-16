package org.zeros.bouncy_balls.Calculations;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Calculations.Equations.BezierCurve;
import org.zeros.bouncy_balls.Calculations.Equations.LinearEquation;
import org.zeros.bouncy_balls.Calculations.Equations.QuadraticEquation;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;

import java.util.ArrayList;

public class Bounce {
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

        double vPerpendicular12 = (v11.getY() * (m1 - m2) + 2 * m2 * v21.getY()) / (m1 + m2);
        double vPerpendicular22 = (v21.getY() * (m2 - m1) + 2 * m1 * v11.getY()) / (m1 + m2);

        Point2D v12 = new Point2D(v11.getX(), vPerpendicular12);
        Point2D v22 = new Point2D(v21.getX(), vPerpendicular22);

        v12 = VectorMath.rotateVector(v12, -angle);
        v22 = VectorMath.rotateVector(v22, -angle);
        double frameElapsed;


        if (ball1.center().distance(ball1.nextCenter()) != 0) {
            frameElapsed = (ball1.frameElapsed() + (1 - ball1.frameElapsed()) / ball1.center().distance(ball1.nextCenter()) * ball1.center().distance(centerAtBounce1));
        } else {
            frameElapsed = (ball2.frameElapsed() + (1 - ball2.frameElapsed()) / ball2.center().distance(ball2.nextCenter()) * ball2.center().distance(centerAtBounce1));
        }


        ball1.updateCenter(centerAtBounce1);
        ball2.updateCenter(centerAtBounce2);
        ball1.updateVelocity(v12, frameElapsed);
        ball2.updateVelocity(v22, frameElapsed);
        ball1.updateNextCenter(ball1.center().add(ball1.frameVelocity().multiply(1 - frameElapsed)));
        ball2.updateNextCenter(ball2.center().add(ball2.frameVelocity().multiply(1 - frameElapsed)));
        return true;
    }

    public static Double calculateTimeToCollision(Ball ball1, Ball ball2) {

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
            ball2.updateVelocity(ball2.velocity(), ball1.frameElapsed());
        } else {
            ball1.updateCenter(ball1.center().add(ball1.frameVelocity().multiply(ball2.frameElapsed() - ball1.frameElapsed())));
            ball1.updateVelocity(ball1.velocity(), ball2.frameElapsed());
        }
    }


    public static boolean ballFromObstacle(Ball ball, Area obstacle) {

        ResultBouncingSet result = new ResultBouncingSet(new Point2D(Double.MAX_VALUE, Double.MAX_VALUE), new Point2D(-Double.MAX_VALUE, -Double.MAX_VALUE), null);

        for (int i = 0; i < obstacle.getCornerLines().size(); i++) {
            if (obstacle.getSegmentPoints(i).size() == 2) {
                result = checkStraightLine(ball, obstacle, result, i);
            } else {
                result = checkBezierCurve(ball, obstacle, result, i);
            }
        }

        if (result.bounceLine() == null) {
            return false;
        } else if ((result.bouncePointAtLine.distance(result.bouncePointAtBall) <= ball.center().distance(ball.nextCenter()))) {

            Point2D newVelocity = mirrorVelocityFromLine(ball.velocity(), result.bounceLine());
            return setCenterAfterBounce(result.bounceLine(), ball, newVelocity);

        }
        return false;
    }

    private static ResultBouncingSet checkStraightLine(Ball ball, Area obstacle, ResultBouncingSet result, int segment) {

        Point2D first = obstacle.getSegmentPoints(segment).getFirst();
        Point2D second = obstacle.getSegmentPoints(segment).getLast();
        LinearEquation line = obstacle.getCornerLines().get(segment);

        LinearEquation standardTangent = line.parallelTroughPoint(ball.center()).offsetLine(ball.getRadius(), ball.nextCenter());
        Point2D tBouncePointAtBall = standardTangent.intersection(standardTangent.perpendicularTroughPoint(ball.center()));
        Point2D tBouncePointAtLine = line.intersection(ball.trajectory().parallelTroughPoint(tBouncePointAtBall));

        if (tBouncePointAtLine != null) {
            if (BindsCheck.isBetweenPoints(tBouncePointAtLine, first, second) && tBouncePointAtBall.distance(tBouncePointAtLine) <= result.bouncePointAtBall.distance(result.bouncePointAtLine)) {
                return new ResultBouncingSet(tBouncePointAtLine, tBouncePointAtBall, line);
            } else if (ball.trajectory().distance(first) <= ball.getRadius()) {
                return checkBounceFromCorner(ball, result, first);
            }
        }
        return result;
    }

    private static ResultBouncingSet checkBounceFromCorner(Ball ball, ResultBouncingSet result, Point2D corner) {

        double distance = ball.trajectory().distance(corner);
        double offset = Math.sqrt(ball.getRadius() * ball.getRadius() - distance * distance);
        Point2D tBouncePointAtBall = ball.trajectory().parallelTroughPoint(corner).intersection(ball.trajectory().perpendicularTroughPoint(ball.center()).offsetLine(offset, ball.nextCenter()));
        LinearEquation bounceLine = new LinearEquation(ball.center(), tBouncePointAtBall).perpendicularTroughPoint(corner);
        if (tBouncePointAtBall.distance(corner) <= result.bouncePointAtBall.distance(result.bouncePointAtLine)) {
            return new ResultBouncingSet(corner, tBouncePointAtBall, bounceLine);
        }
        return result;
    }

    private static ResultBouncingSet checkBezierCurve(Ball ball, Area obstacle, ResultBouncingSet result, int segment) {

        if (BindsCheck.intersectWithCurveBoundary(ball, obstacle.getSegmentLines(segment), obstacle.getSegmentPoints(segment))) {
            if (obstacle.getSegmentLines(segment).getFirst().distance(ball.nextCenter()) <= ball.getRadius()) {
                if (!BindsCheck.isBetweenPoints(obstacle.getSegmentLines(segment).getFirst().intersection(obstacle.getSegmentLines(segment).getFirst().perpendicularTroughPoint(ball.nextCenter())), obstacle.getSegmentPoints(segment).getFirst(), obstacle.getSegmentPoints(segment).get(1))) {

                    if (ball.nextCenter().distance(obstacle.getSegmentPoints(segment).getFirst()) <= ball.getRadius()) {
                        return checkBounceFromCorner(ball, result, obstacle.getSegmentPoints(segment).getFirst());
                    }
                }
            }
            return checkExactBezierIntersection(ball, obstacle.getSegmentPoints(segment), result);
        }
        return result;
    }

    private static ResultBouncingSet checkExactBezierIntersection(Ball ball, ArrayList<Point2D> curvePoints, ResultBouncingSet result) {

        BezierCurve bezierCurve = new BezierCurve(curvePoints);
        Point2D tBouncePointAtLine = findBouncePointOnCurve(bezierCurve, ball);
        if (tBouncePointAtLine != null) {
            double offset = Math.sqrt(Math.pow(ball.getRadius(), 2) - Math.pow(ball.trajectory().distance(tBouncePointAtLine), 2));
            Point2D centerAtBounce = ball.trajectory().perpendicularTroughPoint(tBouncePointAtLine).offsetLine(offset, ball.center()).intersection(ball.trajectory());
            LinearEquation bounceLine = new LinearEquation(centerAtBounce, tBouncePointAtLine).perpendicularTroughPoint(tBouncePointAtLine);
            LinearEquation tangent = bounceLine.parallelTroughPoint(ball.center()).offsetLine(ball.getRadius(), ball.nextCenter());
            Point2D tBouncePointAtBall = tangent.intersection(tangent.perpendicularTroughPoint(ball.center()));
            if (tBouncePointAtBall.distance(tBouncePointAtLine) <= result.bouncePointAtBall.distance(result.bouncePointAtLine)) {
                return new ResultBouncingSet(tBouncePointAtLine, tBouncePointAtBall, bounceLine);
            }

        }
        return result;
    }

    private static Point2D findBouncePointOnCurve(BezierCurve bezierCurve, Ball ball) {
        int divisions = (int) ball.getRadius() % 10 + 15;
        LinearEquation diameter = ball.trajectory().perpendicularTroughPoint(ball.center());
        double distance = Double.MAX_VALUE;
        double offset = ball.getRadius() / divisions;

        Point2D pointOnLine = bezierCurve.getClosestIntersectionWithLine(ball.center(), ball.frameVelocity());
        if (pointOnLine != null) {
            distance = ball.center().distance(pointOnLine) - ball.getRadius();
        }

        for (int i = 1; i <= divisions; i++) {
            double iOffset = offset * i - 0.0001;
            Point2D tPointInBall1 = ball.trajectory().offsetLine(iOffset).intersection(diameter);
            Point2D tPointInBall2 = ball.trajectory().offsetLine(-iOffset).intersection(diameter);
            Point2D tPointOnLine1 = bezierCurve.getClosestIntersectionWithLine(tPointInBall1, ball.velocity());
            Point2D tPointOnLine2 = bezierCurve.getClosestIntersectionWithLine(tPointInBall2, ball.velocity());
            double chordLength = Math.sqrt(Math.pow(ball.getRadius(), 2) - Math.pow(iOffset, 2));
            double tDistance;
            if (tPointOnLine1 != null) {
                tDistance = tPointOnLine1.distance(tPointInBall1) - chordLength;
                if (tDistance < distance) {
                    distance = tDistance;
                    pointOnLine = tPointOnLine1;

                }
            }
            if (tPointOnLine2 != null) {
                tDistance = tPointOnLine2.distance(tPointInBall2) - chordLength;
                if (tDistance < distance) {
                    distance = tDistance;
                    pointOnLine = tPointOnLine2;

                }
            }
        }
        return pointOnLine;
    }

    public static boolean setCenterAfterBounce(LinearEquation bounceLine, Ball ball, Point2D newVelocity) {
        if (bounceLine.getA() == ball.trajectory().getA() || (bounceLine.isVertical() && ball.trajectory().isVertical())) {
            return false;
        }
        Point2D bounceMomentCenter = bounceLine.offsetLine(ball.getRadius(), ball.center()).intersection(ball.trajectory());
        double timeOfMove = ball.center().distance(bounceMomentCenter) / ball.frameVelocity().magnitude();
        if (timeOfMove + ball.frameElapsed() > 1) return false;

        ball.updateCenter(bounceMomentCenter);
        ball.updateVelocity(newVelocity, ball.frameElapsed() + timeOfMove);
        if (!(willBounceAgain(ball) && bounceLine.getA() == 0)) {
            ball.updateNextCenter(ball.center().add(ball.frameVelocity().multiply(timeOfMove)));
        } else {
            return bounceAgain(bounceLine, ball);
        }
        return true;
    }

    private static boolean bounceAgain(LinearEquation bounceLine, Ball ball) {
        //works only if assumed that height of next bounce is very low, and bounce line is horizontal

        double timeToStop = Math.abs(ball.velocity().getY() / ball.acceleration().getY());
        ball.updateCenter(ball.center().add(ball.frameVelocity().multiply(timeToStop / 2)));
        if (timeToStop < 0.1) {
            ball.updateCenter(bounceLine.perpendicularTroughPoint(ball.center()).intersection(bounceLine.offsetLine(ball.getRadius(), ball.center())));
            ball.updateVelocity(new Point2D(ball.velocity().getX(), 0), ball.frameElapsed());
            return false;
        }

        double timeToNextBounce = Math.sqrt(2 * bounceLine.distance(ball.center()) / ball.acceleration().getY());
        if (ball.frameElapsed() + timeToNextBounce + timeToStop <= 1) {
            ball.updateCenter(bounceLine.perpendicularTroughPoint(ball.center()).intersection(bounceLine.offsetLine(ball.getRadius(), ball.center())));
            ball.updateVelocity(ball.velocity(), ball.frameElapsed() + timeToStop + timeToNextBounce);
            ball.updateVelocity(new Point2D(ball.velocity().getX(), -ball.velocity().getY()), ball.frameElapsed());
            if (ball.frameElapsed() + Math.abs(ball.velocity().getY() / ball.acceleration().getY()) <= 1) {
                return bounceAgain(bounceLine, ball);
            }
        }
        return true;

    }

    private static boolean willBounceAgain(Ball ball) {
        return ball.velocity().getY() + ball.acceleration().getY() * (1 - ball.frameElapsed()) < 0 && ball.acceleration().getY() > 0;
    }

    private static Point2D mirrorVelocityFromLine(Point2D velocity, LinearEquation bounceLine) {

        Point2D newVelocity = VectorMath.rotateVector(velocity, bounceLine.angle());
        newVelocity = new Point2D(newVelocity.getX(), -newVelocity.getY());
        return VectorMath.rotateVector(newVelocity, -bounceLine.angle());
    }

    private record ResultBouncingSet(Point2D bouncePointAtLine, Point2D bouncePointAtBall, LinearEquation bounceLine) {
    }


}

