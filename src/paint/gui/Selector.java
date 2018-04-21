package paint.gui;

import paint.model.Drawable;

import java.awt.*;

public class Selector implements Drawable {

    int[] x;
    int[] y;


    public Selector(int[] x,int[] y){
        this.x=x;
        this.y=y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);


    }
}
