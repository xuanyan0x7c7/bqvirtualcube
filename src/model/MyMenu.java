package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import puzzle.CrazyCube444;
import puzzle.Cube;
import puzzle.Megaminx;
import puzzle.Pyraminx;

@SuppressWarnings("serial")
public class MyMenu extends JMenuBar {

	MyPanel panel;
	JMenu puzzle_menu = new JMenu("Puzzle");
	JMenuItem cube, pyraminx, megaminx;
	JMenu crazy444_menu = new JMenu("Crazy 4x4x4");
	JMenuItem[] crazy444 = new JMenuItem[4];
	JMenu settings_menu = new JMenu("Settings");
	JMenu relay_count_menu = new JMenu("Relay Count");
	int[] relay_count = new int[] { 2, 3, 4, 5, 6, 8, 10, 15, 20, 25, 30, 40,
			50, 100 };
	JMenuItem[] relay_count_menuitem = new JMenuItem[relay_count.length];

	public MyMenu(MyPanel panel) {
		this.panel = panel;
		setFont(MyPanel.default_font);
		puzzle_menu.add(cube = new JMenuItem("Cube"));
		puzzle_menu.add(pyraminx = new JMenuItem("Pyraminx"));
		puzzle_menu.add(megaminx = new JMenuItem("Megaminx"));
		puzzle_menu.insertSeparator(puzzle_menu.getItemCount());
		crazy444_menu.add(crazy444[0] = new JMenuItem("Crazy 4x4x4 I"));
		crazy444_menu.add(crazy444[1] = new JMenuItem("Crazy 4x4x4 II"));
		crazy444_menu.add(crazy444[2] = new JMenuItem("Crazy 4x4x4 III"));
		crazy444_menu.add(crazy444[3] = new JMenuItem("Crazy 4x4x4 IV"));
		puzzle_menu.add(crazy444_menu);
		add(puzzle_menu);
		for (int i = 0; i < relay_count.length; ++i) {
			relay_count_menu.add(relay_count_menuitem[i] = new JMenuItem(
					relay_count[i] + " cubes"));
		}
		settings_menu.add(relay_count_menu);
		add(settings_menu);

		cube.addActionListener(new CreatePuzzleActionListener());
		pyraminx.addActionListener(new CreatePuzzleActionListener());
		megaminx.addActionListener(new CreatePuzzleActionListener());
		for (int i = 0; i < 4; ++i) {
			crazy444[i].addActionListener(new CreatePuzzleActionListener());
		}
		for (int i = 0; i < relay_count.length; ++i) {
			relay_count_menuitem[i]
					.addActionListener(new RelayCountActionListener());
		}
	}

	private class CreatePuzzleActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			if (item == cube) {
				panel.createPuzzle(Cube.class);
			} else if (item == pyraminx) {
				panel.createPuzzle(Pyraminx.class);
			} else if (item == megaminx) {
				panel.createPuzzle(Megaminx.class);
			} else if (item == crazy444[1]) {
				panel.createPuzzle(CrazyCube444.class);
			}
		}

	}

	private class RelayCountActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			for (int i = 0; i < relay_count.length; ++i) {
				if (item == relay_count_menuitem[i]) {
					panel.setRelayCount(relay_count[i]);
					break;
				}
			}
		}

	}
}
