package paint.model;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        Polygon pol=new Polygon(x,y,this.x.length);
        g.setColor(fillColor);
        g.fillPolygon(pol);
        g.setColor(color);
        g.drawPolygon(pol);
    }

    @Override
    public boolean contains(Point point) {
        if(this.x.length == 2){
            int[] xl = {this.x[0],this.x[1],this.x[1],this.x[0]};
            int[] yl = {this.y[0],this.y[0],this.y[1],this.y[1]};
            return (new Polygon(xl,yl,4)).contains(point);
        }
        return (new Polygon(x, y, x.length)).contains(point);
    }

    @Override
    public JSONObject saveJSON() {
        JSONArray xArr = new JSONArray();
        JSONArray yArr = new JSONArray();
        JSONObject obj = new JSONObject();
        Arrays.stream(x).forEach(a -> xArr.add(a));
        Arrays.stream(y).forEach(a -> yArr.add(a));
        obj.put("X", xArr);
        obj.put("Y", yArr);
        obj.put("type", "PolyLine");
        return obj;
    }

    public Element saveSVG(Document doc){
        Element elem = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "polygon");
        String points = "";
        for(int i = 0; i < x.length; ++i){
            points = String.format("%s %d,%d", points, x[i], y[i]);
        }
        elem.setAttribute("points", points);
        saveSVGColors(elem);
        return elem;
    }

    public void resize(int x, int y){

        if(this.x.length == 4) {
            this.x[2] = x;
            this.y[2] = y;
            this.x[1] = x;
            this.y[3] = y;
            return;
        }else if(this.x.length == 3){
            this.x[1] = x;
            this.y[1] = y;
            this.y[2] = y;
            return;
        }else if(this.x.length == 2){
            this.x[1] = x;
            this.y[1] = y;
            return;
        }
        if(x - position.getX() < 5)
            return;
        if(y - position.getY()  < 5)
            return;
        double scaleX = (x - position.getX())/(bottomRight.getX() - position.getX());
        double scaleY = (y - position.getY())/(bottomRight.getY() - position.getY());
        double offsetX = position.getX()*(1 - scaleX);
        double offsetY = position.getY()*(1 - scaleY);
        this.x = Arrays.stream(this.x).map(n->(int) (n*scaleX + offsetX)).toArray();
        this.y = Arrays.stream(this.y).map(n->(int) (n*scaleY + offsetY)).toArray();
        getBottomRight();
    }



}
