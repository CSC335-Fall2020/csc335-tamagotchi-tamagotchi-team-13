import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.stage.Stage;

public class TamagotchiView extends Application implements Observer{

	
	TamagotchiModel model = new TamagotchiModel();
	TamagotchiController controller = new TamagotchiController(model);
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		model.addObserver(this);
		
	}
	
	/*
	 * GUI retieve name from user (onboarding)
	 */
	
	
	
	
}
