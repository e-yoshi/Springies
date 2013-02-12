package simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import util.Location;
import util.Pixmap;
import util.Sprite;
import util.Vector;

/**
 * XXX.
 * 
 * @author Xu Rui and Yoshi
 */
public class Spring extends Sprite {
    // reasonable default values
    public static final Pixmap DEFUALT_IMAGE = new Pixmap("spring.gif");
    public static final int IMAGE_HEIGHT = 20;

    private Mass myStart;
    private Mass myEnd;
    private double myLength;
    private double myK;
    private double restLength;
    private double prevLength;

    /**
     * XXX.
     */
    public Spring(Mass start, Mass end, double length, double kVal) {
	super(DEFUALT_IMAGE, getCenter(start, end), getSize(start, end));
	myStart = start;
	myEnd = end;
	restLength = length;
	prevLength = length;
	myLength = myStart.distance(myEnd);
	myK = kVal;
    }

    /**
     * Paint Line.
     */
    public void paint(Graphics2D pen) {
	pen.setColor(getColor(myStart.distance(myEnd) - restLength));
	pen.drawLine((int) myStart.getX(), (int) myStart.getY(),
		(int) myEnd.getX(), (int) myEnd.getY());
    }

    /**
     * Update the parameters of the spring.
     */
    @Override
    public void update(double elapsedTime, Dimension bounds) {
	double distBetweenMasses = Vector.distanceBetween(myStart.getCenter(), myEnd.getCenter());
	double angleBetweenMasses = Vector.angleBetween(myStart.getCenter(), myEnd.getCenter());

	// apply hooke's law to each attached mass
	Vector force = new Vector(angleBetweenMasses, myK * (restLength - distBetweenMasses));
	myStart.applyForce(force);
	force.negate();
	myEnd.applyForce(force);
	// update sprite values based on attached masses
	setCenter(getCenter(myStart, myEnd));
	setSize(getSize(myStart, myEnd));
	setVelocity(angleBetweenMasses, 0);
    }

    /**
     * Convenience method.
     */
    protected Color getColor(double diff) {
	if (Vector.fuzzyEquals(diff, 0))
	    return Color.BLACK;
	else if (diff < 0.0)
	    return Color.BLUE;
	else
	    return Color.RED;
    }

    // compute center of this spring
    private static Location getCenter(Mass start, Mass end) {
	return new Location((start.getX() + end.getX()) / 2,
		(start.getY() + end.getY()) / 2);
    }

    // compute size of this spring
    private static Dimension getSize(Mass start, Mass end) {
	return new Dimension((int) start.distance(end), IMAGE_HEIGHT);
    }

    /**
     * Creates an oscillatory movement of the masses attached to it Method used
     * by the muscle class.
     */
    public void moveMassesAlongOscillation() {
	//Vector vectDist = new Vector(myStart.getCenter(), myEnd.getCenter());
	double dx = myStart.getX() - myEnd.getX();
	double dy = myStart.getY() - myEnd.getY();    	
	double angle = Vector.angleBetween(dx, dy);
	double lengthChange = (myLength - prevLength) /2;
/*	if(prevLength > restLength){
	    lengthChange = (myLength - prevLength) / 2;
	}
	else{
	     lengthChange = 0;
	}*/
	
	//myEnd.moveMass(lengthChange, vectDist.getDirection());
	//vectDist.negate();
	//myStart.moveMass(lengthChange, vectDist.getDirection());
	myStart.moveMass(lengthChange, angle);//0
	myEnd.moveMass(lengthChange, angle+180);//180
	prevLength = myLength;
    }

    public void setLength(double length) {
	myLength = length;
    }

    public double getLength() {
	return myLength;
    }
    
    
    public void setEndMass(Mass mass){
	myEnd = mass;
    }
}