package org.zeros.bouncy_balls.Calculations.Equations;

import javafx.geometry.Point2D;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.zeros.bouncy_balls.Calculations.ConvexHull.ConvexHull;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Exceptions.WrongValueException;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.LineSegment;

import java.util.ArrayList;
import java.util.Arrays;

public class BezierCurve extends Equation {
    private final int degree;
    private final ArrayList<Point2D> points;
    private final ArrayList<LineSegment> convexHull;
    private final double[] xPolynomialCoefficients; //curve equation converted to form of polynomial of variable t0
    private final double[] yPolynomialCoefficients;

    public BezierCurve(ArrayList<Point2D> pointsInOrder) {
        this.points = pointsInOrder;
        degree = pointsInOrder.size() - 1;
        xPolynomialCoefficients = new double[degree + 1];
        yPolynomialCoefficients = new double[degree + 1];
        calculateCoefficients();
        convexHull = ConvexHull.calculateLines(points);
    }



    private static ArrayList<Double> getPolynomialSolutions(double[] coefficients) {
        PolynomialFunction function = new PolynomialFunction(coefficients);
        BrentSolver solver = new BrentSolver(Properties.ACCURACY() * Properties.ACCURACY() * Properties.ACCURACY() * Properties.ACCURACY());
        ArrayList<Double> solutions = new ArrayList<>();
        int loop = 0;
        while (loop < 10) {
            try {
                double temp = solver.solve(500, function, 0.1 * loop, 0.1 + 0.1 * loop);
                if (solutions.isEmpty()) {
                    solutions.add(temp);
                } else if (Math.abs(solutions.getLast() - temp) > Properties.ACCURACY()) {
                    solutions.add(temp);
                }
            } catch (Exception ignored) {
            }
            loop++;
        }
        return solutions;
    }

    public static ArrayList<Point2D> getIntersections(BezierCurve curve, LineSegment line) {
        ArrayList<Point2D> intersections = new ArrayList<>();

        if (ConvexHull.hullIntersectsWithLine(curve.convexHull, line)) {
            if (curve.couldBeSimplified()) {
                LineSegment lineB = curve.simplifyToLine();
                if (BindsCheck.linesIntersect(line, lineB)) {
                    intersections.add(line.getEquation().intersection(lineB.getEquation()));
                }
            } else {
                ArrayList<BezierCurve> subCurves = curve.getSubCurves(0.5);
                for (BezierCurve subCurve : subCurves) {
                    intersections.addAll(BezierCurve.getIntersections(subCurve, line));
                }
            }
        }
        return intersections;
    }

    public static ArrayList<Point2D> getIntersections(BezierCurve curve1, BezierCurve curve2) {
        ArrayList<Point2D> intersections = new ArrayList<>();
        if (curve1.equals(curve2)) {
            return intersections;
        }
        if (ConvexHull.hullsIntersects(curve1.convexHull, curve2.convexHull)) {

            if (curve1.couldBeSimplified() && curve2.couldBeSimplified()) {
                LineSegment lineSegment1 = curve1.simplifyToLine();
                LineSegment lineSegment2 = curve2.simplifyToLine();
                if (BindsCheck.linesIntersect(lineSegment1, lineSegment2)) {
                    intersections.add(lineSegment1.getEquation().intersection(lineSegment2.getEquation()));
                }
            } else if (curve1.getPoints().getFirst().distance(curve1.getPoints().getLast()) > curve2.getPoints().getFirst().distance(curve2.getPoints().getLast())) {
                ArrayList<BezierCurve> subCurves = curve1.getSubCurves(0.5);
                for (BezierCurve subCurve : subCurves) {
                    intersections.addAll(BezierCurve.getIntersections(subCurve, curve2));
                }
            } else {
                ArrayList<BezierCurve> subCurves = curve2.getSubCurves(0.5);
                for (BezierCurve subCurve : subCurves) {
                    intersections.addAll(BezierCurve.getIntersections(subCurve, curve1));
                }
            }
        }
        return intersections;
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

    /*public Point2D getPointAt(double pointParameter) {
        if(pointParameter<=endParameter&&pointParameter>=startParameter) {

            double x = 0;
            double y = 0;
            for (int i = 0; i <= degree; i++) {
                x = x + xPolynomialCoefficients[i] * Math.pow(t0, i);
                y = y + yPolynomialCoefficients[i] * Math.pow(t0, i);
            }

            return new Point2D(x, y);
        }
        throw new WrongValueException(startParameter,endParameter,pointParameter);

    }*/
    public Point2D getPointAt(double pointParameter) {
        if (pointParameter == 1) return points.getLast();
        if (pointParameter == 0) return points.getFirst();
        if (pointParameter < 1 && pointParameter > 0) {

            Point2D[][] casteliau = getCasteljauTree(pointParameter);
            return casteliau[casteliau.length - 1][0];
        }
        throw new WrongValueException(0, 1, pointParameter);

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

    public ArrayList<Point2D> getIntersectionsWithLine(Point2D pointOnLine, Point2D direction) {

        ArrayList<Double> solutions = getPolynomialSolutions(getLineIntersectionEquationCoefficients(pointOnLine, direction));
        ArrayList<Point2D> solutionPoints = new ArrayList<>();
        for (double solution : solutions) {
            solutionPoints.add(getPointAt(solution));
        }
        return solutionPoints;
    }

    private double[] getLineIntersectionEquationCoefficients(Point2D linePoint, Point2D direction) {
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
            double c = -linePoint.getX() + m * linePoint.getY();
            for (int i = 0; i < coefficients.length; i++) {
                coefficients[i] = coefficients[i] - yB[i] * m;
            }
            coefficients[0] = coefficients[0] + c;
        } else {
            coefficients = this.get_yPolynomialCoefficients();
            coefficients[0] = coefficients[0] - linePoint.getY();
        }
        return coefficients;
    }

    public ArrayList<BezierCurve> getSubCurves(double brakeParameter) {
        //calculates sub curves of curve broken in point of given parameter by Casteliau subdivision
        ArrayList<BezierCurve> subCurves = new ArrayList<>();
        if (brakeParameter < 1 && brakeParameter > 0) {

            Point2D[][] coefficients = getCasteljauTree(brakeParameter);

            ArrayList<Point2D> firstCurvePoints = new ArrayList<>();
            ArrayList<Point2D> secondCurvePoints = new ArrayList<>();
            for (int i = 0; i < degree + 1; i++) {
                firstCurvePoints.add(coefficients[i][0]);
                secondCurvePoints.add(coefficients[coefficients.length-1-i][coefficients[coefficients.length-1-i].length - 1]);
            }

            subCurves.add(new BezierCurve(firstCurvePoints));
            subCurves.add(new BezierCurve(secondCurvePoints));

        } else if (brakeParameter == 0 || brakeParameter == 1) {
            subCurves.add(this);
        }else {
            throw new IllegalArgumentException("Brake parameter is out of bounds");
        }
        return subCurves;
    }


    public BezierCurve getSubCurveExact(double end1, double end2) {
        if(end1!=end2&&end1 >= 0 && end1 <= 1 && end2 >= 0 && end2 <= 1) {
            double start = Math.min(end1, end2);
            double end = Math.max(end1, end2);
            if (start==0&&end==1)return this;
            if(start==0)return getSubCurves(end).getFirst();
            if(end==1)return getSubCurves(start).getLast();
            ArrayList<BezierCurve> subCurves = getSubCurves(start);
            return subCurves.getLast().getSubCurves(getParameterAtPoint(this.getPointAt(end)).getFirst()).getFirst();

        }
        throw new IllegalArgumentException("One of parameters is out of bounds "+end1+end2);
    }
    public BezierCurve getSubCurveRough(double end1, double end2) {
        if(end1!=end2&&end1 >= 0 && end1 <= 1 && end2 >= 0 && end2 <= 1) {
            double start = Math.min(end1, end2);
            double end = Math.max(end1, end2);
            if (start==0&&end==1)return this;
            if(start==0)return getSubCurves(end).getFirst();
            if(end==1)return getSubCurves(start).getLast();

            ArrayList<BezierCurve> subCurves = getSubCurves(start);
            return subCurves.getLast().getSubCurves((end - start) / (1 - start)).getFirst();

        }
        throw new IllegalArgumentException("One of parameters is out of bounds "+end1+end2);
    }

    private Point2D[][] getCasteljauTree(double t0) {
        Point2D[][] coefficients = new Point2D[degree + 1][];

        for (int i = 0; i < degree + 1; i++) {
            coefficients[i] = new Point2D[degree + 1 - i];
            for (int j = 0; j < degree + 1 - i; j++) {
                if (i == 0) {
                    coefficients[0][j] = points.get(j);
                } else {
                    coefficients[i][j] = coefficients[i - 1][j].multiply(1 - t0).add(coefficients[i - 1][j + 1].multiply(t0));
                }
            }
        }
        return coefficients;
    }

    public boolean couldBeSimplified() {
        LinearEquation equation = new LinearEquation(points.getFirst(), points.getLast());
        for (int i = 1; i < points.size() - 1; i++) {
            if (equation.distance(points.get(i)) >= Properties.ACCURACY()/5) {
                return false;
            }
        }

        return true;
    }

    public LineSegment simplifyToLine() {

        return new LineSegment(points.getFirst(), points.getLast());
    }

    public LinearEquation getTangentAt(double t0) {
        Point2D[][] tree = getCasteljauTree(t0);
        return new LinearEquation(tree[tree.length - 2][0], tree[tree.length - 2][1]);
    }

    /*public ArrayList<Double> getParameterAtPoint(Point2D point) {
        if (point.distance(points.getFirst()) <= Properties.ACCURACY()/100) return new ArrayList<>(List.of(startParameter));
        if (point.distance(points.getLast()) <= Properties.ACCURACY()/100) return new ArrayList<>(List.of(endParameter));
        double[] xCoefficients = get_xPolynomialCoefficients();
        xCoefficients[0] = xCoefficients[0] - point.getX();
        ArrayList<Double> xParameters = getPolynomialSolutions(xCoefficients);
        double[] yCoefficients = get_yPolynomialCoefficients();
        yCoefficients[0] = yCoefficients[0] - point.getY();
        ArrayList<Double> yParameters = getPolynomialSolutions(yCoefficients);
        ArrayList<Double> solutions = new ArrayList<>();
        if(!xParameters.isEmpty()&&!yParameters.isEmpty()) {
            for (double xSolution : xParameters) {
                for (double ySolution : yParameters) {
                    if (ySolution >= xSolution - Properties.ACCURACY() / 100 && ySolution <= xSolution + Properties.ACCURACY() / 100) {
                        solutions.add(startParameter + ((xSolution + ySolution) * 0.5) * (endParameter - startParameter));
                    }
                }
            }
        }else {
            xParameters.addAll(yParameters);
            for (double solution : xParameters) {
                        solutions.add(startParameter + (solution) * (endParameter - startParameter));
            }
        }
        return solutions;
    }*/
    public ArrayList<Double> getParameterAtPoint(Point2D point) {
        return getParameterAtPoint(point,0,1);
    }
    protected ArrayList<Double> getParameterAtPoint(Point2D point,double startParameter,double endParameter) {

         ArrayList<Double> solutions=new ArrayList<>();
         Point2D start=getPointAt(startParameter);
         Point2D end=getPointAt(endParameter);

        if (point.distance(start) <= Properties.ACCURACY()/2 )
        {solutions.add(startParameter);
        return solutions;}
         if (point.distance(end) <= Properties.ACCURACY()/2 )
        { solutions.add(endParameter);
        return solutions;}
         if (getSubCurveRough(startParameter,endParameter).couldBeSimplified()&&start.distance(end)<10*Properties.ACCURACY()) {
                    LinearEquation line=new LinearEquation(start,end);
                    Point2D intersection=line.intersection(line.perpendicularTroughPoint(point));
                    double parameter=startParameter+(endParameter-startParameter)*start.distance(intersection)/start.distance(end);
                    if(BindsCheck.isBetweenPoints(intersection,start,end)&&parameter>=0&&parameter<=1){
                        if(point.distance(getPointAt(parameter))<=Properties.ACCURACY()) {
                            solutions.add(parameter);
                            return solutions;
                        }
        }
         }else {

             double midParameter = (startParameter + endParameter) / 2;
             if (endParameter - startParameter > (double) 1 / degree) {

                 if (ConvexHull.isInside(this.getSubCurveRough(startParameter, midParameter).convexHull, point)) {
                     solutions.addAll(getParameterAtPoint(point, startParameter, midParameter));
                 }
                 if (ConvexHull.isInside(this.getSubCurveRough(midParameter, endParameter).convexHull, point)) {
                     solutions.addAll(getParameterAtPoint(point, midParameter, endParameter));
                 }
             } else {
                 Point2D midpoint = getPointAt(midParameter);
                 if (BindsCheck.isBetweenPoints(point, start, midpoint)) {
                     solutions.addAll(getParameterAtPoint(point, startParameter, midParameter));
                 }
                 if (BindsCheck.isBetweenPoints(point, midpoint, end)) {
                     solutions.addAll(getParameterAtPoint(point, midParameter, endParameter));
                 }
             }
         }


        solutions.replaceAll(this::getParameterValue);
         if(solutions.size()>1) {
             removeRepeatingValues(solutions);
         }

       /* System.out.println(solutions);
        if (!solutions.isEmpty()) {
            System.out.println("given:" + point + "calculated: " + getPointAt(solutions.getFirst()));
            System.out.println("start" + points.getFirst() + " end: " + points.getLast());
            System.out.println(point.distance(getPointAt(solutions.getFirst())));

        }*/

        return solutions;

    }

    private double getParameterValue(Double solution) {
        if(solution>1)return 1;
        if(solution<0)return 0;
        return solution;
    }

    private void removeRepeatingValues(ArrayList<Double> solutions) {
        ArrayList<Double> toRemove=new ArrayList<>();
        for (int i=0;i<solutions.size();i++){
            for (int j=i+1;i<solutions.size();i++){
                if(solutions.get(i)>=solutions.get(j)-Math.pow(Properties.ACCURACY(),2)&&
                        solutions.get(i)<=solutions.get(j)+Math.pow(Properties.ACCURACY(),2)){
                        solutions.remove(j);
                }
            }
        }
    }


    public int getDegree() {
        return degree;
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }


    public ArrayList<LineSegment> getConvexHullLines() {
        return convexHull;
    }
    public boolean isEqualTo(BezierCurve curve) {
        if (this.getPoints().size() != curve.getPoints().size()) return false;
        int incrementFactor;
        int start;
        if (this.getPoints().getFirst().distance(curve.getPoints().getFirst())<=Properties.ACCURACY()) {
            incrementFactor = 1;
            start = 0;
        } else if (this.getPoints().getFirst().distance(curve.getPoints().getLast())<=Properties.ACCURACY()) {
            incrementFactor = -1;
            start = this.getPoints().size() - 1;
        } else return false;

        for (int i = 1; i < this.getPoints().size(); i++) {
            if (this.getPoints().get(i).distance(curve.getPoints().get(start + incrementFactor * i))>=Properties.ACCURACY()) {
                return false;
            }
        }
        return true;
    }

}
