import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


public class TamagotchiController extends Thread {
	
	private TamagotchiModel model;
	private final Object pauseLock = new Object();
	private boolean paused = false;
	
	/**
	 * the default constructor used for starting up a new game
	 * @param model
	 */
	public TamagotchiController(TamagotchiModel model) {
		this.model = model;
	}
	
	/**
	 * Increases the tamagotchi's age by the parameter amount
	 * @param amtToIncreaseBy is the amount to increase the age by
	 * @return the new age of the tamagotchi
	 */
	public void incrementAge(int ageToIncrease) {
		model.increaseAge(ageToIncrease);
	}
	
	/**
	 * Decreases the tamagotchi's health by the parameter amount
	 * @param amtToDecreaseBy is the amount to decrease the health by
	 */
	public void decrementHealth(int healthToDecrease) {
		model.decreaseHealth(healthToDecrease);
	}
	
	/**
	 * feeds the pet by calling the model function to update the stats
	 */
	public void feedPetSnacks() {
		model.feedPetSnacks();
	}
	
	/**
	 * feeds the pet by calling the model function to update the stats
	 */
	public void feedPetMeal() {
		model.feedPetMeal();
	}
	
	/**
	 * gives the pet medicine by calling the model function to update the stats
	 */
	public void giveMedicine() {
		model.giveMedicine();
		model.setSick(false);
	}
	
	/**
	 * updates the paused variable by making it true
	 */
	public void pause() {
		paused = true;
	}
	
	/**
	 * updates the paused variable to be false
	 */
	public void unPause() {
		paused = false;
	}

	/**
	 * creates a string to be put on the main menu
	 * @return String the age of the pet
	 */
	public String getAgeDescription() {
		return "Age: " + model.getAge();
	}
	
	/**
	 * gets the health status of the pet
	 * @return String, the health status
	 */
	public String getHealthDescription() {
		int health = model.getHealth();
		String descr = "";
		if (health>=60) {
			descr = "(Healthy)";
		} else if(health>=20){
			descr = "(Ok)";
		}
		else {
			descr = "(Sick)";
			model.setSick(true);
		}
		return descr;
	}
	
	/**
	 * gets the weight status of the pet
	 * @return String, the weight status
	 */
	public String getWeightDescription() {
		
		int weight = model.getWeight();
		String descr = "";
		if (weight>=75) {
			descr = "(Obese)";
		} else if (weight >50) {
			descr = "(Overweight)";
		} else if (weight==50) {
			descr = "(Perfect)";
		} else if (weight>=25) {
			descr = "(Underweight)";
		} else {
			descr = "(Malnourished)";
		}
		return descr;
	}
	
	/**
	 * gets the happiness status of the pet
	 * @return String, the happiness status
	 */
	public String getHappinessDescription() {
		
		int happiness = model.getHappiness();
		String descr = "";
		if (happiness >= 65) {
			descr = "(Happy)";
		} else if (happiness >= 20) {
			descr = "(Okay)";
		} else {
			descr = "(Unhappy)";
		}
		return descr;
	}

	/**
	 * gets the weight of the pet
	 * @return the weight as an int
	 */
	public int getWeight() {
		return model.getWeight();
	}

	/**
	 * gets the health of the pet
	 * @return the health as an int
	 */
	public int getHealth() {
		return model.getHealth();
	}

	/**
	 * gets the happiness of the pet
	 * @return the happiness as an int
	 */
	public int getHappiness() {
		return model.getHappiness();
	}

	/**
	 * gets the age of the pet
	 * @return the age as an int
	 */
	public int getAge() {
		return model.getAge();
	}

	/**
	 * updates the alive variable in the model that makes sure its dead
	 */
	public void petDies() {
		model.petDies();
	}
	
	/**
	 * Begins the threads so that the game can actually run, this includes
	 * the updating the view and decrementing the values in the model
	 */
	public void run() {
		int i = 0;
		while(!model.isDead()) {
			
            synchronized (pauseLock) {
            	if(model.isDead()) {
            		break;
            	}
            	if(paused) {
            		try {
            			synchronized(pauseLock) {
            				pauseLock.wait();
            			}
            		}
            		catch (InterruptedException ex) {
                        break;
                    }
                    if (model.isDead()){ // running might have changed since we paused
                        break;
                    }
            	}
            }

			
			try {
				Thread.sleep(2000);
				try {
					model.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
			    Thread.currentThread().interrupt();
			}
			
			if(i % 3 == 0) {
				model.increaseAge(1);
			}
			int amtToDecreaseHealth = 2;
			int amtToDecreaseHappiness = 1;
			if(shouldGetSick()) {
				amtToDecreaseHealth += 15;
				amtToDecreaseHappiness += 30;
			}
			
			model.decreaseHealth(amtToDecreaseHealth);
			model.decreaseHappiness(amtToDecreaseHappiness);
			model.decreaseWeight(1);
			i++;
		}
		
		
	}
	
	/**
	 * unlocks the threads so it can start up all again
	 */
	public void resumeGame() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }
	
	
	/**
     * This function returns whether or not the character should get sick, 
     * based on the character's health, happiness, and weight.
     * @return boolean of its sickness status
     */
    public boolean shouldGetSick() {

        if (model.getHealth()<30) {
            return true;
        }

        int chancePool = 15;

        // Increase likelihood when over/under weight
        if (model.getWeight() > 60 || model.getWeight() < 40) {
            chancePool-=5;
        }

        // Increase likelihood when unhappy
        if (model.getHappiness()<30) {
            chancePool-=5;
        }

        // Catch low values
        if (chancePool<2) {
            chancePool = 2;
        }

        // Draw random from pool. If is 1, gets sick (returns true)
        return ThreadLocalRandom.current().nextInt(0, chancePool) == 1;
    }

    /**
     * sets the pet image locations within the model
     * @param pet image location
     * @param sadPet image location
     */
	public void setPet(String pet, String sadPet) {
		model.setPet(pet, sadPet);
	}

	/**
	 * gets the sad pet image location
	 * @return String image location
	 */
	public String getSadPet() {
		return model.getSadPet();
	}

	/**
	 * gets the regular pet image location
	 * @return String image location
	 */
	public String getPet() {
		return model.getPet();
	}

	/**
	 * calls the model function so it can load all the stats into a file
	 * @throws FileNotFoundException
	 */
	public void load() throws FileNotFoundException {
		model.load();
	}

	/**
	 * calls the model function so it can save all the stats into a file
	 * @throws IOException
	 */
	public void save() throws IOException {
		model.save();
	}
	
	/**
	 * gets the status of if the pet is dead
	 * @return boolean if the pet is dead
	 */
	public boolean isDead() {
		return model.isDead();
	}
}
