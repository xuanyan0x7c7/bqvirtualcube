package puzzle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

import draw.Facelet;
import draw.PolygonFacelet;
import draw.Vertex;

public class Skewb extends Puzzle {

	private PolygonFacelet[][] facelet = null;
	private static Color[] color_scheme = new Color[] { Color.WHITE,
			Color.YELLOW, Color.RED, new Color(255, 160, 0), Color.GREEN,
			Color.BLUE, Color.GRAY };

	private static final int UR = 0;
	private static final int UR2 = 1;
	private static final int DBL = 2;
	private static final int DBL2 = 3;
	private static final int UL = 4;
	private static final int UL2 = 5;
	private static final int DBR = 6;
	private static final int DBR2 = 7;
	private static final int UBR = 8;
	private static final int UBR2 = 9;
	private static final int DL = 10;
	private static final int DL2 = 11;
	private static final int UBL = 12;
	private static final int UBL2 = 13;
	private static final int DR = 14;
	private static final int DR2 = 15;
	private static final int x = 16;
	private static final int x3 = 17;
	private static final int y = 18;
	private static final int y3 = 19;
	private static final int z = 20;
	private static final int z3 = 21;

	private static final Vertex[] face_axis = new Vertex[] {
			new Vertex(0, 0, 1), new Vertex(0, 0, -1), new Vertex(0, 1, 0),
			new Vertex(0, -1, 0), new Vertex(1, 0, 0), new Vertex(-1, 0, 0) };
	private static final Vertex[] vertex_axis = new Vertex[] {
			new Vertex(1, 1, 1).normalize(),
			new Vertex(-1, -1, -1).normalize(),
			new Vertex(1, -1, 1).normalize(),
			new Vertex(-1, 1, -1).normalize(),
			new Vertex(-1, 1, 1).normalize(),
			new Vertex(1, -1, -1).normalize(),
			new Vertex(-1, -1, 1).normalize(), new Vertex(1, 1, -1).normalize() };

	public Skewb(JComponent panel) {
		super(panel, "Skewb");
		size = 1;
		facelets = 30;
		facelet = new PolygonFacelet[6][5];
		setEye(new Vertex(5, 3, 4));
		reset();
	}

	@Override
	public int getDefaultSize() {
		return 1;
	}

	@Override
	public int getSmallerSize(int size) {
		return 1;
	}

	@Override
	public int getBiggerSize(int size) {
		return 1;
	}

	@Override
	public Puzzle reset() {
		facelet_ratio = 0.85;
		for (int i = 0; i < 6; ++i) {
			facelet[i][0] = new PolygonFacelet(4, color_scheme[6],
					color_scheme[i]);
			for (int j = 1; j < 5; ++j) {
				facelet[i][j] = new PolygonFacelet(3, color_scheme[6],
						color_scheme[i]);
			}
		}
		facelet[0][0].vertex[0] = new Vertex(-1, 0, 1);
		facelet[0][0].vertex[1] = new Vertex(0, -1, 1);
		facelet[0][0].vertex[2] = new Vertex(1, 0, 1);
		facelet[0][0].vertex[3] = new Vertex(0, 1, 1);
		facelet[0][1].vertex[0] = new Vertex(-1, -1, 1);
		facelet[0][1].vertex[1] = new Vertex(-1, 0, 1);
		facelet[0][1].vertex[2] = new Vertex(0, -1, 1);
		facelet[0][2].vertex[0] = new Vertex(1, -1, 1);
		facelet[0][2].vertex[1] = new Vertex(0, -1, 1);
		facelet[0][2].vertex[2] = new Vertex(1, 0, 1);
		facelet[0][3].vertex[0] = new Vertex(1, 1, 1);
		facelet[0][3].vertex[1] = new Vertex(0, 1, 1);
		facelet[0][3].vertex[2] = new Vertex(1, 0, 1);
		facelet[0][4].vertex[0] = new Vertex(-1, 1, 1);
		facelet[0][4].vertex[1] = new Vertex(-1, 0, 1);
		facelet[0][4].vertex[2] = new Vertex(0, 1, 1);
		for (int i = 0; i < 4; ++i) {
			Vertex p = facelet[0][0].vertex[i];
			facelet[1][0].vertex[i] = new Vertex(-p.x, -p.y, -p.z);
			facelet[2][0].vertex[i] = new Vertex(p.y, p.z, p.x);
			facelet[3][0].vertex[i] = new Vertex(-p.y, -p.z, -p.x);
			facelet[4][0].vertex[i] = new Vertex(p.z, p.x, p.y);
			facelet[5][0].vertex[i] = new Vertex(-p.z, -p.x, -p.y);
		}
		for (int i = 1; i < 5; ++i) {
			for (int j = 0; j < 3; ++j) {
				Vertex p = facelet[0][i].vertex[j];
				facelet[1][i].vertex[j] = new Vertex(-p.x, -p.y, -p.z);
				facelet[2][i].vertex[j] = new Vertex(p.y, p.z, p.x);
				facelet[3][i].vertex[j] = new Vertex(-p.y, -p.z, -p.x);
				facelet[4][i].vertex[j] = new Vertex(p.z, p.x, p.y);
				facelet[5][i].vertex[j] = new Vertex(-p.z, -p.x, -p.y);
			}
		}
		for (int i = 0; i < 6; ++i) {
			PolygonFacelet f = facelet[i][0];
			f.center = new Vertex(f.vertex[0]).move(f.vertex[1])
					.move(f.vertex[2]).move(f.vertex[3]).zoom(0.25);
			for (int j = 0; j < 4; ++j) {
				f.vertex[j].zoom(f.center, facelet_ratio);
			}
		}
		for (int i = 0; i < 6; ++i) {
			for (int j = 1; j < 5; ++j) {
				PolygonFacelet f = facelet[i][j];
				f.center = new Vertex(f.vertex[0]).move(f.vertex[1])
						.move(f.vertex[2]).zoom(1.0 / 3);
				for (int k = 0; k < 3; ++k) {
					f.vertex[k].zoom(f.center, facelet_ratio);
				}
			}
		}
		facelet_list = new Facelet[facelets];
		for (int i = 0; i < 6; ++i) {
			for (int k = 0; k < 5; ++k) {
				facelet_list[i * 5 + k] = facelet[i][k];
			}
		}
		return this;
	}

	@Override
	public boolean isSolved() {
		final double epsilon = 1e-3;
		for (int i = 0; i < 6; ++i) {
			int x = 0;
			for (int j = 0; j < 6; ++j) {
				if (Math.abs(facelet[i][0].center.innerProduct(face_axis[j]) - 1) < epsilon) {
					x = j;
					break;
				}
			}
			for (int k = 1; k < 5; ++k) {
				if (Math.abs(facelet[i][k].center.innerProduct(face_axis[x]) - 1) > epsilon) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Puzzle scramble() {
		final int[] vertex = new int[] { 4, 2, 1, 7 };
		int length = 20;
		int[][][] generator = new int[4][][];
		for (int i = 0; i < 4; ++i) {
			generator[i] = new int[2][];
			generator[i][0] = new int[1];
			generator[i][1] = new int[2];
		}
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				generator[i][1][j] = j;
			}
			generator[i][0][0] = i * 2;
		}
		int[] move = Puzzle.generatorScramble(length, generator);
		for (int i = 0; i < length; ++i) {
			for (int j = 0; j <= move[i] % 2; ++j) {
				twist(vertex[move[i] / 2], true, true);
			}
		}
		return this;
	}

	@Override
	public int apply(int key) {
		switch (key) {
		case KeyEvent.VK_SEMICOLON:
			return y;
		case KeyEvent.VK_A:
			return y3;
		case KeyEvent.VK_B:
			return x3;
		case KeyEvent.VK_D:
			return DBL;
		case KeyEvent.VK_E:
			return DBL2;
		case KeyEvent.VK_F:
			return DL;
		case KeyEvent.VK_I:
			return DBR;
		case KeyEvent.VK_J:
			return DR2;
		case KeyEvent.VK_K:
			return DBR2;
		case KeyEvent.VK_N:
			return x3;
		case KeyEvent.VK_P:
			return z;
		case KeyEvent.VK_Q:
			return z3;
		case KeyEvent.VK_R:
			return DL2;
		case KeyEvent.VK_T:
			return x;
		case KeyEvent.VK_U:
			return DR;
		case KeyEvent.VK_Y:
			return x;
		default:
			return -1;
		}
	}

	@Override
	public boolean isTwistLayer(int layer) {
		return layer < 16;
	}

	@Override
	public boolean isEntirelyTwist(int layer) {
		return layer >= 16;
	}

	@Override
	public boolean isParallel(int l1, int l2) {
		if (l1 < 16) {
			return l1 / 4 == l2 / 4;
		} else {
			return l1 / 2 == l2 / 2;
		}
	}

	@Override
	public Puzzle twist(int layer, boolean scramble) {
		switch (layer) {
		case UR:
			twist(0, true, scramble);
			break;
		case UR2:
			twist(0, false, scramble);
			break;
		case DBL:
			twist(1, true, scramble);
			break;
		case DBL2:
			twist(1, false, scramble);
			break;
		case UL:
			twist(2, true, scramble);
			break;
		case UL2:
			twist(2, false, scramble);
			break;
		case DBR:
			twist(3, true, scramble);
			break;
		case DBR2:
			twist(3, false, scramble);
			break;
		case UBR:
			twist(4, true, scramble);
			break;
		case UBR2:
			twist(4, false, scramble);
			break;
		case DL:
			twist(5, true, scramble);
			break;
		case DL2:
			twist(5, false, scramble);
			break;
		case UBL:
			twist(6, true, scramble);
			break;
		case UBL2:
			twist(6, false, scramble);
			break;
		case DR:
			twist(7, true, scramble);
			break;
		case DR2:
			twist(7, false, scramble);
			break;
		case x:
			twist(8, true, scramble);
			break;
		case x3:
			twist(8, false, scramble);
			break;
		case y:
			twist(9, true, scramble);
			break;
		case y3:
			twist(9, false, scramble);
			break;
		case z:
			twist(10, true, scramble);
			break;
		case z3:
			twist(10, false, scramble);
			break;
		}
		return this;
	}

	public void twist(int face, boolean dir, boolean scramble) {
		final int[] tface = new int[] { 2, 0, 4 };
		if (scramble) {
			for (int i = 0; i < facelets; ++i) {
				Facelet pf = facelet_list[i];
				if (face < 8) {
					double angle = 2 * Math.PI / 3;
					if (dir) {
						angle = -angle;
					}
					Vertex axis = vertex_axis[face];
					if (pf.center.innerProduct(axis) > 0) {
						pf.rotate(axis, angle);
					}
				} else {
					double angle = Math.PI / 2;
					if (dir) {
						angle = -angle;
					}
					pf.rotate(face_axis[tface[face - 8]], angle);
				}
			}
		} else {
			Vertex axis;
			double angle;
			boolean[] move = new boolean[facelets];
			if (face < 8) {
				angle = Math.PI / 6;
				axis = vertex_axis[face];
				for (int i = 0; i < facelets; ++i) {
					move[i] = facelet_list[i].center.innerProduct(axis) > 0;
				}
			} else {
				angle = Math.PI / 8;
				axis = face_axis[tface[face - 8]];
				for (int i = 0; i < facelets; ++i) {
					move[i] = true;
				}
			}
			if (dir) {
				angle = -angle;
			}
			for (int x = 0; x < 4; ++x) {
				long start = System.currentTimeMillis();
				synchronized (facelet_list) {
					for (int i = 0; i < facelets; ++i) {
						if (move[i]) {
							facelet_list[i].rotate(axis, angle);
						}
					}
				}
				panel.repaint();
				long time = System.currentTimeMillis() - start;
				long delay = 20 - time / 1000;
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		draw(g, 0, 0, 1.4);
	}

}
