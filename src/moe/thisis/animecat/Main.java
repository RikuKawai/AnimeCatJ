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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	private ObservableList<Anime> animeData = FXCollections.observableArrayList();
	
	public Main() {
		
	}
	
	public ObservableList<Anime> getAnimeData() {
		return animeData;
	}
	
	private Pane animeCatLayout;
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("AnimeCat");
			this.primaryStage.getIcons().add(new Image("resources/images/animecatB.ico"));
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/animecat.fxml"));
			animeCatLayout = (Pane) loader.load();
			Scene scene = new Scene(animeCatLayout,840,480);

			primaryStage.setScene(scene);
			primaryStage.show();
			
			UIController controller = loader.getController();
			controller.setMain(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		File file = getAnimeFilePath();
		if (file != null) {
			loadAnimeDataFromFile(file);
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public boolean showAnimeDialog(Anime anime) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/EditController.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Anime");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			EditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAnime(anime);
			
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
	
	public File getAnimeFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	public void setAnimeFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
			primaryStage.setTitle("AnimeCat - " + file.getName());
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle("AnimeCat");
		}
	}
	public void loadAnimeDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext
					.newInstance(AnimeListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			AnimeListWrapper wrapper = (AnimeListWrapper) um.unmarshal(file);
			
			animeData.clear();
			animeData.addAll(wrapper.getAnimeList());
			
			setAnimeFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load file:\n" + file.getPath());
			
			alert.showAndWait();
		}
	}
	public void saveAnimeDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext
					.newInstance(AnimeListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			AnimeListWrapper wrapper = new AnimeListWrapper();
			wrapper.setAnimeList(animeData);
			
			m.marshal(wrapper, file);
			
			setAnimeFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save file:\n" + file.getPath());
			
			alert.showAndWait();
		}
	}
}
