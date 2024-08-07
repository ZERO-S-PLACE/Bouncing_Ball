package org.zeros.bouncy_balls.Applications.GameApplication.Model;

import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Applications.GameApplication.Views.ControllersBase;
import org.zeros.bouncy_balls.Applications.GameApplication.Views.ViewFactory;

import java.util.ArrayList;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final ControllersBase controllers;
    private final ArrayList<Animation> runningAnimations = new ArrayList<>();


    private Model() {
        this.viewFactory = new ViewFactory();
        this.controllers = new ControllersBase();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ControllersBase controllers() {
        return controllers;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public ArrayList<Animation> getRunningAnimations() {
        return runningAnimations;
    }

    public void addAnimation(Animation animation) {
        runningAnimations.add(animation);
    }

    public Animation getRunningAnimation(String animationName) {
        for (Animation animation : runningAnimations) {
            if (animation.getAnimationIdentifier().equals(animationName)) return animation;
        }
        return null;
    }

}
