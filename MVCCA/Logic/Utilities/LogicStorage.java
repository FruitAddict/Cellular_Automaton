package MVCCA.Logic.Utilities;

import MVCCA.Logic.GameOfLifeLogic;
import MVCCA.Logic.LangtonsAntLogic;

/**
 * Created by FruitAddict on 2014-10-20.
 */
public class LogicStorage {
    private LogicStorage(){}

    static volatile LangtonsAntLogic logicAnt=null;
    static volatile GameOfLifeLogic logicLife=null;
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

    public static synchronized boolean isPaused(){
        return paused;
    }

    public static synchronized  void setPaused(Boolean bool){
        paused = bool;
    }
}
