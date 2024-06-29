package org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.Model.Properties;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.VectorArea;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexArea extends VectorArea implements Serializable {
    private final ArrayList<ComplexAreaPart> parts;
    private Color color = Color.BLACK;

    public ComplexArea(ArrayList<ComplexAreaPart> areaParts) {
        this.parts = areaParts;
    }

    public ComplexArea(ArrayList<Area> areasToInclude, ArrayList<Area> areasToExclude) {
        //areas should not be intersecting each other , and areas to exclude should be inside areas to include
        parts = new ArrayList<>();
        for (Area area : areasToInclude) {
            parts.add(new ComplexAreaPart(area));
        }
        for (Area area : areasToExclude) {
            boolean wasExcluded = false;
            for (ComplexAreaPart part : parts) {
                if (AreasMath.isInsideArea(part.area(), area)) {
                    part.excludeArea(area);
                    wasExcluded = true;
                    break;
                }
            }
            if (!wasExcluded) {
                throw new IllegalArgumentException("Area " + area + " cannot be excluded");
            }
        }
    }

    public ComplexArea() {
        parts = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<ComplexAreaPart> partAreas() {
        return parts;
    }

    public void rescale(double factor) {
        for (ComplexAreaPart part : parts) {
            part.rescale(factor);
        }
    }

    public ArrayList<Area> getAllIncludedAreas() {
        ArrayList<Area> includedAreas = new ArrayList<>();
        for (ComplexAreaPart part : parts) {
            includedAreas.addAll(part.getAllIncludedAreas());
        }
        return includedAreas;
    }

    public ArrayList<Area> getAllExcludedAreas() {
        ArrayList<Area> excludedAreas = new ArrayList<>();
        for (ComplexAreaPart part : parts) {
            excludedAreas.addAll(part.getAllExcludedAreas());
        }
        return excludedAreas;
    }
    public static void addComplexAreaToPane(ComplexArea complexArea, Color color, Pane pane) {
        if (complexArea != null) {
            ArrayList<ComplexAreaPart> included = complexArea.partAreas();
            addAreaLayer(included, color,pane);
        }
    }

    private static void addAreaLayer(ArrayList<ComplexAreaPart> included, Color color,Pane pane) {
        ArrayList<ComplexAreaPart> excluded = new ArrayList<>();

        for (ComplexAreaPart part : included) {
            part.area().getPath().setFill(color);
            excluded.addAll(part.excluded());
            Platform.runLater(() -> {
                pane.getChildren().remove(part.area().getPath());
                pane.getChildren().add(part.area().getPath());
            });
        }
        ArrayList<ComplexAreaPart> included2 = new ArrayList<>();
        for (ComplexAreaPart part : excluded) {
            part.area().getPath().setFill(Properties.BACKGROUND_COLOR());
            included2.addAll(part.excluded());
            Platform.runLater(() -> {
                pane.getChildren().remove(part.area().getPath());
                pane.getChildren().add(part.area().getPath());
            });
        }
        if (!included2.isEmpty()) {
            addAreaLayer(included2, color,pane);
        }
    }
}
