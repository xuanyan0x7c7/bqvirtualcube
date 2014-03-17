package model;

import java.awt.Dimension;

import javax.swing.JApplet;

@SuppressWarnings("serial")
public class MyApplet extends JApplet {

	private MyPanel panel;

	@Override
	public void init() {
		int width = Integer.parseInt(getParameter("width"));
		int height = Integer.parseInt(getParameter("height"));
		panel = new MyPanel(null, new Dimension(width, height));
		addKeyListener(panel);
		getContentPane().add(panel);
		setVisible(true);
	}
}
