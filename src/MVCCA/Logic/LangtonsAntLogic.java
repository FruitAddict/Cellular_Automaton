package MVCCA.Logic;

import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Utilities.Grid;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Logic class for Langton's Ant, extends abstract class Logic for use with controller. Same deal
 * as GameOfLife class
 */
public class LangtonsAntLogic extends Logic {

    private final int width;
    private final int height;
    private Grid currentGrid;
    private final Color[] colorArray = {Color.AQUAMARINE, Color.BLACK, Color.GREEN, Color.BLUE, Color.PURPLE, Color.YELLOW, Color.RED};
    private CopyOnWriteArrayList<Ant> antList;
    String additionalMessage;

    public LangtonsAntLogic(int width, int height) {
        this.width = width;
        this.height = height;
        currentGrid = new Grid(width,height,1,0);
        clear();
        antList = new CopyOnWriteArrayList<>();
    }
    @Override
    public void clear() {
        genNumber=0;
        //Initially sets values of the whole array to 0 (dead cells)
        currentGrid.clear();
        antList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void genAdvance() {
        Grid snapshot = currentGrid.copy();
        genNumber++;
        additionalMessage = "Number of ants: "+antList.size();
        for(Ant a: antList){
            a.update(snapshot, antList);
        }
    }

    @Override
    public Grid getCurrentGrid() {
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

    @Override
    public String getUtilityButtonName(){
        return "";
    }

    @Override
    public void performUtilityAction(){
        //nothing
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
        private int positionX;
        private int positionY;

        private int currentDirection=3; //left=0 right =1

        private int antColorId;

        public Ant(int x, int y){
            positionX = x;
            positionY = y;
            antColorId =2+ new Random().nextInt(5);
        }

        public void update(Grid snapshot, CopyOnWriteArrayList<Ant> antArrayList){
            for(Ant ant : antArrayList){
                if(ant != this){
                    if(ant.getPositionX() == this.getPositionX() && ant.getPositionY() == this.getPositionY()){
                        antArrayList.remove(ant);
                    }
                }
            }
            if(snapshot.get(positionX,positionY)==1){
                currentGrid.set(positionX,positionY,antColorId);
                move(1);
            }
            else if(snapshot.get(positionX,positionY)!=1){
                currentGrid.set(positionX,positionY,1);
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

        public int getPositionX(){
            return  positionX;
        }
        public int getPositionY(){
            return positionY;
        }
    }
}
