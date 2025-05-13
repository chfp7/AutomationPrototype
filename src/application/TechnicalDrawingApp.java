package application;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.SnapshotParameters;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class TechnicalDrawingApp extends Application {
    private Group drawingArea;
    private Shape selectedShape;
    private double offsetX, offsetY;
    private Scale scaleTransform = new Scale(1, 1, 0, 0);
    private Deque<UndoableAction> undoStack = new ArrayDeque<>();
    private Deque<UndoableAction> redoStack = new ArrayDeque<>();
    private Line tempLine;
    private ToggleButton drawLineToggle;


    @Override
    public void start(Stage primaryStage) {
        drawingArea = new Group();
        drawingArea.getTransforms().add(scaleTransform);
        setupMouseEvents();

        BorderPane root = new BorderPane();
        root.setCenter(drawingArea);

        ToolBar toolbar = new ToolBar();
        Button addCircleBtn = new Button("Add Circle");
        addCircleBtn.setOnAction(e -> addShape(createCircle(305, 200, 50)));
        Button addRectangleBtn = new Button("Add Rectangle");
        addRectangleBtn.setOnAction(e -> addShape(createRectangle(100, 100, 150, 100)));
        Button addLineBtn = new Button("Add Line");
        addLineBtn.setOnAction(e -> addShape(createLine(100, 100, 200, 200)));
        drawLineToggle = new ToggleButton("ADD Line");
        Button autoShapesBtn = new Button("Auto Shapes");
        autoShapesBtn.setOnAction(e -> createAutomaticShapes());
        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> deleteSelectedShape());
        Button undoBtn = new Button("Undo");
        undoBtn.setOnAction(e -> undo());
        Button redoBtn = new Button("Redo");
        redoBtn.setOnAction(e -> redo());
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> saveDrawing());
        Button resetZoomBtn = new Button("Reset Zoom");
        resetZoomBtn.setOnAction(e -> resetZoom());

        toolbar.getItems().addAll(addCircleBtn, addRectangleBtn, drawLineToggle,addLineBtn, autoShapesBtn, deleteBtn, undoBtn, redoBtn, saveBtn, resetZoomBtn);
        root.setTop(toolbar);

        Scene scene = new Scene(root, 800, 600);
        //when scrolling we zoom in and zoom out
        scene.setOnScroll(e -> {
            double scaleFactor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            scaleTransform.setX(scaleTransform.getX() * scaleFactor);
            scaleTransform.setY(scaleTransform.getY() * scaleFactor);
        });

        primaryStage.setTitle("Technical Drawing Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addShape(Shape shape) {
        drawingArea.getChildren().add(shape);
        undoStack.push(new UndoableAction(ActionType.ADD, shape));
        redoStack.clear();
    }


    private void deleteSelectedShape() {
        if (selectedShape != null) {
            drawingArea.getChildren().remove(selectedShape);
            undoStack.push(new UndoableAction(ActionType.REMOVE, selectedShape));
            redoStack.clear();
            selectedShape = null;
        }
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            UndoableAction action = undoStack.pop();
            redoStack.push(action);
            if (action.type == ActionType.ADD) {
                drawingArea.getChildren().remove(action.shape);
            } else {
                drawingArea.getChildren().add(action.shape);
            }
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            UndoableAction action = redoStack.pop();
            undoStack.push(action);
            if (action.type == ActionType.ADD) {
                drawingArea.getChildren().add(action.shape);
            } else {
                drawingArea.getChildren().remove(action.shape);
            }
        }
    }

    private void resetZoom() {
        scaleTransform.setX(1);
        scaleTransform.setY(1);
    }

    private void setupMouseEvents() {
        drawingArea.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (drawLineToggle.isSelected()) {
                tempLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                tempLine.setStroke(Color.BLACK);
                drawingArea.getChildren().add(tempLine);
            } else {
                for (Node shape : drawingArea.getChildren().filtered(n -> n instanceof Shape)) {
                    if (shape.contains(e.getX(), e.getY())) {
                        deselectShape();
                        selectShape(shape);
                        offsetX = e.getX();
                        offsetY = e.getY();
                        return;
                    }
                }
                deselectShape();
            }
        });

        drawingArea.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (drawLineToggle.isSelected() && tempLine != null) {
                tempLine.setEndX(e.getX());
                tempLine.setEndY(e.getY());
            } else if (selectedShape != null) {
                double dx = e.getX() - offsetX;
                double dy = e.getY() - offsetY;
                if (selectedShape instanceof Circle) {
                    ((Circle) selectedShape).setCenterX(((Circle) selectedShape).getCenterX() + dx);
                    ((Circle) selectedShape).setCenterY(((Circle) selectedShape).getCenterY() + dy);
                } else if (selectedShape instanceof Rectangle) {
                    ((Rectangle) selectedShape).setX(((Rectangle) selectedShape).getX() + dx);
                    ((Rectangle) selectedShape).setY(((Rectangle) selectedShape).getY() + dy);
                } else if (selectedShape instanceof Line) {
                    Line line = (Line) selectedShape;
                    line.setStartX(line.getStartX() + dx);
                    line.setStartY(line.getStartY() + dy);
                    line.setEndX(line.getEndX() + dx);
                    line.setEndY(line.getEndY() + dy);
                }
                offsetX = e.getX();
                offsetY = e.getY();
            }
        });

        drawingArea.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (drawLineToggle.isSelected() && tempLine != null) {
                if (!drawingArea.getChildren().contains(tempLine)) { // Check if it's already added
                    addShape(tempLine);
                }
                tempLine = null;
            }
        });

    }


    private void selectShape(Node shape) {
        deselectShape();
        selectedShape = (Shape) shape;
        ((Shape) shape).setStroke(Color.RED);
    }

    private void deselectShape() {
        if (selectedShape != null) {
            selectedShape.setStroke(Color.BLACK);
            selectedShape = null;
        }
    }

    private Rectangle createRectangle(double x, double y, double width, double height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        rect.setStroke(Color.BLACK);
        rect.setFill(Color.TRANSPARENT);
        return rect;
    }

    private Circle createCircle(double x, double y, double radius) {
        Circle circ = new Circle(x, y, radius);
        circ.setStroke(Color.BLACK);
        circ.setFill(Color.TRANSPARENT);
        return circ;
    }

    private Line createLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.BLACK);
        line.setFill(Color.TRANSPARENT);
        return line;
    }

    private void createAutomaticShapes() {
        int rows = 5;
        int cols = 5;
        double spacing = 100;
        double radius = 30;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = 100 + j * spacing;
                double y = 100 + i * spacing;
                addShape(createCircle(x, y, radius));
            }
        }
    }

    private void saveDrawing() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            WritableImage image = drawingArea.snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private enum ActionType { ADD, REMOVE }
    private static class UndoableAction {
        ActionType type;
        Shape shape;
        UndoableAction(ActionType type, Shape shape) {
            this.type = type;
            this.shape = shape;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}