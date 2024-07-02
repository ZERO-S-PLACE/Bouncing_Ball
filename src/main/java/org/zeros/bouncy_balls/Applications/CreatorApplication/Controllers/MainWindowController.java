package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    public ScrollPane mainImageContainer;
    public AnchorPane topPane;
    public AnchorPane leftPane;
    public AnchorPane bottomPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainImageContainer.setContent(CreatorModel.getInstance().getViewFactory().getLevelEditionPanel());

        topPane.getChildren().add(CreatorModel.getInstance().getViewFactory().getViewOfTopPanel());
        AnchorPane.setRightAnchor(CreatorModel.getInstance().getViewFactory().getViewOfTopPanel(),0.0);
        AnchorPane.setLeftAnchor(CreatorModel.getInstance().getViewFactory().getViewOfTopPanel(), 0.0);

        leftPane.getChildren().add(CreatorModel.getInstance().getViewFactory().getActionChoicePanel());
        AnchorPane.setBottomAnchor(CreatorModel.getInstance().getViewFactory().getActionChoicePanel(),0.0);
        AnchorPane.setTopAnchor(CreatorModel.getInstance().getViewFactory().getActionChoicePanel(), 0.0);


        bottomPane.getChildren().add(CreatorModel.getInstance().getViewFactory().getViewOfBottomPanel());
        AnchorPane.setRightAnchor(CreatorModel.getInstance().getViewFactory().getViewOfBottomPanel(),0.0);
        AnchorPane.setLeftAnchor(CreatorModel.getInstance().getViewFactory().getViewOfBottomPanel(), 0.0);

        mainImageContainer.fitToHeightProperty().set(true);
        mainImageContainer.fitToWidthProperty().set(true);


    }
}
