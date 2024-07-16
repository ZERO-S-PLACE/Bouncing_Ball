package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel;

import javafx.scene.control.Button;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.Level.Enums.PolyLineSegmentActions;

import java.net.URL;
import java.util.ResourceBundle;

public class PolyLineDrawingController extends LeftPanelController {
    public Button straightLineButton;
    public Button cubicCurveButton;
    public Button quadCurveButton;
    public Button removeLastButton;
    public Button acceptButton;
    public Button dismissButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonsFunctions();
        setTooltips();
    }

    private void setTooltips() {
        straightLineButton.setTooltip(new CustomTooltip("Add straight segment"));
        quadCurveButton.setTooltip(new CustomTooltip("Add quad curve"));
        cubicCurveButton.setTooltip(new CustomTooltip("Add cubic curve"));
        removeLastButton.setTooltip(new CustomTooltip("Remove last segment"));
        dismissButton.setTooltip(new CustomTooltip("Dismiss"));
        acceptButton.setTooltip(new CustomTooltip("Save"));
    }

    private void setButtonsFunctions() {
        straightLineButton.setOnAction(e -> CreatorModel.getInstance().getLevelCreator().changeSegmentAction(PolyLineSegmentActions.LINE));
        quadCurveButton.setOnAction(e -> CreatorModel.getInstance().getLevelCreator().changeSegmentAction(PolyLineSegmentActions.QUAD_CURVE));
        cubicCurveButton.setOnAction(e -> CreatorModel.getInstance().getLevelCreator().changeSegmentAction(PolyLineSegmentActions.CUBIC_CURVE));
        removeLastButton.setOnAction(e -> CreatorModel.getInstance().getLevelCreator().changeSegmentAction(PolyLineSegmentActions.REMOVE_LAST));
        acceptButton.setOnAction(e -> CreatorModel.getInstance().getLevelCreator().changeSegmentAction(PolyLineSegmentActions.NONE));
        dismissButton.setOnAction(e -> CreatorModel.getInstance().getLevelCreator().dismissAction());
    }
}
