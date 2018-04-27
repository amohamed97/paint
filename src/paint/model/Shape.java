package paint.model;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

abstract public class Shape{

    protected Color color;
    protected Color fillColor;

    abstract void setPosition(int x, int y);

    abstract public Point getPosition();

    abstract public Point getBottomRight();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    public Color getFillColor() {
        return fillColor;
    }

    abstract public Shape cloneShape() ;

    abstract public void draw(Graphics g);

    public abstract void move(int x,int y);

    public abstract void resize(int x, int y);

    abstract public boolean contains(Point point);

    abstract public JSONObject saveJSON();

    private static String colorRGBA(Color color){
        return String.format("rgba(%d, %d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue(),
                color.getAlpha());
    }

    protected void saveSVGColors(Element elem){
        elem.setAttribute("stroke", colorRGBA(color));
        elem.setAttribute("fill", colorRGBA(fillColor));
    }

    abstract public Element saveSVG(Document doc);

}
