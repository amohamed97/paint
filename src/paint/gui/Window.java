package paint.gui;

import paint.gui.ColorDialog;

import javax.swing.*;
import java.awt.*;

public class Window {
	private JButton lineButton;
	private JButton rectangleButton;
	private JButton ellipseButton;
	private JButton backgroundColorButton;
	private JButton brushColorButton;
	private JPanel canvasPanel;
	public JPanel contentPane;

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
	}
}
