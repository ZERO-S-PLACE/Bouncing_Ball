package org.zeros.bouncy_balls.Applications.CreatorApplication.Views;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
import org.zeros.bouncy_balls.Animation.Animation.AnimationProperties;
import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.TrackingPane;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Level.Level;

import java.io.IOException;

public class CreatorViewFactory {

    private AnchorPane currentLeftPanel;
    private AnchorPane actionChoicePanel;
    private AnchorPane complexAreaActionPanel;
    private AnchorPane movingObjectAddPanel;
    private AnchorPane polyLineDrawingPanel;
    private AnchorPane shapeChoicePanel;
    private BorderPane topPanel;
    private BorderPane bottomPanel;
    private AnchorPane generalSettingsPane;
    private AnchorPane physicsSettingsPane;
    private AnchorPane levelEditionPanel;
    private TrackingPane trackingPane;
    private AnimationPane animationPane;


    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/MainWindow.fxml"));
        loader.setController(CreatorModel.getInstance().controllers().getMainWindowController());
        createStage(loader);
    }

    public AnchorPane getLevelEditionPanel() {

        if (levelEditionPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/ImageEditionPanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getLevelEditionController());
                levelEditionPanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return levelEditionPanel;
    }

    public BorderPane getViewOfTopPanel() {

        if (topPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/TopPanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getTopPanelController());
                topPanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return topPanel;
    }
    public BorderPane getViewOfBottomPanel() {

        if (bottomPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/BottomPanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getBottomPanelController());
                bottomPanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return bottomPanel;
    }
    public AnchorPane getCurrentLeftPanel() {
        return currentLeftPanel;
    }

    public void setCurrentLeftPanel(AnchorPane currentLeftPanel) {
        this.currentLeftPanel = currentLeftPanel;
    }
    public AnchorPane getActionChoicePanel() {
        if (actionChoicePanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/LeftPanels/ActionChoicePanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getActionChoiceController());
                actionChoicePanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return actionChoicePanel;
    }
    public AnchorPane getComplexAreaActionPanel() {
        if (complexAreaActionPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/LeftPanels/ComplexAreaActionChoicePanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getComplexAreaActionChoiceController());
                complexAreaActionPanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return complexAreaActionPanel;
    }
    public AnchorPane getMovingObjectAddPanel() {
        if (movingObjectAddPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/LeftPanels/MovingBallAddPanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getMovingObjectAddController());
                movingObjectAddPanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return movingObjectAddPanel;
    }
    public AnchorPane getPolyLineDrawingPanel() {
        if (polyLineDrawingPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/LeftPanels/PolylineDrawingPanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getPolyLineDrawingController());
                polyLineDrawingPanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return polyLineDrawingPanel;
    }
    public AnchorPane getShapeChoicePanel(boolean includeComplex) {
        if (shapeChoicePanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/LeftPanels/ShapeChoicePanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getShapeChoiceController());
                shapeChoicePanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(includeComplex)CreatorModel.getInstance().controllers().getShapeChoiceController().setComplexView();
        else CreatorModel.getInstance().controllers().getShapeChoiceController().setSimpleView();
        return shapeChoicePanel;
    }

    public AnchorPane getGeneralSettingsPane() {
        if (generalSettingsPane == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/Settings/GeneralSettingsPanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getGeneralSettingsController());
                generalSettingsPane =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return generalSettingsPane;
    }
    public AnchorPane getPhysicsSettingsPane() {
        if (physicsSettingsPane == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/Settings/PhysicsSettingsPanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getPhysicsSettingsController());
                physicsSettingsPane =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return physicsSettingsPane;
    }

    public TrackingPane getTrackingPane() {
        if(this.trackingPane==null){
            this.trackingPane = new TrackingPane();
        }
        return trackingPane;
    }
    public AnimationPane getCurrentAnimationPane(){
        if(animationPane==null){
            Level level=new Level(new AnimationProperties((int)(1080*0.5),(int)(1920*0.5)));
            animationPane=new AnimationPane(level,false);
            animationPane.getAnimationPane().setStyle("-fx-background-color: #243253; ");
            AnchorPane.setLeftAnchor(animationPane.getAnimationPane(), (double)CreatorParameters.getDEFAULT_X_OFFSET());
            AnchorPane.setTopAnchor(animationPane.getAnimationPane(), (double)CreatorParameters.getDEFAULT_Y_OFFSET());
        }
        return animationPane;
    }
    public AnimationPane getNextAnimationPane(){
        animationPane=null;
        return getCurrentAnimationPane();
    }

    private static void createStage(FXMLLoader loader) {
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(CreatorViewFactory.class.getResource("/Icons/ProgramIcon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Zeros Paint");
        stage.show();
    }



}
