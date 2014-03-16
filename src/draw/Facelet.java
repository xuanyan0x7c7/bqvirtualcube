package draw;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Facelet {

	public Color color_bound, color_inside;
	public Vertex center;

	public abstract void rotate(Vertex axis, double angle);

	public void setColor(Color color_bound, Color color_inside) {
		this.color_bound = color_bound;
		this.color_inside = color_inside;
	}

	public abstract void draw(Graphics2D g, Vertex eye, Vertex axis_x,
			Vertex axis_y, double dx, double dy, double ratio);

}
