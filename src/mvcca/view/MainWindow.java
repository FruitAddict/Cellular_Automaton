package mvcca.view;

import mvcca.controller.Controller;
import mvcca.logic.CaveGeneratorLogic;
import mvcca.logic.CustomLogic;
import mvcca.logic.GameOfLifeLogic;
import mvcca.logic.utilities.Grid;
import mvcca.view.utilitypanes.GridIO;
import mvcca.logic.utilities.Singletons;
import mvcca.logic.WireworldLogic;
import mvcca.resources.Resources;
import mvcca.view.utilitypanes.InfoPane;
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
public class MainWindow extends Application {

    private static double scale = 4;
    private final int width = 200;
    private final int height = 200;
    private Stage primaryStage;
    private Controller controller;
    private Label genLabel;
    private Label additionalMessageLabel;
    private Canvas canvas;
    private MenuItem fpsOption;
    private MenuItem cameraOption;
    private MenuItem infoOption;
    private MenuItem brushOption;
    private VBox menusPane;
    private HBox buttonBox;
    private ToggleButton playButton;
    private Button utilityButton;
    private Button clearButton;
    private Button advGenButton;
    private Grid drawMatrix;
    private Color[] colorsArray;

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
        buttonBox.setSpacing(5);
        buttonBox.getChildren().addAll(genLabel, clearButton, playButton, advGenButton, utilityButton, additionalMessageLabel);
        buttonBox.setMaxHeight(15);
        mainPane.setBottom(buttonBox);

        /**
         * Menus and handlers for menu items
         * 1-thread allowing in progress
         */
        MenuBar mainBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuTools = new Menu("Tools");
        Menu menuLogic = new Menu("Logic");

        /**
         * loading menus
         */

        loadFileMenus(menuFile);

        loadViewMenus(menuTools);

        loadLogicMenus(menuLogic);


        mainBar.getMenus().addAll(menuFile,menuTools, menuLogic);

        /**
         * Scene and stage initialization, setting up menu to the top of the screen.
         */
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(680);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setTitle("Cellular Automatons - " + controller.getLogicName());
        Image icon = new Image(Resources.class.getResourceAsStream("icon.png"));
        primaryStage.getIcons().add(icon);
        Scene primaryScene = new Scene(holdingPane, (width * scale) + 50, (height * scale) + 50);
        ((BorderPane) primaryScene.getRoot()).setTop(mainBar);
        primaryStage.setScene(primaryScene);
        updateButtons();
        primaryStage.show();

        //debug
        System.out.println("Canvas placed at:" + canvas.getTranslateX() + " " + canvas.getTranslateY());

        /**
         * EVENT HANDLERS
         * Enables all the info panes as default option
         */
        clearButton.setOnAction(e -> controller.clear());

        playButton.setOnAction(e -> {
            controller.pause();
            updateButtons();
        });

        advGenButton.setOnAction(e -> controller.advGen());

        utilityButton.setOnAction(e -> controller.utilityAction());

        // cell drawing handler
        canvas.setOnMouseDragged(e -> {
            controller.setCell((int) e.getX(), (int) e.getY());
            redraw();
        });
        canvas.setOnMousePressed(e -> controller.setCell((int) e.getX(), (int) e.getY()));

        //resizing
        mainPane.widthProperty().addListener(e -> {
            scale = mainPane.getWidth() / width;
            if(scale<3.955) {
                canvas.setScaleX(scale);
                canvas.setScaleY(scale);
            }
        });

        mainPane.heightProperty().addListener(e -> {
            scale = mainPane.getWidth() / width;
            if(scale<3.955) {
                canvas.setScaleX(scale);
                canvas.setScaleY(scale);
            }
        });

        fpsOption.fire();
        infoOption.fire();
        brushOption.fire();
        advGenButton.fire();
        playButton.fire();
        /**
         * /EVENT HANDLERS
         */
    }


    public void setDrawMatrix(Grid matrix) {
        /**
         * Most important method here, receives new grid and draws it to the screen.
         */
        drawMatrix = matrix;
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
                gc.getPixelWriter().setColor(i, j, colorsArray[drawMatrix.get(i, j)]);
            }
        }
    }

    public void setGeneration(int gen) {
        genLabel.setText("Generation: " + gen);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getScale() {
        return scale;
    }

    public void setColorsArray(Color[] colorArray) {
        colorsArray = colorArray;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller c) {
        controller = c;
    }

    public void changeStageName(String s) {
        primaryStage.setTitle("Cellular Automatons - " + controller.getLogicName());
    }

    public void setAdditionalMessage(String s) {
        additionalMessageLabel.setText(s);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public void reloadInfoPane() {
        /**
         * When logic is changed, reloads information text and link in the Info pane.
         */
        InfoPane p = Singletons.getInfoPane(this);
        p.update(controller.getLogic());
    }

    public void updateButtons() {
        /**
         * Updates GUI depending on the info received from
         * the controller.
         */
        additionalMessageLabel.setText(controller.getLogic().getAdditionalMessage());
        utilityButton.setText(controller.getLogic().getUtilityButtonName());
        if (Singletons.isPaused()) {
            playButton.setText("Play");
        } else if (!Singletons.isPaused()) {
            playButton.setText("Stop");
        }
        utilityButton.setText(controller.getUtilityButtonName());
        if (utilityButton.getText().equals("")) {
            buttonBox.getChildren().remove(utilityButton);
        } else {
            buttonBox.getChildren().remove(utilityButton);
            buttonBox.getChildren().add(4, utilityButton);
        }
        if (controller.getLogic() instanceof CaveGeneratorLogic) {
            playButton.setDisable(true);
            advGenButton.setDisable(true);
        } else {
            playButton.setDisable(false);
            advGenButton.setDisable(false);
        }
        if (additionalMessageLabel.getText().equals("")) {
            if (buttonBox.getChildren().contains(additionalMessageLabel)) {
                buttonBox.getChildren().remove(additionalMessageLabel);
            }
        } else {
            if (!buttonBox.getChildren().contains(additionalMessageLabel)) {
                buttonBox.getChildren().add(additionalMessageLabel);
            }

        }
        if (controller.getLogic() instanceof CustomLogic) {
            if (!menusPane.getChildren().contains(Singletons.getRulesetPane(this))) {
                menusPane.getChildren().add(Singletons.getRulesetPane(this));
            }
        } else {
            if (menusPane.getChildren().contains(Singletons.getRulesetPane(this))) {
                menusPane.getChildren().remove(Singletons.getRulesetPane(this));
            }
        }
        if ((controller.getLogic() instanceof CustomLogic) || (controller.getLogic() instanceof GameOfLifeLogic) ||
                                                                controller.getLogic() instanceof WireworldLogic) {
            brushOption.setDisable(false);
        } else {
            brushOption.setDisable(true);
            brushOption.setText("Brush");
            if (menusPane.getChildren().contains(Singletons.getBrushPane(this))) {
                menusPane.getChildren().remove(Singletons.getBrushPane(this));
            }
        }

    }

    public void loadFileMenus(Menu m){
        MenuItem openGrid = new MenuItem("Load from file");
        openGrid.setOnAction(e->{
            GridIO.loadGrid(getController().getLogic(), primaryStage);
            genLabel.setText("Generation: "+Integer.toString(getController().getLogic().getGenNumber()));
            setDrawMatrix(getController().getLogic().getCurrentGrid());
        });

        MenuItem saveGrid = new MenuItem("Save to file");
        saveGrid.setOnAction(e->{
            GridIO.saveGrid(getController().getLogic(), primaryStage);
            genLabel.setText("Generation: "+Integer.toString(getController().getLogic().getGenNumber()));
        });

        MenuItem openImageGrid = new MenuItem("Convert image");
        openImageGrid.setOnAction(e->{
            GridIO.loadFromImage(this,getController().getLogic(),primaryStage);
            setDrawMatrix(getController().getLogic().getCurrentGrid());
        });

        m.getItems().addAll(openGrid, saveGrid, openImageGrid);
    }

    public void loadLogicMenus(Menu m) {
        /**
         * Menu items for loading logics to the program
         */
        MenuItem lifeLogic = new MenuItem("Game of Life");
        lifeLogic.setOnAction(e -> controller.changeLogic(Singletons.getGameOfLifeLogic(width, height)));
        MenuItem antLogic = new MenuItem("Langton's Ant");
        antLogic.setOnAction(e -> controller.changeLogic(Singletons.getLangtonsAntLogic(width, height)));
        MenuItem caveLogic = new MenuItem("Cave Generator");
        caveLogic.setOnAction(e -> controller.changeLogic(Singletons.getCaveGeneratorLogic(width, height)));
        MenuItem customLogic = new MenuItem("Custom");
        customLogic.setOnAction(e -> controller.changeLogic(Singletons.getCustomLogic(width, height)));
        MenuItem animalLogic = new MenuItem("Animals Grazing");
        animalLogic.setOnAction(e -> controller.changeLogic(Singletons.getAnimalLogic(width, height)));
        MenuItem wireLogic = new MenuItem("Wireworld");
        wireLogic.setOnAction(e-> controller.changeLogic(Singletons.getWireworldLogic(width,height)));
        m.getItems().addAll(lifeLogic, antLogic, caveLogic, animalLogic,wireLogic, customLogic);
    }

    public void loadViewMenus(Menu m) {
        /**
         * Menu items for loading view widgets to the menus pane
         */
        fpsOption = new MenuItem("FPS");
        fpsOption.setOnAction(e -> {
            if (menusPane.getChildren().contains(Singletons.getNumberPane(this))) {
                menusPane.getChildren().remove(Singletons.getNumberPane(this));
                fpsOption.setText("FPS");
            } else {
                menusPane.getChildren().add(Singletons.getNumberPane(this));
                fpsOption.setText("FPS \u2713");
            }

        });
        cameraOption = new MenuItem("Camera Position");
        cameraOption.setOnAction(e -> {
            if (menusPane.getChildren().contains(Singletons.getCameraPane(this))) {
                menusPane.getChildren().remove(Singletons.getCameraPane(this));
                cameraOption.setText("Camera Position");
            } else {
                menusPane.getChildren().add(Singletons.getCameraPane(this));
                cameraOption.setText("Camera Position \u2713");
            }
        });
        //info pane must be changed each time logic is changed.
        infoOption = new MenuItem("Info");
        infoOption.setOnAction(e -> {
            if (menusPane.getChildren().contains(Singletons.getInfoPane(this))) {
                menusPane.getChildren().remove(Singletons.getInfoPane(this));
                infoOption.setText("Info");
            } else {
                menusPane.getChildren().add(Singletons.getInfoPane(this));
                infoOption.setText("Info \u2713");
            }
        });
        brushOption = new MenuItem("Brush");
        brushOption.setOnAction(e -> {
            if (menusPane.getChildren().contains(Singletons.getBrushPane(this))) {
                menusPane.getChildren().remove(Singletons.getBrushPane(this));
                brushOption.setText("Brush");
            } else {
                menusPane.getChildren().add(Singletons.getBrushPane(this));
                brushOption.setText("Brush \u2713");
            }
        });
        m.getItems().addAll(fpsOption, cameraOption, infoOption, brushOption);
    }
}
