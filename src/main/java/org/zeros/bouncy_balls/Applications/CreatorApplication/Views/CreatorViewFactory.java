package org.zeros.bouncy_balls.Applications.CreatorApplication.Views;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;

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
    private AnchorPane imageEditionPanel;
    private AnchorPane trackingPane;


    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/MainWindow.fxml"));
        loader.setController(CreatorModel.getInstance().controllers().getMainWindowController());
        createStage(loader);
    }

    public AnchorPane getViewOfImageEditionPanel() {

        if (imageEditionPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/ImageEditionPanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getImageEditionPanelController());
                imageEditionPanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return imageEditionPanel;
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
    public AnchorPane getShapeChoicePanel() {
        if (shapeChoicePanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/CreatorApplication/LeftPanels/ShapeChoicePanel.fxml"));
                loader.setController(CreatorModel.getInstance().controllers().getShapeChoiceController());
                shapeChoicePanel =loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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










    public AnchorPane getTrackingPane() {
        if(this.trackingPane==null){
            this.trackingPane = new AnchorPane();
        }
        return trackingPane;
    }
    public void updateTrackingPaneOverlay(){
        trackingPane.getChildren().removeAll(trackingPane.getChildren());
       /* Rectangle rectangle1=new Rectangle(trackingPane.getBoundsInLocal().getWidth(), background.offsetCoordinatesProperty().get().getY());
        Rectangle rectangle2=new Rectangle(trackingPane.getBoundsInLocal().getWidth(),
                trackingPane.getBoundsInLocal().getHeight()- background.offsetCoordinatesProperty().get().getY()- background.heightProperty().get());
        Rectangle rectangle3=new Rectangle(background.offsetCoordinatesProperty().get().getX(),trackingPane.getBoundsInLocal().getHeight());
        Rectangle rectangle4=new Rectangle(trackingPane.getBoundsInLocal().getWidth()- background.offsetCoordinatesProperty().get().getX()-
                background.widthProperty().get(),trackingPane.getBoundsInLocal().getHeight());
        rectangle1.setFill(Parameters.getSidesOverlayColor());
        rectangle2.setFill(Parameters.getSidesOverlayColor());
        rectangle3.setFill(Parameters.getSidesOverlayColor());
        rectangle4.setFill(Parameters.getSidesOverlayColor());

        AnchorPane.setTopAnchor(rectangle1,0.0);
        AnchorPane.setBottomAnchor(rectangle2,0.0);
        AnchorPane.setLeftAnchor(rectangle3,0.0);
        AnchorPane.setRightAnchor(rectangle4,0.0);
        trackingPane.getChildren().addAll(rectangle1,rectangle2,rectangle3,rectangle4);*/


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
