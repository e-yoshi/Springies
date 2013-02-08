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

	private Canvas myCanvas;
	//gravity information
	private double gravityMagnitude;
	private double gravityAngle;
	//viscosity information
	private double viscosityScale;
	//centermass information
	private double fieldMag;
	private double fieldOrder;
	//wallforces information
	private HashMap<Vector, Double> wallForcesMap = new HashMap<Vector, Double>();

	private ArrayList<Vector> myForces = new ArrayList<Vector>();

	public Environment(Canvas canvas) {
		myCanvas = canvas;
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
		myForces.clear();
		myForces.add(new Gravity(gravityAngle, gravityMagnitude));
		myForces.add(new Viscosity(viscosityScale, mass));
		myForces.add(new Wallforce(myCanvas, wallForcesMap, mass));
		myForces.add(new CenterOfMassForce(fieldMag, fieldOrder, myCanvas, mass));
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

}
