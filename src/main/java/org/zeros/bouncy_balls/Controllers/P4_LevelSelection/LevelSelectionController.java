package org.zeros.bouncy_balls.Controllers.P4_LevelSelection;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.zeros.bouncy_balls.Animation.Animation.AnimationType;
import org.zeros.bouncy_balls.DisplayUtil.CustomScrollPaneSkin;
import org.zeros.bouncy_balls.DisplayUtil.NodeAnimations;
import org.zeros.bouncy_balls.Level.Level;
import org.zeros.bouncy_balls.Model.Model;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LevelSelectionController implements Initializable {

    public ScrollPane scrollPane;
    public AnchorPane listContainer;
    public ListView<Level> levelsList;
    public Label levelTypeNameLabel;
    public Label levelTypeIconLabel;
    public Button returnButton;
    public BorderPane levelChoicePanel;
    private Map<Level, LevelListCellController> controllersMap = new HashMap<>();
    private TreeSet<Level> levelsInOrder;
    private AnimationType type;
    private String subtype;

    public Map<Level, LevelListCellController> getControllersMap() {
        return controllersMap;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listContainer.requestFocus();
        scrollPane.setSkin(new CustomScrollPaneSkin(scrollPane));
        levelsList.setCellFactory(param -> {
            CustomListCell cell = new CustomListCell();
            cell.controllerProperty().addListener((obs, oldItem, newItem) -> {
                if (oldItem != null) {
                    controllersMap.remove(cell.getItem());
                }
                if (newItem != null) {
                    controllersMap.put(cell.getItem(), newItem);
                }
            });

            return cell;
        });
        levelsList.getItems().addAll(levelsInOrder);
        returnButton.prefWidthProperty().bind(levelChoicePanel.heightProperty().multiply(0.34 * 0.2));
        returnButton.prefHeightProperty().bind(levelChoicePanel.heightProperty().multiply(0.34 * 0.2));
        returnButton.setOnMouseEntered(event -> NodeAnimations.increaseBrightness(returnButton, 0.25));
        returnButton.setOnMouseExited(event -> NodeAnimations.resetBrightness(returnButton));
        returnButton.setOnAction(event -> transitionToSubtypeSelection());
        setSubtypeIcon();
    }

    public void reloadLevelsList() {
        loadLevelsList(type, subtype);
    }

    public void loadLevelsList(AnimationType type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        controllersMap = new HashMap<>();
        if (levelsList != null) {
            levelsList.getItems().removeAll(levelsList.getItems());
        }
        levelsInOrder = new TreeSet<>(Comparator.comparing(Level::getNAME));
        Path path = Paths.get(Level.getDirectoryPath(type, subtype));

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path file : stream) {
                if (!Files.isDirectory(file)) {
                    levelsInOrder.add(Level.load(file.toString()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (levelsList != null) {
            updateIcons();
        }
    }


    private void updateIcons() {
        levelsList.getItems().addAll(levelsInOrder);
        setSubtypeIcon();
    }


    private void setSubtypeIcon() {
        String imagePath = Objects.requireNonNull(getClass().getResource("/Icons/General/Icon" + subtype + ".png")).toExternalForm();
        levelTypeIconLabel.setStyle("-fx-background-image: url('" + imagePath + "');");
        String name = "              " + subtype;
        if (!type.equals(AnimationType.GAME)) {
            name = name + " simulation";
        }
        levelTypeNameLabel.setText(name);
    }

    private void transitionToSubtypeSelection() {
        NodeAnimations.increaseBrightnessOnExit(returnButton);
        Model.getInstance().controllers().getMainWindowController().changeTopLayer(Model.getInstance().getViewFactory().getLevelSubtypePanel(type), 0.3);
    }
    public String getSubtype() {
        return subtype;
    }
}
