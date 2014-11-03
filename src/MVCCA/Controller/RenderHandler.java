package MVCCA.Controller;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.Singletons;
import MVCCA.View.View;
import javafx.event.Event;
import javafx.event.EventHandler;

public class RenderHandler implements EventHandler {
    /**
     * renderHandler specifies one step that will be done
     * within the timeline (rendering loop). Contains synchronized methods to get one instance of the renderer
     * prevents some weird bugs with timeline screwing itself up.
     */

    volatile static RenderHandler instance = null;

    Logic logic;
    View view;

    public static synchronized RenderHandler getInstance(Logic l, View v) {
        if (instance != null && Singletons.getLogicName(instance.logic).equals(Singletons.getLogicName(l))) {
            return instance;
        } else {
            instance = new RenderHandler();
            instance.logic = l;
            instance.view = v;
            return instance;
        }
    }

    @Override
    public void handle(Event event) {
        if (!Singletons.isPaused()) {
            logic.genAdvance();
            view.setAdditionalMessage(logic.getAdditionalMessage());
            view.setGeneration(logic.getGenNumber());
            view.setDrawMatrix(logic.getCurrentGrid());
        }
    }
}