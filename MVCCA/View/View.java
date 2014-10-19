package MVCCA.View;

import MVCCA.Controller.Controller;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * REQUIRES JRE 8_20u
 */
public class View extends Application {


    Controller controller;
    Label genLabel;
    Canvas c;
    MenuBar mainBar;
    MenuItem speedOption;
    VBox menusPane;
    int[][] drawMatrix;
    public static Color[] colorsArray;
    private static final int width = 200;
    private static final int height = 200;
    private static double scale = 4.5;

    public void start(Stage primaryStage){

        BorderPane canvasPane = new BorderPane(); //canvas
        BorderPane mainPane = new BorderPane(); //main
        menusPane = new VBox(); //menu pane
        mainPane.setCenter(canvasPane);
        mainPane.setRight(menusPane);
        Pane drawStore = new Pane();
        genLabel = new Label("");
        genLabel.setStyle("-fx-background-color: turquoise");
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
        clearButton.setStyle("-fx-background-color: paleturquoise");
        ToggleButton playButton = new ToggleButton("Stop");
        playButton.setStyle("-fx-background-color: paleturquoise");
        Button advGenButton = new Button("Advance Generation");
        advGenButton.setStyle("-fx-background-color: paleturquoise");

        /**
         * buttonBox holds the buttons at the bottom of the screen in horizontal alignment
         * speedBar changes the time between new generations are drawn to the sreen
         * zoomBar controls the zoom (borderpane prevents from moving canvas around so its centered)
         * Separate HBoxes to hold the bars together with info labels
         * genLabel shows the current generation obtained from the logic
         */
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox.setSpacing(5); //spacing between buttons in this box
        buttonBox.getChildren().addAll(genLabel,clearButton,playButton,advGenButton); //adding buttons to the box


        //placing the gui components into main pane
        canvasPane.setCenter(drawStore);
        canvasPane.setBottom(buttonBox);

        /**
         * Menus and handlers for menu items
         * 1-thread allowing in progress
         */
        mainBar = new MenuBar();
        Menu menuView = new Menu("View");

        speedOption = new MenuItem("Time Between Generations");
        speedOption.setOnAction(e->{
            createGenTimePane();
        });
        menuView.getItems().add(speedOption);

        mainBar.getMenus().add(menuView);

        //Initalizing the Stage and creating a Scene for the main pane
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setTitle("Cellular Automatons");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
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

    public void createGenTimePane(){
        speedOption.setDisable(true);
        NumberPane genTimePane = new NumberPane("Time between generations",10,1000,"ms",controller);
        menusPane.getChildren().add(genTimePane);
        genTimePane.setOnMouseClicked(e->{
            if(e.isControlDown()){
                menusPane.getChildren().remove(genTimePane);
                speedOption.setDisable(false);
            }
        });
        ;

    }

}
