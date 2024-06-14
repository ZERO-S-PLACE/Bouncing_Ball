package org.zeros.bouncy_balls.Controllers.P4_LevelSelection;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.Level.Level;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class LevelSelectionController implements Initializable {
    public AnchorPane buttonsContainer;
    public Button returnButton;
    public BorderPane levelChoicePanel;
    private TreeSet<Level> levelsInOrder =new TreeSet<>(Comparator.comparing(Level::getNAME));

    public ListView<Level> levelsList;


public void loadLevelsList(AnimationType type,String subtype){


}
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        levelsList.setCellFactory(param ->new LevelListCelFactory());

    }

}
