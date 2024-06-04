package org.zeros.bouncy_balls.Objects.Area.ComplexArea;

import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Objects.Area.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.Area.VectorArea;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexArea extends VectorArea implements Serializable {
    private  ArrayList<Area> includedAreas=new ArrayList<>();
    private  ArrayList<Area> excludedAreas=new ArrayList<>();
    public ComplexArea(ArrayList<Area> includedAreas,ArrayList<Area> excludedAreas){
        this.includedAreas=includedAreas;
        this.excludedAreas=excludedAreas;
    }
    public ComplexArea(){}

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private Color color=Color.BLACK;
    public ArrayList<Area> includedAreas() {
        return includedAreas;
    }
    public ArrayList<Area> excludedAreas() {
        return excludedAreas;
    }

    public void rescale(double factor) {
        for (Area area : includedAreas) {
            area.rescale(factor);
        }
        for (Area area : excludedAreas) {
            area.rescale(factor);
        }
    }
}
