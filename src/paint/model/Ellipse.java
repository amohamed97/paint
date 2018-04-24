package paint.model;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

public class Ellipse extends Shape {
    int x, y, width, height;


    public Ellipse(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 0;
        this.height = 0;
    }

    public Ellipse(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawOval(x, y, width, height);
        g.setColor(fillColor);
        g.fillOval(x, y, width, height);
    }

    @Override
    void setPosition(int x, int y) {
        Point p = getPosition();
        int xDiff = x - (int) p.getX();
        int yDiff = y - (int) p.getY();
        move(xDiff, yDiff);
    }

    public Point getPosition() {
        Point p = new Point(this.x, y);
        return p;
    }

    public Point getBottomRight() {
        return new Point(this.x + width, this.y + height);
    }

    @Override
    public Ellipse cloneShape() {
        return new Ellipse(this.x, this.y, this.width, this.height);
    }

    public boolean contains(Point point) {
        return (new Ellipse2D.Float(x, y, width, height)).contains(point);
    }

    public void move(int xDiff, int yDiff) {
        this.x += xDiff;
        this.y += yDiff;
    }

    public void resize(int x, int y) {
        this.width = x - this.x;
        this.height = y - this.y;
    }

}
