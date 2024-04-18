package org.zeros.bouncy_balls.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.zeros.bouncy_balls.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    public BorderPane mainPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPanel.setCenter(Model.getInstance().getViewFactory().getGameView());

    }
}
