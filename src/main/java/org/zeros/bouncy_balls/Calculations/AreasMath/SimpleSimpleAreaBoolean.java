package org.zeros.bouncy_balls.Calculations.AreasMath;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Model.Model;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleSimpleAreaBoolean extends AreaBoolean {

    private  Area areaA;
    private  Area areaB;
    private final CopyOnWriteArrayList<Area> subAreas=new CopyOnWriteArrayList<>();

    public SimpleSimpleAreaBoolean(Area areaA, Area areaB){
        this.areaA=areaA.clone();
        this.areaB=areaB.clone();
        try {
            subAreas.addAll(AreasMath.areaSplit(areaA,areaB));
        }catch (IllegalArgumentException e){

            subAreas.add(areaA);
            subAreas.add(areaB);
        }
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

            simplifyConnectedAreas(areasToInclude);
            areasToInclude.removeIf(area->AreasMath.containsArea(area,areasToExclude));


        }
    return new ComplexArea(areasToInclude,areasToExclude);
    }

   /* private void addPreview(ArrayList<Area> areasToInclude) {
        for (Area area:areasToInclude){
            area.getPath().setFill(Color.ALICEBLUE);
            area.getPath().setStrokeWidth(3);
            if(!Model.getInstance()
                    .getLevelCreatorController().preview.getChildren().contains(area.getPath())) {
                Platform.runLater(() -> Model.getInstance()
                        .getLevelCreatorController().preview.getChildren().add(area.getPath()));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }*/


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


    private void simplifyConnectedAreas(ArrayList<Area> tanAreas)throws IllegalArgumentException {
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
                         if(combined.isEmpty())throw new IllegalArgumentException("Error combining areas");
                         tanAreas.remove(subArea1);
                         tanAreas.remove(subArea2);
                         tanAreas.addAll(combined);
                         objectWasSimplified=true;
                         break out;
                        }
                    }

                }
            }
        }
        System.out.println("Areas after simplify: "+tanAreas.size());

    }


}
