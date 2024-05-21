package org.zeros.bouncy_balls.Calculations.Equations;

import static java.lang.Float.NaN;

public class QuadraticEquation extends Equation {

    private final double a;
    private final double b;
    private final double c;
    private final double x01;
    private final double x02;
    private final double delta;


    public QuadraticEquation(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;

        delta = b * b - 4 * a * c;

        if (delta >= 0) {
            x01 = (-b - Math.sqrt(delta)) / (2 * a);
            x02 = (-b + Math.sqrt(delta)) / (2 * a);
        } else {
            x01 = NaN;
            x02 = NaN;
        }

    }

    public double getValue(double x) {
        return Math.pow(a, 2) * x + b * x + c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getSolution1() {
        return x01;
    }

    public double getSolution2() {
        return x02;
    }


}
