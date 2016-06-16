package moe.thisis.animecat.model;

import javafx.beans.property.*;

public class Anime {
	private final StringProperty animeTitle;
	private final StringProperty animeID;
	private final StringProperty metaEpisodes;
	private final StringProperty metaYear;
	private final StringProperty metaStatus;
	private final StringProperty metaRating;
	private final StringProperty imageURL;
	
	public Anime() {
		this(null, null);
	}

	public Anime(String animeTitle, String animeID) {
		this.animeTitle = new SimpleStringProperty(animeTitle);
		this.animeID = new SimpleStringProperty(animeID);
		this.metaEpisodes = new SimpleStringProperty("24");
		this.metaYear = new SimpleStringProperty("2013-10-04");
		this.metaStatus = new SimpleStringProperty("Finished Airing");
		this.metaRating = new SimpleStringProperty("8.24");
		this.imageURL = new SimpleStringProperty("http://cdn.myanimelist.net/images/anime/9/9453.jpg");
	}
	public String getAnimeTitle() {
		return animeTitle.get();
	}
	public void setAnimeTitle(String metaTitle) {
		this.animeTitle.set(metaTitle);
	}
	public StringProperty animeTitleProperty() {
		return animeTitle;
	}
	public String getMetaEpisodes() {
		return metaEpisodes.get();
	}
	public void setMetaEpisodes(String metaEpisodes) {
		this.metaEpisodes.set(metaEpisodes);
	}
	public StringProperty metaEpisodesProperty() {
		return metaEpisodes;
	}
	public String getMetaYear() {
		return metaYear.get();
	}
	public void setMetaYear(String metaYear) {
		this.metaYear.set(metaYear);
	}
	public StringProperty metaYearProperty() {
		return metaYear;
	}
	public String getMetaStatus() {
		return metaStatus.get();
	}
	public void setMetaStatus(String metaStatus) {
		this.metaStatus.set(metaStatus);
	}
	public StringProperty metaStatusProperty() {
		return metaStatus;
	}
	public String getMetaRating() {
		return metaRating.get();
	}
	public void setMetaRating(String metaRating) {
		this.metaRating.set(metaRating);
	}
	public StringProperty metaRatingProperty() {
		return metaRating;
	}
	public String getAnimeID() {
		return animeID.get();
	}
	public void setAnimeID(String animeID) {
		this.animeID.set(animeID);
	}
	public StringProperty animeIDProperty() {
		return animeID;
	}
	public String getImageURL() {
		return imageURL.get();
	}
	public void setImageURL(String imageURL) {
		this.imageURL.set(imageURL);
	}
	public StringProperty imageURLProperty() {
		return imageURL;
	}
}
