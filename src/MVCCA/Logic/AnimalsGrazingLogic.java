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
 * Animals on a plain. Not a cellular automaton, more like simulation. Currently not plugged in.
 */
public class AnimalsGrazingLogic extends Logic {
    private final int width;
    private final int height;
    private Grid currentGrid;
    private final Color[] colorArray = {Color.web("827970"), Color.BLACK, Color.GREEN, Color.RED};
    private CopyOnWriteArrayList<Animal> animalList;
    private Resolver resolver;
    private Brush brush;
    private Random rng;


    public AnimalsGrazingLogic(int x, int y) {
        rng = new Random();
        width = x;
        height = y;
        currentGrid = new Grid(width, height, 1, 0, getName());
        currentGrid.clear();
        animalList = new CopyOnWriteArrayList<>();
        Utilities.applyBrush(Utilities.getBasicBrushData(), this);

        /**
         * HARDCODED RESOLVER WITH RANDOMNESS
         */
        resolver = new Resolver() {

            @Override
            public int ifDead(int n) {
                if (n == 3 && rng.nextInt(101) <= 2) {
                    return 2;
                } else {
                    return 1;
                }
            }

            @Override
            public int ifAlive(int n) {
                if (n > 0 && n < 4) {
                    return 2;
                } else if (rng.nextInt(101) <= 5) {
                    return 2;
                } else {
                    return 1;
                }

            }
        };
        /**
         * /HARDCODED RESOLVER
         */
    }

    @Override
    public void clear() {
        currentGrid.setGenNumber(0);
        currentGrid.clear();
    }

    @Override
    public void genAdvance() {
        Grid snapshot = currentGrid.copy();
        currentGrid.setGenNumber(currentGrid.getGenNumber()+1);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGrid.set(i, j, resolveCell(i, j, snapshot, currentGrid.get(i, j)));
            }
        }
        for (Animal a : animalList) {
            a.update();
        }
    }

    @Override
    public Grid getCurrentGrid() {
        return currentGrid;
    }

    @Override
    public void setCell(int x, int y, int value) {
        if (x > 0 && y > 0 && x < width - 1 && y < height - 1)
            animalList.add(new Animal(x, y));
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
    public String getAdditionalMessage() {
        return "Animals alive: " + animalList.size();
    }

    @Override
    public String getUtilityButtonName() {
        return "Random";
    }

    @Override
    public void performUtilityAction() {
        Utilities.randomFill(this, width, height, 10, 2);
    }

    @Override
    public void setResolver(Resolver r) {
        //nothing
    }

    @Override
    public String getName(){
        return "Animals Grazing";
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
    public void setCurrentGrid(Grid grid){
        currentGrid = grid;
    }

    private int resolveCell(int x, int y, Grid snapshot, int currentValue) {
        if (currentValue != 0) {
            int numberOfNeighbours = Utilities.getNumberOfMooreNeighbours(x, y, width, height, snapshot);
            if (currentValue == 1) {
                return ((resolver != null) ? resolver.ifDead(numberOfNeighbours) : 1);
            } else if (currentValue >= 2) {
                return ((resolver != null) ? resolver.ifAlive(numberOfNeighbours) : 1);
            }
        } else {
            return 0;
        }
        return 1;
    }

    private class Animal {
        private int positionX;
        private int positionY;
        private int lifeSpan;
        private final int lifeSpanPerGrassTile = 25;
        private int generationsFromLastReproduction;
        private int nextDirection;
        private boolean fertile;

        public Animal(int x, int y) {
            positionX = x;
            positionY = y;
            lifeSpan = 100;
            currentGrid.set(positionX, positionY, 3);
            fertile = true;
        }

        public void update() {
            currentGrid.set(positionX, positionY, 1);
            lifeSpan--;
            if (lifeSpan <= 0) {
                animalList.remove(this);
                System.out.println("Animal died.");
                if (positionX > 2 && positionY > 2 && positionX < width - 3 && positionY < height - 3)
                    for (int i = -2; i < 3; i++) {
                        for (int j = -2; j < 3; j++) {
                            currentGrid.set(positionX + i, positionY + j, 2);
                        }
                    }

            } else {
                if (!fertile) {
                    generationsFromLastReproduction++;
                    if (generationsFromLastReproduction > 500) {
                        fertile = true;
                        generationsFromLastReproduction=0;
                    }
                }
                nextDirection = 1 + rng.nextInt(4);
                switch (nextDirection) {
                    case 1: {
                        if (positionY - 1 > 0) {
                            if (currentGrid.get(positionX, positionY - 1) == 2) {
                                lifeSpan += lifeSpanPerGrassTile;
                                currentGrid.set(positionX, positionY, 1);
                                positionY -= 1;
                                currentGrid.set(positionX, positionY, 3);
                                break;
                            } else {
                                currentGrid.set(positionX, positionY, 1);
                                positionY -= 1;
                                currentGrid.set(positionX, positionY, 3);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    case 2: {
                        if (positionX + 1 < width - 1) {
                            if (currentGrid.get(positionX + 1, positionY) == 2) {
                                lifeSpan += lifeSpanPerGrassTile;
                                currentGrid.set(positionX, positionY, 1);
                                positionX += 1;
                                currentGrid.set(positionX, positionY, 3);
                                break;
                            } else {
                                currentGrid.set(positionX, positionY, 1);
                                positionX += 1;
                                currentGrid.set(positionX, positionY, 3);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    case 3: {
                        if (positionY + 1 < height - 1) {
                            if (currentGrid.get(positionX, positionY + 1) == 2) {
                                lifeSpan += lifeSpanPerGrassTile;
                                currentGrid.set(positionX, positionY, 1);
                                positionY += 1;
                                currentGrid.set(positionX, positionY, 3);
                                break;
                            } else {
                                currentGrid.set(positionX, positionY, 1);
                                positionY += 1;
                                currentGrid.set(positionX, positionY, 3);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    case 4: {
                        if (positionX - 1 > 1) {
                            if (currentGrid.get(positionX - 1, positionY) == 2) {
                                lifeSpan += lifeSpanPerGrassTile;
                                currentGrid.set(positionX, positionY, 1);
                                positionX -= 1;
                                currentGrid.set(positionX, positionY, 3);
                                break;
                            } else {
                                currentGrid.set(positionX, positionY, 1);
                                positionX -= 1;
                                currentGrid.set(positionX, positionY, 3);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    default: {
                        break;
                    }
                }
                for (Animal a : animalList) {
                    if (a != this) {
                        if (a.positionX == this.positionX && a.positionY == this.positionY && a.fertile && this.fertile) {
                            animalList.add(new Animal(positionX, positionY));
                            System.out.println("Animal was born!");
                            a.fertile = false;
                            this.fertile = false;
                        }
                    }
                }
            }
        }
    }
}
