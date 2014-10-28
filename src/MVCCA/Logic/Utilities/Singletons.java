package MVCCA.Logic.Utilities;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.CaveGeneratorLogic;
import MVCCA.Logic.CustomLogic;
import MVCCA.Logic.GameOfLifeLogic;
import MVCCA.Logic.LangtonsAntLogic;
import MVCCA.View.*;

/**
 * Created by FruitAddict on 2014-10-20.
 */
public class Singletons {
    private Singletons(){}

    static volatile LangtonsAntLogic logicAnt=null;
    static volatile GameOfLifeLogic logicLife=null;
    static volatile CaveGeneratorLogic logicCave=null;
    static volatile CustomLogic customLogic = null;
    static volatile CameraPane camPane = null;
    static volatile NumberPane numPane = null;
    static volatile InfoPane infoPane = null;
    static volatile RulesetPane rulesetPane = null;
    static volatile boolean paused = false;

    public static synchronized LangtonsAntLogic getLangtonsAntLogic(int x, int y){
        return ((logicAnt!=null) ? logicAnt : (logicAnt = new LangtonsAntLogic(x,y)));
    }
    public static synchronized GameOfLifeLogic getGameOfLifeLogic(int x, int y){
        return ((logicLife!=null) ? logicLife : (logicLife = new GameOfLifeLogic(x,y)));
    }
    public static synchronized CaveGeneratorLogic getCaveGeneratorLogic(int x, int y){
        return ((logicCave!=null) ? logicCave : (logicCave = new CaveGeneratorLogic(x,y)));
    }

    public static synchronized CustomLogic getCustomLogic(int x, int y){
        return ((customLogic!=null) ? customLogic : (customLogic = new CustomLogic(x,y)));
    }

    public static synchronized CameraPane getCameraPane(View v){
        return ((camPane!=null) ? camPane : (camPane  =new CameraPane(v)));
    }

    public static synchronized NumberPane getNumberPane(View v){
        return ((numPane!=null)? numPane : (numPane = new NumberPane(1,250,v)));
    }

    public static synchronized InfoPane getInfoPane(View v){
        return ((infoPane!=null) ? infoPane : (infoPane = new InfoPane(v)));
    }

    public static synchronized RulesetPane getRulesetPane(View v){
        return ((rulesetPane!=null) ? rulesetPane : (rulesetPane = new RulesetPane(v)));
    }

    public static String getLogicName(Logic l){
        if(l instanceof GameOfLifeLogic){
            return "Conway's Game Of Life";
        }
        else if(l instanceof LangtonsAntLogic){
            return "Langton's Ant";
        }
        else if(l instanceof  CaveGeneratorLogic){
            return "Cave Generator";
        } else if(l instanceof CustomLogic){
            return "Custom";
        }
        return "Logic Name Not Found";
    }

    public static synchronized boolean isPaused(){
        return paused;
    }

    public static synchronized  void setPaused(Boolean bool){
        paused = bool;
    }
}
