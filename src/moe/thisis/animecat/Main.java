package moe.thisis.animecat;
	
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import moe.thisis.animecat.model.Anime;
import moe.thisis.animecat.model.AnimeListWrapper;
import moe.thisis.animecat.view.EditController;
import moe.thisis.animecat.view.UIController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * Main.java
 * AnimeCat main class
 * @author	Quinlan McNellen
 * @date	2016/06/17
 */
public class Main extends Application {
	private ObservableList<Anime> animeData = FXCollections.observableArrayList();
	
	public Main() {
		
	}
	/**
	 * Observable list for Anime data
	 * @return
	 */
	public ObservableList<Anime> getAnimeData() {
		return animeData;
	}
	
	private Pane animeCatLayout;
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("AnimeCat"); //set application title
			this.primaryStage.setResizable(false);
			this.primaryStage.getIcons().add(new Image("file:resources/images/appIcon.png")); //load and set icon
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/animecat.fxml")); //load fxml
			animeCatLayout = (Pane) loader.load();
			Scene scene = new Scene(animeCatLayout,830,470); //create scene

			primaryStage.setScene(scene);
			primaryStage.show();
			
			UIController controller = loader.getController(); //load controller class
			controller.setMain(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		File file = getAnimeFilePath(); //get previous file path
		if (file != null) {
			loadAnimeDataFromFile(file); //load previous file if one exists
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	/**
	 * Show the dialog to add a new Anime to the list
	 * @param	anime	Anime to add
	 * @return
	 */
	public boolean showAnimeDialog(Anime anime) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/EditController.fxml")); //load fxml
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage(); //create new stage
			dialogStage.setTitle("Add Anime"); //set dialog title
			dialogStage.setResizable(false);
			dialogStage.getIcons().add(new Image("file:resources/images/appIcon.png")); //load and set icon
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			EditController controller = loader.getController(); //load controller class
			controller.setDialogStage(dialogStage);
			controller.setAnime(anime); //set anime to add
			
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
	/**
	 * Return previous file path from preferences if available
	 * @return
	 */
	public File getAnimeFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	/**
	 * Store last saved file path in preferences
	 * @param	file	File to store path for
	 */
	public void setAnimeFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
			primaryStage.setTitle("AnimeCat - " + file.getName()); //append current file to application title
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle("AnimeCat"); //set title back to normal
		}
	}
	/**
	 * Load Anime data from a file
	 * @param	file	File to load
	 */
	public void loadAnimeDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext
					.newInstance(AnimeListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			AnimeListWrapper wrapper = (AnimeListWrapper) um.unmarshal(file);
			
			animeData.clear(); //clear any existing data
			animeData.addAll(wrapper.getAnimeList()); //unwrap from XML and add all data from file
			
			setAnimeFilePath(file); //set current file path
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load file:\n" + file.getPath());
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("view/animecat.css").toExternalForm());
			dialogPane.getStyleClass().add("animecat");
			
			alert.showAndWait();
		}
	}
	/**
	 * Save Anime data to a file
	 * @param	file	File to save
	 */
	public void saveAnimeDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext
					.newInstance(AnimeListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			AnimeListWrapper wrapper = new AnimeListWrapper();
			wrapper.setAnimeList(animeData); //wrap data for XML storage
			
			m.marshal(wrapper, file);
			
			setAnimeFilePath(file); //set current file path
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save file:\n" + file.getPath());
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("view/animecat.css").toExternalForm());
			dialogPane.getStyleClass().add("animecat");
			
			alert.showAndWait();
		}
	}
}
