package org.zeros.bouncy_balls.Level;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorModel;
import org.zeros.bouncy_balls.Applications.CreatorApplication.Models.CreatorParameters;
import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.OrthoType;
import org.zeros.bouncy_balls.Applications.CreatorApplication.TrackingPane.TrackingPane;
import org.zeros.bouncy_balls.Applications.GameApplication.Model.Properties;
import org.zeros.bouncy_balls.Calculations.AreasMath.SimpleComplexAreaBoolean;
import org.zeros.bouncy_balls.Calculations.VectorMath;
import org.zeros.bouncy_balls.DisplayUtil.BackgroundImages;
import org.zeros.bouncy_balls.Level.Callable.InputValues;
import org.zeros.bouncy_balls.Level.Enums.*;
import org.zeros.bouncy_balls.Level.Previews.*;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.OvalArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.RectangleArea;

import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;


public class LevelCreator {
    private final TrackingPane trackingPane = CreatorModel.getInstance().getViewFactory().getTrackingPane();
    private AnchorPane animationPane;
    private final Stack<AnchorPane> previousLeftPanes = new Stack<>();
    private Level level;
    private Animation animation;
    private Thread inputThread;
    private Thread inputInnerThread;
    private ComplexArea complexArea;
    private ComplexAreaInput complexAreaPurpose = ComplexAreaInput.NONE;
    private AreaPurpose areaPurpose = AreaPurpose.NONE;
    private MovingObjectPurpose movingObjectPurpose = MovingObjectPurpose.NONE;
    private ComplexAreaAction complexAreaAction=ComplexAreaAction.NONE;
    private PolyLineSegmentActions polyLineSegment = PolyLineSegmentActions.LINE;
    private boolean inputActive = false;
    private final InputValues inputValues=new InputValues();
    private Preview preview;
    private Area areaToConfirm;
    private CountDownLatch confirmLatch;
    private final EventHandler<KeyEvent> confirmKeyHandler=this::confirm;
    private final EventHandler<KeyEvent> dismissKeyHandler =this::dismiss;
    public void create() {
        CreatorModel.getInstance().getViewFactory().getScene().addEventHandler(KeyEvent.KEY_PRESSED,confirmKeyHandler);
        CreatorModel.getInstance().getViewFactory().getScene().addEventHandler(KeyEvent.KEY_PRESSED, dismissKeyHandler);
        animation = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getAnimation();
        animationPane= CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getAnimationPane();
        level = animation.getLevel();
    }

    private void confirm(KeyEvent event) {
        if(event.getCode().equals(CreatorParameters.CONFIRM_KEY_CODE()) ) {
            AnchorPane pane=CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel();
            if (pane.equals(CreatorModel.getInstance().getViewFactory()
                            .getShapeChoicePanel())) acceptArea();
            else if (pane.equals(CreatorModel.getInstance().getViewFactory()
                            .getPolyLineDrawingPanel())) changeSegmentAction(PolyLineSegmentActions.NONE);
        }
    }
    private void dismiss(KeyEvent event) {
        if(event.getCode().equals(CreatorParameters.ESCAPE_KEY_CODE())) dismissAction();
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
        CreatorModel.getInstance().controllers().getShapeChoiceController().setSimpleView();
        setTipText("Adding complex area...");
        this.complexAreaPurpose = complexAreaPurpose;
        if(complexAreaPurpose.equals(ComplexAreaInput.OBSTACLE)&&areaPurpose.equals(AreaPurpose.OBSTACLE_TO_ADD)){
            this.complexAreaPurpose=ComplexAreaInput.OBSTACLE_TO_ADD;
        }
        complexArea = new ComplexArea();
    }
    public void changeComplexAreaAction(ComplexAreaAction areaAction) {
        if(areaAction!=ComplexAreaAction.NONE) {
            changeLeftPane(CreatorModel.getInstance().getViewFactory().getShapeChoicePanel());
            this.areaPurpose = AreaPurpose.COMPLEX_AREA;
            this.complexAreaAction = areaAction;
        }else {
            CreatorModel.getInstance().controllers().getShapeChoiceController().setStandardView();
           loadLastPane();
           if(complexAreaPurpose.equals(ComplexAreaInput.OBSTACLE))areaPurpose=AreaPurpose.OBSTACLE;
           else  if(complexAreaPurpose.equals(ComplexAreaInput.OBSTACLE_TO_ADD))areaPurpose=AreaPurpose.OBSTACLE_TO_ADD;
           else this.areaPurpose = AreaPurpose.NONE;
            ComplexArea.removeComplexAreaPreview(complexArea,animationPane);
            addComplexAreaAccordingToFunction();
            this.complexAreaAction = ComplexAreaAction.NONE;
            this.complexAreaPurpose=ComplexAreaInput.NONE;

        }
    }

    private void addComplexAreaAccordingToFunction() {
        switch (complexAreaPurpose){
            case INPUT -> level.setInputArea(complexArea);
            case TARGET -> level.setTargetArea(complexArea);
            case OBSTACLE ,OBSTACLE_TO_ADD-> addSubAreasAsObstacles();
            default -> throw new IllegalArgumentException("Not supported operation");
        }
        CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1);
    }

    private void addSubAreasAsObstacles() {
        for(ComplexAreaPart part:complexArea.partAreas()){
            addAreaAccordingToFunctions(part.area());
        }
    }
    private void performComplexAreaOperation(Area area) {
        ComplexArea.removeComplexAreaPreview(complexArea,animationPane);
        SimpleComplexAreaBoolean areaBoolean=new SimpleComplexAreaBoolean(area,complexArea);
        switch (complexAreaAction){
            case SUM -> complexArea=areaBoolean.sum();
            case INTERSECTION -> complexArea=areaBoolean.intersection();
            case SUBTRACTION_A_B -> complexArea=areaBoolean.subtractAFromB();
            case SUBTRACTION_B_A -> complexArea=areaBoolean.subtractBFromA();
            default -> throw new IllegalArgumentException("not supported operation");
        }
        CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1);
        ComplexArea.addComplexAreaPreview(complexArea,Properties.OBSTACLE_COLOR(),animationPane);
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
                preview.remove();
                setAreaWaitingForConfirm(new RectangleArea(corner1, corner2, 0));

            }
        });
        inputThread.start();
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
                setAreaWaitingForConfirm(new OvalArea(center, radiusX, radiusY, 0));
            }
        });
        inputThread.start();
    }
    private void setAreaWaitingForConfirm(Area area) {
        areaToConfirm=area;
        BackgroundImages.setObstacleBackground(area);
        confirmLatch=new CountDownLatch(1);
        CreatorModel.getInstance().controllers().getShapeChoiceController().setViewOnTransform();
        Platform.runLater(()->animationPane.getChildren().add(areaToConfirm.getPath()));
        try {
            confirmLatch.await();
        } catch (InterruptedException ignored) {
            Platform.runLater(()->CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1));
            CreatorModel.getInstance().controllers().getShapeChoiceController().setStandardView();
            areaToConfirm=null;
        }
    }

    public void acceptArea() {
        if(areaToConfirm!=null) {
            addAreaAccordingToFunctions(areaToConfirm);
            areaToConfirm = null;
            confirmLatch.countDown();
            CreatorModel.getInstance().controllers().getShapeChoiceController().setStandardView();
        }
    }

    private void addAreaAccordingToFunctions(Area area) {
        switch (areaPurpose) {
            case OBSTACLE -> addObstacle(area);
            case OBSTACLE_TO_ADD -> level.addObstacleToAdd(area);
            case COMPLEX_AREA -> performComplexAreaOperation(area);
        }
    }

    private void addObstacle(Area area) {
        if (animation.hasFreePlace(area)) {
            level.addObstacle(area);
            Platform.runLater(() -> CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1));
        } else {
           setTipText("Area intersects other objects");
       }
    }
    public void initializePolyLineDrawing() {
        setTipText("Adding poly line area...");
        this.polyLineSegment = PolyLineSegmentActions.LINE;
        drawPolyLine();
    }
    private void drawPolyLine() {
        dismiss("");
        inputThread = new Thread(() -> {
            while (true) {
                changeLeftPane(CreatorModel.getInstance().getViewFactory().getPolyLineDrawingPanel());
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

                this.polyLineSegment = PolyLineSegmentActions.LINE;
                area.closeAndSave();
                polyLinePreview.remove();
                loadLastPane();
                if(!PolylineArea.isSelfIntersecting(area)) {
                    setAreaWaitingForConfirm(area);
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
    public void rotateCurrentObstacle() {
        if(areaToConfirm!=null){
            inputInnerThread=new Thread(()-> {
                Point2D finalReference;
                Point2D reference;
                try {
                    reference = inputValues.getPoint("Reference point:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }

                RotationPreview rotationPreview = new RotationPreview(areaToConfirm,areaToConfirm.getMassCenter(),reference);
                rotationPreview.start();
                try {
                    finalReference = inputValues.getPoint("Final point:");
                } catch (InterruptedException | ExecutionException e) {
                    rotationPreview.remove();
                    return;
                }
                double angle=areaToConfirm.getMassCenter().angle(reference, finalReference)/360*2*Math.PI;
                if(VectorMath.rotationIsClockWise(reference,finalReference,areaToConfirm.getMassCenter())) {
                    areaToConfirm.setRotation(-angle);
                }
                else {
                    areaToConfirm.setRotation(angle);
                }
                Platform.runLater(()->animationPane.getChildren().add(areaToConfirm.getPath()));
                rotationPreview.remove();

            });
            inputInnerThread.start();
        }

    }
    public void moveCurrentObstacle() {
        inputValues.terminateInput();
        if(areaToConfirm!=null){
            inputInnerThread=new Thread(()-> {

                Point2D reference;
                Point2D finalReference;
                try {
                    reference = inputValues.getPoint("Move: Reference point:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }
                MovePreview movePreview = new MovePreview(areaToConfirm,reference);
                movePreview.start();
                Platform.runLater(()->animationPane.getChildren().remove(areaToConfirm.getPath()));
                try {
                    finalReference = inputValues.getPoint("Move: New point :");
                } catch (InterruptedException | ExecutionException e) {

                    movePreview.remove();
                    Platform.runLater(()->animationPane.getChildren().add(areaToConfirm.getPath()));
                    inputActive = false;
                    return;
                }
                movePreview.remove();
                areaToConfirm.move(areaToConfirm.getMassCenter().add(finalReference.subtract(reference)));
                Platform.runLater(()->animationPane.getChildren().add(areaToConfirm.getPath()));

            });
            inputInnerThread.start();
        }

    }


    public void addMovingBall() {
        dismiss("");
        inputThread = new Thread(() -> {
            inputActive = true;
            while (true) {
                Point2D center;
                double radius;
                Point2D velocityPoint;
                try {
                    center = inputValues.getPoint("Moving ball : center:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }
                MovingBallPreview preview = new MovingBallPreview(center);
                this.preview=preview;
                preview.start();

                try {
                    radius = inputValues.getDimension("Moving ball : radius: ", center);
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    preview.remove();
                    return;
                }

                try {
                    velocityPoint = inputValues.getPoint("Moving ball : velocity /s: ");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    preview.remove();
                    return;
                }

                setTipText("");
                preview.remove();

            }
        });
        inputThread.start();



    }

    public void addMovingBallsArray() {
    }

    public void moveCurrentMovingObject() {
    }

    public void rotateCurrentMovingObject() {
    }
}
