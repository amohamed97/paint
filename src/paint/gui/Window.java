package paint.gui;

import paint.controller.Engine;
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

            public void mouseReleased(MouseEvent e){
                super.mouseReleased(e);
                if(actionCommand.equals("Line"))
                    engine.addShape(new Polyline(new int[]{startX, e.getX()}, new int[]{startY, e.getY()}));
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
