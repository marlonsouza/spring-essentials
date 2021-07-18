package ueh.marlon.springessentials.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;
import ueh.marlon.springessentials.domain.Anime;
import ueh.marlon.springessentials.requests.AnimePostRequestBody;
import ueh.marlon.springessentials.requests.AnimePutRequestBody;

@Log4j2
public class SpringClient {
	public static void main(String[] args) {
		ResponseEntity<Anime> entity = 
			new RestTemplate()
				.getForEntity("http://localhost:8080/animes/{id}", Anime.class,3);
		log.info(entity);
		
		Anime anime = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class,3);
		
		log.info(anime);
		
		Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
		
		log.info(Arrays.toString(animes));
		
		ResponseEntity<List<Anime>> exchange = new RestTemplate()
				.exchange("http://localhost:8080/animes/all", HttpMethod.GET, null,
					new ParameterizedTypeReference<>() {});
		
		log.info(exchange.getBody());
		
		AnimePostRequestBody animePostRequestBody = AnimePostRequestBody.builder().name("churato").build();
		
		Anime churatoSaved = new RestTemplate().postForObject("http://localhost:8080/animes/", animePostRequestBody,Anime.class);
		
		log.info("saved anime {}", churatoSaved);
		
		AnimePutRequestBody animePutRequestBody = AnimePutRequestBody.builder().id(churatoSaved.getId()).name("churatu").build();
		
		ResponseEntity<Void> churatoUpdated = new RestTemplate().exchange("http://localhost:8080/animes/{id}", 
								HttpMethod.PUT,
								new HttpEntity<>(animePutRequestBody),
								Void.class,
								animePutRequestBody.getId());
		
		log.info("saved anime {}", churatoUpdated);
		
		new RestTemplate().exchange("http://localhost:8080/animes/{id}", 
				HttpMethod.DELETE,
				null,
				Void.class,
				animePutRequestBody.getId());

		log.info("Deleted anime with id {}", animePutRequestBody.getId());
	}
}
