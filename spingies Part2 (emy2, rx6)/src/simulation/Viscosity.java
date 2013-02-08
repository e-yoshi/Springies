package simulation;
/**
 * Creates a viscosity force.
 * @author XuRui & Yoshi
 *
 */
public class Viscosity extends Force{
	/**
	 * Implicit constructor.
	 * @param angle
	 * @param magnitude
	 */
	public Viscosity(double angle, double magnitude) {
		super(angle, magnitude);
	}
	/**
	 * Creates a viscosity force and mass.
	 * @param viscosityScale
	 * @param mass
	 */
	public Viscosity(double viscosityScale, Mass mass) {
		this.setDirection((mass.getVelocity().getDirection() + 180) % 360);
		this.setMagnitude(viscosityScale * mass.getVelocity().getMagnitude());
	}
	

}
