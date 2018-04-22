package paint.controller;

import paint.model.Shape;

import java.awt.*;
import java.util.ArrayList;

public class Engine {
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    int selected = -1;

    public void removeLastShape() {
        shapes.remove(shapes.size()-1);
    }


    public void refresh(Graphics g) {
        shapes.forEach(s -> s.draw(g));
        if(selected != -1){
            Shape selectedShape = shapes.get(selected);
            Point pos = selectedShape.getPosition();
            Point end = selectedShape.getBottomRight();
            g.setColor(Color.BLUE);
            g.drawRect(new Double(pos.getX()).intValue(), new Double(pos.getY()).intValue(),
                    new Double(end.getX() - pos.getX()).intValue(), new Double(end.getY() - pos.getY()).intValue());
            System.out.println("Refreshing");
        }
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }
    public Shape[] getShapes() {
        return (Shape[]) shapes.toArray();
    }

    public void selectShape(Point point){
        for(int i = shapes.size()-1; i >= 0; i--)
            if (shapes.get(i).contains(point)) {
                selected = i;
                return;
            }
        selected = -1;
    }

    public void moveShape(int diffX,int diffY){
        if(selected !=-1) {
            shapes.get(selected).move(diffX, diffY);
        }
    }

    public void deleteShape(){
        shapes.remove(selected);
        selected=-1;
    }

}
