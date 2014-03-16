package puzzle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import model.MyPanel;

import draw.BezierFacelet;
import draw.Facelet;
import draw.PolygonFacelet;
import draw.Vertex;

public class CrazyCube444 extends Puzzle {

	private Facelet[][] facelet = null;
	private static Color[] color_scheme = new Color[] { Color.WHITE,
			Color.YELLOW, Color.RED, new Color(255, 160, 0), Color.GREEN,
			Color.BLUE, Color.GRAY };

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
	private static final int R = 10;
	private static final int R3 = 11;
	private static final int L = 12;
	private static final int L3 = 13;
	private static final int Rw = 14;
	private static final int Rw3 = 15;
	private static final int Lw = 16;
	private static final int Lw3 = 17;
	private static final int x = 18;
	private static final int x3 = 19;
	private static final int F = 20;
	private static final int F3 = 21;
	private static final int B = 22;
	private static final int B3 = 23;
	private static final int Fw = 24;
	private static final int Fw3 = 25;
	private static final int Bw = 26;
	private static final int Bw3 = 27;
	private static final int z = 28;
	private static final int z3 = 29;

	private static final Vertex[] face_axis = new Vertex[] {
			new Vertex(0, 0, 1), new Vertex(0, 0, -1), new Vertex(0, 1, 0),
			new Vertex(0, -1, 0), new Vertex(1, 0, 0), new Vertex(-1, 0, 0) };

	public CrazyCube444(MyPanel panel) {
		super("Crazy Cube 444");
		size = 4;
		this.panel = panel;
		facelets = 144;
		facelet = new Facelet[6][24];
		setEye(new Vertex(5, 3, 4));
		reset();
	}

	@Override
	public int getDefaultSize() {
		return 4;
	}

	@Override
	public int getSmallerSize(int size) {
		return 4;
	}

	@Override
	public int getBiggerSize(int size) {
		return 4;
	}

	@Override
	public Puzzle reset() {
		facelet_ratio = 0.85;
		double[][][] p = new double[24][][];
		double small = (1 - facelet_ratio) / 4;
		double bsmall = 0.5 + small;
		double big = (1 + facelet_ratio) / 4;
		double bbig = 0.5 + big;
		p[0] = new double[][] { { -big, -big }, { -big, -small },
				{ -small, -small }, { -small, -big } };
		p[1] = new double[][] { { -big, big }, { -big, small },
				{ -small, small }, { -small, big } };
		p[2] = new double[][] { { big, -big }, { big, -small },
				{ small, -small }, { small, -big } };
		p[3] = new double[][] { { big, big }, { big, small }, { small, small },
				{ small, big } };
		p[4] = new double[][] { { -bbig, -bbig }, { -bbig, -bsmall },
				{ -bsmall, -bsmall }, { -bsmall, -bbig } };
		p[5] = new double[][] { { -bbig, bbig }, { -bbig, bsmall },
				{ -bsmall, bsmall }, { -bsmall, bbig } };
		p[6] = new double[][] { { bbig, -bbig }, { bbig, -bsmall },
				{ bsmall, -bsmall }, { bsmall, -bbig } };
		p[7] = new double[][] { { bbig, bbig }, { bbig, bsmall },
				{ bsmall, bsmall }, { bsmall, bbig } };
		double ry1 = small, rx2 = bsmall;
		double rx1 = Math.sqrt(2 * big * big - small * small);
		double ry2 = Math.sqrt(2 * big * big - bsmall * bsmall);
		double angler = Math.acos((rx1 * rx2 + ry1 * ry2) / (2 * big * big));
		double rt = 4 * Math.tan(angler / 4) / 3;
		double[] r1 = new double[] { rx1 - ry1 * rt, ry1 + rx1 * rt };
		double[] r2 = new double[] { rx2 + ry2 * rt, ry2 - rx2 * rt };
		p[8] = new double[][] { { rx2, ry1 }, { (2 * rx2 + rx1) / 3, ry1 },
				{ (rx2 + 2 * rx1) / 3, ry1 }, { rx1, ry1 }, { r1[0], r1[1] },
				{ r2[0], r2[1] }, { rx2, ry2 }, { rx2, (2 * ry2 + ry1) / 3 },
				{ rx2, (ry2 + 2 * rx1) / 3 } };
		for (int i = 9; i < 16; ++i) {
			p[i] = new double[9][2];
		}
		for (int i = 0; i < 9; ++i) {
			p[9][i][0] = p[8][i][1];
			p[9][i][1] = p[8][i][0];
			p[10][i][0] = -p[8][i][0];
			p[10][i][1] = p[8][i][1];
			p[11][i][0] = p[8][i][1];
			p[11][i][1] = -p[8][i][0];
			p[12][i][0] = p[8][i][0];
			p[12][i][1] = -p[8][i][1];
			p[13][i][0] = -p[8][i][1];
			p[13][i][1] = p[8][i][0];
			p[14][i][0] = -p[8][i][0];
			p[14][i][1] = -p[8][i][1];
			p[15][i][0] = -p[8][i][1];
			p[15][i][1] = -p[8][i][0];
		}
		double Ry1 = small, Ry2 = big;
		double Rx1 = Math.sqrt(2 * bsmall * bsmall - small * small);
		double Rx2 = Math.sqrt(2 * bsmall * bsmall - big * big);
		double angleR = Math.acos((Rx1 * Rx2 + Ry1 * Ry2)
				/ (2 * bsmall * bsmall));
		double Rt = 4 * Math.tan(angleR / 4) / 3;
		double[] R1 = new double[] { Rx1 - Ry1 * Rt, Ry1 + Rx1 * Rt };
		double[] R2 = new double[] { Rx2 + Ry2 * Rt, Ry2 - Rx2 * Rt };
		p[16] = new double[][] { { bbig, Ry2 }, { bbig, (2 * Ry2 + Ry1) / 3 },
				{ bbig, (Ry2 + 2 * Ry1) / 3 }, { bbig, Ry1 },
				{ (2 * bbig + Rx1) / 3, Ry1 }, { (bbig + 2 * Rx1) / 3, Ry1 },
				{ Rx1, Ry1 }, { R1[0], R1[1] }, { R2[0], R2[1] }, { Rx2, Ry2 },
				{ (2 * Rx2 + bbig) / 3, Ry2 }, { (Rx2 + 2 * bbig) / 3, Ry2 }, };
		for (int i = 17; i < 24; ++i) {
			p[i] = new double[12][2];
		}
		for (int i = 0; i < 12; ++i) {
			p[17][i][0] = p[16][i][1];
			p[17][i][1] = p[16][i][0];
			p[18][i][0] = -p[16][i][0];
			p[18][i][1] = p[16][i][1];
			p[19][i][0] = p[16][i][1];
			p[19][i][1] = -p[16][i][0];
			p[20][i][0] = p[16][i][0];
			p[20][i][1] = -p[16][i][1];
			p[21][i][0] = -p[16][i][1];
			p[21][i][1] = p[16][i][0];
			p[22][i][0] = -p[16][i][0];
			p[22][i][1] = -p[16][i][1];
			p[23][i][0] = -p[16][i][1];
			p[23][i][1] = -p[16][i][0];
		}
		for (int i = 0; i < 6; ++i) {
			for (int k = 0; k < 8; ++k) {
				facelet[i][k] = new PolygonFacelet(4, color_scheme[6],
						color_scheme[i]);
			}
			for (int k = 8; k < 16; ++k) {
				facelet[i][k] = new BezierFacelet(3, color_scheme[6],
						color_scheme[i]);
			}
			for (int k = 16; k < 24; ++k) {
				facelet[i][k] = new BezierFacelet(4, color_scheme[6],
						color_scheme[i]);
			}
		}
		for (int i = 0; i < 8; ++i) {
			PolygonFacelet f = (PolygonFacelet) facelet[0][i];
			for (int j = 0; j < 4; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], p[i][j][1], 1);
			}
			f = (PolygonFacelet) facelet[1][i];
			for (int j = 0; j < 4; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], p[i][j][1], -1);
			}
			f = (PolygonFacelet) facelet[2][i];
			for (int j = 0; j < 4; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], 1, p[i][j][1]);
			}
			f = (PolygonFacelet) facelet[3][i];
			for (int j = 0; j < 4; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], -1, p[i][j][1]);
			}
			f = (PolygonFacelet) facelet[4][i];
			for (int j = 0; j < 4; ++j) {
				f.vertex[j] = new Vertex(1, p[i][j][0], p[i][j][1]);
			}
			f = (PolygonFacelet) facelet[5][i];
			for (int j = 0; j < 4; ++j) {
				f.vertex[j] = new Vertex(-1, p[i][j][0], p[i][j][1]);
			}
		}
		for (int i = 8; i < 16; ++i) {
			BezierFacelet f = (BezierFacelet) facelet[0][i];
			for (int j = 0; j < 9; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], p[i][j][1], 1);
			}
			f = (BezierFacelet) facelet[1][i];
			for (int j = 0; j < 9; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], p[i][j][1], -1);
			}
			f = (BezierFacelet) facelet[2][i];
			for (int j = 0; j < 9; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], 1, p[i][j][1]);
			}
			f = (BezierFacelet) facelet[3][i];
			for (int j = 0; j < 9; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], -1, p[i][j][1]);
			}
			f = (BezierFacelet) facelet[4][i];
			for (int j = 0; j < 9; ++j) {
				f.vertex[j] = new Vertex(1, p[i][j][0], p[i][j][1]);
			}
			f = (BezierFacelet) facelet[5][i];
			for (int j = 0; j < 9; ++j) {
				f.vertex[j] = new Vertex(-1, p[i][j][0], p[i][j][1]);
			}
		}
		for (int i = 16; i < 24; ++i) {
			BezierFacelet f = (BezierFacelet) facelet[0][i];
			for (int j = 0; j < 12; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], p[i][j][1], 1);
			}
			f = (BezierFacelet) facelet[1][i];
			for (int j = 0; j < 12; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], p[i][j][1], -1);
			}
			f = (BezierFacelet) facelet[2][i];
			for (int j = 0; j < 12; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], 1, p[i][j][1]);
			}
			f = (BezierFacelet) facelet[3][i];
			for (int j = 0; j < 12; ++j) {
				f.vertex[j] = new Vertex(p[i][j][0], -1, p[i][j][1]);
			}
			f = (BezierFacelet) facelet[4][i];
			for (int j = 0; j < 12; ++j) {
				f.vertex[j] = new Vertex(1, p[i][j][0], p[i][j][1]);
			}
			f = (BezierFacelet) facelet[5][i];
			for (int j = 0; j < 12; ++j) {
				f.vertex[j] = new Vertex(-1, p[i][j][0], p[i][j][1]);
			}
		}
		facelet_list = new Facelet[facelets];
		for (int i = 0; i < 6; ++i) {
			for (int k = 0; k < 8; ++k) {
				PolygonFacelet f = (PolygonFacelet) facelet[i][k];
				double x = 0, y = 0, z = 0;
				for (int j = 0; j < 4; ++j) {
					x += f.vertex[j].x;
					y += f.vertex[j].y;
					z += f.vertex[j].z;
				}
				f.center = new Vertex(x / 4, y / 4, z / 4);
			}
			for (int k = 8; k < 16; ++k) {
				BezierFacelet f = (BezierFacelet) facelet[i][k];
				double x = 0, y = 0, z = 0;
				for (int j = 0; j < 9; j += 3) {
					x += f.vertex[j].x;
					y += f.vertex[j].y;
					z += f.vertex[j].z;
				}
				f.center = new Vertex(x / 3, y / 3, z / 3);
			}
			for (int k = 16; k < 24; ++k) {
				BezierFacelet f = (BezierFacelet) facelet[i][k];
				double x = 0, y = 0, z = 0;
				for (int j = 0; j < 12; j += 3) {
					x += f.vertex[j].x;
					y += f.vertex[j].y;
					z += f.vertex[j].z;
				}
				f.center = new Vertex(x / 4, y / 4, z / 4);
			}
			for (int k = 0; k < 24; ++k) {
				facelet_list[i * 24 + k] = facelet[i][k];
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
			for (int k = 1; k < 24; ++k) {
				if (Math.abs(facelet[i][k].center.innerProduct(face_axis[x]) - 1) > epsilon) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Puzzle scramble() {
		int length = 40;
		int[][][] generator = new int[3][][];
		for (int i = 0; i < 3; ++i) {
			generator[i] = new int[2][];
			generator[i][0] = new int[3];
			generator[i][1] = new int[3];
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				generator[i][1][j] = j;
			}
			for (int j = 0; j < 3; ++j) {
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
	public Puzzle preScramble() {
		return this.twist(z, true).twist(z, true).twist(y3, true);
	}

	@Override
	public int turn(int key) {
		switch (key) {
		case KeyEvent.VK_SEMICOLON:
			return y;
		case KeyEvent.VK_COMMA:
			return Uw;
		case KeyEvent.VK_SLASH:
			return Dw3;
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
		return layer % 10 < 8;
	}

	@Override
	public boolean isShift(int layer) {
		return false;
	}

	@Override
	public boolean isEntirelyTwist(int layer) {
		return layer % 10 > 7;
	}

	@Override
	public boolean isParallel(int l1, int l2) {
		return l1 / 10 == l2 / 10;
	}

	@Override
	public void shift(int layer) {
	}

	@Override
	public Puzzle twist(int layer, boolean scramble) {
		switch (layer) {
		case U:
			twist(0, 1, true, scramble);
			break;
		case U3:
			twist(0, 1, false, scramble);
			break;
		case D:
			twist(1, 1, true, scramble);
			break;
		case D3:
			twist(1, 1, false, scramble);
			break;
		case Uw:
			twist(0, 2, true, scramble);
			break;
		case Uw3:
			twist(0, 2, false, scramble);
			break;
		case Dw:
			twist(1, 2, true, scramble);
			break;
		case Dw3:
			twist(1, 2, false, scramble);
			break;
		case y:
			twist(7, 0, true, scramble);
			break;
		case y3:
			twist(7, 0, false, scramble);
			break;
		case R:
			twist(2, 1, true, scramble);
			break;
		case R3:
			twist(2, 1, false, scramble);
			break;
		case L:
			twist(3, 1, true, scramble);
			break;
		case L3:
			twist(3, 1, false, scramble);
			break;
		case Rw:
			twist(2, 2, true, scramble);
			break;
		case Rw3:
			twist(2, 2, false, scramble);
			break;
		case Lw:
			twist(3, 2, true, scramble);
			break;
		case Lw3:
			twist(3, 2, false, scramble);
			break;
		case x:
			twist(6, 0, true, scramble);
			break;
		case x3:
			twist(6, 0, false, scramble);
			break;
		case F:
			twist(4, 1, true, scramble);
			break;
		case F3:
			twist(4, 1, false, scramble);
			break;
		case B:
			twist(5, 1, true, scramble);
			break;
		case B3:
			twist(5, 1, false, scramble);
			break;
		case Fw:
			twist(4, 2, true, scramble);
			break;
		case Fw3:
			twist(4, 2, false, scramble);
			break;
		case Bw:
			twist(5, 2, true, scramble);
			break;
		case Bw3:
			twist(5, 2, false, scramble);
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
		double distance = 1 - layer / 2.0;
		if (scramble) {
			double angle = Math.PI / 2;
			if (dir) {
				angle = -angle;
			}
			for (int i = 0; i < facelets; ++i) {
				Facelet pf = facelet_list[i];
				if (face < 6) {
					Vertex axis = face_axis[face];
					if (pf.center.innerProduct(axis) > distance
							&& (layer == 2 || Vertex.sqrDist(pf.center, axis) > 0.5)) {
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
					move[i] = facelet_list[i].center.innerProduct(axis) > distance
							&& (layer == 2 || Vertex.sqrDist(
									facelet_list[i].center, axis) > 0.5);
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
