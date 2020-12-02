import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
		BorderPane window = new BorderPane();
		model.addObserver(this);
		Scene mainScene = new Scene(window, 900, 400);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Tamgaochi");
		primaryStage.show();
	}
	
	/*
	 * GUI retieve name from user (onboarding)
	 */
	
	
	
	
}
