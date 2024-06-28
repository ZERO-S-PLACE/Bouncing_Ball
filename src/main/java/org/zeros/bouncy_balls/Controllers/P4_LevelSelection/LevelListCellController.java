package org.zeros.bouncy_balls.Controllers.P4_LevelSelection;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;
import org.zeros.bouncy_balls.DisplayUtil.NodeAnimations;
import org.zeros.bouncy_balls.Level.GameScore;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Model;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LevelListCellController implements Initializable {
    private final Level level;
    public BorderPane cellContainer;
    public Button stateButton;
    public Text nameText;
    public Label starLabel1;
    public Label starLabel2;
    public Label starLabel3;
    private boolean clicked;
    private LevelState state;
    private final EventHandler<MouseEvent> mouseClickedOutside = this::mouseClickedOutside;
    private final EventHandler<MouseEvent> mouseClickedInside = this::mouseClickedInside;
    private GameScore score;
    public LevelListCellController(Level level) {
        this.level = level;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureIcons();
        nameText.setText(level.getNAME() + ". ");
        Model.getInstance().controllers().getLevelSelectionController().levelsList.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedOutside);
        cellContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedInside);
    }

    private void configureIcons() {
        if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
            String subtype = Model.getInstance().controllers().getLevelSelectionController().getSubtype();
            score = GameScore.load(subtype, level.getNAME());
            if (score != null) {
                if (score.scoreValue() > level.getOneStarBound()) {
                    state = LevelState.COMPLETED;
                    configureAsCompleted();
                } else {
                    state = LevelState.ENABLED;
                    configureAsEnabled();
                }
            } else {
                if (subtype.equals("User") || level.getNAME().equals("1")) {
                    state = LevelState.ENABLED;
                    configureAsEnabled();
                } else {
                    GameScore gameScorePrevious = GameScore.load(subtype, String.valueOf(Integer.parseInt(level.getNAME()) - 1));
                    if (gameScorePrevious != null) {
                        if (gameScorePrevious.scoreValue() > level.getOneStarBound()) {
                            state = LevelState.ENABLED;
                            configureAsEnabled();
                        } else {
                            state = LevelState.DISABLED;
                            configureAsDisabled();
                        }
                    } else {
                        state = LevelState.DISABLED;
                        configureAsDisabled();
                    }
                }
            }
        } else {
            state = LevelState.ENABLED;
            configureAsSimulation();

        }
    }


    public void setCellBackground(boolean enabled) {
        String name;
        if (enabled) {
            name = "";
        } else {
            name = "Disabled";
        }
        String imagePath = Objects.requireNonNull(getClass().getResource("/Icons/P4_LevelSelection/LevelBench" + name + ".png")).toExternalForm();
        cellContainer.setStyle("-fx-background-image: url('" + imagePath + "');");
    }

    private void configureAsDisabled() {
        setCellBackground(false);
        BackgroundImages.setCircleBackground(stateButton, "Gray");
        BackgroundImages.setStarBackground(starLabel1, "Gray");
        BackgroundImages.setStarBackground(starLabel2, "Gray");
        BackgroundImages.setStarBackground(starLabel3, "Gray");
        nameText.setFill(new Color((double) 87 / 255, (double) 87 / 255, (double) 87 / 255, 0.93));
    }

    private void configureAsEnabled() {
        setCellBackground(true);
        BackgroundImages.setCircleBackground(stateButton, "Blue");
        BackgroundImages.setStarBackground(starLabel1, "Blue");
        BackgroundImages.setStarBackground(starLabel2, "Blue");
        BackgroundImages.setStarBackground(starLabel3, "Blue");
        nameText.setFill(new Color(0.98, 0.98, 0.98, 0.93));
    }

    private void configureAsCompleted() {
        setCellBackground(true);
        BackgroundImages.setCircleBackground(stateButton, "Blue");
        BackgroundImages.setStarBackground(starLabel1, "Yellow");

        if (score.scoreValue() >= level.getThreeStarBound()) {
            BackgroundImages.setStarBackground(starLabel2, "Yellow");
            BackgroundImages.setStarBackground(starLabel3, "Yellow");
        } else {
            if (score.scoreValue() >= level.getTwoStarBound()) {
                BackgroundImages.setStarBackground(starLabel2, "Yellow");
                BackgroundImages.setStarBackground(starLabel3, "Blue");
            } else {
                BackgroundImages.setStarBackground(starLabel2, "Blue");
                BackgroundImages.setStarBackground(starLabel3, "Blue");
            }
        }
        nameText.setFill(new Color(0.98, 0.98, 0.98, 0.93));
    }

    private void configureAsSimulation() {
        setCellBackground(true);
        BackgroundImages.setCircleBackground(stateButton, "Blue");
        starLabel1.setVisible(false);
        starLabel2.setVisible(false);
        starLabel3.setVisible(false);
        nameText.setFill(new Color(0.98, 0.98, 0.98, 0.93));
    }

    private void mouseClickedOutside(MouseEvent mouseEvent) {
        NodeAnimations.resetBrightness(cellContainer);
        if (state.equals(LevelState.DISABLED)) {
            BackgroundImages.setCircleBackground(stateButton, "Gray");
        } else {
            BackgroundImages.setCircleBackground(stateButton, "Blue");
        }

        clicked = false;
    }

    private void mouseClickedInside(MouseEvent mouseEvent) {
        if (!clicked) {
            if (state.equals(LevelState.DISABLED)) {
                BackgroundImages.setCircleBackground(stateButton, "Red");
                NodeAnimations.increaseBrightness(cellContainer, 0.1);
            } else {
                BackgroundImages.setCircleBackground(stateButton, "Green");
                NodeAnimations.increaseBrightness(cellContainer, 0.3);
                clicked = true;
            }
        } else {
            NodeAnimations.increaseBrightnessOnExit(cellContainer);
            clicked = false;
            transitionToGame();
        }
        mouseEvent.consume();
    }

    private void transitionToGame() {
        Model.getInstance().getViewFactory().getBackgroundAnimation().getAnimation().pause();
        AnimationPane pane = Model.getInstance().getViewFactory().getNewAnimationPane(level);
        Model.getInstance().controllers().getMainWindowController().changeBottomLayer(pane.getAnimationPane(), 0.6);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getGamePausedPanel(), 0.1);
    }

    public LevelState getState() {
        return state;
    }
    public Level getLevel() {
        return level;
    }
}
