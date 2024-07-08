package org.zeros.bouncy_balls.Level;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.zeros.bouncy_balls.Animation.Animation.Animation;
import org.zeros.bouncy_balls.Animation.Animation.AnimationPane;
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
import org.zeros.bouncy_balls.Objects.MovingObjects.Ball;
import org.zeros.bouncy_balls.Objects.MovingObjects.MovingObject;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexArea;
import org.zeros.bouncy_balls.Objects.VectorArea.ComplexArea.ComplexAreaPart;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.Area;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.OvalArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.PolylineArea;
import org.zeros.bouncy_balls.Objects.VectorArea.SimpleArea.RectangleArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.*;

public class LevelCreator {
    private final TrackingPane trackingPane = CreatorModel.getInstance().getViewFactory().getTrackingPane();
    private final Stack<AnchorPane> previousLeftPanes = new Stack<>();
    private final InputValues inputValues = new InputValues();
    private AnimationPane animationPane;
    private Level level;
    private Animation animation;
    private Thread inputThread;
    private Thread inputInnerThread;
    private ComplexArea complexArea;
    private ComplexAreaInput complexAreaPurpose = ComplexAreaInput.NONE;
    private AreaPurpose areaPurpose = AreaPurpose.NONE;
    private MovingObjectPurpose movingObjectPurpose = MovingObjectPurpose.NONE;
    private MovingObjectFunction movingObjectFunction = MovingObjectFunction.NONE;
    private ComplexAreaAction complexAreaAction = ComplexAreaAction.NONE;
    private boolean movingObjectInArray = false;
    private PolyLineSegmentActions polyLineSegment = PolyLineSegmentActions.LINE;
    private boolean inputActive = false;
    private Preview preview;
    private final EventHandler<KeyEvent> dismissKeyHandler = this::dismiss;
    private Area areaToConfirm;
    private CountDownLatch confirmLatch;
    private MovingObject movingObjectToConfirm;
    private EditAction editAction = EditAction.NONE;
    private Map<Area, EventHandler<MouseEvent>> obstacleHandlers = new HashMap<>();
    private Map<MovingObject, EventHandler<MouseEvent>> movingObjectHandlers = new HashMap<>();
    private final EventHandler<KeyEvent> confirmKeyHandler = this::confirm;

    private static void reloadAnimationPane() {
        Platform.runLater(() -> CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1));
    }

    public void create() {
        CreatorModel.getInstance().getViewFactory().getScene().addEventHandler(KeyEvent.KEY_PRESSED, confirmKeyHandler);
        CreatorModel.getInstance().getViewFactory().getScene().addEventHandler(KeyEvent.KEY_PRESSED, dismissKeyHandler);
        animation = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().getAnimation();
        animationPane = CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane();
        level = animation.getLevel();
        changeLeftPane(CreatorModel.getInstance().getViewFactory().getActionChoicePanel());
        setTipText("");
    }

    private void confirm(KeyEvent event) {
        if (event.getCode().equals(CreatorParameters.CONFIRM_KEY_CODE())) {
            AnchorPane pane = CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel();
            if (pane.equals(CreatorModel.getInstance().getViewFactory().getShapeChoicePanel())) acceptArea();
            else if (pane.equals(CreatorModel.getInstance().getViewFactory().getPolyLineDrawingPanel()))
                changeSegmentAction(PolyLineSegmentActions.NONE);
            else if (pane.equals(CreatorModel.getInstance().getViewFactory().getMovingObjectAddPanel()))
                acceptMovingObject();
            else if (pane.equals(CreatorModel.getInstance().getViewFactory().getActionChoicePanel())) {
                setEditAction(EditAction.NONE);
            }
        }
    }

    private void dismiss(KeyEvent event) {
        if (event.getCode().equals(CreatorParameters.ESCAPE_KEY_CODE())) dismissAction();
    }

    public void dismissAction() {
        if (inputActive) dismiss("");
        dismissAndReturn("");
    }

    private void dismissAndReturn(String message) {
        dismiss(message);
        loadLastPane();
    }

    private void dismiss(String message) {
        if (preview != null) {
            preview.remove();
            preview = null;
        }
        inputValues.terminateInput();
        if (inputThread != null) inputThread.interrupt();
        setTipText(message);
        Platform.runLater(()->CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel().requestFocus());
    }

    private void changeLeftPane(AnchorPane pane) {
        previousLeftPanes.push(CreatorModel.getInstance().getViewFactory().getCurrentLeftPanel());
        Platform.runLater(() -> {
            CreatorModel.getInstance().controllers().getMainWindowController().setLeftPanel(pane);
            pane.requestFocus();
        });
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
        if (complexAreaPurpose.equals(ComplexAreaInput.OBSTACLE) && areaPurpose.equals(AreaPurpose.OBSTACLE_TO_ADD)) {
            this.complexAreaPurpose = ComplexAreaInput.OBSTACLE_TO_ADD;
        }
        complexArea = new ComplexArea();
    }

    public void changeComplexAreaAction(ComplexAreaAction areaAction) {
        if (areaAction != ComplexAreaAction.NONE) {
            changeLeftPane(CreatorModel.getInstance().getViewFactory().getShapeChoicePanel());
            this.areaPurpose = AreaPurpose.COMPLEX_AREA;
            this.complexAreaAction = areaAction;
        } else {
            CreatorModel.getInstance().controllers().getShapeChoiceController().setStandardView();
            loadLastPane();
            if (complexAreaPurpose.equals(ComplexAreaInput.OBSTACLE)) areaPurpose = AreaPurpose.OBSTACLE;
            else if (complexAreaPurpose.equals(ComplexAreaInput.OBSTACLE_TO_ADD))
                areaPurpose = AreaPurpose.OBSTACLE_TO_ADD;
            else this.areaPurpose = AreaPurpose.NONE;
            ComplexArea.removeComplexAreaPreview(complexArea, animationPane.getAnimationPane());
            addComplexAreaAccordingToFunction();
            this.complexAreaAction = ComplexAreaAction.NONE;
            this.complexAreaPurpose = ComplexAreaInput.NONE;

        }
    }

    private void addComplexAreaAccordingToFunction() {
        switch (complexAreaPurpose) {
            case INPUT -> level.setInputArea(complexArea);
            case TARGET -> level.setTargetArea(complexArea);
            case OBSTACLE, OBSTACLE_TO_ADD -> addSubAreasAsObstacles();
            default -> throw new IllegalArgumentException("Not supported operation");
        }
        CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1);
    }

    private void addSubAreasAsObstacles() {
        for (ComplexAreaPart part : complexArea.partAreas()) {
            addAreaAccordingToFunctions(part.area());
        }
    }

    private void performComplexAreaOperation(Area area) {
        ComplexArea.removeComplexAreaPreview(complexArea, animationPane.getAnimationPane());
        SimpleComplexAreaBoolean areaBoolean = new SimpleComplexAreaBoolean(area, complexArea);
        switch (complexAreaAction) {
            case SUM -> complexArea = areaBoolean.sum();
            case INTERSECTION -> complexArea = areaBoolean.intersection();
            case SUBTRACTION_A_B -> complexArea = areaBoolean.subtractAFromB();
            case SUBTRACTION_B_A -> complexArea = areaBoolean.subtractBFromA();
            default -> throw new IllegalArgumentException("not supported operation");
        }
        CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1);
        ComplexArea.addComplexAreaPreview(complexArea, Properties.OBSTACLE_COLOR(), animationPane.getAnimationPane());
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
                this.preview = preview;
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
                this.preview = preview;
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
        areaToConfirm = area;
        BackgroundImages.setObstacleBackground(area);
        confirmLatch = new CountDownLatch(1);
        CreatorModel.getInstance().controllers().getShapeChoiceController().setViewOnTransform();
        Platform.runLater(() -> animationPane.getAnimationPane().getChildren().add(areaToConfirm.getPath()));
        try {
            confirmLatch.await();
        } catch (InterruptedException ignored) {
            Platform.runLater(() -> CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1));
            CreatorModel.getInstance().controllers().getShapeChoiceController().setStandardView();
            areaToConfirm = null;
        }
    }

    public void acceptArea() {
        if (areaToConfirm != null) {
            addAreaAccordingToFunctions(areaToConfirm);
            areaToConfirm = null;
            confirmLatch.countDown();
            CreatorModel.getInstance().controllers().getShapeChoiceController().setStandardView();
        }
    }

    private void addAreaAccordingToFunctions(Area area) {
        trackingPane.resetView();
        Platform.runLater(() -> animationPane.getAnimationPane().getChildren().remove(area.getPath()));
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
                if (!PolylineArea.isSelfIntersecting(area)) {
                    setAreaWaitingForConfirm(area);
                } else {
                    dismissAndReturn("Self intersecting areas not supported");
                    break;
                }
            }
        });
        inputThread.start();
    }

    public void changeSegmentAction(PolyLineSegmentActions polyLineSegmentActions) {
        this.polyLineSegment = polyLineSegmentActions;
        if (preview != null) {
            inputValues.terminateInput();
        }
    }

    public void setSegmentAction(PolyLineSegmentActions polyLineSegmentActions) {
        this.polyLineSegment = polyLineSegmentActions;
    }

    public void rotateCurrentObstacle() {
        rotateObstacle(areaToConfirm);

    }

    private void rotateObstacle(Area area) {
        if (area != null) {
            inputInnerThread = new Thread(() -> {
                Point2D finalReference;
                Point2D reference;
                try {
                    reference = inputValues.getPoint("Reference point:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }

                RotationPreview rotationPreview = new RotationPreview(area, area.getMassCenter(), reference);
                rotationPreview.start();
                try {
                    finalReference = inputValues.getPoint("Final point:");
                } catch (InterruptedException | ExecutionException e) {
                    rotationPreview.remove();
                    return;
                }
                double angle = area.getMassCenter().angle(reference, finalReference) / 360 * 2 * Math.PI;
                if (VectorMath.rotationIsClockWise(reference, finalReference, area.getMassCenter())) {
                    area.setRotation(-angle);
                } else {
                    area.setRotation(angle);
                }
                Platform.runLater(() -> animationPane.getAnimationPane().getChildren().add(area.getPath()));
                rotationPreview.remove();

            });
            inputInnerThread.start();
        }
    }

    public void moveCurrentObstacle() {
        moveObstacle(areaToConfirm);
    }

    private void moveObstacle(Area area) {
        inputValues.terminateInput();
        if (area != null) {
            inputInnerThread = new Thread(() -> {

                Point2D reference;
                Point2D finalReference;
                try {
                    reference = inputValues.getPoint("Move: Reference point:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }
                MovePreview movePreview = new MovePreview(area, reference);
                movePreview.start();
                Platform.runLater(() -> animationPane.getAnimationPane().getChildren().remove(area.getPath()));
                try {
                    finalReference = inputValues.getPoint("Move: New point :");
                } catch (InterruptedException | ExecutionException e) {
                    movePreview.remove();
                    addNodeToAnimationPane(area.getPath());
                    inputActive = false;
                    return;
                }
                movePreview.remove();
                area.move(area.getMassCenter().add(finalReference.subtract(reference)));
                addNodeToAnimationPane(area.getPath());

            });
            inputInnerThread.start();
        }
    }

    private void addNodeToAnimationPane(Node node) {
        Platform.runLater(() -> {
            if (!animationPane.getAnimationPane().getChildren().contains(node)) {
                animationPane.getAnimationPane().getChildren().add(node);
            }
        });
    }

    public void addMovingBall() {
        dismiss("");
        inputThread = new Thread(() -> {
            Point2D center;
            double radius = -1;
            Point2D velocityPoint = null;
            inputActive = true;
            while (true) {
                try {
                    center = inputValues.getPoint("Moving ball : center:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }
                MovingBallPreview preview = new MovingBallPreview(center);
                this.preview = preview;
                preview.start();
                if (radius == -1) {
                    try {
                        radius = inputValues.getDimension("Moving ball : radius: ", center);
                    } catch (InterruptedException | ExecutionException e) {
                        inputActive = false;
                        preview.remove();
                        return;
                    }
                }
                preview.setRadius(radius);

                if (velocityPoint == null) {
                    try {
                        velocityPoint = inputValues.getPoint("Moving ball : velocity /s: ");
                    } catch (InterruptedException | ExecutionException e) {
                        velocityPoint = new Point2D(0, 0);
                    }
                }
                setTipText("");
                preview.remove();
                Ball ball = new Ball(radius, animation);
                ball.updateCenter(center);
                ball.updateNextCenter(center);
                ball.setInitialVelocity(velocityPoint.subtract(center));
                setMovingObjectWaitingToConfirm(ball);

            }
        });
        inputThread.start();

    }

    private void setMovingObjectWaitingToConfirm(MovingObject movingObject) {
        movingObjectToConfirm = movingObject;
        BackgroundImages.setBallStandardBackground(movingObjectToConfirm.getShape());
        confirmLatch = new CountDownLatch(1);
        CreatorModel.getInstance().controllers().getMovingObjectAddController().setViewOnTransform();
        Platform.runLater(() -> animationPane.getAnimationPane().getChildren().add(movingObjectToConfirm.getShape()));
        try {
            confirmLatch.await();
        } catch (InterruptedException ignored) {
            Platform.runLater(() -> CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1));
            CreatorModel.getInstance().controllers().getMovingObjectAddController().setStandardView();
            movingObjectToConfirm = null;
        }
    }

    public void acceptMovingObject() {
        if (movingObjectToConfirm != null) {
            addMovingObjectAccordingToPurpose(movingObjectToConfirm, true);
            movingObjectToConfirm = null;
            confirmLatch.countDown();
            CreatorModel.getInstance().controllers().getMovingObjectAddController().setStandardView();
        }
    }

    private void addMovingObjectAccordingToPurpose(MovingObject movingObject, boolean reload) {
        if (movingObjectInArray) createArrayOfMovingObjects(movingObject);
        else {
            trackingPane.resetView();
            Platform.runLater(() -> animationPane.getAnimationPane().getChildren().remove(movingObject.getShape()));
            switch (movingObjectPurpose) {
                case MOVING_OBJECT -> addMovingObjectToLevel(movingObject, reload);
                case MOVING_OBJECT_TO_ADD -> {
                    level.addMovingObjectToAdd(movingObject);
                    addMovingObjectFunctions(movingObject);
                }
            }
        }

    }

    private void addMovingObjectFunctions(MovingObject movingObject) {
        switch (movingObjectFunction) {
            case CANNOT_ENTER_TARGET -> level.addMovingObjectCannotEnter(movingObject);
            case HAVE_TO_ENTER_TARGET -> level.addMovingObjectHaveToEnter(movingObject);
        }
    }

    private void addMovingObjectToLevel(MovingObject movingObject, boolean reload) {
        if (animation.hasFreePlace((Ball) (movingObject))) {
            level.addMovingObject(movingObject);
            if (reload) reloadAnimationPane();
        } else {
            setTipText("Moving object intersects other objects");
        }
    }

    public void addMovingBallsArray() {
        this.movingObjectInArray = true;
        addMovingBall();
    }

    private void createArrayOfMovingObjects(MovingObject movingObject) {

        dismiss("");
        inputInnerThread = new Thread(() -> {
            double spacingU;
            double spacingV;
            Point2D totalWidthU;
            Point2D totalWidthV;

            try {
                spacingU = inputValues.getDimension("Array : spacing dir.U:", movingObject.center());

            } catch (InterruptedException | ExecutionException e) {
                inputActive = false;
                return;
            }
            try {
                totalWidthU = inputValues.getPoint("Array : total width dir.U:").subtract(movingObject.center());
            } catch (InterruptedException | ExecutionException e) {
                inputActive = false;
                return;
            }
            try {
                spacingV = inputValues.getDimension("Array :spacing dir.V:", movingObject.center());
            } catch (InterruptedException | ExecutionException e) {
                inputActive = false;
                return;
            }
            try {
                totalWidthV = inputValues.getPoint("Array : total width dir.V:").subtract(movingObject.center());
            } catch (InterruptedException | ExecutionException e) {
                inputActive = false;
                return;
            }
            setTipText("");
            preview.remove();
            int objectsU = (int) Math.round(totalWidthU.magnitude() / spacingU);
            int objectsV = (int) Math.round(totalWidthV.magnitude() / spacingV);
            Point2D spacingVectorU = totalWidthU.multiply((double) 1 / objectsU);
            Point2D spacingVectorV = totalWidthV.multiply((double) 1 / objectsV);

            ArrayList<MovingObject> objects = new ArrayList<>();
            for (int i = 0; i < objectsU; i++) {
                for (int j = 0; j < objectsV; j++) {
                    MovingObject object = movingObject.clone();
                    object.updateCenter(object.center().add(spacingVectorU.multiply(i)).add(spacingVectorV.multiply(j)));
                    object.updateNextCenter(object.center());
                    objects.add(object);
                }
            }
            movingObjectInArray = false;
            for (MovingObject object : objects) {
                addMovingObjectAccordingToPurpose(object, false);
            }
            Platform.runLater(() -> CreatorModel.getInstance().getViewFactory().getCurrentAnimationPane().reloadNodes(1));

        });
        inputInnerThread.start();
    }

    public void moveCurrentMovingObject() {
        inputValues.terminateInput();
        if (movingObjectToConfirm != null) {
            inputInnerThread = new Thread(() -> {
                Point2D reference;
                Point2D finalReference;
                try {
                    reference = inputValues.getPoint("Move: Reference point:");
                } catch (InterruptedException | ExecutionException e) {
                    inputActive = false;
                    return;
                }
                MovePreview movePreview = new MovePreview(movingObjectToConfirm.getShape(), reference);
                movePreview.start();
                Platform.runLater(() -> animationPane.getAnimationPane().getChildren().remove(movingObjectToConfirm.getShape()));
                try {
                    finalReference = inputValues.getPoint("Move: New point :");
                } catch (InterruptedException | ExecutionException e) {

                    movePreview.remove();
                    Platform.runLater(() -> animationPane.getAnimationPane().getChildren().add(movingObjectToConfirm.getShape()));
                    inputActive = false;
                    return;
                }
                movePreview.remove();
                movingObjectToConfirm.updateCenter(movingObjectToConfirm.center().add(finalReference.subtract(reference)));
                movingObjectToConfirm.updateNextCenter(movingObjectToConfirm.center());
                Platform.runLater(() -> animationPane.getAnimationPane().getChildren().add(movingObjectToConfirm.getShape()));

            });
            inputInnerThread.start();
        }

    }

    public void rotateCurrentMovingObject() {
        //to add with different shapes of MovingObjects
    }

    public void changeMovingObjectFunction(MovingObjectFunction movingObjectFunction) {
        this.movingObjectFunction = movingObjectFunction;
    }

    public void setEditAction(EditAction editAction) {
        dismiss("");
        disableObjectsSelection();
        if (!editAction.equals(EditAction.NONE)) {
            CreatorModel.getInstance().controllers().getActionChoiceController().setOnTransform();
            this.editAction = editAction;
            enableObjectsSelection();
        } else {
            CreatorModel.getInstance().controllers().getActionChoiceController().setOnActionPick();
        }

    }

    private void disableObjectsSelection() {
        trackingPane.setMouseTransparent(false);
        trackingPane.setVisible(true);
        if (!obstacleHandlers.isEmpty()) {
            for (Area obstacle : level.getObstacles()) {
                if (obstacleHandlers.get(obstacle) != null)
                    obstacle.getPath().removeEventHandler(MouseEvent.MOUSE_CLICKED, obstacleHandlers.get(obstacle));
            }
        }
        if (!movingObjectHandlers.isEmpty()) {
            for (MovingObject object : level.getMovingObjects()) {
                if (movingObjectHandlers.get(object) != null)
                    object.getShape().removeEventHandler(MouseEvent.MOUSE_CLICKED, movingObjectHandlers.get(object));
            }
        }
        movingObjectHandlers = new HashMap<>();
        obstacleHandlers = new HashMap<>();
    }

    private void enableObjectsSelection() {
        trackingPane.setMouseTransparent(true);
        for (Area obstacle : level.getObstacles()) {
            obstacleHandlers.put(obstacle, obstacleSelectedHandler(obstacle));
            obstacle.getPath().addEventHandler(MouseEvent.MOUSE_CLICKED, obstacleHandlers.get(obstacle));
        }
        for (MovingObject object : level.getMovingObjects()) {
            movingObjectHandlers.put(object, movingObjectSelectedHandler(object));
            object.getShape().addEventHandler(MouseEvent.MOUSE_CLICKED, movingObjectHandlers.get(object));
        }
    }

    private EventHandler<MouseEvent> obstacleSelectedHandler(Area obstacle) {
        return e -> {
            inputThread = new Thread(() -> {
                disableObjectsSelection();
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Area backup = obstacle.clone();
                executorService.submit(() -> {
                    trackingPane.setMouseTransparent(false);
                    obstacle.getPath().setFill(Properties.OBSTACLE_COLOR().brighter());
                    switch (editAction) {
                        case MOVE -> moveObstacle(obstacle);
                        case ROTATE -> rotateObstacle(obstacle);
                        case DELETE -> deleteObstacle(obstacle);
                    }
                });
                try {
                    executorService.awaitTermination(100, TimeUnit.SECONDS);
                } catch (InterruptedException ignored) {
                } finally {
                    verifyNewPosition(obstacle, backup);
                    setEditAction(editAction);
                }


            });
            inputThread.start();
        };
    }

    private void verifyNewPosition(Area obstacle, Area backup) {
        if (!editAction.equals(EditAction.DELETE)) {
            level.getObstacles().remove(obstacle);
            if (animation.hasFreePlace(obstacle)) {
                level.getObstacles().add(obstacle);
            } else {
                level.getObstacles().add(backup);
            }
        }
        reloadAnimationPane();
    }

    private EventHandler<MouseEvent> movingObjectSelectedHandler(MovingObject movingObject) {
        return e -> {
            inputThread = new Thread(() -> {
                disableObjectsSelection();
                MovingObject backup = movingObject.clone();
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(() -> {
                    trackingPane.setMouseTransparent(false);
                    movingObject.getShape().setFill(Color.LIGHTGREEN);
                    movingObjectToConfirm = movingObject;
                    switch (editAction) {
                        case MOVE -> moveCurrentMovingObject();
                        case ROTATE -> rotateCurrentMovingObject();
                        case DELETE -> deleteMovingObject(movingObject);
                    }
                });
                try {
                    executorService.awaitTermination(100, TimeUnit.SECONDS);
                } catch (InterruptedException ignored) {
                } finally {
                    verifyNewPosition(movingObject, backup);
                    setEditAction(editAction);
                }

            });
            inputThread.start();
        };
    }

    private void verifyNewPosition(MovingObject movingObject, MovingObject backup) {
        if (!editAction.equals(EditAction.DELETE)) {
            level.getMovingObjects().remove(movingObject);
            if (animation.hasFreePlace((Ball) movingObject)) {
                level.getMovingObjects().add(movingObject);
            } else {
                level.getMovingObjects().add(backup);
                if (level.getMovingObjectsCannotEnter().contains(movingObject)) {
                    level.getMovingObjectsCannotEnter().remove(movingObject);
                    level.getMovingObjectsCannotEnter().add(backup);
                }
                if (level.getMovingObjectsHaveToEnter().contains(movingObject)) {
                    level.getMovingObjectsHaveToEnter().remove(movingObject);
                    level.getMovingObjectsHaveToEnter().add(backup);
                }
            }
        }
        reloadAnimationPane();
    }

    private void deleteMovingObject(MovingObject movingObject) {
        level.getMovingObjects().remove(movingObject);
        level.getMovingObjectsHaveToEnter().remove(movingObject);
        level.getMovingObjectsCannotEnter().remove(movingObject);

    }
    private void deleteObstacle(Area obstacle) {
        level.getObstacles().remove(obstacle);
    }
}
