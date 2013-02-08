package simulation;

import util.Vector;

public abstract class Force extends Vector{

	public Force(double angle, double magnitude){
		super(angle, magnitude);
	}
	/**
	 * Creates an empty constructor.
	 */
	public Force() {
	}


}
