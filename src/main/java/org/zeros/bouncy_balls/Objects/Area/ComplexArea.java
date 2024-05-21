package org.zeros.bouncy_balls.Objects.Area;

import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Model.Properties;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexArea implements Serializable {
    private final ArrayList<Area> includedAreas=new ArrayList<>();
    private final ArrayList<Area> excludedAreas=new ArrayList<>();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private Color color=Color.BLACK;

    public void includeArea(Area inputArea){
        if(inputArea!=null) {
        /*
        if(isInsideIncludedAreas(inputArea)%2==0)
        {
            return;
        }
        Area addArea=inputArea;
        for(Area area:includedAreas){
            if(AreasMath.isInside(addArea,area)){
                addArea=null;
               break;
            }
            if(AreasMath.areasIntersect(addArea,area)){
                includedAreas.remove(area);
                inputArea=AreasMath.sum(addArea,area);
            }
        }
        if(addArea!=null)includedAreas.add(addArea);

        Area removeArea=inputArea;
        for(Area area:excludedAreas){
            if(AreasMath.isInside(removeArea,area)){
                includedAreas.add(removeArea);
                removeArea=null;
                break;
            }
            if(AreasMath.areasIntersect(removeArea,area)){
                includedAreas.remove(area);
                inputArea=AreasMath.subtract(removeArea,area);
            }
        }
        if(addArea!=null)includedAreas.add(addArea);
        */
            inputArea.getPath().setFill(color);
            includedAreas.add(inputArea);
        }
    }
    public void excludeArea(Area inputArea){
        if(inputArea!=null) {
            inputArea.getPath().setFill(Properties.BACKGROUND_COLOR());
            inputArea.getPath().setOpacity(1);
            excludedAreas.add(inputArea);
        }
    }


    /*private int isInsideIncludedAreas(Area area){
        int count =0;
        for(Area area1:includedAreas){
            if(AreasMath.isInside(area,area1)){
                count++;
            }
        }
        return count;

    }
    private int isInsideExcludedAreas(Area area){
        int count =0;
        for(Area area1:excludedAreas){
            if(AreasMath.isInside(area,area1)){
                count++;
            }
        }
        return count;

    }*/


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
