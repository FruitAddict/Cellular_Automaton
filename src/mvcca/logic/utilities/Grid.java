package mvcca.logic.utilities;

import java.io.Serializable;

/**
 * Created by FruitAddict on 2014-10-24.
 */
public class Grid implements Serializable {
    private int[][] storage;
    private int width;
    private int height;
    private int planeId;
    private int borderId;
    private int genNumber;
    private String ownerName; //for checking if the logic type is correct during loading (See GridIO class)

    public Grid(int width, int height, int planeId, int borderId, String ownerName) {
        this.planeId = planeId;
        this.borderId = borderId;
        this.width = width;
        this.height = height;
        this.ownerName = ownerName;
        genNumber = 0;
        storage = new int[width][height];
    }

    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                storage[i][j] = planeId;
            }
        }
        /**
         * This part of the method fill the borders of array with 3-cell thick
         * walls. This operation simplifies the process of avoiding NPE's later on.
         */
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j < 1) {
                    storage[i][j] = borderId;
                } else if ((j > 0 || j < height - 1) && (i < 1 || i >= width - 1)) {
                    storage[i][j] = borderId;
                } else if (j >= height - 1) {
                    storage[i][j] = borderId;
                }
            }
        }
    }

    public int get(int x, int y) {
        return storage[x][y];
    }

    public void set(int x, int y, int value) {
        storage[x][y] = value;
    }

    public void setGenNumber(int n){
        genNumber=n;
    }

    public int getGenNumber(){
        return genNumber;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public int[][] getGrid() {
        return storage;
    }

    public Grid copy() {
        Grid temp = new Grid(width, height, planeId, borderId,ownerName);
        temp.storage = Utilities.copy2DArray(this.getGrid(), width, height);
        return temp;
    }
}
