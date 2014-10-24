package MVCCA.Logic.Abstract;

import javafx.scene.paint.Color;

/**
 * Abstract class for automaton logic
 */
public abstract class Logic {

    public int genNumber;

    public abstract void clear();

    public abstract void genAdvance();

    public abstract int[][] getCurrentGrid();

    public abstract void setCell(int x, int y, int value);

    public abstract int getGenNumber();

    public abstract Color[] getColors();

    public abstract String getAdditionalMessage();

    public abstract String getUtilityButtonName();

    public abstract void performUtilityAction();
}
