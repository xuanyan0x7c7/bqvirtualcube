package puzzle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

import puzzle.Puzzle;

import draw.Facelet;
import draw.PolygonFacelet;
import draw.Vertex;

public class Cube extends Puzzle {

	private PolygonFacelet[][] facelet = null;
	private static Color[] color_scheme = new Color[] { Color.WHITE,
			Color.YELLOW, Color.RED, new Color(255, 160, 0), Color.GREEN,
			Color.BLUE, Color.GRAY };
	private int shiftU, shiftD, shiftR, shiftL, shiftF, shiftB;

	private static final int U = 0;
	private static final int U3 = 1;
	private static final int D = 2;
	private static final int D3 = 3;
	private static final int Uw = 4;
	private static final int Uw3 = 5;
	private static final int Dw = 6;
	private static final int Dw3 = 7;
	private static final int y = 8;
	private static final int y3 = 9;
	private static final int sUi = 10;
	private static final int sUo = 11;
	private static final int sDi = 12;
	private static final int sDo = 13;
	private static final int R = 14;
	private static final int R3 = 15;
	private static final int L = 16;
	private static final int L3 = 17;
	private static final int Rw = 18;
	private static final int Rw3 = 19;
	private static final int Lw = 20;
	private static final int Lw3 = 21;
	private static final int x = 22;
	private static final int x3 = 23;
	private static final int sRi = 24;
	private static final int sRo = 25;
	private static final int sLi = 26;
	private static final int sLo = 27;
	private static final int F = 28;
	private static final int F3 = 29;
	private static final int B = 30;
	private static final int B3 = 31;
	private static final int Fw = 32;
	private static final int Fw3 = 33;
	private static final int Bw = 34;
	private static final int Bw3 = 35;
	private static final int z = 36;
	private static final int z3 = 37;
	private static final int sFi = 38;
	private static final int sFo = 39;
	private static final int sBi = 40;
	private static final int sBo = 41;
	private static final int sReset = 42;

	private static final Vertex[] face_axis = new Vertex[] {
			new Vertex(0, 0, 1), new Vertex(0, 0, -1), new Vertex(0, 1, 0),
			new Vertex(0, -1, 0), new Vertex(1, 0, 0), new Vertex(-1, 0, 0) };

	public Cube(JComponent panel) {
		this(panel, 3);
	}

	public Cube(JComponent panel, int size) {
		super(panel, size + "*" + size + " Cube");
		this.size = size;
		facelets = 6 * size * size;
		facelet = new PolygonFacelet[6][size * size];
		setEye(new Vertex(4.5, 0, 4.5));
		reset();
	}

	@Override
	public Cube reset() {
		facelet_ratio = 0.83;
		shiftU = shiftD = shiftR = shiftL = shiftF = shiftB = 1;
		for (int x = 0; x < size * size; ++x) {
			int j = x / size;
			int k = x % size;
			for (int i = 0; i < 6; ++i) {
				facelet[i][x] = new PolygonFacelet(4, color_scheme[6],
						color_scheme[i]);
			}
			facelet[0][x].center = new Vertex((2.0 * j + 1) / size - 1,
					(2.0 * k + 1) / size - 1, 1);
			facelet[0][x].vertex[0] = new Vertex((2.0 * j) / size - 1,
					(2.0 * k) / size - 1, 1);
			facelet[0][x].vertex[1] = new Vertex((2.0 * j) / size - 1,
					(2.0 * k + 2) / size - 1, 1);
			facelet[0][x].vertex[2] = new Vertex((2.0 * j + 2) / size - 1,
					(2.0 * k + 2) / size - 1, 1);
			facelet[0][x].vertex[3] = new Vertex((2.0 * j + 2) / size - 1,
					(2.0 * k) / size - 1, 1);
			facelet[1][x].center = new Vertex(1 - (2.0 * j + 1) / size,
					(2.0 * k + 1) / size - 1, -1);
			facelet[1][x].vertex[0] = new Vertex(1 - (2.0 * j) / size,
					(2.0 * k) / size - 1, -1);
			facelet[1][x].vertex[1] = new Vertex(1 - (2.0 * j) / size,
					(2.0 * k + 2) / size - 1, -1);
			facelet[1][x].vertex[2] = new Vertex(1 - (2.0 * j + 2) / size,
					(2.0 * k + 2) / size - 1, -1);
			facelet[1][x].vertex[3] = new Vertex(1 - (2.0 * j + 2) / size,
					(2.0 * k) / size - 1, -1);
			facelet[2][x].center = new Vertex(1 - (2.0 * k + 1) / size, 1, 1
					- (2.0 * j + 1) / size);
			facelet[2][x].vertex[0] = new Vertex(1 - (2.0 * k) / size, 1, 1
					- (2.0 * j) / size);
			facelet[2][x].vertex[1] = new Vertex(1 - (2.0 * k) / size, 1, 1
					- (2.0 * j + 2) / size);
			facelet[2][x].vertex[2] = new Vertex(1 - (2.0 * k + 2) / size, 1, 1
					- (2.0 * j + 2) / size);
			facelet[2][x].vertex[3] = new Vertex(1 - (2.0 * k + 2) / size, 1, 1
					- (2.0 * j) / size);
			facelet[3][x].center = new Vertex((2.0 * k + 1) / size - 1, -1, 1
					- (2.0 * j + 1) / size);
			facelet[3][x].vertex[0] = new Vertex((2.0 * k) / size - 1, -1, 1
					- (2.0 * j) / size);
			facelet[3][x].vertex[1] = new Vertex((2.0 * k) / size - 1, -1, 1
					- (2.0 * j + 2) / size);
			facelet[3][x].vertex[2] = new Vertex((2.0 * k + 2) / size - 1, -1,
					1 - (2.0 * j + 2) / size);
			facelet[3][x].vertex[3] = new Vertex((2.0 * k + 2) / size - 1, -1,
					1 - (2.0 * j) / size);
			facelet[4][x].center = new Vertex(1, (2.0 * k + 1) / size - 1, 1
					- (2.0 * j + 1) / size);
			facelet[4][x].vertex[0] = new Vertex(1, (2.0 * k) / size - 1, 1
					- (2.0 * j) / size);
			facelet[4][x].vertex[1] = new Vertex(1, (2.0 * k) / size - 1, 1
					- (2.0 * j + 2) / size);
			facelet[4][x].vertex[2] = new Vertex(1, (2.0 * k + 2) / size - 1, 1
					- (2.0 * j + 2) / size);
			facelet[4][x].vertex[3] = new Vertex(1, (2.0 * k + 2) / size - 1, 1
					- (2.0 * j) / size);
			facelet[5][x].center = new Vertex(-1, 1 - (2.0 * k + 1) / size, 1
					- (2.0 * j + 1) / size);
			facelet[5][x].vertex[0] = new Vertex(-1, 1 - (2.0 * k) / size, 1
					- (2.0 * j) / size);
			facelet[5][x].vertex[1] = new Vertex(-1, 1 - (2.0 * k) / size, 1
					- (2.0 * j + 2) / size);
			facelet[5][x].vertex[2] = new Vertex(-1, 1 - (2.0 * k + 2) / size,
					1 - (2.0 * j + 2) / size);
			facelet[5][x].vertex[3] = new Vertex(-1, 1 - (2.0 * k + 2) / size,
					1 - (2.0 * j) / size);
			for (int i = 0; i < 6; ++i) {
				for (int l = 0; l < 4; ++l) {
					facelet[i][x].vertex[l].zoom(facelet[i][x].center,
							facelet_ratio);
				}
			}
		}
		facelet_list = new Facelet[facelets];
		for (int i = 0; i < 6; ++i) {
			for (int k = 0; k < size * size; ++k) {
				facelet_list[i * size * size + k] = facelet[i][k];
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
			for (int k = 1; k < size * size; ++k) {
				if (Math.abs(facelet[i][k].center.innerProduct(face_axis[x]) - 1) > epsilon) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Cube scramble() {
		int length = size == 2 ? 15 : (size == 3 ? 25 : 20 * (size - 2));
		int[][][] generator = new int[3][][];
		for (int i = 0; i < 3; ++i) {
			generator[i] = new int[2][];
			generator[i][0] = new int[size - 1];
			generator[i][1] = new int[3];
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				generator[i][1][j] = j;
			}
			for (int j = 0; j < size - 1; ++j) {
				generator[i][0][j] = i * 6 + j * 9 - (j & 1) * 6;
			}
		}
		int[] move = Puzzle.generatorScramble(length, generator);
		for (int i = 0; i < length; ++i) {
			for (int j = 0; j <= move[i] % 3; ++j) {
				twist((move[i] % 18) / 3, move[i] / 18 + 1, true, true);
			}
		}
		return this;
	}

	@Override
	public Cube preScramble() {
		return this.twist(z, true).twist(z, true).twist(y3, true);
	}

	@Override
	public int turn(int key) {
		switch (key) {
		case KeyEvent.VK_SPACE:
			return sReset;
		case KeyEvent.VK_SEMICOLON:
			return y;
		case KeyEvent.VK_COMMA:
			return Uw;
		case KeyEvent.VK_SLASH:
			return Dw3;
		case KeyEvent.VK_3:
			return sLo;
		case KeyEvent.VK_4:
			return sLi;
		case KeyEvent.VK_7:
			return sRi;
		case KeyEvent.VK_8:
			return sRo;
		case KeyEvent.VK_A:
			return y3;
		case KeyEvent.VK_B:
			return x3;
		case KeyEvent.VK_C:
			return Uw3;
		case KeyEvent.VK_D:
			return L;
		case KeyEvent.VK_E:
			return L3;
		case KeyEvent.VK_F:
			return U3;
		case KeyEvent.VK_G:
			return F3;
		case KeyEvent.VK_H:
			return F;
		case KeyEvent.VK_I:
			return R;
		case KeyEvent.VK_J:
			return U;
		case KeyEvent.VK_K:
			return R3;
		case KeyEvent.VK_L:
			return D3;
		case KeyEvent.VK_M:
			return Rw3;
		case KeyEvent.VK_N:
			return x3;
		case KeyEvent.VK_O:
			return B3;
		case KeyEvent.VK_P:
			return z;
		case KeyEvent.VK_Q:
			return z3;
		case KeyEvent.VK_R:
			return Lw3;
		case KeyEvent.VK_S:
			return D;
		case KeyEvent.VK_T:
			return x;
		case KeyEvent.VK_U:
			return Rw;
		case KeyEvent.VK_V:
			return Lw;
		case KeyEvent.VK_W:
			return B;
		case KeyEvent.VK_Y:
			return x;
		case KeyEvent.VK_Z:
			return Dw;
		default:
			return -1;
		}
	}

	@Override
	public boolean isTwistLayer(int layer) {
		return layer % 14 < 8 && layer != 42;
	}

	@Override
	public boolean isShift(int layer) {
		return layer % 14 > 9 || layer == 42;
	}

	@Override
	public boolean isEntirelyTwist(int layer) {
		int x = layer % 14;
		return x == 8 || x == 9;
	}

	@Override
	public boolean isParallel(int l1, int l2) {
		return l1 / 14 == l2 / 14;
	}

	@Override
	public void shift(int layer) {
		switch (layer) {
		case sUi:
			if (shiftU < size - 2) {
				++shiftU;
			}
			break;
		case sUo:
			if (shiftU > 1) {
				--shiftU;
			}
			break;
		case sDi:
			if (shiftD < size - 2) {
				++shiftD;
			}
			break;
		case sDo:
			if (shiftD > 1) {
				--shiftD;
			}
			break;
		case sRi:
			if (shiftR < size - 2) {
				++shiftR;
			}
			break;
		case sRo:
			if (shiftR > 1) {
				--shiftR;
			}
			break;
		case sLi:
			if (shiftL < size - 2) {
				++shiftL;
			}
			break;
		case sLo:
			if (shiftL > 1) {
				--shiftL;
			}
			break;
		case sFi:
			if (shiftF < size - 2) {
				++shiftF;
			}
			break;
		case sFo:
			if (shiftF > 1) {
				--shiftF;
			}
			break;
		case sBi:
			if (shiftB < size - 2) {
				++shiftB;
			}
			break;
		case sBo:
			if (shiftB > 1) {
				--shiftB;
			}
			break;
		case sReset:
			shiftU = shiftD = shiftR = shiftL = shiftF = shiftB = 1;
			break;
		}
	}

	@Override
	public Cube twist(int layer, boolean scramble) {
		switch (layer) {
		case U:
			twist(0, shiftU, true, scramble);
			break;
		case U3:
			twist(0, shiftU, false, scramble);
			break;
		case D:
			twist(1, shiftD, true, scramble);
			break;
		case D3:
			twist(1, shiftD, false, scramble);
			break;
		case Uw:
			twist(0, shiftU + 1, true, scramble);
			break;
		case Uw3:
			twist(0, shiftU + 1, false, scramble);
			break;
		case Dw:
			twist(1, shiftD + 1, true, scramble);
			break;
		case Dw3:
			twist(1, shiftD + 1, false, scramble);
			break;
		case y:
			twist(7, 0, true, scramble);
			break;
		case y3:
			twist(7, 0, false, scramble);
			break;
		case R:
			twist(2, shiftR, true, scramble);
			break;
		case R3:
			twist(2, shiftR, false, scramble);
			break;
		case L:
			twist(3, shiftL, true, scramble);
			break;
		case L3:
			twist(3, shiftL, false, scramble);
			break;
		case Rw:
			twist(2, shiftR + 1, true, scramble);
			break;
		case Rw3:
			twist(2, shiftR + 1, false, scramble);
			break;
		case Lw:
			twist(3, shiftL + 1, true, scramble);
			break;
		case Lw3:
			twist(3, shiftL + 1, false, scramble);
			break;
		case x:
			twist(6, 0, true, scramble);
			break;
		case x3:
			twist(6, 0, false, scramble);
			break;
		case F:
			twist(4, shiftF, true, scramble);
			break;
		case F3:
			twist(4, shiftF, false, scramble);
			break;
		case B:
			twist(5, shiftB, true, scramble);
			break;
		case B3:
			twist(5, shiftB, false, scramble);
			break;
		case Fw:
			twist(4, shiftF + 1, true, scramble);
			break;
		case Fw3:
			twist(4, shiftF + 1, false, scramble);
			break;
		case Bw:
			twist(5, shiftB + 1, true, scramble);
			break;
		case Bw3:
			twist(5, shiftB + 1, false, scramble);
			break;
		case z:
			twist(8, 0, true, scramble);
			break;
		case z3:
			twist(8, 0, false, scramble);
			break;
		}
		return this;
	}

	public void twist(int face, int layer, boolean dir, boolean scramble) {
		final int[] tface = new int[] { 0, 1, 2, 3, 4, 5, 2, 0, 4 };
		double distance = 1 - 2.0 * layer / size;
		if (scramble) {
			double angle = Math.PI / 2;
			if (dir) {
				angle = -angle;
			}
			for (int i = 0; i < facelets; ++i) {
				Facelet pf = facelet_list[i];
				if (face < 6) {
					Vertex axis = face_axis[face];
					if (pf.center.innerProduct(axis) > distance) {
						pf.rotate(axis, angle);
					}
				} else {
					pf.rotate(face_axis[tface[face]], angle);
				}
			}
		} else {
			double angle = Math.PI / 8;
			if (dir) {
				angle = -angle;
			}
			Vertex axis = face_axis[tface[face]];
			boolean[] move = new boolean[facelets];
			if (face < 6) {
				for (int i = 0; i < facelets; ++i) {
					move[i] = facelet_list[i].center.innerProduct(axis) > distance;
				}
			} else {
				for (int i = 0; i < facelets; ++i) {
					move[i] = true;
				}
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
