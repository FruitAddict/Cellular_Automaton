package MVCCA.Logic;

import MVCCA.Logic.Abstract.Brush;
import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Abstract.Resolver;
import MVCCA.Logic.Utilities.Grid;
import javafx.scene.paint.Color;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Animals on a plain. Not a cellular automaton, more like simulation. Currently not plugged in.
 */
public class AnimalLogic extends Logic {
    private final int width;
    private final int height;
    private Grid currentGrid;
    private final Color[] colorArray = {Color.web("827970"), Color.BLACK, Color.web("94FF00"), Color.web("E89B0C"), Color.web("FF0005"), Color.web("440CE8"), Color.web("0DFCFF")};
    private CopyOnWriteArrayList<Animal> animalList;


    public AnimalLogic(int x, int y){
        width=x;
        height=y;
        currentGrid = new Grid(width,height,1,0);
    }
    @Override
    public void clear() {
        genNumber=0;
        currentGrid.clear();
    }

    @Override
    public void genAdvance() {

    }

    @Override
    public Grid getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void setCell(int x, int y, int value) {
        animalList.add(new Animal(x,y));
    }

    @Override
    public int getGenNumber() {
        return 0;
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
        return "";
    }

    @Override
    public void performUtilityAction() {
        //nothing
    }

    @Override
    public void setResolver(Resolver r) {
        //nothing
    }

    @Override
    public void setBrush(Brush b) {
        //nothing
    }

    @Override
    public Brush getBrush() {
        return null;
    }

    private class Animal{
        int positionX;
        int positionY;

            public Animal(int x, int y){
                positionX=x;
                positionY=y;
            }

        public void update(){

        }
    }
}
