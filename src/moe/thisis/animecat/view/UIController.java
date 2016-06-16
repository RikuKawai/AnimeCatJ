package moe.thisis.animecat.view;

import java.io.File;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;

import moe.thisis.animecat.Main;

import moe.thisis.animecat.model.Anime;

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
	
	@FXML
	private void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("AnimeCat");
		alert.setHeaderText("About");
		alert.setContentText("Author: Quinlan McNellen\nWebsite: http://thisis.moe");
		
		alert.showAndWait();
	}
	// Event Listener on Button[#controlAdd].onAction
	@FXML
	public void entryAdd() {
		Anime tempAnime = new Anime();
		boolean okClicked = mainApp.showAnimeDialog(tempAnime);
		if (okClicked) {
			mainApp.getAnimeData().add(tempAnime);
		}
	}
	// Event Listener on Button[#controlSave].onAction
	@FXML
	public void listSave() {
		File animeFile = mainApp.getAnimeFilePath();
		if (animeFile != null) {
			mainApp.saveAnimeDataToFile(animeFile);
		} else {
			listSaveAs();
		}
	}
	// Event Listener on Button[#controlQuit].onAction
	@FXML
	public void listLoad() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", ".xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			mainApp.loadAnimeDataFromFile(file);
		}
	}
	@FXML
	public void listNew() {
		mainApp.getAnimeData().clear();
		mainApp.setAnimeFilePath(null);
	}
	@FXML
	public void listSaveAs() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		
		if (file != null) {
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath());
			}
			mainApp.saveAnimeDataToFile(file);
		}
	}
	// Event Listener on Button[#controlRemove].onAction
	@FXML
	private void entryRemove() {
		int selectedIndex = animeTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			animeTable.getItems().remove(selectedIndex);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Anime Selected");
			alert.setContentText("Please select an anime from the list first.");
			
			alert.showAndWait();
		}
		
	}
	
	private Main mainApp;
	
	public UIController() {
		
	}
	
	@FXML
	private void initialize() {
		animeTitleColumn.setCellValueFactory(cellData -> cellData.getValue().animeTitleProperty());
		animeIDColumn.setCellValueFactory(cellData -> cellData.getValue().animeIDProperty());
		
		showAnimeDetails(null);
		
		animeTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showAnimeDetails(newValue));
	}
	
	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
		
		animeTable.setItems(mainApp.getAnimeData());
	}
	
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
			metaCover.setImage(new Image("http://i.imgur.com/jmpe90k.jpg"));
		}
	}
}
