package org.zeros.bouncy_balls.Model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.zeros.bouncy_balls.Controllers.GamePanelController;
import org.zeros.bouncy_balls.Views.ViewFactory;

import java.io.IOException;

public class Model {
    private final ViewFactory viewFactory;

    GamePanelController gamePanelController;
    private static Model model;

    private Model() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }


    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public GamePanelController getGamePanelController() {
        if(this.gamePanelController==null){
            this.gamePanelController = new GamePanelController();
        }
        return gamePanelController;
    }




}
