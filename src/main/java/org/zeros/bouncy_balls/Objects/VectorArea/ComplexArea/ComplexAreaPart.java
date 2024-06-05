package org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea;

import org.zeros.bouncy_balls.Calculations.AreasMath.AreasMath;
import org.zeros.bouncy_balls.Calculations.AreasMath.SimpleSimpleAreaBoolean;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.VectorArea;

import java.util.ArrayList;
import java.util.Objects;

public class ComplexAreaPart extends VectorArea implements Cloneable{
    private Area area;
    private ArrayList<ComplexAreaPart> excludedAreas;

    public ComplexAreaPart(Area area, ArrayList<ComplexAreaPart> excludedAreas) {
        this.area = area;
        this.excludedAreas = excludedAreas;
        joinIntersectingAreas(excludedAreas);
    }

    public ComplexAreaPart(Area area) {
        this.area = area;
        this.excludedAreas = new ArrayList<>();
    }

    public static void subtractAll(ArrayList<ComplexAreaPart> areasToSubtract, ArrayList<ComplexAreaPart> newAreas) {
        for (ComplexAreaPart partExcluded : areasToSubtract) {
            ArrayList<ComplexAreaPart> changedAreas = new ArrayList<>();
            for (ComplexAreaPart subtractionPart : newAreas) {
                if(AreasMath.areasIntersect(subtractionPart.area, partExcluded.area)) {
                    changedAreas.addAll(subtractionPart.subtract(partExcluded));
                    newAreas.set(newAreas.indexOf(subtractionPart), null);
                }
            }
            newAreas.removeIf(Objects::isNull);
            newAreas.addAll(changedAreas);
        }
    }

    public static void joinIntersectingAreas(ArrayList<ComplexAreaPart> parts) {

            for (ComplexAreaPart part1 : parts) {
                for (ComplexAreaPart part2 : parts) {
                    if (!part1.area.isEqualTo(part2.area)) {
                        if (AreasMath.areasIntersect(part1.area, part2.area)) {
                            parts.addAll(part1.join(part2));
                            parts.remove(part1);
                            parts.remove(part2);
                            joinIntersectingAreas(parts);
                            return;
                        }
                    }

                }
            }

    }

    public Area area() {
        return area;
    }

    public ArrayList<ComplexAreaPart> excluded() {
        return excludedAreas;
    }

    public void excludeArea(Area insideArea) throws IllegalArgumentException {

        if (AreasMath.isInsideArea(area, insideArea)&&!insideArea.isEqualTo(area)) {
            excludedAreas.add(new ComplexAreaPart(insideArea));
            joinIntersectingAreas(excludedAreas);
            return;
        }
        throw new IllegalArgumentException("Given area does not lay inside , can not be excluded");

    }

    public void excludeAreas(ArrayList<Area> insideAreas) throws IllegalArgumentException {
        for (Area area : insideAreas) {
            excludeArea(area);
        }
    }

    public ArrayList<ComplexAreaPart> subtract(ComplexAreaPart part2) {
        ArrayList<ComplexAreaPart> result = new ArrayList<>();
        if (AreasMath.areasIntersect(area, part2.area())) {
            if (AreasMath.isInsideArea(area, part2.area())) {
                ComplexAreaPart part=this.clone();
                part.excludedAreas.add(part2);
                joinIntersectingAreas(part.excludedAreas);
                result.add(part);
            } else if (AreasMath.isInsideArea(part2.area(), area)) {
                for (ComplexAreaPart partExcluded : part2.excludedAreas) {
                    result.addAll(commonPartWith(partExcluded));
                }
            } else {
                result.addAll(intersectingPartSubtract(part2));
            }

        } else {
            ComplexAreaPart part=this.clone();
            result.add(part);
        }
        return result;

    }

    private ArrayList<ComplexAreaPart> intersectingPartSubtract(ComplexAreaPart part2) {

        SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area, part2.area);
        ArrayList<ComplexAreaPart> newAreas = areaBool.subtractBfromA().partAreas();
        for (ComplexAreaPart partExcluded : part2.excludedAreas) {
            newAreas.addAll(commonPartWith(partExcluded));
        }
        subtractAll(excludedAreas, newAreas);
        return newAreas;
    }

    public ArrayList<ComplexAreaPart> join(ComplexAreaPart part2) {
        ArrayList<ComplexAreaPart> result = new ArrayList<>();
        if (AreasMath.areasIntersect(area, part2.area())) {
            if (AreasMath.isInsideArea(area, part2.area())) {
                ComplexAreaPart part=this.clone();

                ArrayList<ComplexAreaPart> temp=new ArrayList<>();
                temp.add(part2);
                subtractAll(temp,part.excludedAreas);
                result.add(part);
            } else if (AreasMath.isInsideArea(part2.area(), area)) {

                ComplexAreaPart part=this.clone();
                ArrayList<ComplexAreaPart> temp=new ArrayList<>();
                temp.add(part);
                subtractAll(temp,part2.excludedAreas);
                result.add(part2);
            } else {
                result.addAll(intersectingPartSum(part2));
            }
        } else {
            ComplexAreaPart part=this.clone();
            result.add(part);
            result.add(part2);
        }
        return result;
    }

    private ArrayList<ComplexAreaPart> intersectingPartSum(ComplexAreaPart part2) {
        SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area, part2.area);
        ArrayList<ComplexAreaPart> newAreas = areaBool.sum().partAreas();
        ArrayList<ComplexAreaPart> areasToSubtract1 = new ArrayList<>(excludedAreas);
        ArrayList<ComplexAreaPart> first=new ArrayList<>();
        first.add(part2);
        subtractAll(first,areasToSubtract1);

        ArrayList<ComplexAreaPart> areasToSubtract2 = new ArrayList<>(part2.excludedAreas);
        ArrayList<ComplexAreaPart> second=new ArrayList<>();
        second.add(this);
        subtractAll(second,areasToSubtract2);


        areasToSubtract1.addAll(areasToSubtract2);
        subtractAll(areasToSubtract1, newAreas);
        return newAreas;
    }

    public ArrayList<ComplexAreaPart> commonPartWith(ComplexAreaPart part2) {
        ArrayList<ComplexAreaPart> newAreas = new ArrayList<>();
        if (AreasMath.areasIntersect(area, part2.area())) {
            SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area, part2.area);
            newAreas = areaBool.intersection().partAreas();
            ArrayList<ComplexAreaPart> areasToSubtract = new ArrayList<>(excludedAreas);
            areasToSubtract.addAll(part2.excludedAreas);
            subtractAll(areasToSubtract, newAreas);
        }
        return newAreas;
    }

    public void rescale(double factor) {
        area.rescale(factor);
        for (ComplexAreaPart excluded : excludedAreas) {
            excluded.rescale(factor);
        }
    }

    public ArrayList<Area> getAllIncludedAreas() {
        ArrayList<Area> included = new ArrayList<>();
        included.add(area);
        for (ComplexAreaPart part : excludedAreas) {
            included.addAll(part.getAllExcludedAreas());
        }
        return included;
    }

    public ArrayList<Area> getAllExcludedAreas() {
        ArrayList<Area> excluded = new ArrayList<>();
        for (ComplexAreaPart part : excludedAreas) {
            excluded.addAll(part.getAllIncludedAreas());
        }
        return excluded;
    }

    public boolean isSolid() {
        return excludedAreas.isEmpty();
    }

    @Override
    public ComplexAreaPart clone() {
        ComplexAreaPart part = null;
        try {
            part = (ComplexAreaPart) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        part.area=area;
        part.excludedAreas=excludedAreas;
        return part;

    }
}
