package simulation;

import java.util.ArrayList;
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
 */	public static final double DEFAULT_WALLFORCE_EXPONENT = 2.0;
	public static final double DEFAULT_WALLFORCE_MAGNITUDE = 100.0;
	public static final String WALL_KEYWORD = "wall";

	
	
	public Wallforce(double angle, double magnitude, boolean isOn) {
		super(angle, magnitude, isOn);
	}
/**
 * Calculates the forces for each wall(side) of the rectangular bounds.
 * @param canvas
 * @param wallForces
 * @param mass
 */
	
	public Wallforce(Canvas canvas, ArrayList<SingleWallForce> wallForcesList, Mass mass) {
		Vector wallForce = new Vector();
		for (SingleWallForce oneWallForce : wallForcesList) {
			double wallDirection = oneWallForce.getDirection();
			double wallMagnitude = oneWallForce.getMagnitude();
			double distanceFromWall;

			switch ((int) (oneWallForce.getDirection())) {
			case DOWN_DIRECTION:
				distanceFromWall = mass.getY();
				break;
			case LEFT_DIRECTION:
				distanceFromWall = canvas.getSize().getWidth() - mass.getX();
				break;
			case UP_DIRECTION:
				distanceFromWall = canvas.getSize().getHeight() - mass.getY();
				break;
			case RIGHT_DIRECTION:
				distanceFromWall = mass.getX();
				break;
			default:
				distanceFromWall = Double.POSITIVE_INFINITY;
			}
			//Condition to prevent wall forces of infinite magnitude.
			if (distanceFromWall > 1 && oneWallForce.isTurnedOn()){
				wallForce.sum(new Vector(wallDirection, wallMagnitude
											* (Math.pow(distanceFromWall, -oneWallForce.getExponent()))));
				this.setDirection(wallForce.getDirection());
				this.setMagnitude(wallForce.getMagnitude());
			}
			else
				this.reset();
		}
	}
}
