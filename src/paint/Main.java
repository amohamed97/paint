package paint;

import javax.swing.*;

import paint.gui.Window;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Paint");
		frame.setContentPane(new Window().contentPane);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}