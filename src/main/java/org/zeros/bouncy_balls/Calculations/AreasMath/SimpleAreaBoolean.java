package org.zeros.bouncy_balls.Calculations.AreasMath;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.controlsfx.control.PropertySheet;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.Area.ComplexArea;

import java.util.ArrayList;

public class SimpleAreaBoolean {

    private  Area areaA;
    private  Area areaB;
    private final ArrayList<Area> subAreas;

    public SimpleAreaBoolean(Area areaA,Area areaB){
        this.areaA=areaA;
        this.areaB=areaB;
        subAreas=AreasMath.areaSplit(areaA,areaB);
    }
    public  ComplexArea sum() {

        ArrayList<Area> areasToInclude=new ArrayList<>();
        ArrayList<Area> areasToExclude=new ArrayList<>();
        if(!AreasMath.areasIntersect(areaA,areaB)){
            areasToInclude.add(areaA);
            areasToInclude.add(areaB);

        }
        else if(AreasMath.isInsideArea(areaA,areaB)){
            areasToInclude.add(areaA);
        }else if(AreasMath.isInsideArea(areaB,areaA)){
            areasToInclude.add(areaB);
        }else {
            for (Area subArea : subAreas) {
                boolean inA=AreasMath.isInsideArea(areaA, subArea.getPointInside());
                boolean inB=AreasMath.isInsideArea(areaB, subArea.getPointInside());
                if ( inA|| inB) {
                        areasToInclude.add(subArea);
                }else {
                    areasToExclude.add(subArea);
                }
            }
            addPreview(areasToInclude);
            simplifyConnectedAreas(areasToInclude);


        }

    return new ComplexArea(areasToInclude,areasToExclude);
    }

    private void addPreview(ArrayList<Area> areasToInclude) {
        for (Area area:areasToInclude){
            area.getPath().setFill(Color.ALICEBLUE);
            area.getPath().setStrokeWidth(3);
            if(!Model.getInstance()
                    .getLevelCreatorController().preview.getChildren().contains(area.getPath())) {
                Platform.runLater(() -> Model.getInstance()
                        .getLevelCreatorController().preview.getChildren().add(area.getPath()));
            }
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public ComplexArea intersection() {
        ArrayList<Area> areasToInclude=new ArrayList<>();
        ArrayList<Area> areasToExclude=new ArrayList<>();
        if(AreasMath.areasIntersect(areaA,areaB)) {

            if (AreasMath.isInsideArea(areaA, areaB)) {
                areasToInclude.add(areaB);
            } else if (AreasMath.isInsideArea(areaB, areaA)) {
                areasToInclude.add(areaA);
            } else {
                for (Area subArea : subAreas) {
                    boolean inA = AreasMath.isInsideArea(areaA, subArea.getPointInside());
                    boolean inB = AreasMath.isInsideArea(areaB, subArea.getPointInside());
                    if (inA && inB) {
                        areasToInclude.add(subArea);
                    }
                }

            }
        }
        addPreview(areasToInclude);
        return new ComplexArea(areasToInclude,areasToExclude);
    }
    public  ComplexArea subtractBfromA() {
        ArrayList<Area> areasToInclude=new ArrayList<>();
        ArrayList<Area> areasToExclude=new ArrayList<>();
        if(!AreasMath.areasIntersect(areaA,areaB)){
            areasToInclude.add(areaA);

        }
        else if(AreasMath.isInsideArea(areaA,areaB)){
            areasToInclude.add(areaA);
            areasToExclude.add(areaB);
        }else if(!AreasMath.isInsideArea(areaB,areaA)) {
            for (Area subArea : subAreas) {
                boolean inA = AreasMath.isInsideArea(areaA, subArea.getPointInside());
                boolean inB = AreasMath.isInsideArea(areaB, subArea.getPointInside());
                if (inA && !inB) {
                    areasToInclude.add(subArea);
                }
            }

        }
        addPreview(areasToInclude);
        return new ComplexArea(areasToInclude,areasToExclude);
    }
    public  ComplexArea subtractAfromB() {
        reverseAreasOrder();
        ComplexArea difference=subtractBfromA();
        reverseAreasOrder();
        return difference;
    }

    private void reverseAreasOrder() {
        Area temp=areaB;
        areaB=areaA;
        areaA=temp;
    }


    private ArrayList<Area> simplifyConnectedAreas(ArrayList<Area> tanAreas) {
        System.out.println("Areas before simplify: "+tanAreas.size());
        boolean objectWasSimplified=true;
        while (objectWasSimplified){
            objectWasSimplified=false;
            out:for (Area subArea1:tanAreas){
                for (Area subArea2:tanAreas){
                    if(!subArea1.equals(subArea2)){
                        if(AreasMath.haveCommonEdge(subArea1,subArea2)){
                            ArrayList<Area> temp=new ArrayList<>();
                            temp.add(subArea1);
                            temp.add(subArea2);
                         ArrayList<Area> combined=AreasMath.combineTangentAreas(temp);
                         if(combined.size()>1)throw new IllegalArgumentException("areas were not tangent");
                         tanAreas.remove(subArea1);
                         tanAreas.remove(subArea2);
                         tanAreas.add(combined.getFirst());
                         objectWasSimplified=true;
                         break out;
                        }
                    }

                }
            }
        }
        System.out.println("Areas afrer simplify: "+tanAreas.size());
        return tanAreas;
    }


}
