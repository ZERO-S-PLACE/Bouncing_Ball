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
        setRescaling();
        setEnterAnimation();
        setToolTips();
        setButtonFunctions();
        setConfigurationAtNewGame();
    }


    public void setConfigurationAtNewGame() {
        nextLevel = getLevel(1,false);
        previousLevel = getLevel(-1,false);
        endPanelContainer.setVisible(false);
        pausedContainer.setVisible(true);
        restartButton.setDisable(true);
        nextButton.setDisable(nextLevel == null);
        previousButton.setDisable(previousLevel == null);
    }
    public void setConfigurationAtPaused() {
        endPanelContainer.setVisible(false);
        pausedContainer.setVisible(true);
        restartButton.setDisable(false);
        nextButton.setDisable(nextLevel == null);
        previousButton.setDisable(previousLevel == null);
    }

    private void setToolTips() {
        returnButton.setTooltip(new CustomTooltip("Return to level selection"));
        runButton.setTooltip(new CustomTooltip("""
                Run game - click left mouse button to add a ball
                , and right button to add obstacle
                if there are any available"""));
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
            NodeAnimations.increaseBrightnessOnExit(returnButton);
            transitionToLevel(getLevel(0,true));});
        runButton.setOnAction(event ->runGame());
        }

    private void runGame() {
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getCountDownPanel(), 0.3);
        Model.getInstance().controllers().getGameCountDownController().countDownAndRun(true);
    }

    private void transitionToLevelSelection() {
        Model.getInstance().getViewFactory().getBackgroundAnimation().getAnimation().resume();
       Model.getInstance().controllers().getMainWindowController().loadBackgroundAnimation();
        NodeAnimations.increaseBrightnessOnExit(returnButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getLevelSelectionPanel(), 0.3);
    }
    private void transitionToLevel(Level level) {
        AnimationPane pane = Model.getInstance().getViewFactory().getNewAnimationPane(level);
        Model.getInstance().controllers().getMainWindowController().changeBottomLayer(pane.getAnimationPane(),0.3);
        setConfigurationAtNewGame();
    }

    private Level getLevel(int distance,boolean reset) {
        ListView<Level> listView = Model.getInstance().controllers().getLevelSelectionController().levelsList;
        int indexCurrent=listView.getItems().indexOf(Model.getInstance().getViewFactory().getCurrentAnimationPane().getAnimation().getLevel());
        if(indexCurrent+distance>=0&&indexCurrent+distance<listView.getItems().size()){
            if(reset)Model.getInstance().controllers().getLevelSelectionController().reloadLevelsList();
            return listView.getItems().get(indexCurrent+distance);
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

        centerCircle.radiusProperty().bind(gamePausedPanel.heightProperty().multiply(0.45));
        buttonsContainer.spacingProperty().bind(gamePausedPanel.heightProperty().multiply(0.08));
        runButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34));
        runButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34));
        restartButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.5));
        restartButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.5));
        previousButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.3));
        previousButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.3));
        nextButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.3));
        nextButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.3));
        returnButton.prefWidthProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.15));
        returnButton.prefHeightProperty().bind(gamePausedPanel.heightProperty().multiply(0.34 * 0.15));
        nextButton.setRotate(180);
    }


}
