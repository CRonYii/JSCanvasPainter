package ca.bcit.cst.rongyi.painter;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Painter
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class Painter {

    private Canvas canvas;
    private GraphicsContext context;

    private Shape tempShape;

    private List<Shape> shapeList = new ArrayList<>();

    /**
     * Constructs an object of type Painter.
     * 
     * @param graphicsContext2D
     */
    public Painter(Canvas canvas) {
        this.canvas = canvas;
        this.context = canvas.getGraphicsContext2D();
    }

    /**
     * Paint the canvas.
     */
    public void paint() {
        paintBackground();
        paintShapes();
        if (tempShape != null) {
            paintShape(tempShape);
        }
    }

    /**
     * Paint the background.
     */
    private void paintBackground() {
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());
    }

    /**
     * Paint the shapes.
     */
    private void paintShapes() {
        for (Shape shape : shapeList) {
            paintShape(shape);
        }
    }

    /**
     * Paint the shapes.
     */
    private void paintShape(Shape shape) {
        if (shape instanceof Line) {
            paintLine((Line) shape);
        } else if (shape instanceof Rectangle) {
            paintRect((Rectangle) shape);
        }
    }

    private void paintLine(Line shape) {
        context.setFill(shape.getFill());
        context.setStroke(shape.getStroke());
        context.strokeLine(shape.getStartX(), shape.getStartY(), shape.getEndX(), shape.getEndY());
    }

    /**
     * Paint rectangle using canvas.
     * 
     * @param rect
     */
    private void paintRect(Rectangle rect) {
        context.setFill(rect.getFill());
        context.setStroke(rect.getStroke());
        context.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void addShape(Shape shape) {
        this.shapeList.add(shape);
        this.paint();
    }

    /**
     * Returns the width of the canvas.
     * 
     * @return width
     */
    public int getCanvasWidth() {
        return (int) canvas.getWidth();
    }

    /**
     * Returns the height of the canvas.
     * 
     * @return height
     */
    public int getCanvasHeight() {
        return (int) canvas.getHeight();
    }

    /**
     * Returns the tempShape for this Painter.
     * @return the tempShape
     */
    public Shape getTempShape() {
        return tempShape;
    }

    /**
     * Sets the tempShape for this Painter.
     * @param tempShape the tempShape to set
     */
    public void setTempShape(Shape tempShape) {
        this.tempShape = tempShape;
        this.paint();
    }

    public void addTempShape() {
        this.addShape(tempShape);
        tempShape = null;
    }
}
