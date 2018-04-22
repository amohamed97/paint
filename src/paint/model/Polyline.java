package paint.model;

import java.awt.*;
import java.awt.geom.Path2D;
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
        move(xDiff, yDiff);
    }

    public Point getPosition(){
        int minX= Arrays.stream(this.x).min().getAsInt();
        int minY= Arrays.stream(this.y).min().getAsInt();
        Point p=new Point(minX,minY);
        return p;
    }

    public Point getBottomRight(){
        int maxX= Arrays.stream(this.x).max().getAsInt();
        int maxY= Arrays.stream(this.y).max().getAsInt();
        Point p=new Point(maxX,maxY);
        return p;
    }

    public void move(int xDiff, int yDiff){

        this.x=Arrays.stream(this.x).map(n->n+xDiff).toArray();
        this.y=Arrays.stream(this.y).map(n->n+yDiff).toArray();

    }

    @Override
    public void delete() {
    }

    @Override
    public void draw(Graphics g){
        g.setColor(color);
        Polygon pol=new Polygon(x,y,this.x.length);
        g.drawPolygon(pol);
    }

    @Override
    public boolean contains(Point point) {
        return (new Polygon(x, y, x.length)).contains(point);
    }
}
