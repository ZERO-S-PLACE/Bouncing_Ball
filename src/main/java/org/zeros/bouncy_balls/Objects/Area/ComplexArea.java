package org.zeros.bouncy_balls.Objects.Area;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexArea implements Serializable {
    private final ArrayList<Area> includedAreas=new ArrayList<>();
    private final ArrayList<Area> excludedAreas=new ArrayList<>();

    public void includeArea(Area inputArea){
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
        includedAreas.add(inputArea);
    }
    public void excludeArea(Area inputArea){
        excludedAreas.add(inputArea);
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
}
