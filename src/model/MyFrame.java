package model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame implements ComponentListener {

	private MyPanel panel;
	boolean fullscreen = false;
	Dimension size = new Dimension(600, 600);
	Point location;

	public MyFrame() {
		super("BQ Virtual Cube");
		setSize(size);
		setLocationRelativeTo(null);
		addComponentListener(this);
		panel = new MyPanel(this);
		addKeyListener(panel);
		getContentPane().add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		location = getLocationOnScreen();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		location = getLocationOnScreen();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		panel.myResize(size = getSize());
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
}
