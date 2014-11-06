package MVCCA.Logic;

import MVCCA.Logic.Abstract.Brush;
import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Abstract.Resolver;
import MVCCA.Logic.Utilities.Grid;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;

/**
 * Created by FruitAddict on 2014-11-05.
 */
public class WireworldLogic extends Logic {
    private  Color[] colorArray = {Color.web("827970"), Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW};
    private int width;
    private int height;
    private Grid currentGrid;
    private Resolver resolver;
    private Brush brush;
    private int setCellValue;

    public WireworldLogic(int x, int y) {
        width = x;
        height = y;
        currentGrid = new Grid(width, height, 1, 0);
        currentGrid.clear();
        setCellValue = 4;
        /**
         * HARDCODED RESOLVER
         */
        resolver = new Resolver() {
            @Override
            public int ifDead(int n) {
                return 1;
            }

            @Override
            public int ifAlive(int n) {
                if(n==1 || n == 2){
                    return 2;
                } else {
                    return 4;
                }
            }
        };
        /**
         * Setting basic brush
         */

        Utilities.applyBrush(Utilities.getBasicBrushData(), this);

        clear();
    }
    @Override
    public void clear() {
        genNumber=0;
        currentGrid.clear();
    }

    @Override
    public void genAdvance() {
        genNumber++;
        Grid snapshot = currentGrid.copy();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid.set(i, j, resolveCell(i, j, snapshot, currentGrid.get(i, j)));
            }
        }
    }

    @Override
    public Grid getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void setCell(int x, int y, int value) {
        if ((x > 2 && x < width - 3) && (y > 2 && y < height - 3)) {
            brush.setCells(currentGrid, x, y, setCellValue);
        }
    }

    @Override
    public int getGenNumber() {
        return genNumber;
    }

    @Override
    public String getName(){
        return "Wireworld";
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
        return (setCellValue==4)?"Wire":"Electron";
    }

    @Override
    public void performUtilityAction() {
        if(setCellValue==4){
            setCellValue=2;
        }else {
            setCellValue=4;
        }
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

    private int resolveCell(int x, int y, Grid snapshot, int value){
        if(value != 0) {
            if(value == 1){
                return resolver.ifDead(0);
            }
            else if(value == 2){
                return 3;
            } else if (value == 3){
                return 4;
            } else if (value==4){
                int numberOfNeighbours = Utilities.getNumberOfMooreNeighboursOnly2(x, y, width, height, snapshot);
                return resolver.ifAlive(numberOfNeighbours);
            }
        }else {
            return 0;
        }
        return 1;
    }
}
