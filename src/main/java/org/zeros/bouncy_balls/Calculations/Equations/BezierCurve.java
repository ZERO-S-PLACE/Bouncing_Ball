package org.zeros.bouncy_balls.Calculations.Equations;

import javafx.geometry.Point2D;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.zeros.bouncy_balls.Calculations.AreasMath.ConvexHull;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Exceptions.WrongValueException;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.Area.PolyLineSegment.LineSegment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BezierCurve extends Equation{
    private final int degree;
    private final ArrayList<Point2D> points;
    private ArrayList<LineSegment> convexHull;
    private final double[] xPolynomialCoefficients; //curve equation converted to form of polynomial of variable t0
    private final double[] yPolynomialCoefficients;

    private double startParameter=0;
    private double endParameter=1;

    public BezierCurve(ArrayList<Point2D> pointsInOrder) {
        this.points = pointsInOrder;
        degree = pointsInOrder.size() - 1;
        xPolynomialCoefficients = new double[degree + 1];
        yPolynomialCoefficients = new double[degree + 1];
        calculateCoefficients();
        convexHull= ConvexHull.calculateLines(points);
    }
    public BezierCurve(ArrayList<Point2D> pointsInOrder,double tStart,double tEnd) {
        this.points = pointsInOrder;
        degree = pointsInOrder.size() - 1;
        xPolynomialCoefficients = new double[degree + 1];
        yPolynomialCoefficients = new double[degree + 1];
        calculateCoefficients();
        convexHull= ConvexHull.calculateLines(points);
        startParameter=tStart;
        endParameter=tEnd;
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

    public Point2D getPointAt(double pointParameter) {
        if(pointParameter<=endParameter&&pointParameter>=startParameter) {
            double t0=pointParameter-startParameter/(endParameter-startParameter);
            double x = 0;
            double y = 0;
            for (int i = 0; i <= degree; i++) {
                x = x + xPolynomialCoefficients[i] * Math.pow(t0, i);
                y = y + yPolynomialCoefficients[i] * Math.pow(t0, i);
            }

            return new Point2D(x, y);
        }
        throw new WrongValueException(startParameter,endParameter,pointParameter);

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
        ArrayList<Point2D>solutionPoints=new ArrayList<>();
        for (double solution:solutions){
            solutionPoints.add(getPointAt(startParameter+(endParameter-startParameter)*solution));
        }
        return solutionPoints;
    }
    public ArrayList<Point2D> getIntersectionsWithLine(LineSegment lineSegment) {

        ArrayList<Point2D>solutionPoints=getIntersectionsWithLine(lineSegment.getPoint1(),lineSegment.getEquation().getDirection());
        solutionPoints.removeIf(solution -> !BindsCheck.isOnLine(solution, lineSegment));
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
    private static ArrayList<Double> getPolynomialSolutions(double[] coefficients) {
        PolynomialFunction function = new PolynomialFunction(coefficients);
        BrentSolver solver = new BrentSolver();
        ArrayList<Double> solutions = new ArrayList<>();
        int loop = 0;
        while (loop < 10) {
            try {
                double temp = solver.solve(100, function, 0.1 * loop, 0.1 + 0.1 * loop);
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

    public boolean areOnDifferentSides(Point2D point1, Point2D point2) {
        return BindsCheck.isBetweenPoints(this.getClosestIntersectionWithLine(point1, point2.subtract(point1)), point1, point2);
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

    public ArrayList<BezierCurve> getSubCurves(double brakeParameter){
        //calculates subcurves of curve broken in point t0
        ArrayList<BezierCurve> subCurves=new ArrayList<>();
        if(brakeParameter<=endParameter&&brakeParameter>=startParameter) {
            double t0=(brakeParameter-startParameter)/(endParameter-startParameter);
            Point2D[][] coefficients = getCasteljauTree(t0);

            ArrayList<Point2D> firstCurvePoints=new ArrayList<>();
            ArrayList<Point2D> secondCurvePoints=new ArrayList<>();
            for (int i=0;i<degree+1;i++){
                firstCurvePoints.add(coefficients[i][0]);
                secondCurvePoints.add(coefficients[i][coefficients[i].length-1]);
            }

            subCurves.add(new BezierCurve(firstCurvePoints,startParameter,brakeParameter));
            subCurves.add(new BezierCurve(secondCurvePoints,brakeParameter,endParameter));
            return subCurves;
        }else if(brakeParameter==startParameter||brakeParameter==endParameter){
            subCurves.add(this);

        }
        return subCurves;
        }
    public ArrayList<BezierCurve> getSubCurves(ArrayList<Double> brakeParameters){
        ArrayList<BezierCurve> subCurves=new ArrayList<>();
        subCurves.add(this);
        for (double parameter:brakeParameters){
            for (BezierCurve subCurve:subCurves){
                if(parameter>subCurve.startParameter&&parameter<subCurve.endParameter){
                    subCurves.addAll(subCurve.getSubCurves(parameter));
                    subCurves.remove(subCurve);
                }
            }
        }
        return subCurves;
    }


    private Point2D[][] getCasteljauTree(double t0) {
        Point2D[][] coefficients= new Point2D[degree+1][];
        for (int i = 0; i < degree+1; i++) {
            coefficients[i]=new Point2D[degree+1 -i];
            for (int j = 0; j < degree+1 - i; j++) {
                if (i == 0) {
                    coefficients[0][j] = points.get(j);
                } else {
                    coefficients[i][j] = coefficients[i-1][j].multiply(1 - t0).add(coefficients[i-1][j + 1].multiply(t0));
                }
            }
        }
        return coefficients;
    }

    public static ArrayList<Point2D> getIntersections(BezierCurve curve1,BezierCurve curve2){
        ArrayList<Point2D> intersections=new ArrayList<>();
        if(curve1.equals(curve2)){
           return intersections;
        }
        if(ConvexHull.hullsIntersects(curve1.convexHull,curve2.convexHull)){

             if(curve1.couldBeSimplified()&&curve2.couldBeSimplified()){
                LineSegment lineSegment1 =curve1.simplifyToLine();
                LineSegment lineSegment2 =curve2.simplifyToLine();
                if(BindsCheck.linesIntersect(lineSegment1, lineSegment2)){
                    intersections.add(lineSegment1.getEquation().intersection(lineSegment2.getEquation()));
                }
            }else if(curve1.endParameter-curve1.startParameter>curve2.endParameter-curve2.startParameter){
                ArrayList<BezierCurve> subCurves=curve1.getSubCurves(curve1.startParameter+(curve1.endParameter-curve1.startParameter)/2);
                for (BezierCurve subCurve:subCurves){
                    intersections.addAll(BezierCurve.getIntersections(subCurve,curve2));
                }
            }else {
                ArrayList<BezierCurve> subCurves=curve2.getSubCurves(curve2.startParameter+(curve2.endParameter-curve2.startParameter)/2);
                for (BezierCurve subCurve:subCurves){
                    intersections.addAll(BezierCurve.getIntersections(subCurve,curve1));
                }
            }
        }
        return intersections;
        }

        public boolean couldBeSimplified(){
        LinearEquation equation= new LinearEquation(points.getFirst(),points.getLast());
        for(int i=1;i<points.size()-1;i++){
            if(equation.distance(points.get(i))>=Properties.ACCURACY()/200){
                return false;
            }
        }
        return true;
        }
        public LineSegment simplifyToLine(){
        if(couldBeSimplified()){
            return new LineSegment(points.getFirst(),points.getLast());
        }
        return null;

        }
        public LinearEquation getTangentAt(double t0){
        Point2D[][] tree= getCasteljauTree(t0);
        return new LinearEquation(tree[tree.length-2][0],tree[tree.length-2][1]);
        }


    public ArrayList<Double> getParameterAtPoint(Point2D point) {
        if(point.distance(points.getFirst())<=Properties.ACCURACY())return new ArrayList<>(List.of(0.0));
        if(point.distance(points.getLast())<=Properties.ACCURACY())return new ArrayList<>(List.of(1.0));
        double[] xCoefficients =get_xPolynomialCoefficients();
        xCoefficients[0]=xCoefficients[0]-point.getX();
        ArrayList<Double> xParameters=getPolynomialSolutions(xCoefficients);
        double[] yCoefficients =get_yPolynomialCoefficients();
        yCoefficients[0]=yCoefficients[0]-point.getY();
        ArrayList<Double> yParameters=getPolynomialSolutions(yCoefficients);
        ArrayList<Double> solutions = new ArrayList<>();
        for (double xSolution:xParameters){
            for (double ySolution:yParameters){
                if(ySolution>=xSolution-Properties.ACCURACY()/100&&ySolution<=xSolution+Properties.ACCURACY()/100){
                    solutions.add( startParameter+((xSolution+ySolution)/2)*(endParameter-startParameter));
                }
            }
        }

        return solutions;



    }


}
