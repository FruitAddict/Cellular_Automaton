package MVCCA.View;

import MVCCA.Controller.Controller;
import MVCCA.Logic.CaveGeneratorLogic;
import MVCCA.Logic.Utilities.Grid;
import MVCCA.Logic.Utilities.Singletons;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
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
 * WARNING:
 * THIS CLASS IS A MESS
 */
public class View extends Application {

    private Stage primaryStage;
    private Controller controller;
    private Label genLabel;
    private Label additionalMessageLabel;
    private Canvas canvas;
    private MenuItem fpsOption;
    private MenuItem cameraOption;
    private MenuItem infoOption;
    private VBox menusPane;
    private HBox buttonBox;
    private ToggleButton playButton;
    private Button utilityButton;
    private Button clearButton;
    private Button advGenButton;
    private Grid drawMatrix;
    private Color[] colorsArray;
    private final int width = 200;
    private final int height = 200;

    public static double scale = 4;

    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        BorderPane mainPane = new BorderPane();
        BorderPane holdingPane = new BorderPane();
        mainPane.setStyle("-fx-border-color: #827970; -fx-border-width: 1");
        holdingPane.setCenter(mainPane);
        menusPane = new VBox(); //menu pane
        menusPane.setMaxWidth(width * scale / 5);


        holdingPane.setRight(menusPane);
        genLabel = new Label("");
        genLabel.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 1 ; -fx-border-color: #827970");
        additionalMessageLabel = new Label("");
        additionalMessageLabel.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 1 ; -fx-border-color: #827970");
        canvas = new Canvas(width, height);
        canvas.setScaleX(scale);
        canvas.setScaleY(scale);
        mainPane.setCenter(canvas);

        /**
         * Buttons:
         * clearButton- Resets the canvas to its original state (all white)
         * playButton - stops/resumes progression of the generations
         * advGenButton - manually advances the generation of the cells by one
         * utilityButton - custom button to be used by logic's quirks
         */
        clearButton = new Button("Clear");
        clearButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 1 ; -fx-border-color: #827970");
        playButton = new ToggleButton();
        playButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 1 ; -fx-border-color: #827970");
        advGenButton = new Button("Advance Generation");
        advGenButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 1 ; -fx-border-color: #827970");
        utilityButton = new Button(controller.getUtilityButtonName());
        utilityButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-width: 1 ; -fx-border-color: #827970");

        /**
         * buttonBox holds the buttons at the bottom of the screen in horizontal alignment
         * speedBar changes the time between new generations are drawn to the sreen
         * zoomBar controls the zoom (borderpane prevents from moving canvas around so its centered)
         * Separate HBoxes to hold the bars together with info labels
         * genLabel shows the current generation obtained from the logic
         */
        buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(5); //spacing between buttons in this box
        buttonBox.getChildren().addAll(genLabel, clearButton, playButton, advGenButton,utilityButton, additionalMessageLabel); //adding buttons to the box
        buttonBox.setMaxHeight(15);
        //placing the gui components into main pane
        mainPane.setBottom(buttonBox);

        /**
         * Menus and handlers for menu items
         * 1-thread allowing in progress
         */
        MenuBar mainBar = new MenuBar();
        Menu menuView = new Menu("View");
        Menu menuLogic = new Menu("Logic");

        /**
         * menu options handlers
         */
        fpsOption = new MenuItem("FPS");
        fpsOption.setOnAction(e -> {
            if(menusPane.getChildren().contains(Singletons.getNumberPane(this))){
                menusPane.getChildren().remove(Singletons.getNumberPane(this));
                fpsOption.setText("FPS");
            }else {
                menusPane.getChildren().add(Singletons.getNumberPane(this));
                fpsOption.setText("FPS \u2713");
            }

        });
        cameraOption = new MenuItem("Camera Position");
        cameraOption.setOnAction(e -> {
            if(menusPane.getChildren().contains(Singletons.getCameraPane(this))){
                menusPane.getChildren().remove(Singletons.getCameraPane(this));
                cameraOption.setText("Camera Position");
            }else {
                menusPane.getChildren().add(Singletons.getCameraPane(this));
                cameraOption.setText("Camera Position \u2713");
            }
        });
        //info pane must be changed each time logic is changed.
        infoOption = new MenuItem("Info");
        infoOption.setOnAction(e->{
            if(menusPane.getChildren().contains(Singletons.getInfoPane(this))){
                menusPane.getChildren().remove(Singletons.getInfoPane(this));
                infoOption.setText("Info");
            } else {
                menusPane.getChildren().add(Singletons.getInfoPane(this));
                infoOption.setText("Info \u2713");
            }
        });


        MenuItem lifeLogic = new MenuItem("Game of Life");
        lifeLogic.setOnAction(e -> controller.changeLogic(Singletons.getGameOfLifeLogic(width, height)));
        MenuItem antLogic = new MenuItem("Langton's Ant");
        antLogic.setOnAction(e -> controller.changeLogic(Singletons.getLangtonsAntLogic(width, height)));
        MenuItem caveLogic = new MenuItem("Cave Generator");
        caveLogic.setOnAction(e -> controller.changeLogic(Singletons.getCaveGeneratorLogic(width, height)));

        menuView.getItems().addAll(fpsOption, cameraOption, infoOption);
        menuLogic.getItems().addAll(lifeLogic, antLogic, caveLogic);

        mainBar.getMenus().addAll(menuView, menuLogic);

        //Initalizing the Stage and creating a Scene for the main pane
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setTitle("Cellular Automatons - " + controller.getLogicName());
        primaryStage.getIcons().add(new Image("resources\\icon.png"));
        Scene primaryScene = new Scene(holdingPane, (width * scale) + 50, (height * scale) + 50);
        ((BorderPane) primaryScene.getRoot()).setTop(mainBar);
        primaryStage.setScene(primaryScene);
        updateButtons();
        primaryStage.show();

        //debug
        System.out.println("Canvas placed at:"+canvas.getTranslateX() + " " + canvas.getTranslateY());

        //setting up event handlers
        clearButton.setOnAction(e -> controller.clear());

        playButton.setOnAction(e -> {
            controller.pause();
            updateButtons();
        });

        advGenButton.setOnAction(e -> controller.advGen());

        utilityButton.setOnAction(e->controller.utilityAction());

        // cell drawing handler
        canvas.setOnMouseDragged(e -> {
                controller.setCell((int) e.getX(), (int) e.getY());
                redraw();
        });
        canvas.setOnMousePressed(e->{
            controller.setCell((int)e.getX(), (int)e.getY());
        });

        //resizing
        mainPane.widthProperty().addListener(e->{
            scale = mainPane.getWidth()/width;
            canvas.setScaleX(scale);
            canvas.setScaleY(scale);
        });

        mainPane.heightProperty().addListener(e->{
            scale = mainPane.getWidth()/width;
            canvas.setScaleX(scale);
            canvas.setScaleY(scale);
        });

        fpsOption.fire();
        infoOption.fire();
        cameraOption.fire();
    }



    public void setDrawMatrix(Grid matrix){
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
                gc.getPixelWriter().setColor(i, j, colorsArray[drawMatrix.get(i,j)]);
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


    public void setColorsArray(Color[] colorArray){
        colorsArray = colorArray;
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

    public void reloadInfoPane(){
        InfoPane p = Singletons.getInfoPane(this);
        p.update(controller.getLogic());
    }

    public void updateButtons(){
        if (Singletons.isPaused()) {
            playButton.setText("Play");
        } else if(!Singletons.isPaused()) {
            playButton.setText("Stop");
        }
        utilityButton.setText(controller.getUtilityButtonName());
        if(utilityButton.getText().equals("")){
            buttonBox.getChildren().remove(utilityButton);
        }else{
            buttonBox.getChildren().remove(utilityButton);
            buttonBox.getChildren().add(4,utilityButton);
        }
        if(controller.getLogic() instanceof CaveGeneratorLogic){
            playButton.setDisable(true);
            advGenButton.setDisable(true);
        } else {
            playButton.setDisable(false);
            advGenButton.setDisable(false);
        }
        if(additionalMessageLabel.getText().equals("")){
            if(buttonBox.getChildren().contains(additionalMessageLabel)){
                buttonBox.getChildren().remove(additionalMessageLabel);
            }
        } else {
            if(!buttonBox.getChildren().contains(additionalMessageLabel)){
                buttonBox.getChildren().add(additionalMessageLabel);
                }

        }

    }
}
