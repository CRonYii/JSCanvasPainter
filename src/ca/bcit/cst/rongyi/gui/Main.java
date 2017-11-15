package ca.bcit.cst.rongyi.gui;

import java.util.Optional;

import ca.bcit.cst.rongyi.painter.PaintIO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Main
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class Main extends Application {
    private final static double MIN_WINDOW_WIDTH = 900.0;
    private final static double MIN_WINDOW_HEIGHT = 700.0;
    
    private Stage mainWindow;
    private CanvasPane canvasPane;

    private int canvasWidth = 500;
    private int canvasHeight = 500;

    /**
     * @see javafx.application.Application#start(javafx.stage.Stage)
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        promptForCanvasSize();
    }

    /**
     * start up a new main canvas windows.
     * 
     * @param stage
     */
    public void startMainWindow(Stage stage) {
        if (mainWindow != null)
            mainWindow.close();
        mainWindow = stage;

        Parent root = getRootParent();
        
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setScene(new Scene(root));
        stage.setTitle("JavaScript Canvas Painter");
        stage.show();
    }

    private Parent getRootParent() {
        VBox root = new VBox();
        // VBox row 0
        // Menu Bar
        MenuBar menuBar = this.getMenuBar();

        // VBox row 1
        // Control Pane
        HBox controlPane = new HBox();
        controlPane.setPadding(new Insets(10.0));
        controlPane.setSpacing(10.0);

        // Shape Picker
        Label shapeLabel = new Label("Shape: ");
        ComboBox<String> shapePicker = this.getShapesPicker();
        // Fill Color Picker
        Label fillColorLabel = new Label("Fill Color: ");
        ColorPicker fillColorPicker = new ColorPicker(Color.web("#FFFFFF", 0.0));

        // Fill Color Picker
        Label strokeColorLabel = new Label("Stroke Color: ");
        ColorPicker strokeColorPicker = new ColorPicker(Color.BLACK);
        
        // Stroke Width Picker
        Label strokeWidthLabel = new Label("Stroke Width: ");
        Label strokeWidthValueLabel = new Label("1px");
        Slider strokeWidthPicker = new Slider(0.0, 100.0, 1.0);
        strokeWidthPicker.setBlockIncrement(1.0);
        strokeWidthPicker.valueProperty().addListener(listener -> {
            strokeWidthValueLabel.setText((int) strokeWidthPicker.getValue() + "px");
        });
        strokeWidthPicker.setShowTickMarks(true);
        
        controlPane.getChildren().addAll(shapeLabel, shapePicker, fillColorLabel, fillColorPicker, strokeColorLabel,
                strokeColorPicker, strokeWidthLabel, strokeWidthPicker, strokeWidthValueLabel);

        // VBox row 2
        // Canvas Pane
        SplitPane middleCanvasPane = new SplitPane();
        
        // Main Canvas
        ScrollPane mainCanvasPane = new ScrollPane();
        mainCanvasPane.setPadding(new Insets(5.0));
        VBox.setVgrow(middleCanvasPane, Priority.ALWAYS);

        canvasPane = new CanvasPane(canvasWidth, canvasHeight);

        mainCanvasPane.setContent(canvasPane);
        
        // Details Pane
        DetailsPane detailsPane = new DetailsPane(canvasPane);
        
        // add elements 
        middleCanvasPane.getItems().addAll(mainCanvasPane, detailsPane);
        middleCanvasPane.setDividerPositions(0.7);

        // VBox row 3
        // Status Bar
        HBox statusBar = new HBox();
        statusBar.setSpacing(5.0);

        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);

        Label cursorPositionLabel = new Label("Cursor Position: ");
        cursorPositionLabel.setMinWidth(150.0);
        Label canvasSizeLabel = new Label("Canvas size: " + canvasWidth + " x " + canvasHeight + "px");
        canvasSizeLabel.setMinWidth(100.0);

        statusBar.getChildren().addAll(cursorPositionLabel, separator1, canvasSizeLabel, separator2);

        // add elements to VBox
        root.getChildren().addAll(menuBar, controlPane, middleCanvasPane, statusBar);
        root.setPrefSize(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);

        canvasPane.setCursorPositionLabel(cursorPositionLabel);
        canvasPane.setFillColorPicker(fillColorPicker);
        canvasPane.setStrokeColorPicker(strokeColorPicker);
        canvasPane.setStrokeWidthPicker(strokeWidthPicker);
        canvasPane.setShapePicker(shapePicker);
        
        return root;
    }

    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Menu Tab - File
        Menu fileMenu = new Menu("File");

        // Menu Item for menu tab - File
        MenuItem newCanvasMenuItem = new MenuItem("New Canvas");
        newCanvasMenuItem.setOnAction(this::promptForCanvasSize);

        MenuItem generateJSMenuItem = new MenuItem("Generate JavaScript");
        generateJSMenuItem.setOnAction(this::generateJS);

        // Add items to menu tab - File
        fileMenu.getItems().addAll(newCanvasMenuItem, generateJSMenuItem);

        // Add tabs to menu bar
        menuBar.getMenus().addAll(fileMenu);

        return menuBar;
    }

    private ComboBox<String> getShapesPicker() {
        ObservableList<String> options = FXCollections.observableArrayList();
        ComboBox<String> shapesPicker = new ComboBox<>(options);

        options.addAll("Line", "Rectangle");

        shapesPicker.getSelectionModel().selectFirst();

        return shapesPicker;
    }

    private void generateJS(ActionEvent event) {
        PaintIO.saveAsHTML(canvasPane.getPainter());
    }

    /**
     * For Button to work.
     * 
     * @param event
     *            action event
     */
    private void promptForCanvasSize(ActionEvent event) {
        promptForCanvasSize();
    }

    /**
     * Prompt a dialog to ask for size of canvas, a new canvas window will start
     * after submit.
     */
    private void promptForCanvasSize() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Set Canvas Szie");
        dialog.setHeaderText("Please enter the size of the canvas\nThe canvas is 500 x 500 by default");

        GridPane pane = new GridPane();
        pane.setVgap(5.0);

        TextField widthInput = new TextField("500");
        TextField heightInput = new TextField("500");

        pane.add(new Label("Width: "), 0, 0);
        pane.add(widthInput, 1, 0);
        pane.add(new Label("Height: "), 0, 1);
        pane.add(heightInput, 1, 1);

        ButtonType okButtonType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
        Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            String width = widthInput.getText();
            String height = heightInput.getText();
            if (!width.matches("^-?\\d+$") || !height.matches("^-?\\d+$")) {
                event.consume();
            }
        });

        dialog.getDialogPane().setContent(pane);

        Platform.runLater(() -> widthInput.requestFocus());

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return new Pair<>(widthInput.getText(), heightInput.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            this.canvasWidth = Integer.parseInt(pair.getKey());
            this.canvasHeight = Integer.parseInt(pair.getValue());

            try {
                this.startMainWindow(new Stage());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    /**
     * Prompt a dialog to ask for size of canvas, a new canvas window will start
     * after submit.
     */
    private void promptForFileName() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Enter the name of the html");
        dialog.setHeaderText("Please enter the file name for the HTML file");

        GridPane pane = new GridPane();
        pane.setVgap(5.0);

        TextField nameInput = new TextField("canvas");

        pane.add(new Label("Width: "), 0, 0);
        pane.add(nameInput, 1, 0);

        ButtonType okButtonType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().add(okButtonType);
//        Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
//        btOk.addEventFilter(ActionEvent.ACTION, event -> {
//            String width = nameInput.getText();
//            if (!width.matches("^-?\\d+$")) {
//                event.consume();
//            }
//        });

        dialog.getDialogPane().setContent(pane);

        Platform.runLater(() -> nameInput.requestFocus());

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return nameInput.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String name = result.get();
            try {
                this.startMainWindow(new Stage());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Drives the program.
     * 
     * @param args
     *            unused
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

}
