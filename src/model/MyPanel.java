package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import puzzle.CrazyCube444;
import puzzle.Cube;
import puzzle.Megaminx;
import puzzle.Puzzle;
import puzzle.Pyraminx;
import thread.MyTimer;
import thread.StateThread;
import thread.ThreadQueue;

@SuppressWarnings("serial")
public class MyPanel extends JPanel implements KeyListener, MouseListener,
		MouseMotionListener, MouseWheelListener {

	private final int width = 600;
	private final int height = 600;
	static final Font default_font = new Font(null, Font.TRUETYPE_FONT, 24);
	private BufferedImage buf;
	private Graphics2D g2;
	public Class<? extends Puzzle> puzzle_type;
	public Puzzle puzzle;
	private int puzzle_size;
	public final MyTimer timer;
	private boolean relay_layer;
	private boolean relay_n;
	private int count;
	private int ncount = 10;
	public ThreadQueue thread_queue;
	private Thread twist_thread;

	private List<Class<? extends Puzzle>> puzzle_list = new ArrayList<Class<? extends Puzzle>>();

	public MyPanel() {
		super();
		setSize(width, height);
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setLayout(null);
		MyMenu menu = new MyMenu(this);
		menu.setBounds(0, 0, width, 24);
		add(menu);
		MyStatusBar statusbar = new MyStatusBar();
		statusbar.setBounds((width - 300) / 2, 30, 300, 20);
		add(statusbar);
		setVisible(true);
		buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g2 = buf.createGraphics();
		g2.translate(width / 2, height / 2);
		g2.scale(width, -height);
		g2.setStroke(new BasicStroke((float) 0.001));
		timer = new MyTimer(statusbar);
		puzzle_list.add(Cube.class);
		puzzle_list.add(Pyraminx.class);
		puzzle_list.add(Megaminx.class);
		puzzle_list.add(CrazyCube444.class);
		createPuzzle(puzzle_list.get(0));
		count = 0;
		relay_layer = relay_n = false;
		new Thread(timer).start();
		new Thread(new StateThread(this)).start();
		changePuzzle();
	}

	@Override
	public void paintComponent(Graphics g) {
		g2.setColor(Color.BLACK);
		g2.fillRect(-1, -1, 2, 2);
		puzzle.draw(g2);
		g.drawImage(buf, 0, 0, null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getExtendedKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			int x = puzzle_list.indexOf(puzzle_type);
			createPuzzle(puzzle_list.get((x + 1) % puzzle_list.size()));
			break;
		case KeyEvent.VK_DOWN:
			x = puzzle_list.indexOf(puzzle_type);
			createPuzzle(puzzle_list.get((x + puzzle_list.size() - 1)
					% puzzle_list.size()));
			break;
		case KeyEvent.VK_LEFT:
			int new_size = puzzle.getSmallerSize(puzzle_size);
			if (new_size != puzzle_size) {
				puzzle_size = new_size;
				createPuzzle(puzzle_type, puzzle_size);
				relay_layer = relay_n = false;
				count = 0;
				timer.resetTimer();
				timer.setPrefixString("");
				changePuzzle();
			}
			break;
		case KeyEvent.VK_RIGHT:
			new_size = puzzle.getBiggerSize(puzzle_size);
			if (new_size != puzzle_size) {
				puzzle_size = new_size;
				createPuzzle(puzzle_type, puzzle_size);
				relay_layer = relay_n = false;
				count = 0;
				timer.resetTimer();
				timer.setPrefixString("");
				changePuzzle();
			}
			break;
		case KeyEvent.VK_ENTER:
			if (timer.state == MyTimer.stop) {
				timer.resetTimer();
				timer.setPrefixString("");
			} else if (timer.state == MyTimer.reset) {
				puzzle.reset().scramble();
				timer.startInspection();
				repaint();
			}
			break;
		case KeyEvent.VK_ESCAPE:
			relay_layer = relay_n = false;
			count = 0;
			puzzle.reset();
			timer.resetTimer();
			timer.setPrefixString("");
			repaint();
			break;
		case KeyEvent.VK_F1:
			if (puzzle_type == Cube.class) {
				relay_layer = true;
				relay_n = false;
				timer.setPrefixString("");
				createPuzzle(puzzle_type, 2);
				puzzle.scramble();
				changePuzzle();
				timer.startInspection();
			}
			break;
		case KeyEvent.VK_F2:
			relay_layer = false;
			relay_n = true;
			timer.setPrefixString("1/" + ncount + " ");
			count = 1;
			createPuzzle(puzzle_type, puzzle_size);
			puzzle.scramble();
			changePuzzle();
			timer.startInspection();
			break;
		default:
			int k = puzzle.turn(key);
			if (k != -1) {
				if (timer.state == MyTimer.stop) {
					break;
				} else if (timer.state == MyTimer.inspection
						&& puzzle.isTwistLayer(k)) {
					timer.startTimer();
				}
				thread_queue.apply(k);
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	private int[] drag = new int[3];

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		switch (drag[0]) {
		case MouseEvent.BUTTON1:
			double scale_x = g2.getTransform().getScaleX();
			double scale_y = g2.getTransform().getScaleY();
			g2.scale(1.0 / scale_x, 1.0 / scale_y);
			g2.translate(x - drag[1], y - drag[2]);
			g2.scale(scale_x, scale_y);
			repaint();
			break;
		case MouseEvent.BUTTON2:
			double scale = Math.pow(1.01, x - y - drag[1] + drag[2]);
			g2.scale(scale, scale);
			repaint();
			break;
		case MouseEvent.BUTTON3:
			puzzle.rotateEye(-2.0 * (x - drag[1]) / width, 2.0 * (y - drag[2])
					/ height);
			repaint();
			break;
		}
		drag[1] = x;
		drag[2] = y;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!hasFocus()) {
			requestFocus();
		}
		drag[0] = e.getButton();
		drag[1] = e.getX();
		drag[2] = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double zoom = Math.pow(1.05, e.getWheelRotation());
		puzzle.zoom(zoom);
		repaint();
	}

	public void setRelayCount(int n) {
		ncount = n;
	}

	public void createPuzzle(Class<? extends Puzzle> puzzle_type) {
		this.puzzle_type = puzzle_type;
		try {
			puzzle = puzzle_type.cast(puzzle_type.getConstructor(MyPanel.class)
					.newInstance(this));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		puzzle_size = puzzle.getDefaultSize();
		relay_layer = relay_n = false;
		count = 0;
		timer.resetTimer();
		timer.setPrefixString("");
		changePuzzle();
	}

	public void createPuzzle(Class<? extends Puzzle> puzzle_type, int size) {
		this.puzzle_type = puzzle_type;
		try {
			puzzle = puzzle_type.cast(puzzle_type.getConstructor(MyPanel.class,
					int.class).newInstance(this, puzzle_size = size));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		relay_layer = relay_n = false;
		count = 0;
		timer.resetTimer();
		timer.setPrefixString("");
		changePuzzle();
	}

	public boolean nextPuzzle() {
		if (relay_layer) {
			if (puzzle_size < 7) {
				createPuzzle(puzzle_type,
						puzzle_size = puzzle.getBiggerSize(puzzle_size));
				puzzle.preScramble().scramble();
				changePuzzle();
				return true;
			} else {
				return relay_layer = false;
			}
		} else if (relay_n) {
			if (count < ncount) {
				timer.setPrefixString(++count + "/" + ncount + " ");
				createPuzzle(puzzle_type, puzzle_size);
				puzzle.preScramble().scramble();
				changePuzzle();
				return true;
			} else {
				count = 0;
				return relay_n = false;
			}
		} else {
			return false;
		}
	}

	private void changePuzzle() {
		thread_queue = new ThreadQueue(puzzle);
		twist_thread = new Thread(thread_queue);
		twist_thread.start();
		repaint();
	}
}
