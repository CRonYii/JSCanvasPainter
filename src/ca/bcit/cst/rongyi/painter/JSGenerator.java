package ca.bcit.cst.rongyi.painter;

import java.util.List;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * JSGenerator
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class JSGenerator {
    public static void generateJS(List<Shape> shapeList) {
        String js = "";
        for (Shape shape: shapeList) {
            if (shape instanceof Line) {
                Line line = (Line) shape;
                js += getJSLine(line);
            } else if (shape instanceof Rectangle) {
                
            }
        }
        System.out.println(js);
    }
    
    private static String getJSLine(Line shape) {
        return "\ncontext.strokeStyle = \"" + getJSColorCode(shape.getStroke()) + "\";\n" +
                "context.moveTo(" +  shape.getStartX() + ", " + shape.getStartY() + ");\n" + 
                "context.lineTo(" + shape.getEndX() + ", " + shape.getEndY() + ");\n" + 
                "context.stroke();\n";
    }
    
    private static String getJSColorCode(Paint paint) {
        String paintCode = paint.toString();
        return "#" + paintCode.substring(2, paintCode.length() - 2);
    }
}
