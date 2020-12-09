import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
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
	Button feedMedicine = new Button("Give Medicine");
	Stage primaryStage = null;

	/**
	 * When the model updates from the threads running in the controller, update
	 * gets called to update all the gui animations, the color of the bars
	 * change, and so does all the text
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		int age = controller.getAge();
		int happyNum = controller.getHappiness();
		int healthNum = controller.getHealth();
		int weightNum = controller.getWeight();
		
		// changes the text for the happiness/health/weight
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
		
		//updates the bars
		if (weightNum<=100 && weightNum>0) {
			weight.setWidth(weightNum * 2);
		} else if (weightNum <= 10 || weightNum >= 120) {
			weight.setWidth(weightNum * 2);
			allMechanics.setDisable(true);
			controller.interrupt();
			if(!controller.isDead()) {
				controller.petDies();
				Platform.runLater(()->{
					gameOverMessage();
				});
			}	
		}
		
		//update bar colors
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
		//update text descriptions
		healthStatus.setText(controller.getHealthDescription());
		happinessStatus.setText(controller.getHappinessDescription());
		ageText.setText("Age: " + Integer.toString(age));
		weightStatus.setText("\t\t\t" + controller.getWeightDescription());
		if (happyNum<=100 && happyNum>0) {
			happiness.setWidth(happyNum * 2);
		} else if (happyNum<=0) {
			happiness.setWidth(happyNum * 2);
			allMechanics.setDisable(true);
			controller.interrupt();
			if(!controller.isDead()) {
				controller.petDies();
				Platform.runLater(()->{
					gameOverMessage();
				});
			}
		}
		//check if it should die
		if(healthNum < 1) {
			allMechanics.setDisable(true);
			controller.interrupt();
			if(!controller.isDead()) {
				controller.petDies();
				Platform.runLater(()->{
					gameOverMessage();
				});
			}
		}
		else {
			health.setWidth(healthNum * 2);
		}
		//set color of the bars
		if (healthNum > 60) {
			health.setFill(Color.GREEN);
			feedMedicine.setDisable(true);
			
			if(model.isSick()) {
				Image newPet = new Image(controller.getPet());
				pet.setImage(newPet);
			}
		} else if (healthNum > 40) {
			feedMedicine.setDisable(false);
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
		healthStatus.setText(controller.getHealthDescription());
		happinessStatus.setText(controller.getHappinessDescription());
		ageText.setText("Age: " + Integer.toString(age));
		weightStatus.setText("\t\t\t" + controller.getWeightDescription());
		
	}

	@Override
	/**
	 * simply starts the game given the primary stage
	 */
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		model.addObserver(this);
		this.primaryStage.setScene(mainMenu());
		this.primaryStage.setTitle("Tamagotchi");
		this.primaryStage.show();
	}
	
	/**
	 * creates the main menu scene by calling the smaller parts that add up
	 * 
	 * @return the main menu scene
	 * @throws FileNotFoundException
	 */
	private Scene mainMenu() throws FileNotFoundException {
		addBackground(root);
		addMenu();
		return new Scene(root, WIDTH, HEIGHT);
	}
	
	private VBox menuBox = new VBox(-5);
	
	/** 
	 * creates the main menu scene with all the formatting/buttons/font/etc.
	 * 
	 * @throws FileNotFoundException
	 */
	private void addMenu() throws FileNotFoundException {
		menuBox.setSpacing(10);
		menuBox.setPadding(new Insets(0,0,0,20));
		menuBox.setAlignment(Pos.CENTER_LEFT);
		Button newGame = new Button("New Game");
		newGame.setPrefWidth(150);
		newGame.setFont(Font.font(15));
		
		//upon clicking new game, it creates the scene for the selection
		newGame.setOnMouseClicked(e -> {
			try {
				Scene newScene = tamagotchiSelection();
				primaryStage.setScene(newScene);
				primaryStage.show();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Button loadGame = new Button("Load Game");
		loadGame.setPrefWidth(150);
		loadGame.setFont(Font.font(15));
		
		//if the game has a saved file, it can load up the game
		loadGame.setOnMouseClicked(e ->{
			try {
				controller.load();
				petImage = controller.getPet();
				this.update(null, null);
				Scene newScene = runGame();
				primaryStage.setScene(newScene);
				primaryStage.show();
				controller.start();
			} catch (FileNotFoundException e1) {
				Alert noFile = new Alert(AlertType.ERROR,"No Saved Game, Start New Game");
				noFile.showAndWait();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Button quit = new Button("Quit");
		quit.setPrefWidth(150);
		quit.setFont(Font.font(15));
		
		quit.setOnMouseClicked(e ->{
			primaryStage.close();
		});
		
		
		menuBox.getChildren().addAll(newGame,loadGame,quit);
		root.setLeft(menuBox);
		ImageView logo = new ImageView(new Image(new FileInputStream("./Pet Images/tamagotchiLogo.png")));
		root.setCenter(logo);
	}
	
	
	
	/**
	 * Adds the background picture to our scenes
	 * @param root the primary scene that we add the background photo to
	 * @throws FileNotFoundException
	 */
	private void addBackground(BorderPane root) throws FileNotFoundException {
		ImageView image = new ImageView(new Image(new FileInputStream("./Pet Images/background.jpg")));
		root.getChildren().add(image);
	}
	
	/**
	 * Creates the main scene for the game with all the buttons and pictures
	 * also contains all the lambda functions for the buttons
	 * @return the scene to be displayed
	 * @throws Exception
	 */
	private Scene runGame() throws Exception {
		BorderPane newRoot = new BorderPane();
		addBackground(newRoot);
		VBox textContainer = new VBox();
		VBox imgContainer = new VBox();
		Button feedSnacks = new Button("Feed Snacks");
		Button feedMeal = new Button("Feed Meal");
		feedMedicine.setDisable(true);
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
		
		//pauses the game and has all the functionalities
		pause.setOnMouseClicked(e ->{
			//stops the timer from the threading
			//pops up alert box to either save game/resume game/ quit game
			controller.pause();
			primaryStage.getScene().getRoot().setOpacity(0.4);
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
				try {
					controller.save();
					controller.resumeGame();
					primaryStage.getScene().getRoot().setOpacity(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if(result.get() == quitGame){	
				primaryStage.close();
			}
			else if(result.get() == resumeGame) {
				controller.resumeGame();
				primaryStage.getScene().getRoot().setOpacity(1);
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

	/**
	 * Creates the tamagotchi character selection scene
	 * @return the scene to be displayed
	 * @throws FileNotFoundException
	 */
	private Scene tamagotchiSelection() throws FileNotFoundException {
		String[] images = {"./Pet Images/transparent_panda.png", "./Pet Images/transparent_mouse.png", "./Pet Images/transparent_dog.png"};
		String[] sadImages = {"./Pet Images/sad_panda.png", "./Pet Images/sad_mouse.png", "./Pet Images/sad_dog.png"};
		String[] buttons = {"Panda", "Mouse", "Dog"};
		BorderPane newRoot = new BorderPane();
		addBackground(newRoot);
		
		HBox backgroundHBox = new HBox();
		VBox confirmVBox = new VBox();
		Button confirm = new Button("Confirm");
		//sends which pet to be displayed on the main game and also sets up the
		//scene for the game
		confirm.setOnMouseClicked(e -> {
			Scene newScene;
			try {
				newScene = runGame();
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
	
	/**
	 * this is called by the main menu function so that it has all the
	 * progress bars at the top
	 * @param newRoot is the scene with all the progress bars
	 */
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
		
		happinessText.setFill(Color.LIGHTGRAY);
		weightText.setFill(Color.LIGHTGRAY);
		healthText.setFill(Color.LIGHTGRAY);
		
		progressBars.setPadding(new Insets(10.0, 10.0, 10.0, 50.0));
		newRoot.getChildren().add(progressBars);
	}
	
	/**
	 * Creates the overlay for when the game is over, it adds the opacity, pops
	 * up an alert box, and waits until the user clicks quit to exit the game
	 */
	private void gameOverMessage() {
		primaryStage.getScene().getRoot().setOpacity(0.4);
		ButtonType quitGame = new ButtonType("Quit Game", 
				ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert endGame = new Alert(AlertType.INFORMATION,"Game Over",quitGame);
		endGame.setTitle("Game Over");
		endGame.setHeaderText("Game Over");
		Optional<ButtonType> result = endGame.showAndWait();
		if(result.get() == quitGame){
			endGame.close();
			primaryStage.close();
		}
	}
}