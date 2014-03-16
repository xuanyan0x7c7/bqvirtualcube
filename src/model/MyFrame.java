package model;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {

	private MyPanel panel;

	public MyFrame() {
		super("BQ Virtual Cube");
		setSize(600, 600);
		setLocationByPlatform(true);
		setResizable(false);
		setFocusable(true);
		panel = new MyPanel();
		addKeyListener(panel);
		getContentPane().add(panel);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
