import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
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
		
		HBox allMechanics = new HBox();
		Button feedSnacks = new Button("Feed Snacks");
		Button feedMeal = new Button("Feed Meal");
		Button feedMedicine = new Button("Give Medicine");
		ProgressBar health = new ProgressBar();
		feedSnacks.setDisable(true);
		feedMeal.setDisable(true);
		feedMedicine.setDisable(true);
		
		allMechanics.getChildren().addAll(feedSnacks,feedMeal,feedMedicine);
		allMechanics.setPadding(new Insets(0,5,30,325));
		allMechanics.setSpacing(10);
		
		BorderPane window = new BorderPane();
		window.setTop(health);
		window.setBottom(allMechanics);
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
