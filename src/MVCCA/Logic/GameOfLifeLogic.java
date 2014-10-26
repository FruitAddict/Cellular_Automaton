package MVCCA.Logic;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.Grid;
import MVCCA.Logic.Utilities.Point;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;
import static MVCCA.Logic.Utilities.Point.*;

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
    final private Color[] colorArray = {Color.AQUAMARINE, Color.BLACK, Color.WHITE, Color.LIGHTPINK,Color.ORANGE,Color.DARKRED};

    //constructor taking w and height as arguements. inits the grid and clears it
    public GameOfLifeLogic(int width, int height){
        this.width=width;
        this.height=height;
        currentGrid = new Grid(width,height,1,0);
        clear();
    }
    @Override
    public void clear() {
        genNumber=0;
        //Initially sets values of the whole array to 0 (dead cells)
        currentGrid.clear();

    }
    @Override
    public Grid getCurrentGrid(){
        return currentGrid;
    }

    @Override
    public void genAdvance(){
        /**
         * Advances the generation by one by creating a snapshot of the current grid
         * for the resolver to work on- passing x and y coordinates of the cell
         * current value of that cell and the snapshot to the resolver method
         */
        Grid snapshot = currentGrid.copy();
        genNumber++;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid.set(i,j,resolve(i, j, currentGrid.getGrid()[i][j], snapshot));
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

    private int resolve(int x, int y, int currentValue, Grid snapshot) {
        /**
         * Resolver counts the number of neighbours of the given cell
         * based on the snapshot and returns a correct new value of the cell
         * based on rules here: http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
         * If the entry value of the cell is 0, it returns 0 instantly(border)
         */
        if (currentValue != 0) {
            int numOfNeighbours = 0;

            Point currentPosition = XY(x,y);
            for(int i=0;i<8;i++){
                Point check = currentPosition.merge(Utilities.Directions[i]);
                if(snapshot.get(check.getX(),check.getY()) >=2 ){
                    numOfNeighbours++;
                } else if(snapshot.get(check.getX(),check.getY()) == 0){
                    switch(i){
                        case 0: {
                            if(check.getX()==2 && check.getY()==2 && snapshot.get(width-4,height-4)>=2){
                                numOfNeighbours++;
                            } else if(check.getX()==2 && snapshot.get(width-4,check.getY())>=2){
                                numOfNeighbours++;
                            } else {
                                if(snapshot.get(check.getX(),height-4)>=2){
                                    numOfNeighbours++;
                                }
                            }
                            break;
                        }

                        case 1: {
                            if(snapshot.get(check.getX(),height-4)>=2){
                                numOfNeighbours++;
                            }
                            break;
                        }

                        case 2: {
                            if(check.getX()==width-3 && check.getY()==2 && snapshot.get(3,height-4)>=2){
                                numOfNeighbours++;
                            }else if (check.getX() == width-3 && snapshot.get(3,check.getY())>=2){
                                numOfNeighbours++;
                            } else {
                                if(snapshot.get(check.getX(),height-4)>=2){
                                    numOfNeighbours++;
                                }
                            }
                            break;
                        }

                        case 3: {
                            if(snapshot.get(width-4,check.getY())>=2){
                                numOfNeighbours++;
                            }
                            break;
                        }

                        case 4: {
                            if(snapshot.get(3,check.getY())>=2){
                                numOfNeighbours++;
                            }
                            break;
                        }

                        case 5: {
                            if(check.getX()==2 && check.getY() == height-3 && snapshot.get(width-4,3)>=2){
                                numOfNeighbours++;
                            } else if (check.getX()==2 && snapshot.get(width-4,check.getY())>=2){
                                numOfNeighbours++;
                            } else {
                                if(snapshot.get(check.getX(),3)>=2){
                                    numOfNeighbours++;
                                }
                            }
                            break;
                        }

                        case 6: {
                            if(snapshot.get(check.getX(),3)>=2){
                                numOfNeighbours++;
                            }
                            break;
                        }

                        case 7: {
                            if(check.getX()==width-3 && check.getY() == height-3 && snapshot.get(3,3)>=2){
                                numOfNeighbours++;
                            } else if(check.getX()==width-3 && snapshot.get(3,check.getY())>=2){
                                numOfNeighbours++;
                            } else {
                                if(snapshot.get(check.getX(),3)>=2){
                                    numOfNeighbours++;
                                }
                            }
                            break;
                        }
                    }
                }
            }


            if (currentValue == 1) {
                if (numOfNeighbours == 3) {
                    return 5;
                } else {
                    return 1;
                }

            }
            else if (currentValue >= 2) {
                if (numOfNeighbours < 2) {
                    return 1;
                }
                else if (numOfNeighbours == 2) {
                    return 2;
                }
                else if (numOfNeighbours == 3){
                    return 4;
                }
                else if (numOfNeighbours > 3 && numOfNeighbours <6) {
                    return 1;
                } else if(numOfNeighbours==7){
                    return 1;
                }
            }
            return 1;
        } else {
            return 0;
        }
    }
    @Override
    public void setCell(int x, int y, int value){
        //changes the value of a single cell in the current grid
        if((x>=3 && x <width-3) && (y>=3 && y<height-3)) {
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
