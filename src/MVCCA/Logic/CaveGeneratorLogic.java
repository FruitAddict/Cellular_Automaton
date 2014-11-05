package MVCCA.Logic;

import MVCCA.Logic.Abstract.Brush;
import MVCCA.Logic.Abstract.Logic;
import MVCCA.Logic.Abstract.Resolver;
import MVCCA.Logic.Utilities.Grid;
import MVCCA.Logic.Utilities.Utilities;
import javafx.scene.paint.Color;

/**
 * Cave Generator Logic
 */
public class CaveGeneratorLogic extends Logic {
    private Grid currentGrid;
    private int width;
    private int height;
    private Color[] colorArray = {Color.web("827970"), Color.WHITE, Color.BLACK};
    private Resolver resolver;
    private Brush brush;

    public CaveGeneratorLogic(int width, int height) {
        this.width = width;
        this.height = height;
        /**
         * HARDCODED RESOLVER FOR CAVE GENERATOR
         */
        setResolver(new Resolver() {
            /**
             * Resolver counts the number of neighbours of the given cell
             * based on the snapshot and returns a correct new value of the cell
             * based on Cave Generator rules found here:
             * http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels
             * If the entry value of the cell is 0, returns 0 instantly (border)
             */
            @Override
            public int ifDead(int n) {
                if (n >= 5) {
                    return 2;
                } else {
                    return 1;
                }
            }

            @Override
            public int ifAlive(int n) {
                if (n >= 4) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        /**
         * END OF HARDCODED RESOLVER
         */
        currentGrid = new Grid(width, height, 1, 0);
        Utilities.applyBrush(Utilities.getBasicBrushData(), this);
        clear();
    }

    @Override
    public void genAdvance() {
    }

    @Override
    public String getName(){
        return "Cave Generator";
    }

    @Override
    public Grid getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void clear() {
        //Initially sets values of the whole array to 0 (dead cells)
        genNumber = 0;
        currentGrid.clear();
    }

    @Override
    public void setCell(int x, int y, int value) {
        clear();
        Utilities.randomFill(this, width, height, 50, 2);
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
    public String getUtilityButtonName() {
        return "Next";
    }

    @Override
    public void performUtilityAction() {
        Grid snapshot = currentGrid.copy();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid.set(i, j, resolve(i, j, snapshot.get(i, j), snapshot));
            }
        }
        genNumber++;
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

    private int resolve(int x, int y, int currentValue, Grid snapshot) {

        if (currentValue != 0) {
            int numOfNeighbours = Utilities.getNumberOfMooreNeighbours(x, y, width, height, snapshot);

            if (currentValue == 1) {
                return resolver.ifDead(numOfNeighbours);
            }
            if (currentValue == 2) {
                return resolver.ifAlive(numOfNeighbours);
            }
            return 1;
        } else {
            return 0;
        }
    }

}
