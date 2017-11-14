package ca.bcit.cst.rongyi.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Callback;

/**
 * DetailsPane
 *
 * @author Rongyi Chen
 * @version 2017
 */
public class DetailsPane extends SplitPane {

    private CanvasPane canvasPane;

    private ListView<Shape> elementListView;
    private ObservableList<Shape> shapeList;

    private Label posLabel;
    private Label typeLabel;

    private ColorPicker fillPicker;
    private ColorPicker strokePicker;

    private Slider strokeWidthPicker;

    /**
     * Constructs an object of type DetailsPane.
     */
    public DetailsPane(CanvasPane canvasPane) {
        this.canvasPane = canvasPane;
        this.getItems().addAll(getDetailsPane(), getElementsView());

        this.setOrientation(Orientation.VERTICAL);
        this.setDividerPositions(0.5);
    }

    private VBox getDetailsPane() {
        VBox detailsPane = new VBox();
        detailsPane.setPadding(new Insets(10.0));
        detailsPane.setSpacing(10.0);

        HBox buttonGroup = new HBox();
        buttonGroup.setSpacing(10.0);

        Button applyButton = new Button("Apply");
        applyButton.setOnAction(event -> {
            Shape shape = elementListView.getSelectionModel().getSelectedItem();
            shape.setFill(fillPicker.getValue());
            shape.setStroke(strokePicker.getValue());
            shape.setStrokeWidth((int) strokeWidthPicker.getValue());

            canvasPane.getPainter().paint();
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Shape shape = elementListView.getSelectionModel().getSelectedItem();
            shapeList.remove(shape);
        });

        buttonGroup.getChildren().addAll(applyButton, deleteButton);

        typeLabel = new Label("Type: ");
        posLabel = new Label("Position: ");

        Label fillLabel = new Label("Fill Color: ");
        fillPicker = new ColorPicker();
        Label strokeLabel = new Label("Stroke Color: ");
        strokePicker = new ColorPicker();

        // Stroke Width Picker
        Label strokeWidthLabel = new Label("Stroke Width: ");
        Label strokeWidthValueLabel = new Label("1px");
        strokeWidthPicker = new Slider(0.0, 100.0, 1.0);
        strokeWidthPicker.setBlockIncrement(1.0);
        strokeWidthPicker.valueProperty().addListener(listener -> {
            strokeWidthValueLabel.setText((int) strokeWidthPicker.getValue() + "px");
        });
        strokeWidthPicker.setShowTickMarks(true);

        HBox strokeWidthSetter = new HBox(strokeWidthLabel, strokeWidthPicker, strokeWidthValueLabel);

        Separator separator1 = new Separator();
        Separator separator2 = new Separator();
        Separator separator3 = new Separator();

        detailsPane.getChildren().addAll(buttonGroup, separator1, typeLabel, posLabel, separator2, fillLabel,
                fillPicker, strokeLabel, strokePicker, separator3, strokeWidthSetter);

        return detailsPane;
    }

    private ListView<Shape> getElementsView() {
        shapeList = canvasPane.getPainter().getShapeList();

        elementListView = new ListView<>(shapeList);
        elementListView.setCellFactory(new Callback<ListView<Shape>, ListCell<Shape>>() {
            @Override
            public ListCell<Shape> call(ListView<Shape> list) {
                return new ShapeElementCell();
            }
        });
        
        elementListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Shape>() {
            @Override
            public void changed(ObservableValue<? extends Shape> observable, Shape oldValue, Shape newValue) {
                Shape shape = elementListView.getSelectionModel().getSelectedItem();
                if (shape == null)
                    return;

                String type = "";
                String pos = "";
                if (shape instanceof Line) {
                    Line line = (Line) shape;
                    type = "Line";
                    pos = "Start Point: " + line.getStartX() + " ," + line.getStartY() + "\nEnd Point: "
                            + line.getEndX() + ", " + line.getEndY();
                } else if (shape instanceof Rectangle) {
                    Rectangle rect = (Rectangle) shape;
                    type = "Rectangle";
                    pos = "Position: " + rect.getX() + ", " + rect.getY() + "\nSize: " + rect.getWidth() + " x "
                            + rect.getHeight();
                }
                typeLabel.setText("Type: " + type);
                posLabel.setText(pos);
                fillPicker.setValue((Color) shape.getFill());
                strokePicker.setValue((Color) shape.getStroke());
                strokeWidthPicker.setValue(shape.getStrokeWidth());
            }
        });

        return elementListView;
    }

    static HBox getItemDisplay(Shape shape) {
        HBox displayBox = new HBox();
        displayBox.setSpacing(10.0);
        
        WritableImage snapshot = new WritableImage(35, 35);
        snapshot = shape.snapshot(new SnapshotParameters(), snapshot);
        
        String type = "Unnamed";
        if (shape instanceof Line) {
            type = "Line";
        } else if (shape instanceof Rectangle) {
            type = "Rectangle";
        }
        Label name = new Label(type);
        
        displayBox.getChildren().addAll(new ImageView(snapshot), name);
        
        return displayBox;
    }
    
    static class ShapeElementCell extends ListCell<Shape> {
        @Override
        public void updateItem(Shape item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setGraphic(getItemDisplay(item));
            } else {
                setGraphic(null);
            }
        }
    }

}
