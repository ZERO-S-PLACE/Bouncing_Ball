package org.zeros.bouncy_balls.Calculations.AreasMath;

import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;

import java.util.ArrayList;

public class SimpleComplexAreaBoolean extends AreaBoolean {
    private final ComplexAreaPart areaA;
    private final ComplexArea areaB;

    public SimpleComplexAreaBoolean(Area areaA, ComplexArea areaB) {
        this.areaA = new ComplexAreaPart(areaA);
        this.areaB = areaB;
    }

    @Override
    public ComplexArea sum() {
        ArrayList<ComplexAreaPart> partsToJoin = new ArrayList<>(areaB.partAreas());
        partsToJoin.addFirst(areaA);
        ComplexAreaPart.joinIntersectingAreas(partsToJoin);
        return new ComplexArea(partsToJoin);
    }

    @Override
    public ComplexArea intersection() {
        ArrayList<ComplexAreaPart> partsIntersection = new ArrayList<>();
        for (ComplexAreaPart part : areaB.partAreas()) {
            partsIntersection.addAll(areaA.commonPartWith(part));
        }
        return new ComplexArea(partsIntersection);
    }

    @Override
    public ComplexArea subtractBFromA() {
        ArrayList<ComplexAreaPart> partsDifference = new ArrayList<>();
        partsDifference.add(areaA);
        ComplexAreaPart.subtractAll(areaB.partAreas(), partsDifference);
        return new ComplexArea(partsDifference);
    }

    @Override
    public ComplexArea subtractAFromB() {
        ArrayList<ComplexAreaPart> partsDifference = new ArrayList<>(areaB.partAreas());
        ArrayList<ComplexAreaPart> partsToSubtract = new ArrayList<>();
        partsToSubtract.add(areaA);
        ComplexAreaPart.subtractAll(partsToSubtract, partsDifference);
        return new ComplexArea(partsDifference);
    }
}
