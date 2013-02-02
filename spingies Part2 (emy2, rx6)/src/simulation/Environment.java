package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import util.Location;
import util.Vector;
import view.Canvas;

public class Environment {
    // data file keywords
    private static final String GRAVITY_KEYWORD = "gravity";
    private static final String VISCOSITY_KEYWORD = "viscosity";
    private static final String CM_KEYWORD = "centermass";
    private static final String WALL_KEYWORD = "wall";

    private Canvas myCanvas;
    private Model mySim;

    private Vector myGravity;
    private Vector myViscosity;
    private Vector wallForces;

    public double viscosityScale;
    public double fieldMag;
    public double fieldOrder;
    public double totalMass;

    private double gravityMagnitude;
    private double gravityAngle;
    private HashMap<Vector, Double> wallForcesList = new HashMap<Vector, Double>();

    public static ArrayList<Vector> myForces = new ArrayList<Vector>();

    public Environment(Canvas canvas) {
	myGravity = new Vector();
	myViscosity = new Vector();
	wallForces = new Vector();
	myCanvas = canvas;
    }

    /**
     * Loads the variables required to create the environment.
     * 
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
	    // should not happen because File came from user selection
	    e.printStackTrace();
	}
    }

    /**
     * getAllForces calculates the resultant force vector of a certain mass.
     * 
     * @param mass
     * @return totalForce
     */
    public Vector getAllForces(Mass mass) {
	setAllForces(mass);
	Vector totalForce = new Vector();
	for (Vector force : myForces) {
	    totalForce.sum(force);
	}
	return totalForce;
    }

    /**
     * helper function for getAllForces
     * 
     * @param mass
     */
    private void setAllForces(Mass mass) {
	myForces.clear();

	setGravity(mass);
	setViscosity(mass);
	setWallForces(mass);
	setFieldForce(mass);
    }

    // Setting and Calculating forces

    private void setGravity(Mass mass) {
	myGravity = new Vector(gravityAngle, gravityMagnitude);
	// myGravity.scale(mass.getMass());
	myForces.add(myGravity);
    }

    private void setViscosity(Mass mass) {
	myViscosity = new Vector(
		(mass.getVelocity().getDirection() + 180) % 360, viscosityScale
			* mass.getVelocity().getMagnitude());
	myForces.add(myViscosity);
    }

    private void setWallForces(Mass mass) {
	for (Entry<Vector, Double> entry : wallForcesList.entrySet()) {
	    double exponent = entry.getValue();
	    double magnitude = entry.getKey().getMagnitude();
	    double distance = 0;

	    // Switch was not working for mysterious reason
	    if (entry.getKey().getDirection() == 90.0) {// top wall, push down
		distance = mass.getY();
		// System.out.printf("distance top is %f and magnitude is %f\n",
		// distance, magnitude*(Math.pow(distance,-exponent)));
	    }

	    if (entry.getKey().getDirection() == 180.0) {// right wall, push
							 // left
		distance = myCanvas.getWidth() - mass.getX();
		// System.out.printf("distance right is %f and magnitude is %f\n",
		// distance, magnitude*(Math.pow(distance,-exponent)));

	    }
	    if (entry.getKey().getDirection() == 270.0) { // bottom wall, push
							  // up
		distance = myCanvas.getHeight() - mass.getY();
		// System.out.printf("distance bottom is %f and magnitude is %f\n",
		// distance, magnitude*(Math.pow(distance,-exponent)));

	    }
	    if (entry.getKey().getDirection() == 0.0) {// left wall, push right
		distance = mass.getX();
		// System.out.printf("distance left is %f and magnitude is %f\n",
		// distance, magnitude*(Math.pow(distance,-exponent)));
	    }

	    // condition for the masses velocity if they go out of bounds.
	    if (distance > 1) {
		Vector wallForce = new Vector(entry.getKey().getDirection(),
			magnitude * (Math.pow(distance, -exponent)));
		wallForces.sum(wallForce);
	    }
	    // System.out.println(wallForce);
	}
	myForces.add(wallForces);
    }

    private void setFieldForce(Mass mass) {
	Location CMLocation = getCMLocation(myCanvas);
	Vector distCM = new Vector(mass.getCenter(), CMLocation);

	Vector Force = new Vector(distCM.getDirection(), Math.abs(fieldMag)
		* Math.pow(distCM.getMagnitude(), -fieldOrder));
	if (fieldMag < 0) {
	    Force.negate();
	}
	myForces.add(Force);
    }

    public void addForce(Vector force) {
	myForces.add(force);
    }

    private void wallCommand(Scanner line) {
	int id = line.nextInt();
	double magnitude = line.nextDouble();
	double exponent = line.nextDouble();
	if (id == 1) {
	    wallForcesList.put(new Vector(90, magnitude), exponent);
	    // System.out.println(" WALL 1 IN LIST");
	}

	if (id == 2) {
	    wallForcesList.put(new Vector(180, magnitude), exponent);
	    // System.out.println(" WALL 2 IN LIST");
	}

	if (id == 3) {
	    wallForcesList.put(new Vector(270, magnitude), exponent);
	    // System.out.println(" WALL 3 IN LIST");

	}

	if (id == 4) {
	    wallForcesList.put(new Vector(0, magnitude), exponent);
	    // System.out.println(" WALL 4 IN LIST");
	}
    }

    /**
     * Brings mySimulation model inside the environment
     * 
     * @param model
     */
    private void setModel(Model model) {
	mySim = model;
    }

    /**
     * Calculates the position of the center of mass of all masses after they
     * are loaded in mySimulation
     * 
     * @param myCanvas
     * @return
     */
    private Location getCMLocation(Canvas myCanvas) {
	if (myCanvas.getModel() != null) {
	    setModel(myCanvas.getModel());

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
