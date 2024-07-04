package org.zeros.bouncy_balls.Level;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.OrthoType;
import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.TrackingPane;
import org.zeros.bouncy_balls.Level.Callable.InputValues;
import org.zeros.bouncy_balls.Level.Enums.AreaPurpose;
import org.zeros.bouncy_balls.Level.Enums.ComplexAreaInput;
import org.zeros.bouncy_balls.Level.Enums.MovingObjectPurpose;
import org.zeros.bouncy_balls.Level.Enums.PolyLineSegmentActions;
import org.zeros.bouncy_balls.Level.Previews.OvalPreview;
import org.zeros.bouncy_balls.Level.Previews.PolyLinePreview;
import org.zeros.bouncy_balls.Level.Previews.Preview;
import org.zeros.bouncy_balls.Level.Previews.RectanglePreview;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.OvalArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.RectangleArea;

import java.util.Stack;
import java.util.concurrent.ExecutionException;


public class LevelCreator {
    private final TrackingPane trackingPane = CreatorModel.getInstance().getViewFactory().getTrackingPane();
    private final Stack<AnchorPane> previousLeftPanes = new Stack<>();
    private Level level;
    private Animation animation;
    private Thread inputThread;
    private Thread inputInnerThread;
    private ComplexArea complexArea;
    private ComplexAreaInput complexAreaPurpose = ComplexAreaInput.NONE;
    private AreaPurpose areaPurpose = AreaPurpose.NONE;
    private MovingObjectPurpose movingObjectPurpose = MovingObjectPurpose.NONE;
    private PolyLineSegmentActions polyLineSegment = PolyLineSegmentActions.LINE;
    private boolean inputActive = false;
    private InputValues inputValues=new InputValues();
    private Preview preview;

    public void create() {
        level = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getLevel();
        animation = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getAnimation();
        /*while (true) {
            double dimension= 0;
            try {
                dimension = getDimension();
            } catch (InterruptedException | ExecutionException ignored) {}
            double finalDimension = dimension;
            Platform.runLater(()->CreatorModel.getInstance()
                    .controllers().getBottomPanelController().tipLabel.textProperty().set(String.valueOf((int) finalDimension)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Point2D point= null;
            try {
                point = getPoint();
            } catch (InterruptedException | ExecutionException ignored) {

            }
            Point2D finalPoint = point;
            Platform.runLater(()->CreatorModel.getInstance()
                    .controllers().getBottomPanelController().tipLabel.textProperty().set(String.valueOf(finalPoint)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }*/

        /*addElements();
        simulateAnimation();
        if (agreeTo("Save level ? Y/N")) {
            saveLevel(getStringInput("Enter name: "));
        }
        if (agreeTo("Create next ? Y/N")) {
            this.create();
        } else Platform.exit();*/
    }

    private void changeLeftPane(AnchorPane pane) {
        previousLeftPanes.push(CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel());
        Platform.runLater(() -> CreatorModel.getInstance().controllers().getMainWindowController().setLeftPanel(pane));
    }

    private void loadLastPane() {
        if (!previousLeftPanes.isEmpty()) {
            AnchorPane last = previousLeftPanes.pop();
            Platform.runLater(() -> CreatorModel.getInstance().controllers().getMainWindowController().setLeftPanel(last));
        }
    }

    private void setTipText(String text) {
        Platform.runLater(() -> CreatorModel.getInstance().controllers().getBottomPanelController().tipLabel.setText(text));
    }


    public void initializeMovingObjectsInput(MovingObjectPurpose movingObjectPurpose) {
        changeLeftPane(CreatorModel.getInstance().getViewFactory().getMovingObjectAddPanel());
        setTipText("Adding moving object...");
        this.movingObjectPurpose = movingObjectPurpose;
    }


    public void initializeObstacleInput(AreaPurpose areaPurpose) {
        changeLeftPane(CreatorModel.getInstance().getViewFactory().getShapeChoicePanel(!areaPurpose.equals(AreaPurpose.COMPLEX_AREA)));
        setTipText("Adding area object...");
        this.areaPurpose = areaPurpose;
    }

    public void initializeComplexAreaInput(ComplexAreaInput complexAreaPurpose) {
        changeLeftPane(CreatorModel.getInstance().getViewFactory().getComplexAreaActionPanel());
        setTipText("Adding complex area...");
        this.complexAreaPurpose = complexAreaPurpose;
        complexArea = new ComplexArea();
    }

    public void dismissAction() {
        if (inputActive) dismiss("");
        else dismissAndReturn("");
    }

    private void dismissAndReturn(String message) {
        dismiss(message);
        loadLastPane();
    }

    private void dismiss(String message) {
        if (preview != null){
            preview.remove();
            preview=null;
        }
        inputValues.terminateInput();
        if (inputThread != null) inputThread.interrupt();
        setTipText(message);
    }


    public void drawOval() {
        dismiss("");
        inputThread = new Thread(() -> {
            inputActive = true;
            while (true) {
                Point2D center;
                double radiusX;
                double radiusY;
                try {
                    center = inputValues.getPoint("Oval : center:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }
                OvalPreview preview = new OvalPreview(center);
                this.preview=preview;
                preview.start();
                trackingPane.setOrthoType(OrthoType.ORTHO_Y);

                try {
                    radiusX = inputValues.getDimension("Oval : radius X: ", center);
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    preview.remove();
                    return;
                }
                preview.setOvalRadiusPicked(radiusX);
                trackingPane.setOrthoType(OrthoType.ORTHO_X);
                try {
                    radiusY = inputValues.getDimension("Oval : radius Y: ", center);
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    preview.remove();
                    return;
                }
                trackingPane.setOrthoType(OrthoType.NONE);
                setTipText("");
                preview.remove();
                addAreaAccordingToFunction(new OvalArea(center, radiusX, radiusY, 0));

            }
        });
        inputThread.start();
    }

    public void drawRectangle() {
        dismiss("");
        inputThread = new Thread(() -> {
            inputActive = true;
            while (true) {
                Point2D corner1;
                Point2D corner2;

                try {
                    corner1 = inputValues.getPoint("Rectangle : corner 1:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }
                RectanglePreview preview = new RectanglePreview(corner1);
                this.preview=preview;
                preview.start();
                try {
                    corner2 = inputValues.getPoint("Rectangle : corner 2:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    preview.remove();
                    return;
                }
                setTipText("");
                addAreaAccordingToFunction(new RectangleArea(corner1, corner2, 0));
                preview.remove();
            }
        });
        inputThread.start();
    }

    private void addAreaAccordingToFunction(Area area) {
        switch (areaPurpose) {
            case OBSTACLE -> addObstacle(area);
            case OBSTACLE_TO_ADD -> level.addObstacleToAdd(area);
            case COMPLEX_AREA -> performComplexAreaOperation(area);
        }
    }

    private void addObstacle(Area area) {
        //if (animation.hasFreePlace(area)) {
            level.addObstacle(area);
            Platform.runLater(() -> CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1));
        //} else {
        //    setTipText("Area intersects other objects");
       // }
    }

    private void performComplexAreaOperation(Area area) {
    }

    public void initializePolyLineDrawing() {
        changeLeftPane(CreatorModel.getInstance().getViewFactory().getPolyLineDrawingPanel());
        setTipText("Adding poly line area...");
        this.polyLineSegment = PolyLineSegmentActions.LINE;
        drawPolyLine();

    }

    private void drawPolyLine() {
        dismiss("");
        inputThread = new Thread(() -> {
            while (true) {
                inputActive = true;
                Point2D start;
                try {
                    start = inputValues.getPoint("Poly line : start:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }
                PolylineArea area = new PolylineArea(start);
                PolyLinePreview polyLinePreview = new PolyLinePreview(start);
                this.preview = polyLinePreview;
                polyLinePreview.start();


                while (!polyLineSegment.equals(PolyLineSegmentActions.NONE)) {
                        inputValues.attachPolyLineSegment(area, polyLinePreview, polyLineSegment);
                }

                polyLinePreview.remove();
                preview=null;
                this.polyLineSegment = PolyLineSegmentActions.LINE;
                area.closeAndSave();
                if(!PolylineArea.isSelfIntersecting(area)) {
                    addAreaAccordingToFunction(area);
                }else {
                    dismissAndReturn("Self intersecting areas not supported");
                    break;
                }
            }
        }
        );
        inputThread.start();
    }


    public void changeSegmentAction(PolyLineSegmentActions polyLineSegmentActions) {
        this.polyLineSegment = polyLineSegmentActions;
        if(preview!=null) {
            inputValues.terminateInput();
        }
    }
    public void setSegmentAction(PolyLineSegmentActions polyLineSegmentActions) {
        this.polyLineSegment = polyLineSegmentActions;

    }

}
