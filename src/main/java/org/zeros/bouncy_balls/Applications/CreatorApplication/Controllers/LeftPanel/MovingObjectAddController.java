package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.Level.Enums.ComplexAreaInput;

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
            acceptButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().acceptArea());
            dismissButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().dismissAction());

            haveToEnterButton.setOnAction(e->setMovingObjectFunction());
            cannotEnterButton.setOnAction(e->setMovingObjectFunction());
        }

    private void setMovingObjectFunction() {

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
