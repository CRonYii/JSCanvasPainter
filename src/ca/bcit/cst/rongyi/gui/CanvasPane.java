package ca.bcit.cst.rongyi.gui;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * CanvasPane
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class CanvasPane extends AnchorPane {

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
        context.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());
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

}
