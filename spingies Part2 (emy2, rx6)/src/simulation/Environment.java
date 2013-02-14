package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
	private static final double DEFAULT_GRAVITY_MAGNITUDE= 5.0;
	private static final double DEFAULT_GRAVITY_ANGLE=Vector.DOWN_DIRECTION;
	private static final double DEFAULT_VISCOSITY_SCALE = 0.01;
	private static final double DEFAULT_FIELDORDER = 1.0;
	private static final double DEFAULT_FIELDMAGNITUDE = 1.0;
	private static final double DEFAULT_WALLFORCE_EXPONENT = 2.0;
	private static final double DEFAULT_WALLFORCE_MAGNITUDE = 100.0;
	private static final SingleWallForce DEFAULT_WALLFORCE_1 = new SingleWallForce(Vector.DOWN_DIRECTION, DEFAULT_WALLFORCE_MAGNITUDE, DEFAULT_WALLFORCE_EXPONENT);
	private static final SingleWallForce DEFAULT_WALLFORCE_2 = new SingleWallForce(Vector.LEFT_DIRECTION, DEFAULT_WALLFORCE_MAGNITUDE, DEFAULT_WALLFORCE_EXPONENT);
	private static final SingleWallForce DEFAULT_WALLFORCE_3 = new SingleWallForce(Vector.UP_DIRECTION, DEFAULT_WALLFORCE_MAGNITUDE, DEFAULT_WALLFORCE_EXPONENT);
	private static final SingleWallForce DEFAULT_WALLFORCE_4 = new SingleWallForce(Vector.RIGHT_DIRECTION, DEFAULT_WALLFORCE_MAGNITUDE, DEFAULT_WALLFORCE_EXPONENT);
	
	//Game State
	private boolean gravityOn = true;
	private boolean viscosityOn = true;
	private boolean centerOfMassOn = true;
	private boolean wallOneOn = true;
	private boolean wallTwoOn = true;
	private boolean wallThreeOn = true;
	private boolean wallFourOn = true;
	
	private Canvas myCanvas;
	
	//gravity information
	private double gravityMagnitude=DEFAULT_GRAVITY_MAGNITUDE;
	private double gravityAngle=DEFAULT_GRAVITY_ANGLE;
	//viscosity information
	private double viscosityScale = DEFAULT_VISCOSITY_SCALE;
	//center of mass information
	private double fieldMag = DEFAULT_FIELDMAGNITUDE;
	private double fieldOrder = DEFAULT_FIELDORDER;
	//wall forces information
	private SingleWallForce wallForceTop = DEFAULT_WALLFORCE_1;
	private SingleWallForce wallForceRight = DEFAULT_WALLFORCE_2;
	private SingleWallForce wallForceBottom = DEFAULT_WALLFORCE_3;
<<<<<<< HEAD
	private SingleWallForce wallForce4 = DEFAULT_WALLFORCE_4;
=======
	private SingleWallForce wallForceLeft = DEFAULT_WALLFORCE_4;
>>>>>>> minor changes
	
	private ArrayList<SingleWallForce> wallForcesList = new ArrayList<SingleWallForce>();

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
		int key = myCanvas.getLastKeyPressed();
		
		myForces.clear();
		wallForcesList.clear();
		checkControls(key);
		
		if (gravityOn){
		    myForces.add(new Gravity(gravityAngle, gravityMagnitude));
		}
		if (viscosityOn){
		    myForces.add(new Viscosity(viscosityScale, mass));
		}
		if (centerOfMassOn){
		    myForces.add(new CenterOfMassForce(fieldMag, fieldOrder, myCanvas, mass));
		}
		
		if (wallOneOn){
			wallForcesList.add(wallForceTop);
		}
		if (wallTwoOn){
			wallForcesList.add(wallForceRight);
		}
		if (wallThreeOn){
			wallForcesList.add(wallForceBottom);
		}
		if (wallFourOn){
			wallForcesList.add(wallForceLeft);
		}
		
		myForces.add(new Wallforce(myCanvas, wallForcesList, mass));
		for (Vector force: myForces){
			//System.out.println(force);
		}
		myCanvas.resetLastKeyPressed();
	}
	
	private void checkControls(int key){
		if (key == Canvas.TOGGLE_GRAVITY){
		    gravityOn = toggle(gravityOn);
		}
		if (key == Canvas.TOGGLE_VISCOSITY){
		    viscosityOn = toggle(viscosityOn);
		}
		if (key == Canvas.TOGGLE_CM){
		    centerOfMassOn = toggle(centerOfMassOn);
		}
		
		if (key == Canvas.TOGGLE_WALLFORCE1){
			wallOneOn = toggle(wallOneOn);
		}
		if (key == Canvas.TOGGLE_WALLFORCE2){
			wallTwoOn = toggle(wallTwoOn);
		}
		if (key == Canvas.TOGGLE_WALLFORCE3){
			wallThreeOn = toggle(wallThreeOn);
		}
		if (key == Canvas.TOGGLE_WALLFORCE4){
			wallFourOn = toggle(wallFourOn);
		}
	}

	/**
<<<<<<< HEAD
	 * Reads wall forces information for data
=======
	 * Reads wall forces information for data 
>>>>>>> minor changes
	 * file and puts them into wallForcesMap.
	 * @param line
	 */
	private void wallCommand(Scanner line) {
		int wallSide = line.nextInt();
		double magnitude = line.nextDouble();
		double exponent = line.nextDouble();

		switch (wallSide) {

		case 1:
			wallForceTop = new SingleWallForce(Vector.DOWN_DIRECTION, magnitude, exponent);
			break;
		case 2:
			wallForceRight = new SingleWallForce(Vector.LEFT_DIRECTION, magnitude, exponent);
			break;
		case 3:
			wallForceBottom = new SingleWallForce(Vector.UP_DIRECTION, magnitude, exponent);
			break;
		case 4:
			wallForceLeft = new SingleWallForce(Vector.RIGHT_DIRECTION, magnitude, exponent);
			break;
		default:
			break;
		}
	}
	/**
	 * Toggle the force.
	 * @param bool
	 * @return
	 */
	private boolean toggle(boolean bool){
	    bool = !bool;
	    return bool;
	}
}
