package org.zeros.bouncy_balls.Controllers.P3_LevelSubtypeChoice;

import javafx.animation.ParallelTransition;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LevelSubtypeChoiceController implements Initializable {
    private ParallelTransition parallelTransition;
    private final ArrayList<Button> leftButtons = new ArrayList<>();
    private final ArrayList<Button> rightButtons = new ArrayList<>();

    private Button middleButton;

    public Button returnButton;
    public AnchorPane buttonsContainer;
    public BorderPane gameTypeChoicePanel;
    private final Map<Button, ChangeListener<Number>> buttonHeightListenerMap = new HashMap<>();
    private final Map<Button, ChangeListener<Number>> buttonWidthListenerMap = new HashMap<>();
    private Point2D dragReference;

    private AnimationType type;

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
        parallelTransition=new ParallelTransition();
        animateTransitionOfElement(middleButton, 1, true);
        for (Button button : rightButtons) {
            animateTransitionOfElement(button, rightButtons.indexOf(button) + 2, true);
        }
        for (Button button : leftButtons) {
            animateTransitionOfElement(button, leftButtons.indexOf(button) + 2, false);
        }

        parallelTransition.setCycleCount(1);
        parallelTransition.play();

        parallelTransition.setOnFinished(event -> {
            moveButtons();
        });

    }

    private void moveButtons() {
        resetButtonBindings(middleButton);
        setButtonSizeAndPosition(middleButton, 1, false);
        for (Button button : rightButtons) {
            resetButtonBindings(button);
            setButtonSizeAndPosition(button, rightButtons.indexOf(button) + 2, true);
        }
        for (Button button : leftButtons) {
            resetButtonBindings(button);
            setButtonSizeAndPosition(button, leftButtons.indexOf(button) + 2, false);
        }

        setLayerBrightness(leftButtons);
        setLayerBrightness(rightButtons);
    }

    private void animateTransitionOfElement(Button button, int layer, boolean right) {
        parallelTransition.getChildren().add(getRescaleTransition(button,calculateWidth(layer)));
       parallelTransition.getChildren().add(getTranslateTransition(button,calculateLeftAnchor(layer,right),right));
    }

   private ScaleTransition getRescaleTransition(Button button, double nextWidth) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(4000),button);
        double scale =nextWidth/button.getWidth();
        transition.setFromX(1);
        transition.setFromY(1);
        transition.setToX(scale);
        transition.setToY(scale);

        return transition;
    }

    private TranslateTransition getTranslateTransition(Button button, double nextX,boolean right) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(4000),button);

        transition.setToX(nextX - button.getLayoutX());
      transition.setCycleCount(1);
      transition.setAutoReverse(false);



        return transition;
    }

    private double calculateWidth(int layer) {
        return gameTypeChoicePanel.heightProperty().get()*0.6 / layer;
    }
    private double calculateLeftAnchor(int layer,boolean right){
        double nextWidth=calculateWidth(layer);
        return (gameTypeChoicePanel.getWidth()-nextWidth) / 2 + getXOffset(layer, right, nextWidth);
    }

    private void setButtonSizeAndPosition(Button button, int layer, boolean right) {
        button.prefHeightProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.6 / layer));
        button.prefWidthProperty().bind(gameTypeChoicePanel.heightProperty().multiply(0.6  / layer));
        ChangeListener<Number> heightListener = getHightChangeListener(button);
        buttonHeightListenerMap.put(button, heightListener);
        button.heightProperty().addListener(heightListener);
        ChangeListener<Number> widthListener = getWidthChangeListener(button, layer, right);
        buttonWidthListenerMap.put(button, widthListener);
        button.widthProperty().addListener(widthListener);
    }

    private double getXOffset(int layer, boolean right, double width) {
        double offset = width * (layer - 1);
        if (right) return offset;
        return -offset;
    }

    private void setLayerBrightness(ArrayList<Button> buttons) {
        for (int i = 1; i <= buttons.size(); i++) {
            Button button = buttons.get(i - 1);
            NodeAnimations.resetBrightness(button);
            NodeAnimations.decreaseBrightness(button, i * 0.05);
            button.setOpacity(1);
        }
    }


    private void turnButtonsRight() {
        if (!leftButtons.isEmpty()) {
            rightButtons.addFirst(middleButton);
            setMiddleButton(leftButtons.getFirst());
            leftButtons.removeFirst();
            animateTransition();
        }
    }


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
        setLayerBrightness(rightButtons);

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
        button.prefHeightProperty().unbind();
        button.prefWidthProperty().unbind();
        button.heightProperty().removeListener(buttonHeightListenerMap.get(button));
        button.widthProperty().removeListener(buttonWidthListenerMap.get(button));
    }





    private ChangeListener<Number> getHightChangeListener(Button button) {
        return (obs, oldVal, newVal) -> {
            AnchorPane.setTopAnchor(button, (gameTypeChoicePanel.getHeight() - newVal.doubleValue()) / 2);
        };
    }


    private ChangeListener<Number> getWidthChangeListener(Button button, int layer, boolean right) {
        return (obs, oldVal, newVal) -> {
            AnchorPane.setLeftAnchor(button, calculateLeftAnchor(layer,right));
        };
    }



    public void setAnimationType(AnimationType type) {
        this.type = type;
    }
}
