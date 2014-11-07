package mvcca.controller;

import mvcca.logic.abstracted.Logic;
import mvcca.logic.utilities.Singletons;
import mvcca.view.MainWindow;
import javafx.event.Event;
import javafx.event.EventHandler;

public class RenderHandler implements EventHandler {
    /**
     * renderHandler specifies one step that will be done
     * within the timeline (rendering loop). Contains synchronized methods to get one instance of the renderer
     * prevents some weird bugs with timeline screwing itself up.
     */

    private volatile static RenderHandler instance = null;

    private Logic logic;
    private MainWindow mainWindow;

    public static synchronized RenderHandler getInstance(Logic l, MainWindow v) {
        if (instance != null && instance.logic.getName().equals(l.getName())) {
            return instance;
        } else {
            instance = new RenderHandler();
            instance.logic = l;
            instance.mainWindow = v;
            return instance;
        }
    }

    @Override
    public void handle(Event event) {
        if (!Singletons.isPaused()) {
            logic.genAdvance();
            mainWindow.setAdditionalMessage(logic.getAdditionalMessage());
            mainWindow.setGeneration(logic.getGenNumber());
            mainWindow.setDrawMatrix(logic.getCurrentGrid());
        }
    }
}