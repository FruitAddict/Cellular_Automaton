package MVCCA.Logic.Abstract;

import MVCCA.Logic.Utilities.Grid;

/**
 * Brush functional interface interface
 * Used to specify what happens when canvas is clicked
 */
public interface Brush {
    public void setCells(Grid g, int x, int y, int value);
}
