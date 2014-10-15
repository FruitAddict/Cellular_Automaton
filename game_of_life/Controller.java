package MVCCA;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Created by FruitAddict on 2014-10-14.
 */
public class Controller extends Application {
    //Holds logic and view for the MVC pattern
    private GameOfLifeLogic logic;
    private JavaFXView view;

    //boolean to stop the auto logic updates
    private boolean logicRunning=true;

    //time for the thread to sleep between genrations, initialy 50 ms
    int sleepTime = 50;

    public void start(Stage primaryStage){
        /* Creates a new View and Logic, assigns them to the variables in this controller
         *class, passes this instance of the controller to the view and logic
         * and starts the view using its own primarystage (JavaFX apps
         * can only be run on one thread)
         */
        JavaFXView view = new JavaFXView();
        GameOfLifeLogic logic = new GameOfLifeLogic(view.getWidth(),view.getHeight());
        setThings(view,logic);
        view.setController(this);
        view.start(primaryStage);

        //initial clear call to prepare for running
        clear();

        /* New thread obtaining new data from the logic
         *and passing it to the view
         */
                Handler handler = new Handler();
                Thread loopThread = new Thread(handler);
                loopThread.start();

    }

    //handles clearing the screen and drawing the borders
    public void clear(){
        logic.clear();
        view.setDrawMatrix(logic.getCurrentGrid());
    }

    public void pause(){
        setLogicRunning(!logicRunning);
    }

    public void advGen(){
        logic.genAdvance();
        view.setGeneration(logic.getGenNumber());
        view.setDrawMatrix(logic.getCurrentGrid());
    }

    public void setCell(int x, int y){
        logic.setCell(x,y,1);
        view.setDrawMatrix(logic.getCurrentGrid());
    }

    //Assigns view and logic
    public void setThings(JavaFXView view, GameOfLifeLogic logic){
        this.view=view;
        this.logic=logic;
}

    public boolean isLogicRunning() {
        return logicRunning;
    }

    public void setLogicRunning(boolean logicRunning) {
        this.logicRunning = logicRunning;
    }


    private class Handler implements Runnable{
        /* Handler runnable class that contains the while gen-adv loop
         *  everything MVC
         */
        public void run() {
            while (true) {
                if(isLogicRunning()) {
                    Platform.runLater(() -> {
                        logic.genAdvance();
                        view.setGeneration(logic.getGenNumber());
                        view.setDrawMatrix(logic.getCurrentGrid());
                    });
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (Exception ex) {
                }
            }
        }
    }
}