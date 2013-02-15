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
	private Canvas myCanvas;
	
<<<<<<< HEAD
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
	private SingleWallForce wallForceLeft = DEFAULT_WALLFORCE_4;
=======
	//gravity information, create default values
	private double gravityAngle = Gravity.DEFAULT_GRAVITY_ANGLE;
	private double gravityMagnitude = Gravity.DEFAULT_GRAVITY_MAGNITUDE;
	private Gravity myGravity = new Gravity(gravityAngle, gravityMagnitude, true);


	//viscosity information, create default values
	private Viscosity myViscosity = new Viscosity(Force.DEFAULT_ANGLE, Force.DEFAULT_MAGNITUDE, true);
	private double viscosityScale = Viscosity.DEFAULT_VISCOSITY_SCALE;
	
	//center of mass information, create default values
	private CenterOfMassForce myCMForce = new CenterOfMassForce(Force.DEFAULT_ANGLE, Force.DEFAULT_MAGNITUDE, true);
	private double fieldMag = CenterOfMassForce.DEFAULT_FIELDMAGNITUDE;
	private double fieldOrder = CenterOfMassForce.DEFAULT_FIELDORDER;
>>>>>>> Final version of Springies
	
	//wall forces information, create default wall forces;
	private SingleWallForce wallForceTop = new SingleWallForce(Vector.DOWN_DIRECTION, Wallforce.DEFAULT_WALLFORCE_MAGNITUDE, Wallforce.DEFAULT_WALLFORCE_EXPONENT);
	private SingleWallForce wallForceRight = new SingleWallForce(Vector.LEFT_DIRECTION, Wallforce.DEFAULT_WALLFORCE_MAGNITUDE, Wallforce.DEFAULT_WALLFORCE_EXPONENT);
	private SingleWallForce wallForceBottom =  new SingleWallForce(Vector.UP_DIRECTION, Wallforce.DEFAULT_WALLFORCE_MAGNITUDE, Wallforce.DEFAULT_WALLFORCE_EXPONENT);
	private SingleWallForce wallForceLeft = new SingleWallForce(Vector.RIGHT_DIRECTION, Wallforce.DEFAULT_WALLFORCE_MAGNITUDE, Wallforce.DEFAULT_WALLFORCE_EXPONENT);
	private ArrayList<SingleWallForce> wallForcesList = new ArrayList<SingleWallForce>();

	private ArrayList<Vector> myForces = new ArrayList<Vector>();

	/**
	 * Constructor.
	 * 
	 * @param canvas
	 */
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
					if (Gravity.GRAVITY_KEYWORD.equals(type)) {
						gravityAngle = line.nextDouble();
						gravityMagnitude = line.nextDouble();
					} else if (Viscosity.VISCOSITY_KEYWORD.equals(type)) {
						viscosityScale = line.nextDouble();

					} else if (CenterOfMassForce.CM_KEYWORD.equals(type)) {
						fieldMag = line.nextDouble();
						fieldOrder = line.nextDouble();
					} else if (Wallforce.WALL_KEYWORD.equals(type)) {
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
	 * Sets all forces to myForces according to switch information.
	 * @param mass
	 */
	
	private void setAllForces(Mass mass){
		myViscosity = new Viscosity(viscosityScale, mass, myViscosity.isTurnedOn());
		myCMForce = new CenterOfMassForce(fieldMag, fieldOrder, myCanvas, mass, myCMForce.isTurnedOn());
		myGravity = new Gravity(gravityAngle, gravityMagnitude, myGravity.isTurnedOn());
		
		wallForcesList.add(wallForceTop);
		wallForcesList.add(wallForceRight);
		wallForcesList.add(wallForceBottom);
		wallForcesList.add(wallForceLeft);
	}
	
	/**
	 * Adds all forces to myForces after checking for user controls.
	 * @param mass
	 */
	private void addAllForces(Mass mass) {
		int key = myCanvas.getLastKeyPressed();
		myForces.clear();
		wallForcesList.clear();
		checkControls(key);
		setAllForces(mass);
		
<<<<<<< HEAD
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
		
=======
		myForces.add(myGravity);
		myForces.add(myViscosity);
		myForces.add(myCMForce);
>>>>>>> Final version of Springies
		myForces.add(new Wallforce(myCanvas, wallForcesList, mass));
		myCanvas.resetLastKeyPressed();
	}
	
	/**
	 * check for user input and toggle switches accordingly.
	 * @param key
	 */
	private void checkControls(int key){
		if (key == Canvas.TOGGLE_GRAVITY){
			myGravity.toggleSwitch();
		}
		if (key == Canvas.TOGGLE_VISCOSITY){
		    myViscosity.toggleSwitch();
		}
		if (key == Canvas.TOGGLE_CM){
			myCMForce.toggleSwitch();
		}
		if (key == Canvas.TOGGLE_WALLFORCE1){
			wallForceTop.toggleSwitch();
		}
		if (key == Canvas.TOGGLE_WALLFORCE2){
			wallForceRight.toggleSwitch();
		}
		if (key == Canvas.TOGGLE_WALLFORCE3){
			wallForceBottom.toggleSwitch();
		}
		if (key == Canvas.TOGGLE_WALLFORCE4){
			wallForceLeft.toggleSwitch();

		}
	}

	/**
	 * Reads wall forces information for data 
	 * file and puts them into wallForcesMap.
	 * @param line
	 */
	private void wallCommand(Scanner line) {
		int wallSide = line.nextInt();
		double wallMagnitude = line.nextDouble();
		double wallExponent = line.nextDouble();

		switch (wallSide) {

		case 1:
<<<<<<< HEAD
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
=======
			wallForceTop = new SingleWallForce(Vector.DOWN_DIRECTION, wallMagnitude, wallExponent);
			break;
		case 2:
			wallForceRight = new SingleWallForce(Vector.LEFT_DIRECTION, wallMagnitude, wallExponent);
			break;
		case 3:
			wallForceBottom = new SingleWallForce(Vector.UP_DIRECTION, wallMagnitude, wallExponent);
			break;
		case 4:
			wallForceLeft = new SingleWallForce(Vector.RIGHT_DIRECTION, wallMagnitude, wallExponent);
>>>>>>> Final version of Springies
			break;
		default:
			break;
		}
	}
<<<<<<< HEAD
	/**
	 * Toggle the force.
	 * @param bool
	 * @return
	 */
	private boolean toggle(boolean bool){
	    bool = !bool;
	    return bool;
	}
=======
>>>>>>> Final version of Springies
}
