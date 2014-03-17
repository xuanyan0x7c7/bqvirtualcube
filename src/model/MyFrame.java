package model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {

	private MyPanel panel;
	boolean fullscreen = false;
	Dimension size = new Dimension(600, 600);
	Point location;

	public MyFrame() {
		super("BQ Virtual Cube");
		setSize(size);
		setLocationRelativeTo(null);
		panel = new MyPanel(this);
		addKeyListener(panel);
		getContentPane().add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		location = getLocationOnScreen();

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				location = getLocationOnScreen();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				panel.myResize(size = getSize());
			}
		});
	}
}
