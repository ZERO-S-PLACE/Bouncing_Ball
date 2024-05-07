package org.zeros.bouncy_balls.Animation;

import java.io.Serializable;

public class AnimationProperties implements Serializable {
    public final int HEIGHT;
    public final int WIDTH;
    public final double GRAVITY;
    public final double FRAME_RATE;
    public final BordersType BOUNDARIES;
    public final int MAX_EVALUATIONS;

    public AnimationProperties(int HEIGHT, int WIDTH, double GRAVITY, double FRAME_RATE, BordersType BOUNDARIES, int MAX_EVALUATIONS) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.GRAVITY = GRAVITY;
        this.FRAME_RATE = FRAME_RATE;
        this.BOUNDARIES = BOUNDARIES;
        this.MAX_EVALUATIONS = MAX_EVALUATIONS;

    }

    public AnimationProperties(int HEIGHT, int WIDTH, BordersType BOUNDARIES) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.GRAVITY = 0;
        this.FRAME_RATE = 120;
        this.BOUNDARIES = BOUNDARIES;
        this.MAX_EVALUATIONS = 50;

    }

    public AnimationProperties(int HEIGHT, int WIDTH, double GRAVITY) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.GRAVITY = GRAVITY;
        this.FRAME_RATE = 120;
        this.BOUNDARIES = BordersType.BOUNCING;
        this.MAX_EVALUATIONS = 50;
    }

    public AnimationProperties(int HEIGHT, int WIDTH) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.GRAVITY = 4;
        this.FRAME_RATE = 120;
        this.BOUNDARIES = BordersType.BOUNCING;
        this.MAX_EVALUATIONS = 50;
    }

}
