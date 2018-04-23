package paint.model;

import java.awt.*;

abstract public class Shape{

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

    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    Color getFillColor() {
        return fillColor;
    }

    public void cloneShape() {

    }

    abstract public void draw(Graphics g);

    public abstract void move(int x,int y);
    public abstract void delete();

    abstract public boolean contains(Point point);

}
