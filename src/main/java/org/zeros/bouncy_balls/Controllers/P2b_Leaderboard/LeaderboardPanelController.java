package org.zeros.bouncy_balls.Controllers.P2b_Leaderboard;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.DisplayUtil.NodeAnimations;
import org.zeros.bouncy_balls.Model.Model;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LeaderboardPanelController implements Initializable {
    public BorderPane leaderboardPanel;
    public Button returnButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRescaling();
        setEnterAnimation();
        setToolTips();
        setButtonFunctions();
    }

    private void setToolTips() {
        returnButton.setTooltip(new CustomTooltip("Return to previous page"));
    }

    private void setButtonFunctions() {
        returnButton.setOnAction(event -> transitionToWelcomePanel());
    }
    private void transitionToWelcomePanel() {
        NodeAnimations.increaseBrightnessOnExit(returnButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getWelcomePanel(), 0.3);
    }

    private void setEnterAnimation() {
        returnButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(returnButton,0.25));
        returnButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(returnButton));
    }
    private void setRescaling() {
        returnButton.prefWidthProperty().bind(leaderboardPanel.heightProperty().multiply(0.34 * 0.2));
        returnButton.prefHeightProperty().bind(leaderboardPanel.heightProperty().multiply(0.34 * 0.2));
    }

}
