package org.zeros.bouncy_balls.Level.Callable;

import javafx.geometry.Point2D;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Level.Enums.PolyLineSegmentActions;
import org.zeros.bouncy_balls.Level.Previews.PolyLinePreview;
import org.zeros.bouncy_balls.Objects.VectorArea.PolyLineSegment.Segment;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class PolyLineInput implements Callable<Boolean> {
    private final InputValues inputValues=new InputValues();
    private final PolyLinePreview polyLinePreview;
    private final PolylineArea area;

    public InputValues getInputValues() {
        return inputValues;
    }
    private final PolyLineSegmentActions action;
    public PolyLineInput(PolylineArea area, PolyLinePreview polyLinePreview, PolyLineSegmentActions action) {

        this.polyLinePreview = polyLinePreview;
        this.area = area;
        this.action = action;
    }


    @Override
    public Boolean call() {
        return addNextSegment();
    }
    private boolean addNextSegment() {
        switch (action){
            case LINE -> {return addLineSegment(area);}
            case QUAD_CURVE -> {return addQuadCurve(area);}
            case CUBIC_CURVE -> {return addCubicCurve(area);}
            case REMOVE_LAST -> {return removeLastSegment(area);}
            default -> {
               throw new IllegalArgumentException("Not supported operation at polyline");
            }
        }
    }

    private boolean removeLastSegment(PolylineArea area) {
        area.removeLastSegment();
        polyLinePreview.removeLast();
        CreatorModel.getInstance().getLevelCreator().setSegmentAction(PolyLineSegmentActions.LINE);
        return true;
    }

    private boolean addCubicCurve(PolylineArea area) {
        Point2D controlPoint1;
        Point2D controlPoint2;
        Point2D end;
        try {
            controlPoint1 = inputValues.getPoint("Poly line : cubic curve : control point 1:");
        } catch (InterruptedException | ExecutionException e) {
            polyLinePreview.dismissElement();
            return false;
        }
        polyLinePreview.addPoint(controlPoint1);
        try {
            controlPoint2 = inputValues.getPoint("Poly line : cubic curve : control point 2:");
        } catch (InterruptedException | ExecutionException e) {
            polyLinePreview.dismissElement();
            return false;
        }
        polyLinePreview.addPoint(controlPoint2);
        try {
            end = inputValues.getPoint("Poly line : quad curve : end :");
        } catch (InterruptedException | ExecutionException e) {
            polyLinePreview.dismissElement();
            return false;
        }
        polyLinePreview.nextElement(end);
        area.drawCubicCurveTo(controlPoint1,controlPoint2,end);
        return true;
    }

    private boolean addQuadCurve(PolylineArea area) {
        Point2D controlPoint;
        Point2D end;
        try {
            controlPoint = inputValues.getPoint("Poly line : quad curve : control point :");
        } catch (InterruptedException | ExecutionException e) {
            polyLinePreview.dismissElement();
            return false;
        }
        polyLinePreview.addPoint(controlPoint);
        try {
            end = inputValues.getPoint("Poly line : quad curve : end :");
        } catch (InterruptedException | ExecutionException e) {
            polyLinePreview.dismissElement();
            return false;
        }
        polyLinePreview.nextElement(end);
        area.drawQuadCurveTo(controlPoint,end);
        return true;
    }

    private boolean addLineSegment(PolylineArea area) {
        Point2D end;
        try {
            end = inputValues.getPoint("Poly line : line : end :");
        } catch (InterruptedException | ExecutionException e) {
            polyLinePreview.dismissElement();
            return false;
        }
        polyLinePreview.nextElement(end);
        area.drawStraightSegmentTo(end);
        return true;
    }

}
