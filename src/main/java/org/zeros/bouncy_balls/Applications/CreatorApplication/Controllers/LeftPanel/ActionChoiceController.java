package org.zeros.bouncy_balls.Applications.CreatorApplication.Controllers.LeftPanel;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ActionChoiceController extends LeftPanelController {

    public Button movingObjectButton;
    public Button obstacleButton;
    public Button targetAreaButton;
    public Button movingObjectToAddButton;
    public Button obstacleToAddButton;
    public Button inputAreaButton;
    public Button moveElementButton;
    public Button moveButton;
    public Button rotateButton;
    public Button deleteButton;
    public Button acceptButton;
    public Button dismissButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonsGraphic();
    }

    private void setButtonsGraphic() {
       // movingObjectButton.setGraphic(getGraphic("BallMoving"));


    }
private ImageView getGraphic(String name){
/*
    Image image = new Image(getClass().getResource("path/to/your/image.png").toExternalForm());

    // Create an ImageView with the desired size
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(50);  // Set the desired width
    imageView.setFitHeight(50); // Set the desired height
    imageView.setPreserveRatio(true); // Preserve the aspect ratio*/
    return null;

}

    public void resetChoice() {
        CreatorModel.getInstance().getViewFactory().getTrackingPane().fireEvent(new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "",
                "",
                KeyCode.ESCAPE,
                false,
                false,
                false,
                false));
    }


    public void disableButtons(boolean setDisabled){

    }
}
