package org.zeros.bouncy_balls.DisplayUtil;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class ClockMeasurementController implements Initializable {

    private final double RANGE;
    private final String UNIT;
    public AnchorPane clockPane;
    public Circle clockBackground;
    public Arc clockMeasurement;
    public Label valueLabel;
    public Label unitLabel;
    private Color backgroundColor = Color.web("#393987");
    private Color measurementColorStart = Color.web("#EFBA5C");
    private Color measurementColorEnd = Color.web("#F4716E");
    private Color textColor = Color.web("#DADAF7");
    private double value;
    private double size = 100;

    public ClockMeasurementController(double range, String unit) {
        this.RANGE = range;
        this.value = range;
        this.UNIT = unit;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateSize();
        updateColors();
        setMeasurementValue();
    }

    public void updateSize() {

        clockPane.setPrefSize(size, size);

        clockBackground.setRadius(size / 2);
        clockBackground.setCenterX(size / 2);
        clockBackground.setCenterY(size / 2);

        clockMeasurement.setRadiusX(size / 2 * 0.95);
        clockMeasurement.setRadiusY(size / 2 * 0.95);
        clockMeasurement.setCenterX(size / 2);
        clockMeasurement.setCenterY(size / 2);

        valueLabel.setPrefSize(valueLabelSize(), valueLabelSize());
        valueLabel.setText(String.valueOf((int) value));
        AnchorPane.setTopAnchor(valueLabel, size / 2 - valueLabelSize() / 2);
        AnchorPane.setLeftAnchor(valueLabel, size / 2 - valueLabelSize() / 2);

        unitLabel.setPrefSize(unitLabelSize(), unitLabelSize());
        unitLabel.setText(UNIT);
        AnchorPane.setTopAnchor(unitLabel, 0.0);
        AnchorPane.setLeftAnchor(unitLabel, 0.0);
    }

    private void updateColors() {
        clockBackground.setFill(backgroundColor);
        clockMeasurement.setFill(measurementColorStart.interpolate(measurementColorEnd,  value / RANGE));
        valueLabel.setStyle("-fx-background-color: " + getColorString(backgroundColor.darker()) + ";" + "-fx-font-size: " + (int) (valueLabelSize() / 2) + "px;" + "-fx-text-fill: " + getColorString(textColor) + "; ");
        unitLabel.setStyle("-fx-background-color: " + getColorString(backgroundColor.brighter()) + ";" + "-fx-font-size: " + (int) (unitLabelSize() / 2) + "px;" + "-fx-text-fill: " + getColorString(textColor) + "; ");
    }

    private String getColorString(Color color) {
        return "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")";
    }

    private void setMeasurementValue() {
        valueLabel.setText(String.valueOf((int) (RANGE - value)));
        clockMeasurement.setStartAngle(-270);
        clockMeasurement.setLength((1 - value / RANGE) * 360);
        clockMeasurement.setFill(measurementColorStart.interpolate(measurementColorEnd,  value / RANGE));
    }

    private double valueLabelSize() {
        return size * 0.35;
    }

    private double unitLabelSize() {
        return size * 0.25;
    }

    public void setSize(double size) {
        this.size = size;
        updateSize();
        updateColors();
    }

    public void setValueSubtracted(double value) {
        this.value = value;
        updateColors();
        setMeasurementValue();
    }

    public void setValue(int value) {
        this.value = RANGE - value;
        updateColors();
        setMeasurementValue();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        updateColors();
    }

    public Color getMeasurementColorStart() {
        return measurementColorStart;
    }

    public void setMeasurementColorStart(Color measurementColorStart) {
        this.measurementColorStart = measurementColorStart;
        updateColors();
    }

    public Color getMeasurementColorEnd() {
        return measurementColorEnd;
    }

    public void setMeasurementColorEnd(Color measurementColorEnd) {
        this.measurementColorEnd = measurementColorEnd;
        updateColors();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        updateColors();
    }


}
