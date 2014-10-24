package MVCCA.Logic;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.Grid;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;

/**
 * Logic class for Conway's game of life. Extends abstract Logic class (for use with controller)
 * contains rules for advancing to the new generation as well as making chosen cells alive
 * stores its colors array for use with some viewer (breaks mvc but just a little: D)
 */
public class GameOfLifeLogic extends Logic {
    //holds the current version of grid, prone to changes
    Grid currentGrid;

    //width and height, assigned on creation
    final private int width;
    final private int height;

    //additional message, can be anything. Can be retrieved by controller
    String additionalMessage;

    //color array
    final private Color[] colorArray = {Color.BLACK, Color.RED, Color.AQUAMARINE};

    //constructor taking w and height as arguements. inits the grid and clears it
    public GameOfLifeLogic(int width, int height){
        this.width=width;
        this.height=height;
        currentGrid = new Grid(width,height,0,2);
        clear();
    }
    @Override
    public void clear() {
        genNumber=0;
        //Initially sets values of the whole array to 0 (dead cells)
        currentGrid.clear();

    }
    @Override
    public int[][] getCurrentGrid(){
        return currentGrid.getGrid();
    }

    @Override
    public void genAdvance(){
        /**
         * Advances the generation by one by creating a snapshot of the current grid
         * for the resolver to work on- passing x and y coordinates of the cell
         * current value of that cell and the snapshot to the resolver method
         */
        int[][] snapshot = Utilities.copy2DArray(currentGrid.getGrid(), width, height);
        genNumber++;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid.getGrid()[i][j] = resolve(i, j, currentGrid.getGrid()[i][j], snapshot);
            }
        }

    }

    @Override
    public String getUtilityButtonName(){
        return "";
    }
    @Override
    public void performUtilityAction(){
        //nothing
    }

    private int resolve(int x, int y, int currentValue, int[][] snapshot) {
        /**
         * Resolver counts the number of neighbours of the given cell
         * based on the snapshot and returns a correct new value of the cell
         * based on rules here: http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
         * If the entry value of the cell is 2, it returns 2 instantly(border)
         */
        if (currentValue != 2) {
            int numOfNeighbours = 0;
            if (snapshot[x - 1][y - 1] == 1) {
                numOfNeighbours++;
            }
            if (snapshot[x][y - 1] == 1) {
                numOfNeighbours++;
            }
            if (snapshot[x + 1][y - 1] == 1) {
                numOfNeighbours++;
            }
            if (snapshot[x - 1][y] == 1) {
                numOfNeighbours++;
            }
            if (snapshot[x + 1][y] == 1) {
                numOfNeighbours++;
            }
            if (snapshot[x - 1][y + 1] == 1) {
                numOfNeighbours++;
            }
            if (snapshot[x][y + 1] == 1) {
                numOfNeighbours++;
            }
            if (snapshot[x + 1][y + 1] == 1) {
                numOfNeighbours++;
            }

            if (currentValue == 0) {
                if (numOfNeighbours == 3) {
                    return 1;
                } else {
                    return 0;
                }

            }
            if (currentValue == 1) {
                if (numOfNeighbours < 2) {
                    return 0;
                }
                else if (numOfNeighbours == 2 || numOfNeighbours == 3) {
                    return 1;
                }
                else if (numOfNeighbours > 3) {
                    return 0;
                }
            }
            System.out.println("Something went terribly wrong, are you sure that the board is all 0,1 and 2's?");
            return 1;
        } else {
            return 2;
        }
    }
    @Override
    public void setCell(int x, int y, int value){
        //changes the value of a single cell in the current grid
        if((x>3 && x <width-3) && (y>3 && y<height-3)) {
            currentGrid.getGrid()[x][y] = value;
        }
    }

    public int getGenNumber(){
        return genNumber;
    }

    public Color[] getColors(){
        return colorArray;
    }

    @Override
    public String getAdditionalMessage(){
        return additionalMessage;
    }

}
