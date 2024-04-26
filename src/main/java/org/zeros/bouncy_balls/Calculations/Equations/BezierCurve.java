package org.zeros.bouncy_balls.Calculations.Equations;

import javafx.geometry.Point2D;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.zeros.bouncy_balls.Calculations.BindsCheck;
import org.zeros.bouncy_balls.Calculations.VectorMath;

import java.util.ArrayList;
import java.util.Arrays;

public class BezierCurve {
    private final int degree;
    private final ArrayList<Point2D> points;
    private final double[] xPolynomialCoefficients; //curve equation converted to form of polynomial of variable t0
    private final double[] yPolynomialCoefficients;

    public BezierCurve(ArrayList<Point2D> pointsInOrder) {
        this.points = pointsInOrder;
        degree = pointsInOrder.size() - 1;
        xPolynomialCoefficients = new double[degree + 1];
        yPolynomialCoefficients = new double[degree + 1];
        calculateCoefficients();
    }

    public double[] get_xPolynomialCoefficients() {
        return Arrays.copyOf(xPolynomialCoefficients, xPolynomialCoefficients.length);
    }

    public double[] get_yPolynomialCoefficients() {
        return Arrays.copyOf(yPolynomialCoefficients, yPolynomialCoefficients.length);
    }

    private void calculateCoefficients() {
        for (int i = 0; i <= degree; i++) {
            int mp = VectorMath.binomialCoefficient(degree, i);
            for (int j = i; j <= degree; j++) {

                xPolynomialCoefficients[j] = xPolynomialCoefficients[j] +
                        mp * VectorMath.binomialCoefficient(degree - i, j - i) * points.get(i).getX() * Math.pow(-1, j - i);
                yPolynomialCoefficients[j] = yPolynomialCoefficients[j] +
                        mp * VectorMath.binomialCoefficient(degree - i, j - i) * points.get(i).getY() * Math.pow(-1, j - i);

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
            return new Point2D(x,y);
        }
        return null;

    }

    public Point2D getIntersectionWithLine(Point2D center,Point2D direction) {
       /* Calculating intersection of curve with line
        Xb-Yb*(Vx/Vy)-Xo+Yo*(Vx/Vy)=0
        Yb*m-Xb +i=0
        m=(Vx/Vy)
        c=-Xo+Yo*Vx/Vy
        where Yb and Xb are bezier polynomials*/

    double[] coefficients = this.get_xPolynomialCoefficients();
        if(direction.getY()!=0) {
        double[] yB = this.get_yPolynomialCoefficients();
        double m = direction.getX() / direction.getY();
        double c = -center.getX() + m * center.getY();
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = coefficients[i] - yB[i] * m;
        }
        coefficients[0] = coefficients[0] + c;
        } else {
        coefficients = this.get_yPolynomialCoefficients();
        coefficients[0]=coefficients[0]-center.getY();
    }


        PolynomialFunction function = new PolynomialFunction(coefficients);
        BrentSolver solver = new BrentSolver(0.01);
        ArrayList<Double> solutions = new ArrayList<>();
        int loop = 1;
        while (loop < 5) {
            try {
                double temp = solver.solve(15, function, 0, 1, 0.2 * loop+0.1);
                if(solutions.isEmpty()){
                    solutions.add(temp);
                }else if(Math.abs(solutions.getLast()*100-temp*100)>2){
                    solutions.add(temp);
                }
            } catch (Exception ignored) {
            }
            loop++;
        }

        if (solutions.isEmpty()) {
            return null;
        }


        Point2D closestPoint = null;
        double minDistance = Double.MAX_VALUE;
        if(solutions.size()>1){
            System.out.println(solutions);
        }
        for (Double solution : solutions) {
            Point2D temp = this.getPointAt(solution);

            if (temp != null) {
                double distance = temp.distance(center);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPoint = temp;
                }
            }
        }

        return closestPoint;
}
public boolean areOnDifferentSides(Point2D point1,Point2D point2){
    return BindsCheck.isBetweenPoints(this.getIntersectionWithLine(point1, point2.subtract(point1)), point1, point2);
}





}
/*public Point2D getPointAt(double t0){
        if(t0<=1&&t0>=0) {
            int n = points.size() - 1;
            Point2D[] coefficients = new Point2D[n + 1];

            for (int i = 0; i <= n; i++) {
                for (int j = 0; j <= n - i; j++) {
                    if (i == 0) {
                        coefficients[j] = points.get(j);
                    } else {
                        coefficients[j] = coefficients[j].multiply(1 - t0).add(coefficients[j + 1].multiply(t0));
                    }
                }
            }
            Circle circle =new Circle(coefficients[0].getX(), coefficients[0].getY(), 3);
            circle.setFill(Color.BLUE);
            Model.getInstance().getGamePanelController().gameBackground.getChildren().add(circle);
            return coefficients[0];
        }
        return null;

        }*/
