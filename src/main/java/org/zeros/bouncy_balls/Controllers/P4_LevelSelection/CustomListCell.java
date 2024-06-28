package org.zeros.bouncy_balls.Controllers.P4_LevelSelection;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import org.zeros.bouncy_balls.Level.Level;

public class CustomListCell extends ListCell<Level> {
    private final ObjectProperty<LevelListCellController> controllerProperty = new SimpleObjectProperty<>();

    @Override
    protected void updateItem(Level level, boolean empty) {
        super.updateItem(level, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/4_LevelSelection/LevelListCell.fxml"));
            controllerProperty.set(new LevelListCellController(getItem()));
            loader.setController(controllerProperty.get());
            setText(null);
            try {
                setGraphic(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public ObjectProperty<LevelListCellController> controllerProperty() {
        return controllerProperty;
    }

}
