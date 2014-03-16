package model;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MyStatusBar extends JTextField {

	public MyStatusBar() {
		super("Press Enter to play");
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createEmptyBorder());
		setDisabledTextColor(Color.WHITE);
		setEnabled(false);
		setFont(MyPanel.default_font);
		setHorizontalAlignment(JTextField.CENTER);
	}
}
