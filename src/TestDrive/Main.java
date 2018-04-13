package TestDrive;

import javax.swing.*;

import paint.gui.Window;
import paint.model.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Paint");
        Window win=new Window();
        frame.setContentPane(win.contentPane);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        int[] x={20,30};
//        int[] y={50,60};
        win.canvasPanel.getGraphics().setColor(Color.BLUE);
        Ellipse pl=new Ellipse(0,0,50,50);
        pl.draw(win.canvasPanel.getGraphics());
        win.canvasPanel.repaint();
        win.canvasPanel.paintComponents(win.canvasPanel.getGraphics());
        frame.setVisible(true);
//        win.canvasPanel.add(new JButton("Test"));
    }
}



