package MVCCA.Logic.Utilities;

/**
 * Created by FruitAddict on 2014-10-24.
 */
public class Grid {
    public int[][] storage;
    int width;
    int height;
    int planeId;
    int borderId;

    public Grid(int width, int height, int planeId, int borderId){
        this.planeId = planeId;
        this.borderId = borderId;
        this.width=width;
        this.height=height;
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
                if (j < 3) {
                    storage[i][j] = borderId;
                } else if ((j > 2 || j < height - 3) && (i < 3 || i >= width - 3)) {
                    storage[i][j] = borderId;
                } else if (j >= height - 3) {
                    storage[i][j] = borderId;
                }
            }
        }
    }

    public int get(int x, int y){
        return storage[x][y];
    }

    public int[][] getGrid(){
        return storage;
    }
}
