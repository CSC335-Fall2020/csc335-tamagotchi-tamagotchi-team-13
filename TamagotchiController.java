
public class TamagotchiController {
	
	private TamagotchiModel model;
	
	public TamagotchiController(TamagotchiModel game) {
		model = game;
	}
	
	/**
	 * Increases the tamagotchi's age by the parameter amount
	 * @param amtToIncreaseBy is the amount to increase the age by
	 * @return the new age of the tamagotchi
	 */
	public int incrementAge(int ageToIncrease) {
		return model.increaseAge(ageToIncrease);
	}
	
	/**
	 * Decreases the tamagotchi's health by the parameter amount
	 * @param amtToDecreaseBy is the amount to decrease the health by
	 * @return the new health of the tamagotchi
	 */
	public int decrementHealth(int healthToDecrease) {
		return model.decreaseHealth(healthToDecrease);
	}
	
	public String getAgeDescription() {
		return "Age: " + model.getAge();
	}
	
	public String getHealthDescription() {
		int health = model.getHealth();
		String descr = "";
		if (health>=50) {
			descr = "(Healthy)";
		} else {
			descr = "(Sick)";
		}
		return "" + health + " " + descr;
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
		return "" + weight + " " + descr;
	}
	
	public String getHappinessDescription() {
		
		int happiness = model.getHappiness();
		String descr = "";
		if (happiness >= 75) {
			descr = "(Happy)";
		} else if (happiness >= 25) {
			descr = "(Okay)";
		} else {
			descr = "(Unhappy)";
		}
		return "" + happiness + " " + descr;
	}
	
}
