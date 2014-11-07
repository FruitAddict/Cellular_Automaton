package mvcca.logic;

import mvcca.logic.abstracted.Brush;
import mvcca.logic.abstracted.Logic;
import mvcca.logic.abstracted.Resolver;
import mvcca.logic.utilities.Grid;
import mvcca.logic.utilities.Utilities;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * logic class for Langton's Ant, extends abstract class logic for use with controller.
 * Doesn't do anything with Resolver interface, as this automaton is not
 * doable with those simple rulesets in the current state of this program/
 */
public class LangtonsAntLogic extends Logic {

    private final int width;
    private final int height;
    private Grid currentGrid;
    private final Color[] colorArray = {Color.web("827970"), Color.BLACK, Color.web("94FF00"), Color.web("E89B0C"), Color.web("FF0005"), Color.web("440CE8"), Color.web("0DFCFF")};
    private CopyOnWriteArrayList<Ant> antList;
    private String additionalMessage;
    private Brush brush;

    public LangtonsAntLogic(int width, int height) {
        this.width = width;
        this.height = height;
        currentGrid = new Grid(width, height, 1, 0,getName());
        Utilities.applyBrush(Utilities.getBasicBrushData(), this);
        clear();
        antList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void clear() {
        currentGrid.setGenNumber(0);
        //Initially sets values of the whole array to 0 (dead cells)
        currentGrid.clear();
        antList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void genAdvance() {
        Grid snapshot = currentGrid.copy();
        currentGrid.setGenNumber(currentGrid.getGenNumber()+1);
        additionalMessage = "Number of ants: " + antList.size();
        for (Ant a : antList) {
            a.update(snapshot, antList);
        }
    }

    @Override
    public Grid getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void setCurrentGrid(Grid grid){
        currentGrid = grid;
    }

    @Override
    public void setCell(int x, int y, int value) {
        createNewAnt(x, y);
    }

    @Override
    public int getGenNumber() {
        return currentGrid.getGenNumber();
    }

    @Override
    public Color[] getColors() {
        return colorArray;
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
        brush = b;
    }

    @Override
    public Brush getBrush() {
        return brush;
    }
    @Override
    public String getName(){
        return "Langton's Ant";
    }


    public String getAdditionalMessage() {
        return additionalMessage;
    }

    private void createNewAnt(int x, int y) {
        /**
         * Creates new ant object in given position ( it must be inside boundaries)
         */
        if ((x > 3 && x < width - 3) && (y > 3 && y < height - 3)) {
            antList.add(new Ant(x, y));
        }
    }

    private class Ant {
        /**
         * Ant class, contains current coordinates, method to determine current direction
         * and method to advance itself to next generation
         */
        private int positionX;
        private int positionY;

        private int currentDirection = 3; //left=0 right =1

        private int antColorId;

        public Ant(int x, int y) {
            positionX = x;
            positionY = y;
            antColorId = 2 + new Random().nextInt(5);
        }

        public void update(Grid snapshot, CopyOnWriteArrayList<Ant> antArrayList) {
            for (Ant ant : antArrayList) {
                if (ant != this) {
                    if (ant.getPositionX() == this.getPositionX() && ant.getPositionY() == this.getPositionY()) {
                        antArrayList.remove(ant);
                    }
                }
            }
            if (snapshot.get(positionX, positionY) == 1) {
                currentGrid.set(positionX, positionY, antColorId);
                move(1);
            } else if (snapshot.get(positionX, positionY) != 1) {
                currentGrid.set(positionX, positionY, 1);
                move(2);
            }
        }

        private void move(int side) {
            switch (currentDirection) {
                case 1: {
                    if (side == 1) {
                        if (positionX - 1 > 0) {
                            positionX -= 1;
                        } else {
                            positionX = width - 2;
                        }
                        currentDirection = 4;
                        break;
                    } else {
                        if (positionX + 1 < width - 1) {
                            positionX += 1;
                        } else {
                            positionX = 1;
                        }
                        currentDirection = 2;
                        break;
                    }
                }

                case 2: {
                    if (side == 1) {
                        if (positionY - 1 > 0) {
                            positionY -= 1;
                        } else {
                            positionY = height - 2;
                        }
                        currentDirection = 1;
                        break;
                    } else {
                        if (positionY + 1 < height - 1) {
                            positionY += 1;
                        } else {
                            positionY = 1;
                        }
                        currentDirection = 3;
                        break;
                    }
                }

                case 3: {
                    if (side == 1) {
                        if (positionX + 1 < width - 1) {
                            positionX += 1;
                        } else {
                            positionX = 1;
                        }
                        currentDirection = 2;
                        break;
                    } else {
                        if (positionX - 1 > 0) {
                            positionX -= 1;
                        } else {
                            positionX = width - 2;
                        }
                        currentDirection = 4;
                        break;
                    }
                }

                case 4: {
                    if (side == 1) {
                        if (positionY + 1 < height - 1) {
                            positionY += 1;
                        } else {
                            positionY = 1;
                        }
                        currentDirection = 3;
                        break;
                    } else {
                        if (positionY - 1 > 0) {
                            positionY -= 1;
                        } else {
                            positionY = height - 2;
                        }
                        currentDirection = 1;
                        break;
                    }
                }
            }

        }

        public int getPositionX() {
            return positionX;
        }

        public int getPositionY() {
            return positionY;
        }
    }
}
