package paint.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import paint.gui.ColorDialog;

public class Window {
	private JButton lineButton;
	private JButton rectangleButton;
	private JButton ellipseButton;
	private JButton backgroundColorButton;
	private JButton brushColorButton;
	private JPanel canvasPanel;

	private Color brushColor = Color.black;

	public Window() {
		backgroundColorButton.addActionListener(e -> {
			canvasPanel.setBackground(ColorDialog.getColor());
		});
		brushColorButton.addActionListener(e -> {
			brushColor = ColorDialog.getColor();
		});
	}
}
