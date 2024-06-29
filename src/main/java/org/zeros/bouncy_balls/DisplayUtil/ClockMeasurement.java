package org.zeros.bouncy_balls.DisplayUtil;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ClockMeasurement extends AnchorPane {

    private final ClockMeasurementController controller;

    public ClockMeasurement(double range, String unit) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CustomNodes/Clock.fxml"));
        controller = new ClockMeasurementController(range, unit);
        loader.setController(controller);
        try {
            AnchorPane clock = loader.load();
            this.getChildren().setAll(clock);
        } catch (Exception e) {
            throw new RuntimeException("Loading clock pane failed");
        }
    }

    public ClockMeasurementController getController() {
        return controller;
    }
}
