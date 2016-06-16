package moe.thisis.animecat;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import moe.thisis.animecat.model.Anime;
import moe.thisis.animecat.view.EditController;
import moe.thisis.animecat.view.UIController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	private ObservableList<Anime> animeData = FXCollections.observableArrayList();
	
	public Main() {
		animeData.add(new Anime("KILL la KILL", "18679"));
		animeData.add(new Anime("Toradora!", "4224"));
		animeData.add(new Anime("Death Note", "1535"));
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
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/animecat.fxml"));
			animeCatLayout = (Pane) loader.load();
			//Pane root = (Pane)FXMLLoader.load(getClass().getResource("view/animecat.fxml"));
			Scene scene = new Scene(animeCatLayout,840,480);
			scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			UIController controller = loader.getController();
			controller.setMain(this);
		} catch(Exception e) {
			e.printStackTrace();
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
}
