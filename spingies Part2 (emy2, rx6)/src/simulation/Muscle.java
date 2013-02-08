package simulation;

import java.awt.Dimension;

public class Muscle extends Spring {

	private double myAmplitude;
	private double myRestLength;
	private double myFrequency;
	private double myMuscleAge;

	/**
	 * Constructs a muscle.
	 * @param start
	 * @param end
	 * @param length
	 * @param kVal
	 * @param amplitude
	 */
	public Muscle(Mass start, Mass end, double length, double kVal, double amplitude){
		super(start, end, length, kVal);
		myAmplitude = amplitude;
		myRestLength = length;
		myMuscleAge = 0;
		// The standard oscillation frequency was set here.
		myFrequency = 2;
	}

	
	public void update(double elapsedTime, Dimension bounds) {
		setLength(myRestLength + 2 * myAmplitude
				* Math.sin(2 * Math.PI * myFrequency * myMuscleAge));
		// System.out.printf("restLength is %f\n", myRestLength);
		moveMassesAlongOscillation();
		myMuscleAge += elapsedTime;
	}
}