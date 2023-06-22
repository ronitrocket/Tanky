package Tanky;

public class Vector2D {

	// Instance Variables
	public double x;
	public double y;


	/**
	 * Default constructor.
	 * 
	 * Creates a new Vector representation
	 * 
	 * @param x		The value of the x coordinate of the vector
	 * @param y		The value of the x coordinate of the vector
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Multiply the vector by a number
	 * 
	 * @param scalar		The number we are multiplying the vector by
	 * @return newVector	The new vector
	 */
	public Vector2D scalarMultiply(double scalar) {
		return new Vector2D(this.x * scalar, this.y * scalar);
	}

	/**
	 * Add the vector with another vector
	 * 
	 * @param otherVector		The vector we are adding
	 * @return newVector	The new vector
	 */
	public Vector2D addVector(Vector2D otherVector) {
		return new Vector2D(this.x + otherVector.x, this.y + otherVector.y);
	}
	
	/**
	 * Subtract the vector by another vector
	 * 
	 * @param otherVector		The vector we are subtracting by
	 * @return newVector	The new vector
	 */
	public Vector2D subtractVector(Vector2D otherVector) {
		return new Vector2D(this.x - otherVector.x, this.y - otherVector.y);
	}

	/**
	 * Returns the magnitude of the vector
	 */
	public double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	/**
	 * Returns the unit vector of this vector
	 */
	public Vector2D unit() {
		return new Vector2D(this.x / this.magnitude(), this.y / this.magnitude());
	}
	
	/**
	 * Dot product of two vectors
	 * 
	 * @param vector1		The first vector
	 * @param vector2		The second vector
	 * @return dotProduct	The dot product of the vectors
	 */
	public static double dot(Vector2D vector1, Vector2D vector2) {
		return vector1.x * vector2.x + vector1.y * vector2.y;
	}
	
	/**
	 * Returns the distance between two vectors
	 * @param otherVector	The other vector
	 */
	public double dist(Vector2D otherVector) {
		return Math.sqrt(Math.pow(this.x - otherVector.x, 2) + Math.pow(this.y - otherVector.y, 2));
	}
	
	/**
	 * Returns the Vector2D as a string.
	 * 
	 */
	@Override
	public String toString() {
		return Double.toString(x) + ", " + Double.toString(y);
	}
}