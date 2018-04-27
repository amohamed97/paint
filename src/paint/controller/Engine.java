package paint.controller;

import fileManagement.*;
import paint.controller.command.*;
import paint.model.Shape;

import java.awt.*;
import java.util.ArrayList;


public class Engine {
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    ArrayList<Command> commands = new ArrayList<Command>();
    FileHandler fileHandler;
    int selected = -1;
    int commandIndex = 0;


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


    private void resetRedo(){
        for(int i = commands.size() - 1; i >= commandIndex; --i)
            commands.remove(i);
    }

    public void checkpointAdd(){
        resetRedo();
        int index = shapes.size() - 1;
        commands.add(commandIndex++, new Add(shapes.get(index), index));
    }

    public Shape[] getShapes() {
        return (Shape[]) shapes.toArray();
    }

    public void selectShape(Point point){
        for(int i = shapes.size()-1; i >= 0; i--) {

            if (shapes.get(i).contains(point)) {
                selected = i;
                return;
            }
        }
        selected = -1;
    }

    public void moveShape(int diffX,int diffY , Point point){
        resetRedo();
        if(selected !=-1 &&  shapes.get(selected).contains(point)) {
            shapes.get(selected).move(diffX, diffY);
        }
    }

    public void checkpointMove(int xDiff, int yDiff){
        resetRedo();
        commands.add(commandIndex++, new Move(xDiff, yDiff, selected));
    }

    public void resize(Point point){
        shapes.get(selected).resize((int) point.getX(), (int) point.getY());
    }
    public void checkpointResize(int x,int y,int oldX,int oldY) {
        resetRedo();
        commands.add(commandIndex++, new Resize(selected,x,y,oldX,oldY));
    }

    public void deleteShape(){
        commands.add(commandIndex++, new Delete(shapes.get(selected), selected));
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
            selected = shapes.size() - 1;
            checkpointAdd();
            return shape;
        }
        return null;
    }

    public void undo(){
        commands.get(--commandIndex).reverse(shapes);

    }

    public void redo(){
        commands.get(commandIndex++).execute(shapes);
    }

    public boolean hasUndo(){
        return commandIndex > 0;
    }

    public boolean hasRedo(){
        return commandIndex < commands.size();
    }

    public void save(String fileName, Color background){
        if (fileName.endsWith(".json")) {
            fileHandler = new JSONFileHandler(shapes);
        }
        else {
            fileHandler = new SVGFileHandler(shapes);
        }
        fileHandler.save(fileName, background);
    }

    public Color load(String fileName) {
        commands.clear();
        commandIndex = 0;
        if (fileName.endsWith(".json")) {
            fileHandler = new JSONFileHandler(shapes);
            return fileHandler.load(fileName);
        }else {
            fileHandler = new SVGFileHandler(shapes);
            return fileHandler.load(fileName);
        }
    }

    public boolean selectionExists(){
        return selected != -1;
    }
}
