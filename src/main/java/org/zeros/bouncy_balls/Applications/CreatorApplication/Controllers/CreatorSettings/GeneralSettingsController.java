package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.CreatorSettings;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.Animation.Borders.BordersType;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Level.Level;

import java.net.URL;
import java.util.ResourceBundle;

public class GeneralSettingsController implements Initializable {
    public TextField nameField;
    public ChoiceBox<AnimationType> typeChoiceBox;
    public TextField widthField;
    public TextField heightField;
    public ChoiceBox<BordersType> bordersChoiceBox;
    public TextField timeField;
    public Text errorText;
    public Button acceptButton;
    public Button dismissButton;
    private Level level;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureNodes();
        setInitialValues();
        setButtonsFunctions();
    }

    private void configureNodes() {
        typeChoiceBox.getItems().add(AnimationType.GAME);
        typeChoiceBox.getItems().add(AnimationType.SIMULATION);
        bordersChoiceBox.getItems().add(BordersType.BOUNCING);
        bordersChoiceBox.getItems().add(BordersType.INFINITE);
        bordersChoiceBox.getItems().add(BordersType.CONNECTED);

    }

    public void setInitialValues() {
        level = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getLevel();
        errorText.setVisible(false);
        nameField.textProperty().set(level.getNAME());
        typeChoiceBox.setValue(level.PROPERTIES().getTYPE());
        widthField.textProperty().set(String.valueOf((int) (level.PROPERTIES().getWIDTH() / Properties.SIZE_FACTOR())));
        heightField.textProperty().set(String.valueOf((int) (level.PROPERTIES().getHEIGHT() / Properties.SIZE_FACTOR())));
        timeField.textProperty().set(String.valueOf(level.PROPERTIES().getTIME()));
        bordersChoiceBox.setValue(level.PROPERTIES().getBOUNDARIES());
    }

    private void setButtonsFunctions() {
        acceptButton.setOnAction(e -> checkAndSaveValues());
        dismissButton.setOnAction(e -> returnToLevelCreation());
    }

    private void checkAndSaveValues() {
        try {
            String name = nameField.textProperty().get();
            int width = Integer.parseInt(widthField.textProperty().get());
            int height = Integer.parseInt(heightField.textProperty().get());
            double time = Double.parseDouble(timeField.textProperty().get());
            if (!name.isEmpty() && width > 100 && width < 30000 && height > 100 && height < 300000 && time > 1) {
                level.setNAME(name);
                level.PROPERTIES().setTYPE(typeChoiceBox.getValue());
                level.PROPERTIES().setWIDTH((int) (width * Properties.SIZE_FACTOR()));
                level.PROPERTIES().setHEIGHT((int) (height * Properties.SIZE_FACTOR()));
                level.PROPERTIES().setTIME(time);
                level.PROPERTIES().setBOUNDARIES(bordersChoiceBox.getValue());
                returnToLevelCreation();
                Platform.runLater(() -> CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1));
            } else {
                errorText.setVisible(true);
                errorText.setText("Wrong value!");
            }
        } catch (Exception e) {
            errorText.setVisible(true);
            errorText.setText("Wrong value!");
        }
    }

    private void returnToLevelCreation() {
        CreatorModel.getInstance().controllers().getLevelEditionController().removeGeneralSettingsPanel();
    }
}
