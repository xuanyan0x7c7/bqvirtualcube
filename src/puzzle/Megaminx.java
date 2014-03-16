package puzzle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import model.MyPanel;

import draw.Facelet;
import draw.PolygonFacelet;
import draw.Vertex;

public class Megaminx extends Puzzle {

	private PolygonFacelet[][] facelet = null;
	private static Color[] color_scheme = new Color[] { Color.WHITE,
			Color.DARK_GRAY, Color.GREEN, new Color(128, 255, 128), Color.RED,
			new Color(255, 160, 0), Color.BLUE, new Color(128, 128, 255),
			new Color(224, 224, 0), new Color(255, 255, 64), Color.MAGENTA,
			new Color(255, 128, 255), Color.GRAY };
	private MyPanel panel;
	private int minimum_size = 3;
	private int shiftU, shiftD, shiftF, shiftB, shiftR, shiftDBL, shiftBR,
			shiftDL, shiftBL, shiftDR, shiftL, shiftDBR;

	private static final int U = 0;
	private static final int U4 = 1;
	private static final int D = 2;
	private static final int D4 = 3;
	private static final int Uw = 4;
	private static final int Uw4 = 5;
	private static final int Dw = 6;
	private static final int Dw4 = 7;
	private static final int rU = 8;
	private static final int rU4 = 9;
	private static final int sUi = 10;
	private static final int sUo = 11;
	private static final int sDi = 12;
	private static final int sDo = 13;
	private static final int F = 14;
	private static final int F4 = 15;
	private static final int B = 16;
	private static final int B4 = 17;
	private static final int Fw = 18;
	private static final int Fw4 = 19;
	private static final int Bw = 20;
	private static final int Bw4 = 21;
	private static final int rF = 22;
	private static final int rF4 = 23;
	private static final int sFi = 24;
	private static final int sFo = 25;
	private static final int sBi = 26;
	private static final int sBo = 27;
	private static final int R = 28;
	private static final int R4 = 29;
	private static final int DBL = 30;
	private static final int DBL4 = 31;
	private static final int Rw = 32;
	private static final int Rw4 = 33;
	private static final int DBLw = 34;
	private static final int DBLw4 = 35;
	private static final int rR = 36;
	private static final int rR4 = 37;
	private static final int sRi = 38;
	private static final int sRo = 39;
	private static final int sDBLi = 40;
	private static final int sDBLo = 41;
	private static final int BR = 42;
	private static final int BR4 = 43;
	private static final int DL = 44;
	private static final int DL4 = 45;
	private static final int BRw = 46;
	private static final int BRw4 = 47;
	private static final int DLw = 48;
	private static final int DLw4 = 49;
	private static final int rBR = 50;
	private static final int rBR4 = 51;
	private static final int sBRi = 52;
	private static final int sBRo = 53;
	private static final int sDLi = 54;
	private static final int sDLo = 55;
	private static final int BL = 56;
	private static final int BL4 = 57;
	private static final int DR = 58;
	private static final int DR4 = 59;
	private static final int BLw = 60;
	private static final int BLw4 = 61;
	private static final int DRw = 62;
	private static final int DRw4 = 63;
	private static final int rBL = 64;
	private static final int rBL4 = 65;
	private static final int sBLi = 66;
	private static final int sBLo = 67;
	private static final int sDRi = 68;
	private static final int sDRo = 69;
	private static final int L = 70;
	private static final int L4 = 71;
	private static final int DBR = 72;
	private static final int DBR4 = 73;
	private static final int Lw = 74;
	private static final int Lw4 = 75;
	private static final int DBRw = 76;
	private static final int DBRw4 = 77;
	private static final int rL = 78;
	private static final int rL4 = 79;
	private static final int sLi = 80;
	private static final int sLo = 81;
	private static final int sDBRi = 82;
	private static final int sDBRo = 83;
	private static final int sReset = 84;

	private static final double phi = (Math.sqrt(5) - 1) / 2;
	private static final double rphi = 1 / Math.sqrt(2 - phi);

	private static final Vertex[] face_axis = new Vertex[] {
			new Vertex(phi * rphi, 0, rphi), new Vertex(-phi * rphi, 0, -rphi),
			new Vertex(rphi, phi * rphi, 0), new Vertex(-rphi, -phi * rphi, 0),
			new Vertex(0, rphi, phi * rphi), new Vertex(0, -rphi, -phi * rphi),
			new Vertex(-phi * rphi, 0, rphi), new Vertex(phi * rphi, 0, -rphi),
			new Vertex(0, -rphi, phi * rphi), new Vertex(0, rphi, -phi * rphi),
			new Vertex(rphi, -phi * rphi, 0), new Vertex(-rphi, phi * rphi, 0) };

	public Megaminx(MyPanel panel) {
		this(panel, 3);
	}

	public Megaminx(MyPanel panel, int size) {
		super("Megaminx");
		this.panel = panel;
		this.size = size;
		facelets = 12 * (5 * size * size / 4);
		facelet = new PolygonFacelet[12][5 * size * size / 4];
		setEye(new Vertex(5, 5 * phi, 0), new Vertex(-1, 1 + phi, phi),
				new Vertex(phi, -1, 3 + phi));
		reset();
	}

	@Override
	public int getSmallerSize(int size) {
		return size > minimum_size ? size - 2 : size;
	}

	@Override
	public int getBiggerSize(int size) {
		return size < maximum_size ? size + 2 : size;
	}

	@Override
	public Megaminx reset() {
		facelet_ratio = 0.85;
		shiftU = shiftF = shiftR = shiftBR = shiftBL = shiftL = 1;
		shiftD = shiftB = shiftDBL = shiftDL = shiftDR = shiftDBR = 1;
		final Vertex[] vertex = new Vertex[] { new Vertex(0, -phi, 1 + phi),
				new Vertex(0, phi, -1 - phi), new Vertex(1, -1, 1),
				new Vertex(-1, 1, -1), new Vertex(1 + phi, 0, phi),
				new Vertex(-1 - phi, 0, -phi), new Vertex(1, 1, 1),
				new Vertex(-1, -1, -1), new Vertex(0, phi, 1 + phi),
				new Vertex(0, -phi, -1 - phi), new Vertex(-1, -1, 1),
				new Vertex(1, 1, -1), new Vertex(phi, -1 - phi, 0),
				new Vertex(-phi, 1 + phi, 0), new Vertex(1 + phi, 0, -phi),
				new Vertex(-1 - phi, 0, phi), new Vertex(phi, 1 + phi, 0),
				new Vertex(-phi, -1 - phi, 0), new Vertex(-1, 1, 1),
				new Vertex(1, -1, -1) };
		final int[][] facelet_vertex = new int[][] { { 0, 2, 4, 6, 8 },
				{ 1, 3, 5, 7, 9 }, { 11, 16, 6, 4, 14 }, { 10, 17, 7, 5, 15 },
				{ 13, 18, 8, 6, 16 }, { 12, 19, 9, 7, 17 },
				{ 15, 10, 0, 8, 18 }, { 14, 11, 1, 9, 19 },
				{ 17, 12, 2, 0, 10 }, { 16, 13, 3, 1, 11 },
				{ 19, 14, 4, 2, 12 }, { 18, 15, 5, 3, 13 } };
		double alpha = 2.0 / (2 * size - 1);
		double beta = (size - 1.0) / (2 * size - 1);
		int s = 5 * size * size / 4;
		for (int i = 0; i < 12; ++i) {
			facelet[i][0] = new PolygonFacelet(5, color_scheme[12],
					color_scheme[i]);
			for (int k = 1; k < s; ++k) {
				facelet[i][k] = new PolygonFacelet(4, color_scheme[12],
						color_scheme[i]);
			}
			PolygonFacelet f = facelet[i][0];
			Vertex[] v = new Vertex[] { vertex[facelet_vertex[i][0]],
					vertex[facelet_vertex[i][1]], vertex[facelet_vertex[i][2]],
					vertex[facelet_vertex[i][3]], vertex[facelet_vertex[i][4]] };
			for (int j = 0; j < 5; ++j) {
				f.vertex[j] = new Vertex(v[j]).move(
						v[j].getVector(v[(j + 1) % 5]).zoom(beta)).move(
						v[j].getVector(v[(j + 4) % 5]).zoom(beta));
			}
			int index = 5 * size / 2 - 1;
			for (int j = 0; j < 5; ++j) {
				Vertex v0 = v[j];
				Vertex vx = v[j].getVector(v[(j + 1) % 5]);
				Vertex vy = v[j].getVector(v[(j + 4) % 5]);
				for (int k = 0; k < size / 2; ++k) {
					for (int l = 0; l < size / 2; ++l) {
						f = facelet[i][index++];
						f.vertex[0] = new Vertex(v0).move(
								new Vertex(vx).zoom(k * alpha)).move(
								new Vertex(vy).zoom(l * alpha));
						f.vertex[1] = new Vertex(v0).move(
								new Vertex(vx).zoom((k + 1) * alpha)).move(
								new Vertex(vy).zoom(l * alpha));
						f.vertex[2] = new Vertex(v0).move(
								new Vertex(vx).zoom((k + 1) * alpha)).move(
								new Vertex(vy).zoom((l + 1) * alpha));
						f.vertex[3] = new Vertex(v0).move(
								new Vertex(vx).zoom(k * alpha)).move(
								new Vertex(vy).zoom((l + 1) * alpha));
					}
				}
			}
			index = 1;
			for (int j = 0; j < 5; ++j) {
				for (int k = 0; k < size / 2; ++k) {
					PolygonFacelet p1 = facelet[i][(size - 1) * (size - 1) / 4
							* j + k + size * (size + 6) / 4];
					PolygonFacelet p2 = facelet[i][(size - 1) * (size - 1) / 4
							* ((j + 1) % 5) + size / 2 * k + 3 * size - 3];
					f = facelet[i][index++];
					f.vertex[0] = new Vertex(p2.vertex[3]);
					f.vertex[1] = new Vertex(p2.vertex[2]);
					f.vertex[2] = new Vertex(p1.vertex[2]);
					f.vertex[3] = new Vertex(p1.vertex[1]);
				}
			}
			for (int k = 0; k < s; ++k) {
				f = facelet[i][k];
				f.center = new Vertex();
				for (int j = 0; j < f.n; ++j) {
					f.center.move(f.vertex[j]);
				}
				f.center.zoom(1.0 / f.n);
				for (int j = 0; j < f.n; ++j) {
					f.vertex[j].zoom(f.center, facelet_ratio);
				}
			}
		}
		facelet_list = new Facelet[12 * s];
		for (int i = 0; i < 12; ++i) {
			for (int k = 0; k < s; ++k) {
				facelet_list[i * s + k] = facelet[i][k];
			}
		}
		return this;
	}

	@Override
	public boolean isSolved() {
		final double epsilon = 1e-3;
		final double r = (1 + phi) * rphi;
		int s = 5 * size * size / 4;
		for (int i = 0; i < 12; ++i) {
			int x = 0;
			for (int j = 0; j < 12; ++j) {
				if (Math.abs(facelet[i][0].center.innerProduct(face_axis[j])
						- r) < epsilon) {
					x = j;
					break;
				}
			}
			for (int k = 1; k < s; ++k) {
				if (Math.abs(facelet[i][k].center.innerProduct(face_axis[x])
						- r) > epsilon) {
					return false;
				}
			}
		}
		return true;
	}

	public Megaminx scramble() {
		int length = 40 * size;
		int[][][] generator = new int[6][][];
		for (int i = 0; i < 6; ++i) {
			generator[i] = new int[2][];
			generator[i][0] = new int[size - 1];
			generator[i][1] = new int[4];
		}
		for (int i = 0; i < 6; ++i) {
			for (int j = 0; j < 4; ++j) {
				generator[i][1][j] = j;
			}
			for (int j = 0; j < size - 1; ++j) {
				generator[i][0][j] = i * 4 + j * 24;
			}
		}
		int[] move = Puzzle.generatorScramble(length, generator);
		for (int i = 0; i < length; ++i) {
			for (int j = 0; j <= move[i] % 4; ++j) {
				int face = (move[i] % 24) / 4;
				int layer = move[i] / 24 + 1;
				if (layer > size / 2) {
					layer = size - layer;
					++face;
				}
				twist(face, move[i] / 24 + 1, true, true);
			}
		}
		return this;
	}

	@Override
	public int turn(int key) {
		switch (key) {
		case KeyEvent.VK_SPACE:
			return sReset;
		case KeyEvent.VK_SEMICOLON:
			return rU;
		case KeyEvent.VK_COMMA:
			return Uw;
		case KeyEvent.VK_SLASH:
			return DRw4;
		case KeyEvent.VK_3:
			return sLo;
		case KeyEvent.VK_4:
			return sLi;
		case KeyEvent.VK_7:
			return sRi;
		case KeyEvent.VK_8:
			return sRo;
		case KeyEvent.VK_A:
			return rU4;
		case KeyEvent.VK_B:
			return rL;
		case KeyEvent.VK_C:
			return Uw4;
		case KeyEvent.VK_D:
			return L;
		case KeyEvent.VK_E:
			return L4;
		case KeyEvent.VK_F:
			return U4;
		case KeyEvent.VK_G:
			return F4;
		case KeyEvent.VK_H:
			return F;
		case KeyEvent.VK_I:
			return R;
		case KeyEvent.VK_J:
			return U;
		case KeyEvent.VK_K:
			return R4;
		case KeyEvent.VK_L:
			return DR4;
		case KeyEvent.VK_M:
			return Rw4;
		case KeyEvent.VK_N:
			return rR4;
		case KeyEvent.VK_O:
			return BR4;
		case KeyEvent.VK_P:
			return rF;
		case KeyEvent.VK_Q:
			return rF4;
		case KeyEvent.VK_R:
			return Lw4;
		case KeyEvent.VK_S:
			return DR;
		case KeyEvent.VK_T:
			return rL4;
		case KeyEvent.VK_U:
			return Rw;
		case KeyEvent.VK_V:
			return Lw;
		case KeyEvent.VK_W:
			return BR;
		case KeyEvent.VK_Y:
			return rR;
		case KeyEvent.VK_Z:
			return DRw;
		default:
			return -1;
		}
	}

	@Override
	public boolean isTwistLayer(int layer) {
		return layer % 14 < 8 && layer != 84;
	}

	@Override
	public boolean isShift(int layer) {
		return layer % 14 > 9 || layer == 84;
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
			if (shiftU < size / 2) {
				++shiftU;
			}
			break;
		case sUo:
			if (shiftU > 1) {
				--shiftU;
			}
		case sDi:
			if (shiftD < size / 2) {
				++shiftD;
			}
			break;
		case sDo:
			if (shiftD > 1) {
				--shiftD;
			}
		case sFi:
			if (shiftF < size / 2) {
				++shiftF;
			}
			break;
		case sFo:
			if (shiftF > 1) {
				--shiftF;
			}
		case sBi:
			if (shiftB < size / 2) {
				++shiftB;
			}
			break;
		case sBo:
			if (shiftB > 1) {
				--shiftB;
			}
		case sRi:
			if (shiftR < size / 2) {
				++shiftR;
			}
			break;
		case sRo:
			if (shiftR > 1) {
				--shiftR;
			}
		case sDBLi:
			if (shiftDBL < size / 2) {
				++shiftDBL;
			}
			break;
		case sDBLo:
			if (shiftDBL > 1) {
				--shiftDBL;
			}
		case sBRi:
			if (shiftBR < size / 2) {
				++shiftBR;
			}
			break;
		case sBRo:
			if (shiftBR > 1) {
				--shiftBR;
			}
		case sDLi:
			if (shiftDL < size / 2) {
				++shiftDL;
			}
			break;
		case sDLo:
			if (shiftDL > 1) {
				--shiftDL;
			}
		case sBLi:
			if (shiftBL < size / 2) {
				++shiftBL;
			}
			break;
		case sBLo:
			if (shiftBL > 1) {
				--shiftBL;
			}
		case sDRi:
			if (shiftDR < size / 2) {
				++shiftDR;
			}
			break;
		case sDRo:
			if (shiftDR > 1) {
				--shiftDR;
			}
		case sLi:
			if (shiftL < size / 2) {
				++shiftL;
			}
			break;
		case sLo:
			if (shiftL > 1) {
				--shiftL;
			}
		case sDBRi:
			if (shiftDBR < size / 2) {
				++shiftDBR;
			}
			break;
		case sDBRo:
			if (shiftDBR > 1) {
				--shiftDBR;
			}
		case sReset:
			shiftU = shiftF = shiftR = shiftBR = shiftBL = shiftL = 1;
			shiftD = shiftB = shiftDBL = shiftDL = shiftDR = shiftDBR = 1;
			break;
		}
	}

	@Override
	public Megaminx twist(int layer, boolean scramble) {
		switch (layer) {
		case U:
			twist(0, shiftU, true, scramble);
			break;
		case U4:
			twist(0, shiftU, false, scramble);
			break;
		case D:
			twist(1, shiftD, true, scramble);
			break;
		case D4:
			twist(1, shiftD, false, scramble);
			break;
		case Uw:
			twist(0, shiftU + 1, true, scramble);
			break;
		case Uw4:
			twist(0, shiftU + 1, false, scramble);
			break;
		case Dw:
			twist(1, shiftD + 1, true, scramble);
			break;
		case Dw4:
			twist(1, shiftD + 1, false, scramble);
			break;
		case rU:
			twist(12, 0, true, scramble);
			break;
		case rU4:
			twist(12, 0, false, scramble);
			break;
		case F:
			twist(2, shiftF, true, scramble);
			break;
		case F4:
			twist(2, shiftF, false, scramble);
			break;
		case B:
			twist(3, shiftB, true, scramble);
			break;
		case B4:
			twist(3, shiftB, false, scramble);
			break;
		case Fw:
			twist(2, shiftF + 1, true, scramble);
			break;
		case Fw4:
			twist(2, shiftF + 1, false, scramble);
			break;
		case Bw:
			twist(3, shiftB + 1, true, scramble);
			break;
		case Bw4:
			twist(3, shiftB + 1, false, scramble);
			break;
		case rF:
			twist(13, 0, true, scramble);
			break;
		case rF4:
			twist(13, 0, false, scramble);
			break;
		case R:
			twist(4, shiftR, true, scramble);
			break;
		case R4:
			twist(4, shiftR, false, scramble);
			break;
		case DBL:
			twist(5, shiftDBL, true, scramble);
			break;
		case DBL4:
			twist(5, shiftDBL, false, scramble);
			break;
		case Rw:
			twist(4, shiftR + 1, true, scramble);
			break;
		case Rw4:
			twist(4, shiftR + 1, false, scramble);
			break;
		case DBLw:
			twist(5, shiftDBL + 1, true, scramble);
			break;
		case DBLw4:
			twist(5, shiftDBL + 1, false, scramble);
			break;
		case rR:
			twist(14, 0, true, scramble);
			break;
		case rR4:
			twist(14, 0, false, scramble);
			break;
		case BR:
			twist(6, shiftBR, true, scramble);
			break;
		case BR4:
			twist(6, shiftBR, false, scramble);
			break;
		case DL:
			twist(7, shiftDL, true, scramble);
			break;
		case DL4:
			twist(7, shiftDL, false, scramble);
			break;
		case BRw:
			twist(6, shiftBR + 1, true, scramble);
			break;
		case BRw4:
			twist(6, shiftBR + 1, false, scramble);
			break;
		case DLw:
			twist(7, shiftDL + 1, true, scramble);
			break;
		case DLw4:
			twist(7, shiftDL + 1, false, scramble);
			break;
		case rBR:
			twist(15, 0, true, scramble);
			break;
		case rBR4:
			twist(15, 0, false, scramble);
			break;
		case BL:
			twist(8, shiftBL, true, scramble);
			break;
		case BL4:
			twist(8, shiftBL, false, scramble);
			break;
		case DR:
			twist(9, shiftDR, true, scramble);
			break;
		case DR4:
			twist(9, shiftDR, false, scramble);
			break;
		case BLw:
			twist(8, shiftBL + 1, true, scramble);
			break;
		case BLw4:
			twist(8, shiftBL + 1, false, scramble);
			break;
		case DRw:
			twist(9, shiftDR + 1, true, scramble);
			break;
		case DRw4:
			twist(9, shiftDR + 1, false, scramble);
			break;
		case rBL:
			twist(16, 0, true, scramble);
			break;
		case rBL4:
			twist(16, 0, false, scramble);
			break;
		case L:
			twist(10, shiftL, true, scramble);
			break;
		case L4:
			twist(10, shiftL, false, scramble);
			break;
		case DBR:
			twist(11, shiftDBR, true, scramble);
			break;
		case DBR4:
			twist(11, shiftDBR, false, scramble);
			break;
		case Lw:
			twist(10, shiftL + 1, true, scramble);
			break;
		case Lw4:
			twist(10, shiftL + 1, false, scramble);
			break;
		case DBRw:
			twist(11, shiftDBR + 1, true, scramble);
			break;
		case DBRw4:
			twist(11, shiftDBR + 1, false, scramble);
			break;
		case rL:
			twist(17, 0, true, scramble);
			break;
		case rL4:
			twist(17, 0, false, scramble);
			break;
		}
		return this;
	}

	public void twist(int face, int layer, boolean dir, boolean scramble) {
		if (layer > size / 2) {
			--layer;
		}
		double distance = (1 + (1 - 4.0 * layer / (2 * size - 1)) * phi) * rphi;
		if (scramble) {
			double angle = 2 * Math.PI / 5;
			if (dir) {
				angle = -angle;
			}
			for (int i = 0; i < facelets; ++i) {
				Facelet pf = facelet_list[i];
				if (face < 12) {
					Vertex axis = face_axis[face];
					if (pf.center.innerProduct(axis) > distance) {
						pf.rotate(axis, angle);
					}
				} else {
					pf.rotate(face_axis[2 * (face - 12)], angle);
				}
			}
		} else {
			double angle = Math.PI / 10;
			if (dir) {
				angle = -angle;
			}
			Vertex axis = face_axis[face < 12 ? face : 2 * (face - 12)];
			boolean[] move = new boolean[facelets];
			if (face < 12) {
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
		draw(g, 0, 0, 1.15);
	}

}
