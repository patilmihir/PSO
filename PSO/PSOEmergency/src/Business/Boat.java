package Business;

public class Boat {
	private double fitnessValue;
	private Velocity velocity;
	private Location location;

	public Boat() {
		super();
	}

	public Velocity getVelocity() { 
		return velocity;
	} 

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getFitnessValue() {
		fitnessValue = PSOLogic.evaluate(location);
		return fitnessValue;
	}
}
