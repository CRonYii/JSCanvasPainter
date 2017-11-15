package ca.bcit.cst.rongyi.painter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * PaintIO
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class PaintIO {
    
    public static void saveAsHTML(Painter painter) {
        String header = 
                "<!DOCTYPE html>\r\n" + 
                "<html>\r\n" + 
                "    <head>\r\n" + 
                "        <meta charset=\"UTF-8\">\r\n" + 
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
                "        <title>Canvas Car</title>\r\n" + 
                "        <script>\n" +
                "            function draw() {\n" +
                "               var canvas = document.getElementById(\"canvas\");\r\n" + 
                "               var context = canvas.getContext(\"2d\");";
        String footer = 
                "            }"
                + "</script>\r\n" + 
                "    </head>\r\n" + 
                "    <body onLoad=\"draw()\">\r\n" + 
                String.format("        <canvas id=\"canvas\" width=\"%s\" height=\"%s\">\r\n", painter.getCanvasWidth(), painter.getCanvasHeight()) + 
                "            \r\n" + 
                "        </canvas>\r\n" + 
                "    </body>\r\n" + 
                "</html>";
        String js = JSGenerator.generateJS(painter.getShapeList());
        String html = header + js + footer;
        
        FileOutputStream outputStream;
        try {
            byte[] strToBytes = html.getBytes();
            outputStream = new FileOutputStream("html/canvas.html");
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
    
}
