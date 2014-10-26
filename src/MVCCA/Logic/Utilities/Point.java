package MVCCA.Logic.Utilities;

/**
 *  Point class to be used with logic neighbour checking in a loop
 *  Contains no public constructor, only static XY method to obtain a new
 *  object to be used with static imports (multiple point code is easier on the eye this way)
 */
public class Point {
    private int x;
    private int y;

    private Point(){

    }

    public static Point XY(int x, int y){
        Point p = new Point();
        p.setX(x);
        p.setY(y);
        return p;

    }

    public void setX(int x){
        this.x=x;
    }

    public void setY(int y){
        this.y=y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Point merge(Point p){
        return XY(this.x+p.getX(),this.y+p.getY());
    }

    @Override
    public String toString(){
        return "X: "+getX()+" "+"Y: "+getY();
    }


}

