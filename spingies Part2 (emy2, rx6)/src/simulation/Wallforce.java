package simulation;

import java.util.HashMap;
import java.util.Map.Entry;

import util.Vector;
import view.Canvas;
/**
 * Creates a wall force that can repel/attract masses.
 * @author XuRui & Yoshi
 *
 */
public class Wallforce extends Force{
/**
 * Constructor of the wall forces.
 * @param angle
 * @param magnitude
 */
	public Wallforce(double angle, double magnitude) {
		super(angle, magnitude);
	}
/**
 * Calculates the forces for each wall(side) of the rectangular bounds.
 * @param canvas
 * @param wallForces
 * @param mass
 */
	public Wallforce(Canvas canvas, HashMap<Vector, Double> wallForces, Mass mass) {
		Vector wallForce = new Vector();
		for (Entry<Vector, Double>entry : wallForces.entrySet()) {
			double wallExponent = entry.getValue();
			double wallMagnitude = entry.getKey().getMagnitude();
			double distanceFromWall;

			switch ((int) (entry.getKey().getDirection())) {
			case DOWN_DIRECTION:
				distanceFromWall = mass.getY();
				break;
			case LEFT_DIRECTION:
				distanceFromWall = canvas.getWidth() - mass.getX();
				break;
			case UP_DIRECTION:
				distanceFromWall = canvas.getHeight() - mass.getY();
				break;
			case RIGHT_DIRECTION:
				distanceFromWall = mass.getX();
				break;
			default:
				distanceFromWall = Double.POSITIVE_INFINITY;
			}
			//Condition to prevent wall forces of infinite magnitude.
			if (distanceFromWall > 1){
				wallForce.sum(new Vector(entry.getKey().getDirection(), wallMagnitude
											* (Math.pow(distanceFromWall, -wallExponent))));
				this.setDirection(wallForce.getDirection());
				this.setMagnitude(wallForce.getMagnitude());
			}
			else
				this.reset();
		}
	}
}
