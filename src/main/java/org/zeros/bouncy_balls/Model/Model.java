package org.zeros.bouncy_balls.Model;

import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Controllers.Animation.GamePanelController;
import org.zeros.bouncy_balls.Controllers.LevelCreator.LevelCreatorController;
import org.zeros.bouncy_balls.Views.ViewFactory;

import java.util.ArrayList;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final ArrayList<Animation> runningAnimations = new ArrayList<>();
    private GamePanelController gamePanelController;
    private LevelCreatorController levelCreatorController;

    private Model() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public GamePanelController getGamePanelController() {
        if (this.gamePanelController == null) {
            this.gamePanelController = new GamePanelController();
        }
        return gamePanelController;
    }

    public ArrayList<Animation> getRunningAnimations() {
        return runningAnimations;
    }

    public void addAnimation(Animation animation) {
        runningAnimations.add(animation);
    }

    public LevelCreatorController getLevelCreatorController() {
        if (this.levelCreatorController == null) {
            this.levelCreatorController = new LevelCreatorController();
        }
        return levelCreatorController;
    }


    public Animation getRunningAnimation(String animationName) {
        for (Animation animation : runningAnimations) {
            if (animation.getName().equals(animationName)) return animation;
        }
        return null;
    }
}
