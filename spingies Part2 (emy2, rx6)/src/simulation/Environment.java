package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import util.Vector;
import view.Canvas;

/**
 * Get information from environment data file,
 * loads forces and sets field force.
 * @author XuRui & Yoshi
 *
 */
public class Environment {
	// data file keywords
	private static final String GRAVITY_KEYWORD = "gravity";
	private static final String VISCOSITY_KEYWORD = "viscosity";
	private static final String CM_KEYWORD = "centermass";
	private static final String WALL_KEYWORD = "wall";
	//default values
	private static final double DEFAULT_GRAVITY_MAGNITUDE=10.0;
	private static final double DEFAULT_GRAVITY_ANGLE=Vector.DOWN_DIRECTION;
	private static final double DEFAULT_VISCOSITY_SCALE = 0.01;
	private static final double DEFAULT_FIELDORDER = 1.0;
	private static final double DEFAULT_FIELDMAGNITUDE = 1.0;
	private static final double DEFAULT_WALLFORCE_EXPONENT = 2.0;
	private static final double DEFAULT_WALLFORCE_MAGNITUDE = 10.0;
	private static final Vector DEFAULT_WALLFORCE_1 = new Vector(Vector.DOWN_DIRECTION, DEFAULT_WALLFORCE_MAGNITUDE);
	private static final Vector DEFAULT_WALLFORCE_2 = new Vector(Vector.LEFT_DIRECTION, DEFAULT_WALLFORCE_MAGNITUDE);
	private static final Vector DEFAULT_WALLFORCE_3 = new Vector(Vector.UP_DIRECTION, DEFAULT_WALLFORCE_MAGNITUDE);
	private static final Vector DEFAULT_WALLFORCE_4 = new Vector(Vector.RIGHT_DIRECTION, DEFAULT_WALLFORCE_MAGNITUDE);
	
	//Game State
	private boolean gravityOn = true;
	private boolean viscosityOn = true;
	private boolean centerOfMassOn = true;
	
	private Canvas myCanvas;
	
	//gravity information
	private double gravityMagnitude=DEFAULT_GRAVITY_MAGNITUDE;
	private double gravityAngle=DEFAULT_GRAVITY_ANGLE;
	//viscosity information
	private double viscosityScale = DEFAULT_VISCOSITY_SCALE;
	//centermass information
	private double fieldMag = DEFAULT_FIELDMAGNITUDE;
	private double fieldOrder = DEFAULT_FIELDORDER;
	//wallforces information
	private HashMap<Vector, Double> wallForcesMap = new HashMap<Vector, Double>();

	private ArrayList<Vector> myForces = new ArrayList<Vector>();

	public Environment(Canvas canvas) {
		myCanvas = canvas;
		wallForcesMap.put(DEFAULT_WALLFORCE_1, DEFAULT_WALLFORCE_EXPONENT);
		wallForcesMap.put(DEFAULT_WALLFORCE_2, DEFAULT_WALLFORCE_EXPONENT);
		wallForcesMap.put(DEFAULT_WALLFORCE_3, DEFAULT_WALLFORCE_EXPONENT);
		wallForcesMap.put(DEFAULT_WALLFORCE_4, DEFAULT_WALLFORCE_EXPONENT);
	}

	/**
	 * Loads the variables required to create the environment.
	 */
	public void loadEnv(Model model, File modelFile) {
		try {
			Scanner input = new Scanner(modelFile);
			while (input.hasNext()) {
				Scanner line = new Scanner(input.nextLine());
				if (line.hasNext()) {
					String type = line.next();
					if (GRAVITY_KEYWORD.equals(type)) {
						gravityAngle = line.nextDouble();
						gravityMagnitude = line.nextDouble();
					} else if (VISCOSITY_KEYWORD.equals(type)) {
						viscosityScale = line.nextDouble();

					} else if (CM_KEYWORD.equals(type)) {
						fieldMag = line.nextDouble();
						fieldOrder = line.nextDouble();
					} else if (WALL_KEYWORD.equals(type)) {
						wallCommand(line);
					}
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getAllForces calculates the resultant force vector of a certain mass.
	 * @param mass
	 * @return totalForce
	 */
	public Vector getAllForces(Mass mass) {
		addAllForces(mass);
		Vector totalForce = new Vector();
		for (Vector force : myForces) {
			totalForce.sum(force);
		}
		return totalForce;
	}

	/**
	 * Adds all forces to myForces, use this function to addition of additional forces
	 * @param mass
	 */
	private void addAllForces(Mass mass) {
		int key = myCanvas.getLastKeyPressed();
		
		myForces.clear();
		if (key == Canvas.TOGGLE_GRAVITY){
		    gravityOn = toggle(gravityOn);
		}
		if (key == Canvas.TOGGLE_VISCOSITY){
		    viscosityOn = toggle(viscosityOn);
		}
		if (key == Canvas.TOGGLE_CM){
		    centerOfMassOn = toggle(centerOfMassOn);
		}
		if (gravityOn){
		    myForces.add(new Gravity(gravityAngle, gravityMagnitude));
		}
		if (viscosityOn){
		    myForces.add(new Viscosity(viscosityScale, mass));
		}
		if (centerOfMassOn){
		    myForces.add(new CenterOfMassForce(fieldMag, fieldOrder, myCanvas, mass));

		}
		myForces.add(new Wallforce(myCanvas, wallForcesMap, mass));
		myCanvas.resetLastKeyPressed();

	}

	/**
	 * Reads wall forces information for data file and puts them into wallForcesMap
	 * @param line
	 */
	private void wallCommand(Scanner line) {
		int wallSide = line.nextInt();
		double magnitude = line.nextDouble();
		double exponent = line.nextDouble();

		switch (wallSide) {

		case 1:
			wallForcesMap.put(new Vector(Vector.DOWN_DIRECTION, magnitude), exponent);
			break;
		case 2:
			wallForcesMap.put(new Vector(Vector.LEFT_DIRECTION, magnitude), exponent);
			break;
		case 3:
			wallForcesMap.put(new Vector(Vector.UP_DIRECTION, magnitude), exponent);
			break;
		case 4:
			wallForcesMap.put(new Vector(Vector.RIGHT_DIRECTION, magnitude), exponent);
			break;
		default:
			break;
		}
	}
	
	private boolean toggle(boolean bool){
	    if(bool == true)
		return false;
	    else
		return true;
	}

}
