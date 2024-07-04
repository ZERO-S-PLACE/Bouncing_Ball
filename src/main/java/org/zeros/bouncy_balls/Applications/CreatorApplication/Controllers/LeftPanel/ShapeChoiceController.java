package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel;

import javafx.scene.control.Button;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.Level.Enums.ComplexAreaInput;

import java.net.URL;
import java.util.ResourceBundle;

public class ShapeChoiceController extends LeftPanelController {
    public Button rectangleButton;
    public Button ovalButton;
    public Button polylineButton;
    public Button complexAreaButton;
    public Button moveElementButton;
    public Button rotateButton;
    public Button acceptButton;
    public Button dismissButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonsFunctions();
        setTooltips();

    }
    private void setTooltips() {
        rectangleButton.setTooltip(new CustomTooltip("Draw rectangle"));
        ovalButton.setTooltip(new CustomTooltip("Draw oval"));
        polylineButton.setTooltip(new CustomTooltip("Draw poly line"));
        complexAreaButton.setTooltip(new CustomTooltip("Draw complex area(inner areas will be skipped)"));
        moveElementButton.setTooltip(new CustomTooltip("Move"));
        rotateButton.setTooltip(new CustomTooltip("Rotate"));
        acceptButton.setTooltip(new CustomTooltip("Save"));
        dismissButton.setTooltip(new CustomTooltip("Dismiss"));
    }

    private void setButtonsFunctions() {
        rectangleButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().drawRectangle());
        ovalButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().drawOval());
        polylineButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().initializePolyLineDrawing());
        complexAreaButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().initializeComplexAreaInput(ComplexAreaInput.OBSTACLE));
        moveElementButton.setTooltip(new CustomTooltip("Move"));
        rotateButton.setTooltip(new CustomTooltip("Rotate"));
        acceptButton.setTooltip(new CustomTooltip("Save"));
        dismissButton.setOnAction(e->CreatorModel.getInstance().getLevelCreator().dismissAction());
    }

    public void setComplexView() {
        complexAreaButton.setVisible(true);
    }

    public void setSimpleView() {
        complexAreaButton.setVisible(false);
    }
}
