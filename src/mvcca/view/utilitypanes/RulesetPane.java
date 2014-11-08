package mvcca.view.utilitypanes;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mvcca.logic.abstracted.Resolver;
import mvcca.logic.utilities.Singletons;
import mvcca.view.MainWindow;
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

import java.io.*;


/**
 * RulesetPane for use with Custom logic.
 */
public class RulesetPane extends BorderPane {
    private MainWindow mainWindow;
    private int[][] aliveRules = new int[3][3];
    private int[][] deadRules = new int[3][3];

    private NeighbourGrid aliveGrid;
    private NeighbourGrid deadGrid;


    public RulesetPane(MainWindow v) {
        mainWindow = v;
        aliveGrid = new NeighbourGrid(1, 2, 1);
        deadGrid = new NeighbourGrid(1, 2, 0);

        this.setMaxWidth(v.getWidth() * v.getScale() / 5);

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

        Button saveButton = new Button("Save");
        saveButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 10));

        Button loadButton = new Button("Load");
        loadButton.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 10));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(1);
        buttonBox.getChildren().addAll(deadButton,saveButton,loadButton, aliveButton);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                aliveRules[i][j] = 1;
                deadRules[i][j] = 1;
            }
        }
        this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
        this.setTop(nameLabel);
        setAlignment(nameLabel, Pos.CENTER);
        this.setCenter(aliveGrid);
        this.setBottom(buttonBox);

        aliveButton.setOnAction(e -> {
            this.setCenter(aliveGrid);
        });
        deadButton.setOnAction(e -> {
            this.setCenter(deadGrid);
        });
        applyButton.setOnAction(e -> {
            applyRules();
        });
        saveButton.setOnAction(e-> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("RULESET (*.rlst)", "*.rlst"));
            File file = fileChooser.showSaveDialog(v.getPrimaryStage());
            if (file != null) {
                try (
                        ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))
                ) {
                    output.writeObject(aliveRules);
                    output.writeObject(deadRules);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        loadButton.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("RULESET (*.rlst)", "*.rlst"));
            File file = fileChooser.showOpenDialog(v.getPrimaryStage());
            if(file!=null){
                try(
                        ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))
                        ){
                    aliveRules = (int[][])input.readObject();
                    deadRules = (int[][])input.readObject();

                }catch(IOException | ClassNotFoundException ex){
                    ex.printStackTrace();
                }
            }
            aliveGrid.update();
            deadGrid.update();
        });



        applyRules();


    }

    private class NeighbourGrid extends GridPane {
        int colorArrayLength = Singletons.getCustomLogic(mainWindow.getWidth(), mainWindow.getHeight()).getColors().length;
        int valueDead;
        int valueAlive;
        int type;

        public NeighbourGrid(int valueDead, int valueAlive, int type) {
            this.valueAlive=valueAlive;
            this.valueDead=valueDead;
            this.type = type;
            this.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
            update();
        }

        public void update(){
            this.getChildren().clear();
            int value = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ValueRectangle rec = new ValueRectangle(i, j, 54, value);
                    if(type == 0){
                        if(deadRules[i][j] <=1){
                            rec.rec.setFill(Color.WHITE);
                            rec.lastValue = valueDead;
                        } else {
                            rec.lastValue = deadRules[i][j];
                            rec.rec.setFill(Singletons.getCustomLogic(mainWindow.getWidth(), mainWindow.getHeight()).getColors()[rec.lastValue]);
                        }

                    } else if ( type == 1){
                        if(aliveRules[i][j] <=1){
                            rec.rec.setFill(Color.WHITE);
                            rec.lastValue = valueDead;
                        } else {
                            rec.lastValue = aliveRules[i][j];
                            rec.rec.setFill(Singletons.getCustomLogic(mainWindow.getWidth(), mainWindow.getHeight()).getColors()[rec.lastValue]);
                        }
                    }
                    value++;
                    this.add(rec, j, i);
                    rec.setOnMouseClicked(e -> {
                        if (type == 0) {
                            if (rec.lastValue < colorArrayLength - 1) {
                                ++rec.lastValue;
                                rec.rec.setFill(Singletons.getCustomLogic(mainWindow.getWidth(), mainWindow.getHeight()).getColors()[rec.lastValue]);
                                deadRules[rec.posX][rec.posY] = rec.lastValue;
                            } else if (rec.lastValue == colorArrayLength - 1) {
                                ++rec.lastValue;
                                deadRules[rec.posX][rec.posY] = valueDead;
                                rec.lastValue = valueDead;
                                rec.rec.setFill(Color.WHITE);
                            }
                        } else if (type == 1) {
                            if (rec.lastValue < colorArrayLength - 1) {
                                ++rec.lastValue;
                                rec.rec.setFill(Singletons.getCustomLogic(mainWindow.getWidth(), mainWindow.getHeight()).getColors()[rec.lastValue]);
                                aliveRules[rec.posX][rec.posY] = rec.lastValue;
                            } else if (rec.lastValue == colorArrayLength - 1) {
                                ++rec.lastValue;
                                aliveRules[rec.posX][rec.posY] = valueDead;
                                rec.lastValue = valueDead;
                                rec.rec.setFill(Color.WHITE);
                            }
                        }
                    });
                }
            }
        }

        private class ValueRectangle extends StackPane {
            private int posX;
            private int posY;
            private Rectangle rec;
            private int lastValue;

            public ValueRectangle(int x, int y, int side, int value) {
                final int[] valuesArray = {1, 2, 3, 8, 0, 4, 7, 6, 5};
                posX = x;
                posY = y;
                rec = new Rectangle(side, side);
                rec.setStroke(Color.web("827970"));
                Text tx = new Text(Integer.toString(valuesArray[value]));
                tx.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 10));
                rec.setFill(Color.WHITE);
                this.getChildren().addAll(rec, tx);

            }
        }
    }

    private void applyRules() {
        Singletons.getCustomLogic(mainWindow.getWidth(), mainWindow.getHeight()).setResolver(new Resolver() {
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
