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
        Point selectedPosition, selectedBottomRight;
        shapes.forEach(s -> s.draw(g));
        if(selected != -1){
            Shape selectedShape = shapes.get(selected);
            selectedPosition = selectedShape.getPosition();
            selectedBottomRight = selectedShape.getBottomRight();
            g.setColor(Color.BLUE);
            g.drawRect(new Double(selectedPosition.getX()).intValue(), new Double(selectedPosition.getY()).intValue(),
                    new Double(selectedBottomRight.getX() - selectedPosition.getX()).intValue(),
                    new Double(selectedBottomRight.getY() - selectedPosition.getY()).intValue());
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

    public void moveShape(int diffX,int diffY , Point point){
        if(selected !=-1 && shapes.get(selected).contains(point)) {
            shapes.get(selected).move(diffX, diffY);
        }
    }

    public void deleteShape(){
        shapes.remove(selected);
        selected=-1;
    }


    public void unselect(){
        selected = -1;
    }

    public boolean containsSelected(Point point){
        if(selected == -1)
            return false;

        if(shapes.get(selected).contains(point))
            return true;
        else
            return false;
    }

    public Shape cloneShape(){
        if(selected != -1) {
            Shape shape = shapes.get(selected).cloneShape();
            shape.move(-10,-10);
            addShape(shape);
            return shape;
        }
        return null;
    }

}
