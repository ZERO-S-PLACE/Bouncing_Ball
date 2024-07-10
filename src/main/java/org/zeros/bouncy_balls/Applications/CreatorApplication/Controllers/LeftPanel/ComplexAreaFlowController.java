package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel;

import javafx.scene.control.Button;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.Level.Enums.ComplexAreaAction;
import org.zeros.bouncy_balls.Level.Enums.PolyLineSegmentActions;

import java.net.URL;
import java.util.ResourceBundle;

public class ComplexAreaFlowController extends LeftPanelController {
    public Button sumButton;
    public Button intersectionButton;
    public Button subtractFromComplexButton;
    public Button subtractComplexButton;
    public Button acceptButton;
    public Button dismissButton;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            setButtonsFunctions();
            setTooltips();
        }
        private void setTooltips() {
            sumButton.setTooltip(new CustomTooltip("Add area to current"));
            intersectionButton.setTooltip(new CustomTooltip("Intersection with current area"));
            subtractFromComplexButton.setTooltip(new CustomTooltip("Subtract area from current"));
            subtractComplexButton.setTooltip(new CustomTooltip("Subtract current area from given"));
            dismissButton.setTooltip(new CustomTooltip("Dismiss"));
            acceptButton.setTooltip(new CustomTooltip("Save"));
        }

        private void setButtonsFunctions() {
            sumButton.setOnAction(
                    e-> CreatorModel.getInstance().getLevelCreator().changeComplexAreaAction(ComplexAreaAction.SUM));
            intersectionButton.setOnAction(
                    e-> CreatorModel.getInstance().getLevelCreator().changeComplexAreaAction(ComplexAreaAction.INTERSECTION));
            subtractFromComplexButton.setOnAction(
                    e-> CreatorModel.getInstance().getLevelCreator().changeComplexAreaAction(ComplexAreaAction.SUBTRACTION_A_B));
            subtractComplexButton.setOnAction(
                    e-> CreatorModel.getInstance().getLevelCreator().changeComplexAreaAction(ComplexAreaAction.SUBTRACTION_B_A));
            acceptButton.setOnAction(
                    e-> CreatorModel.getInstance().getLevelCreator().changeComplexAreaAction(ComplexAreaAction.NONE));
            dismissButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().dismissAction());
        }


}
