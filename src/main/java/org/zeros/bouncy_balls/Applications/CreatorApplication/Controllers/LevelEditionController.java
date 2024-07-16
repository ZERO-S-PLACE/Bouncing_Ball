package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Level.Level;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelEditionController implements Initializable {

    public AnchorPane layersContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void addGeneralSettingsPanel() {
        AnchorPane settingsPanel = CreatorModel.getInstance().getViewFactory().getGeneralSettingsPane();
        AnchorPane.setLeftAnchor(settingsPanel, (double) CreatorParameters.getDEFAULT_X_OFFSET());
        AnchorPane.setTopAnchor(settingsPanel, (double) CreatorParameters.getDEFAULT_Y_OFFSET());
        Platform.runLater(() -> {
            layersContainer.getChildren().add(settingsPanel);
        });
        CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel().setDisable(true);
        CreatorModel.getInstance().getViewFactory().getViewOfBottomPanel().setDisable(true);
    }

    public void removeGeneralSettingsPanel() {
        Platform.runLater(() -> {
            layersContainer.getChildren().remove(CreatorModel.getInstance().getViewFactory().getGeneralSettingsPane());
        });
        CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel().setDisable(false);
        CreatorModel.getInstance().getViewFactory().getViewOfBottomPanel().setDisable(false);
    }

    public void addPhysicsSettingsPanel() {
        AnchorPane settingsPanel = CreatorModel.getInstance().getViewFactory().getPhysicsSettingsPane();
        AnchorPane.setLeftAnchor(settingsPanel, (double) CreatorParameters.getDEFAULT_X_OFFSET());
        AnchorPane.setTopAnchor(settingsPanel, (double) CreatorParameters.getDEFAULT_Y_OFFSET());
        Platform.runLater(() -> {
            layersContainer.getChildren().add(settingsPanel);
        });
        CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel().setDisable(true);
        CreatorModel.getInstance().getViewFactory().getViewOfBottomPanel().setDisable(true);
    }

    public void removePhysicsSettingsPanel() {
        Platform.runLater(() -> {
            layersContainer.getChildren().remove(CreatorModel.getInstance().getViewFactory().getPhysicsSettingsPane());
        });
        CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel().setDisable(false);
        CreatorModel.getInstance().getViewFactory().getViewOfBottomPanel().setDisable(false);
    }


    public void addEditableElements() {
        Platform.runLater(() -> {
            layersContainer.getChildren().add(CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getAnimationPane());
            CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1);
            layersContainer.getChildren().add(CreatorModel.getInstance().getViewFactory().getTrackingPane());
        });
    }


    public void changeAnimationPane(Level level) {
        Platform.runLater(() -> layersContainer.getChildren().clear());
        if (level == null) CreatorModel.getInstance().getViewFactory().getNextAnimationPane();
        else CreatorModel.getInstance().getViewFactory().getNextAnimationPane(level);
        addEditableElements();
        CreatorModel.getInstance().getNewLevelCreator().create();
    }
}




