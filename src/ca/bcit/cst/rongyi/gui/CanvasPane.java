package ca.bcit.cst.rongyi.gui;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * CanvasPane
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class CanvasPane extends Group {

    private Canvas canvas;
    private GraphicsContext context;
    
    public CanvasPane(int width, int height) {
        canvas = new Canvas(width, height);
        context = canvas.getGraphicsContext2D();
        this.paint();
        
        this.getChildren().add(canvas);
    }
    
    public void paint() {
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, getWidth(), getHeight());
    }
    
    /**
     * Returns the width of the canvas.
     * @return width
     */
    public int getWidth() {
        return (int) canvas.getWidth();
    }
    
    /**
     * Returns the height of the canvas.
     * @return height
     */
    public int getHeight() {
        return (int) canvas.getHeight();
    }
    
}
