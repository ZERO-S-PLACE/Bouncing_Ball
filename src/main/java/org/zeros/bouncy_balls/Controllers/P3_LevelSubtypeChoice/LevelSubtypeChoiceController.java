package org.zeros.bouncy_balls.Controllers.P3_LevelSubtypeChoice;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.DisplayUtil.CustomTooltip;
import org.zeros.bouncy_balls.DisplayUtil.NodeAnimations;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Model.Properties;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LevelSubtypeChoiceController implements Initializable {

    private final ArrayList<Button> leftButtons = new ArrayList<>();
    private final ArrayList<Button> rightButtons = new ArrayList<>();
    private Button middleButton;
    public Button returnButton;
    public AnchorPane buttonsContainer;
    public BorderPane gameTypeChoicePanel;
    private final ArrayList< ChangeListener<Number>> paneHeightListeners = new ArrayList<>();
    private Point2D dragReference;

    private AnimationType type;
    private void transitionToLevelSelection(String subLevelType) {

    }

    private void transitionToGameSelection() {
        NodeAnimations.increaseBrightnessOnExit(returnButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getLevelTypeChoicePanel(), 0.3);
    }

    private void setButtonsAnimations() {
        returnButton.prefWidthProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.34 * 0.2));
        returnButton.prefHeightProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.34 * 0.2));
        returnButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(returnButton, 0.25));
        returnButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(returnButton));
        setMiddleButton(rightButtons.getFirst());
        rightButtons.removeFirst();
        for(Button button:rightButtons){
            setLayerBrightness(button,rightButtons.indexOf(button)+2);
        }
    }

    public static Image getIcon(String name) {

        Image image;
        try {
            image = new Image(String.valueOf(LevelSubtypeChoiceController.class.getResource("/Icons/P3_LevelSubtypeChoice/Icon" + name + ".png")));
        } catch (Exception e) {
            image = new Image(String.valueOf(LevelSubtypeChoiceController.class.getResource("/Icons/General/BackBallBlue.png")));
        }
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
        return image;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadButtons();
        setButtonsAnimations();
        setActions();
    }

    private void setActions() {
        gameTypeChoicePanel.setOnMousePressed(event -> dragReference = new Point2D(event.getX(), event.getY()));
        gameTypeChoicePanel.setOnMouseReleased(this::turnButtonsList);
        returnButton.setOnAction(event -> transitionToGameSelection());
    }

    private void turnButtonsList(MouseEvent event) {
        if (dragReference.getX() < event.getX()) {
            turnButtonsRight();
        }
        if (dragReference.getX() > event.getX()) {
            turnButtonsLeft();
        }
    }

    private void turnButtonsLeft() {
        if (!rightButtons.isEmpty()) {
            leftButtons.addFirst(middleButton);
            setMiddleButton(rightButtons.getFirst());
            rightButtons.removeFirst();
            animateTransition();
        }
    }

    private void animateTransition() {
        for(ChangeListener<Number> listener:paneHeightListeners){
            gameTypeChoicePanel.heightProperty().removeListener(listener);
        }
            animateTransitionOfElement(middleButton, 1,true);
            for (Button button : rightButtons) {
                animateTransitionOfElement(button, rightButtons.indexOf(button) + 2, true);
            }
            for (Button button : leftButtons) {
                animateTransitionOfElement(button, leftButtons.indexOf(button) + 2, false);
            }
    }

    private void animateTransitionOfElement(Button button, int layer, boolean right) {
      new Thread(()->
       { double finalWidth=calculateWidth(layer);
           double finalAnchor=calculateLeftAnchor(layer,right,finalWidth);
           double startWidth=button.getWidth();
           double startAnchor= AnchorPane.getLeftAnchor(button);
           setLayerBrightness(button,layer);

           for (int i=1;i<=30;i++){
               button.setPrefWidth(startWidth+ (double) i /30*(finalWidth-startWidth));
               button.setPrefHeight(button.getPrefWidth());
               AnchorPane.setLeftAnchor(button,startAnchor+(double) i /30*(finalAnchor-startAnchor));
               AnchorPane.setTopAnchor(button,(gameTypeChoicePanel.getHeight()-button.getWidth())/2);
               try {
                   Thread.sleep(25);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
           resetButtonBindings(middleButton);
           setButtonSizeAndPosition(middleButton, 1, right);
       }).start();
    }

    private double calculateWidth(int layer) {
        return gameTypeChoicePanel.heightProperty().get()*0.6 / layer;
    }
    private double calculateLeftAnchor(int layer,boolean right,double nextWidth){

        if(layer==1)return (gameTypeChoicePanel.getWidth()-nextWidth) / 2;
        return (gameTypeChoicePanel.getWidth()-nextWidth) / 2 + getXOffset(layer, right, nextWidth);
    }

    private void setButtonSizeAndPosition(Button button, int layer, boolean right) {

        double newWidth = gameTypeChoicePanel.heightProperty().doubleValue() * 0.6 / layer;
        button.prefWidthProperty().set(newWidth);
        button.prefHeightProperty().set(newWidth);
        AnchorPane.setLeftAnchor(button, calculateLeftAnchor(layer, right, newWidth));
        AnchorPane.setTopAnchor(button, (gameTypeChoicePanel.heightProperty().doubleValue() - newWidth) / 2);
        ChangeListener<Number> listener=getHeightChangeListener(button, layer, right);
        paneHeightListeners.add(listener);
        gameTypeChoicePanel.heightProperty().addListener(listener);
    }


    private ChangeListener<Number> getHeightChangeListener(Button button, int layer, boolean right) {
        return (observable, oldValue, newValue) -> {
            double newWidth = newValue.doubleValue() * 0.6 / layer;
            button.prefWidthProperty().set(newWidth);
            button.prefHeightProperty().set(newWidth);
            AnchorPane.setLeftAnchor(button, calculateLeftAnchor(layer, right, newWidth));
            AnchorPane.setTopAnchor(button, (newValue.doubleValue() - newWidth) / 2);
        };
    }

    private double getXOffset(int layer, boolean right, double width) {
        double offset = width * (layer - 1);
        if(layer>1)offset=offset+gameTypeChoicePanel.heightProperty().get()*0.6/3;
        if (right) return offset;
        return -offset;
    }

    private void setLayerBrightness(Button button,int layer) {
            button.setOpacity(1);
            NodeAnimations.resetBrightness(button);
            NodeAnimations.decreaseBrightness(button, layer * 0.07);

    }


    private void turnButtonsRight() {
        if (!leftButtons.isEmpty()) {
            rightButtons.addFirst(middleButton);
            setMiddleButton(leftButtons.getFirst());
            leftButtons.removeFirst();
            animateTransition();
        }
    }




    private void setMiddleButton(Button button) {
        if (middleButton != null) {
            middleButton.setDisable(true);
            middleButton.setOnMouseEntered(null);
            middleButton.setOnMouseExited(null);
            middleButton.setOnAction(null);
        }
        middleButton = button;
        buttonsContainer.getChildren().remove(middleButton);
        buttonsContainer.getChildren().add(middleButton);
        middleButton.setDisable(false);
        middleButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(middleButton, 0.25));
        middleButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(middleButton));
        middleButton.setOnAction(event -> transitionToLevelSelection(middleButton.getText()));
    }

    private void loadButtons() {
        for (int i = 1; i <= Properties.getAnimationGenres().size(); i++) {
            Button button = new Button("");
            buttonsContainer.getChildren().addFirst(button);
            rightButtons.add(button);
            setButtonSizeAndPosition(button, i, true);
            button.setDisable(true);
            button.setText(Properties.getAnimationGenres().get(i - 1));
            button.setTooltip(new CustomTooltip(Properties.getAnimationGenresDescriptions().get(i - 1)));
        }
    }

    private void resetButtonBindings(Button button) {
        button.setScaleX(1);
        button.setScaleY(1);
        button.setTranslateX(0);
    }



    public void setAnimationType(AnimationType type) {
        this.type = type;
    }
}
