package org.zeros.bouncy_balls.Controllers.P3_LevelSubtypeChoice;

import javafx.fxml.Initializable;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelSubtypeChoiceController implements Initializable {
    AnimationType type;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setAnimationType (AnimationType type){
        this.type=type;
    }
}
