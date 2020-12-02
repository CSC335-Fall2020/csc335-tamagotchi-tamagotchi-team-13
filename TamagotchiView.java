import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TamagotchiView extends Application implements Observer{

	private static final int WIDTH = 900;
	private static final int HEIGHT = 400;
	
	
	TamagotchiModel model = new TamagotchiModel();
	TamagotchiController controller = new TamagotchiController(model);
	BorderPane root = new BorderPane();
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		model.addObserver(this);
		primaryStage.setScene(mainMenu(primaryStage));
		primaryStage.setTitle("Tamgaochi");
		primaryStage.show();
	}
	
	private Scene mainMenu(Stage primaryStage) {
		addBackground(root);
		addMenu(primaryStage);
		return new Scene(root, WIDTH, HEIGHT);
	}
	
	private VBox menuBox = new VBox(-5);
	
	private void addMenu(Stage primaryStage) {
		menuBox.setSpacing(10);
		menuBox.setPadding(new Insets(0,0,0,20));
		menuBox.setAlignment(Pos.CENTER_LEFT);
		Button newGame = new Button("New Game");
		newGame.setPrefWidth(150);
		newGame.setFont(Font.font(15));
		
		newGame.setOnMouseClicked(e -> {
			try {
				Scene newScene = runGame();
				primaryStage.setScene(newScene);
				primaryStage.show();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		Button loadGame = new Button("Load Game");
		loadGame.setPrefWidth(150);
		loadGame.setFont(Font.font(15));
		
		loadGame.setOnMouseClicked(e ->{
			
		});
		
		Button quit = new Button("Quit");
		quit.setPrefWidth(150);
		quit.setFont(Font.font(15));
		
		quit.setOnMouseClicked(e ->{
			primaryStage.close();
		});
		
		
		menuBox.getChildren().addAll(newGame,loadGame,quit);
		root.setLeft(menuBox);
	}
	
	
	
	
	private void addBackground(BorderPane root) {
		root.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
	}
	
	private Scene runGame() throws Exception {
		//Scene newScene = new Scene(WIDTH,HEIGHT);
		BorderPane newRoot = new BorderPane();
		addBackground(newRoot);
		HBox allMechanics = new HBox();
		Button feedSnacks = new Button("Feed Snacks");
		Button feedMeal = new Button("Feed Meal");
		Button feedMedicine = new Button("Give Medicine");
		//ProgressBar health = new ProgressBar();
		Button pause = new Button("Pause");
		Image pet;
		pet = null;
		try {
			pet = new Image(new FileInputStream("./Pet Images/petArt.jpg"));
		}
		catch (Exception e){
			Alert a = new Alert(AlertType.ERROR, "Pet Image not Found");
			a.showAndWait();
		}
		ImageView mainView = new ImageView(pet);
		feedSnacks.setDisable(true);
		feedMeal.setDisable(true);
		feedMedicine.setDisable(true);
		
		allMechanics.getChildren().addAll(feedSnacks,feedMeal,feedMedicine);
		allMechanics.setPadding(new Insets(0,5,30,325));
		allMechanics.setSpacing(10);
		
		newRoot.setTop(pause);
		newRoot.setCenter(mainView);
		newRoot.setBottom(allMechanics);
		Scene newScene = new Scene(newRoot, WIDTH,HEIGHT);
		return newScene;
	}
	
	/*
	 * GUI retieve name from user (onboarding)
	 */
}
