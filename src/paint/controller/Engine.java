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
            Point selectedPosition = selectedShape.getPosition();
            Point selectedBottomRight = selectedShape.getBottomRight();
            int selectedPositionX = new Double(selectedPosition.getX()).intValue();
            int selectedPositionY = new Double(selectedPosition.getY()).intValue();
            int selectedBottomRightX = new Double(selectedBottomRight.getX()).intValue();
            int selectedBottomRightY = new Double(selectedBottomRight.getY()).intValue();
            g.setColor(Color.BLUE);
            g.drawRect(selectedPositionX, selectedPositionY,
                    selectedBottomRightX - selectedPositionX,
                    selectedBottomRightY - selectedPositionY);
            g.fillRect(selectedBottomRightX-5, selectedBottomRightY-5, 5, 5);
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

    public void resize(Point point){
        shapes.get(selected).resize((int) point.getX(), (int) point.getY());
    }

    public void deleteShape(){
        shapes.remove(selected);
        selected=-1;
    }


    public void unselect(){
        selected = -1;
    }

    public int cursorMode(Point point){
        if(selected == -1)
            return 0;

        Shape selectedShape = shapes.get(selected);

        if(selectedShape.getBottomRight().distance(point) <= 5)
            return 2;

        return selectedShape.contains(point)? 1 : 0;
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
