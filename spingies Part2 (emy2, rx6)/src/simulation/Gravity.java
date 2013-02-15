package simulation;
/**
 * Creates a gravity force.
 * @author XuRui & Yoshi
 *
 */
public class Gravity extends Force {
	public static final double DEFAULT_GRAVITY_MAGNITUDE = 5.0;
	public static final double DEFAULT_GRAVITY_ANGLE = 90.0;
	public static final String GRAVITY_KEYWORD = "gravity";
	
	public Gravity(double angle, double magnitude, boolean isOn) {
		super(angle, magnitude, isOn);
	}


}
