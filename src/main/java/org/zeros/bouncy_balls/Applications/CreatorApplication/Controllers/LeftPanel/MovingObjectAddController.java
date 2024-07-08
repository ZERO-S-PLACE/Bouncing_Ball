package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import org.jetbrains.annotations.NotNull;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Model;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.Level.Enums.MovingObjectFunction;

import java.net.URL;
import java.util.ResourceBundle;

public class MovingObjectAddController extends LeftPanelController {
    public Button movingBallButton;
    public Button ballsArrayButton;

    public Button moveElementButton;
    public Button rotateButton;
    public Button acceptButton;
    public Button dismissButton;
    public RadioButton haveToEnterButton;
    public RadioButton cannotEnterButton;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            setButtonsFunctions();
            setTooltips();
            setStandardView();
        }


        private void setTooltips() {
            movingBallButton.setTooltip(new CustomTooltip("Add moving ball"));
            ballsArrayButton.setTooltip(new CustomTooltip("Add moving balls array"));
            moveElementButton.setTooltip(new CustomTooltip("Move"));
            rotateButton.setTooltip(new CustomTooltip("Rotate"));
            acceptButton.setTooltip(new CustomTooltip("Save"));
            dismissButton.setTooltip(new CustomTooltip("Dismiss"));
        }

        private void setButtonsFunctions() {
            movingBallButton.setOnAction(e-> CreatorModel.getInstance().getLevelCreator().addMovingBall());
            ballsArrayButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().addMovingBallsArray());
            moveElementButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().moveCurrentMovingObject());
            rotateButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().rotateCurrentMovingObject());
            acceptButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().acceptMovingObject());
            dismissButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().dismissAction());
            haveToEnterButton.selectedProperty().addListener(getHaveToEnterChangeListener());
            cannotEnterButton.selectedProperty().addListener(getCannotEnterChangeListener());
        }


    private ChangeListener<Boolean> getHaveToEnterChangeListener() {
        return (observable, oldValue, newValue) -> {
            if(!oldValue.equals(newValue)) {
                if (newValue.equals(true)) {
                    cannotEnterButton.selectedProperty().set(false);
                    CreatorModel.getInstance().getLevelCreator().changeMovingObjectFunction(MovingObjectFunction.HAVE_TO_ENTER_TARGET);
                }else if (!cannotEnterButton.selectedProperty().get()) {
                    CreatorModel.getInstance().getLevelCreator().changeMovingObjectFunction(MovingObjectFunction.NONE);
                }
            }
        };
    }
    private ChangeListener<Boolean> getCannotEnterChangeListener() {
        return (observable, oldValue, newValue) -> {
            if(!oldValue.equals(newValue)) {
                if (newValue.equals(true)) {
                    haveToEnterButton.selectedProperty().set(false);
                    CreatorModel.getInstance().getLevelCreator().changeMovingObjectFunction(MovingObjectFunction.CANNOT_ENTER_TARGET);
                }else if (!haveToEnterButton.selectedProperty().get()){
                    CreatorModel.getInstance().getLevelCreator().changeMovingObjectFunction(MovingObjectFunction.NONE);
                }
            }
        };
    }


    public void setViewOnTransform(){
            movingBallButton.setDisable(true);
            ballsArrayButton.setDisable(true);
            haveToEnterButton.setDisable(true);
            cannotEnterButton.setDisable(true);
            moveElementButton.setDisable(false);
            rotateButton.setDisable(false);
            acceptButton.setDisable(false);
        }
        public void setStandardView(){
            movingBallButton.setDisable(false);
            ballsArrayButton.setDisable(false);
            haveToEnterButton.setDisable(false);
            cannotEnterButton.setDisable(false);
            moveElementButton.setDisable(true);
            rotateButton.setDisable(true);
            acceptButton.setDisable(true);
        }


}
