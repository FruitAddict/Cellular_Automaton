package MVCCA.View;

import MVCCA.Logic.Abstract.Resolver;
import MVCCA.Logic.Utilities.Singletons;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * RulesetPane for use with Custom Logic.
 */
public class RulesetPane extends BorderPane {
    private View view;
    private int[][] aliveRules = new int[3][3];
    private int[][] deadRules = new int[3][3];

    private NeighbourGrid aliveGrid;
    private NeighbourGrid deadGrid;


    public RulesetPane(View v){
        view = v;
        aliveGrid = new NeighbourGrid(1,2,1);
        deadGrid = new NeighbourGrid(1,2,0);

        this.setMaxWidth(v.getWidth()*v.getScale()/5);

        Label nameLabel = new Label("Ruleset Editor");
        nameLabel.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 20));

        ToggleGroup group = new ToggleGroup();

        RadioButton aliveButton = new RadioButton("Alive");
        aliveButton.setToggleGroup(group);
        aliveButton.fire();
        aliveButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 10));

        RadioButton deadButton = new RadioButton("Dead");
        deadButton.setToggleGroup(group);
        deadButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 10));



        Button applyButton = new Button("Force Apply");
        applyButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 10));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(25);
        buttonBox.getChildren().addAll(deadButton,aliveButton);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                aliveRules[i][j]=1;
                deadRules[i][j]=1;
            }
        }
        this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
        this.setTop(nameLabel);
        this.setAlignment(nameLabel,Pos.CENTER);
        this.setCenter(aliveGrid);
        this.setBottom(buttonBox);

        aliveButton.setOnAction(e->{
            this.setCenter(aliveGrid);
        });
        deadButton.setOnAction(e->{
            this.setCenter(deadGrid);
        });
        applyButton.setOnAction(e->{
            applyRules();
        });

        applyRules();
        


    }

    private class NeighbourGrid extends GridPane{

        public NeighbourGrid(int valueDead, int valueAlive, int type) {

            this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
            int value=0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int colorArrayLength = view.getController().getLogic().getColors().length;
                    ValueRectangle rec = new ValueRectangle(i,j,54,value);
                    rec.lastValue = 1;
                    value++;
                    this.add(rec,j,i);
                    rec.setOnMouseClicked(e->{
                            if(type == 0){
                                if(rec.lastValue<colorArrayLength-1) {
                                    ++rec.lastValue;
                                    rec.rec.setFill(view.getController().getLogic().getColors()[rec.lastValue]);
                                    deadRules[rec.posX][rec.posY] = rec.lastValue;
                                } else if (rec.lastValue==colorArrayLength-1) {
                                    ++rec.lastValue;
                                    deadRules[rec.posX][rec.posY] = valueDead;
                                    rec.lastValue=valueDead;
                                    rec.rec.setFill(Color.WHITE);
                                }
                            }
                            else if ( type ==1){
                                if(rec.lastValue<colorArrayLength-1) {
                                    ++rec.lastValue;
                                    rec.rec.setFill(view.getController().getLogic().getColors()[rec.lastValue]);
                                    aliveRules[rec.posX][rec.posY] = rec.lastValue;
                                } else if (rec.lastValue==colorArrayLength-1) {
                                    ++rec.lastValue;
                                    aliveRules[rec.posX][rec.posY] = valueDead;
                                    rec.lastValue=valueDead;
                                    rec.rec.setFill(Color.WHITE);
                                }
                            }
                    });
                }
            }
        }

        private class ValueRectangle extends StackPane{
            private int posX;
            private int posY;
            private Rectangle rec;
            private int lastValue;

            public ValueRectangle(int x, int y, int side, int value){
                final int[] valuesArray = {1,2,3,8,0,4,7,6,5};
                posX=x;
                posY=y;
                rec = new Rectangle(side,side);
                rec.setStroke(Color.web("827970"));
                Text tx = new Text(Integer.toString(valuesArray[value]));
                tx.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 10));
                rec.setFill(Color.WHITE);
                this.getChildren().addAll(rec,tx);

            }
        }
    }

    private void applyRules(){
        Singletons.getCustomLogic(view.getWidth(),view.getHeight()).setResolver(new Resolver() {
            @Override
            public int ifDead(int n) {
                if (n == 0) {
                    return deadRules[1][1];
                } else if (n == 1) {
                    return deadRules[0][0];
                } else if (n == 2) {
                    return deadRules[0][1];
                } else if (n == 3) {
                    return deadRules[0][2];
                } else if (n == 4) {
                    return deadRules[1][2];
                } else if (n == 5) {
                    return deadRules[2][2];
                } else if (n == 6) {
                    return deadRules[2][1];
                } else if (n == 7) {
                    return deadRules[2][0];
                } else {
                    return deadRules[1][0];
                }

            }

            @Override
            public int ifAlive(int n) {
                if (n == 0) {
                    return aliveRules[1][1];
                } else if (n == 1) {
                    return aliveRules[0][0];
                } else if (n == 2) {
                    return aliveRules[0][1];
                } else if (n == 3) {
                    return aliveRules[0][2];
                } else if (n == 4) {
                    return aliveRules[1][2];
                } else if (n == 5) {
                    return aliveRules[2][2];
                } else if (n == 6) {
                    return aliveRules[2][1];
                } else if (n == 7) {
                    return aliveRules[2][0];
                } else {
                    return aliveRules[1][0];
                }
            }
        });
    }
}
