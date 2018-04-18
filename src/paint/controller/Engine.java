package paint.controller;

import paint.model.Shape;

import java.awt.*;
import java.util.Arrays;

public class Engine {
    Shape[] shapes = {};

    public void refresh(Graphics g) {
        Arrays.stream(shapes).forEach(s -> s.draw(g));
    }

    public void addShape(Shape shape) {
        shapes[shapes.length] = shape;
    }

    public Shape[] getShapes() {
        return shapes;
    }
}
