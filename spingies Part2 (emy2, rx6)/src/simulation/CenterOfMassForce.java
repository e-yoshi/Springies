package simulation;

import util.Location;
import util.Vector;
import view.Canvas;

public class CenterOfMassForce extends Force {

	public static final double DEFAULT_FIELDORDER = 1.0;
	public static final double DEFAULT_FIELDMAGNITUDE = 1.0;
	public static final String CM_KEYWORD = "centermass";

	
	
	private Model mySim;
	/**
	 * Constructs the center of mass forces.
	 * @param angle
	 * @param magnitude
	 */
	public CenterOfMassForce(double angle, double magnitude, boolean isOn) {
		super(angle, magnitude, isOn);
	}

	/**
	 * Set center of mass force.
	 * @param fieldMag
	 * @param fieldOrder
	 * @param myCanvas
	 * @param mass
	 * @param isOn
	 */
	public CenterOfMassForce(double fieldMag, double fieldOrder, Canvas myCanvas, Mass mass, boolean isOn) {
		Location centerMassLocation = getCMLocation(myCanvas);
		Vector distCenterOfMass = new Vector(mass.getCenter(), centerMassLocation);
		Vector centerMassForce = new Vector(distCenterOfMass.getDirection(),Math.abs(fieldMag)
				* Math.pow(distCenterOfMass.getMagnitude(), -fieldOrder));
		if (fieldMag < 0) {
			centerMassForce.negate();
		}
		if(distCenterOfMass.getMagnitude()>1){
			this.setDirection(centerMassForce.getDirection());
			this.setMagnitude(centerMassForce.getMagnitude());
		}
		if (!isOn){
			this.reset();
		}
	}

	/**
	 * Calculates the position of the center of mass of all masses after they
	 * are loaded in mySimulation.
	 * 
	 * @param myCanvas
	 */
	private Location getCMLocation(Canvas myCanvas) {
		if (myCanvas.getModel() != null) {

			mySim = myCanvas.getModel();

			double xPos = 0;
			double yPos = 0;
			double totalMass = 0;

			// calculates the center of mass
			for (Mass m : mySim.getMasses()) {
				xPos += m.getX() * m.getMass();
				yPos += m.getY() * m.getMass();
				totalMass += m.getMass();
			}

			xPos = xPos / totalMass;
			yPos = yPos / totalMass;

			return new Location(xPos, yPos);

		} else {
			return new Location();
		}
	}

}
