package simulation;

import util.Vector;

public class Force extends Vector{
	
	public static final double DEFAULT_ANGLE = 90.0;
	public static final double DEFAULT_MAGNITUDE = 1.0;
	
	private boolean turnOn = true;
	
	public Force(double angle, double magnitude, boolean isOn){
		super(angle, magnitude);		
		turnOn = isOn;
		if (!isOn){
			this.reset();
		}
	}
	/**
	 * Creates an empty constructor.
	 */
	public Force() {
	}
	
	public boolean isTurnedOn(){
		return turnOn;
	}
	
	public void toggleSwitch(){
		turnOn = !turnOn;
	}
	
	public void setSwitch(boolean status){
		turnOn = status;
	}


}
