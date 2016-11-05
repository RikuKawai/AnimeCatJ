package moe.thisis.animecat.view;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import moe.thisis.animecat.Main;

import moe.thisis.animecat.model.Anime;

/**
 * UIController.java
 * Controller class for main GUI
 * @author	Quinlan McNellen
 * @date	2016/11/05
 */
public class UIController {
	@FXML
	private TableView<Anime> animeTable;
	@FXML
	private TableColumn<Anime, String> animeTitleColumn;
	@FXML
	private TableColumn<Anime, String> animeIDColumn;
	@FXML
	private ImageView metaCover;
	@FXML
	private Label animeTitleLabel;
	@FXML
	private Label metaEpisodesLabel;
	@FXML
	private Label metaYearLabel;
	@FXML
	private Label metaStatusLabel;
	@FXML
	private Label metaRatingLabel;
	@FXML
	private Button controlAdd;
	@FXML
	private Button controlSave;
	@FXML
	private Button controlLoad;
	@FXML
	private Button controlRemove;
	@FXML
	private Button controlSaveAs;
	@FXML
	private Button controlNew;
	@FXML
	private Button controlAbout;
	
	/**
	 * Show about dialog when the about button is clicked
	 */
	@FXML
	private void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AnimeCat");
		alert.setHeaderText("About");
		alert.setContentText("Author: Quinlan McNellen\nWebsite: http://thisis.moe");
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
		   getClass().getResource("animecat.css").toExternalForm());
		dialogPane.getStyleClass().add("animecat");
		
		alert.showAndWait();
	}
	/**
	 * Show edit dialog when the add button is clicked
	 */
	@FXML
	public void entryAdd() {
		Anime tempAnime = new Anime(); //temporary Anime entry
		boolean okClicked = mainApp.showAnimeDialog(tempAnime); //open dialog for temporary Anime
		if (okClicked) {
			mainApp.getAnimeData().add(tempAnime); //add temporary Anime to list
		}
	}
	/**
	 * Save list to file
	 */
	@FXML
	public void listSave() {
		File animeFile = mainApp.getAnimeFilePath(); //get path of current file
		if (animeFile != null) {
			mainApp.saveAnimeDataToFile(animeFile); //save list
		} else {
			listSaveAs(); //call save as if there is no open file
		}
	}
	/**
	 * Load list from file
	 */
	@FXML
	public void listLoad() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage()); //open file dialog
		
		if (file != null) {
			mainApp.loadAnimeDataFromFile(file); //load list
		}
	}
	/**
	 * Create new blank list
	 */
	@FXML
	public void listNew() {
		mainApp.getAnimeData().clear(); //clear data
		mainApp.setAnimeFilePath(null); //clear file path
	}
	/**
	 * Choose file name and save list
	 */
	@FXML
	public void listSaveAs() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage()); //save file dialog
		
		if (file != null) {
			if (!file.getPath().endsWith(".xml")) { //append .xml to file if the user did not
				file = new File(file.getPath());
			}
			mainApp.saveAnimeDataToFile(file); //save list
		}
	}
	/**
	 * Remove entry from list
	 */
	@FXML
	private void entryRemove() {
		int selectedIndex = animeTable.getSelectionModel().getSelectedIndex(); //get selected entry
		if (selectedIndex >= 0) { //check if anything is selected
			animeTable.getItems().remove(selectedIndex); //remove selected entry
		} else { //throw error if nothing is selected
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Anime Selected");
			alert.setContentText("Please select an anime from the list first.");
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("animecat.css").toExternalForm());
			dialogPane.getStyleClass().add("animecat");
			
			alert.showAndWait();
		}
		
	}
	
	private Main mainApp;
	
	public UIController() {
		
	}
	/**
	 * Initialize table with two columns
	 */
	@FXML
	private void initialize() {
		animeTitleColumn.setCellValueFactory(cellData -> cellData.getValue().animeTitleProperty());
		animeIDColumn.setCellValueFactory(cellData -> cellData.getValue().animeIDProperty());
		
		checkConnection(); //check Internet connection
		showLoginDialog(); //get credentials from user
		
		showAnimeDetails(null); //show null details
		
		animeTable.getSelectionModel().selectedItemProperty().addListener( //listen for selections and show details
				(observable, oldValue, newValue) -> showAnimeDetails(newValue));
	}
	/**
	 * Checks Internet connection by trying to connect to Google
	 */
	private void checkConnection() {
		try {
			URL url = new URL("http://google.ca");
			url.openStream(); //attempts to open connection
		} catch (Exception e) { //catches connection failure
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Connection");
			alert.setHeaderText("Connection Failed");
			alert.setContentText("Please check your Internet connection");
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("animecat.css").toExternalForm());
			dialogPane.getStyleClass().add("animecat");
			
			alert.showAndWait();
			System.exit(0); //closes the application, as it cannot function properly offline
		}
	}
	
	public void showLoginDialog() {
		//create custom dialog
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login");
		dialog.setHeaderText("myanimelist.net credentials");
		
		//set button types
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		
		//create username and password labels and fields
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		
		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);
		
		//enable/disable login button if a username is/isn't entered
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);
		
		//validate username
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});
		
		dialog.getDialogPane().setContent(grid);
		
		//request focus on the username field
		Platform.runLater(() -> username.requestFocus());
		
		//convert the result to a username-password-pair when login is clicked
		dialog.setResultConverter(dialogButton -> {
			if(dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});
		
		Optional<Pair<String, String>> result = dialog.showAndWait();
		
		result.ifPresent(usernamePassword -> {
			EditController.setUsername(usernamePassword.getKey());
			EditController.setPassword(usernamePassword.getValue());
		});
	}
	
	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
		
		animeTable.setItems(mainApp.getAnimeData());
	}
	/**
	 * Update the details section with details for an Anime
	 * @param	anime	Anime to show details for
	 */
	private void showAnimeDetails(Anime anime) {
		if (anime != null) {
			animeTitleLabel.setText(anime.getAnimeTitle());
			metaEpisodesLabel.setText(anime.getMetaEpisodes());
			metaYearLabel.setText(anime.getMetaYear());
			metaStatusLabel.setText(anime.getMetaStatus());
			metaRatingLabel.setText(anime.getMetaRating());
			metaCover.setImage(new Image(anime.getImageURL()));		
		} else {
			animeTitleLabel.setText("");
			metaEpisodesLabel.setText("");
			metaYearLabel.setText("");
			metaStatusLabel.setText("");
			metaRatingLabel.setText("");
			metaCover.setImage(new Image("http://i.imgur.com/PPyqZdP.png")); //filler image so that the imageview is never empty			
		}
	}
}
