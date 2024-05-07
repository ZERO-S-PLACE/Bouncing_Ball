package org.zeros.bouncy_balls.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.zeros.bouncy_balls.Level.CreatorApplication;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelCreatorController implements Initializable {
    public static Object getLock() {
        return lock;
    }

    private static final Object lock=new Object();
    public AnchorPane preview;
    public Label messageLabel;
    public TextField inputLabel;
    private final EventHandler<KeyEvent> confirmHandler = this::confirmInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputLabel.addEventHandler(KeyEvent.KEY_PRESSED,confirmHandler);

    }

    private void confirmInput(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){

           synchronized (lock){
               lock.notifyAll();
           }
        }
    }
}
