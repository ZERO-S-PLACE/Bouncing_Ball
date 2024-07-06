package org.zeros.bouncy_balls.Level.Previews;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

public class MovingBallPreview extends Preview{
    private final Point2D center;
    public MovingBallPreview(Point2D center) {
        this.center=rescaleToLayout(center);
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void remove() {

    }

    @Override
    public Shape getShape() {
        return null;
    }
}
