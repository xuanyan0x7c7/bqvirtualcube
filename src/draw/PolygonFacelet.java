package draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

public class PolygonFacelet extends Facelet {

	public int n;
	public Vertex[] vertex;

	public PolygonFacelet(int n) {
		this(n, Color.BLACK, Color.BLACK);
	}

	public PolygonFacelet(int n, Color color_bound, Color color_inside) {
		this.n = n;
		vertex = new Vertex[n];
		this.color_bound = color_bound;
		this.color_inside = color_inside;
	}

	public PolygonFacelet(PolygonFacelet p) {
		this.n = p.n;
		this.color_bound = p.color_bound;
		this.color_inside = p.color_inside;
		this.center = new Vertex(p.center);
		this.vertex = new Vertex[p.n];
		for (int i = 0; i < p.n; ++i) {
			this.vertex[i] = new Vertex(p.vertex[i]);
		}
	}

	@Override
	public void rotate(Vertex axis, double angle) {
		center.rotate(axis, angle);
		for (int i = 0; i < n; ++i) {
			vertex[i].rotate(axis, angle);
		}
	}

	@Override
	public void draw(Graphics2D g, Vertex eye, Vertex axis_x, Vertex axis_y,
			double dx, double dy, double ratio) {
		double[][] vertex2d = new double[n][2];
		for (int i = 0; i < n; ++i) {
			vertex2d[i] = vertex[i].project(eye, axis_x, axis_y);
			vertex2d[i][0] = vertex2d[i][0] * ratio + dx;
			vertex2d[i][1] = vertex2d[i][1] * ratio + dy;
		}
		GeneralPath path = new GeneralPath();
		path.moveTo(vertex2d[n - 1][0], vertex2d[n - 1][1]);
		for (int i = 0; i < n; ++i) {
			path.lineTo(vertex2d[i][0], vertex2d[i][1]);
		}
		Color current_color = g.getColor();
		g.setColor(color_inside);
		g.fill(path);
		g.setColor(color_bound);
		g.draw(path);
		g.setColor(current_color);
	}
}
