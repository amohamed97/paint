package paint.controller;

import paint.model.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Engine {
    ArrayList<Shape> shapes = new ArrayList<Shape>();

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
