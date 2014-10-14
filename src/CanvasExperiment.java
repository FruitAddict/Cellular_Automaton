import com.sun.javaws.jnl.JavaFXAppDesc;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;


/**
 * Created by FruitAddict on 2014-10-14.
 */
public class CanvasExperiment extends Application {
    int[][] currentstate;
    Label genLabel;
    int sleepTime=50;

    boolean running=true;

    public static Color[] cs = {Color.WHITE, Color.RED, Color.BLACK};

    Canvas c;

    private static final int width = 200;
    private static final int height = 200;
    private static double scale = 4.5;

    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        HBox box = new HBox();
        c = new Canvas(width, height);
        c.toFront();
        c.setScaleX(scale);
        c.setScaleY(scale);
        currentstate = new int[width][height];

        clear();

        c.setOnMousePressed(e -> {

            int x = (int) e.getX();
            int y = (int) e.getY();
            currentstate[x][y] = 1;
            redraw();

        });

        c.setOnMouseDragged(e -> {
            int x = (int) e.getX();
            int y = (int) e.getY();
            currentstate[x][y] = 1;
            redraw();
        });

        genLabel = new Label();
        Button clearButton = new Button("Clear");
        Button stopButton = new Button("Stop");
        Button advButton = new Button("Gen adv");

        box.setSpacing(10);
        box.getChildren().addAll(genLabel, clearButton, stopButton, advButton);
        box.setAlignment(Pos.CENTER);
        box.toFront();

        mainPane.setCenter(c);

        ScrollBar sli = new ScrollBar();
        sli.setMin(10);
        sli.setMax(500);
        sli.setValue(50);

        ScrollBar zoom = new ScrollBar();
        zoom.setMin(1);
        zoom.setMax(10);
        zoom.setValue(4.5);
        zoom.setOrientation(Orientation.VERTICAL);


        mainPane.setBottom(box);
        mainPane.setTop(sli);
        mainPane.setRight(zoom);

        primaryStage.setScene(new Scene(mainPane, (width * scale)+50, (height * scale)+50));
        primaryStage.setTitle("WLACZ SIE PROSZEE");
        primaryStage.show();

        Ticker tick = new Ticker();
        Thread t = new Thread(tick);
        t.start();

        clearButton.setOnAction(e->clear());
        stopButton.setOnAction(e->{
            running = !running;
            System.out.println(running);
            if(stopButton.getText().equals("Stop")){
                stopButton.setText("Play");
            } else {
                stopButton.setText("Stop");
            }
        });
        advButton.setOnAction(e->{
            tick.genAdvance();
        });


        sli.valueProperty().addListener(e->{
            sleepTime=(int)sli.getValue();
        });

        zoom.valueProperty().addListener(e->{
            scale=zoom.getValue();
            c.setScaleX(scale);
            c.setScaleY(scale);
        });



    }

    private void clear() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentstate[i][j] = 0;
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j < 3) {
                    currentstate[i][j] = 2;
                } else if ((j > 2 || j < height - 3) && (i < 3 || i >= width - 3)) {
                    currentstate[i][j] = 2;
                } else if (j >= height - 3) {
                    currentstate[i][j] = 2;
                }
            }
        }
        redraw();
    }

    private void redraw() {
        GraphicsContext gc = c.getGraphicsContext2D();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gc.getPixelWriter().setColor(i, j, cs[currentstate[i][j]]);
            }
        }
    }

    private class Ticker implements Runnable {
        public long gen;
        public void run() {
            while (true) {
                if(running) {
                    genAdvance();
                }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (Exception ex) {
                    }
                    Platform.runLater(() -> {
                        redraw();
                        genLabel.setText("Generation: "+gen);
                    });

            }
        }

        private void genAdvance(){


                int[][] snapshot = copyArray(currentstate);
                gen++;
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        currentstate[i][j] = resolve(i, j, currentstate[i][j], snapshot);
                    }
                }

        }

        private int[][] copyArray(int[][] src){
            int[][] result = new int[width][height];
            for(int i=0;i<width;i++){
                for(int j=0;j<height;j++){
                    result[i][j]=src[i][j];
                }
            }
            return result;
        }

        private int resolve(int x, int y, int curst, int[][] snapshot) {
            if (curst != 2) {
                int numOfNeighbours = 0;
                if (snapshot[x - 1][y - 1] == 1) {
                    numOfNeighbours++;
                }
                if (snapshot[x][y - 1] == 1) {
                    numOfNeighbours++;
                }
                if (snapshot[x + 1][y - 1] == 1) {
                    numOfNeighbours++;
                }
                if (snapshot[x - 1][y] == 1) {
                    numOfNeighbours++;
                }
                if (snapshot[x + 1][y] == 1) {
                    numOfNeighbours++;
                }
                if (snapshot[x - 1][y + 1] == 1) {
                    numOfNeighbours++;
                }
                if (snapshot[x][y + 1] == 1) {
                    numOfNeighbours++;
                }
                if (snapshot[x + 1][y + 1] == 1) {
                    numOfNeighbours++;
                }

                if (curst == 0) {
                    if (numOfNeighbours == 3) {
                        return 1;
                    } else {
                        return 0;
                    }

                }
                if (curst == 1) {
                    if (numOfNeighbours < 2) {
                        return 0;
                    }
                    else if (numOfNeighbours == 2 || numOfNeighbours == 3) {
                        return 1;
                    }
                    else if (numOfNeighbours > 3) {
                        return 0;
                    }
                }
                System.out.println("you dun fucked up m8");
                return 1;
            } else {
                return 2;
            }


        }


    }
}
