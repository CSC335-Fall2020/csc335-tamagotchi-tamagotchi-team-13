import java.util.Observable;

public class TamagotchiModel extends Observable{
	
	// Name of the tamagotchi
	private String name;
	
	// Age, starting at 0 and increasing
	private int age;
	
	// Health, starting at 100. Above 50 is healthy
	private int health;
	
	// Weight, starting at 50 (healthy). 100 (Obese), 10 (Starved)
	private int weight;
	
	// Happiness, starting at 75 (mostly happy). 100 (Very happy), 0 (Depressed)
	private int happiness;
	
	private boolean alive = true;
	
	private boolean paused = false;
	
	/**
	 * Default constructor with standard values
	 */
	public TamagotchiModel() {
		this("Unnamed", 1, 100, 50, 75);
	}
	
	/**
	 * Constructor with standard values, but custom name
	 * @param name is the name of the new tamagotchi
	 */
	public TamagotchiModel(String name) {
		this(name, 1, 100, 50, 75);
	}
	
	/**
	 * Constructor for loading saved wellbeing values
	 * @param name
	 * @param age
	 * @param health
	 * @param weight
	 * @param happiness
	 */
	public TamagotchiModel(String name, int age, int health, int weight, int happiness) {
		this.name = name;
		this.age = age;
		this.health = health;
		this.weight = weight;
		this.happiness = happiness;
	}
	
	/**
	 * Increases the tamagotchi's age by the parameter amount
	 * @param amtToIncreaseBy is the amount to increase the age by
	 * @return the new age of the tamagotchi
	 */
	public int increaseAge(int amtToIncreaseBy) {
		age += amtToIncreaseBy;
		return age;
	}
	
	/**
	 * Decreases the tamagotchi's health by the parameter amount
	 * @param amtToDecreaseBy is the amount to decrease the health by
	 * @return the new health of the tamagotchi
	 */
	public int decreaseHealth(int amtToDecreaseBy) {
		health -= amtToDecreaseBy;
		return health;
	}
	
	public void feedPetSnacks() {
		weight+=5;
		happiness+=5;
		setChanged();
		notifyObservers();
	}
	
	public void feedPetMeal() {
		weight+=2;
		setChanged();
		notifyObservers();
	}
	
	public void giveMedicine() {
		health+=10;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns whether or not the tamagotchi is dead.
	 * @return is the boolean described above.
	 */
	public boolean isDead() {
		//return !(health>0 && weight>10 && weight<120 && happiness>0);
		return !(alive);
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public int getHealth() {
		return health;
	}

	public int getWeight() {
		return weight;
	}

	public int getHappiness() {
		return happiness;
	}

	public boolean pauseGame() {
		paused = true;
		return paused;
	}
	
	public boolean unpauseGame() {
		paused = false;
		return paused;
	}
	
	public boolean isPause() {
		return paused;
	}

	public void petDies() {
		alive = false;
	}





}
