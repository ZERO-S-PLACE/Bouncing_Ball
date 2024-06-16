package org.zeros.bouncy_balls.Controllers.P4_LevelSelection;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.zeros.bouncy_balls.DisplayUtil.NodeAnimations;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelListCellController implements Initializable {


    public BorderPane cellContainer;
    private final EventHandler<MouseEvent> mouseClickedOutside = this::mouseClickedOutside;
    private final EventHandler<MouseEvent> mouseClickedInside = this::mouseClickedInside;
    public Button stateButton;
    public Text nameText;
    public Label starLabel1;
    public Label starLabel2;
    public Label starLabel3;
    private final Level level;
    private boolean clicked;


    public LevelListCellController(Level level) {
        this.level = level;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String subtype =Model.getInstance().controllers().getLevelSelectionController().getSubtype();
        nameText.setText(level.getNAME());
        Model.getInstance().controllers().getLevelSelectionController().levelsList.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedOutside);
        cellContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedInside);
    }
    private void mouseClickedOutside(MouseEvent mouseEvent) {
        NodeAnimations.resetBrightness(cellContainer);
        clicked=false;
    }
    private void mouseClickedInside(MouseEvent mouseEvent) {
        if(!clicked){
            NodeAnimations.increaseBrightness(cellContainer,0.3);
            clicked=true;
        }else {
            NodeAnimations.increaseBrightnessOnExit(cellContainer);
            clicked=false;
            transitionToGame();
        }
        mouseEvent.consume();
    }

    private void transitionToGame() {
    }
}
