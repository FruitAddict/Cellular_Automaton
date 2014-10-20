package MVCCA.Logic.Abstract;

import javafx.scene.paint.Color;

/**
 * Abstract class for automaton logic
 */
public abstract class Logic {

    public int genNumber;

    private String logicName;

    public String getName(){
        return logicName;
    }

    public void setLogicName(String s){
        logicName = s;
    }

    public abstract void clear();

    public abstract void genAdvance();

    public abstract int[][] getCurrentGrid();

    public abstract void setCell(int x, int y, int value);

    public abstract int getGenNumber();

    public abstract Color[] getColors();
}
