package TestDrive;

import paint.model.Ellipse;
import paint.model.Polyline;

import javax.swing.*;
import java.awt.*;

public class GUI extends JPanel {
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        g.setColor(Color.BLUE);
        int[] x={25,40};
        int[] y={30,50};
        Polyline pol=new Polyline(x,y);
        pol.draw(g);
    }
}
