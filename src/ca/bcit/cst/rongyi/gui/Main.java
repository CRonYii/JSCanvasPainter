package ca.bcit.cst.rongyi.gui;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Main
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class Main extends Application {
    private Stage mainWindow;

    private int width = 500;
    private int height = 500;

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

        VBox root = new VBox();
        // VBox row 0
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");

        menuBar.getMenus().addAll(fileMenu);

        // VBox row 1
        // Main Pane
        SplitPane mainPane = new SplitPane();
        VBox.setVgrow(mainPane, Priority.ALWAYS);
        
        // Canvas Pane
        ScrollPane mainCanvasPane = new ScrollPane();
        CanvasPane canvasPane = new CanvasPane(width, height);
        mainCanvasPane.setPadding(new Insets(5.0));
        mainCanvasPane.setContent(canvasPane);

        // Control Pane
        GridPane controlPane = new GridPane();
        Button newButton = new Button("New Canvas");
        newButton.setOnAction(this::promptForCanvasSize);
        controlPane.add(newButton, 0, 0);

        // Details Pane
        GridPane detailsPane = new GridPane();

        // add elements to main pane (SplitPane)
        mainPane.getItems().addAll(controlPane, mainCanvasPane, detailsPane);
        mainPane.setDividerPositions(0.2, 0.8);

        // VBox row 2
        HBox statusBar = new HBox();

        Label status = new Label("Ready");
        statusBar.getChildren().add(status);

        // add elements to VBox
        root.getChildren().addAll(menuBar, mainPane, statusBar);
        root.setPrefSize(1000.0, 600.0);

        stage.setScene(new Scene(root));
        stage.setTitle("JavaScript Canvas Painter");
        stage.show();
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
            this.width = Integer.parseInt(pair.getKey());
            this.height = Integer.parseInt(pair.getValue());

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
