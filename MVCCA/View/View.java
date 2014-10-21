package MVCCA.View;

import MVCCA.Controller.Controller;
import MVCCA.Logic.Utilities.LogicStorage;
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

    private Stage primaryStage;
    private Controller controller;
    private Label genLabel;
    private Label additionalMessageLabel;
    private Canvas canvas;
    private MenuItem fpsOption;
    private MenuItem cameraOption;
    private MenuItem lifeLogic;
    private MenuItem antLogic;
    private VBox menusPane;
    ToggleButton playButton;
    private int[][] drawMatrix;
    private Color[] colorsArray;
    private final int width = 200;
    private final int height = 200;

    public static double scale = 4.5;

    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        BorderPane canvasPane = new BorderPane(); //canvas
        BorderPane mainPane = new BorderPane(); //main
        menusPane = new VBox(); //menu pane
        menusPane.setSpacing(25);
        menusPane.setMaxWidth(width * scale / 5);
        mainPane.setCenter(canvasPane);
        mainPane.setRight(menusPane);
        Pane drawStore = new Pane();
        genLabel = new Label("");
        genLabel.setStyle("-fx-background-color: turquoise");
        additionalMessageLabel = new Label("");
        additionalMessageLabel.setStyle("-fx-background-color: turquoise");
        canvas = new Canvas(width, height);
        canvas.setScaleX(scale);
        canvas.setScaleY(scale);
        canvas.setTranslateX(380);
        canvas.setTranslateY(360);
        drawStore.getChildren().add(canvas);

        /**
         * Buttons:
         * clearButton- Resets the canvas to its original state (all white)
         * playButton - stops/resumes progression of the generations
         * advGenButton - manually advances the generation of the cells by one
         */
        Button clearButton = new Button("Clear");
        clearButton.setStyle("-fx-background-color: paleturquoise");
        playButton = new ToggleButton();
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
        buttonBox.getChildren().addAll(genLabel, clearButton, playButton, advGenButton, additionalMessageLabel); //adding buttons to the box


        //placing the gui components into main pane
        canvasPane.setCenter(drawStore);
        canvasPane.setBottom(buttonBox);

        /**
         * Menus and handlers for menu items
         * 1-thread allowing in progress
         */
        MenuBar mainBar = new MenuBar();
        Menu menuView = new Menu("View");
        Menu menuLogic = new Menu("Logic");


        fpsOption = new MenuItem("FPS");
        fpsOption.setOnAction(e -> createGenTimePane());
        cameraOption = new MenuItem("Camera Position");
        cameraOption.setOnAction(e -> createCamOptionPane());

        lifeLogic = new MenuItem("Game of Life logic");
        lifeLogic.setOnAction(e-> controller.changeLogic(LogicStorage.getGameOfLifeLogic(width,height)));
        antLogic = new MenuItem("Langton's Ant logic");
        antLogic.setOnAction(e-> controller.changeLogic(LogicStorage.getLangtonsAntLogic(width,height)));

        menuView.getItems().addAll(fpsOption, cameraOption);
        menuLogic.getItems().addAll(lifeLogic,antLogic);

        mainBar.getMenus().addAll(menuView, menuLogic);

        //Initalizing the Stage and creating a Scene for the main pane
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setTitle("Cellular Automatons - " + controller.getLogicName());
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("\\resources\\icon.png")));
        Scene primaryScene = new Scene(mainPane, (width * scale) + 50, (height * scale) + 50);
        ((BorderPane) primaryScene.getRoot()).setTop(mainBar);
        primaryStage.setScene(primaryScene);
        primaryStage.show();

        //debug
        System.out.println(canvas.getTranslateX() + " " + canvas.getTranslateY());

        //setting up event handlers
        clearButton.setOnAction(e -> controller.clear());

        playButton.setOnAction(e -> {
            controller.pause();
            updateButtons();
        });

        advGenButton.setOnAction(e -> controller.advGen());

        // cell drawing handler && moving the pane around
        canvas.setOnMouseDragged(e -> {
            if (e.isControlDown()) {
                canvas.setTranslateX(e.getX());
                canvas.setTranslateY(e.getY());
            } else {
                controller.setCell((int) e.getX(), (int) e.getY());
                redraw();
            }
        });
        canvas.setOnMousePressed(e->{
            controller.setCell((int)e.getX(), (int)e.getY());
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
        GraphicsContext gc = canvas.getGraphicsContext2D();
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


    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        View.scale = scale;
    }

    public void rescale(double scale){
        setScale(scale);
        canvas.setScaleX(scale);
        canvas.setScaleY(scale);
    }

    public void setColorsArray(Color[] colorArray){
        colorsArray = colorArray;
    }

    public void translateCanvas(double x, double y){
        canvas.setTranslateX(x);
        canvas.setTranslateY(y);
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public Controller getController(){
        return controller;
    }

    public void changeStageName(String s){
        primaryStage.setTitle("Cellular Automatons - " + controller.getLogicName());
    }

    public void setAdditionalMessage(String s){
        additionalMessageLabel.setText(s);
    }

    public void updateButtons(){
        if (LogicStorage.isPaused()) {
            playButton.setText("Play");
        } else if(!LogicStorage.isPaused()) {
            playButton.setText("Stop");
        }
    }

    private void createGenTimePane(){
        /**
         * those two last methods are very simmiliar
         * disable speed option from the menu, reenable when the widget pane is closed
         * create new widget pane
         */
        fpsOption.setDisable(true);
        NumberPane genTimePane = new NumberPane(2,60,this);
        menusPane.getChildren().add(genTimePane);
        genTimePane.setOnMouseClicked(e->{
            if(e.isControlDown()){
                menusPane.getChildren().remove(genTimePane);
                fpsOption.setDisable(false);
            }
        });


    }

    private void createCamOptionPane(){
        cameraOption.setDisable(true);
        CameraPane cameraPane = new CameraPane(this);
        menusPane.getChildren().add(cameraPane);
        cameraPane.setOnMouseClicked(e->{
            if(e.isControlDown()){
                menusPane.getChildren().remove(cameraPane);
                cameraOption.setDisable(false);
            }
        });
    }

}
