package mvcca.controller;

import mvcca.logic.abstracted.Logic;
import mvcca.logic.utilities.Singletons;
import mvcca.view.MainWindow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;

/**
 * MVC controller class.
 */
@SuppressWarnings("unchecked")

public class Controller extends Application {
    private int fps = 30;
    private Duration duration = Duration.millis(1000 / fps);
    private Timeline timeline;
    //Holds logic and mainWindow for the MVC pattern
    private Logic logic;
    private MainWindow mainWindow;
    //initially 60fps

    public void start(Stage primaryStage) {

        /**
         * Creates a new MainWindow and logic, assigns them to the variables in this controller
         * class, passes this instance of the controller to the mainWindow and logic
         * and starts the mainWindow using its own primaryStage (JavaFX apps
         * can only be run on one thread)
         *
         * Sets the brush of starting logic to basic brush (1 pixel w/h)
         */

        System.out.println("Program started at: " + LocalDateTime.now());
        MainWindow mainWindow = new MainWindow();
        logic = Singletons.getGameOfLifeLogic(mainWindow.getWidth(), mainWindow.getHeight());
        setThings(mainWindow, logic);
        mainWindow.setController(this);
        mainWindow.setColorsArray(logic.getColors());
        mainWindow.start(primaryStage);

        //initial clear call to prepare for running
        clear();
        mainWindow.updateButtons();

        timeline = new Timeline(new KeyFrame(duration, RenderHandler.getInstance(logic, mainWindow)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    //handles clearing the screen and drawing the borders
    public void clear() {
        logic.clear();
        mainWindow.setDrawMatrix(logic.getCurrentGrid());
    }

    //pauses/unpauses auto gen progression
    public void pause() {
        setLogicRunning(!Singletons.isPaused());
        mainWindow.updateButtons();
    }

    //advances the generation by one (mvc)
    public void advGen() {
        logic.genAdvance();
        mainWindow.setGeneration(logic.getGenNumber());
        mainWindow.setDrawMatrix(logic.getCurrentGrid());
    }

    //sets the cell (mvc) prevents drawing out of borders, uses default 1px wide drawing
    public void setCell(int x, int y) {
        logic.setCell(x, y, 2);
        mainWindow.setDrawMatrix(logic.getCurrentGrid());
    }

    //changes time between generations
    public void setFramesPerSecond(Duration fps) {
        duration = fps;
        timeline.stop();
        timeline = new Timeline(new KeyFrame(duration, RenderHandler.getInstance(logic, mainWindow)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //Assigns mainWindow and logic
    public void setThings(MainWindow mainWindow, Logic logic) {
        this.mainWindow = mainWindow;
        this.logic = logic;
    }

    public void setLogicRunning(boolean logicRunning) {
        Singletons.setPaused(logicRunning);
    }

    public String getLogicName() {
        return logic.getName();
    }

    public void changeLogic(Logic logic) {
        /**
         * Changes the current logic.
         * setFramesPerSecond method used to renew the timeline (gameloop) using the new logic and mainWindow.
         * no fancy methods for this as I'd have to rewrite the same code.
         */
        this.logic = logic;
        mainWindow.changeStageName(getLogicName());
        mainWindow.setColorsArray(logic.getColors());
        setFramesPerSecond(Duration.millis(1000 / fps));
        mainWindow.reloadInfoPane();
        advGen();
        mainWindow.setAdditionalMessage(logic.getAdditionalMessage());
        mainWindow.updateButtons();
        Singletons.getBrushPane(mainWindow).update();
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int f) {
        fps = f;
        setFramesPerSecond(Duration.millis(1000 / fps));
    }

    public String getUtilityButtonName() {
        return logic.getUtilityButtonName();
    }

    public Logic getLogic() {
        return logic;
    }

    public void utilityAction() {
        logic.performUtilityAction();
        mainWindow.setDrawMatrix(logic.getCurrentGrid());
        mainWindow.updateButtons();
    }
}
