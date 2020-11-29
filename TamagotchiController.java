
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
	
	
	
}
