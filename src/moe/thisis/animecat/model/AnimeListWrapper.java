package moe.thisis.animecat.model;

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "animelist")
public class AnimeListWrapper {
	private List<Anime> animelist;
	
	@XmlElement(name = "anime")
	public List<Anime> getAnimeList() {
		return animelist;
	}
	public void setAnimeList(List<Anime> animelist) {
		this.animelist = animelist;
	}
}
