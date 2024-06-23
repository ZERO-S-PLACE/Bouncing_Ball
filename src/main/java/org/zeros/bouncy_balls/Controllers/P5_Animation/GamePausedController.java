package org.zeros.bouncy_balls.Controllers.P5_Animation;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Controllers.P4_LevelSelection.LevelListCelFactory;
import org.zeros.bouncy_balls.Controllers.P4_LevelSelection.LevelListCellController;
import org.zeros.bouncy_balls.Controllers.P4_LevelSelection.LevelState;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.DisplayUtil.NodeAnimations;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Model;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GamePausedController implements Initializable {


    public BorderPane gamePausedPanel;
    public Circle centerCircle;
    public VBox buttonsContainer;
    public BorderPane pausedContainer;
    public Button runButton;
    public BorderPane endPanelContainer;
    public Text endMessageText;
    public HBox starsBox;
    public Label starLabel1;
    public Label starLabel2;
    public Label starLabel3;
    public Text scoreText;
    public HBox toolsContainer;
    public Button previousButton;
    public Button restartButton;
    public Button nextButton;
    public Button returnButton;
    private Level nextLevel;
    private Level previousLevel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCirclesFill();
        nextLevel = getLevel(1);
        previousLevel = getLevel(-1);
        getPreviousLevel();
        setRescaling();
        setEnterAnimation();
        setToolTips();
        setButtonFunctions();
        setConfigurationAtNewGame();
    }

    private void getPreviousLevel() {
    }

    private void setConfigurationAtNewGame() {
        endPanelContainer.setVisible(false);
        pausedContainer.setVisible(true);
        restartButton.setDisable(true);
        if(nextLevel==null){
            nextButton.setDisable(true);
        }
        if(previousLevel==null){
            previousButton.setDisable(true);
        }
    }

    private void setToolTips() {
        returnButton.setTooltip(new CustomTooltip("Return to level selection"));
        runButton.setTooltip(new CustomTooltip("Run game"));
        restartButton.setTooltip(new CustomTooltip("Reset and run new game"));
        nextButton.setTooltip(new CustomTooltip("Go to next level"));
        previousButton.setTooltip(new CustomTooltip("Go to previous level"));

    }

    private void setButtonFunctions() {
        returnButton.setOnAction(event -> transitionToLevelSelection());
        previousButton.setOnAction(event ->{ NodeAnimations.increaseBrightnessOnExit(previousButton);
            transitionToLevel(previousLevel);});
        nextButton.setOnAction(event ->{ NodeAnimations.increaseBrightnessOnExit(nextButton);
            transitionToLevel(nextLevel);});
        restartButton.setOnAction(event ->{
            NodeAnimations.increaseBrightnessOnExit(nextButton);
            transitionToLevel(getLevel(0));});
        runButton.setOnAction(event ->runGame());
        }

    private void runGame() {
    }

    private void transitionToLevelSelection() {
       Model.getInstance().controllers().getMainWindowController().loadBackgroundAnimation();
        NodeAnimations.increaseBrightnessOnExit(returnButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getLevelSelectionPanel(), 0.3);
    }
    private void transitionToLevel(Level level) {
        AnimationPane pane = Model.getInstance().getViewFactory().getNewAnimationPane(level);
        Model.getInstance().controllers().getMainWindowController().changeBottomLayer(pane.getAnimationPane(),0.3);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(
                Model.getInstance().getViewFactory().getGamePausedPanel(), 0.3);
    }

    private Level getLevel(int distance) {
        ListView<Level> listView = Model.getInstance().controllers().getLevelSelectionController().levelsList;
        for (int i = 0; i < Model.getInstance().controllers().getLevelSelectionController().levelsList.getItems().size(); i++) {
            LevelListCelFactory cell = (LevelListCelFactory) listView.lookup(".list-cell:nth-child(" + (i + 1) + ")");
            if (cell != null) {
                LevelListCellController controller = cell.getController();
                if (controller.getLevel().equals(Model.getInstance().getViewFactory().getCurrentAnimationPane().getLevel())) {
                    LevelListCelFactory cellNext = (LevelListCelFactory) listView.lookup(".list-cell:nth-child(" + (i + 1 + distance) + ")");
                    if (cellNext != null) {
                        LevelListCellController controllerNext = cellNext.getController();
                        if (!controllerNext.getState().equals(LevelState.DISABLED)) {
                            return controllerNext.getLevel();
                        }

                    }
                    return null;
                }
            }

        }
        return null;

    }


    private void setCirclesFill() {
        String imagePath = Objects.requireNonNull(getClass().getResource("/Icons/General/BackBallBlue.png")).toExternalForm();
        ImagePattern imagePattern = new ImagePattern(new javafx.scene.image.Image(imagePath));
        centerCircle.setFill(imagePattern);
    }


    private void setEnterAnimation() {
        restartButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(restartButton, 0.25));
        restartButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(restartButton));
        runButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(runButton, 0.25));
        runButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(runButton));
        previousButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(previousButton, 0.25));
        previousButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(previousButton));
        nextButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(nextButton, 0.25));
        nextButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(nextButton));
        returnButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(returnButton, 0.25));
        returnButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(returnButton));
    }

    private void setRescaling() {

        centerCircle.radiusProperty().bind(gamePausedPanel.heightProperty().multiply(0.33));
        buttonsContainer.spacingProperty().bind(gamePausedPanel.heightProperty().multiply(0.08));
        runButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34));
        runButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34));
        restartButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.5));
        restartButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.5));
        previousButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.3));
        previousButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.3));
        nextButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.3));
        nextButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.3));
        returnButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.2));
        returnButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.2));
        nextButton.setRotate(180);
    }
}
