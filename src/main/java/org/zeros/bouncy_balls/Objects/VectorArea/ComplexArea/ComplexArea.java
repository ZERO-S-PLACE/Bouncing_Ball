package org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea;

import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.VectorArea;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexArea extends VectorArea implements Serializable {
    private  ArrayList<ComplexAreaPart> parts;
    public ComplexArea(ArrayList<ComplexAreaPart> areaParts){
        this.parts =areaParts;
    }
    public ComplexArea(ArrayList<Area> areasToInclude,ArrayList<Area> areasToExclude){
        //areas should not be intersecting each other , and areas to exclude should be inside areas to include
        parts =new ArrayList<>();
        for (Area area:areasToInclude){
            parts.add(new ComplexAreaPart(area));
        }
        for (Area area:areasToExclude){
            boolean wasExcluded=false;
            for (ComplexAreaPart part:parts){
                if(AreasMath.isInsideArea(part.area(),area)){
                    part.excludeArea(area);
                    wasExcluded=true;
                    break;
                }
            }
            if(!wasExcluded){
                throw new IllegalArgumentException("Area "+area +" cannot be excluded");
            }
        }
    }
    public ComplexArea(){
        parts=new ArrayList<>();
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    private Color color=Color.BLACK;
    public ArrayList<ComplexAreaPart> partAreas() {
        return parts;
    }

    public void rescale(double factor) {
        for (ComplexAreaPart part : parts) {
            part.rescale(factor);
        }
    }

    public ArrayList<Area> getAllIncludedAreas() {
        ArrayList<Area> includedAreas=new ArrayList<>();
        for (ComplexAreaPart part:parts){
            includedAreas.addAll(part.getAllIncludedAreas());
        }
        return includedAreas;
    }

    public ArrayList<Area> getAllExcludedAreas() {
        ArrayList<Area> excludedAreas=new ArrayList<>();
        for (ComplexAreaPart part:parts){
            excludedAreas.addAll(part.getAllExcludedAreas());
        }
        return excludedAreas;
    }
}
