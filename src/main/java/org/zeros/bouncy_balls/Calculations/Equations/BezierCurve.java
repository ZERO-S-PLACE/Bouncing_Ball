package org.zeros.bouncy_balls.Calculations.Equations;

import javafx.geometry.Point2D;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.zeros.bouncy_balls.Calculations.AreasMath.ConvexHull;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.VectorMath;

import java.util.ArrayList;
import java.util.Arrays;

public class BezierCurve extends Equation{
    private final int degree;
    private final ArrayList<Point2D> points;
    private ArrayList<Point2D> convexHullPoints;
    private final ArrayList<LinearEquation> convexHullLines=new ArrayList<>();
    private final double[] xPolynomialCoefficients; //curve equation converted to form of polynomial of variable t0
    private final double[] yPolynomialCoefficients;

    public BezierCurve(ArrayList<Point2D> pointsInOrder) {
        this.points = pointsInOrder;
        degree = pointsInOrder.size() - 1;
        xPolynomialCoefficients = new double[degree + 1];
        yPolynomialCoefficients = new double[degree + 1];
        calculateCoefficients();
        convexHullPoints = calculateConvexHull();
    }

    private ArrayList<Point2D> calculateConvexHull() {

        convexHullPoints= ConvexHull.calculate(points);
        for (int i=1;i<convexHullPoints.size();i++){
            convexHullLines.add(new LinearEquation(convexHullPoints.get(i-1),convexHullPoints.get(i)));
        }
        return convexHullPoints;
    }

    public double[] get_xPolynomialCoefficients() {
        return Arrays.copyOf(xPolynomialCoefficients, xPolynomialCoefficients.length);
    }

    public double[] get_yPolynomialCoefficients() {
        return Arrays.copyOf(yPolynomialCoefficients, yPolynomialCoefficients.length);
    }

    private void calculateCoefficients() {
        for (int i = 0; i <= degree; i++) {
            double mp = VectorMath.binomialCoefficient(degree, i);
            for (int j = i; j <= degree; j++) {

                xPolynomialCoefficients[j] = xPolynomialCoefficients[j] + mp * VectorMath.binomialCoefficient(degree - i, j - i) * points.get(i).getX() * Math.pow(-1, j - i);
                yPolynomialCoefficients[j] = yPolynomialCoefficients[j] + mp * VectorMath.binomialCoefficient(degree - i, j - i) * points.get(i).getY() * Math.pow(-1, j - i);

            }
        }

    }

    public Point2D getPointAt(double t0) {
        if (t0 <= 1 && t0 >= 0) {
            double x = 0;
            double y = 0;
            for (int i = 0; i <= degree; i++) {
                x = x + xPolynomialCoefficients[i] * Math.pow(t0, i);
                y = y + yPolynomialCoefficients[i] * Math.pow(t0, i);
            }

            return new Point2D(x, y);
        }
        return null;

    }

    public Point2D getClosestIntersectionWithLine(Point2D center, Point2D direction) {
        ArrayList<Point2D> solutions = getIntersectionsWithLine(center, direction);
        if (solutions.isEmpty()) return null;

        Point2D closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Point2D solution : solutions) {


                double distance = solution.distance(center);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPoint = solution;
                }

        }

        return closestPoint;
    }


    public ArrayList<Point2D> getIntersectionsWithLine(Point2D center, Point2D direction) {

        ArrayList<Double> solutions = getLineIntersectionParameterSolutions(getIntersectionWithLineEquationCoefficients(center, direction));
        ArrayList<Point2D>solutionPoints=new ArrayList<>();
        for (double solution:solutions){
            solutionPoints.add(this.getPointAt(solution));
        }
        return solutionPoints;
    }


    private double[] getIntersectionWithLineEquationCoefficients(Point2D center, Point2D direction) {
    /* Calculating intersection of curve with line
     Xb-Yb*(Vx/Vy)-Xo+Yo*(Vx/Vy)=0
     Yb*m-Xb +c=0
     m=(Vx/Vy)
     c=-Xo+Yo*m
     where Yb and Xb are bezier polynomials*/

        double[] coefficients = this.get_xPolynomialCoefficients();
        if (direction.getY() != 0) {
            double[] yB = this.get_yPolynomialCoefficients();
            double m = direction.getX() / direction.getY();
            double c = -center.getX() + m * center.getY();
            for (int i = 0; i < coefficients.length; i++) {
                coefficients[i] = coefficients[i] - yB[i] * m;
            }
            coefficients[0] = coefficients[0] + c;
        } else {
            coefficients = this.get_yPolynomialCoefficients();
            coefficients[0] = coefficients[0] - center.getY();
        }
        return coefficients;
    }
    private static ArrayList<Double> getLineIntersectionParameterSolutions(double[] coefficients) {
        PolynomialFunction function = new PolynomialFunction(coefficients);
        BrentSolver solver = new BrentSolver(0.0001);
        ArrayList<Double> solutions = new ArrayList<>();
        int loop = 0;
        while (loop < 5) {
            try {
                double temp = solver.solve(100, function, 0.2 * loop, 0.2 + 0.2 * loop);
                if (solutions.isEmpty()) {
                    solutions.add(temp);
                } else if (Math.abs(solutions.getLast() - temp) > 0.05) {
                    solutions.add(temp);
                }
            } catch (Exception ignored) {
            }
            loop++;
        }
        return solutions;
    }

    public boolean areOnDifferentSides(Point2D point1, Point2D point2) {
        return BindsCheck.isBetweenPoints(this.getClosestIntersectionWithLine(point1, point2.subtract(point1)), point1, point2);
    }
    public int getDegree() {
        return degree;
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    public ArrayList<Point2D> getConvexHullPoints() {
        return convexHullPoints;
    }

    public ArrayList<LinearEquation> getConvexHullLines() {
        return convexHullLines;
    }



}
