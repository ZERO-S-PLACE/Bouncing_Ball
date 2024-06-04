package org.zeros.bouncy_balls.Objects.SerializableObjects;

import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;

import java.util.ArrayList;

public class ComplexAreaPartSerializable {
    private final AreaSerializable area;
    private final ArrayList<ComplexAreaPartSerializable> excludedAreas = new ArrayList<>();

    public ComplexAreaPartSerializable(ComplexAreaPart area) {
        this.area = new AreaSerializable(area.area());
        for (ComplexAreaPart part : area.excluded()) {
            excludedAreas.add(new ComplexAreaPartSerializable(part));
        }
    }

    public ComplexAreaPart deserialize() {
        ArrayList<ComplexAreaPart> excludedAreas = new ArrayList<>();
        for (ComplexAreaPartSerializable part : this.excludedAreas) {
            excludedAreas.add(part.deserialize());
        }
        return new ComplexAreaPart(area.deserialize(), excludedAreas);
    }
}
