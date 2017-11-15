package ca.bcit.cst.rongyi.painter;

import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * CodeTranslator.
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class CodeTranslator {
    
    public static String generateJS(List<Shape> shapeList) {
        String js = "";
        for (Shape shape: shapeList) {
            if (shape instanceof Line) {
                Line line = (Line) shape;
                js += getJSLine(line);
            } else if (shape instanceof Rectangle) {
                Rectangle rect = (Rectangle) shape;
                js += getJSRect(rect);
            }
        }
        return js;
    }
    
    private static String getJSLine(Line shape) {
        return "\ncontext.strokeStyle = \"" + getJSColorCode(shape.getStroke()) + "\";\n" +
                "context.moveTo(" +  shape.getStartX() + ", " + shape.getStartY() + ");\n" + 
                "context.lineTo(" + shape.getEndX() + ", " + shape.getEndY() + ");\n" + 
                "context.lineWidth = " + shape.getStrokeWidth() + ";\n" +
                "context.stroke();\n";
    }
    
    private static String getJSRect(Rectangle shape) {
        return "\ncontext.strokeStyle = \"" + getJSColorCode(shape.getStroke()) + "\";\n" +
                "context.fillStyle = \"" + getJSColorCode(shape.getFill()) + "\";\n" +
                "context.lineWidth = " + shape.getStrokeWidth() + ";\n" +
                "context.fillRect(" + shape.getX() + ", " + shape.getY() + ", " + shape.getWidth() + ", " + shape.getHeight() + ");\n" +
                "context.strokeRect(" + shape.getX() + ", " + shape.getY() + ", " + shape.getWidth() + ", " + shape.getHeight() + ");\n";
                
    }
    
    public static String generateXML(List<Shape> shapeList) {
        String xml = "";
        for (Shape shape: shapeList) {
            if (shape instanceof Line) {
                Line line = (Line) shape;
                xml += getXMLLine(line);
            } else if (shape instanceof Rectangle) {
                Rectangle rect = (Rectangle) shape;
                xml += getXMLRect(rect);
            }
        }
        return xml;
    }
    
    private static String getXMLRect(Rectangle shape) {
        return "<shape>\r\n" + 
                "    <type>Rectangle</type>\r\n" + 
                String.format("    <stroke_style>%s</stroke_style>\r\n", getXMLColorCode(shape.getStroke())) + 
                String.format("    <fill_style>%s</fill_style>\r\n", getXMLColorCode(shape.getFill())) + 
                String.format("    <line_width>%s</line_width>\r\n", shape.getStrokeWidth()) + 
                String.format("    <posinfo x=\"%s\" y=\"%s\" width=\"%s\" height=\"%s\" />\r\n", shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight()) + 
                "</shape>\n";
    }

    

    private static String getXMLLine(Line shape) {
        return "<shape>\r\n" + 
                "    <type>Line</type>\r\n" + 
                String.format("    <stroke_style>%s</stroke_style>\r\n", getXMLColorCode(shape.getStroke())) + 
                String.format("    <fill_style>%s</fill_style>\r\n", getXMLColorCode(shape.getFill())) + 
                String.format("    <line_width>%s</line_width>\r\n", shape.getStrokeWidth()) + 
                String.format("    <posinfo x1=\"%s\" y1=\"%s\" x2=\"%s\" y2=\"%s\" />\r\n", shape.getStartX(), shape.getStartY(), shape.getEndX(), shape.getEndY()) + 
                "</shape>\n";
    }

    public static String getJSColorCode(Paint paint) {
        String paintCode = paint.toString().toUpperCase();
        
        int r = Integer.parseInt(paintCode.substring(2, 4), 16);
        int g = Integer.parseInt(paintCode.substring(4, 6), 16);
        int b = Integer.parseInt(paintCode.substring(6, 8), 16);
        double a = (double) (Integer.parseInt(paintCode.substring(8, 10), 16)) / 255.0;
        return String.format("rgba(%s, %s, %s, %s)", r, g, b, a);
    }
    
    public static Color getColorFromString(String str) {
        String[] rgba = str.split(", ");
        int r = Integer.parseInt(rgba[0]);
        int g = Integer.parseInt(rgba[1]);
        int b = Integer.parseInt(rgba[2]);
        double a = Double.parseDouble(rgba[3]);
        
        return Color.rgb(r, g, b, a);
    }
    
    private static Object getXMLColorCode(Paint paint) {
        String paintCode = paint.toString().toUpperCase();
        
        int r = Integer.parseInt(paintCode.substring(2, 4), 16);
        int g = Integer.parseInt(paintCode.substring(4, 6), 16);
        int b = Integer.parseInt(paintCode.substring(6, 8), 16);
        double a = (double) (Integer.parseInt(paintCode.substring(8, 10), 16)) / 255.0;
        return String.format("%s, %s, %s, %s", r, g, b, a);
    }
}
