package Tanky;

public class Vector2D {

	public double x;
	public double y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return Double.toString(x) + ", " + Double.toString(y);
	}

	public Vector2D scalarMultiply(double scalar) {
		return new Vector2D(this.x * scalar, this.y * scalar);
	}

	public Vector2D addVector(Vector2D otherVector) {
		return new Vector2D(this.x + otherVector.x, this.y + otherVector.y);
	}

	public double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public Vector2D unit() {
		return new Vector2D(this.x / this.magnitude(), this.y / this.magnitude());
	}
	
	public static double dot(Vector2D vector1, Vector2D vector2) {
		return vector1.x * vector2.x + vector1.y * vector2.y;
	}
}