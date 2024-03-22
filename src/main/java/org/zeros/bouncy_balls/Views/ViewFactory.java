package org.zeros.bouncy_balls.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainWindow.fxml"));
        createStage(loader);
    }

   /* public AnchorPane getViewOfGamePanel() {

        if (imageEditionPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/ImageEditionPanel.fxml"));
                loader.setController(Model.getInstance().getImageEditionPanelController());
                imageEditionPanel =loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return imageEditionPanel;
    }

    public AnchorPane getViewOfTopPanel() {

        if (topPanel == null) {
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/TopPanel.fxml"));
                loader.setController(Model.getInstance().getTopPanelController());
                topPanel =loader.load();
            } catch (Exception e) {
}
*/

    private static void createStage(FXMLLoader loader) {
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Stage stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(ViewFactory.class.getResource("/Icons/ProgramIcon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Bouncy Balls Game");
        stage.show();
    }
}