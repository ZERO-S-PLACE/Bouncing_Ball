package org.zeros.bouncy_balls.Applications.CreatorApplication.Models;


import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel.ShapeChoiceController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Views.ControllersBaseCreator;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Views.CreatorViewFactory;
import org.zeros.bouncy_balls.Level.LevelCreator;

public class CreatorModel {

    private static CreatorModel model;
    private final CreatorViewFactory viewFactory;
    private final ControllersBaseCreator controllers;
    private LevelCreator levelCreator;


    private CreatorModel() {
        this.controllers = new ControllersBaseCreator();
        this.viewFactory = new CreatorViewFactory();
    }

    public static synchronized CreatorModel getInstance() {
        if (model == null) {
            model = new CreatorModel();
        }
        return model;
    }

    public ControllersBaseCreator controllers() {
        return controllers;
    }

    public CreatorViewFactory getViewFactory() {
        return viewFactory;
    }
    public LevelCreator getLevelCreator() {
        if(this.levelCreator ==null){
            this.levelCreator = new LevelCreator();
        }
        return levelCreator;
    }
    public LevelCreator getNewLevelCreator() {
       levelCreator=null;
       return getLevelCreator();
    }

}
