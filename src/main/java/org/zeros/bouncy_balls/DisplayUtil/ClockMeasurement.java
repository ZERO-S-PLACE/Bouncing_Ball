package org.zeros.bouncy_balls.DisplayUtil;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ClockMeasurement extends AnchorPane {


    public ClockMeasurement(double range, double value, String unit) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CustomNodes/Clock.fxml"));
        loader.setController(new ClockMeasurementController(range, value, unit));
        try {
            AnchorPane clock = loader.load();
            this.getChildren().setAll(clock);
        } catch (Exception e) {
           throw new RuntimeException("Loading clock pane failed");
        }
    }
}
