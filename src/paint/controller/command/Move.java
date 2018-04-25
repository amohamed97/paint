package paint.controller.command;

import paint.model.Shape;

import java.util.ArrayList;

public class Move implements Command {
    int index;
    int xDiff, yDiff;

    public Move(int xDiff, int yDiff, int index){
        this.xDiff = xDiff;
        this.yDiff = yDiff;
        this.index = index;
    }

    public void execute(ArrayList<Shape> shapes){
        shapes.get(index).move(xDiff, yDiff);
    }

    public void reverse(ArrayList<Shape> shapes){
        shapes.get(index).move(-xDiff, -yDiff);
    }
}