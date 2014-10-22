package MVCCA.Logic;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Logic class for Langton's Ant, extends abstract class Logic for use with controller. Same deal
 * as GameOfLife class
 */
public class LangtonsAntLogic extends Logic {

    private final int width;
    private final int height;
    private int[][] currentGrid;
    private final Color[] colorArray = {Color.AQUAMARINE, Color.BLACK, Color.GREEN, Color.BLUE, Color.PURPLE, Color.YELLOW, Color.RED};
    private ArrayList<Ant> antList;
    String additionalMessage;

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
        additionalMessage = "Number of ants: "+antList.size();
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

    public String getAdditionalMessage(){
        return additionalMessage;
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

        int antColorId;

        public Ant(int x, int y){
            System.out.println("New ant created succesfully at "+x+" "+y);
            positionX = x;
            positionY = y;
            antColorId =2+ new Random().nextInt(5);
        }

        public void update(int[][] snapshot){
            if(snapshot[positionX][positionY]==1){
                currentGrid[positionX][positionY]=antColorId;
                move(1);
            }
            else if(snapshot[positionX][positionY]!=1){
                currentGrid[positionX][positionY]=1;
                move(2);
            }
        }

        private void move(int side){
            switch(currentDirection){
                case 1: {
                    if(side==1){
                        if(positionX-1>2){
                        positionX-=1;} else {
                            positionX=width-4;
                        }
                        currentDirection = 4;
                        break;
                    } else {
                        if(positionX+1<width-3) {
                            positionX += 1;
                        }else {
                            positionX=3;
                        }
                        currentDirection = 2;
                        break;
                    }
                }

                case 2: {
                    if(side==1){
                        if(positionY-1>2) {
                            positionY -= 1;
                        }else {
                            positionY = height-4;
                        }
                        currentDirection=1;
                        break;
                    }else {
                        if(positionY+1<height-3) {
                            positionY += 1;
                        } else {
                            positionY=3;
                        }
                        currentDirection=3;
                        break;
                    }
                }

                case 3:{
                    if(side==1){
                        if(positionX+1<width-3) {
                            positionX += 1;
                        } else {
                            positionX=3;
                        }
                        currentDirection=2;
                        break;
                    }else {
                        if(positionX-1>2) {
                            positionX -= 1;
                        } else {
                            positionX=width-4;
                        }
                        currentDirection=4;
                        break;
                    }
                }

                case 4:{
                    if(side==1){
                        if(positionY+1<height-3) {
                            positionY += 1;
                        } else {
                            positionY = 3;
                        }
                        currentDirection=3;
                        break;
                    } else {
                        if( positionY-1 >2) {
                            positionY -= 1;
                        } else {
                            positionY=height-4;
                        }
                        currentDirection=1;
                        break;
                    }
                }
            }

        }
    }
}
