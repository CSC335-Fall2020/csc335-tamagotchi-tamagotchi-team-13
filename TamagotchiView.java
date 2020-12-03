import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.stage.Stage;

public class TamagotchiView extends Application implements Observer{

	
	TamagotchiModel model = new TamagotchiModel();
	TamagotchiController controller = new TamagotchiController(model);
	private int interval = 3000;
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		model.addObserver(this);

		// Create gameplay window
		

		while (!model.isDead()) {

			// Drive game events




			// Delay loop
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Character died

	}
	
	/*
	 * GUI retieve name from user (onboarding)
	 */
	
	
	
	
}
