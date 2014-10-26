package MVCCA.Logic.Utilities;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.CaveGeneratorLogic;
import MVCCA.Logic.GameOfLifeLogic;
import MVCCA.Logic.LangtonsAntLogic;
import MVCCA.View.CameraPane;
import MVCCA.View.InfoPane;
import MVCCA.View.NumberPane;
import MVCCA.View.View;

/**
 * Created by FruitAddict on 2014-10-20.
 */
public class Singletons {
    private Singletons(){}

    static volatile LangtonsAntLogic logicAnt=null;
    static volatile GameOfLifeLogic logicLife=null;
    static volatile CaveGeneratorLogic logicCave=null;
    static volatile CameraPane camPane = null;
    static volatile NumberPane numPane = null;
    static volatile InfoPane infoPane = null;
    static volatile boolean paused = false;

    public static synchronized LangtonsAntLogic getLangtonsAntLogic(int x, int y){
            if(logicAnt!=null){
                return logicAnt;
            } else {
                logicAnt = new LangtonsAntLogic(x,y);
                return logicAnt;
            }
    }
    public static synchronized GameOfLifeLogic getGameOfLifeLogic(int x, int y){
        if(logicLife!=null){
            return logicLife;
        } else {
            logicLife = new GameOfLifeLogic(x,y);
            return logicLife;
        }
    }
    public static synchronized CaveGeneratorLogic getCaveGeneratorLogic(int x, int y){
        if(logicCave!=null){
            return logicCave;
        } else {
            logicCave = new CaveGeneratorLogic(x,y);
            return logicCave;
        }
    }

    public static synchronized CameraPane getCameraPane(View v){
        if(camPane!=null){
            return camPane;
        }else {
            camPane = new CameraPane(v);
            return camPane;
        }
    }

    public static synchronized NumberPane getNumberPane(View v){
        if(numPane!=null){
            return numPane;
        } else {
            numPane = new NumberPane(1,250,v);
            return numPane;
        }
    }

    public static synchronized InfoPane getInfoPane(View v){
        if(infoPane!=null){
            return infoPane;
        } else {
            infoPane = new InfoPane(v);
            return infoPane;
        }
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
