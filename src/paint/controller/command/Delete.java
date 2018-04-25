package paint.controller.command;

import paint.model.Shape;

import java.util.ArrayList;

public class Delete implements Command {
    Shape shape;
    int index;

    public Delete(Shape shape, int index){
        this.shape = shape;
        this.index = index;
    }

    public void execute(ArrayList<Shape> shapes){
        shapes.remove(index);
    }

    public void reverse(ArrayList<Shape> shapes){
        shapes.add(index, shape);
    }

}
