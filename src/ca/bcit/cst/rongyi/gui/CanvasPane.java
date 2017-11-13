package ca.bcit.cst.rongyi.gui;

import ca.bcit.cst.rongyi.painter.Painter;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * CanvasPane
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class CanvasPane extends AnchorPane {

    private Canvas canvas;
    private Painter painter;
    
    private Label cursorPositionLabel;
    private ColorPicker colorPicker;
    private ComboBox<String> shapePicker;
    
    private int cursorStartX, cursorStartY;
    
    /**
     * Constructs an object of type CanvasPane.
     * 
     * @param width int
     * @param height int
     */
    public CanvasPane(int width, int height) {
        canvas = new Canvas(width, height);
        
        canvas.setOnMousePressed(this::setCursorStartPos);
        canvas.setOnMouseDragged(this::paintTempShapeWhenDragged);
        canvas.setOnMouseReleased(this::addShapeWhenDragReleased);
        canvas.setOnMouseMoved(this::updateCursorPositionLabel);
        
        painter = new Painter(canvas);
        painter.paint();

        this.getChildren().add(canvas);
    }

    private void paintTempShapeWhenDragged(MouseEvent event) {
        double startX = cursorStartX;
        double startY = cursorStartY;
        double endX = event.getX();
        double endY = event.getY();
        
        if (cursorStartX > event.getX()) {
            startX = event.getX();
            endX = cursorStartX;
        }
        if (cursorStartY > event.getY()) {
            startY = event.getY();
            endY = cursorStartY;
        }
        
        Shape shape = null;
        
        switch (shapePicker.getValue())  {
        case "Line":
            shape = new Line(cursorStartX, cursorStartY, event.getX(), event.getY());
            break;
        case "Rectangle":
            shape = new Rectangle(startX, startY, Math.abs(endX - startX), Math.abs(endY - startY));
            break;
        }
        shape.setStroke(colorPicker.getValue());
        shape.setFill(colorPicker.getValue());
        
        painter.setTempShape(shape);
    }
    
    private void addShapeWhenDragReleased(MouseEvent event) {
        painter.addTempShape();
    }
    
    private void setCursorStartPos(MouseEvent event) {
        cursorStartX = (int) event.getX();
        cursorStartY =(int) event.getY();
    }
    
    /**
     * Update the label with current cursor position.
     * @param event MouseEvent
     */
    private void updateCursorPositionLabel(MouseEvent event) {
        if (cursorPositionLabel != null) {
            int x = (int) event.getX();
            int y =(int) event.getY();
            cursorPositionLabel.setText("Cursor Position: " + x + ", " + y + "px");
        }
    }

    /**
     * Sets the cursorPositionLabel for this CanvasPane.
     * @param cursorPositionLabel the cursorPositionLabel to set
     */
    public void setCursorPositionLabel(Label cursorPositionLabel) {
        this.cursorPositionLabel = cursorPositionLabel;
    }

    /**
     * Sets the colorPicker for this CanvasPane.
     * @param colorPicker the colorPicker to set
     */
    public void setColorPicker(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
    }

    /**
     * Sets the shapePicker for this CanvasPane.
     * @param shapePicker the shapePicker to set
     */
    public void setShapePicker(ComboBox<String> shapePicker) {
        this.shapePicker = shapePicker;
    }

}
