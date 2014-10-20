package MVCCA.Logic;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Logic class for Langton's Ant, extends abstract class Logic for use with controller. Same deal
 * as GameOfLife class
 */
public class LangtonsAntLogic extends Logic {

    private final int width;
    private final int height;
    private int[][] currentGrid;
    private final Color[] colorArray = {Color.PURPLE, Color.BLACK, Color.BLUE, Color.BLUE, Color.PURPLE, Color.YELLOW};
    private ArrayList<Ant> antList;

    public LangtonsAntLogic(int width, int height) {
        this.width = width;
        this.height = height;
        currentGrid = new int[width][height];
        clear();
        setLogicName("Langton's Ant");
        antList = new ArrayList<>();
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
        antList = new ArrayList<>();
    }

    @Override
    public void genAdvance() {
        int[][] snapshot = Utilities.copy2DArray(currentGrid,width,height);
        genNumber++;
        for(Ant a: antList){
            a.update(snapshot);
        }
    }

    @Override
    public int[][] getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void setCell(int x, int y, int value) {
        createNewAnt(x, y);
    }

    @Override
    public int getGenNumber() {
        return genNumber;
    }

    @Override
    public Color[] getColors() {
        return colorArray;
    }

    private void createNewAnt(int x, int y) {
        /**
         * Creates new ant object in given position ( it must be inside boundaries)
         */
        if ((x > 3 && x < width - 3) && (y > 3 && y < height - 3)) {
            antList.add(new Ant(x,y));
        }
    }

    private class Ant{
        /**
         * Ant class, contains current coordinates, method to determine current direction
         * and method to advance itself to next generation
         */
        int positionX;
        int positionY;

        int currentDirection=3; //left=0 right =1

        public Ant(int x, int y){
            System.out.println("New ant created succesfully at "+x+" "+y);
            positionX = x;
            positionY = y;
        }

        public void update(int[][] snapshot){
            System.out.println("Ant at "+positionX+" "+positionY);
            if(snapshot[positionX][positionY]==1){
                currentGrid[positionX][positionY]=2;
                move(1);
            }
            else if(snapshot[positionX][positionY]==2){
                currentGrid[positionX][positionY]=1;
                move(2);
            }
        }

        private void move(int side){
            switch(currentDirection){
                case 1: {
                    if(side==1){
                        positionX-=1;
                        currentDirection = 4;
                        break;
                    } else {
                        positionX+=1;
                        currentDirection = 2;
                        break;
                    }
                }

                case 2: {
                    if(side==1){
                        positionY-=1;
                        currentDirection=1;
                        break;
                    }else {
                        positionY+=1;
                        currentDirection=3;
                        break;
                    }
                }

                case 3:{
                    if(side==1){
                        positionX+=1;
                        currentDirection=2;
                        break;
                    }else {
                        positionX-=1;
                        currentDirection=4;
                        break;
                    }
                }

                case 4:{
                    if(side==1){
                        positionY+=1;
                        currentDirection=3;
                        break;
                    } else {
                        positionY-=1;
                        currentDirection=1;
                        break;
                    }
                }
            }

        }
    }
}
