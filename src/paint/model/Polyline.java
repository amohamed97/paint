package paint.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Polyline extends Shape {


    int[] x;
    int[] y;
    int positionX;
    int positionY;

    boolean isPolygon;



    public Polyline(int[] x, int[] y){
        this.x=x;
        this.y=y;
    }

    void setPosition(int x,int y){
        Point p=getPosition();
        int xDiff=x - (int) p.getX();
        int yDiff=y - (int) p.getY();
        this.x=Arrays.stream(this.x).map(n->n+xDiff).toArray();
        this.y=Arrays.stream(this.y).map(n->n+yDiff).toArray();
    }

    Point getPosition(){
        int minX= Arrays.stream(this.x).min().getAsInt();
        int minY= Arrays.stream(this.y).min().getAsInt();
        Point p=new Point(minX,minY);
        return p;
    }


    @Override
    public void draw(Graphics g){
        Polygon pol=new Polygon(x,y,this.x.length);
        g.drawPolygon(pol);
    }

}
