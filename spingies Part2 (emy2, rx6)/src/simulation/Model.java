package simulation;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

import util.Vector;
import view.Canvas;


/**
 * XXX.
 * 
 * @author Xu Rui and Yoshi
 */
public class Model {
    // bounds and input for game
    private Canvas myView;
    private Environment myEnv; 
    // simulation state
    private List<Mass> myMasses;
    private List<Spring> mySprings;
    private List<Muscle> myMuscles;
    private UserSpring userSpring;
    private Mass userTrackerMass;
    //private List<Vector> myForces;
    private static final double TRACKER_MASS = 30;
    private static final double TRACKER_K = 1000;    
    private boolean userSpringLoaded = false;

    /**
     * Create a game of the given size with the given display for its shapes.
     */
    public Model (Canvas canvas) {
	myView = canvas;
	myMasses = new ArrayList<Mass>();
	mySprings = new ArrayList<Spring>();
	myMuscles = new ArrayList<Muscle>();
	myEnv = canvas.getEnvironment();
    }

    /**
     * Draw all elements of the simulation.
     */
    public void paint (Graphics2D pen) {
	for (Spring s : mySprings) {
	    s.paint(pen);
	}
	for (Mass m : myMasses) {
	    m.paint(pen);
	}
	for (Muscle m : myMuscles) {
	    m.paint(pen);
	}
	if(userSpringLoaded){
	    userSpring.paint(pen);
	}
    }

    /**
     * Update simulation for this moment, given the time since the last moment.
     */
    public void update (double elapsedTime) {	
	//Get the user input
	int key = myView.getLastKeyPressed();
	Point myMousePosition = myView.getLastMousePosition();
	Dimension bounds = myView.getSize();

	if(myMousePosition != null && !userSpringLoaded){ //load new spring
	    System.out.println(userSpringLoaded);
	    userTrackerMass = new Mass(myMousePosition.getX(), myMousePosition.getY(), TRACKER_MASS, -1);
	    createUserSpring(myMasses, myMousePosition, userTrackerMass);
	    System.out.println("spring created");
	    userSpringLoaded = true;

	}
	else if(myMousePosition != null  && userSpringLoaded){ //drag existing spring
	    System.out.println(userSpringLoaded);
	    System.out.println(mySprings);
	    System.out.println(mySprings);
	    userTrackerMass.setCenter(myMousePosition.getX(), myMousePosition.getY());
	}
	else{
	    userSpringLoaded = false;
	}

	if(key == Canvas.LOAD_ASSEMBLY ){
	    myView.loadModel();

	}else if(key == Canvas.CLEAR_ASSEMBLY){
	    myMasses.clear();
	    mySprings.clear();
	    myMuscles.clear();
	}

	if (key == Canvas.INCREASE_WALL_SIZE){
	    myView.adjustSize(true);
	}

	if (key == Canvas.DECREASE_WALL_SIZE){
	    myView.adjustSize(false);
	}

	for (Spring s : mySprings) {
	    s.update(elapsedTime, bounds);
	}
	for (Mass m : myMasses) {
	    m.update(myEnv, elapsedTime, bounds);


	}
	for (Muscle m : myMuscles) {
	    m.update(elapsedTime, bounds);
	}
	
	if (userSpringLoaded){
	    userSpring.update(elapsedTime, bounds);
	}

	myView.resetLastKeyPressed();

    }

    /**
     * Add given mass to this simulation.
     */
    public void add (Mass mass) {
	myMasses.add(mass);
    }

    /**
     * Get all the masses from Main
     * @return
     */
    public List<Mass> getMasses(){
	return  myMasses;
    }

    /**
     * Add given spring to this simulation.
     */
    public void add (Spring spring) {
	mySprings.add(spring);
    }

    /**
     * Add given muscle to this simulation.
     */
    public void add (Muscle muscle){
	myMuscles.add(muscle);
    }

    private void createUserSpring(List<Mass> masses, Point mousePosition, Mass userTrackerMass){
	double currentDistance = Double.MAX_VALUE;
	Mass myClosestMass = null;
	for(Mass m: masses){
	    double distance = Vector.distanceBetween(m.getCenter(), mousePosition);
	    if(distance < currentDistance){
		myClosestMass = m;
		currentDistance = Vector.distanceBetween(myClosestMass.getCenter(), mousePosition);
	    }
	}
	userSpring = new UserSpring(userTrackerMass, myClosestMass, currentDistance, TRACKER_K);
    }
}
