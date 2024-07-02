package org.zeros.bouncy_balls.Animation.Animation;

import org.zeros.bouncy_balls.Animation.Borders.BordersType;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;

import java.io.Serializable;

public class AnimationProperties implements Serializable {
    private AnimationType TYPE;
    private int HEIGHT;
    private int WIDTH;
    private double GRAVITY;
    private BordersType BOUNDARIES;
    private double FRICTION;
    private double TIME;
    public AnimationProperties(int HEIGHT, int WIDTH) {
        this.TYPE = AnimationType.GAME;
        this.HEIGHT = (int) (HEIGHT* Properties.SIZE_FACTOR());
        this.WIDTH = (int) (WIDTH* Properties.SIZE_FACTOR());
        this.GRAVITY = 0;
        this.BOUNDARIES = BordersType.BOUNCING;
        this.TIME = 240;
        this.FRICTION = 0;
    }

    public AnimationType getTYPE() {
        return TYPE;
    }
    public void setTYPE(AnimationType TYPE) {
        this.TYPE = TYPE;
    }
    public int getHEIGHT() {
        return HEIGHT;
    }
    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }
    public int getWIDTH() {
        return WIDTH;
    }
    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }
    public double getGRAVITY() {
        return GRAVITY;
    }
    public void setGRAVITY(double GRAVITY) {
        this.GRAVITY = GRAVITY;
    }
    public BordersType getBOUNDARIES() {
        return BOUNDARIES;
    }
    public void setBOUNDARIES(BordersType BOUNDARIES) {
        this.BOUNDARIES = BOUNDARIES;
    }
    public double getFRICTION() {
        return FRICTION;
    }
    public void setFRICTION(double FRICTION) {
        this.FRICTION = FRICTION;
    }
    public double getTIME() {
        return TIME;
    }
    public void setTIME(double TIME) {
        this.TIME = TIME;
    }


}
