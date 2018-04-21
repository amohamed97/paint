package paint.model;

import java.awt.*;

abstract public class Shape implements Drawable {

    protected Color color;
    protected Color fillColor;

    abstract void setPosition(int x,int y);

    abstract public Point getPosition();

    abstract public Point getBottomRight();

    Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color=color;
    }

    void setFillColor(Color color){
        this.fillColor=color;
    }

    Color getFillColor(){
        return fillColor;
    }

    void cloneShape(){

    }

    abstract public boolean contains(Point point);

}
