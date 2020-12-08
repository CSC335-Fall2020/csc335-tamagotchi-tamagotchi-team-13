import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

public class TamagotchiView extends Application implements Observer{

	private static final int WIDTH = 900;
	private static final int HEIGHT = 400;
	private String petImage = "";
	
	
	TamagotchiModel model = new TamagotchiModel();
	TamagotchiController controller = new TamagotchiController(model);
	BorderPane root = new BorderPane();
	
	Rectangle health = new Rectangle(200.0, 50.0, Color.GREEN);
	Rectangle weight = new Rectangle(100.0, 50.0, Color.GREEN);
	Rectangle happiness = new Rectangle(150.0, 50.0, Color.GREEN);
	Text happinessText = new Text("75");
	Text weightText = new Text("50");
	Text healthText = new Text("100");
	Text ageText = new Text("Age: 0");
	Text healthStatus = new Text("(Healthy)");
	VBox healthVBox = new VBox();
	Text happinessStatus = new Text("(Happy)");
	Text weightStatus = new Text("\t\t\t(Perfect)");
	ImageView pet = null;
	VBox bottomHalf = new VBox();
	HBox allMechanics = new HBox();
	
	@Override
	public void update(Observable arg0, Object arg1) {
		int age = controller.getAge();
		int happyNum = controller.getHappiness();
		int healthNum = controller.getHealth();
		int weightNum = controller.getWeight();
		
		if(healthNum - Integer.parseInt(healthText.getText()) > 10) {
			//POPOX for getting sick
		}
		
		
		if(happyNum < 0) {
			happinessText.setText("0");
		}
		else {
			happinessText.setText(Integer.toString(happyNum));
		}
		if(healthNum < 0) {
			healthText.setText("0");
		}
		else {
			healthText.setText(Integer.toString(healthNum));
		}
		
		if(weightNum < 0) {
			weightText.setText("0");
		}
		else {
			weightText.setText(Integer.toString(weightNum));
		}
		
		if (weightNum<=100 && weightNum>0) {
			weight.setWidth(weightNum * 2);
		} else if (weightNum <= 10 || weightNum >= 120) {
			weight.setWidth(weightNum * 2);
			controller.petDies();
			allMechanics.setDisable(true);
		}
		
		if (weightNum<=60 && weightNum>=40) {
			weight.setFill(Color.GREEN);
		} else if ((weightNum<40 && weightNum>=20) || (weightNum>60 && weightNum<=80)) {
			weight.setFill(Color.YELLOW);
		} else {
			weight.setFill(Color.RED);
		}
				
		if (happyNum>=65) {
			happiness.setFill(Color.GREEN);
		} else if (happyNum>=20) {
			happiness.setFill(Color.YELLOW);
		} else {
			happiness.setFill(Color.RED);
		}		
		healthStatus.setText(controller.getHealthDescription());
		happinessStatus.setText(controller.getHappinessDescription());
		ageText.setText("Age: " + Integer.toString(age));
		weightStatus.setText("\t\t\t" + controller.getWeightDescription());
		if (happyNum<=100 && happyNum>0) {
			happiness.setWidth(happyNum * 2);
		} else if (happyNum<=0) {
			happiness.setWidth(happyNum * 2);
			controller.petDies();
			allMechanics.setDisable(true);
		}
		if(healthNum < 1) {
			health.setWidth(healthNum * 2);
			controller.petDies();
			allMechanics.setDisable(true);
		}
		else {
			health.setWidth(healthNum * 2);

		}
		if (healthNum > 60) {
			health.setFill(Color.GREEN);
			if(model.isSick()) {
				Image newPet = new Image(controller.getPet());
				pet.setImage(newPet);
			}
		} else if (healthNum > 20) {
			health.setFill(Color.YELLOW);
			if(model.isSick()) {
				Image newPet = new Image(controller.getPet());
				pet.setImage(newPet);
			}
		} else {
			health.setFill(Color.RED);
			Image newPet = new Image(controller.getSadPet());
			pet.setImage(newPet);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		model.addObserver(this);
		primaryStage.setScene(mainMenu(primaryStage));
		primaryStage.setTitle("Tamagotchi");
		primaryStage.show();
	}
	
	private Scene mainMenu(Stage primaryStage) throws FileNotFoundException {
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
				Scene newScene = tamagotchiSelection(primaryStage);
				primaryStage.setScene(newScene);
				primaryStage.show();
			} catch (Exception e1) {
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
	
	
	
	
	private void addBackground(BorderPane root) throws FileNotFoundException {
		ImageView image = new ImageView(new Image(new FileInputStream("./Pet Images/background.jpg")));
		root.getChildren().add(image);
	}
	
	private Scene runGame(Stage primaryStage) throws Exception {
		BorderPane newRoot = new BorderPane();
		addBackground(newRoot);
		VBox textContainer = new VBox();
		VBox imgContainer = new VBox();
		Button feedSnacks = new Button("Feed Snacks");
		Button feedMeal = new Button("Feed Meal");
		Button feedMedicine = new Button("Give Medicine");
		//Button pause = new Button("Pause");
		ImageView pause = new ImageView(new Image(new FileInputStream("./Pet Images/pauseButtonImg.png")));
		try {
			pet = new ImageView(new Image(new FileInputStream(petImage)));
		}
		catch (Exception e){
			Alert a = new Alert(AlertType.ERROR, "Pet Image not Found");
			a.showAndWait();
		}
		feedSnacks.setPrefWidth(150);
		feedMeal.setPrefWidth(150);
		feedMedicine.setPrefWidth(150);
		
		allMechanics.getChildren().addAll(feedSnacks,feedMeal,feedMedicine);
		allMechanics.setPadding(new Insets(0,5,30,210));
		allMechanics.setSpacing(10);
		
		imgContainer.getChildren().add(pet);
		imgContainer.setPadding(new Insets(0,5,15,315));
		
		textContainer.getChildren().add(ageText);
		ageText.setFont(Font.font("Impact", 35));
		textContainer.setPadding(new Insets(0,0,15,400));
		
		bottomHalf.getChildren().addAll(imgContainer,textContainer,allMechanics);
		pause.setCursor(Cursor.HAND);
		pause.setOnMouseClicked(e ->{
			//stops the timer from the threading
			//pops up alert box to either save game/resume game/ quit game
			ButtonType saveGame = new ButtonType("Save Game", 
					ButtonBar.ButtonData.OK_DONE);
			ButtonType resumeGame = new ButtonType("Resume Game", 
					ButtonBar.ButtonData.BACK_PREVIOUS);
			ButtonType quitGame = new ButtonType("Quit Game", 
					ButtonBar.ButtonData.CANCEL_CLOSE);
			
			Alert alert = new Alert(AlertType.INFORMATION,"", 
					resumeGame,saveGame,quitGame);
			
			alert.setTitle("Pause Menu");
			alert.setHeaderText("Pause Menu");
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == saveGame) {
				
			} else {
				
			}
		});
		
		feedSnacks.setOnMouseClicked(e ->{
			//increase the weight by alot and happiness but hunger alittle
			controller.feedPetSnacks();
		});
		
		feedMeal.setOnMouseClicked(e ->{
			//only increases weight by alittle and hunger alot
			controller.feedPetMeal();
		});

		feedMedicine.setOnMouseClicked(e ->{
			//increase hp
			controller.giveMedicine();
		});
		
		setProgressBars(newRoot);
		
		newRoot.setTop(pause);
		newRoot.setBottom(bottomHalf);
		Scene newScene = new Scene(newRoot, WIDTH,HEIGHT);
		return newScene;
	}

	private Scene tamagotchiSelection(Stage primaryStage) throws FileNotFoundException {
		String[] images = {"./Pet Images/transparent_panda.png", "./Pet Images/transparent_mouse.png", "./Pet Images/transparent_dog.png"};
		String[] sadImages = {"./Pet Images/sad_panda.png", "./Pet Images/sad_mouse.png", "./Pet Images/sad_dog.png"};
		String[] buttons = {"Panda", "Mouse", "Dog"};
		BorderPane newRoot = new BorderPane();
		addBackground(newRoot);
		
		HBox backgroundHBox = new HBox();
		VBox confirmVBox = new VBox();
		Button confirm = new Button("Confirm");
		confirm.setOnMouseClicked(e -> {
			Scene newScene;
			try {
				newScene = runGame(primaryStage);
				primaryStage.setScene(newScene);
				controller.start();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		for(int i = 0; i < images.length; i++) {
			VBox vbox = new VBox();
			String petStr = images[i];
			String sadPetStr = sadImages[i];
			Image pet = new Image(new FileInputStream(petStr));
			ImageView petView = new ImageView(pet);
			Button button = new Button(buttons[i]);
			button.setOnMouseClicked(e -> {
				petImage = petStr;
				if(confirm.isDisabled()) {
					confirm.setDisable(false);
				}
				controller.setPet(petStr, sadPetStr);
			});
			button.setAlignment(Pos.CENTER);
			vbox.getChildren().addAll(petView, button);
			vbox.setAlignment(Pos.CENTER);
			backgroundHBox.getChildren().add(vbox);
			
		}
		if(petImage.equals("")) {
			confirm.setDisable(true);
		}
		confirm.setPrefWidth(200);
		confirm.setPrefHeight(50);
		confirm.setAlignment(Pos.CENTER);
		confirmVBox.getChildren().add(confirm);
		confirmVBox.setAlignment(Pos.BOTTOM_RIGHT);
		
		newRoot.setCenter(backgroundHBox);
		newRoot.setBottom(confirmVBox);
		Scene newScene = new Scene(newRoot, WIDTH, HEIGHT);
		return newScene;
	}

	private void setProgressBars(BorderPane newRoot) {
		HBox progressBars = new HBox();
		StackPane healthPane = new StackPane();
		StackPane weightPane = new StackPane();
		StackPane happinessPane = new StackPane();
		VBox weightVBox = new VBox();
		VBox happinessVBox = new VBox();
		// Health Bar
		Rectangle healthBackground = new Rectangle(200.0, 50.0, Color.BLACK);
		healthText.setFont(Font.font("Impact", 35));
		Text healthLabel = new Text("Health");
		healthPane.getChildren().addAll(healthLabel, healthBackground, health, healthText);
		healthVBox.getChildren().addAll(healthLabel, healthPane,healthStatus);
		healthVBox.setPadding(new Insets(0, 0, 0, 0));
		healthVBox.setSpacing(10);
		weightVBox.setSpacing(10);
		happinessVBox.setSpacing(10);
		progressBars.getChildren().add(healthVBox);
		
		
		// Weight Bar
		Rectangle weightBackground = new Rectangle(200.0, 50.0, Color.BLACK);
		weightText.setFont(Font.font("Impact", 35));
		Text weightLabel = new Text("Weight");
		weightPane.getChildren().addAll(weightLabel, weightBackground, weight, weightText);
		weightVBox.getChildren().addAll(weightLabel, weightPane, weightStatus);
		weightVBox.setPadding(new Insets(0, 100, 100, 100));
		progressBars.getChildren().add(weightVBox);
		
		// Happiness Bar
		Rectangle happinessBackground = new Rectangle(200.0, 50.0, Color.BLACK);
		happinessText.setFont(Font.font("Impact", 35));
		Text happinessLabel = new Text("Happiness");
		happinessPane.getChildren().addAll(happinessLabel, happinessBackground, happiness, happinessText);
		happinessVBox.getChildren().addAll(happinessLabel, happinessPane,happinessStatus);
		happinessVBox.setPadding(new Insets(0, 100, 100, 0));
		progressBars.getChildren().add(happinessVBox);
		
		//
		happinessText.setFill(Color.LIGHTGRAY);
		weightText.setFill(Color.LIGHTGRAY);
		healthText.setFill(Color.LIGHTGRAY);
		
		progressBars.setPadding(new Insets(10.0, 10.0, 10.0, 50.0));
		newRoot.getChildren().add(progressBars);
	}
	
}