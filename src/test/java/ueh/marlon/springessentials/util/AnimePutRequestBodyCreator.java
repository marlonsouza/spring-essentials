package ueh.marlon.springessentials.util;

import ueh.marlon.springessentials.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

	public static AnimePutRequestBody validAnimePut() {
		return AnimePutRequestBody.builder()
				.id(1L)
				.name("Naruto")
				.build();
	}
	
}
