package moe.thisis.animecat.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import moe.thisis.animecat.model.Anime;

public class EditController {
	
	@FXML
	private TextField searchQuery;
	
	private Stage dialogStage;
	private Anime anime;
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {
		
	}
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setAnime(Anime anime) {
		this.anime = anime;
	}
	public boolean isOkClicked() {
		return okClicked;
	}
	
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			//backend database code goes here
			
			okClicked = true;
			dialogStage.close();
		}
	}
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	private boolean isInputValid() {
		String errorMessage = "";
		
		if (searchQuery.getText() == null || searchQuery.getText().length() == 0) {
			errorMessage += "No search query entered!";
		}
		
		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Entry");
			alert.setHeaderText("Please correct input field");
			alert.setContentText(errorMessage);
			
			alert.showAndWait();
			
			return false;
		}
	}
}
