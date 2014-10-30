package MVCCA.Logic.Abstract;

import MVCCA.Logic.Utilities.Grid;

/**
 * Brush class
 * Used to specify what happens when canvas is clicked
 */
public abstract class Brush {
    public int[][] data = new int[5][5];

    public Brush(int[][] data){
        this.data=data;
    }

    public abstract void setCells(Grid g, int x, int y, int value);
}
