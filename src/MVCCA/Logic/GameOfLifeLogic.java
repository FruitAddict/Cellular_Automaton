package mvcca.logic;

import mvcca.logic.abstracted.Brush;
import mvcca.logic.abstracted.Logic;
import mvcca.logic.abstracted.Resolver;
import mvcca.logic.utilities.Grid;
import mvcca.logic.utilities.Utilities;
import javafx.scene.paint.Color;

/**
 * logic class for Conway's game of life. Extends abstract logic class (for use with controller)
 * contains rules for advancing to the new generation as well as making chosen cells alive
 * stores its colors array for use with some viewer (breaks mvc but just a little: D)
 */
public class GameOfLifeLogic extends Logic {
    //holds the current version of grid, prone to changes
    private Grid currentGrid;

    //width and height, assigned on creation
    final private int width;
    final private int height;

    //additional message, can be anything. Can be retrieved by controller
    private String additionalMessage = "";

    //resolver for use with cell udpating
    private Resolver resolver;

    //Brush
    private Brush brush;

    //color array
    final private Color[] colorArray = {Color.web("827970"), Color.BLACK, Color.WHITE, Color.color(1, 76 / 255, 80 / 255), Color.color(1, 0, 5 / 255), Color.color(127 / 255, 0, 3 / 255)};

    //constructor taking w and height as arguements. inits the grid and clears it
    public GameOfLifeLogic(int width, int height) {
        this.width = width;
        this.height = height;

        /**
         * HARDCODED RESOLVER FOR GAME OF LIFE
         */
        setResolver(new Resolver() {
            @Override
            public int ifDead(int n) {
                if (n == 3) {
                    return 2;
                } else {
                    return 1;
                }

            }

            @Override
            public int ifAlive(int n) {
                if (n < 2) {
                    return 1;
                } else if (n == 2) {
                    return 3;
                } else if (n == 3) {
                    return 4;
                } else {
                    return 1;
                }
            }

        });
        /**
         * END OF HARDCODED RESOLVER
         */
        currentGrid = new Grid(width, height, 1, 0, getName());

        /**
         * Basic brush
         */

        Utilities.applyBrush(Utilities.getBasicBrushData(), this);

        clear();
    }

    @Override
    public void clear() {
        currentGrid.setGenNumber(0);
        //Initially sets values of the whole array to 0 (dead cells)
        currentGrid.clear();

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
    public String getName(){
        return "Conway's Game of Life";
    }

    @Override
    public void genAdvance() {
        /**
         * Advances the generation by one by creating a snapshot of the current grid
         * for the resolver to work on- passing x and y coordinates of the cell
         * current value of that cell and the snapshot to the resolver method
         */
        Grid snapshot = currentGrid.copy();
        currentGrid.setGenNumber(currentGrid.getGenNumber()+1);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid.set(i, j, resolve(i, j, currentGrid.get(i, j), snapshot));
            }
        }

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

    private int resolve(int x, int y, int currentValue, Grid snapshot) {
        /**
         * Resolver counts the number of neighbours of the given cell
         * based on the snapshot and returns a correct new value of the cell
         * based on rules here: http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
         * If the entry value of the cell is 0, it returns 0 instantly(border)
         */
        if (currentValue != 0) {

            int numOfNeighbours = Utilities.getNumberOfMooreNeighbours(x, y, width, height, snapshot);

            if (currentValue == 1) {
                return resolver.ifDead(numOfNeighbours);
            } else if (currentValue >= 2) {
                return resolver.ifAlive(numOfNeighbours);
            }
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void setCell(int x, int y, int value) {
        //changes the value of a single cell in the current grid
        if ((x > 2 && x < width - 3) && (y > 2 && y < height - 3)) {
            brush.setCells(currentGrid, x, y, value);
        }
    }

    public int getGenNumber() {
        return currentGrid.getGenNumber();
    }

    public Color[] getColors() {
        return colorArray;
    }

    @Override
    public String getAdditionalMessage() {
        return additionalMessage;
    }

}
