package org.zeros.bouncy_balls.Objects.SerializableObjects;

import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexAreaPartSerializable implements Serializable {
    private final AreaSerializable area;
    private final ArrayList<ComplexAreaPartSerializable> excludedAreas = new ArrayList<>();

    public ComplexAreaPartSerializable(ComplexAreaPart part) {
        this.area = new AreaSerializable(part.area());
        for (ComplexAreaPart part1 : part.excluded()) {
            excludedAreas.add(new ComplexAreaPartSerializable(part1));
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
