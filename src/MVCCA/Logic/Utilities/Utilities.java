package MVCCA.Logic.Utilities;
import MVCCA.Logic.Abstract.Logic;

import java.util.Random;

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

    public static int getNumberOfNeighbours(int x, int y,int width, int height, Grid snapshot){
        /**
         * returns Number of Neighbours in von N. neighbourhood, goes over the borders
         * if border is met.
         */
        int numOfNeighbours=0;
        Point currentPosition = XY(x,y);
        for(int i=0;i<8;i++){
            Point check = currentPosition.merge(Utilities.Directions[i]);
            if(snapshot.get(check.getX(),check.getY()) >=2 ){
                numOfNeighbours++;
            } else if(snapshot.get(check.getX(),check.getY()) == 0){
                switch(i){
                    case 0: {
                        if(check.getX()==0 && check.getY()==0 && snapshot.get(width-2,height-2)>=2){
                            numOfNeighbours++;
                        } else if(check.getX()==0 && snapshot.get(width-2,check.getY())>=2){
                            numOfNeighbours++;
                        } else {
                            if(snapshot.get(check.getX(),height-2)>=2){
                                numOfNeighbours++;
                            }
                        }
                        break;
                    }

                    case 1: {
                        if(snapshot.get(check.getX(),height-2)>=2){
                            numOfNeighbours++;
                        }
                        break;
                    }

                    case 2: {
                        if(check.getX()==width-1 && check.getY()==0 && snapshot.get(1,height-2)>=2){
                            numOfNeighbours++;
                        }else if (check.getX() == width-1 && snapshot.get(1,check.getY())>=2){
                            numOfNeighbours++;
                        } else {
                            if(snapshot.get(check.getX(),height-2)>=2){
                                numOfNeighbours++;
                            }
                        }
                        break;
                    }

                    case 3: {
                        if(snapshot.get(width-2,check.getY())>=2){
                            numOfNeighbours++;
                        }
                        break;
                    }

                    case 4: {
                        if(snapshot.get(1,check.getY())>=2){
                            numOfNeighbours++;
                        }
                        break;
                    }

                    case 5: {
                        if(check.getX()==0 && check.getY() == height-1 && snapshot.get(width-2,1)>=2){
                            numOfNeighbours++;
                        } else if (check.getX()==0 && snapshot.get(width-2,check.getY())>=2){
                            numOfNeighbours++;
                        } else {
                            if(snapshot.get(check.getX(),1)>=2){
                                numOfNeighbours++;
                            }
                        }
                        break;
                    }

                    case 6: {
                        if(snapshot.get(check.getX(),1)>=2){
                            numOfNeighbours++;
                        }
                        break;
                    }

                    case 7: {
                        if(check.getX()==width-1 && check.getY() == height-1 && snapshot.get(1,1)>=2){
                            numOfNeighbours++;
                        } else if(check.getX()==width-1 && snapshot.get(1,check.getY())>=2){
                            numOfNeighbours++;
                        } else {
                            if(snapshot.get(check.getX(),1)>=2){
                                numOfNeighbours++;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return numOfNeighbours;
    }

    public static void randomFill(Logic l, int width, int height, int percentage, int value){
        /**
         * Fills the grid with cells under given percentage
         * and value
         */
        Random rng = new Random();
        for(int i = 1; i< width-1; i++){
            for(int j =1; j<height-1;j++){
                int random = rng.nextInt(101);
                if(random<=percentage){
                    l.getCurrentGrid().set(i, j, value);
                }
            }
        }
    }
}
