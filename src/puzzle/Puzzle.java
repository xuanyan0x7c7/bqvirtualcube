package puzzle;

import java.awt.Graphics2D;
import java.util.Random;

import utility.Util;

import model.MyPanel;

import draw.Facelet;
import draw.Vertex;

public abstract class Puzzle {

	protected String name = "Puzzle";
	protected int size;
	protected int facelets;
	protected Facelet[] facelet_list = null;
	protected MyPanel panel;
	protected Vertex eye, axis_x, axis_y;
	protected double facelet_ratio;
	protected int default_size = 3;
	protected int minimum_size = 2;
	protected int maximum_size = 0x7fffffff;

	public Puzzle(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEye(Vertex eye) {
		this.eye = new Vertex(eye);
		if (eye.x == 0.0 && eye.y == 0.0) {
			axis_x = new Vertex(0.0, 1.0, 0.0);
			if (eye.z > 0.0) {
				axis_y = new Vertex(-1.0, 0.0, 0.0);
			} else {
				axis_y = new Vertex(1.0, 0.0, 0.0);
			}
		} else {
			axis_x = new Vertex(-eye.y, eye.x, 0.0).normalize();
			axis_y = eye.cross(axis_x).normalize();
		}
	}

	public void setEye(Vertex eye, Vertex axis_x, Vertex axis_y) {
		this.eye = new Vertex(eye);
		this.axis_x = new Vertex(axis_x).normalize();
		this.axis_y = new Vertex(axis_y).normalize();
	}

	public void zoom(double zoom) {
		eye.zoom(zoom);
	}

	public void rotateEye(double x, double y) {
		eye.rotate(axis_y, x * Math.PI / 2);
		axis_x.rotate(axis_y, x * Math.PI / 2);
		eye.rotate(axis_x, -y * Math.PI / 2);
		axis_y.rotate(axis_x, -y * Math.PI / 2);
		return;
	}

	public int getDefaultSize() {
		return default_size;
	}

	public int getSmallerSize(int size) {
		return size > minimum_size ? size - 1 : size;
	}

	public int getBiggerSize(int size) {
		return size < maximum_size ? size + 1 : size;
	}

	public abstract Puzzle reset();

	public abstract boolean isSolved();

	public abstract Puzzle scramble();

	public Puzzle preScramble() {
		return this;
	}

	public abstract int turn(int key);

	public abstract boolean isTwistLayer(int layer);

	public abstract boolean isShift(int layer);

	public abstract boolean isEntirelyTwist(int layer);

	public abstract boolean isParallel(int l1, int l2);

	public abstract void shift(int layer);

	public abstract Puzzle twist(int layer, boolean scramble);

	public abstract void draw(Graphics2D g);

	protected static int[] generatorScramble(int length, int[][][] generator) {
		int[][] index = new int[1000][3];
		int totalMoves = 0;
		for (int i = 0; i < generator.length; i++) {
			for (int j = 0; j < generator[i][0].length; j++) {
				for (int k = 0; k < generator[i][1].length; k++) {
					int t = totalMoves + j * generator[i][1].length + k;
					index[t][0] = i;
					index[t][1] = j;
					index[t][2] = k;
				}
			}
			totalMoves += generator[i][0].length * generator[i][1].length;
		}

		int move = 0;
		int[] sequence = new int[length];
		Random rand = new Random();
		int lastAxis = -1;
		boolean[] shiftMoved = null;
		for (int i = 0; i < length; ++i) {
			int axis, shift, count;
			do {
				int code = rand.nextInt(totalMoves);
				axis = index[code][0];
				shift = index[code][1];
				count = index[code][2];
			} while (axis == lastAxis && shiftMoved[shift]);
			sequence[move++] = generator[axis][0][shift]
					+ generator[axis][1][count];
			if (axis == lastAxis) {
				shiftMoved[shift] = true;
			} else {
				shiftMoved = new boolean[generator[axis][0].length];
				for (int j = 0; j < generator[axis][0].length; ++j) {
					shiftMoved[j] = false;
				}
				shiftMoved[shift] = true;
				lastAxis = axis;
			}
		}
		return sequence;
	}

	protected void draw(Graphics2D g, double dx, double dy, double zoom_ratio) {
		double eye_dist = eye.modulus();
		double[] distance = new double[facelets];
		for (int i = 0; i < facelets; ++i) {
			distance[i] = Vertex.sqrDist(eye, facelet_list[i].center);
		}
		int[] seq = new int[facelets];
		for (int i = 0; i < facelets; ++i) {
			seq[i] = i;
		}
		Util.sort(seq, distance);
		for (int i = facelets; --i >= 0;) {
			facelet_list[seq[i]].draw(g, eye, axis_x, axis_y, dx, dy,
					zoom_ratio / eye_dist);
		}
	}

}
