package paint.gui;

import paint.controller.Engine;
import paint.model.Ellipse;
import paint.model.Polyline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window {
	private JToggleButton lineToggleButton;
	private JToggleButton rectangleToggleButton;
	private JToggleButton ellipseToggleButton;
	private JButton backgroundColorButton;
	private JButton brushColorButton;
	public JPanel contentPane;
    private Engine engine = new Engine();
    private Canvas canvasPanel;
    private JToggleButton selectToggleButton;
    private String actionCommand;



    private Color brushColor = Color.black;

	public Window() {
		backgroundColorButton.addActionListener(e -> {
			Color color = ColorDialog.getColor();
			if(color != null)
				canvasPanel.setBackground(color);
		});
		brushColorButton.addActionListener(e -> {
			Color color = ColorDialog.getColor();
			if(color != null)
				brushColor = color;
		});
        canvasPanel.setEngine(engine);

        MouseAdapter shaper = new MouseAdapter() {
            private int startX, startY;
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e){
                super.mouseReleased(e);
                paint.model.Shape newShape;
                int endX = e.getX();
                int endY = e.getY();
                if(actionCommand.equals("Line"))
                    newShape = new Polyline(new int[]{startX, endX}, new int[]{startY, endY});
                else if(actionCommand.equals("Ellipse")) {
                    if(endX < startX){
                        int tmp = startX;
                        startX = endX;
                        endX = tmp;
                    }
                    if(endY < startY){
                        int tmp = startY;
                        startY = endY;
                        endY = tmp;
                    }
                    newShape = new Ellipse(startX, startY, endX - startX, endY - startY);
                }else if(actionCommand.equals("Rectangle")) {
                    newShape = new Polyline(new int[]{startX, endX, endX, startX},
                            new int[]{startY, startY, endY, endY});
                }else
                    throw new UnsupportedOperationException();
                engine.addShape(newShape);
                canvasPanel.repaint();
            }
        };

        ActionListener modeChanged = e -> {
            actionCommand = e.getActionCommand();
            if(actionCommand.equals("Select")){
                canvasPanel.setCursor(Cursor.getDefaultCursor());
                canvasPanel.removeMouseListener(shaper);
            } else {
                canvasPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                canvasPanel.addMouseListener(shaper);
            }
        };
        selectToggleButton.addActionListener(modeChanged);
        lineToggleButton.addActionListener(modeChanged);
        ellipseToggleButton.addActionListener(modeChanged);
        rectangleToggleButton.addActionListener(modeChanged);
    }
}
