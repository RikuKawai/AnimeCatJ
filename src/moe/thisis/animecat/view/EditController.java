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
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import moe.thisis.animecat.model.Anime;

/**
 * EditController.java
 * Dialog for adding a new entry
 * 
 * @author	Quinlan McNellen
 * @date	2016/11/05
 */
public class EditController {

	@FXML
	private TextField searchQuery;
	
	
	private Stage dialogStage;
	private Anime anime;
	private boolean okClicked = false;
	
	private static String username;
	private static String password;
	
	@FXML
	private void initialize() {
		
	}
	/**
	 * Set the stage of the dialog
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	/**
	 * Set the Anime to be edited
	 * @param anime
	 */
	public void setAnime(Anime anime) {
		this.anime = anime;
	}
	public boolean isOkClicked() {
		return okClicked;
	}
	/**
	 * Checks input and sends it to getData, then sets Anime data based on results
	 * @throws SAXException
	 * @throws IOException
	 */
	@FXML
	private void handleOk() throws SAXException, IOException {
		if (isInputValid()) {
			String[] animeData = getData(searchQuery.getText()); //get Anime data
			
			if (animeData != null) { //data is valid
				anime.setAnimeID(animeData[0]);
				anime.setAnimeTitle(animeData[1]);
				anime.setMetaEpisodes(animeData[2]);
				anime.setMetaRating(animeData[3]);
				anime.setMetaStatus(animeData[4]);
				anime.setMetaYear(animeData[5]);
				anime.setImageURL(animeData[6]);
				
				okClicked = true;
				dialogStage.close(); //close dialog
			}
		}
	}
	/**
	 * Close dialog if cancel is clicked
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	/**
	 * Checks if the input field contains valid information
	 * @return
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		
		if (searchQuery.getText() == null || searchQuery.getText().length() == 0) { //check if the input is blank
			errorMessage += "No search query entered!";
		}
		
		if (errorMessage.length() == 0) { //check if there is an error message to display
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Entry");
			alert.setHeaderText("Please correct input field");
			alert.setContentText(errorMessage);
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("animecat.css").toExternalForm());
			dialogPane.getStyleClass().add("animecat");
			
			alert.showAndWait(); //display error dialog
			
			return false;
		}
	}
	/**
	 * Connects to the MyAnimeList API and retrieves data for a search query
	 * @param	query	Anime title to search for
	 * @return
	 * @throws	SAXException
	 * @throws	IOException
	 */
	public String[] getData(String query) throws SAXException, IOException {
		Authenticator.setDefault(new Authenticator() {
			 @Override
			        protected PasswordAuthentication getPasswordAuthentication() {
			         return new PasswordAuthentication(
			   username, password.toCharArray());
			        }
			});
		URL url = new URL("https://myanimelist.net/api/anime/search.xml?q=" + formatQuery(query)); //format and append search query
		DOMParser parser = new DOMParser();
		boolean valid = true; //assume URL is valid
		try { //try to send API request and parse response
			parser.parse(new InputSource(url.openStream())); 
		} catch (Exception e) { //catch invalid URL exception or connection error
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Search Failed");
			alert.setHeaderText("Anime Searching Failed");
			alert.setContentText("Try a different query or check your connection.");
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(
			   getClass().getResource("animecat.css").toExternalForm());
			dialogPane.getStyleClass().add("animecat");
			
			alert.showAndWait(); //display error dialog
			valid = false; //set URL to invalid
		}
		if (valid) { //only try to parse response if there is a response to parse
			Document doc = parser.getDocument(); //load response into DOM
			
			NodeList root = doc.getChildNodes(); //retrieve DOM nodes
			
			Node anime = getNode("anime", root); //move into anime node
			Node entry = getNode("entry", anime.getChildNodes()); //get first search result
			
			NodeList nodes = entry.getChildNodes(); //load metadata
			String[] animeData = new String[7]; //anime data is stored in a string array so it can be returned
			animeData[0] = getNodeValue("id", nodes); //animeID
			
			String englishTitle = getNodeValue("english", nodes); //retrieve english title
			String jpTitle = getNodeValue("title", nodes); //retrieve original title
			String[] shortTitles = getNodeValue("synonyms", nodes).split(";"); String shortTitle = shortTitles[0]; //get first short title
			
			//some database entries have a blank english title field
			if (englishTitle.length() > 0) { //check if the english title is present
				animeData[1] =  englishTitle;
			} else {
				animeData[1] = jpTitle; //use the title field if it isn't
			}
			
			if (animeData[1].length() > 33) { //some Anime have excessively long titles that do not fit in the GUI
				if (shortTitle.length() > 0) {
					animeData[1] = shortTitle; //use short title instead if the title is longer than 33 characters
				} else {
					animeData[1] = jpTitle; //use original title if there is no short title available
				}
				
			}
			animeData[2] = getNodeValue("episodes", nodes); //metaEpisodes
			animeData[3] = getNodeValue("score", nodes); //metaRating
			animeData[4] = getNodeValue("status", nodes); //metaStatus
			animeData[5] = getNodeValue("start_date", nodes); //metaYear
			animeData[6] = getNodeValue("image", nodes); //imageURL
				
			return animeData;
		} else {
			return null; //return a null value if there were no results
		}
		
		
	}
	/**
	 * Replaces all spaces in a string with + characters so it can be used in an URL
	 * @param	query	String to be formatted
	 * @return
	 */
	public static String formatQuery(String query) {
		String formattedQuery = query.replaceAll(" ", "+");
		return formattedQuery;
	}
	/**
	 * Get a node from the DOM
	 * @param	tagName	Tag to locate
	 * @param	nodes	Nodes to look in
	 * @return
	 */
	protected static Node getNode(String tagName, NodeList nodes) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            return node;
	        }
	    }
	 
	    return null;
	}
	/**
	 * Return the value of a DOM node
	 * @param	node
	 * @return
	 */
	protected static String getNodeValue( Node node ) {
	    NodeList childNodes = node.getChildNodes();
	    for (int x = 0; x < childNodes.getLength(); x++ ) {
	        Node data = childNodes.item(x);
	        if ( data.getNodeType() == Node.TEXT_NODE )
	            return data.getNodeValue();
	    }
	    return "";
	}
	/**
	 * Search for and return the value of a DOM node 
	 * @param	tagName	Tag to locate
	 * @param	nodes	Nodes to look in
	 * @return
	 */
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
	/**
	 * Returns node attributes
	 * @param	attrName	Attribute name
	 * @param	node
	 * @return
	 */
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
	/**
	 * Searches for a node and returns node attributes
	 * @param	tagName	Tag to locate
	 * @param	attrName	Attribute name
	 * @param	nodes
	 * @return
	 */
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
	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public static void setUsername(String username) {
		EditController.username = username;
	}
	/**
	 * @param password the password to set
	 */
	public static void setPassword(String password) {
		EditController.password = password;
	}
}
