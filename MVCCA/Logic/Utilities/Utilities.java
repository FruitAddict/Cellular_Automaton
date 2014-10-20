package MVCCA.Logic.Utilities;

public class Utilities {

    public static int[][] copy2DArray(int[][] src, int width, int height){
        /**
         * Performs a deep copy of 2d array
         * Regular System.arraycopy and .clone() methods copied only the outermost layer
         * so this thing was needed, probably not the most efficient way to do this
         */
        int[][] result = new int[width][height];
        for(int i=0;i<width;i++){
            System.arraycopy(src[i], 0, result[i], 0, height);
        }
        return result;
    }
}
