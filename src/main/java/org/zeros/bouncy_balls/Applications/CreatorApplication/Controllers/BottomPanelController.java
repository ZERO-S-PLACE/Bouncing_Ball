package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class BottomPanelController implements Initializable {
    public Label tipLabel;
    public TextField inputTextField;
    private StringProperty textEntered;
    private final EventHandler<KeyEvent> confirmHandler = this::confirmTextInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textEntered=new SimpleStringProperty();
        inputTextField.addEventHandler(KeyEvent.KEY_PRESSED, confirmHandler);
    }

    private void confirmTextInput(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            textEntered.setValue(inputTextField.textProperty().getValue());
        }
    }
    public StringProperty textEnteredProperty() {
        return textEntered;
    }
    public void setTextEntered(String textEntered) {
        this.textEntered.setValue(textEntered);
        inputTextField.textProperty().setValue(textEntered);
    }

}
