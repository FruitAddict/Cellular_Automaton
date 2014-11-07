package mvcca.view.utilitypanes;

import mvcca.logic.utilities.Utilities;
import mvcca.view.MainWindow;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Pane to hold grid for creating custom brushes.
 * hardcoded to be 5x5, can be perhaps modified easily later on
 * Works almost the same as RulesetPane
 */
public class BrushPane extends BorderPane {
    private MainWindow mainWindow;
    private int[][] brushValues = new int[5][5];
    private BrushGrid grid;


    public BrushPane(MainWindow v) {
        mainWindow = v;
        this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
        HBox buttonBox = new HBox();
        grid = new BrushGrid();
        update();

        this.setMaxWidth(v.getWidth() * v.getScale() / 5);

        Label nameLabel = new Label("Brush Editor");
        nameLabel.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 20));

        Button applyButton = new Button("Apply");
        applyButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 10));

        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(applyButton);

        this.setTop(nameLabel);
        setAlignment(nameLabel, Pos.CENTER);
        this.setCenter(grid);
        this.setBottom(buttonBox);

        applyButton.setOnAction(e -> {
            applyBrush();
        });
    }

    public void update() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                brushValues[i][j] = mainWindow.getController().getLogic().getBrush().data[i][j];
            }
        }
        grid.update();
    }

    private void applyBrush() {
        int[][] copy = Utilities.copy2DArray(brushValues, 5, 5);
        Utilities.applyBrush(copy, mainWindow.getController().getLogic());
    }

    private class BrushGrid extends GridPane {
        public BrushGrid() {
            this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
            update();
        }

        public void update() {
            this.getChildren().clear();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    ValueRectangle rec = new ValueRectangle(i, j, 32);
                    rec.setStroke(Color.web("827970"));
                    if (brushValues[i][j] == 1) {
                        rec.setFill(Color.WHITE);
                    } else {
                        rec.setFill(Color.web("8279FF"));
                    }
                    this.add(rec, j, i);
                    rec.setOnMouseClicked(e -> {
                        if (rec.getFill() == Color.WHITE) {
                            brushValues[rec.posX][rec.posY] = 2;
                            rec.setFill(Color.web("8279FF"));
                        } else {
                            brushValues[rec.posX][rec.posY] = 1;
                            rec.setFill(Color.WHITE);
                        }
                    });
                }
            }
        }

        private class ValueRectangle extends Rectangle {
            private int posX;
            private int posY;

            public ValueRectangle(int posx, int posy, int side) {
                super(side, side);
                this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
                posX = posx;
                posY = posy;
            }
        }
    }

}
