package paint.model;

import java.awt.*;

abstract public class Shape implements Drawable {

    protected Color color;
    protected Color fillColor;

    abstract void setPosition(int x, int y);

    abstract public Point getPosition();

    abstract public Point getBottomRight();

    Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    void setFillColor(Color color) {
        this.fillColor = color;
    }

    Color getFillColor() {
        return fillColor;
    }

    public void cloneShape() {

    }


    public abstract void move(int x,int y);
    public abstract void delete();

    abstract public boolean contains(Point point);

}
