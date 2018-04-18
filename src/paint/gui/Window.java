package paint.gui;

import paint.gui.ColorDialog;
import paint.controller.Engine;

import javax.swing.*;
import java.awt.*;

public class Window {
	private JButton lineButton;
	private JButton rectangleButton;
	private JButton ellipseButton;
	private JButton backgroundColorButton;
	private JButton brushColorButton;
	public JPanel contentPane;
    private Engine engine = new Engine();
    private Canvas canvasPanel;

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
        canvasPanel.setBackground(Color.WHITE);
        canvasPanel.setEngine(engine);
	}
}
