package MVCCA.View;

import MVCCA.Logic.Abstract.Brush;
import MVCCA.Logic.Utilities.Grid;
import MVCCA.Logic.Utilities.Point;
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
import static MVCCA.Logic.Utilities.Point.*;

/**
 * Pane to hold grid for creating custom brushes.
 * hardcoded to be 5x5, can be perhaps modified easily later on
 * Works almost the same as RulesetPane
 */
public class BrushPane extends BorderPane {
    private View view;
    private int[][] brushValues = new int[5][5];
    private Point[][] positions = {
            {XY(-2,-2),XY(-1,-2),XY(0,-2),XY(1,-2),XY(2,-2)},
            {XY(-2,-1),XY(-1,-1),XY(0,-1),XY(1,-1),XY(2,-1)},
            {XY(-2,0),XY(-1,0),XY(0,0),XY(1,0),XY(2,0)},
            {XY(-2,1),XY(-1,1),XY(0,1),XY(1,1),XY(2,1)},
            {XY(-2,2),XY(-1,2),XY(0,2),XY(1,2),XY(2,2)}
    };

    public BrushPane(View v){
        view = v;
        this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
        HBox buttonBox = new HBox();
        BrushGrid grid = new BrushGrid();

        this.setMaxWidth(v.getWidth()*v.getScale()/5);

        Label nameLabel = new Label("Brush Editor");
        nameLabel.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 20));

        Button applyButton = new Button("Apply");
        applyButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 10));

        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(applyButton);

        this.setTop(nameLabel);
        this.setAlignment(nameLabel,Pos.CENTER);
        this.setCenter(grid);
        this.setBottom(buttonBox);

        applyButton.setOnAction(e->{
            applyBrush();
        });
    }

    private void applyBrush(){
        view.getController().getLogic().setBrush((g, x, y, value) -> {
            for(int i=0; i <5;i++){
                for(int j=0;j <5;j++) {
                    Point current = XY(x, y);
                    Point merged = current.merge(positions[i][j]);
                    if (brushValues[i][j]==2){
                        g.set(merged.getX(),merged.getY(),2);
                    }
                }
            }
        });
    }

    private class BrushGrid extends GridPane{
        public BrushGrid() {
            for(int i =0 ;i <3 ;i++){
                for(int j =0 ;j <3 ;j++){
                    brushValues[i][j] = 1;
                }
            }
            this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    ValueRectangle rec = new ValueRectangle(i,j,32);
                    rec.setStroke(Color.web("827970"));
                    rec.setFill(Color.WHITE);
                    this.add(rec,j,i);
                    rec.setOnMouseClicked(e->{
                        if(rec.getFill()==Color.WHITE){
                            brushValues[rec.posX][rec.posY]=2;
                            rec.setFill(Color.web("8279FF"));
                        } else{
                            brushValues[rec.posX][rec.posY]=1;
                            rec.setFill(Color.WHITE);
                        }
                    });
                }
            }
        }
        private class ValueRectangle extends Rectangle{
            private int posX;
            private int posY;
            public ValueRectangle(int posx, int posy, int side){
                super(side,side);
                this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
                posX = posx;
                posY = posy;
            }
        }
    }

}
