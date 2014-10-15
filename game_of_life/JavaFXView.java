package MVCCA;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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

import javax.security.auth.login.Configuration;


/**
 * REQUIRES JRE 8_20u
 */
public class JavaFXView extends Application {

    //controller
    Controller controller;

    //Stage and config to launch app from diff code
    private Stage primaryStage;
    Configuration configuration;

    //Label to hold current generation number
    Label genLabel;

    //Color array to use with rendering based on int matrix
    public static Color[] colorsArray = {Color.WHITE, Color.RED, Color.BLACK};

    //canvas to draw pixels representing grid on
    Canvas c;

    //matrix of ints to hold data to draw
    int[][] drawMatrix;

    //speed between generations
    int timeValue;

    //Width, Height and scale of canvas
    private static final int width = 200;
    private static final int height = 200;
    private static double scale = 4.5;

    public JavaFXView(){

    }
    public void start(Stage primaryStage){
        //BorderPane as the main Pane of the visualizer and GUI stuff inits
        BorderPane mainPane = new BorderPane();
        genLabel = new Label("");
        c = new Canvas(width,height);
        c.setScaleX(scale);
        c.setScaleY(scale);

        /*Buttons:
         *clearButton- Resets the canvas to its original state (all white)
         * playButton - stops/resumes progression of the generations
         * advGenButton - manually advances the generation of the cells by one
         */
        Button clearButton = new Button("Clear");
        Button playButton = new Button("Stop");
        Button advGenButton = new Button("Advance Generation");

        /* buttonBox holds the buttons at the bottom of the screen in horizontal alignment
         * speedBar changes the time between new generations are drawn to the sreen
         * zoomBar controls the zoom (borderpane prevents from moving canvas around so its centered)
         * genLabel shows the current generation obtained from the logic
         */
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(5); //spacing between buttons in this box
        buttonBox.getChildren().addAll(genLabel,clearButton,playButton,advGenButton); //adding buttons to the box

        ScrollBar speedBar = new ScrollBar();
        speedBar.setMin(10); //minimum 10ms
        speedBar.setMax(1000); //max 1 second
        speedBar.setValue(50); //initial value

        ScrollBar zoomBar = new ScrollBar();
        zoomBar.setMin(1);
        zoomBar.setMax(10);
        zoomBar.setValue(scale);
        zoomBar.setOrientation(Orientation.VERTICAL);

        //placing the gui components into main pane
        mainPane.setCenter(c);
        mainPane.setBottom(buttonBox);
        mainPane.setTop(speedBar);
        mainPane.setRight(zoomBar);

        //Initalizing the Stage and creating a Scene for the main pane
        primaryStage.setScene(new Scene(mainPane,(width*scale)+50,(height*scale)+50)); //the window size with addi
        primaryStage.show();                                                           //tional 50px for gui stuff

        //setting up event handlers
        clearButton.setOnAction(e->controller.clear());

        playButton.setOnAction(e->{
            controller.pause();
        });

        advGenButton.setOnAction(e->{
            controller.advGen();
        });

        c.setOnMouseDragged(e->{
            controller.setCell((int)e.getX(),(int)e.getY());
        });
    }

    public void setDrawMatrix(int[][] matrix){
        drawMatrix=matrix;
        redraw();
    }

    private void redraw() {
        /* This method obtains the pixelwriter for the canvas and draws pixels to the screen
         * with respect to the data stored in drawMatrix
         */
        GraphicsContext gc = c.getGraphicsContext2D();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gc.getPixelWriter().setColor(i, j, colorsArray[drawMatrix[i][j]]);
            }
        }
    }

    public void setGeneration(int gen){
        genLabel.setText("Generation: "+gen);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public void setController(Controller c){
        controller=c;
    }

}
