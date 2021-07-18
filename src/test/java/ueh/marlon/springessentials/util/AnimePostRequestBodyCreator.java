package ueh.marlon.springessentials.util;

import ueh.marlon.springessentials.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

	public static AnimePostRequestBody validPostAnime() {
		return AnimePostRequestBody.builder()
				.name("Bob esponja")
				.build();
	}
}
