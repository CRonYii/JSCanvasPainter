package ca.bcit.cst.rongyi.painter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * PaintIO
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class PaintIO {

    public static void saveAsHTML(File file, Painter painter) {
        String header = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "    <head>\r\n" + "        <meta charset=\"UTF-8\">\r\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + "        <title>" + file.getName() + "</title>\r\n" + "        <script>\n"
                + "            function draw() {\n"
                + "               var canvas = document.getElementById(\"canvas\");\r\n"
                + "               var context = canvas.getContext(\"2d\");";
        String footer = "            }\n" + "</script>\r\n" + "    </head>\r\n" + "    <body onLoad=\"draw()\">\r\n"
                + String.format("        <canvas id=\"canvas\" width=\"%s\" height=\"%s\">\r\n",
                        painter.getCanvasWidth(), painter.getCanvasHeight())
                + "            \r\n" + "        </canvas>\r\n" + "    </body>\r\n" + "</html>";
        String js = CodeTranslator.generateJS(painter.getShapeList());
        String html = header + js + footer;

        FileOutputStream outputStream;
        try {
            byte[] strToBytes = html.getBytes();
            outputStream = new FileOutputStream(file);
            outputStream.write(strToBytes);

            outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static PaintData readFromFile(File file) {
        ObservableList<Shape> shapeList = FXCollections.observableArrayList();
        int canvasWidth = 0;
        int canvasHeight = 0;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();
            
            canvasWidth = Integer.parseInt(doc.getDocumentElement().getAttribute("width"));
            canvasHeight = Integer.parseInt(doc.getDocumentElement().getAttribute("height"));

            NodeList nList = doc.getElementsByTagName("shape");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                    Color storkeStyle = CodeTranslator
                            .getColorFromString(eElement.getElementsByTagName("stroke_style").item(0).getTextContent());
                    Color fillStyle = CodeTranslator
                            .getColorFromString(eElement.getElementsByTagName("fill_style").item(0).getTextContent());
                    double lineWidth = Double
                            .parseDouble(eElement.getElementsByTagName("line_width").item(0).getTextContent());
                    Element posinfo = (Element) eElement.getElementsByTagName("posinfo").item(0);

                    Shape shape = null;

                    if (type.equals("Rectangle")) {
                        double x = Double.parseDouble(posinfo.getAttribute("x"));
                        double y = Double.parseDouble(posinfo.getAttribute("y"));
                        double width = Double.parseDouble(posinfo.getAttribute("width"));
                        double height = Double.parseDouble(posinfo.getAttribute("height"));

                        shape = new Rectangle(x, y, width, height);
                    } else if (type.equals("Line")) {
                        double x1 = Double.parseDouble(posinfo.getAttribute("x1"));
                        double y1 = Double.parseDouble(posinfo.getAttribute("y1"));
                        double x2 = Double.parseDouble(posinfo.getAttribute("x2"));
                        double y2 = Double.parseDouble(posinfo.getAttribute("y2"));
                        
                        shape = new Line(x1, y1, x2, y2);
                    }

                    shape.setStroke(storkeStyle);
                    shape.setFill(fillStyle);
                    shape.setStrokeWidth(lineWidth);

                    shapeList.add(shape);
                }
            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new PaintData(shapeList, canvasWidth, canvasHeight);
    }

    public static void saveAsXML(File file, Painter painter) {
        String xml = "<?xml version=\"1.0\"?>\n";
        xml += String.format("<paint width=\"%s\" height=\"%s\">\n", painter.getCanvasWidth(), painter.getCanvasHeight());
        
        xml += CodeTranslator.generateXML(painter.getShapeList());
        
        xml += "</paint>";

        FileOutputStream outputStream;
        try {
            byte[] strToBytes = xml.getBytes();
            outputStream = new FileOutputStream(file);
            outputStream.write(strToBytes);

            outputStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static class PaintData {
        
        private ObservableList<Shape> shapeList;
        private int width;
        private int height;

        public PaintData(ObservableList<Shape> shapeList, int width, int height) {
            super();
            this.shapeList = shapeList;
            this.width = width;
            this.height = height;
        }

        /**
         * Returns the shapeList for this PaintIO.PaintData.
         * 
         * @return the shapeList
         */
        public ObservableList<Shape> getShapeList() {
            return shapeList;
        }

        /**
         * Returns the width for this PaintIO.PaintData.
         * 
         * @return the width
         */
        public int getWidth() {
            return width;
        }

        /**
         * Returns the height for this PaintIO.PaintData.
         * 
         * @return the height
         */
        public int getHeight() {
            return height;
        }
        
    }
}
