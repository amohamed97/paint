package paint.gui;

import paint.controller.Engine;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    Engine engine;

    public void setEngine(Engine engine){
        this.engine = engine;
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        engine.refresh(g);
    }
}
