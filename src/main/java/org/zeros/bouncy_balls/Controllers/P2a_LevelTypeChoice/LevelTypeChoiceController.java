package org.zeros.bouncy_balls.Controllers.P2a_LevelTypeChoice;

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

public class LevelTypeChoiceController implements Initializable {
    public BorderPane gameTypeChoicePanel;
    public Circle centerCircle;
    public VBox buttonsContainer;
    public Button returnButton;
    public Button gameButton;
    public Button simulationButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCirclesFill();
        setRescaling();
        setEnterAnimation();
        setToolTips();
        setButtonFunctions();
    }

    private void setToolTips() {
        gameButton.setTooltip(new CustomTooltip("Play a game"));
        simulationButton.setTooltip(new CustomTooltip("Run a simulation"));
        returnButton.setTooltip(new CustomTooltip("Return to previous page"));
    }

    private void setButtonFunctions() {
        gameButton.setOnAction(event -> transitionToGameSelection());
        simulationButton.setOnAction(event -> transitionToSimulationSelection());
        returnButton.setOnAction(event -> transitionToWelcomePanel());
    }

    private void transitionToSimulationSelection() {
        NodeAnimations.increaseBrightnessOnExit(simulationButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getLevelSubtypePanel(AnimationType.SIMULATION), 0.3);
    }

    private void transitionToWelcomePanel() {
        NodeAnimations.increaseBrightnessOnExit(returnButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getWelcomePanel(), 0.3);
    }

    private void transitionToGameSelection() {
        NodeAnimations.increaseBrightnessOnExit(gameButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getLevelSubtypePanel(AnimationType.GAME), 0.3);
    }

    private void setCirclesFill() {
        String imagePath = Objects.requireNonNull(getClass().getResource("/Icons/General/BackBallBlue.png")).toExternalForm();
        ImagePattern imagePattern = new ImagePattern(new javafx.scene.image.Image(imagePath));
        centerCircle.setFill(imagePattern);
    }


    private void setEnterAnimation() {
        gameButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(gameButton,0.25));
        gameButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(gameButton));
        simulationButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(simulationButton,0.25));
        simulationButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(simulationButton));
        returnButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(returnButton,0.25));
        returnButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(returnButton));
    }
    private void setRescaling() {
        centerCircle.radiusProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.45));
        buttonsContainer.spacingProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.08));
        gameButton.prefHeightProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.34));
        gameButton.prefWidthProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.34));
        simulationButton.prefHeightProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.34 * 0.5));
        simulationButton.prefWidthProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.34 * 0.5));
        returnButton.prefWidthProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.34 * 0.2));
        returnButton.prefHeightProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.34 * 0.2));
    }

}
