import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


public class TamagotchiController extends Thread {
	
	private TamagotchiModel model;
	private final Object pauseLock = new Object();
	private boolean paused = false;
	
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
	
	public void feedPetSnacks() {
		model.feedPetSnacks();
	}
	
	public void feedPetMeal() {
		model.feedPetMeal();
	}
	
	public void giveMedicine() {
		model.giveMedicine();
		model.setSick(false);
	}
	
	public void pause() {
		paused = true;
	}
	
	public void unPause() {
		paused = false;
	}

	public String getAgeDescription() {
		return "Age: " + model.getAge();
	}
	
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

	public int getWeight() {
		return model.getWeight();
	}

	public int getHealth() {
		return model.getHealth();
	}

	public int getHappiness() {
		return model.getHappiness();
	}

	public int getAge() {
		return model.getAge();
	}

	public void petDies() {
		model.petDies();
	}
	
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
				Thread.sleep(1000);
				try {
					model.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
			    Thread.currentThread().interrupt();
			}
			
			if(i % 5 == 0) {
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
	
	public void resumeGame() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }
	
	
	/**
     * This function returns whether or not the character should get sick, 
     * based on the character's health, happiness, and weight.
     * @return
     */
    private boolean shouldGetSick() {

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

	public void setPet(String pet, String sadPet) {
		model.setPet(pet, sadPet);
	}

	public String getSadPet() {
		return model.getSadPet();
	}

	public String getPet() {
		return model.getPet();
	}

	public void load() throws FileNotFoundException {
		model.load();
	}

	public void save() throws IOException {
		model.save();
	}
}
