package ueh.marlon.springessentials.util;

import ueh.marlon.springessentials.domain.Anime;

public class AnimeCreator {

	public static Anime toBeSaved() {
		return Anime.builder()
				.name("DBZ")
				.build();
	}
	
	public static Anime validAnimeWithId() {
		return Anime.builder()
				.name("Bob esponja")
				.id(1L)
				.build();
	}
	
	public static Anime updatedAnime() {
		return Anime.builder()
				.id(1L)
				.name("Bob esponja e Lula Molusco")
				.build();
	}
}
