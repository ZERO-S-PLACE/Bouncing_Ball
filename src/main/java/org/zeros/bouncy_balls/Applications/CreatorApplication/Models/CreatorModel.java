package org.zeros.bouncy_balls.Applications.CreatorApplication.Models;


import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.BottomPanelController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.ImageEditionPanelController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.TopPanelController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel.ActionChoicePanelController;

import org.zeros.bouncy_balls.Applications.CreatorApplication.Views.ControllersBaseCreator;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Views.CreatorViewFactory;
import org.zeros.bouncy_balls.Applications.GameApplication.Views.ControllersBase;

public class CreatorModel {

    private final CreatorViewFactory viewFactory;
    private static CreatorModel model;
    private final ControllersBaseCreator controllers;


    public ControllersBaseCreator controllers() {
        return controllers;
    }

    private CreatorModel() {
        this.controllers = new ControllersBaseCreator();
        this.viewFactory = new CreatorViewFactory();
    }
    public CreatorViewFactory getViewFactory() {
        return viewFactory;
    }

    public static synchronized CreatorModel getInstance() {
        if (model == null) {
            model = new CreatorModel();
        }
        return model;
    }

}
