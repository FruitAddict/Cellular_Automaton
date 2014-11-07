package mvcca.logic.utilities;

import mvcca.logic.*;
import mvcca.view.*;
import mvcca.view.utilitypanes.*;

/**
 * Class for singletons. Provides access to logic modules.
 */
public class Singletons {
    private Singletons() {
    }

    private static volatile LangtonsAntLogic logicAnt = null;
    private static volatile GameOfLifeLogic logicLife = null;
    private static volatile CaveGeneratorLogic logicCave = null;
    private static volatile CustomLogic customLogic = null;
    private static volatile AnimalsGrazingLogic animalsGrazingLogic = null;
    private static volatile WireworldLogic wireworldLogic = null;
    private static volatile CameraPane camPane = null;
    private static volatile NumberPane numPane = null;
    private static volatile InfoPane infoPane = null;
    private static volatile RulesetPane rulesetPane = null;
    private static volatile BrushPane brushPane = null;
    private static volatile boolean paused = false;

    public static synchronized LangtonsAntLogic getLangtonsAntLogic(int x, int y) {
        return ((logicAnt != null) ? logicAnt : (logicAnt = new LangtonsAntLogic(x, y)));
    }

    public static synchronized GameOfLifeLogic getGameOfLifeLogic(int x, int y) {
        return ((logicLife != null) ? logicLife : (logicLife = new GameOfLifeLogic(x, y)));
    }

    public static synchronized CaveGeneratorLogic getCaveGeneratorLogic(int x, int y) {
        return ((logicCave != null) ? logicCave : (logicCave = new CaveGeneratorLogic(x, y)));
    }

    public static synchronized CustomLogic getCustomLogic(int x, int y) {
        return ((customLogic != null) ? customLogic : (customLogic = new CustomLogic(x, y)));
    }

    public static synchronized AnimalsGrazingLogic getAnimalLogic(int x, int y) {
        return ((animalsGrazingLogic != null) ? animalsGrazingLogic : (animalsGrazingLogic = new AnimalsGrazingLogic(x, y)));
    }

    public static synchronized WireworldLogic getWireworldLogic(int x, int y){
        return ((wireworldLogic !=null) ? wireworldLogic : (wireworldLogic = new WireworldLogic(x,y)));
    }

    public static synchronized CameraPane getCameraPane(MainWindow v) {
        return ((camPane != null) ? camPane : (camPane = new CameraPane(v)));
    }

    public static synchronized NumberPane getNumberPane(MainWindow v) {
        return ((numPane != null) ? numPane : (numPane = new NumberPane(1, 200, v)));
    }

    public static synchronized InfoPane getInfoPane(MainWindow v) {
        return ((infoPane != null) ? infoPane : (infoPane = new InfoPane(v)));
    }

    public static synchronized RulesetPane getRulesetPane(MainWindow v) {
        return ((rulesetPane != null) ? rulesetPane : (rulesetPane = new RulesetPane(v)));
    }

    public static synchronized BrushPane getBrushPane(MainWindow v) {
        return ((brushPane != null) ? brushPane : (brushPane = new BrushPane(v)));
    }

    public static synchronized boolean isPaused() {
        return paused;
    }

    public static synchronized void setPaused(Boolean bool) {
        paused = bool;
    }

}
