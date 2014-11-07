package mvcca.logic;

import mvcca.logic.abstracted.Brush;
import mvcca.logic.abstracted.Logic;
import mvcca.logic.abstracted.Resolver;
import mvcca.logic.utilities.Grid;
import mvcca.logic.utilities.Utilities;
import javafx.scene.paint.Color;

/**
 * Created by FruitAddict on 2014-10-28.
 */
public class CustomLogic extends Logic {
    private int width;
    private int height;
    private Grid currentGrid;
    private final Color[] colorArray = {Color.web("827970"), Color.BLACK, Color.web("94FF00"), Color.web("E89B0C"), Color.web("FF0005"), Color.web("440CE8"), Color.web("0DFCFF"), Color.web("FFFF66")};
    private Resolver resolver;
    private Brush brush;

    public CustomLogic(int x, int y) {
        width = x;
        height = y;
        currentGrid = new Grid(width, height, 1, 0,getName());

        /**
         * Setting basic brush
         */

        Utilities.applyBrush(Utilities.getBasicBrushData(), this);

        clear();
    }

    @Override
    public void clear() {
        currentGrid.clear();
        currentGrid.setGenNumber(0);
    }

    @Override
    public void genAdvance() {
        Grid snapshot = currentGrid.copy();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid.set(i, j, resolveCell(i, j, snapshot, currentGrid.get(i, j)));
            }
        }
        currentGrid.setGenNumber(currentGrid.getGenNumber()+1);
    }

    @Override
    public Grid getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void setCurrentGrid(Grid grid){
        currentGrid = grid;
    }

    @Override
    public void setCell(int x, int y, int value) {
        if ((x > 2 && x < width - 3) && (y > 2 && y < height - 3)) {
            brush.setCells(currentGrid, x, y, value);
        }
    }

    @Override
    public String getName(){
        return "Custom";
    }

    @Override
    public int getGenNumber() {
        return currentGrid.getGenNumber();
    }

    @Override
    public Color[] getColors() {
        return colorArray;
    }

    @Override
    public String getAdditionalMessage() {
        return "";
    }

    @Override
    public String getUtilityButtonName() {
        return "Random";
    }

    @Override
    public void performUtilityAction() {
        Utilities.randomFill(this, width, height, 10, 2);
    }

    @Override
    public void setResolver(Resolver r) {
        resolver = null;
        resolver = r;
    }

    @Override
    public void setBrush(Brush b) {
        brush = b;
    }

    @Override
    public Brush getBrush() {
        return brush;
    }

    private int resolveCell(int x, int y, Grid snapshot, int currentValue) {
        if (currentValue != 0) {
            int numberOfNeighbours = Utilities.getNumberOfMooreNeighbours(x, y, width, height, snapshot);
            if (currentValue == 1) {
                return ((resolver != null) ? resolver.ifDead(numberOfNeighbours) : 1);
            } else if (currentValue >= 2) {
                return ((resolver != null) ? resolver.ifAlive(numberOfNeighbours) : 1);
            }
        } else {
            return 0;
        }
        return 1;
    }
}
