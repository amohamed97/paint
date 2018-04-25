package paint.controller.command;

import paint.model.Shape;

import java.util.ArrayList;

public interface Command {
    void execute(ArrayList<Shape> shapes);
    void reverse(ArrayList<Shape> shapes);
}
