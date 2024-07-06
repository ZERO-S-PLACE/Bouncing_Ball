package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelEditionController implements Initializable {

    public AnchorPane layersContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addTrackingPane() {
        AnchorPane.setLeftAnchor(CreatorModel.getInstance().getViewFactory().getTrackingPane(), 0.0);
        AnchorPane.setRightAnchor(CreatorModel.getInstance().getViewFactory().getTrackingPane(), 0.0);
        AnchorPane.setTopAnchor(CreatorModel.getInstance().getViewFactory().getTrackingPane(), 0.0);
        AnchorPane.setBottomAnchor(CreatorModel.getInstance().getViewFactory().getTrackingPane(), 0.0);
        Platform.runLater(() -> {
            layersContainer.getChildren().add(CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getAnimationPane());
            layersContainer.getChildren().add(CreatorModel.getInstance().getViewFactory().getTrackingPane());
            CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1);
        });
    }


}




