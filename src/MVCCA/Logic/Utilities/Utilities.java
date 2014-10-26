package MVCCA.Logic.Utilities;
import static MVCCA.Logic.Utilities.Point.*;

public class Utilities {

    public final static Point[] Directions = {XY(-1,-1) , XY(0,-1), XY(1,-1) , XY(-1,0), XY(1,0) ,XY(-1,1), XY(0,1), XY(1,1)};

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
