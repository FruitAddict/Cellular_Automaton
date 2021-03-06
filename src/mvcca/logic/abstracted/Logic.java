package mvcca.logic.abstracted;

import mvcca.logic.utilities.Grid;
import javafx.scene.paint.Color;

/**
 * Abstract class for automaton logic
 */
public abstract class Logic {

    protected int genNumber;

    public abstract void clear();

    public abstract void genAdvance();

    public abstract Grid getCurrentGrid();

    public abstract void setCurrentGrid(Grid grid);

    public abstract void setCell(int x, int y, int value);

    public abstract int getGenNumber();

    public abstract Color[] getColors();

    public abstract String getName();

    /**
     * Utility button name and additional message methods should
     * return empty string ("") if you dont want those to appear
     * on the screen.
     */
    public abstract String getAdditionalMessage();

    public abstract String getUtilityButtonName();

    public abstract void performUtilityAction();

    public abstract void setResolver(Resolver r);

    public abstract void setBrush(Brush b);

    public abstract Brush getBrush();
}
