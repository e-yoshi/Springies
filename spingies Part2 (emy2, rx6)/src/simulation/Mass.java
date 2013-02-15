package simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;

/**
 * Mass class.
 * @author Robert C. Duvall
 */
public class Mass extends Sprite {
	// reasonable default values
	public static final Dimension DEFAULT_SIZE = new Dimension(16, 16);
	public static final Pixmap DEFUALT_IMAGE = new Pixmap("mass.gif");
	private double myMass;
	private Vector myAcceleration;
	private int myID;	

	/**
	 * Mass constructor.
	 */
	public Mass(double x, double y, double mass, int id) {
		super(DEFUALT_IMAGE, new Location(x, y), DEFAULT_SIZE);
		myMass = mass;
		myAcceleration = new Vector();
		myID = id;
		setCenter(x, y);
	}

	/**
	 * Update the sprites.
	 */

	public void update (Environment env, double elapsedTime, Dimension bounds) {
		if (myMass > 0) {
			//In case of collision
			applyForce(getBounce(bounds));
			//Apply external forces from the surroundings
			applyForce(env.getAllForces(this));
     			// convert force back into Mover's velocity
			getVelocity().sum(myAcceleration);
			myAcceleration.reset();
			// move mass by velocity
			super.update(elapsedTime, bounds);
		}
		else{
			applyForce(new Vector());
			setVelocity(0, 0);
			myAcceleration.reset();
		}
	}

	/**
	 * Paint.
	 */
	@Override
	public void paint(Graphics2D pen){
		pen.setColor(Color.BLACK);
		pen.fillOval((int)getLeft(), (int)getTop(), (int)getWidth(), (int)getHeight());
	}

	/**
	 * Use the given force to change this mass's acceleration.
	 */
	public void applyForce (Vector force) {
		myAcceleration.sum(force);
	}

	/**
	 * Convenience method.
	 */
	public double distance (Mass other) {
		// this is a little awkward, so hide it
		return new Location(getX(), getY()).distance(new Location(other.getX(), other.getY()));
	}

	// check for move out of bounds
	private Vector getBounce (Dimension bounds) {
		final double IMPULSE_MAGNITUDE = 2;
		Vector impulse = new Vector();
		if (getLeft() <= 0) {
			impulse = new Vector(RIGHT_DIRECTION, IMPULSE_MAGNITUDE);
			this.setCenter(this.getWidth()/2, this.getCenter().getY());
		}
		else if (getRight() >= bounds.getWidth()) {
			impulse = new Vector(LEFT_DIRECTION, IMPULSE_MAGNITUDE);
			this.setCenter(bounds.getWidth()-this.getWidth()/2, getCenter().getY());
		}
		if (getTop() <= 0) {
			impulse = new Vector(DOWN_DIRECTION, IMPULSE_MAGNITUDE);
			this.setCenter(getCenter().getX(),this.getHeight()/2);
		}
		else if (getBottom() >= bounds.getHeight()) {
			impulse = new Vector(UP_DIRECTION, IMPULSE_MAGNITUDE);
			this.setCenter(getCenter().getX(),  bounds.getHeight()-this.getHeight()/2);
		}
		impulse.scale(getVelocity().getRelativeMagnitude(impulse));
		return impulse;
	}

	//used in Environment to calculate the center of mass
	public double getMass(){
		return myMass;
	}
	
	public int getID(){
	    return myID;
	}

	/**
	 * Creates an oscillatory movement in mass.
	 * @param increase
	 * @param angle
	 */
	public void moveMass(double increase, double angle){
		double x = increase * Math.cos(Math.toRadians(angle));
		double y = increase * Math.sin(Math.toRadians(angle));
		this.setCenter(getCenter().getX() + x, getCenter().getY() + y);
	}
}