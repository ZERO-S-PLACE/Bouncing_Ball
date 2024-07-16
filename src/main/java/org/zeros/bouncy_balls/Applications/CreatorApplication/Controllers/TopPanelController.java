package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Level.Level;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.util.ResourceBundle;

public class TopPanelController implements Initializable {


    public MenuItem saveButton;
    public MenuItem newProjectButton;
    public MenuItem openFileButton;
    public MenuItem generalSettingsButton;
    public MenuItem physicsSettingsButton;
    public MenuItem manualButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonFunctions();

    }

    private void setButtonFunctions() {
        saveButton.setOnAction(e -> saveLevel());
        openFileButton.setOnAction(e -> openSavedLevel());
        newProjectButton.setOnAction(e -> openNewProject());
        generalSettingsButton.setOnAction(e -> CreatorModel.getInstance().controllers().getLevelEditionController().addGeneralSettingsPanel());
        physicsSettingsButton.setOnAction(e -> CreatorModel.getInstance().controllers().getLevelEditionController().addPhysicsSettingsPanel());
        manualButton.setOnAction(e -> showErrorDialog("Page doesn't exist", "Manual not available in this version"));
    }

    private boolean saveLevel() {
        Level level = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getLevel();
       /* if (level.PROPERTIES().getTYPE().equals(AnimationType.GAME)) {
            if (level.getMovingObjects().isEmpty() && level.getMovingObjectsToAdd().isEmpty()) {
                showErrorDialog("Not a game", "No moving objects available");
                return false;
            }
            if (level.getMovingObjectsHaveToEnter().isEmpty() && level.getMovingObjectsCannotEnter().isEmpty()) {
                showErrorDialog("Not a game", "No functional objects - cannot be won");
                return false;
            }
            if (level.getObstaclesToAdd().isEmpty() && level.getObstaclesToAdd().isEmpty()) {
                showErrorDialog("Not a game", "No objects to add in runtime");
                return false;
            }
            if (level.getTargetArea() == null) {
                showErrorDialog("Not a game", "No target area");
                return false;
            }
            if (level.getInputArea() == null && !level.getMovingObjectsToAdd().isEmpty()) {
                showErrorDialog("Not a game", "No input area to add moving objects");
                return false;
            }
        }*/
        try {
            level.save();
            showPopUp("File saved successfully..");
            return true;

        } catch (FileSystemException e) {
            showErrorDialog("File cannot be saved", "Try changing name");
            return false;
        }
    }


    private void openNewProject() {
        if (saveLevel()) {
            CreatorModel.getInstance().controllers().getLevelEditionController().changeAnimationPane(null);
            CreatorModel.getInstance().controllers().getLevelEditionController().addGeneralSettingsPanel();
        }
    }

    private void openSavedLevel() {
        if (saveLevel()) {
            CreatorModel.getInstance().getViewFactory().getNextAnimationPane();
            Level level = getLevelLoadedFromFiles();
            if (level != null) {
                CreatorModel.getInstance().controllers().getLevelEditionController().changeAnimationPane(level);
                showPopUp("File loaded successfully..");
            }

        }
    }

    private Level getLevelLoadedFromFiles() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open a File");

        fileChooser.setInitialDirectory(new File(Level.getDirectoryPath(CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getLevel().PROPERTIES().getTYPE(), "User")));

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bouncy Balls levels", "*.ser"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            if (selectedFile.isFile()) {
                try {
                    return Level.load(selectedFile.getAbsolutePath());
                } catch (Exception e) {
                    showErrorDialog("Damaged file", "File cannot be open");
                    return null;
                }
            }
        }
        return null;


    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showPopUp(String message) {
        PopupControl popup = new PopupControl();
        Text text = new Text(message);
        text.setStyle("-fx-font-size: 1.0em;" + "    -fx-fill: #B8CC8B;");
        StackPane root = new StackPane(text);
        root.setOpacity(0.5);
        root.setBackground(new Background(new BackgroundFill(Properties.BACKGROUND_COLOR().brighter(), new CornerRadii(5), new Insets(-8))));
        popup.getScene().setRoot(root);
        popup.show(CreatorModel.getInstance().getViewFactory().getLevelEditionPanel().getScene().getWindow(), 70, 70);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> popup.hide()));
        timeline.setCycleCount(1); // Only run once
        timeline.play();
    }

}
