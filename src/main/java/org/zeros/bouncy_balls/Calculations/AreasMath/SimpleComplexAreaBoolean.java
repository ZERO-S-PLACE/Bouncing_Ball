package org.zeros.bouncy_balls.Calculations.AreasMath;

import org.zeros.bouncy_balls.Objects.Area.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.Area.SimpleArea.Area;

import java.util.ArrayList;
import java.util.Objects;

public class SimpleComplexAreaBoolean extends AreaBoolean {
    private final Area areaA;
    private final ComplexArea areaB;

    public SimpleComplexAreaBoolean(Area areaA, ComplexArea areaB) {
        this.areaA = areaA;
        this.areaB = areaB;
    }

    @Override
    public ComplexArea sum() {
        ArrayList<Area> includedAreas = new ArrayList<>(areaB.includedAreas());
        ArrayList<Area> excludedAreas = new ArrayList<>();
        for (Area area : areaB.excludedAreas()) {
            if (AreasMath.areasIntersect(area, areaA)) {
                SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(areaA, area);
                excludedAreas.addAll(areaBool.subtractBfromA().includedAreas());
            } else {
                excludedAreas.add(area);
            }
        }

        includedAreas.addFirst(areaA);
        for (int i = 1; i < includedAreas.size(); i++) {
            Area area1 = includedAreas.getFirst();
            Area area2 = includedAreas.get(i);
            if (AreasMath.areasIntersect(area1, area2)) {
                includedAreas.set(i, null);
                SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area1, area2);
                ComplexArea sumArea = areaBool.sum();
                includedAreas.set(0, sumArea.includedAreas().getFirst());
                excludedAreas.addAll(sumArea.excludedAreas());
            }
        }
        includedAreas.removeIf(Objects::isNull);
        return new ComplexArea(includedAreas, excludedAreas);
    }

    @Override
    public ComplexArea intersection() {
        ArrayList<Area> includedAreas = new ArrayList<>();
        ArrayList<Area> excludedAreas = new ArrayList<>();
        for (Area area : areaB.includedAreas()) {
            if (AreasMath.areasIntersect(area, areaA)) {
                SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area, areaA);
                includedAreas.addAll(areaBool.intersection().includedAreas());
            }
        }

        for (Area area1 : areaB.excludedAreas()) {
            ArrayList<Area> areasToInclude = new ArrayList<>();
            for (Area area2 : includedAreas) {
                if (AreasMath.areasIntersect(area1, area2)) {
                    SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area1, area2);
                    areasToInclude.addAll(areaBool.subtractAfromB().includedAreas());
                    includedAreas.set(includedAreas.indexOf(area2), null);
                }
            }
            includedAreas.removeIf(Objects::isNull);
            includedAreas.addAll(areasToInclude);
        }
        return new ComplexArea(includedAreas, excludedAreas);
    }

    @Override
    public ComplexArea subtractBfromA() {
        Area temp = areaA.clone();
        ArrayList<Area> includedAreas = new ArrayList<>();
        ArrayList<Area> excludedAreas = new ArrayList<>();
        includedAreas.add(temp);

        for (Area area1 : areaB.includedAreas()) {
            ArrayList<Area> results = new ArrayList<>();
            for (Area area2 : includedAreas) {
                if (AreasMath.areasIntersect(area1, area2)) {
                    SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area1, area2);
                    ComplexArea subtract = areaBool.subtractAfromB();
                    results.addAll(subtract.includedAreas());
                    excludedAreas.addAll(subtract.excludedAreas());
                    includedAreas.set(includedAreas.indexOf(area2), null);
                }
            }
            includedAreas.removeIf(Objects::isNull);
            includedAreas.addAll(results);
        }
        for (Area area : areaB.excludedAreas()) {
            if (AreasMath.areasIntersect(area, areaA)) {
                SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area, areaA);
                includedAreas.addAll(areaBool.intersection().includedAreas());
            }
        }
        return new ComplexArea(includedAreas, excludedAreas);
    }

    @Override
    public ComplexArea subtractAfromB() {

        ArrayList<Area> includedAreas = new ArrayList<>();
        ArrayList<Area> excludedAreas = new ArrayList<>();
        for (Area area : areaB.includedAreas()) {
            if (AreasMath.areasIntersect(area, areaA)) {
                SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area, areaA);
                includedAreas.addAll(areaBool.subtractBfromA().includedAreas());
                if (!areaBool.subtractBfromA().excludedAreas().isEmpty() && excludedAreas.isEmpty()) {
                    excludedAreas.add(areaA);
                }
            } else {
                includedAreas.add(area);
            }
        }
        if (excludedAreas.isEmpty()) {
            for (Area area1 : areaB.excludedAreas()) {
                ArrayList<Area> areasToInclude = new ArrayList<>();
                for (Area area2 : includedAreas) {
                    if (AreasMath.areasIntersect(area1, area2)) {
                        SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area1, area2);
                        areasToInclude.addAll(areaBool.subtractAfromB().includedAreas());
                        areasToInclude.set(areasToInclude.indexOf(area2), null);
                    } else {
                        excludedAreas.add(area1);
                    }
                }
                includedAreas.removeIf(Objects::isNull);
                includedAreas.addAll(areasToInclude);

            }
        } else {
            excludedAreas.addAll(areaB.excludedAreas());
            for (int i = 1; i < excludedAreas.size(); i++) {
                Area area1 = excludedAreas.getFirst();
                Area area2 = excludedAreas.get(i);
                if (AreasMath.areasIntersect(area1, area2)) {
                    excludedAreas.set(i, null);
                    SimpleSimpleAreaBoolean areaBool = new SimpleSimpleAreaBoolean(area1, area2);
                    ComplexArea sumArea = areaBool.sum();
                    excludedAreas.set(0, sumArea.includedAreas().getFirst());
                    includedAreas.addAll(sumArea.excludedAreas());
                }
            }
            excludedAreas.removeIf(Objects::isNull);

        }
        return new ComplexArea(includedAreas, excludedAreas);
    }
}
