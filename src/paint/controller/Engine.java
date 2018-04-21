package paint.controller;

import paint.model.Shape;

import java.awt.*;
import java.util.ArrayList;

public class Engine {
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    public void clearShapes() {
        if(shapes.isEmpty()){

        }
        else{
            shapes.remove(shapes.size()-1);
        }
    }


    public void refresh(Graphics g) {
        shapes.forEach(s -> s.draw(g));
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }
    public Shape[] getShapes() {
        return (Shape[]) shapes.toArray();
    }
}
