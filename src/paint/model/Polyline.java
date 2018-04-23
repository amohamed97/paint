package paint.model;

import java.awt.*;
import java.util.Arrays;

public class Polyline extends Shape {


    int[] x;
    int[] y;
    Point position = null, bottomRight = null;

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
        position = p;
        return p;
    }

    public Point getBottomRight(){
        int maxX= Arrays.stream(this.x).max().getAsInt();
        int maxY= Arrays.stream(this.y).max().getAsInt();
        Point p=new Point(maxX,maxY);
        bottomRight = p;
        return p;
    }

    @Override
    public Polyline cloneShape() {
        return new Polyline(this.x,this.y);
    }

    public void move(int xDiff, int yDiff){

        this.x=Arrays.stream(this.x).map(n->n+xDiff).toArray();
        this.y=Arrays.stream(this.y).map(n->n+yDiff).toArray();

    }

    @Override
    public void draw(Graphics g){
        g.setColor(color);
        Polygon pol=new Polygon(x,y,this.x.length);
        g.drawPolygon(pol);
        g.setColor(fillColor);
        g.fillPolygon(pol);
    }

    @Override
    public boolean contains(Point point) {
        return (new Polygon(x, y, x.length)).contains(point);
    }

    public void resize(int x, int y){
        double scaleX = (x - position.getX())/(bottomRight.getX() - position.getX());
        double scaleY = (y - position.getY())/(bottomRight.getY() - position.getY());
        double offsetX = scaleX*position.getX() - position.getX();
        double offsetY = scaleY*position.getY() - position.getY();
        this.x = Arrays.stream(this.x).map(n->(int) (n*scaleX - offsetX)).toArray();
        this.y = Arrays.stream(this.y).map(n->(int) (n*scaleY - offsetY)).toArray();
    }
}
