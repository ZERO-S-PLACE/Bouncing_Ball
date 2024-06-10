package org.zeros.bouncy_balls.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.zeros.bouncy_balls.Model.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    public BorderPane mainPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            mainPanel.setCenter(Model.getInstance().getViewFactory().getGameView());
        } catch (IOException e) {
            e.printStackTrace();
           showFilesDamagedLabel();
        }
    }

    private void showFilesDamagedLabel() {
        AnchorPane pane=new AnchorPane();
        mainPanel.setCenter(pane);
        pane.getChildren().add(new Label("ERROR 404 "));
        pane.getChildren().add(new Label("Files are damaged, please reinstall"));
    }
}
