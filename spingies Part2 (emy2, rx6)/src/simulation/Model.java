package simulation;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

import view.Canvas;


/**
 * XXX.
 * 
 * @author Robert C. Duvall
 */
public class Model {
	// bounds and input for game
	private Canvas myView;
	private Environment myEnv; 
	// simulation state
	private List<Mass> myMasses;
	private List<Spring> mySprings;
	private List<Muscle> myMuscles;
	//private List<Vector> myForces;


	/**
	 * Create a game of the given size with the given display for its shapes.
	 */
	public Model (Canvas canvas) {
		myView = canvas;
		myMasses = new ArrayList<Mass>();
		mySprings = new ArrayList<Spring>();
		myMuscles = new ArrayList<Muscle>();
		myEnv = canvas.getEnvironment();
		// myForces = new ArrayList<Vector>();
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
	}

	/**
	 * Update simulation for this moment, given the time since the last moment.
	 */
	public void update (double elapsedTime) {	
		//Get the user input
		int key = myView.getLastKeyPressed();

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
		
		Dimension bounds = myView.getSize();
		for (Spring s : mySprings) {
			s.update(elapsedTime, bounds);
		}
		for (Mass m : myMasses) {
			m.update(myEnv, elapsedTime, bounds);


		}
		for (Muscle m : myMuscles) {
			m.update(elapsedTime, bounds);
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
}
