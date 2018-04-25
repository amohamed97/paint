package paint.controller.command;

import paint.model.Shape;

import java.util.ArrayList;

public class Resize implements Command {
    int index;
    int x,y;
    int oldX,oldY;

//    public Resize(int oldX,int oldY){
//        this.oldX = oldX;
//        this.oldY = oldY;
//    }

    public Resize(int index ,int x ,int y , int oldX , int oldY){
        this.index = index;
        this.x = x;
        this.y = y;
        this.oldX = oldX;
        this.oldY = oldY;
    }

    public void execute(ArrayList<Shape> shapes){
        shapes.get(index).resize(this.x,this.y);
    }

    public void reverse(ArrayList<Shape> shapes){
        shapes.get(index).resize(this.oldX,this.oldY);
    }
}
