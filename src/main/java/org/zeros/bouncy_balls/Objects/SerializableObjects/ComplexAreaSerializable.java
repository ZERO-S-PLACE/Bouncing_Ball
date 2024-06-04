package org.zeros.bouncy_balls.Objects.SerializableObjects;

import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexAreaSerializable implements Serializable {
    private final ArrayList<ComplexAreaPartSerializable> includedAreas = new ArrayList<>();

    public ComplexAreaSerializable(ComplexArea complexArea) {
        for (ComplexAreaPart area : complexArea.partAreas()) {
            includedAreas.add(new ComplexAreaPartSerializable(area));
        }
    }


    public ComplexArea deserialize() {
        ArrayList<ComplexAreaPart> areas = new ArrayList<>();
        for (ComplexAreaPartSerializable areaS : includedAreas) {
            areas.add(areaS.deserialize());
        }
        return new ComplexArea(areas);
    }

}
