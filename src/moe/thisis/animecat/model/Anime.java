package moe.thisis.animecat.model;

import javafx.beans.property.*;

public class Anime {
	private final StringProperty animeTitle;
	private final StringProperty animeID;
	private final IntegerProperty metaEpisodes;
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
		this.metaEpisodes = new SimpleIntegerProperty(24);
		this.metaYear = new SimpleStringProperty("2013-10-04");
		this.metaStatus = new SimpleStringProperty("Finished Airing");
		this.metaRating = new SimpleStringProperty("8.24");
		this.imageURL = new SimpleStringProperty("");
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
	public int getMetaEpisodes() {
		return metaEpisodes.get();
	}
	public void setMetaEpisodes(int metaEpisodes) {
		this.metaEpisodes.set(metaEpisodes);
	}
	public IntegerProperty metaEpisodesProperty() {
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
	public void setMetaRatin(String metaRating) {
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
}
