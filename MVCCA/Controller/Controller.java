package MVCCA.Controller;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.LogicStorage;
import MVCCA.View.View;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * MVC Controller class.
 */
@SuppressWarnings("unchecked")

public class Controller extends Application {
    //Holds logic and view for the MVC pattern
    private Logic logic;
    private View view;

    int fps = 30;
    Duration duration = Duration.millis(1000/fps);
    Timeline timeline;
    //initially 60fps

    public void start(Stage primaryStage){

        /**
         * Creates a new View and Logic, assigns them to the variables in this controller
         * class, passes this instance of the controller to the view and logic
         * and starts the view using its own primaryStage (JavaFX apps
         * can only be run on one thread)
         */
        View view = new View();
        Logic logic = LogicStorage.getLangtonsAntLogic(view.getWidth(), view.getHeight());
        setThings(view,logic);
        view.setController(this);
        view.setColorsArray(logic.getColors());
        view.start(primaryStage);

        //initial clear call to prepare for running
        clear();
        view.updateButtons();

        timeline = new Timeline(new KeyFrame(duration, RenderHandler.getInstance(logic,view)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //handles clearing the screen and drawing the borders
    public void clear(){
        logic.clear();
        view.setDrawMatrix(logic.getCurrentGrid());
    }
    //pauses/unpauses auto gen progression
    public void pause(){
        setLogicRunning(!LogicStorage.isPaused());
        view.updateButtons();
    }

    //advances the generation by one (mvc)
    public void advGen(){
        logic.genAdvance();
        view.setGeneration(logic.getGenNumber());
        view.setDrawMatrix(logic.getCurrentGrid());
    }
    //sets the cell (mvc) prevents drawing out of borders
    public void setCell(int x, int y){
            logic.setCell(x, y, 1);
            view.setDrawMatrix(logic.getCurrentGrid());
    }

    //changes time between generations
    public void setFramesPerSecond(Duration fps){
        duration=fps;
        timeline.stop();
        timeline = new Timeline(new KeyFrame(duration, RenderHandler.getInstance(logic, view)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //Assigns view and logic
    public void setThings(View view, Logic logic) {
        this.view = view;
        this.logic = logic;
    }

    public void setLogicRunning(boolean logicRunning) {
        LogicStorage.setPaused(logicRunning);
    }

    public String getLogicName(){
        return LogicStorage.getLogicName(logic);
    }

    public void changeLogic(Logic logic){
        /**
         * Changes the current logic.
         * setFramesPerSecond method used to renew the timeline (gameloop) using the new logic and view.
         * no fancy methods for this as I'd have to rewrite the same code.
         */
        this.logic = logic;
        view.changeStageName(getLogicName());
        view.setColorsArray(logic.getColors());
        view.updateButtons();
        setFramesPerSecond(Duration.millis(1000/fps));
        advGen();
    }

    public void setFps(int f){
        fps=f;
        setFramesPerSecond(Duration.millis(1000/fps));
    }

    public int getFps(){
        return fps;
    }

    public String getUtilityButtonName(){
        return logic.getUtilityButtonName();
    }

    public Logic getLogic(){
        return logic;
    }

    public void utilityAction(){
        logic.performUtilityAction();
    }

}
