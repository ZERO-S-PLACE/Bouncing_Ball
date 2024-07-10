package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.CreatorSettings;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Level.Level;

import java.net.URL;
import java.util.ResourceBundle;

public class PhysicsSettingsController implements Initializable {
    public TextField frictionFactorField;
    public TextField downwardGravityFactorField;
    public Text errorText;
    public Button acceptButton;
    public Button dismissButton;

    private Level level;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitialValues();
        setButtonsFunctions();
    }



    public void setInitialValues() {
        level= CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getLevel();
        errorText.setVisible(false);
        frictionFactorField.textProperty().set(String.valueOf(level.PROPERTIES().getFRICTION()));
        downwardGravityFactorField.textProperty().set(String.valueOf(level.PROPERTIES().getGRAVITY()));
    }
    private void setButtonsFunctions() {
        acceptButton.setOnAction(e->checkAndSaveValues());
        dismissButton.setOnAction(e->returnToLevelCreation());
    }

    private void checkAndSaveValues() {
        try {
            double friction = Double.parseDouble(frictionFactorField.textProperty().get());
            double gravity = Double.parseDouble(downwardGravityFactorField.textProperty().get());
            if(friction>=0&&friction<1&&gravity>=0&&gravity<=100){
                level.PROPERTIES().setGRAVITY(gravity*100);
                level.PROPERTIES().setFRICTION(friction);
                returnToLevelCreation();
                Platform.runLater(()->CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane()
                        .reloadNodes(1));
            }else {
                errorText.setVisible(true);
                errorText.setText("Wrong value!");
            }
        }catch (Exception e){
            errorText.setVisible(true);
            errorText.setText("Wrong value!");
        }
    }

    private void returnToLevelCreation() {
        CreatorModel.getInstance().controllers().getLevelEditionController().removePhysicsSettingsPanel();
    }
}
