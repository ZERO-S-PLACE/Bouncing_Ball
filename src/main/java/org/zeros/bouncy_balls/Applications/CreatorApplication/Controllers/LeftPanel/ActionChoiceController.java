package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel;

import javafx.scene.control.Button;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.Level.Enums.AreaPurpose;
import org.zeros.bouncy_balls.Level.Enums.ComplexAreaInput;
import org.zeros.bouncy_balls.Level.Enums.EditAction;
import org.zeros.bouncy_balls.Level.Enums.MovingObjectPurpose;

import java.net.URL;
import java.util.ResourceBundle;

public class ActionChoiceController extends LeftPanelController {

    public Button movingObjectButton;
    public Button obstacleButton;
    public Button targetAreaButton;
    public Button movingObjectToAddButton;
    public Button obstacleToAddButton;
    public Button inputAreaButton;
    public Button moveButton;
    public Button rotateButton;
    public Button deleteButton;
    public Button acceptButton;
    public Button dismissButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonsFunctions();
        setTooltips();
        setOnActionPick();
    }

    public void setOnActionPick() {
        acceptButton.setVisible(false);
        dismissButton.setVisible(false);
        movingObjectButton.setDisable(false);
        obstacleButton.setDisable(false);
        targetAreaButton.setDisable(false);
        movingObjectToAddButton.setDisable(false);
        obstacleToAddButton.setDisable(false);
        inputAreaButton.setDisable(false);
        moveButton.setDisable(false);
        rotateButton.setDisable(false);
        deleteButton.setDisable(false);
        acceptButton.setDisable(true);
        dismissButton.setDisable(true);
    }
    public void setOnTransform() {
        acceptButton.setVisible(true);
        dismissButton.setVisible(true);
        movingObjectButton.setDisable(true);
        obstacleButton.setDisable(true);
        targetAreaButton.setDisable(true);
        movingObjectToAddButton.setDisable(true);
        obstacleToAddButton.setDisable(true);
        inputAreaButton.setDisable(true);
        moveButton.setDisable(true);
        rotateButton.setDisable(true);
        deleteButton.setDisable(true);
        acceptButton.setDisable(false);
        dismissButton.setDisable(false);
    }

    private void setTooltips() {
        movingObjectButton.setTooltip(new CustomTooltip("Add moving object"));
        obstacleButton.setTooltip(new CustomTooltip("Add obstacle"));
        targetAreaButton.setTooltip(new CustomTooltip("Add target area for moving objects"));
        movingObjectToAddButton.setTooltip(new CustomTooltip("Add moving object to add in runtime"));
        obstacleToAddButton.setTooltip(new CustomTooltip("Add obstacle to add in runtime"));
        inputAreaButton.setTooltip(new CustomTooltip("Add input area for moving objects"));
        moveButton.setTooltip(new CustomTooltip("Click and move elements "));
        rotateButton.setTooltip(new CustomTooltip("Click and rotate elements "));
        deleteButton.setTooltip(new CustomTooltip("Click and delete elements "));
        acceptButton.setTooltip(new CustomTooltip("Accept"));
        dismissButton.setTooltip(new CustomTooltip("Dismiss"));
    }

    private void setButtonsFunctions() {
        movingObjectButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().initializeMovingObjectsInput(MovingObjectPurpose.MOVING_OBJECT));
        obstacleButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().initializeObstacleInput(AreaPurpose.OBSTACLE));
        targetAreaButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().initializeComplexAreaInput(ComplexAreaInput.TARGET));
        movingObjectToAddButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().initializeMovingObjectsInput(MovingObjectPurpose.MOVING_OBJECT_TO_ADD));
        obstacleToAddButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().initializeObstacleInput(AreaPurpose.OBSTACLE_TO_ADD));;
        inputAreaButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().initializeComplexAreaInput(ComplexAreaInput.INPUT));

        moveButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().setEditAction(EditAction.MOVE));
        rotateButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().setEditAction(EditAction.ROTATE));
        deleteButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().setEditAction(EditAction.DELETE));
        acceptButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().setEditAction(EditAction.NONE));
        dismissButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().dismissAction());
    }


}
