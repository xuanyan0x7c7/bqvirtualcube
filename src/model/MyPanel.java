package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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

import javax.swing.JComponent;
import javax.swing.JPanel;

import puzzle.CrazyCube444;
import puzzle.Cube;
import puzzle.Megaminx;
import puzzle.Puzzle;
import puzzle.Pyraminx;
import puzzle.Skewb;
import thread.MyTimer;
import thread.StateThread;
import thread.ThreadQueue;

@SuppressWarnings("serial")
public class MyPanel extends JPanel implements KeyListener, MouseListener,
		MouseMotionListener, MouseWheelListener {

	private MyFrame frame;
	private MyMenu menu;
	private MyStatusBar statusbar;
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

	public MyPanel(MyFrame frame) {
		this(frame, frame.size);
	}

	public MyPanel(MyFrame frame, Dimension size) {
		this.frame = frame;
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setLayout(null);
		menu = new MyMenu(this);
		statusbar = new MyStatusBar();
		myResize(size);
		add(menu);
		add(statusbar);
		setVisible(true);
		timer = new MyTimer(statusbar);
		puzzle_list.add(Cube.class);
		puzzle_list.add(Pyraminx.class);
		puzzle_list.add(Megaminx.class);
		puzzle_list.add(Skewb.class);
		puzzle_list.add(CrazyCube444.class);
		createPuzzle(puzzle_list.get(0));
		count = 0;
		relay_layer = relay_n = false;
		new Thread(timer).start();
		new Thread(new StateThread(this)).start();
		changePuzzle();
	}

	public void myResize(Dimension size) {
		setSize(size);
		int width = this.getWidth();
		int height = this.getHeight();
		menu.setBounds(0, 0, width, 24);
		statusbar.setBounds((width - 300) / 2, 30, 300, 20);
		buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g2 = buf.createGraphics();
		g2.translate(width / 2, height / 2);
		double scale = Math.min(width, height);
		g2.scale(scale, -scale);
		g2.setStroke(new BasicStroke((float) 0.001));
	}

	private void toggleFullscreen() {
		if (frame != null) {
			Dimension size;
			if (frame.fullscreen) {
				frame.dispose();
				frame.setUndecorated(false);
				frame.setVisible(false);
				frame.getGraphicsConfiguration().getDevice()
						.setFullScreenWindow(null);
				frame.setSize(size = frame.size);
				frame.setLocation(frame.location);
				frame.setVisible(true);
				frame.fullscreen = false;
			} else {
				frame.dispose();
				frame.setUndecorated(true);
				frame.setVisible(false);
				frame.getGraphicsConfiguration().getDevice()
						.setFullScreenWindow(frame);
				size = frame.getSize();
				frame.fullscreen = true;
			}
			myResize(size);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g2.setColor(Color.BLACK);
		g2.fillRect(-1, -1, 2, 2);
		puzzle.draw(g2);
		g.drawImage(buf, 0, 0, null);
	}

	private boolean alt_pressed = false;

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
		case KeyEvent.VK_F4:
			if (alt_pressed) {
				System.exit(0);
			}
			break;
		case KeyEvent.VK_F11:
			toggleFullscreen();
			break;
		case KeyEvent.VK_ALT:
			alt_pressed = true;
			break;
		default:
			int k = puzzle.apply(key);
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
		switch (e.getExtendedKeyCode()) {
		case KeyEvent.VK_ALT:
			alt_pressed = false;
			break;
		}
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
			puzzle.rotateEye(-2.0 * (x - drag[1]) / getWidth(), 2.0
					* (y - drag[2]) / getHeight());
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
			puzzle = puzzle_type.cast(puzzle_type.getConstructor(
					JComponent.class).newInstance(this));
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
	}

	public void createPuzzle(Class<? extends Puzzle> puzzle_type, int size) {
		this.puzzle_type = puzzle_type;
		try {
			puzzle = puzzle_type.cast(puzzle_type.getConstructor(
					JComponent.class, int.class).newInstance(this,
					puzzle_size = size));
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
	}

	public boolean nextPuzzle() {
		if (relay_layer) {
			if (puzzle_size < 7) {
				createPuzzle(puzzle_type,
						puzzle_size = puzzle.getBiggerSize(puzzle_size));
				puzzle.preScramble().scramble();
				System.out.println(relay_layer);
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