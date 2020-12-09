import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Scanner;


public class TamagotchiModel extends Observable{
	
	// Name of the tamagotchi
	private String name;
	
	// Age, starting at 0 and increasing
	private int age;
	
	// Health, starting at 100. Above 60 is healthy
	private int health;
	
	// Weight, starting at 50 (healthy). 100 (Obese), 10 (Starved)
	private int weight;
	
	// Happiness, starting at 75 (mostly happy). 100 (Very happy), 0 (Depressed)
	private int happiness;
	
	private boolean alive = true;
	
	private boolean paused = false;
	
	private boolean sick = false;
	
	private String pet;
	private String sadPet;
	
	private File loadFile = new File("loadFile.txt");
	
	/**
	 * Default constructor with standard values
	 */
	public TamagotchiModel() {
		this("Unnamed", 0, 100, 50, 75);
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
	public void increaseAge(int amtToIncreaseBy) {
		age += amtToIncreaseBy;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Decreases the tamagotchi's health by the parameter amount
	 * @param amtToDecreaseBy is the amount to decrease the health by
	 * @return the new health of the tamagotchi
	 */
	public void decreaseHealth(int amtToDecreaseBy) {
		health -= amtToDecreaseBy;	
		setChanged();
		notifyObservers();
	}
	
	public void feedPetSnacks() {
		weight+=5;
		happiness+=5;
		if(happiness>100) {
			happiness=100;
		}
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
		if(health>100) {
			health = 100;
		}
		setChanged();
		notifyObservers();
	}
	
	public void decreaseWeight(int decreaseAmount) {
		weight -= decreaseAmount;
		setChanged();
		notifyObservers();
	}
	
	public void decreaseHappiness(int amtToDecreaseHappiness) {
		happiness -= amtToDecreaseHappiness;
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
	
	public boolean isSick() {
		return sick;
	}
	
	public boolean isPause() {
		return paused;
	}

	public void petDies() {
		alive = false;
	}

	public void setSick(boolean b) {
		sick = b;
	}

	public void setPet(String pet, String sadPet) {
		this.pet = pet;
		this.sadPet = sadPet;
	}

	public String getSadPet() {
		return sadPet;
	}

	public String getPet() {
		return pet;
	}
	
	public void load() throws FileNotFoundException {
		Scanner reader = new Scanner(loadFile);
		int pos = 0;
		while(reader.hasNext()) {
			if(pos == 0) {
				health = Integer.parseInt(reader.nextLine());
			}else if(pos == 1) {
				weight = Integer.parseInt(reader.nextLine());
			}else if(pos == 2) {
				happiness = Integer.parseInt(reader.nextLine());
			}else if(pos == 3) {
				age = Integer.parseInt(reader.nextLine());
			}else if(pos == 4) {
				pet = reader.nextLine();
			}else if(pos == 5) {
				sadPet = reader.nextLine();
			}
			pos++;
		}
		reader.close();
	}
	
	public void save() throws IOException {
		loadFile.delete();
		loadFile = new File("loadFile.txt");
		loadFile.createNewFile();
		FileWriter write = new FileWriter(loadFile);
		write.write(health + "\n");
		write.write(weight + "\n");
		write.write(happiness + "\n");
		write.write(age + "\n");
		write.write(pet + "\n");
		write.write(sadPet + "\n");
		write.close();
	}
}
