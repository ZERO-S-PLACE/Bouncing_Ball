package org.zeros.bouncy_balls.Animation;

import java.io.Serializable;

public class AnimationProperties implements Serializable {
    private   AnimationType TYPE;
    private   int HEIGHT;
    private   int WIDTH;
    private   double GRAVITY;
    private   double FRAME_RATE;
    private   BordersType BOUNDARIES;
    private   int MAX_EVALUATIONS;
    private   double FRICTION;
    private double TIME;



    public AnimationProperties(int HEIGHT, int WIDTH) {
        this.TYPE=AnimationType.GAME;
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.GRAVITY = 0;
        this.FRAME_RATE = 120;
        this.BOUNDARIES = BordersType.BOUNCING;
        this.MAX_EVALUATIONS = 150;
        this.TIME =240;
        this.FRICTION=0;
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

    public double getFRAME_RATE() {
        return FRAME_RATE;
    }

    public void setFRAME_RATE(double FRAME_RATE) {
        this.FRAME_RATE = FRAME_RATE;
    }

    public BordersType getBOUNDARIES() {
        return BOUNDARIES;
    }

    public void setBOUNDARIES(BordersType BOUNDARIES) {
        this.BOUNDARIES = BOUNDARIES;
    }

    public int getMAX_EVALUATIONS() {
        return MAX_EVALUATIONS;
    }

    public void setMAX_EVALUATIONS(int MAX_EVALUATIONS) {
        this.MAX_EVALUATIONS = MAX_EVALUATIONS;
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
