package org.zeros.bouncy_balls.Controllers.P4_LevelSelection;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import org.zeros.bouncy_balls.Level.Level;

public class CustomListCell extends ListCell<Level> {
    private LevelListCellController controller;
        @Override
        protected void updateItem(Level level, boolean empty) {
            super.updateItem(level,empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/4_LevelSelection/LevelListCell.fxml"));
                controller = new LevelListCellController(level);
                loader.setController(controller);
                setText(null);
                try {
                    setGraphic(loader.load());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    public LevelListCellController getController() {
        return controller;
    }

    }
