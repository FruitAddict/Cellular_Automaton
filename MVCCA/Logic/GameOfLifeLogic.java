package MVCCA.Logic;

import javafx.scene.paint.Color;

/**
 * Created by FruitAddict on 2014-10-14.
 */
public class GameOfLifeLogic extends Logic {
    //holds the current version of grid, prone to changes
    private int[][] currentGrid;

    //width and height, assigned on creation
    final private int width;
    final private int height;

    //holds the current generation number
    private int genNumber;

    //color array
    final private Color[] colorArray = {Color.WHITE, Color.RED, Color.BLACK};

    //constructor taking w and height as arguements. inits the grid and clears it
    public GameOfLifeLogic(int width, int height){
        this.width=width;
        this.height=height;
        currentGrid = new int[width][height];
        clear();

    }

    public void clear() {
        //Initially sets values of the whole array to 0 (dead cells)
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid[i][j] = 0;
            }
        }
        /**
         * This part of the method fill the borders of array with 3-cell thick
         * walls. This operation simplifies the process of avoiding NPE's later on.
         */
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j < 3) {
                    currentGrid[i][j] = 2;
                } else if ((j > 2 || j < height - 3) && (i < 3 || i >= width - 3)) {
                    currentGrid[i][j] = 2;
                } else if (j >= height - 3) {
                    currentGrid[i][j] = 2;
                }
            }
        }

    }

    public int[][] getCurrentGrid(){
        return currentGrid;
    }

    private int[][] copyArray(int[][] src){
        /**
         * Performs a deep copy of 2d array
         * Regular System.arraycopy and .clone() methods copied only the outermost layer
         * so this thing was needed, probably not the most efficient way to do this
         */
        int[][] result = new int[width][height];
        for(int i=0;i<width;i++){
            System.arraycopy(src[i], 0, result[i], 0, height);
        }
        return result;
    }

    public void genAdvance(){
        /**
         * Advances the generation by one by creating a snapshot of the current grid
         * for the resolver to work on- passing x and y coordinates of the cell
         * current value of that cell and the snapshot to the resolver method
         */
        int[][] snapshot = copyArray(currentGrid);
        genNumber++;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid[i][j] = resolve(i, j, currentGrid[i][j], snapshot);
            }
        }

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

    public void setCell(int x, int y, int value){
        //changes the value of a single cell in the current grid
        currentGrid[x][y] = value;
    }

    public int getGenNumber(){
        return genNumber;
    }

    public Color[] getColors(){
        return colorArray;
    }

}
