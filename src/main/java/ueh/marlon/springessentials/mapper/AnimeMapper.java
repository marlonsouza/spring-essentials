package ueh.marlon.springessentials.mapper;

import org.mapstruct.Mapper;

import ueh.marlon.springessentials.domain.Anime;
import ueh.marlon.springessentials.requests.AnimePostRequestBody;
import ueh.marlon.springessentials.requests.AnimePutRequestBody;

@Mapper(componentModel = "spring")
public interface AnimeMapper {
	
	Anime toAnime(AnimePostRequestBody animePostRequestBody);
	
	Anime toAnime(AnimePutRequestBody animePutRequestBody);
	
}
