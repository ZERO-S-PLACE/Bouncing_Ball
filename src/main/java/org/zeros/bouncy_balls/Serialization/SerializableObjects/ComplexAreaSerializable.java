package org.zeros.bouncy_balls.Serialization.SerializableObjects;

import org.zeros.bouncy_balls.Objects.Area.Area;
import org.zeros.bouncy_balls.Objects.Area.ComplexArea;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexAreaSerializable implements Serializable {
    private final ArrayList<AreaSerializable> includedAreas = new ArrayList<>();
    private final ArrayList<AreaSerializable> excludedAreas = new ArrayList<>();

    public ComplexAreaSerializable(ComplexArea complexArea) {
        for (Area area : complexArea.includedAreas()) {
            includedAreas.add(new AreaSerializable(area));
        }
        for (Area area : complexArea.excludedAreas()) {
            excludedAreas.add(new AreaSerializable(area));
        }
    }

    public ArrayList<AreaSerializable> getIncludedAreas() {
        return includedAreas;
    }

    public ArrayList<AreaSerializable> getExcludedAreas() {
        return excludedAreas;
    }

    public ComplexArea deserialize() {
        ComplexArea area = new ComplexArea();
        for (AreaSerializable areaS : includedAreas) {
            area.includedAreas().add(areaS.deserialize());
        }
        for (AreaSerializable areaS : excludedAreas) {
            area.excludedAreas().add(areaS.deserialize());
        }
        return area;
    }

}
