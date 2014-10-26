package MVCCA.Logic;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.Grid;
import static MVCCA.Logic.Utilities.Point.*;
import MVCCA.Logic.Utilities.Point;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;
import java.util.Random;

/**
 * Cave Generator Logic
 */
public class CaveGeneratorLogic extends Logic {
    Grid currentGrid;
    int width;
    int height;
    Color[] colorArray = {Color.AQUAMARINE,Color.WHITE, Color.BLACK};

    public CaveGeneratorLogic(int width, int height){
        this.width=width;
        this.height=height;
        currentGrid = new Grid(width,height,1,0);
        clear();
    }

    @Override
    public void genAdvance() {
    }

    @Override
    public Grid getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void clear() {
        //Initially sets values of the whole array to 0 (dead cells)
        genNumber=0;
        currentGrid.clear();
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
        return "";
    }

    @Override
    public String getUtilityButtonName(){
        return "Next";
    }

    @Override
    public void performUtilityAction(){
        Grid snapshot = currentGrid.copy();
        for (int i =0 ;i <width;i++){
            for(int j=0;j<height;j++){
                currentGrid.set(i,j,resolve(i,j,snapshot.get(i,j),snapshot));
            }
        }
        genNumber++;

    }
    private int resolve(int x, int y, int currentValue, Grid snapshot) {
        /**
         * Resolver counts the number of neighbours of the given cell
         * based on the snapshot and returns a correct new value of the cell
         * based on Cave Generator rules found here:
         * http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels
         * If the entry value of the cell is 0, returns 0 instantly (border)
         */

        Point currentPosition = XY(x,y);

        if (currentValue != 0) {
            int numOfNeighbours = 0;

            for(int i=0;i<8;i++){
                Point check = currentPosition.merge(Utilities.Directions[i]);
                if(snapshot.get(check.getX(),check.getY()) == 2) {
                    numOfNeighbours++;
                }
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
                    currentGrid.getGrid()[i][j]=2;
                } else {
                    currentGrid.getGrid()[i][j]=1;
                }
            }
        }
    }
}
