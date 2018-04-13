package TestDrive;

import javax.swing.*;
import java.awt.*;

public class Frame {

    public static void main(String[] args) {
        JFrame jf=new JFrame("Test");
        jf.setSize(700,700);
        GUI gui=new GUI();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setBackground(Color.WHITE);
        jf.add(gui);
        gui.setSize(500,500);
        jf.setVisible(true);

    }
}
