package org.zeros.bouncy_balls.Applications.CreatorApplication.Views;

import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.BottomPanelController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.CreatorSettings.GeneralSettingsController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.CreatorSettings.PhysicsSettingsController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel.*;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LevelEditionController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.MainWindowController;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.TopPanelController;

public class ControllersBaseCreator {
    private MainWindowController mainWindowController;
    private LevelEditionController imageEditionPanelController;
    private ActionChoiceController actionChoiceController;
    private TopPanelController topPanelController;
    private BottomPanelController bottomPanelController;
    private ComplexAreaFlowController complexAreaActionChoiceController;
    private LeftPanelController currentLeftPanelController;
    private MovingObjectAddController movingObjectAddController;
    private PolyLineDrawingController polyLineDrawingController;
    private ShapeChoiceController shapeChoiceController;
    private GeneralSettingsController generalSettingsController;
    private PhysicsSettingsController physicsSettingsController;

    public MainWindowController getMainWindowController() {
        if (this.mainWindowController == null) {
            this.mainWindowController = new MainWindowController();
        }
        return mainWindowController;
    }


    public LevelEditionController getLevelEditionController() {
        if (this.imageEditionPanelController == null) {
            this.imageEditionPanelController = new LevelEditionController();
        }
        return imageEditionPanelController;
    }


    public TopPanelController getTopPanelController() {
        if (this.topPanelController == null) {
            this.topPanelController = new TopPanelController();
        }
        return topPanelController;
    }

    public BottomPanelController getBottomPanelController() {
        if (this.bottomPanelController == null) {
            this.bottomPanelController = new BottomPanelController();
        }
        return bottomPanelController;
    }

    public ActionChoiceController getActionChoiceController() {
        if (this.actionChoiceController == null) {
            this.actionChoiceController = new ActionChoiceController();
        }
        return actionChoiceController;
    }

    public ComplexAreaFlowController getComplexAreaActionChoiceController() {
        if (this.complexAreaActionChoiceController == null) {
            this.complexAreaActionChoiceController = new ComplexAreaFlowController();
        }
        return complexAreaActionChoiceController;
    }

    public LeftPanelController getCurrentLeftPanelController() {
        return currentLeftPanelController;
    }

    public void setCurrentLeftPanelController(LeftPanelController currentLeftPanelController) {
        this.currentLeftPanelController = currentLeftPanelController;
    }

    public MovingObjectAddController getMovingObjectAddController() {
        if (this.movingObjectAddController == null) {
            this.movingObjectAddController = new MovingObjectAddController();
        }
        return movingObjectAddController;
    }

    public PolyLineDrawingController getPolyLineDrawingController() {
        if (this.polyLineDrawingController == null) {
            this.polyLineDrawingController = new PolyLineDrawingController();
        }
        return polyLineDrawingController;
    }

    public ShapeChoiceController getShapeChoiceController() {
        if (this.shapeChoiceController == null) {
            this.shapeChoiceController = new ShapeChoiceController();
        }
        return shapeChoiceController;
    }

    public GeneralSettingsController getGeneralSettingsController() {
        if (this.generalSettingsController == null) {
            this.generalSettingsController = new GeneralSettingsController();
        }
        return generalSettingsController;
    }

    public PhysicsSettingsController getPhysicsSettingsController() {
        if (this.physicsSettingsController == null) {
            this.physicsSettingsController = new PhysicsSettingsController();
        }
        return physicsSettingsController;
    }


}
