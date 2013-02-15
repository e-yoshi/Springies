package simulation;

/**
 * Creates a wall force that can repel/attract masses.
 * @author XuRui & Yoshi
 *
 */
public class SingleWallForce extends Force{
/**
 * Constructor of the wall forces.
 * @param angle
 * @param magnitude
 */
	private double myExponent;
	
	public SingleWallForce(double angle, double magnitude, boolean isOn) {
		super(angle, magnitude, isOn);
	}
/**
 * Calculates the forces for each wall(side) of the rectangular bounds.
 * @param canvas
 * @param wallForces
 * @param mass
 */
	public SingleWallForce(int direction, Double magnitude, Double exponent) {
			this.setDirection(direction);
			this.setMagnitude(magnitude);
			myExponent = exponent;
	}
	
	public double getExponent(){
		return myExponent;
	}
	
	
}