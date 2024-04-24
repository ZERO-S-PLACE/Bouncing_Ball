package org.zeros.bouncy_balls.Calculations.Equations;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.Model.Model;

import java.util.ArrayList;

public class BezierCurve {
    private int degree;
    private PolynomialFunction polynomial;
    private ArrayList<Point2D> points;
    private double[] xPolynomialCoeffitients; //curve equation converted to form of polynomial of variable t0
    private double[] yPolynomialCoeffitients;

    public BezierCurve(ArrayList<Point2D> pointsInOrder){
        this.points=pointsInOrder;
        degree=pointsInOrder.size()-1;
        xPolynomialCoeffitients=new double[degree+1];
        yPolynomialCoeffitients=new double[degree+1];
        calculateCoefficients();

    }

    public double[] xPolynomialCoefficients() {
        return xPolynomialCoeffitients;
    }

    public double[] yPolynomialCoefficients() {
        return yPolynomialCoeffitients;
    }

    private void calculateCoefficients() {

        for(int i=0;i<=degree;i++){
            int mp= VectorMath.binomialCoefficient(degree,i);
            for (int j=i;j<=degree;j++){

                xPolynomialCoeffitients[j]=xPolynomialCoeffitients[j]+
                        mp*VectorMath.binomialCoefficient(degree-i,j-i)*points.get(i).getX()*Math.pow(-1,j-i);
                yPolynomialCoeffitients[j]=yPolynomialCoeffitients[j]+
                        mp*VectorMath.binomialCoefficient(degree-i,j-i)*points.get(i).getY()*Math.pow(-1,j-i);

            }
        }

    }
    public Point2D getPointAt(double t0){
        if(t0<=1&&t0>=0) {
           double  x=0;
           double y=0;
           for (int i=0;i<=degree;i++){
               x=x+xPolynomialCoeffitients[i]*Math.pow(t0,i);
               y=y+yPolynomialCoeffitients[i]*Math.pow(t0,i);

           }
            Circle circle =new Circle(x, y, 3);
            circle.setFill(Color.BLUE);
            Model.getInstance().getGamePanelController().gameBackground.getChildren().add(circle);
            return new Point2D(x,y);
        }
        return null;

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
