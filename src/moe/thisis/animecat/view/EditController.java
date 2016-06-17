package moe.thisis.animecat.view;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.*;
//import javax.xml.*;

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
	private void handleOk() throws SAXException, IOException {
		if (isInputValid()) {
			//back end database code goes here
			String[] animeData = getData(searchQuery.getText());
			
			anime.setAnimeID(animeData[0]);
			anime.setAnimeTitle(animeData[1]);
			anime.setMetaEpisodes(animeData[2]);
			anime.setMetaRating(animeData[3]);
			anime.setMetaStatus(animeData[4]);
			anime.setMetaYear(animeData[5]);
			anime.setImageURL(animeData[6]);
			
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
	public static String[] getData(String query) throws SAXException, IOException {
		Authenticator.setDefault(new Authenticator() {
			 @Override
			        protected PasswordAuthentication getPasswordAuthentication() {
			         return new PasswordAuthentication(
			   "RikuKawai", "AnimeCatDevelopment".toCharArray());
			        }
			});
		URL url = new URL("http://myanimelist.net/api/anime/search.xml?q=" + formatQuery(query)); //format and append search query
		DOMParser parser = new DOMParser();
		parser.parse(new InputSource(url.openStream())); //send api request and parse response
		Document doc = parser.getDocument();
		
		NodeList root = doc.getChildNodes();
		
		Node anime = getNode("anime", root);
		Node entry = getNode("entry", anime.getChildNodes());
		
		NodeList nodes = entry.getChildNodes();
		String[] animeData = new String[7]; //anime data is stored in a string array so it can be returned
		animeData[0] = getNodeValue("id", nodes); //animeID
		
		String englishTitle = getNodeValue("english", nodes);
		String jpTitle = getNodeValue("title", nodes);
		
		//some database entries have a blank english title field
		if (englishTitle.length() > 0) { //check if the english title is present
			animeData[1] =  englishTitle;
		} else {
			animeData[1] = jpTitle; //use the title field if it isn't
		}
		animeData[2] = getNodeValue("episodes", nodes); //metaEpisodes
		animeData[3] = getNodeValue("score", nodes); //metaRating
		animeData[4] = getNodeValue("status", nodes); //metaStatus
		animeData[5] = getNodeValue("start_date", nodes); //metaYear
		animeData[6] = getNodeValue("image", nodes); //imageURL
			
		return animeData;
	}
	public static String formatQuery(String query) {
		String formattedQuery = query.replaceAll(" ", "+");
		return formattedQuery;
	}
	protected static Node getNode(String tagName, NodeList nodes) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            return node;
	        }
	    }
	 
	    return null;
	}
	 
	protected static String getNodeValue( Node node ) {
	    NodeList childNodes = node.getChildNodes();
	    for (int x = 0; x < childNodes.getLength(); x++ ) {
	        Node data = childNodes.item(x);
	        if ( data.getNodeType() == Node.TEXT_NODE )
	            return data.getNodeValue();
	    }
	    return "";
	}
	 
	protected static String getNodeValue(String tagName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NodeList childNodes = node.getChildNodes();
	            for (int y = 0; y < childNodes.getLength(); y++ ) {
	                Node data = childNodes.item(y);
	                if ( data.getNodeType() == Node.TEXT_NODE )
	                    return data.getNodeValue();
	            }
	        }
	    }
	    return "";
	}
	 
	protected static String getNodeAttr(String attrName, Node node ) {
	    NamedNodeMap attrs = node.getAttributes();
	    for (int y = 0; y < attrs.getLength(); y++ ) {
	        Node attr = attrs.item(y);
	        if (attr.getNodeName().equalsIgnoreCase(attrName)) {
	            return attr.getNodeValue();
	        }
	    }
	    return "";
	}
	 
	protected static String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NodeList childNodes = node.getChildNodes();
	            for (int y = 0; y < childNodes.getLength(); y++ ) {
	                Node data = childNodes.item(y);
	                if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
	                    if ( data.getNodeName().equalsIgnoreCase(attrName) )
	                        return data.getNodeValue();
	                }
	            }
	        }
	    }
	 
	    return "";
	}
}
