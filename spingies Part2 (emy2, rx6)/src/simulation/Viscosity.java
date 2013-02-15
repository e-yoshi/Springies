package simulation;
/**
 * Creates a viscosity force.
 * @author XuRui & Yoshi
 *
 */
public class Viscosity extends Force{

	public static final double DEFAULT_VISCOSITY_SCALE = 0.01;
	public static final String VISCOSITY_KEYWORD = "viscosity";

	
	/**
	 * Implicit constructor.
	 * @param angle
	 * @param magnitude
	 */
	
	public Viscosity(double angle, double magnitude, boolean isOn) {
		super(angle, magnitude, isOn);
	}
	/**
	 * Creates a viscosity force and mass.
	 * @param viscosityScale
	 * @param mass
	 * @param isOn
	 */
	public Viscosity(double viscosityScale, Mass mass, boolean isOn) {
		this.setDirection((mass.getVelocity().getDirection() + 180) % 360);
		this.setMagnitude(viscosityScale * mass.getVelocity().getMagnitude());
		if (!isOn){
			this.reset();
		}
	}
	

}
