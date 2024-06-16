package org.zeros.bouncy_balls.Controllers.P4_LevelSelection;

import com.almasb.fxgl.entity.level.tiled.Layer;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeCell;
import org.zeros.bouncy_balls.Level.Level;

public class LevelListCelFactory extends ListCell<Level> {
        @Override
        protected void updateItem(Level level, boolean empty) {
            super.updateItem(level,empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/4_LevelSelection/LevelListCell.fxml"));
                LevelListCellController controller = new LevelListCellController(level);
                loader.setController(controller);
                setText(null);
                try {
                    setGraphic(loader.load());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }
