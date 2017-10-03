package Business;

import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;

public class PSOLogic {

	// Global Constants

	// Number of iteration
	int ITERATION = 100;
	
	//Number of swarms
	int SWARM_SIZE = 30; 
	
	// Acceleration coefficients
	double C1 = 1;
	double C2 = 1;
	
	// To calculate inertia
	double W_UPPER = 1.0;
	double W_LOWER = 0.0;
	
	// Counter to count number of iterations completed
	private int t = 0;
	
	// err
	private double err = 9999;
	private double w;
	
	// Draw graph on panel 
	// Minimum x coordinate
	public static final double X_LOC_LOW = -8;
	// Maximum x coordinate
	public static final double X_LOC_HIGH = 8;
	// Minimum y coordinate
	public static final double Y_LOC_LOW = -8;
	// Maximum y coordinate
	public static final double Y_LOC_HIGH = 8;
	
	// Velocity low value
	public static final double LOW_VEL = -1;
	// Velocity high value
	public static final double HIGH_VEL = 1;

	// Error tolerance - less than value more number of iterations
	public static final double ERR_TOLERANCE = 1E-20; 
	
	// Boat(Particles)
	private Vector<Boat> swarm = new Vector<Boat>();

	// Getter
	public Vector<Boat> getSwarm() {
		return swarm;
	}

	// Setter
	public Location getgBLoc() {
		return gBLoc;
	}

	// Personal best
	private double[] pB = new double[SWARM_SIZE];

	// Personal best location
	private Vector<Location> pBLoc = new Vector<Location>();

	// Global best
	private double gB;

	// Global best location
	private Location gBLoc;

	// Fitness Value
	private double[] fitnessValueList = new double[SWARM_SIZE];

	Random generator = new Random();

	// Start point of program
	public static void main(String[] args) {
		Main frame= new Main();
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void execute(boolean forward) {
		
		if(forward && t < ITERATION)
		{
			t++;
		}
		else if(!forward && t > 0)
		{
			t = 0;
		}
		
		
		if(t == 0)
		{
			initializeSwarm();
			updateFitnessList();
			
			for(int i=0; i<SWARM_SIZE; i++) {
				pB[i] = fitnessValueList[i];
				pBLoc.add(swarm.get(i).getLocation());
			}
			err = 9999;
		}
		
		if(t < ITERATION && err > ERR_TOLERANCE) 
		{
			// Update Personal Best
			for(int i=0; i<SWARM_SIZE; i++) {
				if(fitnessValueList[i] < pB[i]) {
					pB[i] = fitnessValueList[i];
					pBLoc.set(i, swarm.get(i).getLocation());
				}
			}
				
			// Update Global Best
			int bestParticleIndex = getMinPos(fitnessValueList);
			if(t == 0 || fitnessValueList[bestParticleIndex] < gB) {
				gB = fitnessValueList[bestParticleIndex];
				gBLoc= swarm.get(bestParticleIndex).getLocation();
			}
			
			w = W_UPPER - (((double) t) / ITERATION) * (W_UPPER - W_LOWER);
			
			for(int i=0; i<SWARM_SIZE; i++) {
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();
				
				Boat boat = swarm.get(i);
				
				// Update Velocity
				double[] newVel = new double[2];
				newVel[0] = (w * boat.getVelocity().getPos()[0]) + 
							(r1 * C1) * (pBLoc.get(i).getLoc()[0] - boat.getLocation().getLoc()[0]) +
							(r2 * C2) * (gBLoc.getLoc()[0] - boat.getLocation().getLoc()[0]);
				newVel[1] = (w * boat.getVelocity().getPos()[1]) + 
							(r1 * C1) * (pBLoc.get(i).getLoc()[1] - boat.getLocation().getLoc()[1]) +
							(r2 * C2) * (gBLoc.getLoc()[1] - boat.getLocation().getLoc()[1]);
				Velocity vel = new Velocity(newVel);
				boat.setVelocity(vel);
				
				// Update Location
				double[] newLoc = new double[2];
				newLoc[0] = boat.getLocation().getLoc()[0] + newVel[0];
				newLoc[1] = boat.getLocation().getLoc()[1] + newVel[1];
				Location loc = new Location(newLoc);
				boat.setLocation(loc);
			}
			
			err = evaluate(gBLoc) - 0;
			
			
			System.out.println("GLOBAL BEST LOCATION FOR ITERATION " + t + ": ");
			System.out.println("BOAT X COORDINATE: " + gBLoc.getLoc()[0]);
			System.out.println("BOAT Y COORDINATE: " + gBLoc.getLoc()[1]);
			System.out.println("Value: " + evaluate(gBLoc));
			System.out.println("_______________________________________________");

			
			updateFitnessList();
		}
		else
		{
			
			System.out.println("Solution found at iteration:" + (t - 1));
			System.out.println("Best X: " + gBLoc.getLoc()[0]);
			System.out.println("Best Y: " + gBLoc.getLoc()[1]);
			System.out.println("_______________________________________________");
			
		}
	}

	// Initialize Swarm
	public void initializeSwarm() {
		Boat boat;
		for (int i = 0; i < SWARM_SIZE; i++) {
			boat = new Boat();

			// Set location of boat using a random generator
			double[] loc = new double[2];
			loc[0] = X_LOC_LOW + generator.nextDouble() * (X_LOC_HIGH - X_LOC_LOW);
			loc[1] = Y_LOC_LOW + generator.nextDouble() * (Y_LOC_HIGH - Y_LOC_LOW);
			Location location = new Location(loc);

			// Set velocity of boat using a random generator
			double[] vel = new double[2];
			vel[0] = LOW_VEL + generator.nextDouble() * (HIGH_VEL - LOW_VEL);
			vel[1] = LOW_VEL + generator.nextDouble() * (HIGH_VEL - LOW_VEL);
			Velocity velocity = new Velocity(vel);

			boat.setLocation(location);
			boat.setVelocity(velocity);
			swarm.add(boat);
		}
	}

	// Update Fitness List
	public void updateFitnessList() {
		for (int i = 0; i < SWARM_SIZE; i++) {
			fitnessValueList[i] = swarm.get(i).getFitnessValue();
		}
	}

	// Get minimum value from fitness list
	public static int getMinPos(double[] list) {
		int pos = 0;
		double minValue = list[0];
		for (int i = 0; i < list.length; i++) {
			if (list[i] < minValue) {
				pos = i;
				minValue = list[i];
			}
		}
		return pos;
	}

	// Returns the fitness value
	public static double evaluate(Location location) {
		double result = 0;
		double x = location.getLoc()[0]; 
		double y = location.getLoc()[1]; 

		result = Math.sqrt(Math.pow(x - 4, 2) + Math.pow(y - 2, 2));
		return result;
	}
}
