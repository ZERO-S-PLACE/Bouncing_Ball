package org.zeros.bouncy_balls.DisplayUtil;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ClockMeasurementController implements Initializable {
    public AnchorPane clockPane;
    public Circle clockBackground;
    public Arc clockMeasurement;
    public Circle valueBackground;
    public Text measurementText;
    private double range;
    private double value;
    private double radius=100;
    private String unit;

    public ClockMeasurementController(double range, double value,String unit) {
        this.range = range;
        this.value = value;
        this.unit = unit;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
