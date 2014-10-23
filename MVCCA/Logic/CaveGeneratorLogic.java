package MVCCA.Logic;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by FruitAddict on 2014-10-23.
 */
public class CaveGeneratorLogic extends Logic {
    int[][] currentGrid;
    int width;
    int height;
    Color[] colorArray = {Color.TEAL,Color.WHITE, Color.BLACK};

    public CaveGeneratorLogic(int width, int height){
        this.width=width;
        this.height=height;
        currentGrid = new int[width][height];
        clear();
        setLogicName("Cave Generator");

    }

    @Override
    public void genAdvance() {
    }

    @Override
    public int[][] getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void clear() {
        //Initially sets values of the whole array to 0 (dead cells)
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid[i][j] = 1;
            }
        }
        /**
         * This part of the method fill the borders of array with 3-cell thick
         * walls. This operation simplifies the process of avoiding NPE's later on.
         */
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j < 3) {
                    currentGrid[i][j] = 0;
                } else if ((j > 2 || j < height - 3) && (i < 3 || i >= width - 3)) {
                    currentGrid[i][j] = 0;
                } else if (j >= height - 3) {
                    currentGrid[i][j] = 0;
                }
            }
        }
    }

    @Override
    public void setCell(int x, int y, int value) {
        clear();
        fillRandom(50);
    }

    @Override
    public int getGenNumber() {
        return genNumber;
    }

    @Override
    public Color[] getColors() {
        return colorArray;
    }

    @Override
    public String getAdditionalMessage() {
        return "Please adv. generations manually using Next button";
    }

    @Override
    public String getUtilityButtonName(){
        return "Next";
    }

    @Override
    public void performUtilityAction(){
        int[][] snapshot = Utilities.copy2DArray(currentGrid,width,height);
        for (int i =0 ;i <width;i++){
            for(int j=0;j<height;j++){
                currentGrid[i][j] = resolve(i,j,snapshot[i][j],snapshot);
            }
        }
        genNumber++;

    }
    private int resolve(int x, int y, int currentValue, int[][] snapshot) {
        /**
         * Resolver counts the number of neighbours of the given cell
         * based on the snapshot and returns a correct new value of the cell
         * based on Cave Generator rules found here:
         * http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels
         * If the entry value of the cell is 0, returns 0 instantly (border)
         */
        if (currentValue != 0) {
            int numOfNeighbours = 0;
            if (snapshot[x - 1][y - 1] == 2) {
                numOfNeighbours++;
            }
            if (snapshot[x][y - 1] == 2) {
                numOfNeighbours++;
            }
            if (snapshot[x + 1][y - 1] == 2) {
                numOfNeighbours++;
            }
            if (snapshot[x - 1][y] == 2) {
                numOfNeighbours++;
            }
            if (snapshot[x + 1][y] == 2) {
                numOfNeighbours++;
            }
            if (snapshot[x - 1][y + 1] == 2) {
                numOfNeighbours++;
            }
            if (snapshot[x][y + 1] == 2) {
                numOfNeighbours++;
            }
            if (snapshot[x + 1][y + 1] == 2) {
                numOfNeighbours++;
            }

            if (currentValue == 1) {
                if (numOfNeighbours >=5) {
                    return 2;
                } else {
                    return 1;
                }

            }
            if (currentValue == 2) {
                if (numOfNeighbours>=4 ) {
                    return 2;
                } else {
                    return 1;
                }
            }
            System.out.println("Something went terribly wrong, are you sure that the board is all 0,1 and 2's?");
            return 1;
        } else {
            return 0;
        }
    }

    public void fillRandom(int percent){
        /**
         * Fills the grid with random pixels
         */
        Random rng = new Random();
        for(int i = 4; i< width-3; i++){
            for(int j =4; j<height-3;j++){
                int random = rng.nextInt(101);
                if(random<=percent){
                    currentGrid[i][j]=2;
                } else {
                    currentGrid[i][j]=1;
                }
            }
        }
    }
}
