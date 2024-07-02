package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ImageEditionPanelController implements Initializable {

    public AnchorPane layersContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        layersContainer.getChildren().addAll(CreatorModel.getInstance().getViewFactory().getTrackingPane());
        AnchorPane.setBottomAnchor(CreatorModel.getInstance().getViewFactory().getTrackingPane(), 0.0);
        AnchorPane.setLeftAnchor(CreatorModel.getInstance().getViewFactory().getTrackingPane(), 0.0);
        AnchorPane.setRightAnchor(CreatorModel.getInstance().getViewFactory().getTrackingPane(), 0.0);
        AnchorPane.setTopAnchor(CreatorModel.getInstance().getViewFactory().getTrackingPane(), 0.0);
        BackgroundFill backgroundFill = new BackgroundFill(Color.TRANSPARENT, null, null);
        Background background = new Background(backgroundFill);
        CreatorModel.getInstance().getViewFactory().getTrackingPane().setBackground(background);

    }
}




