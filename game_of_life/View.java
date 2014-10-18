package MVCCA;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.security.auth.login.Configuration;


/**
 * REQUIRES JRE 8_20u
 */
public class View extends Application {


    Controller controller;
    Label genLabel;
    Canvas c;
    MenuBar mainBar;
    int[][] drawMatrix;
    public static Color[] colorsArray;
    private static final int width = 200;
    private static final int height = 200;
    private static double scale = 4.5;

    public void start(Stage primaryStage){
        //BorderPane as the main Pane of the visualizer and GUI stuff inits, Pane to store canvas
        BorderPane mainPane = new BorderPane();
        Pane drawStore = new Pane();
        genLabel = new Label("");
        c = new Canvas(width,height);
        c.setScaleX(scale);
        c.setScaleY(scale);
        c.setTranslateX(width*scale/2);
        c.setTranslateY(height*scale/2);
        drawStore.getChildren().add(c);

        /**
         * Buttons:
         * clearButton- Resets the canvas to its original state (all white)
         * playButton - stops/resumes progression of the generations
         * advGenButton - manually advances the generation of the cells by one
         */
        Button clearButton = new Button("Clear");
        Button playButton = new Button("Stop");
        Button advGenButton = new Button("Advance Generation");

        /**
         * buttonBox holds the buttons at the bottom of the screen in horizontal alignment
         * speedBar changes the time between new generations are drawn to the sreen
         * zoomBar controls the zoom (borderpane prevents from moving canvas around so its centered)
         * Separate HBoxes to hold the bars together with info labels
         * genLabel shows the current generation obtained from the logic
         */
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(5); //spacing between buttons in this box
        buttonBox.getChildren().addAll(genLabel,clearButton,playButton,advGenButton); //adding buttons to the box

        /* ScrollBar speedBar = new ScrollBar();
        speedBar.setMin(10); //minimum 10ms
        speedBar.setMax(1000); //max 1 second
        speedBar.setValue(50); //init value */

        ScrollBar zoomBar = new ScrollBar();
        zoomBar.setMin(1);
        zoomBar.setMax(10);
        zoomBar.setValue(scale);
        zoomBar.setOrientation(Orientation.VERTICAL);

        //placing the gui components into main pane
        mainPane.setCenter(drawStore);
        mainPane.setBottom(buttonBox);

        /**
         * Menus and handlers for menu items
         * 1-thread allowing in progress
         */
        mainBar = new MenuBar();
        Menu menuView = new Menu("View");

        MenuItem speedOption = new MenuItem("Time Between Generations");
        speedOption.setOnAction(e->{
            createNumberChoiceStage();
        });
        menuView.getItems().add(speedOption);

        mainBar.getMenus().add(menuView);

        //Initalizing the Stage and creating a Scene for the main pane
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setOnCloseRequest(e-> System.exit(0));
        Scene primaryScene = new Scene(mainPane,(width*scale)+50,(height*scale)+50);
        ((BorderPane)primaryScene.getRoot()).setTop(mainBar);
        primaryStage.setScene(primaryScene);
        primaryStage.show();

        //debug
        System.out.println(c.getTranslateX()+" "+ c.getTranslateY());

        //setting up event handlers
        clearButton.setOnAction(e->controller.clear());

        playButton.setOnAction(e->{
            controller.pause();
            if(playButton.getText().equals("Stop")){
                playButton.setText("Play");
            } else {
                playButton.setText("Stop");
            }
        });

        advGenButton.setOnAction(e->{
            controller.advGen();
        });

        // cell drawing handler && moving the pane around
        c.setOnMouseDragged(e->{
            controller.setCell((int) e.getX(), (int) e.getY());
        });

        drawStore.setOnMouseDragged(e -> {
            if (e.isControlDown()) {
                c.setLayoutX(e.getX());
                c.setLayoutY(e.getY());
            }
        });

        //time speed handler
        /*speedBar.valueProperty().addListener(e -> {
            controller.setSleepTime((int) speedBar.getValue());
        });


        zoomBar.valueProperty().addListener(e -> {
            rescale(zoomBar.getValue());
        });
        //translating the canvas (useful when zoomed in)
        mainPane.setOnKeyPressed(e -> {
            if (e.isControlDown()) {
                switch (e.getCode()) {
                    case LEFT: {
                        c.setTranslateX(c.getTranslateX() - 10);
                    }
                    case RIGHT: {
                        c.setTranslateX(c.getTranslateX() + 10);
                    }
                    case UP: {
                        c.setTranslateY(c.getTranslateY() - 10);
                    }
                    case DOWN: {
                        c.setTranslateY(c.getTranslateY() + 10);
                    }
                }

            }
        });*/

    }

    public void setDrawMatrix(int[][] matrix){
        drawMatrix=matrix;
        redraw();
    }

    private void redraw() {
        /**
         * This method obtains the pixelwriter for the canvas and draws pixels to the screen
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

    public void rescale(double scale){
        this.scale=scale;
        c.setScaleX(scale);
        c.setScaleY(scale);
    }

    public void setColorsArray(Color[] colorArray){
        colorsArray = colorArray;
    }

    public void createNumberChoiceStage(){
        mainBar.setDisable(true);
        Stage n = new Stage();
        n.setResizable(false);
        n.setTitle("Time between generations.");
        n.setScene(new Scene(new NumberChoice(10,1000)));
        n.show();
        n.setOnCloseRequest(e->{
            mainBar.setDisable(false);
        });
    }

    private class NumberChoice extends Pane{
        /**
         * Widget pane to initially control time between generations
         * Will be reused later. Made to avoid too much code in lambda (all of this is
         * too confusing already anyway)
         */
        int minValue;
        int maxValue;

        public NumberChoice(int min, int max){
            this.minValue=min;
            this.maxValue=max;

            Button incrementButton = new Button("Increment");
            Button decrementBUtton = new Button("Decrement");
            Label currentValue = new Label(Integer.toString(controller.getSleepTime()));
            currentValue.setPrefWidth(100);

            HBox contentBox = new HBox();
            contentBox.setAlignment(Pos.CENTER);
            contentBox.getChildren().addAll(decrementBUtton,currentValue,incrementButton);
            this.getChildren().add(contentBox);
        }
    }

}
