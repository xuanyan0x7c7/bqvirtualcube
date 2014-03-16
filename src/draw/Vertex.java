package draw;

public class Vertex {

	public double x, y, z;

	public Vertex() {
		x = y = z = 0;
	}

	public Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vertex(Vertex p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public Vertex move(Vertex p) {
		this.x += p.x;
		this.y += p.y;
		this.z += p.z;
		return this;
	}

	public Vertex zoom(double ratio) {
		x *= ratio;
		y *= ratio;
		z *= ratio;
		return this;
	}

	public Vertex zoom(Vertex center, double ratio) {
		this.x = center.x + (this.x - center.x) * ratio;
		this.y = center.y + (this.y - center.y) * ratio;
		this.z = center.z + (this.z - center.z) * ratio;
		return this;
	}

	public strictfp Vertex rotate(Vertex axis, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double a = axis.x;
		double b = axis.y;
		double c = axis.z;
		double x = this.x * (a * a + (1 - a * a) * cos) + this.y
				* (a * b * (1 - cos) - c * sin) + this.z
				* (a * c * (1 - cos) + b * sin);
		double y = this.y * (b * b + (1 - b * b) * cos) + this.z
				* (b * c * (1 - cos) - a * sin) + this.x
				* (b * a * (1 - cos) + c * sin);
		double z = this.z * (c * c + (1 - c * c) * cos) + this.x
				* (c * a * (1 - cos) - b * sin) + this.y
				* (c * b * (1 - cos) + a * sin);
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public double innerProduct(Vertex p) {
		return this.x * p.x + this.y * p.y + this.z * p.z;
	}

	public Vertex cross(Vertex p) {
		return new Vertex(this.y * p.z - this.z * p.y, this.z * p.x - this.x
				* p.z, this.x * p.y - this.y * p.x);
	}

	public double[] project(Vertex eye, Vertex axis_x, Vertex axis_y) {
		double k1 = eye.innerProduct(eye);
		double k2 = this.innerProduct(eye);
		double k = k1 / (k1 - k2);
		return new double[] { k * this.innerProduct(axis_x),
				k * this.innerProduct(axis_y) };
	}

	public static double distance(Vertex p1, Vertex p2) {
		return Math.sqrt(Vertex.distance(p1, p2));
	}

	public static double sqrDist(Vertex p1, Vertex p2) {
		double dx = p1.x - p2.x;
		double dy = p1.y - p2.y;
		double dz = p1.z - p2.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public double modulus() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public Vertex normalize() {
		double length = this.modulus();
		this.x /= length;
		this.y /= length;
		this.z /= length;
		return this;
	}

	public Vertex getVector(Vertex p) {
		return new Vertex(p.x - this.x, p.y - this.y, p.z - this.z);
	}

}
