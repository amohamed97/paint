package paint.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColorDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JColorChooser colorChooser;

	private Color selectedColor = null;

	public static Color getColor(){
		ColorDialog dlg = new ColorDialog();
		dlg.pack();
		dlg.setVisible(true);
		return dlg.selectedColor;
	}

	public ColorDialog() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { dispose(); }
		});

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) { dispose(); }
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	private void onOK() {
		selectedColor = colorChooser.getColor();
		dispose();
	}
}
