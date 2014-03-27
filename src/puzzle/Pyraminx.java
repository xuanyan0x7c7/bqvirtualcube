package puzzle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JComponent;

import draw.Facelet;
import draw.PolygonFacelet;
import draw.Vertex;

public class Pyraminx extends Puzzle {

	private PolygonFacelet[][] facelet = null;
	private static Color[] color_scheme = new Color[] { Color.YELLOW,
			Color.GREEN, Color.BLUE, Color.RED, Color.GRAY };
	public int shiftU, shiftD, shiftR, shiftLf, shiftL, shiftRf, shiftF,
			shiftB;
	private int minimum_size = 3;

	private static final int U = 0;
	private static final int U2 = 1;
	private static final int u = 2;
	private static final int u2 = 3;
	private static final int Uw = 4;
	private static final int Uw2 = 5;
	private static final int D = 6;
	private static final int D2 = 7;
	private static final int Dw = 8;
	private static final int Dw2 = 9;
	private static final int y = 10;
	private static final int y2 = 11;
	private static final int sUi = 12;
	private static final int sUo = 13;
	private static final int sDi = 14;
	private static final int sDo = 15;
	private static final int B = 16;
	private static final int B2 = 17;
	private static final int b = 18;
	private static final int b2 = 19;
	private static final int Bw = 20;
	private static final int Bw2 = 21;
	private static final int F = 22;
	private static final int F2 = 23;
	private static final int Fw = 24;
	private static final int Fw2 = 25;
	private static final int z = 26;
	private static final int z2 = 27;
	private static final int sBi = 28;
	private static final int sBo = 29;
	private static final int sFi = 30;
	private static final int sFo = 31;
	private static final int R = 32;
	private static final int R2 = 33;
	private static final int r = 34;
	private static final int r2 = 35;
	private static final int Rw = 36;
	private static final int Rw2 = 37;
	private static final int Lf = 38;
	private static final int Lf2 = 39;
	private static final int Lfw = 40;
	private static final int Lfw2 = 41;
	private static final int xR = 42;
	private static final int xR2 = 43;
	private static final int sRi = 44;
	private static final int sRo = 45;
	private static final int sLfi = 46;
	private static final int sLfo = 47;
	private static final int L = 48;
	private static final int L2 = 49;
	private static final int l = 50;
	private static final int l2 = 51;
	private static final int Lw = 52;
	private static final int Lw2 = 53;
	private static final int Rf = 54;
	private static final int Rf2 = 55;
	private static final int Rfw = 56;
	private static final int Rfw2 = 57;
	private static final int xL = 58;
	private static final int xL2 = 59;
	private static final int sLi = 60;
	private static final int sLo = 61;
	private static final int sRfi = 62;
	private static final int sRfo = 63;
	private static final int sReset = 64;

	private static final Vertex[] face_axis = new Vertex[] {
			new Vertex(1, 1, 1).normalize(),
			new Vertex(-1, -1, -1).normalize(),
			new Vertex(-1, -1, 1).normalize(),
			new Vertex(1, 1, -1).normalize(),
			new Vertex(1, -1, -1).normalize(),
			new Vertex(-1, 1, 1).normalize(),
			new Vertex(-1, 1, -1).normalize(), new Vertex(1, -1, 1).normalize() };

	public Pyraminx(JComponent panel) {
		this(panel, 3);
	}

	public Pyraminx(JComponent panel, int size) {
		super(panel, size + "*" + size + " Pyraminx");
		minimum_size = 3;
		this.size = size;
		facelets = 4 * size * size;
		facelet = new PolygonFacelet[4][size * size];
		setEye(new Vertex(2, 2, -2));
		reset();
	}

	@Override
	public int getSmallerSize(int size) {
		return size > minimum_size ? size - 1 : size;
	}

	@Override
	public Pyraminx reset() {
		facelet_ratio = 0.75;
		final Vertex vertex[] = new Vertex[] { new Vertex(1, 1, 1),
				new Vertex(-1, -1, 1), new Vertex(1, -1, -1),
				new Vertex(-1, 1, -1) };
		final Vertex[] start_point = new Vertex[] { vertex[1], vertex[0],
				vertex[0], vertex[0] };
		final Vertex[] row_vector = new Vertex[] {
				vertex[1].getVector(vertex[2]).zoom(1.0 / size),
				vertex[0].getVector(vertex[2]).zoom(1.0 / size),
				vertex[0].getVector(vertex[1]).zoom(1.0 / size),
				vertex[0].getVector(vertex[1]).zoom(1.0 / size) };
		final Vertex[] column_vector = new Vertex[] {
				vertex[2].getVector(vertex[3]).zoom(1.0 / size),
				vertex[2].getVector(vertex[3]).zoom(1.0 / size),
				vertex[1].getVector(vertex[3]).zoom(1.0 / size),
				vertex[1].getVector(vertex[2]).zoom(1.0 / size) };
		shiftU = shiftB = shiftR = shiftL = 2;
		shiftD = shiftF = shiftLf = shiftRf = 1;
		for (int i = 0; i < 4; ++i) {
			for (int k = 0; k < size * size; ++k) {
				facelet[i][k] = new PolygonFacelet(3, color_scheme[4],
						color_scheme[i]);
			}
			int index = 0;
			for (int j = 0; j < size; ++j) {
				for (int k = 0; k <= j; ++k) {
					PolygonFacelet f = facelet[i][index++];
					f.vertex[0] = new Vertex(start_point[i]).move(
							new Vertex(row_vector[i]).zoom(j)).move(
							new Vertex(column_vector[i]).zoom(k));
					f.vertex[1] = new Vertex(f.vertex[0]).move(row_vector[i]);
					f.vertex[2] = new Vertex(f.vertex[1])
							.move(column_vector[i]);
				}
			}
			for (int j = 0; j < size - 1; ++j) {
				for (int k = 0; k <= j; ++k) {
					PolygonFacelet f = facelet[i][index++];
					f.vertex[0] = new Vertex(start_point[i]).move(
							new Vertex(row_vector[i]).zoom(j + 2)).move(
							new Vertex(column_vector[i]).zoom(k + 1));
					f.vertex[1] = new Vertex(f.vertex[0]).move(new Vertex(
							row_vector[i]).zoom(-1));
					f.vertex[2] = new Vertex(f.vertex[1]).move(new Vertex(
							column_vector[i]).zoom(-1));
				}
			}
			for (int k = 0; k < size * size; ++k) {
				PolygonFacelet f = facelet[i][k];
				f.center = new Vertex(f.vertex[0]).move(f.vertex[1])
						.move(f.vertex[2]).zoom(1.0 / 3);
				for (int x = 0; x < 3; ++x) {
					f.vertex[x].zoom(f.center, facelet_ratio);
				}
			}
		}
		facelet_list = new Facelet[facelets];
		for (int i = 0; i < 4; ++i) {
			for (int k = 0; k < size * size; ++k) {
				facelet_list[i * size * size + k] = facelet[i][k];
			}
		}
		return this;
	}

	@Override
	public boolean isSolved() {
		final double zero = 1e-3;
		final double r = Math.sqrt(3) / 3;
		for (int i = 0; i < 4; ++i) {
			int x = 0;
			for (int j = 0; j < 4; ++j) {
				if (Math.abs(facelet[i][0].center
						.innerProduct(face_axis[2 * j + 1]) - r) < zero) {
					x = 2 * j + 1;
					break;
				}
			}
			for (int k = 1; k < size * size; ++k) {
				if (Math.abs(facelet[i][k].center.innerProduct(face_axis[x]) - 1) > zero)
					return false;
			}
		}
		return true;
	}

	@Override
	public Pyraminx scramble() {
		int length = 20 * (size - 2);
		int[][][] generator = new int[4][][];
		for (int i = 0; i < 4; ++i) {
			generator[i] = new int[2][];
			generator[i][0] = new int[size - 2];
			generator[i][1] = new int[2];
		}
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 2; ++j) {
				generator[i][1][j] = j;
			}
			for (int j = 0; j < size - 2; ++j) {
				generator[i][0][j] = i * 2 + j * 8;
			}
		}
		int[] move = Puzzle.generatorScramble(length, generator);
		for (int i = 0; i < length; ++i) {
			for (int j = 0; j <= move[i] % 2; ++j) {
				twist((move[i] % 8) / 2 * 2, move[i] / 8 + 2, true, true);
			}
		}
		Random rand = new Random();
		for (int i = 0; i < 4; ++i) {
			int x = rand.nextInt(3);
			for (int j = 0; j < x; ++j) {
				twist(i * 2, 1, true, true);
			}
		}
		return this;
	}

	@Override
	public int apply(int key) {
		switch (key) {
		case KeyEvent.VK_SPACE:
			return sReset;
		case KeyEvent.VK_SEMICOLON:
			return y;
		case KeyEvent.VK_COMMA:
			return Uw;
		case KeyEvent.VK_SLASH:
			return Dw2;
		case KeyEvent.VK_3:
			return sLo;
		case KeyEvent.VK_4:
			return sLi;
		case KeyEvent.VK_7:
			return sRi;
		case KeyEvent.VK_8:
			return sRo;
		case KeyEvent.VK_A:
			return y2;
		case KeyEvent.VK_B:
			return xL;
		case KeyEvent.VK_C:
			return Uw2;
		case KeyEvent.VK_D:
			return L;
		case KeyEvent.VK_E:
			return L2;
		case KeyEvent.VK_F:
			return U2;
		case KeyEvent.VK_I:
			return R;
		case KeyEvent.VK_J:
			return U;
		case KeyEvent.VK_K:
			return R2;
		case KeyEvent.VK_L:
			return D2;
		case KeyEvent.VK_M:
			return Rw2;
		case KeyEvent.VK_N:
			return xR2;
		case KeyEvent.VK_O:
			return B2;
		case KeyEvent.VK_P:
			return z;
		case KeyEvent.VK_Q:
			return z2;
		case KeyEvent.VK_R:
			return Lw2;
		case KeyEvent.VK_S:
			return D;
		case KeyEvent.VK_T:
			return xL2;
		case KeyEvent.VK_U:
			return Rw;
		case KeyEvent.VK_V:
			return Lw;
		case KeyEvent.VK_W:
			return B;
		case KeyEvent.VK_Y:
			return xR;
		case KeyEvent.VK_Z:
			return Dw;
		default:
			return -1;
		}
	}

	@Override
	public boolean isTwistLayer(int layer) {
		return layer % 16 < 10 && layer != 64;
	}

	@Override
	public boolean isShift(int layer) {
		return layer % 16 > 11 || layer == 64;
	}

	public boolean isEntirelyTwist(int layer) {
		int x = layer % 16;
		return x == 10 || x == 11;
	}

	public boolean isParallel(int l1, int l2) {
		return l1 / 16 == l2 / 16;
	}

	public void shift(int layer) {
		switch (layer) {
		case sUi:
			if (shiftU < size - 1) {
				++shiftU;
			}
			break;
		case sUo:
			if (shiftU > 1) {
				--shiftU;
			}
			break;
		case sDi:
			if (shiftD < size - 1) {
				++shiftD;
			}
			break;
		case sDo:
			if (shiftD > 1) {
				--shiftD;
			}
			break;
		case sBi:
			if (shiftB < size - 1) {
				++shiftB;
			}
			break;
		case sBo:
			if (shiftB > 1) {
				--shiftB;
			}
			break;
		case sFi:
			if (shiftF < size - 1) {
				++shiftF;
			}
			break;
		case sFo:
			if (shiftF > 1) {
				--shiftF;
			}
			break;
		case sRi:
			if (shiftR < size - 1) {
				++shiftR;
			}
			break;
		case sRo:
			if (shiftR > 1) {
				--shiftR;
			}
			break;
		case sLfi:
			if (shiftLf < size - 1) {
				++shiftLf;
			}
			break;
		case sLfo:
			if (shiftLf > 1) {
				--shiftLf;
			}
			break;
		case sLi:
			if (shiftL < size - 1) {
				++shiftL;
			}
			break;
		case sLo:
			if (shiftL > 1) {
				--shiftL;
			}
			break;
		case sRfi:
			if (shiftRf < size - 1) {
				++shiftRf;
			}
			break;
		case sRfo:
			if (shiftRf > 1) {
				--shiftRf;
			}
			break;
		case sReset:
			shiftU = shiftB = shiftR = shiftL = 2;
			shiftD = shiftF = shiftLf = shiftRf = 1;
			break;
		}
	}

	@Override
	public Pyraminx twist(int layer, boolean scramble) {
		switch (layer) {
		case U:
			twist(0, shiftU, true, scramble);
			break;
		case U2:
			twist(0, shiftU, false, scramble);
			break;
		case u:
			twist(0, 1, true, scramble);
			break;
		case u2:
			twist(0, 1, false, scramble);
			break;
		case Uw:
			twist(0, shiftU + 1, true, scramble);
			break;
		case Uw2:
			twist(0, shiftU + 1, false, scramble);
			break;
		case D:
			twist(1, shiftD, true, scramble);
			break;
		case D2:
			twist(1, shiftD, false, scramble);
			break;
		case Dw:
			twist(1, shiftD + 1, true, scramble);
			break;
		case Dw2:
			twist(1, shiftD + 1, false, scramble);
			break;
		case y:
			twist(8, 0, true, scramble);
			break;
		case y2:
			twist(8, 0, false, scramble);
			break;
		case B:
			twist(2, shiftB, true, scramble);
			break;
		case B2:
			twist(2, shiftB, false, scramble);
			break;
		case b:
			twist(2, 1, true, scramble);
			break;
		case b2:
			twist(2, 1, false, scramble);
			break;
		case Bw:
			twist(2, shiftB + 1, true, scramble);
			break;
		case Bw2:
			twist(2, shiftB + 1, false, scramble);
			break;
		case F:
			twist(3, shiftF, true, scramble);
			break;
		case F2:
			twist(3, shiftF, false, scramble);
			break;
		case Fw:
			twist(3, shiftF + 1, true, scramble);
			break;
		case Fw2:
			twist(3, shiftF + 1, false, scramble);
			break;
		case z:
			twist(9, 0, true, scramble);
			break;
		case z2:
			twist(9, 0, false, scramble);
			break;
		case R:
			twist(4, shiftR, true, scramble);
			break;
		case R2:
			twist(4, shiftR, false, scramble);
			break;
		case r:
			twist(4, 1, true, scramble);
			break;
		case r2:
			twist(4, 1, false, scramble);
			break;
		case Rw:
			twist(4, shiftR + 1, true, scramble);
			break;
		case Rw2:
			twist(4, shiftR + 1, false, scramble);
			break;
		case Lf:
			twist(5, shiftLf, true, scramble);
			break;
		case Lf2:
			twist(5, shiftLf, false, scramble);
			break;
		case Lfw:
			twist(5, shiftLf + 1, true, scramble);
			break;
		case Lfw2:
			twist(5, shiftLf + 1, false, scramble);
			break;
		case xR:
			twist(10, 0, true, scramble);
			break;
		case xR2:
			twist(10, 0, false, scramble);
			break;
		case L:
			twist(6, shiftL, true, scramble);
			break;
		case L2:
			twist(6, shiftL, false, scramble);
			break;
		case l:
			twist(6, 1, true, scramble);
			break;
		case l2:
			twist(6, 1, false, scramble);
			break;
		case Lw:
			twist(6, shiftL + 1, true, scramble);
			break;
		case Lw2:
			twist(6, shiftL + 1, false, scramble);
			break;
		case Rf:
			twist(7, shiftRf, true, scramble);
			break;
		case Rf2:
			twist(7, shiftRf, false, scramble);
			break;
		case Rfw:
			twist(7, shiftRf + 1, true, scramble);
			break;
		case Rfw2:
			twist(7, shiftRf + 1, false, scramble);
			break;
		case xL:
			twist(11, 0, true, scramble);
			break;
		case xL2:
			twist(11, 0, false, scramble);
			break;
		}
		return this;
	}

	public void twist(int face, int layer, boolean dir, boolean scramble) {
		if (layer == size) {
			return;
		}
		final int[] tface = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 0, 3, 4, 6 };
		double[] distance = new double[] {
				(3 - 4.0 * layer / size) / Math.sqrt(3),
				(1 - 4.0 * layer / size) / Math.sqrt(3) };
		if (scramble) {
			double angle = Math.PI * 2 / 3;
			if (dir) {
				angle = -angle;
			}
			for (int i = 0; i < facelets; ++i) {
				Facelet pf = facelet_list[i];
				if (face < 8) {
					Vertex axis = face_axis[face];
					if (pf.center.innerProduct(axis) > distance[face % 2]) {
						pf.rotate(axis, angle);
					}
				} else {
					pf.rotate(face_axis[tface[face]], angle);
				}
			}
		} else {
			double angle = Math.PI / 6;
			if (dir) {
				angle = -angle;
			}
			Vertex axis = face_axis[tface[face]];
			boolean[] move = new boolean[facelets];
			if (face < 8) {
				for (int i = 0; i < facelets; ++i) {
					move[i] = facelet_list[i].center.innerProduct(axis) > distance[face % 2];
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
		draw(g, 0, -0.1, 0.75);
	}

}
